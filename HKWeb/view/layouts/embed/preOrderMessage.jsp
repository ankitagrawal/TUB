<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>

    <c:choose>
        <c:when test="${product.id == 'NUT1369' || product.id == 'NUT1600'}">
			<span style="font-size: 11px;"> Product is Out of Stock<br>
                Expected to arrive back in stock date is 
            <c:if test="${product.id == 'NUT1369'}">
                31st Jan, 2013
            </c:if>
                 <c:if test="${product.id == 'NUT1600'}">
                     15th Feb, 2013
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
