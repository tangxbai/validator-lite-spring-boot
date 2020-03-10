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
package com.viiyue.validator.spring.boot.samples.handlers;

import com.viiyue.plugins.validator.handler.Handler;
import com.viiyue.plugins.validator.metadata.Fragment;
import com.viiyue.plugins.validator.scripting.Context;

public class Test2Handler implements Handler {

	@Override
	public String name() {
		return "test2";
	}

	@Override
	public boolean support( Class<?> valueType ) {
		return true;
	}

	@Override
	public boolean doHandle( Object value, Fragment fragment, Context context ) {
		System.out.println( fragment.getTemplate() );
		return false;
	}

}
