package cn.zyszkj.cloud.kernel.model.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常的封装
 *
 * @author XuJZ
 */
@Getter
@Setter
public class RequestEmptyException extends ServiceException {

    public RequestEmptyException() {
        super(400, "请求数据不完整或格式错误！");
    }

    public RequestEmptyException(String errorMessage) {
        super(400, errorMessage);
    }

    /**
     * 不拷贝栈信息，提高性能
     *
     * @author fengshuonan
     * @Date 2018/7/25 下午1:48
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
