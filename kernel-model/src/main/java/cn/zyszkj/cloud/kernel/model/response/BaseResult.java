package cn.zyszkj.cloud.kernel.model.response;

import lombok.Data;
import cn.zyszkj.cloud.kernel.model.exception.enums.AbstractExceptionEnum;

import java.io.Serializable;

/**
 * @author XuJZ
 */
@Data
public class BaseResult<T> implements Serializable {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    public static final String DEFAULT_ERROR_MESSAGE = "网络开小差了，请稍后重试~";

    public static final Integer DEFAULT_SUCCESS_CODE = 200;

    public static final Integer DEFAULT_ERROR_CODE = 500;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 消息编号
     */
    private String msgId;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应对象
     */
    private T data;

    public BaseResult() {

    }

    public BaseResult(Boolean success, Integer code, String message, String msgId, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.msgId = msgId;
        this.data = data;
    }

    public static <T> BaseResult<T> success() {
        return new BaseResult<>(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE,null,null);
    }

    public static <T> BaseResult<T> success(T object) {
        return new BaseResult<>(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE,null,object);
    }

    public static <T> BaseResult<T> success(String msgId, T object) {
        return new BaseResult<>(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE,msgId,object);
    }

    public static <T> BaseResult<T> success(Integer code, String message, T object) {
        return new BaseResult<>(true, code, message,null, object);
    }

    public static <T> BaseResult<T> success(Integer code, String message,String msgId, T object) {
        return new BaseResult<>(true, code, message,msgId, object);
    }

    public static <T> BaseResult<T> error() {
        return new BaseResult<>(false, DEFAULT_ERROR_CODE,DEFAULT_ERROR_MESSAGE,null,null);
    }

    public static <T> BaseResult<T> error(String message) {
        return new BaseResult<>(false, DEFAULT_ERROR_CODE,message,null,null);
    }

    public static <T> BaseResult<T> error(Integer code, String message) {
        return new BaseResult<>(false,code, message,null,null);
    }

    public static <T> BaseResult<T> error(Integer code, String message ,String msgId) {
        return new BaseResult<>(false,code, message,msgId,null);
    }

    public static <T> BaseResult<T> error(AbstractExceptionEnum exceptionEnum) {
        return new BaseResult<>(false,exceptionEnum.getCode(), exceptionEnum.getMessage(),null,null);
    }

    public static <T> BaseResult<T> error(AbstractExceptionEnum exceptionEnum, T object) {
        return new BaseResult<>(false,exceptionEnum.getCode(), exceptionEnum.getMessage(),null,object);
    }

    public static <T> BaseResult<T> error(AbstractExceptionEnum exceptionEnum, String msgId, T object) {
        return new BaseResult<>(false,exceptionEnum.getCode(), exceptionEnum.getMessage(),msgId,object);
    }

    public static <T> BaseResult<T> error(Integer code, String message, T object) {
        return new BaseResult<>(false,code, message, null,object);
    }

    /**
     *
     * @param code 编码
     * @param message 消息
     * @param msgId 消息编号
     * @param object 返回结果
     * @param <T> 泛型
     * @return 返回对象
     */
    public static <T> BaseResult<T> error(Integer code, String message,String msgId, T object) {
        return new BaseResult<>(false,code, message, msgId,object);
    }
}
