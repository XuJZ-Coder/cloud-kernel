package cn.zyszkj.cloud.kernel.xxl.config;

import cn.zyszkj.cloud.kernel.xxl.config.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author XuJZ
 */
@Slf4j
@Configuration
public class XxlJobConfig {

    @Value("${spring.application.name}")
    private String appCode;

    @Bean
    @ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
    public XxlJobProperties xxlJobProperties() {
        XxlJobProperties properties = new XxlJobProperties();
        if (properties.getAppName() == null || "".equals(properties.getAppName().trim())) {
            properties.setAppName(appCode);
        }
        return properties;
    }

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties xxlJobProperties) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        String logPath = xxlJobProperties.getAppName() + "/" + XxlJobProperties.XXL_JOB_LOG_PATH;
        if (xxlJobProperties.getLogPath() != null && !"".equals(xxlJobProperties.getLogPath())){
            logPath = xxlJobProperties.getLogPath();
        }
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        log.info("xxl-job log path: " + logPath);
        return xxlJobSpringExecutor;
    }
}
