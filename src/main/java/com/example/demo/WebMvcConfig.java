package com.example.demo;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private CertificationInterceptor interceptor;
    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private ClientInterceptor clientInterceptor;

    @Bean
    public ModelMapper modelMapper(){
        return  new ModelMapper();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(clientInterceptor)
                .addPathPatterns("/")
                .excludePathPatterns("/**");

        registry.addInterceptor(interceptor)
                .addPathPatterns("/mypage", "/admin/**")
                .addPathPatterns("/cart", "/oinfo", "/olist", "pinfo")
                .excludePathPatterns("/login", "/");

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**", "/admin");



    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
