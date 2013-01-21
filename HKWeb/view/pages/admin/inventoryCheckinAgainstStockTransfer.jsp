<%@ page import="com.hk.constants.inventory.EnumStockTransferStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" var="ica"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Stock Transfer Inventory Checkin">
	<c:set var="stOutCompleted" value="<%=EnumStockTransferStatus.Stock_Transfer_Out_Completed.getId()%>" />
	<c:set var="stCheckinInProcess" value="<%=EnumStockTransferStatus.Stock_Transfer_CheckIn_In_Process.getId()%>" />
    <jsp:useBean id="now" class="java.util.Date" scope="request"/>
    <s:layout-component name="htmlHead">
        <%
            String messageColor = request.getParameter("messageColor");
            pageContext.setAttribute("messageColor", messageColor);
        %>
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
        <script type="text/javascript">
            $(document).ready(function() {

                $('.alert').hide();

                $('#productVariantBarcode').focus();
                $('#productVariantBarcode').keydown(function() {
                    $('.alertST').hide();
                });

                if ($('#messageColorParam').val() == "green") {
                    $('.alertST').find('li').css('font-size', '30px').css('color', 'green');
                } else {
                    $('.alertST').find('li').css('font-size', '30px').css('color', 'red');
                }

                $('#stForm2').submit(function() {
                    var pvb = $('#productVariantBarcode').val();
                    if (pvb == null || pvb == "") {
                        alert("Value can't be Empty");
                        return false;
                    }
                });
                $('#productVariantBarcode').change(function() {

                    var formName = $('#stForm2');
                    var formURL = formName.attr('action');
                    formName.attr('action', formURL + "?stockTransfer=" + ${ica.stockTransfer.id} + "&saveStockTransfer=");
                    formName.submit();
                });

            });
        </script>
    </s:layout-component>

    <s:layout-component name="heading">Inventory Checkin Against Stock Transfer</s:layout-component>
    <s:layout-component name="content">
        <div style="display:inline;float:left;">
            <h2>Item Checkin against Stock Transfer#${ica.stockTransfer.id}</h2>
	        <s:form beanclass="com.hk.web.action.admin.inventory.StockTransferAction">
	        <div>
		        <s:submit name="closeStockTransfer" value="Close Stock Transfer"
		                  id="markAsStockTransferOutCompleted"/>
		        <s:hidden name="stockTransfer" value="${ica.stockTransfer.id}" />
	        </div>
	        </s:form>
            <input type="hidden" id="messageColorParam" value="${messageColor}">

            <div class="alertST messages"><s:messages key="generalMessages"/></div>
            <c:if test="${ica.stockTransfer.id != null}">
	            <c:if test="${ica.stockTransfer.stockTransferStatus.id == stOutCompleted || ica.stockTransfer.stockTransferStatus.id == stCheckinInProcess}">
                <s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" id="stForm2">
                    <fieldset class="right_label">
                        <legend>Scan Barcode:</legend>
                        <ul>
                            <li>
                                <s:label name="barcode">Product Variant Barcode</s:label>
                                <s:text name="productVariantBarcode" id="productVariantBarcode"/>
                            </li>
                            <li></li>
                        </ul>
                    </fieldset>
                </s:form>
	            </c:if>
                <table border="1">
                    <thead>
                    <tr>
                        <th>VariantID</th>
                        <th>Details</th>
                        <th>Checkedout Qty</th>
                        <th>CheckedIN Qty</th>
                        <th>Cost Price<br/>(Without TAX)</th>
                        <th>MRP</th>
                        <th>Batch Number</th>
                        <th>Mfg. Date<br/>(yyyy-MM-dd)</th>
                        <th>Exp. Date<br/>(yyyy-MM-dd)</th>
                    </tr>
                    </thead>
                    <tbody id="stTable">
                    <c:forEach var="stockTransferLineItem" items="${ica.stockTransfer.stockTransferLineItems}"
                               varStatus="ctr">
                        <c:set var="productVariant" value="${stockTransferLineItem.sku.productVariant}"/>
                        <c:set var="checkedOutSkuGroup" value="${stockTransferLineItem.checkedOutSkuGroup}"/>
                        <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                            <td>
                                    ${productVariant.id}
                            </td>
                            <td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
                            </td>
                            <td> ${stockTransferLineItem.checkedoutQty}
                            </td>
                            <td> ${stockTransferLineItem.checkedinQty}
                            </td>
                            <td>${checkedOutSkuGroup.costPrice}
                            </td>
                            <td> ${checkedOutSkuGroup.mrp}
                            </td>
                            <td>${checkedOutSkuGroup.batchNumber}</td>
                            <td>
                                <fmt:formatDate value="${checkedOutSkuGroup.mfgDate}" type="both"/></td>
                            <td>
                                <fmt:formatDate value="${checkedOutSkuGroup.expiryDate}" type="both"/></td>
                        </tr>

                    </c:forEach>
                    </tbody>
                </table>
            </c:if>

      <span style="display:inline;float:right;"><h2><s:link
              beanclass="com.hk.web.action.admin.inventory.StockTransferAction">&lang;&lang;&lang;
          Back to Stock Transfer List</s:link></h2></span>
        </div>

    </s:layout-component>
</s:layout-render>