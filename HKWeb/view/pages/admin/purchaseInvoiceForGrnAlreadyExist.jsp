<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Purchase Invoice For GRN Already Exists.">
  <s:layout-component name="heading">
	    Purchase Invoice For GRN Already Exists.
	  </s:layout-component>
	<br/><br/><br/>
	<s:layout-component name="content">
		<s:form beanclass="web.action.admin.PurchaseInvoiceAction">
		<s:submit name="pre" value="Back"/>
		</s:form>
		</s:layout-component>
	</s:layout-render>