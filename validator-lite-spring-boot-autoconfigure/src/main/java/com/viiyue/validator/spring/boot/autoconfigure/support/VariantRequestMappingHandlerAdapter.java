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
package com.viiyue.validator.spring.boot.autoconfigure.support;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.viiyue.plugins.validator.spring.beans.ValidationDataBinderFactory;

/**
 * Wrapped {@link RequestMappingHandlerAdapter}, a binder for adding data validation.
 * 
 * @author tangxbai
 * @since 1.2.0
 */
public class VariantRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

	public void setHandlerAdapter( RequestMappingHandlerAdapter handlerAdapter ) {
		BeanUtils.copyProperties( handlerAdapter, this );
	}

	@Override
	protected InitBinderDataBinderFactory createDataBinderFactory( List<InvocableHandlerMethod> binderMethods ) throws Exception {
		return new ValidationDataBinderFactory( binderMethods, getWebBindingInitializer() );
	}

}
