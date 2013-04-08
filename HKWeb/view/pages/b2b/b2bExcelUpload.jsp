<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@ page import="com.hk.constants.core.PermissionConstants"%>
<%@ page import="com.hk.constants.inventory.EnumPurchaseOrderStatus"%>
<%@ page import="com.hk.pact.dao.MasterDataDao"%>
<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ include file="/includes/_taglibInclude.jsp"%>
<%@ include file="/layouts/_userData.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<s:useActionBean
	beanclass="com.hk.web.action.core.b2b.B2BAddToCartAction" var="atc" />
<s:useActionBean
	beanclass="com.hk.web.action.core.b2b.B2BCartAction" var="boa" />

<style>
.excelDataTable {
	border-collapse: collapse;
	border-spacing: 0;
	margin: 0 auto;
	width: 50%;
}

.excelDataTable th {
	font-size: 14px;
	text-align: center;
	font-weight: bold;
	margin: 10px 0 10px 0;
}

.excelDataTable tbody {
	font-size: 12px;
	text-align: center;
}

.excelDataTable tr {
	background-color: #F2F7FB;
	height: 30px;
	padding: 1px;
}

.excelDataTable td {
	text-align: center;
}

.b2bExcelDiv {
	font-weight: bold;
	margin: 20px 0;
	text-align: center;
	width: 960px;
}

.buttonPosition {
	position: relative;
	float: left;
	left: 40%;
}
</style>


<s:layout-render name="/layouts/b2bLayout.jsp">
	<s:layout-component name="checkoutStep">
		<s:form body="center" accept-charset="UTF-8" class="addToCartForm"
			beanclass="com.hk.web.action.core.b2b.B2BAddToCartAction">
			<div id="ExcelSheetDataDiv">
				<c:choose>
					<c:when test="${fn:length(boa.b2bProductListFromExcel) lt 1}">
						<div class="b2bExcelDiv">Seems like there was an issue in
							parsing the file</div>
							<div class="b2bExcelDiv">Recheck the format of the file.</div>
							
							<div class="buttons" style="margin-top: 70px;">
							<input type="submit" name="cancel" value="cancel"
								class="addToCartButton cta button_green buttonPosition" />


						</div>
							
					</c:when>

					<c:otherwise>

						<div class="b2bExcelDiv">Following are the details of the
							order</div>
						<table class="excelDataTable">
							<thead>
								<tr>
									<th>Sr. No.</th>
									<th>Product Variant</th>
									<th>Quantity</th>
								</tr>
							</thead>

							<tbody>
								<c:if test="${(boa.b2bProductListFromExcel)!=null}">
									<c:forEach items="${boa.b2bProductListFromExcel}"
										var="b2bOrderList" varStatus="item">
										<tr class="bodyTr" style="background-color: #F2F7FB">
											<td>${item.count}.</td>
											<td><input
												name="b2bProductList[${item.count-1}].productId"
												type="hidden" value="${b2bOrderList.productId}" />${b2bOrderList.productId}</td>
											<td><input
												name="b2bProductList[${item.count-1}].quantity"
												type="hidden" value="${b2bOrderList.quantity}" />${b2bOrderList.quantity}</td>
											<c:set var="newIndex" value="${item.count}" scope="page" />
									</c:forEach>
								</c:if>
							</tbody>
						</table>
						<c:if test="${fn:length(boa.b2bInvalidProductList) gt 0}">
							<div class="b2bExcelDiv">
								These products variants are invalid. Please check them and then
								upload again. <br>
								<c:forEach items="${boa.b2bInvalidProductList}"
									var="invalidVariants" varStatus="item">
	 ${invalidVariants.productId} , 
	 <c:if test="${item.count}>10">
									</c:if>
								</c:forEach>
							</div>
						</c:if>
						<div class="buttons" style="margin-top: 70px;">

							<c:if test="${fn:length(boa.b2bInvalidProductList) lt 1}">
								<input type="submit" name=b2bAddToCart value="Submit"
									class="addToCartButton cta button_green buttonPosition" />
							</c:if>
							<input type="submit" name="cancel" value="cancel"
								class="addToCartButton cta button_green buttonPosition" />


						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</s:form>
	</s:layout-component>
	<s:layout-component name="endScripts">
		<script type="text/javascript">
			 
			 $(document).ready(function() { 
		            // bind 'myForm' and provide a simple callback function 
		            $('.addToCartForm').ajaxForm(function(res1) { 
		            	if (res1.code == '<%=HealthkartResponse.STATUS_OK%>') {
							$('.message .line1').html("Your cart has been updated");
							$('#productsInCart').html(res1.data.itemsInCart);
							alert('Added To Cart');
						}
						else if(res1.code == "null_exception"){
							alert(res1.message);
						}
		                else if(res1.code == '<%=HealthkartResponse.STATUS_ERROR%>') {

						if (res1.data.notAvailable != null) {
							alert("Not available");
						} else {
							alert("Warning: Some of the given products are out of stock, So they were not added");
						}
					}
					var path = "${pageContext.request.contextPath}";
					location.replace(path + "/core/b2b/B2BBulkOrder.action");
				});

			});
		</script>
	</s:layout-component>
</s:layout-render>