<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search Order and Checkout Inventory">
  <s:layout-component name="content">
    <div height="500px" align="center">
      <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">
        <label>Search Order:</label>
        <br/><br/>
        <s:text name="gatewayOrderId" id="gatewayOrderId" style="font-size:16px; padding:5px;height:30px;width:300px;"/>
        <script language=javascript type=text/javascript>
          $('#gatewayOrderId').focus();
        </script>
        <br/>
        <br/>
        <s:submit name="checkout" value="Proceed to Checkout"/>
      </s:form>
    </div>
    
  </s:layout-component>
</s:layout-render>