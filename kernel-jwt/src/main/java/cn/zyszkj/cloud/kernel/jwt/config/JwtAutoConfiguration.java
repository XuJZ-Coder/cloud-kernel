package cn.zyszkj.cloud.kernel.jwt.config;

import cn.zyszkj.cloud.kernel.jwt.config.properties.JwtProperties;
import cn.zyszkj.cloud.kernel.jwt.modular.utils.JwtTokenUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jwt自动配置
 * @author XuJZ
 */
@Configuration
@ConditionalOnProperty(prefix = JwtProperties.PREFIX, name = "enabled", havingValue = "true")
public class JwtAutoConfiguration {

    /**
     * jwt token的配置
     */
    @Bean
    @ConfigurationProperties(prefix = JwtProperties.PREFIX)
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    /**
     * jwt工具类
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil(JwtProperties jwtProperties) {
        return new JwtTokenUtil(jwtProperties.getSecret(), jwtProperties.getExpiration());
    }
}

