package cn.zyszkj.cloud.kernel.core.exception.enums;

import cn.zyszkj.cloud.kernel.model.exception.enums.AbstractExceptionEnum;

/**
 * core模块的异常集合
 * @author XuJZ
 */
public enum CoreExceptionEnum  implements AbstractExceptionEnum {

    /**
     * 初始化数据库的异常
     */
    NO_CURRENT_USER(700, "当前没有登录的用户"),
    /**
     * 错误的请求
     */
    IO_ERROR(500, "网络开小差了，请稍后重试~"),
    SERVICE_ERROR(500, "网络开小差了，请稍后重试~"),
    REMOTE_SERVICE_NULL(404, "网络开小差了，请稍后重试~"),
    ASYNC_ERROR(5000, "数据在被别人修改，请稍后重试");

    CoreExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
