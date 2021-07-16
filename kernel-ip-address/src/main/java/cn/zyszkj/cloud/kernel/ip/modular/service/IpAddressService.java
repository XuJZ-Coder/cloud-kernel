package cn.zyszkj.cloud.kernel.ip.modular.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author XuJZ
 */
@Slf4j
public class IpAddressService {

    /**
     * 默认是内存搜索算法，经测试，读取-关闭文件的速度 BTREE>BINARY>>MEMORY, 而使用不关闭资源的方式 MEMORY>BTREE>BINARY
     */
    private static final int ALGORITHM = DbSearcher.MEMORY_ALGORITYM;

    /**
     * 预加载DbSearcher
     */
    private DbSearcher searcher;

    public IpAddressService(DbSearcher searcher) {
        this.searcher = searcher;
    }

    /**
     * 获取ip城市
     *
     * @param ip 地址
     * @return ip城市 例如山西/太原
     */
    public String getCityInfo(String ip) {
        return getCityInfo(ip, ALGORITHM);
    }

    private String getCityInfo(String ip, int algorithm) {
        String cityInfo;
        try {
            cityInfo = getIpAddress(ip, algorithm);
        } catch (Exception e) {
            log.error("解析登录IP地址异常！", e);
            cityInfo = "未找到";
        }
        return cityInfo;
    }

    private String getIpAddress(String ip, int algorithm) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getSearcherMethod(algorithm);
        if (!Util.isIpAddress(ip)) {
            log.error("Error: 无效的IP地址！");
        }
        DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);
        String region = dataBlock.getRegion();
        if (StrUtil.isEmpty(region)) {
            return "未找到";
        }
        if (region.contains("内网IP")) {
            return "内网IP";
        }
        String[] split = region.split("\\|");
        if (split.length > 2) {
            return split[2] + "/" + split[3];
        }
        return region;
    }

    private Method getSearcherMethod(int algorithm) throws NoSuchMethodException {
        Class<DbSearcher> dbSearcherClass = DbSearcher.class;
        Method method = null;
        switch (algorithm) {
            case DbSearcher.BTREE_ALGORITHM:
                method = dbSearcherClass.getMethod("btreeSearch", String.class);
                break;
            case DbSearcher.BINARY_ALGORITHM:
                method = dbSearcherClass.getMethod("binarySearch", String.class);
                break;
            case DbSearcher.MEMORY_ALGORITYM:
                method = dbSearcherClass.getMethod("memorySearch", String.class);
                break;
        }
        return method;
    }

}
