package cn.zyszkj.cloud.kernel.core.config;

import cn.zyszkj.cloud.kernel.core.config.properties.AppProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author XuJZ
 */
@Configuration
@PropertySource("classpath:/default-config.properties")
public class PropertiesAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.application.name")
    public AppProperties appProperties() {
        return new AppProperties();
    }
}
