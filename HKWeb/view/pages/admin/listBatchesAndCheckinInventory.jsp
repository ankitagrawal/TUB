<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ListBatchesAndCheckinInventory" var="ica"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Available Batch List and Inventory Checkin">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="heading">Available Batch List of UPC/VariantID# ${ica.upc}</s:layout-component>
  <s:layout-component name="content">
    <div>
      <b>${ica.productVariant.product.name}</b>,
      <span class="sml gry">${ica.productVariant.optionsCommaSeparated}</span>
      <strong>MRP=${ica.productVariant.markedPrice}</strong>
    </div>
    <br/>

    <div style="display:inline;float:left;">
      <c:choose>
        <c:when test="${!empty ica.availableSkuGroups}">
          <%--<h2>In-stock Batches and Units.</h2>--%>
          Net Inventory (excluding store)= <strong>${hk:netInventoryAtServiceableWarehouses(ica.productVariant)}</strong>
          <hr/>
          <table>
            <thead>
            <tr>
              <th>GRN/RV No.</th>
              <th>WH</th>
              <th>Batch No.</th>
              <th>Mfg. Date</th>
              <th>Expiry Date</th>
              <th>Cost Price</th>
              <th>MRP</th>
              <th>Checkin Date</th>
              <th>Checked-In Units</th>
              <th>In-stock Units</th>
              <th></th>

            </tr>
            </thead>
            <c:forEach items="${ica.availableSkuGroups}" var="skuGroup" varStatus="ctr">
	           <c:set var="batchInv" value="${fn:length(hk:getInStockSkuItems(skuGroup))}"/>
	              <c:choose>
		              <c:when test="${batchInv > 0 && skuGroup.mrp < ica.productVariant.markedPrice}">
			              <tr style="background:lightpink">
		              </c:when>
		              <c:otherwise>
			              <tr>
		              </c:otherwise>
	              </c:choose>
                <td>
                  <c:if test="${skuGroup.goodsReceivedNote != null}">
                    <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view" target="_blank">
                      <s:param name="grn" value="${skuGroup.goodsReceivedNote.id}"/>
                      ${skuGroup.goodsReceivedNote.id}
                    </s:link>
                  </c:if>
                  <c:if test="${skuGroup.reconciliationVoucher != null}">
                    <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" event="view" target="_blank">
                      <s:param name="reconciliationVoucher" value="${skuGroup.reconciliationVoucher.id}"/>
                      ${skuGroup.reconciliationVoucher.id}
                    </s:link>
                  </c:if>

                </td>
                <td>${skuGroup.sku.warehouse.identifier}</td>
                <td>${skuGroup.batchNumber}</td>
                <td><fmt:formatDate value="${skuGroup.mfgDate}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${skuGroup.expiryDate}" pattern="dd/MM/yyyy"/></td>
	            <td>${skuGroup.costPrice}</td>
	            <td>${skuGroup.mrp}</td>
                <td><fmt:formatDate value="${skuGroup.createDate}" pattern="dd/MM/yyyy"/></td>
                <td>${fn:length(skuGroup.skuItems)}</td>
	            <td>${batchInv}</td>
	            <td>
		            <shiro:hasRole name="<%=RoleConstants.GOD%>">
		              <s:link beanclass="com.hk.web.action.admin.inventory.SkuGroupAction" target="_blank">
			              <s:param name="skuGroup" value="${skuGroup.id}"/>
			              <img src="${pageContext.request.contextPath}/images/edit.gif" alt="Edit Batch"/>
		              </s:link>
		            </shiro:hasRole>
	              </td>                
              </tr>
            </c:forEach>
            <%--<tr>
              <td colspan="5" align="right" style="font-weight:bold;">Total</td>
              <td style="font-weight:bold;"><h2>${hk:netInventory(ica.productVariant)}</h2></td>
              <td></td>
              <td></td>
            </tr>--%>
          </table>
        </c:when>
        <c:otherwise>
          <h2>No In-stock Batch. Net Inventory (excluding store)= <h2>${hk:netInventoryAtServiceableWarehouses(ica.productVariant)}</h2></h2>
          <br/>
        </c:otherwise>
      </c:choose>

      <div align="right">
        <s:link beanclass="com.hk.web.action.admin.sku.SearchSkuBatchesAction" style="font-size:1.2em">
          <-- Back to Search Sku Batches</s:link>
      </div>
      <br/>

      <c:if test="${!empty ica.allSkuGroups}">

        <div style="display:inline;float:left;font-size:.7em">
          <h2>History of All Checked-In Batches</h2>
          <hr/>
          <table>
            <thead>
            <tr>
              <th>GRN/RV No.</th>
              <th>WH</th>
              <th>Batch No.</th>
              <th>Mfg. Date</th>
              <th>Expiry Date</th>
              <th>Cost Price</th>
              <th>MRP</th>
              <th>Checkin Date</th>
              <th>Checked-In Units <br/>
                <span class="sml gry">0 means deleted all SKUs to adjust inventory</span>
              </th>
              <th>In-stock Units</th>


            </tr>
            </thead>
            <c:forEach items="${ica.allSkuGroups}" var="skuGroup" varStatus="ctr">
              <tr>
                <td>
                  <c:if test="${skuGroup.goodsReceivedNote != null}">
                    <s:link beanclass="com.hk.web.action.admin.inventory.GRNAction" event="view" target="_blank">
                      <s:param name="grn" value="${skuGroup.goodsReceivedNote.id}"/>
                      ${skuGroup.goodsReceivedNote.id}
                    </s:link>
                  </c:if>
                  <c:if test="${skuGroup.reconciliationVoucher != null}">
                    <s:link beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" event="view" target="_blank">
                      <s:param name="reconciliationVoucher" value="${skuGroup.reconciliationVoucher.id}"/>
                      ${skuGroup.reconciliationVoucher.id}
                    </s:link>
                  </c:if> 
                </td>
                <td>${skuGroup.sku.warehouse.identifier}</td>
                <td>${skuGroup.batchNumber}</td>
                <td><fmt:formatDate value="${skuGroup.mfgDate}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${skuGroup.expiryDate}" pattern="dd/MM/yyyy"/></td>
	            <td>${skuGroup.costPrice}</td>
	            <td>${skuGroup.mrp}</td>
                <td><fmt:formatDate value="${skuGroup.createDate}" pattern="dd/MM/yyyy"/></td>
                <td>${fn:length(skuGroup.skuItems)}</td>
                <td>${fn:length(hk:getInStockSkuItems(skuGroup))}</td>
              </tr>
            </c:forEach>

          </table>
        </div>
      </c:if>
    </div>

  </s:layout-component>
</s:layout-render>