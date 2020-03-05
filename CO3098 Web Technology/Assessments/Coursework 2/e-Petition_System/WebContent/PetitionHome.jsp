<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>e-Petiton Home</title>
</head>
<body>
	<h1>Welcome Back, <%= request.getAttribute("email")%></h1>
	<form action="/e-Petition_System/logout">
		<input type="submit" value="Logout"/>
	</form>

	<br/>
	<h4>Welcome, this is your e-Petition home screen, here you can add a petition or sign and support existing petitions </h4>
	<form action="/e-Petition_System/PetitionCreation.html">
		<input type="submit" value="Add New Petition"/>
	</form>

	<br/>

	<h3>Here are the existing petitions</h3>
	<Table>
	<col width="50">
	<col width="300">
	<col width="300">
	
	<tr>
	<th align="left">#</th>
	<th align="left">Petition Title</th>
	<th align="left">Petition Creator</th>
	<th align="left">Current Number of Votes</th>
	</tr>
	<c:forEach items="${petitionList}" var="petition">
		<tr>
		<td align="left">${petition.getId()}</td>
		<td align="left">${petition.getPetitionName()}</td>
		<td align="left">${petition.getCreator()}</td>
		<td align="left">${petition.getCurrentVotes()}</td>
		<td align="center">
			<form action="/e-Petition_System/GetPetitionPage" method="post">	
				<input type="hidden" name="petitionId" value="${petition.getId()}"></input>
				<input type="submit" value="View"/>
			</form>
		</td>
		
		</tr>
	</c:forEach>

	</Table>

</body>
</html>