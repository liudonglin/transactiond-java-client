package com.liudonglin.transactiond.tr.core.tracing.http;

import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

public interface WebMvcConfigurer extends org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    @Override
    default void configurePathMatch(PathMatchConfigurer configurer) {

    }

    @Override
    default void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

    }

    @Override
    default void configureAsyncSupport(AsyncSupportConfigurer configurer) {

    }

    @Override
    default void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

    }

    @Override
    default void addFormatters(FormatterRegistry registry) {

    }

    @Override
    default void addInterceptors(InterceptorRegistry registry) {

    }

    @Override
    default void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    default void addCorsMappings(CorsRegistry registry) {

    }

    @Override
    default void addViewControllers(ViewControllerRegistry registry) {

    }

    @Override
    default void configureViewResolvers(ViewResolverRegistry registry) {

    }

    @Override
    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

    }

    @Override
    default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }

    @Override
    default void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    default void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    default void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    default void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    default Validator getValidator() {
        return null;
    }

    @Override
    default MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}
