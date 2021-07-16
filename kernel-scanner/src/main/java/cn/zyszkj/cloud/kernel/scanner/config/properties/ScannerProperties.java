package cn.zyszkj.cloud.kernel.scanner.config.properties;

import lombok.Data;

/**
 *
 * 扫描的常量
 * @author XuJZ
 */
@Data
public class ScannerProperties {
    /**
     * 资源扫描器的前缀
     */
    public static final String PREFIX = "trochilus.scanner";

    /**
     * 启用状态
     */
    private boolean enabled = true;

    /**
     * 被扫描应用的名称
     */
    private String appName;

    /**
     * 应用的编码,默认为 spring.application.name
     */
    private String appCode;

    /**
     * 链接符号
     */
    private String linkSymbol = "$";

    /**
     * 延迟上报时间，默认30s后上报
     */
    private long delay = 30*1000L;

    /**
     * 扫描包前缀
     */
    private String[] basePackages;


}
