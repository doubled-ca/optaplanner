<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.optaplanner.webexamples.nurserostering2.NurseRosteringWebAction" %>
<%
  new NurseRosteringWebAction().setup(session);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>OptaPlanner webexamples: Nurse Rostering</title>
  <jsp:include page="/common/head.jsp"/>
  <![endif]-->
</head>
<body>

<div class="container">
  <div class="row">
    <div class="col-md-3">
      <jsp:include page="/common/menu.jsp"/>
    </div>
    <div class="col-md-9">
      <header class="main-page-header">
        <h1>Cloud balancing</h1>
      </header>
      <p>Assign Employees to shifts.</p>
      <p>A dataset has been loaded.</p>
      <div>
        <button class="btn" onclick="window.location.href='solve.jsp'">
          <i class="icon-play"></i> Solve this planning problem
        </button>
      </div>
      <jsp:include page="nurseRosteringPage.jsp"/>
    </div>
  </div>
</div>

<jsp:include page="/common/foot.jsp"/>
</body>
</html>
