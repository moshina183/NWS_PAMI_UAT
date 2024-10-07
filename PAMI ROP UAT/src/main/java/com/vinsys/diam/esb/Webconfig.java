package com.vinsys.diam.esb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.vinsys.diam.interceptor.BaseInterceptor;

@Configuration
public class Webconfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new BaseInterceptor()).excludePathPatterns("/error").excludePathPatterns("/api/v1/getpersoninformation");
		registry.addInterceptor(new BaseInterceptor()).excludePathPatterns("/error");
	}
	
	@Bean 
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	return new PropertySourcesPlaceholderConfigurer();
	}
	
	/*
	 * @Bean public Jaxb2Marshaller marshaller() { Jaxb2Marshaller marshaller = new
	 * Jaxb2Marshaller(); marshaller.setContextPath("com.vinsys.diam.stub.company");
	 * return marshaller; }
	 */
}
