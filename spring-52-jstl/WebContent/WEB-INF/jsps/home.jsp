<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
   hi
<p>   <%=session.getAttribute("name") %> </p>

<p>  request <%=request.getAttribute("name") %> </p>

<p>  request using el ${river} </p>

<p>  request using el ${name} </p>
<c:out value="${name}"></c:out>
</body>
</html>