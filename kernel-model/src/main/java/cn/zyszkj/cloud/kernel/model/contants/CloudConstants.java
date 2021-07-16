package cn.zyszkj.cloud.kernel.model.contants;

/**
 * @author XuJZ
 */
public interface CloudConstants {

    /**
     * 请求号header标识
     */
    String REQUEST_NO_HEADER_NAME = "Request-No";

    /**
     * 请求号header资源标识
     */
    String REQUEST_RESOURCE_HEADER_NAME = "Request-Resource";

    /**
     * header中的spanId，传递规则：request header中传递本服务的id
     */
    String SPAN_ID_HEADER_NAME = "Span-Id";


    /**
     * 默认的ExceptionHandler的aop顺序
     */
    int DEFAULT_EXCEPTION_HANDLER_SORT = 200;

    /**
     * 临时保存RequestData的aop
     */
    int REQUEST_DATA_AOP_SORT = 500;

    /**
     * 参数校验为空的aop
     */
    int PARAM_VALIDATE_AOP_SORT = 510;

    /**
     * 控制器调用链的aop
     */
    int CHAIN_ON_CONTROLLER_SORT = 600;

    /**
     * provider的调用链aop
     */
    int CHAIN_ON_PROVIDER_SORT = 610;

    /**
     * consumer的调用链aop
     */
    int CHAIN_ON_CONSUMMER_SORT = 620;
}
