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
