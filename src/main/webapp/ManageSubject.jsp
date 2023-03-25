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
				<h1>Manage Subject</h1>
			</td>
		</tr>
	</table>
	<table class="center">
		<tr>			
			<td>
				<form action="ManageSubject" method="post">
					<table>
						<tr>
							<th>Add New Subject</th>
						</tr>
						<tr><td><label>ID: </label></td><td><input type="text" name="AddSubject" /></td></tr>
						<tr><td><label>Name: </label></td><td><input type="text" name="name" /></td></tr>						
						<tr>
							<td><input type="submit" value="Add" /></td>
						</tr>
					</table>					
				</form>
			</td>
		</tr>
		<tr>
			<td>
				<form action="ManageSubject" method="post">
					<table>
						<tr>
							<th>Delete Existing Subject</th>
						</tr>
						<tr>
							<td><label>Subject: </label></td>
							<td>
								<select name="DeleteSubject">
									<c:forEach var="entry" items="${SubjectMap}">
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
				if (request.getAttribute("SubjectMessage") != null)
					out.println(request.getAttribute("SubjectMessage"));
				%>
			</td>
		</tr>
	</table>
	</div>
</body>
</html>