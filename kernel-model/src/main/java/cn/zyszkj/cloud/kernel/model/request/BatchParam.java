package cn.zyszkj.cloud.kernel.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XuJZ
 */
@Data
@ApiModel
public class BatchParam<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 批量操作方法(CREATE,UPDATE,DELETE)
     */
    @ApiModelProperty(value = "批量操作方法")
    private String method;

    /**
     * 批量目标
     */
    @ApiModelProperty(value = "操作对象")
    private T object;

}
