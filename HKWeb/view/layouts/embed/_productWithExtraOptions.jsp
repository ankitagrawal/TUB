<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
	<%
		Product product = (Product) pageContext.getAttribute("product");
		pageContext.setAttribute("product", product);
	%>

	<s:form beanclass="com.hk.web.action.core.cart.AddToCartWithExtraOptionsAction" class="addToCartForm2">
		<c:set value="${product.productVariants[0]}" var="variant"/>
		<div class='variants'>
			<div style="font-weight:bold;">
				<c:if test="${hk:isNotBlank(variant.optionsCommaSeparated)}">
					${variant.optionsCommaSeparated}
				</c:if>
			</div>
			<div class='prod_table' style="padding:10px;width:90%">

				<div class="checkboxError" style="margin-bottom:20px; font-size:1.2em; color:salmon;"></div>
				<table width="100%">
					<tr>
						<th></th>
						<c:forEach items="${variant.productExtraOptions}"
						           var="headerExtraOption">
							<th><strong>${headerExtraOption.name}</strong></th>
						</c:forEach>
					</tr>
					<tr height="50px;">
						<s:hidden name="productLineItemWithExtraOptionsDtos[0].productVariant"
						          value="${variant.id}"/>
						<td>
							<strong><s:checkbox
									name="productLineItemWithExtraOptionsDtos[0].selected"
									class="checkbox"/>Left(OS)</strong>
						</td>
						<s:hidden
								name="productLineItemWithExtraOptionsDtos[1].productVariant.qty" value="1"/>
						<c:forEach items="${variant.productExtraOptions}" var="extraOption"
						           varStatus="extraOptionCtr">
							<td>

								<s:hidden
										name="productLineItemWithExtraOptionsDtos[0].extraOptions[${extraOptionCtr.index}].name"
										value="${extraOption.name}"/>
								<s:select
										name="productLineItemWithExtraOptionsDtos[0].extraOptions[${extraOptionCtr.index}].value">
									<c:forTokens items="${extraOption.value}" delims="," var="option">
										<s:option value="${option}">${option}</s:option>
									</c:forTokens>
								</s:select>
							</td>
						</c:forEach>
					</tr>

					<tr height="50px;">
						<s:hidden name="productLineItemWithExtraOptionsDtos[1].productVariant"
						          value="${variant.id}"/>
						<td>
							<strong><s:checkbox
									name="productLineItemWithExtraOptionsDtos[1].selected"
									class="checkbox"/>Right(OD)</strong>
						</td>

						<s:hidden
								name="productLineItemWithExtraOptionsDtos[1].productVariant.qty" value="1"/>

						<c:forEach items="${variant.productExtraOptions}" var="extraOption"
						           varStatus="extraOptionCtr">
							<td>

								<s:hidden
										name="productLineItemWithExtraOptionsDtos[1].extraOptions[${extraOptionCtr.index}].name"
										value="${extraOption.name}"/>
								<s:select
										name="productLineItemWithExtraOptionsDtos[1].extraOptions[${extraOptionCtr.index}].value">
									<c:forTokens items="${extraOption.value}" delims="," var="option">
										<s:option value="${option}">${option}</s:option>
									</c:forTokens>
								</s:select>
							</td>
						</c:forEach>
					</tr>

					<tr>
						<td></td>
						<c:forEach items="${variant.productExtraOptions}">
							<td></td>
						</c:forEach>
					</tr>
				</table>
			</div>
		</div>

		<div class="buy_prod">

			<div class="left_col">
				<div class='prices' style="font-size: 14px;">
					<c:if test="${variant.discountPercent > 0}">
						<div class='cut' style="font-size: 14px;">
                <span class='num' style="font-size: 14px;">
                  Rs <fmt:formatNumber value="${variant.markedPrice}" maxFractionDigits="0"/>
                </span>
						</div>
						<div class='hk' style="font-size: 16px;">
							Our Price
                <span class='num' style="font-size: 20px;">
                  Rs <fmt:formatNumber
		                value="${hk:getApplicableOfferPrice(variant)+ hk:getPostpaidAmount(variant)}"
		                maxFractionDigits="0"/>
                </span>
						</div>
						<div class="special green" style="font-size: 14px;">
							you save
                                <span style="font-weight: bold;"><fmt:formatNumber
		                                value="${variant.discountPercent*100}"
		                                maxFractionDigits="0"/>%</span>
						</div>
					</c:if>
					<c:if test="${variant.discountPercent == 0}">
						<div class='hk' style="font-size: 16px;">
							Our Price
                <span class='num' style="font-size: 20px;">
                  Rs <fmt:formatNumber
		                value="${hk:getApplicableOfferPrice(variant) + hk:getPostpaidAmount(variant)}"
		                maxFractionDigits="0"/>
                </span>
						</div>
					</c:if>
					
				</div>


			</div>
			<div class="right_col">
				<c:if test="${variant.freeProductVariant != null}">
					<p style="font-weight:bold;font-size:1.0em">+ FREE!! ${variant.freeProductVariant.product.name}</p>
				</c:if>
				<s:submit name="addToCart" value="Place Order"
				          class="addToCartButton cta button_green"/>
			</div>
		</div>
	</s:form>

	<script type="text/javascript">
		$(document).ready(function() {
			function _addToCart2(res) {
				if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
					$('.message .line1').html("<strong>" + res.data.name + "</strong> is added to your shopping cart");
				} else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
					$('#cart_error1').html(getErrorHtmlFromJsonResponse(res)).slowFade(3000, 2000);
				} else if (res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>') {
					window.location.replace(res.data.url);
				}
				$('.progressLoader').hide();
			}

			$('.addToCartForm2').ajaxForm({dataType: 'json', success: _addToCart2});
		});
		validateCheckbox = 1;
	</script>
</s:layout-definition>