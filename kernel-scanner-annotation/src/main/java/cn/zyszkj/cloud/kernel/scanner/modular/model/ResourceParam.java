package cn.zyszkj.cloud.kernel.scanner.modular.model;

import java.io.Serializable;

/**
 * 资源
 * @author XuJZ
 */

public class ResourceParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 类名称
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 资源模块编码
     */
    private String modularCode;

    /**
     * 资源模块名称
     */
    private String modularName;

    /**
     * 资源编码
     */
    private String code;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源初始化的服务器ip地址
     */
    private String ipAddress;

    /**
     * 资源url
     */
    private String url;

    /**
     * http请求方法
     */
    private String httpMethod;

    /**
     * 是否需要登录(Y:是 N:否)
     */
    private String requiredLoginFlag;

    /**
     * 是否需要鉴权标识(Y:是 N:否)
     */
    private String requiredPermissionFlag;

    public boolean getRequiredLogin(){
        return requiredLoginFlag.equals("Y");
    }

    public boolean getRequiredPermission(){
        return requiredPermissionFlag.equals("Y");
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getModularCode() {
        return modularCode;
    }

    public void setModularCode(String modularCode) {
        this.modularCode = modularCode;
    }

    public String getModularName() {
        return modularName;
    }

    public void setModularName(String modularName) {
        this.modularName = modularName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequiredLoginFlag() {
        return requiredLoginFlag;
    }

    public void setRequiredLoginFlag(String requiredLoginFlag) {
        this.requiredLoginFlag = requiredLoginFlag;
    }

    public String getRequiredPermissionFlag() {
        return requiredPermissionFlag;
    }

    public void setRequiredPermissionFlag(String requiredPermissionFlag) {
        this.requiredPermissionFlag = requiredPermissionFlag;
    }
}
