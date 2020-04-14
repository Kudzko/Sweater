package by.kudko.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Все обращения имеющие в себе /img/** будет перенаправлять все файлы
        registry.addResourceHandler("/img/**")
                // by path
        .addResourceLocations("file://" + uploadPath + "/");
        // Все обращения имеющие в себе /static/** будет перенаправлять все файлы
        registry.addResourceHandler("/static/**")
                // by path. file - for file system, classpath - for looking for by project dir/ tree
                .addResourceLocations("classpath:/static/");
    }
}
