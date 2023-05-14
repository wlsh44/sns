package com.example.sns.common.dev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Field;
import java.util.List;

@Slf4j
@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DevConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        Class<InterceptorRegistry> aClass = InterceptorRegistry.class;
        try {
            Field field = aClass.getDeclaredField("registrations");
            field.setAccessible(true);
            List<InterceptorRegistration> registrations = (List<InterceptorRegistration>) field.get(registry);
            registrations.get(0)
                    .excludePathPatterns("/h2-console/**")
                    .excludePathPatterns("/favicon.ico")
                    .excludePathPatterns("/ngrinder/**");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
