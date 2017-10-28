<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.optaplanner.webexamples.nurserostering2.NurseRosteringWebAction" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>OptaPlanner webexamples: Nurse Rostering</title>
  <jsp:include page="/common/head.jsp"/>
</head>
<body>

<div class="container">
  <div class="row">
    <div class="col-md-3">
      <jsp:include page="/common/menu.jsp"/>
    </div>
    <div class="col-md-9">
      <header class="main-page-header">
        <h1>Nurse Rostering</h1>
      </header>
      <p>Assign employees to shifts.</p>
      <p>The solver has been terminated. Below is the final solution.</p>
      <div>
        <button class="btn" onclick="window.location.href='loaded.jsp'"><i class="icon-fast-backward"></i> Start again</button>
      </div>
      <jsp:include page="nurseRosteringPage.jsp"/>
    </div>
  </div>
</div>

<jsp:include page="/common/foot.jsp"/>
</body>
</html>
