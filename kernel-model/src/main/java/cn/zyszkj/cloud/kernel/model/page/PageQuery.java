package cn.zyszkj.cloud.kernel.model.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询的请求参数封装
 * @author XuJZ
 */
@Data
@ApiModel
public class PageQuery {

    /**
     * 每页大小的param名称
     */
    @ApiModelProperty(value = "每页大小,默认20")
    private Integer pageSize;
    /**
     * 第几页的param名称
     */
    @ApiModelProperty(value = "第几页,默认1")
    private Integer pageNo;
    /**
     * 升序还是降序的param名称
     */
    @ApiModelProperty(value = "升序还是降序asc/desc")
    private String sort;
    /**
     * 根据那个字段排序的param名称
     */
    @ApiModelProperty(value = "根据哪个字段排序")
    private String orderBy;

    /**
     * 是否数据封装
     */
    @ApiModelProperty(value = "是否数据封装,默认false")
    private Boolean wrapper = false;

    public PageQuery() {
    }

    public PageQuery(Integer pageSize, Integer pageNo, String sort, String orderBy, Boolean wrapper) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.sort = sort;
        this.orderBy = orderBy;
        this.wrapper = wrapper;
    }
}
