package cn.zyszkj.cloud.kernel.scanner.modular.api;

import cn.zyszkj.cloud.kernel.scanner.modular.annotation.ApiResource;
import cn.zyszkj.cloud.kernel.scanner.modular.annotation.ScanResource;
import cn.zyszkj.cloud.kernel.scanner.modular.model.RegisterResourceParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 资源接口
 * @author XuJZ
 */
@ApiResource(name = "资源", path = "/api/resources")
public interface RegisterResourceService {

    /**
     * 新增资源表
     *
     * @author XuJZ
     * @Date 2020-01-09
     */
    @ScanResource(name = "注册资源", path = "/action/registerResource", method = RequestMethod.POST ,requiredLogin = false, requiredPermission = false)
    void registerResource(@RequestBody RegisterResourceParam param);

}
