/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.ureport.build;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年11月1日
 */
public class Dataset {
	public static final int DATASET_TYPE_ALL=0;
	public static final int DATASET_TYPE_FORM=1;
	public static final int DATASET_TYPE_BODY=2;
	private boolean usedInForm;
	private boolean usedInBody;
	private String name;
	private List<?> data;
	
	public Dataset(String name, List<?> data) {
		this.name = name;
		this.data = data;
		this.usedInForm=true;
		this.usedInBody=true;
	}
	public Dataset(String name, List<?> data,boolean usedInForm,boolean usedInBody) {
		this.name = name;
		this.data = data;
		this.usedInForm=usedInForm;
		this.usedInBody=usedInBody;
	}

	public boolean isUsedInForm() {
		return usedInForm;
	}

	public void setUsedInForm(boolean usedInForm) {
		this.usedInForm = usedInForm;
	}

	public boolean isUsedInBody() {
		return usedInBody;
	}

	public void setUsedInBody(boolean usedInBody) {
		this.usedInBody = usedInBody;
	}

	public String getName() {
		return name;
	}
	public List<?> getData() {
		return data;
	}
}
