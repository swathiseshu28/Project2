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
	<table class="center">
		<tr>
			<td>
				<h1>Welcome Learner's Academy!</h1>
			</td>
		</tr>
	</table>
	<form action="Login" method="post">
		<table class="center">
			<tr>
				<td style="">UserName</td>
				<td style=""><input type="text" name="username" /></td>
			</tr>
			<tr>
				<td style="">Password</td>
				<td style=""><input type="password" name="password" /></td>
			</tr>
		</table>
		<table class="center">
			<tr>
				<td><input type="submit" value="Login" /></td>
			</tr>
		</table>
		<table class="center">
			<tr>
				<td>
					<%
					if (request.getAttribute("InvalidLogin") != null)
						out.println(request.getAttribute("InvalidLogin"));
					%>
				</td>
			</tr>
		</table>

	</form>
	</div>
</body>
</html>