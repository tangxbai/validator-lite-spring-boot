package com.viiyue.validator.spring.boot.autoconfigure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.viiyue.plugins.validator.scripting.configuration.ContextConfigurion;

/**
 * Javabean based on springboot automatic assembly property configuration
 *
 * @author tangxbai
 * @since 1.0.0
 */
@ConfigurationProperties( prefix = "validator.setting", ignoreInvalidFields = true )
public class ValidatorProperties extends ContextConfigurion {
}
