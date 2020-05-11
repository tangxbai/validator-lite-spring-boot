/**
 * Copyright (C) 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.validator.spring.boot.autoconfigure.configuration;

import javax.servlet.Servlet;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.viiyue.plugins.validator.ValidatorFactory;
import com.viiyue.plugins.validator.spring.ValidatorLite;
import com.viiyue.plugins.validator.spring.message.SpringMessageResovler;

/**
 * {@link EnableAutoConfiguration Auto-configuration} to configure the validation infrastructure.
 *
 * @author tangxbai
 * @since 1.0.0, Update in 1.2.0
 */
@Configuration
@EnableConfigurationProperties({ ValidatorProperties.class })
@ConditionalOnWebApplication( type = Type.SERVLET )
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class, ValidatorLite.class })
@AutoConfigureBefore( EnableWebMvcConfiguration.class )
@AutoConfigureAfter({ MessageSourceAutoConfiguration.class })
public class ValidatorInitialAutoConfiguration implements InitializingBean, WebMvcConfigurer {

	private final ValidatorProperties validatorProperties;
	private final MessageSource messageSource;
	
	public ValidatorInitialAutoConfiguration( 
		ValidatorProperties validatorProperties, 
		ObjectProvider<MessageSource> messageSourceProvider ) {
		this.validatorProperties = validatorProperties;
		this.messageSource = messageSourceProvider.getIfAvailable();
	}
	
	@Bean
	@Primary
	@Override
	public Validator getValidator() {
		return new ValidatorLite();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		com.viiyue.plugins.validator.Validator.prepare();
		
		// Initialize a custom data validation factory implementation class
		// Not initialized if factory class is null
		com.viiyue.plugins.validator.Validator.initFactory( validatorProperties.getFactoryInstance() );
		com.viiyue.plugins.validator.Validator.configuration( validatorProperties, false );
		
		// Validator message resolver
		if ( messageSource != null && !( messageSource instanceof DelegatingMessageSource ) ) {
			SpringMessageResovler messageResolver = new SpringMessageResovler( messageSource );
			com.viiyue.plugins.validator.Validator.setMessageResolver( messageResolver );
		}
		
		// Registering custom handlers
		ValidatorFactory factory = com.viiyue.plugins.validator.Validator.getFactory();
		for ( String handlerClassName : validatorProperties.getHandlerClassNames() ) {
			factory.addHandler( handlerClassName );
		}
		factory.afterInitialized();
	}

}
