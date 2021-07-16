package cn.zyszkj.cloud.kernel.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author XuJZ
 * 远程服务的参数的基类
 */
@Getter
@Setter
public abstract class AbstractBaseRequest implements Serializable {

    /**
     * 唯一请求号
     */
    private String requestNo;

    /**
     * 业务节点id
     */
    private String spanId;

    /**
     * 当前登录用户的token
     */
    private String token;

}
