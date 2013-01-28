<%@ taglib prefix="g" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.order.Order" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <%
        String pageType = (String)pageContext.getAttribute("pageType");
        pageContext.setAttribute("pageType", pageType);

        String categories = (String)pageContext.getAttribute("categories");
        pageContext.setAttribute("categories", categories);

        String topLevelCategory = (String)pageContext.getAttribute("topLevelCategory");
        pageContext.setAttribute("topLevelCategory", topLevelCategory);

        if(pageContext.getAttribute("order") != null){
            Order order = (Order)pageContext.getAttribute("order");
            pageContext.setAttribute("order", order);
        }

        if(pageContext.getAttribute("googleProductId") != null){
            String productId = (String)pageContext.getAttribute("googleProductId");
            pageContext.setAttribute("googleProductId", productId);
        }
    %>

    <input type="hidden" value="${pageType}" id="pageType">

    <input type="hidden" value="${topLevelCategory}" id="topLevelCategory">
    <input type="hidden" value="${googleProductId}" id="googleProductId">

    <c:set var="excludeCategories" value="rehabilitation-supports,personal-hygiene,breast-cancer-home-test,women-nutrition"/>
    <c:set var="googleProductsSelected" value=""/>
    <c:set var="canGoogleRemarket" value="false"/>
    <c:if test = "${pageType == 'home'}">
        <c:set var="canGoogleRemarket" value="true"/>
    </c:if>

    <c:if test = "${pageType == 'category'}">
        <c:if test = "${topLevelCategory == 'beauty' || topLevelCategory == 'sports'}">
            <c:choose>
                <c:when test="${hk:urlContainsAnyCategory(categories, excludeCategories)}">
                    <%--<script type="text/javascript">
                        alert('false')
                    </script>--%>
                </c:when>
                <c:otherwise>
                    <c:set var="canGoogleRemarket" value="true"/>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:if>

    <c:if test = "${pageType == 'product'}">
        <c:if test = "${topLevelCategory == 'beauty' || topLevelCategory == 'sports'}">
            <c:choose>
                <c:when test="${hk:urlContainsAnyCategory(categories, excludeCategories)}">

                </c:when>
                <c:otherwise>
                    <c:set var="canGoogleRemarket" value="true"/>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:if>

    <c:choose>
    <c:when test = "${pageType == 'cart' || pageType == 'purchase'}">
        <c:forEach items="${order.exclusivelyProductCartLineItems}" var="cartLineItem" varStatus="ctr">
            <c:if test="${!cartLineItem.productVariant.product.googleAdDisallowed}">
                <c:if test = "${cartLineItem.productVariant.product.primaryCategory == 'beauty' || cartLineItem.productVariant.product.primaryCategory == 'sports'}">
                    <c:choose>
                        <c:when test = "${hk:hasProductAnyCategory(cartLineItem.productVariant.product, excludeCategories)}">

                        </c:when>
                        <c:otherwise>
                            <c:set var="googleProductsSelected" value="${googleProductsSelected},'${cartLineItem.productVariant.product.id}'"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </c:if>
        </c:forEach>
        <c:forEach items="${order.exclusivelyComboCartLineItems}" var="cartLineItem" varStatus="ctr">
            <c:if test="${!cartLineItem.productVariant.product.googleAdDisallowed}">
                <c:if test = "${cartLineItem.productVariant.product.primaryCategory == 'beauty' || cartLineItem.productVariant.product.primaryCategory == 'sports'}">
                    <c:choose>
                        <c:when test = "${hk:hasProductAnyCategory(cartLineItem.productVariant.product, excludeCategories)}">

                        </c:when>
                        <c:otherwise>
                            <c:set var="googleProductsSelected" value="${googleProductsSelected},'${cartLineItem.productVariant.product.id}'"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </c:if>
        </c:forEach>
    </c:when>
    </c:choose>

    <input type="hidden" value="${googleProductsSelected}" id="cartProductId">

    <%--<c:otherwise>--%>
    <g:if test="${canGoogleRemarket == 'true'}">

        <script type="text/javascript">
            var pathName = window.location.pathname;
            var pathNames = pathName.split("/");

            var hkPageType = $("#pageType").val();
            var prodId = '';
            var pCategory = '';
            var cartProducts = new Array();
            var canSet = true;

            if(hkPageType == 'category'){
                pCategory = $("#topLevelCategory").val();
                /*if((pCategory == "beauty") || (pCategory=="sports")){
                 canSet = true;
                 }*/

                if(pathNames.length > 3){
                    var thirdLevelSlug = pathNames[4];
                    //google disallowed
                    switch (thirdLevelSlug){
                        case "rehabilitation-supports":
                        case "personal-hygiene":
                        case "breast-cancer-home-test":
                        case "women-nutrition":
                            canSet = false;
                            break;
                        default :
                            break;
                    }
                }

            }
            else if(hkPageType == 'product'){
                prodId = $("#googleProductId").val();
                canSet = true;
            }else if(hkPageType == 'cart' || hkPageType == 'purchase'){
                prodId = $("#cartProductId").val();
                if(prodId && prodId.length > 0){
                    var firstChar = prodId.charAt(0);
                    prodId = prodId.substr(1,prodId.length - 1);
                }
            }else if(hkPageType == 'home'){
            }
            if(canSet)
            {
                alert( 'prodid:' + "'" + prodId + "'" + 'pagetype:' +  "'" + hkPageType + "'" + 'pCat:' +  "'" + pCategory + "'");
                var google_tag_params = {
                    prodid: "'" + prodId + "'" ,
                    pagetype: "'" + hkPageType + "'",
                    pCat: "'" + pCategory + "'"
                };
            }
        </script>

        <script type="text/javascript">
            /* <![CDATA[ */
            var google_conversion_id = 1001307632;
            var google_conversion_label = "wwUqCJDx5AQQ8Pu63QM";
            var google_custom_params = window.google_tag_params;
            var google_remarketing_only = true;
            /* ]]> */
        </script>
        <script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
        </script>
        <noscript>
            <div style="display:inline;">
                <img height="1" width="1" style="border-style:none;" alt="" src="//googleads.g.doubleclick.net/pagead/viewthroughconversion/1001307632/?value=0&amp;label=wwUqCJDx5AQQ8Pu63QM&amp;guid=ON&amp;script=0"/>
            </div>
        </noscript>
    </g:if>
    <%--</c:otherwise>--%>
</s:layout-definition>
