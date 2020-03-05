<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Whoops</title>
</head>
<body>
<h2>Whoops, Looks like you took a wrong turn somewhere.</h2>
<%= request.getAttribute("error")%>

<form action="/e-Petition_System/index">
	<input type="submit" value="Back"/>
</form>
</body>
</html>