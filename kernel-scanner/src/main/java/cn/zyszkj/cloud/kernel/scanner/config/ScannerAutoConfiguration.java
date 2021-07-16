package cn.zyszkj.cloud.kernel.scanner.config;

import cn.hutool.core.util.StrUtil;
import cn.zyszkj.cloud.kernel.scanner.config.properties.ScannerProperties;
import cn.zyszkj.cloud.kernel.scanner.modular.factory.ResourceFactory;
import cn.zyszkj.cloud.kernel.scanner.modular.listener.ResourceReportListener;
import cn.zyszkj.cloud.kernel.scanner.modular.service.ResourceCollectService;
import cn.zyszkj.cloud.kernel.scanner.modular.service.ResourceScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描器默认配置
 * @author XuJZ
 */
@Configuration
@ConditionalOnProperty(prefix = ScannerProperties.PREFIX, name = "enabled", havingValue = "true")
public class ScannerAutoConfiguration {

    @Value("${spring.application.name}")
    private String appCode;

    @Bean
    @ConfigurationProperties(prefix = ScannerProperties.PREFIX)
    public ScannerProperties scannerProperties() {
        ScannerProperties properties = new ScannerProperties();
        if (StrUtil.isEmpty(properties.getAppCode())) {
            properties.setAppCode(appCode);
        }
        return properties;
    }

    /**
     * 资源工厂
     */
    @Bean
    public ResourceFactory resourceFactory() {
        return new ResourceFactory();
    }

    /**
     * 资源收集服务
     */
    @Bean
    public ResourceCollectService resourceCollectService(ResourceFactory resourceFactory, ScannerProperties scannerProperties) {
        return new ResourceCollectService(resourceFactory, scannerProperties);
    }

    /**
     * 资源扫描器
     */
    @Bean
    public ResourceScanner resourceScanner(ResourceFactory resourceFactory, ScannerProperties scannerProperties) {
        return new ResourceScanner(resourceFactory, scannerProperties, scannerProperties.getAppCode());
    }

    /**
     * 资源扫描之后的资源汇报操作（向roses-auth服务）
     */
    @Bean
    public ResourceReportListener resourceReportListener(ScannerProperties scannerProperties) {
        ResourceReportListener resourceReportListener =  new ResourceReportListener();
        resourceReportListener.setScannerProperties(scannerProperties);
        return resourceReportListener;
    }

}
