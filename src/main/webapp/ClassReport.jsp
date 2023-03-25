<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Learner's Academy</title>
<link href="css/styles.css" rel="stylesheet" type="text/css">
</head>
<body><div>
	<table class="right">
		<tr>
			<td><form action="Main.jsp" method="post">
					<input type="submit" value="Main">
				</form></td>
			<td><form action="Logout" method="post">
					<input type="submit" value="Logout">
				</form></td>
		</tr>
	</table>
	<table class="center">
		<tr>
			<td>
				<h1>Class Report</h1>
			</td>
		</tr>
	</table>
	<table class="center">
		<tr>
			<td>
				<table class="borders">
					<c:forEach var="entry" items="${StudentReportMap}">
						<tr>${entry.value}</tr>
					</c:forEach>
				</table>
			</td>
			<td>
				<table class="borders">
					<c:forEach var="entry" items="${ClassSubjectTeacherMap}">
						<tr>${entry.value}</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</table>
	</div>
</body>
</html>