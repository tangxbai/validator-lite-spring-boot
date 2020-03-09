package com.viiyue.validator.spring.boot.autoconfigure.configuration;

import javax.servlet.Servlet;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.lang.Nullable;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.viiyue.plugins.validator.spring.ValidatorLite;
import com.viiyue.plugins.validator.spring.beans.ValidationRequestMappingHandlerAdapter;
import com.viiyue.plugins.validator.spring.message.SpringMessageResovler;

/**
 * {@link EnableAutoConfiguration Auto-configuration} to configure the validation infrastructure.
 *
 * @author tangxbai
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ ValidatorProperties.class })
@ConditionalOnWebApplication( type = Type.SERVLET )
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class, WebMvcRegistrations.class, ValidatorLite.class })
@AutoConfigureBefore({ WebMvcAutoConfiguration.class, org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration.class })
@AutoConfigureAfter({ MessageSourceAutoConfiguration.class })
public class ValidationAutoConfiguration implements WebMvcRegistrations, WebMvcConfigurer {

	public ValidationAutoConfiguration( 
		ValidatorProperties validatorProperties, 
		ObjectProvider<MessageSource> messageSourceProvider ) {
		com.viiyue.plugins.validator.Validator.prepare();
		setMessageResolver( messageSourceProvider.getIfAvailable() );
		com.viiyue.plugins.validator.Validator.configuration( validatorProperties );
	}
	
	@Override
	public Validator getValidator() {
		return new ValidatorLite();
	}

	@Override
	public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
		return new ValidationRequestMappingHandlerAdapter();
	}
	
	private void setMessageResolver( @Nullable MessageSource messageSource ) {
		if ( messageSource != null && !( messageSource instanceof DelegatingMessageSource ) ) {
			SpringMessageResovler messageResolver = new SpringMessageResovler( messageSource );
			com.viiyue.plugins.validator.Validator.setMessageResolver( messageResolver );
		}
	}

}
