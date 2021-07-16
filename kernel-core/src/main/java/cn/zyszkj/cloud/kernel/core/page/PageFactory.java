package cn.zyszkj.cloud.kernel.core.page;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.zyszkj.cloud.kernel.core.context.RequestDataHolder;
import cn.zyszkj.cloud.kernel.core.utils.HttpContext;
import cn.zyszkj.cloud.kernel.model.request.RequestData;
import cn.zyszkj.cloud.kernel.model.utils.ValidateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 默认分页参数构建
 * @author XuJZ
 */
public class PageFactory {
    /**
     * 排序，升序还是降序
     */
    private static final String ASC = "asc";

    /**
     * 每页大小的param名称
     */
    public static final String PAGE_SIZE_PARAM_NAME = "pageSize";

    /**
     * 第几页的param名称
     */
    public static final String PAGE_NO_PARAM_NAME = "pageNo";

    /**
     * 升序还是降序的param名称
     */
    public static final String SORT_PARAM_NAME = "sort";

    /**
     * 根据那个字段排序的param名称
     */
    public static final String ORDER_BY_PARAM_NAME = "orderBy";

    /**
     * 默认规则的分页
     */
    public static <T> Page<T> defaultPage() {

        int pageSize = 20;
        int pageNo = 1;

        HttpServletRequest request = HttpContext.getRequest();

        if (request == null) {
            return new Page<>(pageNo, pageSize);
        }

        //每页条数
        String pageSizeString = getFieldValue(request, PAGE_SIZE_PARAM_NAME);
        if (ValidateUtil.isNotEmpty(pageSizeString)) {
            pageSize = Integer.parseInt(pageSizeString);
        }

        //第几页
        String pageNoString = getFieldValue(request, PAGE_NO_PARAM_NAME);
        if (ValidateUtil.isNotEmpty(pageNoString)) {
            pageNo = Integer.parseInt(pageNoString);
        }

        //获取排序字段和排序类型(asc/desc)
        String sort = getFieldValue(request, SORT_PARAM_NAME);
        String orderByField = getFieldValue(request, ORDER_BY_PARAM_NAME);

        Page<T> page = new Page<>(pageNo, pageSize);
        if (StrUtil.isEmpty(orderByField)) {
            return page;
        }
        //驼峰转下划线
        orderByField = StringUtils.camelToUnderline(orderByField);
        if (StrUtil.isEmpty(sort)) {
            // 默认降序
            page.setOrders(OrderItem.descs(orderByField));
            return page;
        }
        if (ASC.equalsIgnoreCase(sort)) {
            page.setOrders(OrderItem.ascs(orderByField));
        } else {
            page.setOrders(OrderItem.descs(orderByField));
        }
        return page;
    }

    /**
     * 获取参数值，通过param或从requestBody中取
     */
    private static String getFieldValue(HttpServletRequest request, String type) {

        // 先从requestParam中获取值，如果没有就从requestData中获取
        String parameterValue = getRequestParameter(request, type);
        if (parameterValue == null) {
            return getRequestData(type);
        } else {
            return parameterValue;
        }
    }

    /**
     * 获取请求参数的值（从param中获取）
     */
    private static String getRequestParameter(HttpServletRequest request, String type) {
        if (PAGE_SIZE_PARAM_NAME.equals(type)) {
            Set<String> pageSizeFieldNames = PageFieldNamesContainer.getInstance().getPageSizeFieldNames();
            for (String fieldName : pageSizeFieldNames) {
                String pageSizeValue = request.getParameter(fieldName);
                if (StrUtil.isNotEmpty(pageSizeValue)) {
                    return pageSizeValue;
                }
            }
            return null;
        } else if (PAGE_NO_PARAM_NAME.equals(type)) {
            Set<String> pageNoFieldNames = PageFieldNamesContainer.getInstance().getPageNoFieldNames();
            for (String fieldName : pageNoFieldNames) {
                String pageNoValue = request.getParameter(fieldName);
                if (StrUtil.isNotEmpty(pageNoValue)) {
                    return pageNoValue;
                }
            }
            return null;
        } else if (SORT_PARAM_NAME.equals(type)) {
            Set<String> sortFieldNames = PageFieldNamesContainer.getInstance().getSortFieldNames();
            for (String fieldName : sortFieldNames) {
                String sortValue = request.getParameter(fieldName);
                if (StrUtil.isNotEmpty(sortValue)) {
                    return sortValue;
                }
            }
            return null;
        } else if (ORDER_BY_PARAM_NAME.equals(type)) {
            Set<String> orderByFieldNames = PageFieldNamesContainer.getInstance().getOrderByFieldNames();
            for (String fieldName : orderByFieldNames) {
                String orderByValue = request.getParameter(fieldName);
                if (StrUtil.isNotEmpty(orderByValue)) {
                    return orderByValue;
                }
            }
            return null;
        }
        return null;
    }

    /**
     * 获取请求参数的值（从requestData中获取）
     */
    private static String getRequestData(String type) {
        if (PAGE_SIZE_PARAM_NAME.equals(type)) {
            Set<String> pageSizeFieldNames = PageFieldNamesContainer.getInstance().getPageSizeFieldNames();
            return getValueFromRequestData(pageSizeFieldNames);
        } else if (PAGE_NO_PARAM_NAME.equals(type)) {
            Set<String> pageNoFieldNames = PageFieldNamesContainer.getInstance().getPageNoFieldNames();
            return getValueFromRequestData(pageNoFieldNames);
        } else if (SORT_PARAM_NAME.equals(type)) {
            Set<String> sortFieldNames = PageFieldNamesContainer.getInstance().getSortFieldNames();
            return getValueFromRequestData(sortFieldNames);
        } else if (ORDER_BY_PARAM_NAME.equals(type)) {
            Set<String> orderByFieldNames = PageFieldNamesContainer.getInstance().getOrderByFieldNames();
            return getValueFromRequestData(orderByFieldNames);
        }
        return null;
    }

    /**
     * 从requestData中获取指定字段集合名称中某一个值
     */
    private static String getValueFromRequestData(Set<String> fieldNames) {
        RequestData requestData = RequestDataHolder.get();
        if (requestData == null) {
            return null;
        }
        for (String fieldName : fieldNames) {
            Object fieldValue = requestData.get(fieldName);
            if (fieldValue != null) {
                return fieldValue.toString();
            }
        }
        return null;
    }
}
