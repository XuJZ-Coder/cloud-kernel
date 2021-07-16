package cn.zyszkj.cloud.kernel.core.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import cn.zyszkj.cloud.kernel.core.utils.PageResultUtil;
import cn.zyszkj.cloud.kernel.model.page.PageResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author XuJZ
 */
@Slf4j
public abstract class ResultWrapper<T> {
    /**
     * 是否封装
     */
    private Boolean wrapper = false;
    /**
     * 单体对象
     */
    private Object source;

    /**
     * 列表对象
     */
    private List<Object> sourceList;

    /**
     * 分页对象
     */
    private IPage<Object> sourceIPage;

    /**
     * 泛型
     */
    protected Class<T> entityClass = currentModelClass();

    public ResultWrapper() {
    }

    public ResultWrapper(Boolean wrapper) {
        this.wrapper = wrapper;
    }

    public ResultWrapper(Object source,Boolean wrapper) {
        this.wrapper = wrapper;
        if (source instanceof IPage){
            this.sourceIPage = (IPage) source;
        } else if (source instanceof List){
            this.sourceList = (List<Object>) source;
        } else {
            this.source = source;
        }
    }

    public ResultWrapper(Object source) {
        if (source instanceof IPage){
            this.sourceIPage = (IPage) source;
        } else if (source instanceof List){
            this.sourceList = (List<Object>) source;
        } else {
            this.source = source;
        }
    }

    /**
     * 获取泛型Class
     */
    protected Class<T> currentModelClass() {
        Class<?> clazz = getClass();
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(String.format("Warn: %s's superclass not ParameterizedType", clazz.getSimpleName()));
            return null;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof Class)) {
            log.warn(String.format("Warn: %s not set the actual class on superclass generic parameter",clazz.getSimpleName()));
            return null;
        }
        return (Class<T>) type;
    }


    protected abstract void wrapTheDto(T dto);

    public T wrap() {
        return wrap(source);
    }
    public T wrap(Object source) {
        if (source == null)
            return null;
        T bean = BeanUtil.toBean(source,entityClass);
        if (wrapper != null && wrapper)
            wrapTheDto(bean);
        return bean;
    }

    public List<T> wrapList() {
        if (sourceList == null)
            return null;
        return wrapList(sourceList);
    }

    public List<T> wrapList(List<Object> sourceList) {
        List<T> records = new ArrayList<>();
        for (Object source : sourceList) {
            T bean = wrap(source);
            records.add(bean);
        }
        return records;
    }

    public IPage<T> wrapPage() {
        return wrapPage(sourceIPage);
    }

    public IPage<T> wrapPage(IPage<Object> sourceIPage) {
        if (sourceIPage == null){
            return null;
        }
        if (sourceIPage.getRecords().isEmpty()){
            return new Page<T>(sourceIPage.getCurrent(), sourceIPage.getSize(), sourceIPage.getTotal());
        }
        List<T> records = wrapList(sourceIPage.getRecords());
        IPage<T> iPage = new Page<>(sourceIPage.getCurrent(), sourceIPage.getSize(), sourceIPage.getTotal());
        iPage.setRecords(records);
        return iPage;
    }

    public PageResult<T> wrapPageResult(){
        IPage<T> iPage = wrapPage();
        return PageResultUtil.toPageResult(iPage);
    }

    public ResultWrapper<T> setWrapper(Boolean wrapper) {
        this.wrapper = wrapper;
        return this;
    }
}
