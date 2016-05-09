package org.optaplanner.webexamples.nurserostering.rest.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JsonShift {

	
	protected String code;
	protected String description;
	
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
