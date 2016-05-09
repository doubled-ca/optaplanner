package org.optaplanner.webexamples.nurserostering.rest.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@XmlRootElement
public class JsonNurseRosterSolution {

	protected List<JsonShift> shiftList;
	protected List<JsonEmployee> employeeList;
	protected List<JsonShiftAssignment> shiftAssignmentList;
	
	protected Boolean feasible;

	protected String score;

	public List<JsonShift> getShiftList() {
		return shiftList;
	}

	public void setShiftList(List<JsonShift> shiftList) {
		this.shiftList = shiftList;
	}

	public List<JsonEmployee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<JsonEmployee> employeeList) {
		this.employeeList = employeeList;
	}

	public List<JsonShiftAssignment> getShiftAssignmentList() {
		return shiftAssignmentList;
	}

	public void setShiftAssignmentList(List<JsonShiftAssignment> shiftAssignmentList) {
		this.shiftAssignmentList = shiftAssignmentList;
	}

	public Boolean getFeasible() {
		return feasible;
	}

	public void setFeasible(Boolean feasible) {
		this.feasible = feasible;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	
}
