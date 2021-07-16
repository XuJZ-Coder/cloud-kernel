package cn.zyszkj.cloud.kernel.model.exception.enums;

/**
 *
 * @author XuJZ
 * 异常规范
 */
public interface AbstractExceptionEnum {

    /**
     * 获取异常的状态码
     */
    Integer getCode();

    /**
     * 获取异常的提示信息
     */
    String getMessage();
}
