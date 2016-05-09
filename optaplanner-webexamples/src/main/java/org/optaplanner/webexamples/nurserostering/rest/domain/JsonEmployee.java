package org.optaplanner.webexamples.nurserostering.rest.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JsonEmployee {
	
	protected String name;
	protected String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	

}
