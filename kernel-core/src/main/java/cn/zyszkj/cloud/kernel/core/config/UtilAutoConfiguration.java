package cn.zyszkj.cloud.kernel.core.config;

import cn.zyszkj.cloud.kernel.core.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认的工具类
 * @author XuJZ
 */
@Configuration
public class UtilAutoConfiguration {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}

