<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Success!</title>
</head>
<body>
<h2>Your Action has been successful</h2>
<%= request.getAttribute("success")%>

<form action="/e-Petition_System/login">
	<input type="submit" value="Click here to return Home"/>
</form>
</body>
</html>