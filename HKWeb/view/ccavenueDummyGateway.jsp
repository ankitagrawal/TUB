<%@ page import="com.hk.manager.payment.CCAvenueDummyPaymentGatewayWrapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp"%>
<%
  String checksum = CCAvenueDummyPaymentGatewayWrapper.getResponseChecksum(
      request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_MerchantId),
      request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_OrderId),
      request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_Amount),
      request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_AuthDesc),
      CCAvenueDummyPaymentGatewayWrapper.workingKey
  );
%>
<html>
  <head>
    <title>CCAvenue Dummy gateway</title>
  </head>
  <body>
    <div>
      Rs. <%=request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_Amount)%>
      <s:form beanclass="com.hk.web.action.core.payment.gateway.test.CCAvenueDummyGatewaySendReceiveAction">
        <input type="hidden" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_OrderId%>" value="<%=request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_OrderId)%>"/>
        <input type="hidden" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_MerchantId%>" value="<%=request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_MerchantId)%>"/>
        <input type="hidden" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_MerchantParam%>" value="<%=request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_MerchantParam)%>"/>
        <input type="hidden" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_Checksum%>" value="<%=checksum%>"/>
        <p>
          <%=CCAvenueDummyPaymentGatewayWrapper.AuthDesc_Success%> <input type="radio" value="<%=CCAvenueDummyPaymentGatewayWrapper.AuthDesc_Success%>" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_AuthDesc%>"/> |
          <%=CCAvenueDummyPaymentGatewayWrapper.AuthDesc_Fail%> <input type="radio" value="<%=CCAvenueDummyPaymentGatewayWrapper.AuthDesc_Fail%>" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_AuthDesc%>"/> |
          <%=CCAvenueDummyPaymentGatewayWrapper.AuthDesc_PendingApproval%> <input type="radio" value="<%=CCAvenueDummyPaymentGatewayWrapper.AuthDesc_PendingApproval%>" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_AuthDesc%>"/>
        </p>
        <p>
          <input type="text" name="<%=CCAvenueDummyPaymentGatewayWrapper.param_Amount%>" value="<%=request.getParameter(CCAvenueDummyPaymentGatewayWrapper.param_Amount)%>"/>
        </p>
        <s:submit name="callback" value="Proceed"/>
      </s:form>
    </div>

  </body>
</html>