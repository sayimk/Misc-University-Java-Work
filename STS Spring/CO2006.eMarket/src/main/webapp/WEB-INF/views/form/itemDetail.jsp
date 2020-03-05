<!DOCTYPE HTML>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" media="screen" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css">
<html>
<head>
    <title>Order Item Editing</title>
</head>
<body>
<h2>New/edit Order Item Information</h2>
<form:form method="POST" commandName="itemFormDto" action="/item/add">
   <table>
    <tr>
        <td><form:label path="productId">Product</form:label></td>
    	<td><form:select path="productId">
    		<c:choose>
				<c:when test="${itemFormDto.getProductId() < 0}">
					<form:option value="0" label="--- none ---"/>
				</c:when>
				<c:otherwise>
					<form:option value="-1" label="--- none ---" selected="selected"/>
				</c:otherwise>
			</c:choose>
			<c:forEach var="item" items="${itemFormDto.getProductList()}">
				<c:choose>
					<c:when test="${itemFormDto.getProductId()==item.getId()}">
						<form:option value="${item.getId()}" label="${item.getName()}" selected="selected"/>
					</c:when>
					<c:otherwise>
						<form:option value="${item.getId()}" label="${item.getName()}"/>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</form:select></td>
    </tr>
    <tr>
        <td><form:label path="amount">Amount</form:label></td>
        <td><form:input path="amount" /></td>
    </tr>
    <form:hidden path="id" />
    <form:hidden path="orderId" />
    <tr>
        <td colspan="2">
            <input type="submit" name="action" value="Submit" class="btn btn-default"/>
            <input type="submit" name="action" value="Cancel" class="btn btn-default"/>
        </td>
    </tr>
</table>  
</form:form>
</body>
</html>
