package cn.zyszkj.cloud.kernel.scanner.modular.factory;

import cn.hutool.core.util.StrUtil;
import cn.zyszkj.cloud.kernel.scanner.modular.model.ResourceParam;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限资源工厂(map缓存)
 * @author XuJZ
 */
public class ResourceFactory {

    /**
     * 以资源编码为标识的存放
     */
    private Map<String, ResourceParam> resourceParams = new ConcurrentHashMap<>();

    /**
     * 以模块名(控制器名),资源编码为标识的存放
     */
    private Map<String, Map<String, ResourceParam>> modularResourceParams = new ConcurrentHashMap<>();

    /**
     * 模块名称(控制器),对应的编码和中文名称的字典
     */
    private Map<String, String> resourceCodeNameDict = new HashMap<>();

    /**
     * 资源url为标识存放资源声明
     */
    private Map<String, ResourceParam> urlResourceParams = new ConcurrentHashMap<>();

    /**
     * 存储资源
     */
    public synchronized void registerDefinition(List<ResourceParam> apiResource) {
        if (apiResource != null || apiResource.size() > 0) {
            for (ResourceParam resourceParam : apiResource) {
                ResourceParam alreadyFlag = resourceParams.get(resourceParam.getCode());
                if (alreadyFlag != null) {
                    throw new RuntimeException("资源扫描过程中存在重复资源！\n已存在资源：" + alreadyFlag + "\n新资源为： " + resourceParam);
                }
                resourceParams.put(resourceParam.getCode(), resourceParam);
                urlResourceParams.put(resourceParam.getUrl(), resourceParam);

                //store modularResourceParams
                Map<String, ResourceParam> modularResources = modularResourceParams.get(StrUtil.toUnderlineCase(resourceParam.getModularCode()));
                if (modularResources == null) {
                    modularResources = new HashMap<>();
                    modularResources.put(resourceParam.getCode(), resourceParam);
                    modularResourceParams.put(StrUtil.toUnderlineCase(resourceParam.getModularCode()), modularResources);
                } else {
                    modularResources.put(resourceParam.getCode(), resourceParam);
                }

                //添加资源code-中文名称字典
                this.bindResourceName(resourceParam.getCode(), resourceParam.getName());
            }
        }
    }

    /**
     * 获取资源他通过资源编码
     */
    public ResourceParam getResource(String resourceCode) {
        return resourceParams.get(resourceCode);
    }

    /**
     * 获取当前应用所有资源
     */
    public List<ResourceParam> getAllResources() {
        Set<Map.Entry<String, ResourceParam>> entries = resourceParams.entrySet();
        ArrayList<ResourceParam> resourceParams = new ArrayList<>();
        for (Map.Entry<String, ResourceParam> entry : entries) {
            resourceParams.add(entry.getValue());
        }
        return resourceParams;
    }

    /**
     * 通过模块编码获取资源
     */
    public List<ResourceParam> getResourcesByModularCode(String code) {
        Map<String, ResourceParam> stringResourceDefinitionMap = modularResourceParams.get(code);
        ArrayList<ResourceParam> resourceDefinitions = new ArrayList<>();
        for (String key : stringResourceDefinitionMap.keySet()) {
            ResourceParam resourceDefinition = stringResourceDefinitionMap.get(key);
            resourceDefinitions.add(resourceDefinition);
        }
        return resourceDefinitions;
    }

    /**
     * 通过资源code获取资源中文名称
     */
    public String getResourceName(String code) {
        return resourceCodeNameDict.get(code);
    }

    /**
     * 添加资源的code和名称
     */
    public void bindResourceName(String code, String name) {
        resourceCodeNameDict.putIfAbsent(code, name);
    }

    /**
     * 获取所有模块资源
     */
    public Map<String, Map<String, ResourceParam>> getModularResources() {
        return this.modularResourceParams;
    }

    /**
     * 通过编码获取url
     */
    public String getResourceUrl(String code) {
        ResourceParam resourceDefinition = this.resourceParams.get(code);
        if (resourceDefinition == null) {
            return null;
        } else {
            return resourceDefinition.getUrl();
        }
    }

    /**
     * 通过url获取资源声明
     */
    public ResourceParam getResourceByUrl(String url) {
        return this.urlResourceParams.get(url);
    }

    /**
     * 获取所有职员
     */
    public Map<String, ResourceParam> getResourceParams() {
        return resourceParams;
    }
}
