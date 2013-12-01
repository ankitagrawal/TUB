<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.SkuGroupAction" var="sgaBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Checkin">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="heading">Update SkuGroup</s:layout-component>
  <s:layout-component name="content">
    <div style="display:inline;float:left;">
      <h2>SkuGroup#${sgaBean.skuGroup.barcode}</h2>
      <s:form beanclass="com.hk.web.action.admin.inventory.SkuGroupAction">
        <s:hidden name="skuGroup" value="${sgaBean.skuGroup.id}"/>
        <s:hidden name="gatewayOrderId" value="${sgaBean.gatewayOrderId}"/>
        <table border="1">
          <tr>
            <td>Sku Group Barcode:</td>
            <td><s:text name="skuGroup.barcode" readonly="readonly"/></td>
          </tr>
		<tr>
			<td>Cost Price:</td>
			<td><s:text name="skuGroup.costPrice"/></td>
		</tr>
		<tr>
			<td>MRP:</td>
			<td><s:text name="skuGroup.mrp"/></td>
		</tr>
          <tr>
            <td>Batch Number:</td>
            <td><s:text name="skuGroup.batchNumber"/></td>
          </tr>
          <tr>
            <td>Mfg. Date:</td>
            <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="skuGroup.mfgDate"/></td>
          </tr>
          <tr>
            <td>Expiry Date:</td>
            <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="skuGroup.expiryDate"/></td>
          </tr>
        </table>

        <s:submit class="invCheckin" name="save" value="Save"/>
      </s:form>
      <span style="display:inline;float:right;"><h2><s:link beanclass="com.hk.web.action.admin.inventory.InventoryCheckoutAction">&lang;&lang;&lang;
	      <s:param name="gatewayOrderId" value="${sgaBean.gatewayOrderId}"/>
        Back to Inv Checkout</s:link></h2></span>
    </div>

  </s:layout-component>
</s:layout-render>