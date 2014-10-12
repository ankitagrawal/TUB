<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<c:out value="${name}"></c:out>
<sql:query var="rs" dataSource="jdbc/spring">
select id, start_slab, end_slab from emp
</sql:query>


<c:forEach var="row" items="${rs.rows}">
    id ${row.id}<br/>
    slab ${row.start_slab}<br/>
</c:forEach>

</body>
</html>