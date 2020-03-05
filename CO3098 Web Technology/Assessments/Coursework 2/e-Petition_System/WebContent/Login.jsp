<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to the Shangri-La Petition Website </title>
</head>
<body>
<% String output="";
	if (request.getAttribute("email")!=null)
		output=request.getAttribute("email").toString();
%>

<h1>Welcome to the e-Petition Website</h1>
<h3>Here you can create or view petitons to influence how your Shangri-La is run <br/> To get Started, Please Register</h3>
<form action="http://localhost:8080/e-Petition_System/Registration.html">
<Button type="submit" >Click to Register</Button>
</form>
<br/>
<h3>For Existing Users, Please Login In<br/></h3>
<form action="/e-Petition_System/login" method="POST">
<h3>E-mail Address: </h3>
<input type="text" name="email" id="email" value=<%= output%>></input>
<h3>Password: </h3>
<input type="password" name="password" id="password"></input>
<br/>
<button type="submit">Login</button>

</form>

</body>
</html>