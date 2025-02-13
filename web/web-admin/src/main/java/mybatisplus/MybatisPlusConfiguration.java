package mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.grant.MiShe.web.*.mapper")
public class MybatisPlusConfiguration {

}
