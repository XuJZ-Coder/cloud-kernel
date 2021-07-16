package cn.zyszkj.cloud.kernel.scanner.modular.listener;

import cn.zyszkj.cloud.kernel.model.exception.ServiceException;
import cn.zyszkj.cloud.kernel.scanner.config.properties.ScannerProperties;
import cn.zyszkj.cloud.kernel.scanner.modular.api.RegisterResourceService;
import cn.zyszkj.cloud.kernel.scanner.modular.factory.ResourceFactory;
import cn.zyszkj.cloud.kernel.scanner.modular.model.RegisterResourceParam;
import cn.zyszkj.cloud.kernel.scanner.modular.model.ResourceParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 监听项目初始化完毕,报告到服务器资源
 * @author XuJZ
 */
@Slf4j
public class ResourceReportListener implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

    private static boolean INIT_RESOURCE_FLAG = false;

    private ApplicationContext applicationContext;
    private ScannerProperties scannerProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!INIT_RESOURCE_FLAG) {

            //获取当前系统的所有资源
            ResourceFactory resourceFactory = applicationContext.getBean(ResourceFactory.class);
            Map<String, ResourceParam> resourceParams = resourceFactory.getResourceParams();

            //发送资源到资源服务器
            ScannerProperties scannerProperties = applicationContext.getBean(ScannerProperties.class);
            RegisterResourceService resourceService = applicationContext.getBean(RegisterResourceService.class);
            if (scannerProperties.getDelay() == 0L){
                resourceService.registerResource(new RegisterResourceParam(scannerProperties.getAppCode(), resourceParams));
            } else {
                try {
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            resourceService.registerResource(new RegisterResourceParam(scannerProperties.getAppCode(), resourceParams));
                        }
                    };
                    new Timer().schedule(timerTask,scannerProperties.getDelay());
                } catch (ServiceException e) {
                    log.error(e.getMessage(),e);
                }
            }
            INIT_RESOURCE_FLAG = true;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setScannerProperties(ScannerProperties scannerProperties) {
        this.scannerProperties = scannerProperties;
    }
}
