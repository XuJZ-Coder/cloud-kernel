package cn.zyszkj.cloud.kernel.core.converter;

import cn.zyszkj.cloud.kernel.model.request.RequestData;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * 请求转化处理器
 * @author XuJZ
 */
public class RequestDataTypeMethodProcessor extends RequestResponseBodyMethodProcessor {

    public RequestDataTypeMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RequestData.class);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return false;
    }
}
