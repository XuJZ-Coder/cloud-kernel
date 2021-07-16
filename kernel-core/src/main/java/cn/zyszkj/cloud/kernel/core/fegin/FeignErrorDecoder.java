package cn.zyszkj.cloud.kernel.core.fegin;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import cn.zyszkj.cloud.kernel.core.exception.enums.CoreExceptionEnum;
import cn.zyszkj.cloud.kernel.model.exception.ApiServiceException;
import cn.zyszkj.cloud.kernel.model.exception.ServiceException;
import cn.zyszkj.cloud.kernel.model.exception.enums.AbstractExceptionEnum;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * feign错误解码器
 * @author XuJZ
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        /**
         * 首先解析出来ResponseBody内容，如果body解析异常则直接抛出ServiceException
         */
        String responseBody;
        try {
            if (response == null || response.body() == null) {
                if (response != null && response.status() == 404) {
                    return new ServiceException(CoreExceptionEnum.REMOTE_SERVICE_NULL);
                } else {
                    return new ServiceException(CoreExceptionEnum.SERVICE_ERROR);
                }
            }
            responseBody = IoUtil.read(response.body().asInputStream(), "UTF-8");
        } catch (IOException e) {
            return new ServiceException(CoreExceptionEnum.IO_ERROR);
        }

        /**
         * 解析出来ResponseBody后，用json反序列化
         */
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if (log.isDebugEnabled()) {
            log.debug("FeignErrorDecoder收到错误响应结果：" + responseBody);
        }

        /**
         * 获取有效信息
         */
        String exceptionClazz = jsonObject.getString("exceptionClazz");
        Integer code = jsonObject.getInteger("code");
        String message = jsonObject.getString("message");

        /**
         * 首先判断是否有exceptionClazz字段，如果有，代表是服务抛出的业务接口异常ApiServiceException的子类，那么需要返回这个异常
         */
        if (StrUtil.isNotEmpty(exceptionClazz)) {
            ApiServiceException apiServiceExceptionByClassName =
                    this.getApiServiceExceptionByClassName(exceptionClazz, code, message);
            if (apiServiceExceptionByClassName != null) {
                return apiServiceExceptionByClassName;
            }
        }

        /**
         * 如果不是ApiServiceException的子类，则抛出ServiceException
         */
        if (message == null) {
            message = CoreExceptionEnum.SERVICE_ERROR.getMessage();
        }
        if (code == null) {

            //status为spring默认返回的数据
            Integer status = jsonObject.getInteger("status");

            if (status == null) {
                return new ServiceException(CoreExceptionEnum.SERVICE_ERROR.getCode(), message);
            } else {
                return new ServiceException(status, message);
            }
        } else {
            return new ServiceException(code, message);
        }
    }

    /**
     * 通过类名称（字符串）反射获取具体的异常类
     */
    private ApiServiceException getApiServiceExceptionByClassName(String className, Integer code, String message) {

        try {
            Class<?> clazz = Class.forName(className);
            Constructor constructor = clazz.getConstructor(AbstractExceptionEnum.class);
            return (ApiServiceException) constructor.newInstance(new AbstractExceptionEnum() {
                @Override
                public Integer getCode() {
                    return code;
                }

                @Override
                public String getMessage() {
                    return message;
                }
            });
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
