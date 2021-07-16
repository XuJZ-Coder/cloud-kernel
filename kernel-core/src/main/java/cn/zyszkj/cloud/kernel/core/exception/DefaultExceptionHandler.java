package cn.zyszkj.cloud.kernel.core.exception;

import cn.zyszkj.cloud.kernel.core.exception.enums.CoreExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import cn.zyszkj.cloud.kernel.model.contants.CloudConstants;
import cn.zyszkj.cloud.kernel.model.exception.ApiServiceException;
import cn.zyszkj.cloud.kernel.model.exception.RequestEmptyException;
import cn.zyszkj.cloud.kernel.model.exception.ServiceException;
import cn.zyszkj.cloud.kernel.model.response.BaseResult;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;


/**
 *
 * @author XuJZ
 */
@Slf4j
@RestControllerAdvice
@Order(CloudConstants.DEFAULT_EXCEPTION_HANDLER_SORT)
public class DefaultExceptionHandler {

    /**
     * 参数校验错误
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> handleError(MissingServletRequestParameterException e) {
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        log.warn(message);
        return BaseResult.error(400, message);
    }

    /**
     * 参数校验错误
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> handleError(MethodArgumentTypeMismatchException e) {
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        log.warn(message);
        return BaseResult.error(400, message);
    }

    /**
     * 参数校验错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> handleError(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        log.warn("MethodArgumentNotValidException:" + message);
        return BaseResult.error(400, message);
    }

    /**
     * 参数校验错误异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> handleError(BindException e) {
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        log.warn("Bind Exception:" + message);
        return BaseResult.error(400, message);
    }

    /**
     * 参数校验错误异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> handleError(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        log.warn("ConstraintViolationException:" + message);
        return BaseResult.error(400, message);
    }

    /**
     * 参数校验错误异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> handleError(HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException " + e.getMessage());
        String message = String.format("%s", e.getMessage());
        log.warn("HttpMessageNotReadableException:" + message);
        return BaseResult.error(400, message);
    }

    /**
     * 拦截请求为空的异常，返回状态码400
     */
    @ExceptionHandler(RequestEmptyException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> handleError(RequestEmptyException e) {
        return BaseResult.error(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截各个服务的具体异常，返回状态码500
     */
    @ExceptionHandler(ApiServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> apiService(ApiServiceException e) {
        log.warn("服务具体异常:", e);
        return BaseResult.error(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截业务异常，返回状态码500
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> serviceError(ServiceException e) {
        log.error("业务异常:", e);
        return BaseResult.error(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截未知的运行时异常，返回状态码500
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResult<String> serverError(Throwable e) {
        log.error("运行时异常:", e);
        return BaseResult.error(CoreExceptionEnum.SERVICE_ERROR.getCode(), CoreExceptionEnum.SERVICE_ERROR.getMessage());
    }

}
