<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search Batches for UPC/VariantID">
<s:useActionBean beanclass="com.hk.web.action.admin.sku.SearchSkuBatchesAction" var="ssba"/>
  <s:layout-component name="content">
    <div style="margin-top:200px" height="500px" align="center">
      <s:form beanclass="com.hk.web.action.admin.inventory.DoomDayInventorySnapshotAction">
        <label><b>BULK PRODUCT?? IF YES QTY? DEFAULT 1</b></label>
        <br/><br/>
        <s:text name="qty" id="qty" style="font-size:40px; padding:20px;height:30px;width:500px;"/>
        <script language=javascript type=text/javascript>
          $('#qty').focus();
        </script>
        <br/>
        <br/>
        <s:hidden name="barcode"/>
        <s:submit name="saveSnapshot" value="SAVE"/>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>