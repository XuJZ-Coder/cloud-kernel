package cn.zyszkj.cloud.kernel.core.context;

import cn.hutool.core.util.StrUtil;
import cn.zyszkj.cloud.kernel.core.utils.HttpContext;
import lombok.extern.slf4j.Slf4j;
import cn.zyszkj.cloud.kernel.model.contants.CloudConstants;
import cn.zyszkj.cloud.kernel.model.request.AbstractBaseRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前请求的请求号，没有请求号则生成空串
 * @author XuJZ
 */
@Slf4j
public class RequestNoContext {

    public static String getRequestNoByHttpHeader() {
        HttpServletRequest request = HttpContext.getRequest();

        if (request == null) {
            if (log.isDebugEnabled()) {
                log.info("获取请求号失败，当前不是http请求环境！");
            }
            return "";
        } else {
            String requestNo = request.getHeader(CloudConstants.REQUEST_NO_HEADER_NAME);
            if (StrUtil.isEmpty(requestNo)) {
                return "";
            } else {
                return requestNo;
            }
        }
    }

    /**
     * 通过请求参数获取requestNo，参数必须是AbstractBaseRequest的子类
     */
    public static String getRequestNoByRequestParam(Object[] params) {
        if (params != null && params.length > 0) {
            for (Object paramItem : params) {
                if (paramItem instanceof AbstractBaseRequest) {
                    AbstractBaseRequest abstractBaseRequest = (AbstractBaseRequest) paramItem;
                    return abstractBaseRequest.getRequestNo();
                }
            }
        }
        return "";
    }

}
