package cn.zyszkj.cloud.kernel.scanner.modular.service;

import cn.hutool.core.util.StrUtil;
import cn.zyszkj.cloud.kernel.scanner.config.properties.ScannerProperties;
import cn.zyszkj.cloud.kernel.scanner.modular.factory.ResourceFactory;
import cn.zyszkj.cloud.kernel.scanner.modular.model.ResourceParam;
import cn.zyszkj.cloud.kernel.scanner.modular.model.ResourceTreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 资源整合服务
 * @author XuJZ
 */
public class ResourceCollectService {

    /**
     * 应用前缀标识编码的前缀标识
     */
    public static final String APP_CODE_PREFIX = "appType-";

    /**
     * 控制器(模块)编码的前缀标识
     */
    public static final String CONTROLLER_CODE_PREFIX = "conType-";

    /**
     * 资源编码的前缀标识
     */
    public static final String RESOURCE_CODE_PREFIX = "resType-";

    private ResourceFactory resourceFactory;

    private ScannerProperties scannerProperties;

    public ResourceCollectService(ResourceFactory resourceFactory, ScannerProperties scannerProperties) {
        this.resourceFactory = resourceFactory;
        this.scannerProperties = scannerProperties;
    }

    /**
     * 获取所有应用的资源列表

     */
    public List<ResourceParam> getAllAppResourceList(String code, String resourceName) {
        if (StrUtil.isBlank(code)) {
            List<ResourceParam> result = resourceFactory.getAllResources();
            return findResource(resourceName, result);
        } else {
            List<ResourceParam> result = null;
            if (code.startsWith(APP_CODE_PREFIX)) {
                result = resourceFactory.getAllResources();
            } else if (code.startsWith(CONTROLLER_CODE_PREFIX)) {
                result = resourceFactory.getResourcesByModularCode(code.replace(CONTROLLER_CODE_PREFIX, ""));
            } else if (code.startsWith(RESOURCE_CODE_PREFIX)) {
                ResourceParam resourceParams = resourceFactory.getResource(code.replace(CONTROLLER_CODE_PREFIX, ""));
                result = Arrays.asList(resourceParams);
            }
            return findResource(resourceName, result);
        }
    }

    /**
     * 获取资源树
     */
    public List<ResourceTreeNode> getResourceTree() {
        ArrayList<ResourceTreeNode> resourceTreeNodes = new ArrayList<>();

        //收集本应用的所有资源(按模块分类)
        ResourceTreeNode appResources = new ResourceTreeNode();
        appResources.setName(scannerProperties.getAppName());
        appResources.setCode(APP_CODE_PREFIX + scannerProperties.getAppCode());
        appResources.setChildren(new ArrayList<>());

        //构建本应用每个模块的资源
        Map<String, Map<String, ResourceParam>> modularResources = resourceFactory.getModularResources();
        for (String modularCode : modularResources.keySet()) {
            ArrayList<ResourceTreeNode> modularResourceTreeNode = new ArrayList<>();
            Map<String, ResourceParam> stringResourceParamMap = modularResources.get(modularCode);
            for (Map.Entry<String, ResourceParam> entry : stringResourceParamMap.entrySet()) {
                ResourceParam value = entry.getValue();
                modularResourceTreeNode.add(new ResourceTreeNode(value.getName(), RESOURCE_CODE_PREFIX + value.getCode()));
            }
            appResources.getChildren().add(new ResourceTreeNode( resourceFactory.getResourceName(modularCode), CONTROLLER_CODE_PREFIX + modularCode, modularResourceTreeNode));
        }

        resourceTreeNodes.add(appResources);
        return resourceTreeNodes;
    }

    private List<ResourceParam> findResource(String resourceName, List<ResourceParam> resourceParams) {
        if (StrUtil.isBlank(resourceName)) {
            return resourceParams;
        } else {
            for (ResourceParam resourceParam : resourceParams) {
                if (resourceParam.getName().equals(resourceName)) {
                    return Arrays.asList(resourceParam);
                }
            }
            return null;
        }
    }
}

