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
				<h1>Manage Teacher</h1>
			</td>
		</tr>
	</table>
	<table class="center">
		<tr>			
			<td>
				<form action="ManageTeacher" method="post">
					<table>
						<tr>
							<th>Add New Teacher</th>
						</tr>
						<tr><td><label>ID: </label></td><td><input type="text" name="AddTeacher" /></td></tr>
						<tr><td><label>Name: </label></td><td><input type="text" name="name" /></td></tr>
						<tr><td><label>Age: </label></td><td><input type="number" name="age" /></td></tr>
						<tr><td><label>Gender: </label></td><td><select id="gender" name="gender">
								  <option value="M">Male</option>
								  <option value="F">Female</option>
								</select></td>
						</tr>
						<tr>
							<td><input type="submit" value="Add" /></td>
						</tr>
					</table>					
				</form>
			</td>
		</tr>
		<tr>
			<td>
				<form action="ManageTeacher" method="post">
					<table>
						<tr>
							<th>Delete Existing Teacher</th>
						</tr>
						<tr>
							<td><label>Teacher: </label></td>
							<td>
								<select name="DeleteTeacher">
									<c:forEach var="entry" items="${TeacherMap}">
										<option value="${entry.key}"> ${entry.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><input type="submit" value="Delete" /></td>
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
				if (request.getAttribute("TeacherMessage") != null)
					out.println(request.getAttribute("TeacherMessage"));
				%>
			</td>
		</tr>
	</table>
	</div>
</body>
</html>