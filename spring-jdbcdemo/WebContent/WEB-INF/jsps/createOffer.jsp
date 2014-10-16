<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> craete Offers</title>
</head>
<body>

<form method ="get" action="${pageContext.request.contextPath}/docreate">
 Name : <input name="name" type="text" value=""/> <br>
 Email :<input name="email" type="text" value=""> <br>
 <input value="createadvert" type ="submit">
 </form>
</body>
</html>