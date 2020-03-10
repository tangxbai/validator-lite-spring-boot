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
import com.viiyue.plugins.validator.constraints.Rules;

public class Role {

	@Label( "{common.form.roleName}" )
	@Rules( "required; not-blank; email" )
	private String roleName;

	private String remark;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName( String roleName ) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark( String remark ) {
		this.remark = remark;
	}

}
