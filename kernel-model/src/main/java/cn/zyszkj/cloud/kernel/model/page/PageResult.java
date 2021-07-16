package cn.zyszkj.cloud.kernel.model.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页结果集
 * @author XuJZ
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -4071521319254024213L;

    private Long page = 1L;// 要查找第几页
    private Long pageSize = 20L;// 每页显示多少条
    private Long totalPage = 0L;// 总页数
    private Long totalRows = 0L;// 总记录数
    private List<T> rows;// 结果集

    public PageResult() {

    }

    public PageResult(long page, long pageSize) {
        this(page, pageSize, 0);
    }

    public PageResult(long page, long pageSize, long totalRows) {
        if (page > 1) {
            this.page = page;
        }
        this.pageSize = pageSize;
        this.totalRows = totalRows;

        if (pageSize == 0) {
            this.totalPage = 0L;
        } else {
            long pages = totalRows / pageSize;
            if (totalRows % pageSize != 0) {
                pages++;
            }
            this.totalPage = pages;
        }
    }
}
