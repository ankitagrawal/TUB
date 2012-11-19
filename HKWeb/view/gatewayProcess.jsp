<%@ page import="com.akube.framework.service.BasePaymentGatewayWrapper" %>
<%@ page import="com.akube.framework.service.PaymentGatewayWrapper" %>
<%@include file="/includes/_taglibInclude.jsp"%>
<html>
<head>
  <title>Proceeding to gateway</title>
  <jsp:include page="/includes/_style.jsp"/>
  <script language="javascript" type="text/javascript" src="${pageContext.servletContext.contextPath}/otherScripts/jquery-1.3.1.min.js"></script>
  <script language="javascript" type="text/javascript">
    $(document).ready(function(){
      $("#gatewayForm").submit();
    });
  </script>
</head>
<body>
<div id="container" style="margin-top:20px;">
  <div id="content" style="text-align:center; padding:30px;">
    <%

      BasePaymentGatewayWrapper paymentGateway = (BasePaymentGatewayWrapper) request.getAttribute("PaymentGatewayWrapper");       
      String gatewayUrl = "";
      if (!paymentGateway.getGatewayUrl().startsWith("http://") && !paymentGateway.getGatewayUrl().startsWith("https://")) {
        gatewayUrl = request.getContextPath()+paymentGateway.getGatewayUrl();
      } else {
        gatewayUrl = paymentGateway.getGatewayUrl();
      }
    %>
    <p class="notify" style="font-size:1.4em;">Proceeding to payment gateway....</p>
    <c:set var="paymentGateway" value="<%=paymentGateway%>"/>
    <form action="<%=gatewayUrl%>" method="POST" id="gatewayForm">
      <c:forEach items="${paymentGateway.parameters}" var="entry">
        <input type="hidden" name="${entry.key}" value="${entry.value}"/>
      </c:forEach>
    </form>
    </div>
  </div>
</body>
</html>