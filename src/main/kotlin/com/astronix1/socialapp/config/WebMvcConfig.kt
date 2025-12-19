package com.astronix1.socialapp.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val uploadDir = "uploads"
        val uploadPath = Paths.get(uploadDir).toAbsolutePath().toString()

        registry.addResourceHandler("/$uploadDir/**")
            .addResourceLocations("file:$uploadPath/")
    }
}
