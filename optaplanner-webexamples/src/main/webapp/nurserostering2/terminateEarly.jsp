<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.optaplanner.webexamples.nurserostering2.NurseRosteringWebAction" %>
<%
  new NurseRosteringWebAction().terminateEarly(session);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="REFRESH" content="0;url=<%=application.getContextPath()%>/nurserostering2/terminated.jsp"/>
</head>
<body>
</body>
</html>
