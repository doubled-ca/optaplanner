package org.optaplanner.webexamples.nurserostering.rest.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.examples.nurserostering.domain.Employee;
import org.optaplanner.examples.nurserostering.domain.NurseRoster;
import org.optaplanner.examples.nurserostering.domain.Shift;
import org.optaplanner.examples.nurserostering.domain.ShiftAssignment;
import org.optaplanner.webexamples.nurserostering.rest.cdi.NurseRosterSolverManager;
import org.optaplanner.webexamples.nurserostering.rest.domain.JsonEmployee;
import org.optaplanner.webexamples.nurserostering.rest.domain.JsonMessage;
import org.optaplanner.webexamples.nurserostering.rest.domain.JsonNurseRosterSolution;
import org.optaplanner.webexamples.nurserostering.rest.domain.JsonShift;
import org.optaplanner.webexamples.nurserostering.rest.domain.JsonShiftAssignment;

public class DefaultNurseRosteringRestService implements NurseRosteringRestService {

	@Inject
    private NurseRosterSolverManager solverManager;

    @Context
    private HttpServletRequest request;

    @Override
    public JsonNurseRosterSolution getSolution() {
        NurseRoster solution = solverManager.retrieveOrCreateSolution(request.getSession().getId());
        return convertToJsonNurseRoster(solution);
    }
    
    
	private JsonNurseRosterSolution convertToJsonNurseRoster(NurseRoster solution) {
		JsonNurseRosterSolution jsonSolution = new JsonNurseRosterSolution();
		
		List<JsonEmployee> employeeList = new ArrayList<JsonEmployee>(solution.getEmployeeList().size());
		for (Employee e : solution.getEmployeeList()){
			JsonEmployee je = convertToJsonEmployee(e);
			employeeList.add(je);
		}
		jsonSolution.setEmployeeList(employeeList);
		
		List<JsonShift> shiftList = new ArrayList<JsonShift>(solution.getShiftList().size());
		
		for (Shift s : solution.getShiftList()){
			JsonShift jShift = convertToJsonShift(s);
			shiftList.add(jShift);
		}
		jsonSolution.setShiftList(shiftList);
		
		List<JsonShiftAssignment> shiftAssignmentList = new ArrayList<JsonShiftAssignment>(solution.getShiftAssignmentList().size());
		for (ShiftAssignment sa : solution.getShiftAssignmentList()){
			JsonShiftAssignment jShiftAssign = convertToJsonShiftAssignment(sa);
			shiftAssignmentList.add(jShiftAssign);
		}
		jsonSolution.setShiftAssignmentList(shiftAssignmentList);
		
		HardSoftScore score = solution.getScore();
		
		if(score != null){
			jsonSolution.setScore(score.toString());
		} 
		
		jsonSolution.setFeasible(score != null && score.isFeasible());
		
		return jsonSolution;
	}


	private JsonShiftAssignment convertToJsonShiftAssignment(ShiftAssignment sa) {
		JsonShiftAssignment jShiftAssign = new JsonShiftAssignment();
		if(sa.getEmployee() != null){
			jShiftAssign.setEmployee(convertToJsonEmployee(sa.getEmployee()));
		}
		
		jShiftAssign.setShift(convertToJsonShift(sa.getShift()));
		jShiftAssign.setIndexInShift(sa.getShift().getIndex());
		
		Date date = parseDateString(sa);

		jShiftAssign.setShiftDate(date);
		return jShiftAssign;
	}


	private Date parseDateString(ShiftAssignment sa) {
		TimeZone LOCAL_TIMEZONE = TimeZone.getTimeZone("GMT");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(LOCAL_TIMEZONE);
		calendar.clear();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setCalendar(calendar);
		Date date;
		String dateString = sa.getShiftDate().getDateString();
		try {
		    date = dateFormat.parse(dateString);
		} catch (ParseException e) {
		    throw new IllegalStateException("Could not parse shiftDate (" + this
		            + ")'s dateString (" + dateString + ").");
		}
		return date;
	}


	private JsonShift convertToJsonShift(Shift s) {
		JsonShift jShift = new JsonShift();
		jShift.setCode(s.getShiftType().getCode());
		jShift.setDescription(s.getShiftType().getDescription());
		return jShift;
	}


	private JsonEmployee convertToJsonEmployee(Employee e) {
		JsonEmployee je = new JsonEmployee();
		je.setCode(e.getCode());
		je.setName(e.getName());
		return je;
	}
	
	
	
	@Override
	public JsonMessage solve() {
		boolean success = solverManager.solve(request.getSession().getId());
        return new JsonMessage(success ? "Solving started." : "Solver was already running.");
	}

	@Override
	public JsonMessage terminateEarly() {
		boolean success = solverManager.terminateEarly(request.getSession().getId());
        return new JsonMessage(success ? "Solver terminating early." : "Solver was already terminated.");
	}

}
