<%--
  Created by IntelliJ IDEA.
  User: Rajesh Kumar
  Date: 5/30/13
  Time: 10:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <div style="text-align: center;"><h2>BULK EDIT PRODUCTS AND PRODUCT VARIANTS</h2></div>
      <s:form beanclass="com.hk.web.action.admin.catalog.product.BulkUploadRelatedProductAction">


       </s:form>
      </s:layout-component>
    </s:layout-render>