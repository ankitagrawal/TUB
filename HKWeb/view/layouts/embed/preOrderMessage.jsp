<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>

  <c:choose>
    <c:when test="${product.jit != null && product.jit && (product.id == 'NUT1600##' || product.id == 'NUT1599###')}">
			<span style="font-size: 11px;"> Product is Out of Stock<br>
                Expected back in stock date is
             <c:if test="${product.id == 'NUT1600##'}">
               5th April, 2013
             </c:if>
             <c:if test="${product.id == 'NUT1599##'}">
               4th March, 2013
             </c:if>
			 </span>
      <s:submit name="addToCart" value="Pre Order"
                class="addToCartButton cta button_green"/>
    </c:when>
    <c:otherwise>
      <s:submit name="addToCart" value="Place Order"
                class="addToCartButton cta button_green"/>
    </c:otherwise>
  </c:choose>


</s:layout-definition>
