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
package com.viiyue.validator.spring.boot.samples.bean;

import com.viiyue.plugins.validator.annotation.Label;
import com.viiyue.plugins.validator.annotation.Valid;
import com.viiyue.plugins.validator.annotation.When;
import com.viiyue.plugins.validator.constraints.Required;
import com.viiyue.plugins.validator.constraints.Rules;

public class User {

	@Label( "{common.form.username}" )
	@Rules( "required; not-blank; range(10,20); pattern(/\\d+/)" )
	private String username;

	@Label( "{common.form.password}" )
	@Rules( "required; not-blank; password('strong'); equals('#username')" )
	@When( fields = "username", is = When.Result.PASSED )
	private String password;
	
	@Valid
	@Required
	private Role role;

	public String getUsername() {
		return username;
	}

	public void setUsername( String username ) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole( Role role ) {
		this.role = role;
	}

}
