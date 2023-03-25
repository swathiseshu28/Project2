<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<link href="css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
	<%
	//allow access only if session exists
	if (session.getAttribute("user") == null) {
		response.sendRedirect("index.jsp");
	}
	String userName = null;
	String sessionID = null;
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("user"))
		userName = cookie.getValue();
		}
	}
	%>
	<div>
	<table class="center">
		<tr>			
			<td>
	<h1>Learner's Academy Admin's Activities</h1>
	</td>
		</tr>
	</table>
	<table class="right">
		<tr>			
			<td><form action="Logout" method="post">
					<input type="submit" value="Logout">
				</form></td>
		</tr>
	</table>
	<table class="center">
		<tr><td>
		<table>
		<tr>
			<td><form action="LoadClassData" method="post">
					<input type="submit" value="Manage Class">
				</form></td>
		</tr>		
		<tr>
			<td><form action="LoadTeacherData" method="post">
					<input type="submit" value="Manage Teacher">
				</form></td>
		</tr>
		<tr>
			<td><form action="LoadSubjectData" method="post">
					<input type="submit" value="Manage Subject">
				</form></td>
		</tr>
		<tr>
			<td><form action="LoadStudentData" method="post">
					<input type="submit" value="Manage Student">
				</form></td>
		</tr>
		<tr>
			<td><form action="LoadClassSubjectData" method="post">
					<input type="submit" value="Classes and Subjects">
				</form></td>
		</tr>
		<tr>
			<td><form action="LoadClassSubjectTeacherData" method="post">
					<input type="submit" value="Classes, Subjects & Teachers">
				</form></td>
		</tr>
		<tr>
			<td><form action="LoadStudentReport" method="post">
					<input type="submit" value="Student Report">
				</form></td>
		</tr>
		<tr>
			<td><form action="LoadClassReport" method="post">
					<input type="submit" value="Class Report">
				</form></td>
		</tr>
		</table>
		</td>
		<td>
		<img src="images/image-6-900x338.png" alt="Learners" width="100%" height="100%">
		</td>
		</tr>
	</table>
	</div>
</body>
</html>