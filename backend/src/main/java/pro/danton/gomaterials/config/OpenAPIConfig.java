package pro.danton.gomaterials.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GoMaterials Bid  API")
                        .version("1.0.0"))
                .servers(List.of(
                    new Server().url("https://gomaterials.danton.pro/").description("Development server url")))
        ;
    }
}
