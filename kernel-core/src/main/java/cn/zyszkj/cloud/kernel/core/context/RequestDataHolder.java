package cn.zyszkj.cloud.kernel.core.context;

import cn.zyszkj.cloud.kernel.model.request.RequestData;

/**
 * 请求数据的临时容器
 * @author XuJZ
 */
public class RequestDataHolder {

    private static ThreadLocal<RequestData> holder = new ThreadLocal<>();

    public static void put(RequestData s) {
        if (holder.get() == null) {
            holder.set(s);
        }
    }

    public static RequestData get() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }
}

