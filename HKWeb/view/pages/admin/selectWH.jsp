<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp">
  <%
      WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
        pageContext.setAttribute("whList", warehouseService.getAllWarehouses());
  %>

  <s:layout-component name="rhsContent">
    <h1>Select a Warehouse Location </h1>
     <div style="height:300px;width:500px;padding:100px" align="center">
      <s:form beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" id="selectWHForm">
        <s:select name="setWarehouse" style="height:50px;font-size:1.4em;padding:5px;">
          <c:forEach items="${whList}" var="wh">
            <s:option value="${wh.id}">${wh.name}</s:option>
          </c:forEach>
        </s:select>
        <s:submit class="button_orange" name="bindUserWithWarehouse" value="Save"/>
      </s:form>

    </div>
  </s:layout-component>

</s:layout-render>
