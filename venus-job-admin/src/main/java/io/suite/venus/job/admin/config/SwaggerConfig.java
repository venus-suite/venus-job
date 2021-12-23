package io.suite.venus.job.admin.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
@ConditionalOnProperty(name = "enableSwagger", havingValue = "true")
public class SwaggerConfig {
    @Bean("swApi2")
    public Docket createRestApi() {
        List<Parameter> pars = new ArrayList<Parameter>();
        ParameterBuilder authorizationTokenPar = new ParameterBuilder();
        authorizationTokenPar.name("token").
                description("基于权限中心的JWT解析得到，登录后每次请求需要加上").
                modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(authorizationTokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("io.suite.venus.job.admin"))//扫描的是自己写Controller的包名。
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Job管理后台")
                .description("接口描述")
                .version("1.0.0-SNAPSHOT")
                .build();
    }
}
