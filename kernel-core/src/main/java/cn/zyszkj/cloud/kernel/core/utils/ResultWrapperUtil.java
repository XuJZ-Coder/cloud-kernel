package cn.zyszkj.cloud.kernel.core.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.zyszkj.cloud.kernel.model.page.PageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询结果数组对象转换
 * @author XuJZ
 */
public class ResultWrapperUtil {

    /**
     * 转化列表内对象类型
     */
    public static <T> List<T> wrap(Class<T> clazz, List sourceList){
        if (sourceList == null){
            return null;
        }
        List<T> records = new ArrayList<>();
        for (Object source : sourceList) {
            T bean = BeanUtil.toBean(source,clazz);
            records.add(bean);
        }
        return records;
    }

    /**
     * 转化分页对象里列表内对象类型
     */
    public static <T> PageResult<T> wrap(Class<T> clazz, IPage source){
        if (source == null){
            return null;
        }
        IPage<T> iPage = new Page<T>(source.getCurrent(), source.getSize(), source.getTotal());
        if (source.getRecords().isEmpty()){
            iPage.setRecords(new ArrayList<>());
        } else {
            List<T> records = wrap(clazz,source.getRecords());
            iPage.setRecords(records);
        }
        return PageResultUtil.toPageResult(iPage);
    }

    /**
     * 转化分页对象里列表内对象类型
     */
    public static <T> PageResult<T> wrap(Class<T> clazz, PageResult source){
        if (source == null){
            return null;
        }
        IPage<T> iPage = new Page<T>(source.getPage(), source.getPageSize(), source.getTotalRows());
        if (source.getRows().isEmpty()){
            iPage.setRecords(new ArrayList<>());
        } else {
            List<T> records = wrap(clazz,source.getRows());
            iPage.setRecords(records);
        }
        return PageResultUtil.toPageResult(iPage);
    }
}
