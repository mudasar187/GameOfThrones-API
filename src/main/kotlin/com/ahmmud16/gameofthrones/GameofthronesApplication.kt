package com.ahmmud16.gameofthrones

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class GameofthronesApplication


@Bean
fun swaggerApi(): Docket {
    return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .paths(PathSelectors.any())
            .build()
}

private fun apiInfo(): ApiInfo {
    return ApiInfoBuilder()
            .title("API for Game Of Thrones")
            .description("This is a RestAPI info about Game Of Thrones Characters")
            .version("1.0.0") // Note the change in version
            .build()
}

fun main(args: Array<String>) {
    runApplication<GameofthronesApplication>(*args)
}
