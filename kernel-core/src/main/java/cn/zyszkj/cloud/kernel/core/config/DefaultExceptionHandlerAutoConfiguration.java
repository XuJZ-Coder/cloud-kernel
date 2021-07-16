package cn.zyszkj.cloud.kernel.core.config;

import cn.zyszkj.cloud.kernel.core.exception.DefaultExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author XuJZ
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DefaultExceptionHandlerAutoConfiguration {

    /**
     * 默认的异常拦截器
     */
    @Bean
    public DefaultExceptionHandler defaultControllerExceptionHandler() {
        return new DefaultExceptionHandler();
    }

}
