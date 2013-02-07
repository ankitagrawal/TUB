<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@ page import="com.hk.domain.order.ShippingOrder" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%

    String shippingOrderId = request.getParameter("shippingOrderId");
    request.setAttribute("shippingOrderId", shippingOrderId);
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateReverseOrderAction" event="pre" var="reverseOrderAction"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pickup Service">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">
          $(document).ready(function() {

          });
    </script>

    </s:layout-component>


    <s:layout-component name="heading">Create Reverse Order</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.CreateReverseOrderAction">
          <s:errors/>
          <h4>Reverse Order Items:</h4>
          <c:forEach items="${reverseOrderAction.}" varStatus="ctr">
              <li>
                  
              </li>
          </c:forEach>

        <s:submit name="submit" value="Submit"/>
        </s:form>

    </s:layout-component>
</s:layout-render>


