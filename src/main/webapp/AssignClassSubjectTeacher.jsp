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
				<h1>Classes, Subjects and Teachers Assignment</h1>
			</td>
		</tr>
	</table>
	<table class="center">
		<tr>			
			<td>
				<form action="AssignClassSubjectTeacher" method="post">
					<table class="borders">						
						<c:forEach var="entry" items="${ClassSubjectTeacherMap}">
							<tr>${entry.value}</tr>
						</c:forEach>
					</table>
					<table>						
						<tr>
							<th align="left">Assignment</th>
						</tr>
						<tr>
							<td><label>Class: </label></td>
							<td>
								<select name="Class">
									<c:forEach var="entry" items="${ClassMap}">
										<option value="${entry.key}"> ${entry.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><label>Subject: </label></td>
							<td>
								<select name="Subject">
									<c:forEach var="entry" items="${SubjectMap}">
										<option value="${entry.key}"> ${entry.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><label>Teacher: </label></td>
							<td>
								<select name="Teacher">
									<c:forEach var="entry" items="${TeacherMap}">
										<option value="${entry.key}"> ${entry.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>						
						<tr>
							<td><input type="submit" name="Action" value="Assign" /></td><td><input type="submit" name="Action" value="Unassign" /></td>
						</tr>
					</table>					
				</form>
			</td>
		</tr>		
	</table>
	<table class="center">
		<tr>
			<td>
				<%
				if (request.getAttribute("ClassSubjectTeacherMessage") != null)
					out.println(request.getAttribute("ClassSubjectTeacherMessage"));
				%>
			</td>
		</tr>
	</table>
	</div>
</body>
</html>