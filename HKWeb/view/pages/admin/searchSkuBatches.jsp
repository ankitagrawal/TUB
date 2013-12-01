<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search Batches for UPC/VariantID">
<s:useActionBean beanclass="com.hk.web.action.admin.sku.SearchSkuBatchesAction" var="ssba"/>
  <s:layout-component name="content">
    <div height="500px" align="center">
      <s:form beanclass="com.hk.web.action.admin.sku.SearchSkuBatchesAction">
        <label>Search Batches for UPC/VariantID</label>
        <br/><br/>
        <s:text name="upc" id="upc" style="font-size:16px; padding:5px;height:30px;width:300px;"/>
        <script language=javascript type=text/javascript>
          $('#upc').focus();
        </script>
        <br/>
        <br/>
        <s:submit name="showBatches" value="Show Available Batches"/>
        <s:submit name="findProductVariantByUpc" value="PV by UPC"/>
      </s:form>
    </div>
    <div style="margin-top:100px">
    <table>
    <c:forEach items="${ssba.productVariants}" var="productVariant">
      <tr>
        <td>${productVariant.id}</td>
        <td>${productVariant.product.name}</td>
        <td>${productVariant.optionsCommaSeparated}</td>
        <td>${productVariant.variantName}</td>
      </tr>
    </c:forEach>
    </table>
    </div>
  </s:layout-component>
</s:layout-render>