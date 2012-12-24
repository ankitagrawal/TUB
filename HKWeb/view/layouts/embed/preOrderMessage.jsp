<%@include file="/includes/_taglibInclude.jsp"%>
<s:layout-definition>

	<c:choose>
		<c:when test="${product.id == 'NUT1600'}">
			<span style="font-size: 11px;"> Product is Out of Stock<br>
			Expected to arrive back in stock by Dec 28, 2012 </span>
			<s:submit name="addToCart" value="Pre Order"
				class="addToCartButton cta button_green" />
		</c:when>
		<c:otherwise>
			<s:submit name="addToCart" value="Place Order"
				class="addToCartButton cta button_green" />
		</c:otherwise>
	</c:choose>


</s:layout-definition>
