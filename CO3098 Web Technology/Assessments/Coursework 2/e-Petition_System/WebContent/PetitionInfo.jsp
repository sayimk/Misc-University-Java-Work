<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>

<h1>Petition Title: ${petitionTitle}</h1>
<br/>
<h2>Created by ${petitionCreator}</h2>
<h2>Added On: ${petitionAddedDate}</h2>
<h2>Petition Description:</h2>
<p>${petitionContent}</p>

<br/>
<h4>Current Number of Votes: ${petitionVotes}</h4>
<h1 id="error"></h1>

<h3><a href="/e-Petition_System/SignPetition?PetitionId=${petitionId}&UserEmail=${UserEmail}">Click here to Sign this Petition</a></h3>


<br/>
<h3>Comments Posted by MPs</h3>
<Table>
	<col width="300">
	<col width="300">
<tr>
	<th align="left">Comment</th>
	<th align="left">By MP</th>
</tr>
<c:forEach items="${comments}" var="comment">
		<tr>
		<td align="left">${comment.getComment()}</td>
		<td align="left">${comment.getMP()}</td>
		</tr>
	</c:forEach>
</Table>
<form action="/e-Petition_System/CommentPetition" method="post">
<input type="hidden" name ="PetitionId"  id ="PetitionId" value="${petitionId}"></input>
<input type ="text" name="comment"></input>
<input type="submit" value ="Add a Comment"></input
</form>

</body>
</html>