<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Existing Folders</title>
</head>
<body>
<h1>Existing Folders:</h1>

<c:forEach items="${model.folderList}" var="folder">
	<h3>${folder.getParentFolder()} ${folder.getFolderName()}</h3>
</c:forEach>
</body>
</html>