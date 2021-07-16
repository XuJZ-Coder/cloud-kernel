package cn.zyszkj.cloud.kernel.model.request;

/**
 * @author XuJZ
 * 远程调用请求参数的holder
 */
public class RmiRequestHolder {

    private static final ThreadLocal<AbstractBaseRequest> spanIdContext = new ThreadLocal<>();

    public static void set(AbstractBaseRequest spanId) {
        spanIdContext.set(spanId);
    }

    public static AbstractBaseRequest get() {
        return spanIdContext.get();
    }

    public static void remove() {
        spanIdContext.remove();
    }
}
