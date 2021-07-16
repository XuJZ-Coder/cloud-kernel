package cn.zyszkj.cloud.kernel.core.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.zyszkj.cloud.kernel.model.page.PageResult;

/**
 * @author XuJZ
 */
public class PageResultUtil {

    public static <T> PageResult<T> toPageResult(IPage<T> iPage){
        PageResult<T> result = new PageResult(iPage.getCurrent(),iPage.getSize(),iPage.getTotal());
        result.setRows(iPage.getRecords());
        return result;
    }

    public static <T> IPage<T> toIPage(PageResult<T> pageResult){
        IPage<T> iPage = new Page<>(pageResult.getPage(),pageResult.getPageSize(),pageResult.getTotalRows());
        iPage.setRecords(pageResult.getRows());
        return iPage;
    }
}
