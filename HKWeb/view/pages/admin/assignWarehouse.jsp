<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.AssignWarehouseAction" var="userBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="heading">${userBean.currentBreadcrumb.name}</s:layout-component>
  <s:layout-component name="content">

    <s:useActionBean beanclass="com.hk.web.action.admin.warehouse.AssignWarehouseAction" var="userBean"/>
    Name: ${userBean.user.name}<br/>
    Login: ${userBean.user.login}<br/>
    <s:form beanclass="com.hk.web.action.admin.warehouse.AssignWarehouseAction" method="post">
      <h2>Warehouses:</h2>
      <c:forEach items="${userBean.allWarehouses}" var="warehouse" varStatus="count">
          <s:radio name="userWarehouse" value="${warehouse.id}"
              checked="${(userBean.userWarehouse != null && warehouse.id == userBean.userWarehouse.id)}"/> ${warehouse.identifier} - ${warehouse.name}, ${warehouse.city}, ${warehouse.state}<br/>
      </c:forEach>
      <s:hidden name="user" value="${userBean.user.id}"/>
      <s:submit name="change" value="Change Warehouse"/>
    </s:form>
  </s:layout-component>

</s:layout-render>
