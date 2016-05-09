package org.optaplanner.webexamples.nurserostering.rest.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class JsonShiftAssignment {

	protected JsonEmployee employee;
	
	protected JsonShift	shift;
	
	protected int indexInShift;
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	protected Date shiftDate;	

	public JsonEmployee getEmployee() {
		return employee;
	}
	
	
	public JsonShiftAssignment() {
	}

	public JsonShiftAssignment(JsonEmployee employee, JsonShift shift, int indexInShift, Date shiftDate) {
		this.employee = employee;
		this.shift = shift;
		this.indexInShift = indexInShift;
		this.shiftDate = shiftDate;
	}

	public void setEmployee(JsonEmployee employee) {
		this.employee = employee;
	}

	public JsonShift getShift() {
		return shift;
	}

	public void setShift(JsonShift shift) {
		this.shift = shift;
	}

	public int getIndexInShift() {
		return indexInShift;
	}

	public void setIndexInShift(int indexInShift) {
		this.indexInShift = indexInShift;
	}

	public Date getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
	}

	
}
