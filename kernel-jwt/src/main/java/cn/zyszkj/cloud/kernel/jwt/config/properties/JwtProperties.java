package cn.zyszkj.cloud.kernel.jwt.config.properties;

import lombok.Data;

/**
 * jwt相关配置
 * @author XuJZ
 */
@Data
public class JwtProperties {

    /**
     * jwt配置前缀
     */
    public static final String PREFIX = "trochilus.jwt";
    /**
     * 启用状态
     */
    private boolean enabled = true;
    /**
     * jwt的秘钥，注意修改！
     */
    private String secret = "EvTYzdgIOM23ccsE";

    /**
     * jwt过期时间(单位:秒)(默认:1天)
     */
    private Long expiration = 82800L;

}
