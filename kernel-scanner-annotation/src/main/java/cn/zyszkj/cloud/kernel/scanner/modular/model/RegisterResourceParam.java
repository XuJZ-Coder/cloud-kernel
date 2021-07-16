package cn.zyszkj.cloud.kernel.scanner.modular.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 资源注册参数
 * @author XuJZ
 */

public class RegisterResourceParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * app编码
     */
    private String appCode;

    /**
     * 资源集合
     */
    private Map<String, ResourceParam> resourceParams;

    public RegisterResourceParam(String appCode, Map<String, ResourceParam> resourceParams) {
        this.appCode = appCode;
        this.resourceParams = resourceParams;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Map<String, ResourceParam> getResourceParams() {
        return resourceParams;
    }

    public void setResourceParams(Map<String, ResourceParam> resourceParams) {
        this.resourceParams = resourceParams;
    }
}
