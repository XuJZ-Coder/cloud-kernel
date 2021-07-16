package cn.zyszkj.cloud.kernel.scanner.modular.service;

import cn.hutool.core.util.StrUtil;
import cn.zyszkj.cloud.kernel.scanner.config.properties.ScannerProperties;
import cn.zyszkj.cloud.kernel.scanner.modular.factory.ResourceFactory;
import cn.zyszkj.cloud.kernel.scanner.modular.utils.AopTargetUtils;
import cn.zyszkj.cloud.kernel.scanner.modular.annotation.ApiResource;
import cn.zyszkj.cloud.kernel.scanner.modular.annotation.ScanResource;
import cn.zyszkj.cloud.kernel.scanner.modular.model.ResourceParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资源扫描器
 * @author XuJZ
 */
public class ResourceScanner implements BeanPostProcessor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ResourceFactory resourceFactory;

    private ScannerProperties scannerProperties;

    private String springApplicationName;

    public ResourceScanner(ResourceFactory resourceFactory, ScannerProperties scannerProperties, String springApplicationName) {
        this.resourceFactory = resourceFactory;
        this.scannerProperties = scannerProperties;
        this.springApplicationName = springApplicationName;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(scannerProperties.getBasePackages() == null || scannerProperties.getBasePackages().length == 0){
            return bean;
        }

        //如果controller是代理对象,则需要获取原始类的信息
        Object aopTarget = AopTargetUtils.getTarget(bean);

        if (aopTarget == null) {
            aopTarget = bean;
        }
        Class<?> clazz = aopTarget.getClass();
        //判断是不是控制器,不是控制器就略过
        boolean controllerFlag = getControllerFlag(clazz);
        if (!controllerFlag) {
            return bean;
        }
        String packageName = clazz.getPackage().getName();
        for (String basePackage : this.scannerProperties.getBasePackages()) {
            if (packageName.contains(basePackage)){

                //扫描控制器的所有带ApiResource注解的方法
                List<ResourceParam> apiResources = doScan(clazz);

                //将扫描到的注解转化为资源实体存储到缓存
                persistApiResources(apiResources);
                break;
            }
        }

        return bean;
    }


    /**
     * 判断一个类是否是控制器
     */
    private boolean getControllerFlag(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations) {
            if (RestController.class.equals(annotation.annotationType()) || Controller.class.equals(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 扫描整个类中包含的所有控制器
     */
    private List<ResourceParam> doScan(Class<?> clazz) {
        //绑定类的code-中文名称映射
        ApiResource classApiAnnotation = AnnotationUtils.findAnnotation(clazz,ApiResource.class);
        if (classApiAnnotation != null) {
            if (StrUtil.isEmpty(classApiAnnotation.code())) {
                String className = clazz.getSimpleName();
                int controllerIndex = className.indexOf("Controller");
                if (controllerIndex == -1) {
                    throw new IllegalArgumentException("controller class name is not illegal, it should end with Controller!");
                }
                String code = className.substring(0, controllerIndex);
                this.resourceFactory.bindResourceName(StrUtil.toUnderlineCase(code), classApiAnnotation.name());
            } else {
                this.resourceFactory.bindResourceName(StrUtil.toUnderlineCase(classApiAnnotation.code()), classApiAnnotation.name());
            }
        }

        ArrayList<ResourceParam> apiResources = new ArrayList<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        if (declaredMethods.length > 0) {
            for (Method declaredMethod : declaredMethods) {
                ApiResource apiResource = AnnotationUtils.findAnnotation(declaredMethod,ApiResource.class);
                ScanResource scanResource = AnnotationUtils.findAnnotation(declaredMethod,ScanResource.class);

                Annotation annotation = null;
                if (apiResource != null) {
                    annotation = apiResource;
                }

                if (scanResource != null) {
                    annotation = scanResource;
                }

                if (annotation != null) {
                    ResourceParam definition = createDefinition(clazz, declaredMethod, annotation);
                    apiResources.add(definition);
                    log.debug("扫描到资源: " + definition);
                }
            }
        }
        return apiResources;
    }

    /**
     * 存储扫描到的api资源
     */
    private void persistApiResources(List<ResourceParam> apiResources) {
        resourceFactory.registerDefinition(apiResources);
    }

    private ResourceParam createDefinition(Class<?> clazz, Method method, Annotation apiResource) {
        ResourceParam resourceParam = new ResourceParam();
        resourceParam.setClassName(clazz.getSimpleName());
        resourceParam.setMethodName(method.getName());

        String className = resourceParam.getClassName();
        int controllerIndex = className.indexOf("Controller");
        if (controllerIndex == -1) {
            throw new IllegalArgumentException("controller class name is not illegal, it should end with Controller!");
        }
        String modular = className.substring(0, controllerIndex);
        resourceParam.setModularCode(modular);

        //设置模块的中文名称
        ApiResource classApiAnnotation = AnnotationUtils.findAnnotation(clazz,ApiResource.class);
        assert classApiAnnotation != null;
        resourceParam.setModularName(classApiAnnotation.name());

        //如果控制器类上标识了appCode则应用标识上的appCode,如果控制器上没标识则用配置文件中的appCode
        if (StrUtil.isNotBlank(classApiAnnotation.appCode())) {
            resourceParam.setAppCode(classApiAnnotation.appCode());
        } else {
            resourceParam.setAppCode(scannerProperties.getAppCode());
        }

        //如果没有填写code则用"模块名称_方法名称"为默认的标识
        String code = invokeAnnotationMethod(apiResource, "code", String.class);
        if (StrUtil.isEmpty(code)) {
            resourceParam.setCode(resourceParam.getAppCode() + scannerProperties.getLinkSymbol() + StrUtil.toUnderlineCase(modular) + scannerProperties.getLinkSymbol() + StrUtil.toUnderlineCase(method.getName()));
        } else {
            resourceParam.setCode(resourceParam.getAppCode() + scannerProperties.getLinkSymbol() + StrUtil.toUnderlineCase(modular) + scannerProperties.getLinkSymbol() + StrUtil.toUnderlineCase(code));
        }

        //设置其他属性
        String name = invokeAnnotationMethod(apiResource, "name", String.class);
        String[] path = invokeAnnotationMethod(apiResource, "path", String[].class);
        RequestMethod[] requestMethods = invokeAnnotationMethod(apiResource, "method", RequestMethod[].class);
        Boolean requiredLogin = invokeAnnotationMethod(apiResource, "requiredLogin", Boolean.class);
        Boolean requiredPermission = invokeAnnotationMethod(apiResource, "requiredPermission", Boolean.class);

        resourceParam.setRequiredLoginFlag(requiredLogin ? "Y":"N");
        resourceParam.setRequiredPermissionFlag(requiredPermission ? "Y":"N");
        resourceParam.setName(name);
        if (path.length > 0){
            String[] split = path[0].split("/");
            String url = Arrays.stream(split)
                    .map(string -> {
                        if (string.startsWith("{")) {
                            return "{number}";
                        }
                        return string;
                    })
                    .reduce((a, b) -> a + "/" + b)
                    .orElse("");

            resourceParam.setUrl(getControllerClassRequestPath(clazz) + url);
        } else {
            resourceParam.setUrl(getControllerClassRequestPath(clazz));
        }
        StringBuilder methodNames = new StringBuilder();
        for (RequestMethod requestMethod : requestMethods) {
            methodNames.append(requestMethod.name()).append(",");
        }
        resourceParam.setHttpMethod(StrUtil.removeSuffix(methodNames.toString(), ","));

        return resourceParam;
    }

    /**
     * 获取控制器类上的RequestMapping注解的映射路径,用于拼接path
     * <p>
     * 2018年5月2日修改，控制器路径前加上spring.application.name
     */
    private String getControllerClassRequestPath(Class<?> clazz) {
        String result;

        ApiResource controllerRequestMapping = AnnotationUtils.findAnnotation(clazz,ApiResource.class);
        if (controllerRequestMapping == null) {
            result = "";
        } else {
            String[] paths = controllerRequestMapping.path();
            if (paths.length > 0) {
                result = paths[0];
            } else {
                result = "";
            }
        }

        //拼接spring.application.name
        result = "/" + springApplicationName + result;
        return result;
    }

    private <T> T invokeAnnotationMethod(Annotation apiResource, String methodName, Class<T> resultType) {
        try {
            Class<? extends Annotation> annotationType = apiResource.annotationType();
            Method method = annotationType.getMethod(methodName);
            return (T) method.invoke(apiResource);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("扫描api资源时出错!", e);
        }
        throw new RuntimeException("扫描api资源时出错!");
    }
}

