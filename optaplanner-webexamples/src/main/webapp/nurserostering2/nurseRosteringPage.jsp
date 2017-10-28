<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.optaplanner.examples.nurserostering.domain.Employee" %>
<%@ page import="java.util.List" %>
<%@ page import="org.optaplanner.examples.nurserostering.domain.Shift" %>
<%@ page import="org.optaplanner.examples.nurserostering.domain.ShiftAssignment" %>
<%@ page import="org.optaplanner.examples.nurserostering.domain.ShiftDate" %>
<%@ page import="org.optaplanner.examples.nurserostering.domain.NurseRoster" %>
<%@ page import="org.optaplanner.webexamples.nurserostering2.NurseRosteringSessionAttributeName" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore" %>

<%
  NurseRoster solution = (NurseRoster) session.getAttribute(NurseRosteringSessionAttributeName.SHOWN_SOLUTION);
  HardSoftScore score = solution.getScore();	
  List<Shift> shiftList = solution.getShiftList();
  List<Employee> employeeList = solution.getEmployeeList();
  List<ShiftDate> shiftDateList = solution.getShiftDateList();
  
  Map<Employee, Map<ShiftDate, List<Shift>>> employeeToShiftDateToListShiftMap = new LinkedHashMap<Employee, Map<ShiftDate, List<Shift>>>(employeeList.size());
  
  
  
  for (Employee employee : employeeList){
	  Map<ShiftDate, List<Shift>> shiftDateToListShiftMap = new LinkedHashMap<ShiftDate, List<Shift>>();	  
	  for(ShiftDate shiftDate : shiftDateList){
		  shiftDateToListShiftMap.put(shiftDate, new ArrayList<Shift>());
	  }	  
	  employeeToShiftDateToListShiftMap.put(employee, shiftDateToListShiftMap);
  }
  
  Map<ShiftDate, List<Shift>> unassignedShifts = new LinkedHashMap<ShiftDate, List<Shift>>(shiftDateList.size());
  
  for (ShiftDate sd : shiftDateList){
	  unassignedShifts.put(sd, new ArrayList<Shift>());
  }
  
  for (ShiftAssignment sa : solution.getShiftAssignmentList()){
	  ShiftDate sa_sd = sa.getShiftDate();
	  Employee sa_emp = sa.getEmployee();
	  
	  Shift sa_shift = sa.getShift();
	  
	  if(sa_emp == null){
		  unassignedShifts.get(sa_sd).add(0, sa_shift);
	  } else {
		  employeeToShiftDateToListShiftMap.get(sa_emp).get(sa_sd).add(0, sa_shift);  
	  }
	  
  }

%>
  
<p style="margin-top: 10px;">Score: <%=score == null ? "" : score.isFeasible() ? score.getSoftScore() + " $" : "Infeasible"%></p>
<table>
  <thead>
  <tr>
    <th>Employee</th>
    <%
    	for(ShiftDate sd : shiftDateList){
    %>
    <th><%= sd.getLabel() %></th>
    <%
    }
    %>
  </tr>
  </thead>
  <tbody>
  <tr>
  <td>Unassigned</td>
   <%
   	for (Map.Entry<ShiftDate, List<Shift>> unassign : unassignedShifts.entrySet()){
   		ShiftDate sd = unassign.getKey();
   		List<Shift> unassignShiftList = unassign.getValue();
	%>
	
		<td>
		<%
			for (Shift s : unassignShiftList){
			%>
			<%= s.getShiftType().getCode() %><br/>
			<%		
			}
		%>
		
		</td>
 	<% 
   	}
    %>
  </tr>
  <%
    for (Map.Entry<Employee, Map<ShiftDate, List<Shift>>> entry : employeeToShiftDateToListShiftMap.entrySet()) {
      Employee employee = entry.getKey();
      Map<ShiftDate, List<Shift>> shiftDateShiftListMap = entry.getValue();
      
   %>
   <tr>
    <td><%= employee.getLabel() %> </td>
   
    <%
   		for (Map.Entry<ShiftDate, List<Shift>> inner : shiftDateShiftListMap.entrySet()){
    	ShiftDate sd = inner.getKey();
    	List<Shift> innerShiftList = inner.getValue();
    %>
    <td>
    
    <%
    	for (Shift s : innerShiftList){
    %>
    <%= s.getShiftType().getCode() %> <br>
    <%
    	}
    %>
    </td>
    <%
    	}
    %>
  </tr>    
   <%
   }
   %>

  </tbody>
</table>
