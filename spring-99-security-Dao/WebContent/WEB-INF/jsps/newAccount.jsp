<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>  
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
<h2> Create New Account</h2>
<sf:form method ="Post" action="${pageContext.request.contextPath}/createaccount" commandName="user">
 userName : <sf:input path="username" name="username"  type="text" value=""/> <br><sf:errors path="username" cssClass="error"> </sf:errors><br>
  Password : <sf:input path="password" name="password"  type="text" value=""/> <br><sf:errors path="password" cssClass="error"> </sf:errors><br>
 Confirm Password : <input type="text" name="confirmPassword" >
 Email :<sf:input path="email" name="email"  type="text" value=""/> <br><sf:errors path="email" cssClass="error"> </sf:errors><br>
 <input value="createadvert" type ="submit">
 </sf:form>
</body>
</html>