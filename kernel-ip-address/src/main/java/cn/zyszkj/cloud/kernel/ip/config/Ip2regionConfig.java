package cn.zyszkj.cloud.kernel.ip.config;

import cn.zyszkj.cloud.kernel.ip.modular.service.IpAddressService;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ip配置
 *
 * @author XuJZ
 */
@Configuration
public class Ip2regionConfig {

    @Bean
    public DbSearcher dbSearcher() throws IOException, DbMakerConfigException {
        ClassPathResource resource = new ClassPathResource("ip2region.db");
        InputStream fis = resource.getInputStream();
        return new DbSearcher(new DbConfig(), read(fis));
    }

    @Bean
    public IpAddressService addressService(DbSearcher dbSearcher) {
        return new IpAddressService(dbSearcher);
    }

    public byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                stream.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            stream.flush();
            return stream.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
