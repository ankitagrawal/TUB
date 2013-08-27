<%@ taglib prefix="hk" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.constants.sku.EnumSkuItemStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="checkedInStatus" value="<%=EnumSkuItemStatus.Checked_IN.getId()%>"/>
<c:set var="bookedStatus" value="<%=EnumSkuItemStatus.BOOKED.getId()%>"/>
<c:set var="TempBookedStatus" value="<%=EnumSkuItemStatus.TEMP_BOOKED.getId()%>"/>
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
            <c:when test="${ssba.skuGroupList != null}">
	          <c:forEach items="${ssba.skuGroupList}" var="skuGroup">
	              <c:set var="variant" value="${skuGroup.sku.productVariant}"/>
	              <c:set var="product" value="${variant.product}"/>
                  <c:if test="${ssba.skuItemBarcode != null}">
                      <h2>Batch Info of ${ssba.skuItemBarcode.barcode}</h2>
                  </c:if>
                   <c:if test="${ssba.skuItemBarcode == null}">
                     <h2>Batch Info of ${skuGroup.barcode}</h2>
                  </c:if>

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
	                <td>Net Physical Inventory:</td><td>
	                ${fn:length(hk:getNetPhysicalAvailableStockSkuItems(skuGroup))}
                  </td>
	              </tr><tr>
	                <td>Net Available Unbooked Inventory (CHECKED IN):</td><td>
	                ${fn:length(hk:getInStockSkuItems(skuGroup))}
                  </td>
	              </tr>
                      <tr>
                       <c:set var="status" value="${ssba.skuItemBarcode.skuItemStatus.id}"/>
                        <c:if test="${ssba.skuItemBarcode != null  && status == checkedInStatus ||  status == TempBookedStatus || status == bookedStatus}">
                            <td> Item Available</td>
                        </c:if>
                       <c:if test="${ssba.skuItemBarcode != null  && !(status == checkedInStatus ||  status == TempBookedStatus || status == bookedStatus)}">                            
                            <td> Item Not Available</td>
                        </c:if>

                      </tr>
	              </table>
	            </c:forEach>
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