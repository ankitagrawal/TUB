<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search Batches for UPC/VariantID">
  <s:useActionBean beanclass="com.hk.web.action.admin.inventory.SearchHKBatchAction" var="ssba"/>
  <s:layout-component name="content">
    <table width="100%">
      <tr>
        <td width="50%" valign="top">
          <s:form beanclass="com.hk.web.action.admin.inventory.SearchHKBatchAction">
            <label>Search HK Batch</label>
            <br/><br/>
            <s:text name="hkBarcode" id="upc" style="font-size:16px; padding:5px;height:30px;width:300px;"/>
            <script language=javascript type=text/javascript>
              $('#upc').focus();
            </script>
            <br/>
            <br/>
            <s:submit name="showBatchInfo" value="Show Batch Info"/>
          </s:form>
        </td>
        <td valign="top">
          <c:choose>
            <c:when test="${ssba.skuGroup != null}">
              <c:set var="skuGroup" value="${ssba.skuGroup}"/>
              <c:set var="variant" value="${skuGroup.sku.productVariant}"/>
              <c:set var="product" value="${variant.product}"/>
              <h2>Batch Info of ${skuGroup.barcode}</h2>
              <table>
                <tr>
                  <td>Name:</td><td>${product.name}</td>
                </tr><tr>
                <td>Variant:</td><td>${variant.id}</td>
              </tr><tr>
                <td>Variant Options:</td><td>${variant.optionsSlashSeparated}</td>
              </tr><tr>
                <td>MRP:</td><td>${variant.markedPrice}</td>
              </tr><tr>
                <td>Batch:</td><td>${skuGroup.batchNumber}</td>
              </tr><tr>
                <td>Mfg. Date:</td><td>${skuGroup.mfgDate}</td>
              </tr><tr>
                <td>Exp. Date:</td><td>${skuGroup.expiryDate}</td>
              </tr><tr>
                <td>Inventory:</td><td>${fn:length(hk:getInStockSkuItems(skuGroup))}</td>
              </tr>
              </table>
            </c:when>
            <c:otherwise>
              <c:if test="${ssba.hkBarcode != null}">
                <h2>No such HK Batch with provided Barcode</h2>
              </c:if>
            </c:otherwise>
          </c:choose>
        </td></tr></table>
  </s:layout-component>
</s:layout-render>