package com.viiyue.validator.spring.boot.autoconfigure.configuration;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import com.viiyue.plugins.validator.spring.utils.PrecompileUtils;

/**
 * Register a program startup completion listener. After the program startup is
 * completed, all request mapping methods are automatically scanned, and then
 * the validation rules are precompiled to reduce the time consumption in the
 * actual request.
 *
 * @author tangxbai
 * @since 1.0.0
 */
public class ValidatorStartedListener implements ApplicationListener<ApplicationStartedEvent> {

	@Override
	public void onApplicationEvent( ApplicationStartedEvent event ) {
		PrecompileUtils.compile( event.getApplicationContext() );
	}

}
