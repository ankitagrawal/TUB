<%@ taglib prefix="g" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.order.Order" %>
<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <%
        if (pageContext.getAttribute("pageType") != null) {
            String pageType = (String) pageContext.getAttribute("pageType");
            pageContext.setAttribute("pageType", pageType);
        }

        if (pageContext.getAttribute("categories") != null) {
            String categories = (String) pageContext.getAttribute("categories");
            pageContext.setAttribute("categories", categories);
        }

        if (pageContext.getAttribute("topLevelCategory") != null) {
            String topLevelCategory = (String) pageContext.getAttribute("topLevelCategory");
            pageContext.setAttribute("topLevelCategory", topLevelCategory);
        }

        if (pageContext.getAttribute("order") != null) {
            Order order = (Order) pageContext.getAttribute("order");
            pageContext.setAttribute("order", order);
        }

        if (pageContext.getAttribute("googleProduct") != null) {
            Product product = (Product) pageContext.getAttribute("googleProduct");
            pageContext.setAttribute("googleProduct", product);
        }
    %>
    <input type="hidden" value="${pageType}" id="pageType">
    <input type="hidden" value="${topLevelCategory}" id="topLevelCategory">
    <input type="hidden" value="${googleProduct.id}" id="googleProductId">

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
                    <c:if test="${!googleProduct.googleAdDisallowed}">
                        <c:set var="canGoogleRemarket" value="true"/>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:if>

    <c:choose>
       <%-- <c:when test = "${pageType == 'cart' || pageType == 'purchase'}">
            <script type="text/javascript">
                alert('pageType=${pageType}');
            </script>
        </c:when>--%>
    <c:when test = "${pageType == 'cart' || pageType == 'purchase'}">
        <c:forEach items="${order.exclusivelyProductCartLineItems}" var="cartLineItem" varStatus="ctr">
            <c:if test="${!cartLineItem.productVariant.product.googleAdDisallowed}">
                <c:if test = "${cartLineItem.productVariant.product.primaryCategory == 'beauty' || cartLineItem.productVariant.product.primaryCategory == 'sports'}">
                    <c:choose>
                        <c:when test = "${hk:hasProductAnyCategory(cartLineItem.productVariant.product, excludeCategories)}">

                        </c:when>
                        <c:otherwise>
                            <c:set var="googleProductsSelected" value="${googleProductsSelected},${cartLineItem.productVariant.product.id}"/>
                            <c:set var="canGoogleRemarket" value="true"/>
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
                            <c:set var="googleProductsSelected" value="${googleProductsSelected},${cartLineItem.productVariant.product.id}"/>
                            <c:set var="canGoogleRemarket" value="true"/>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </c:if>
        </c:forEach>
    </c:when>
    </c:choose>

    <input type="hidden" value="${googleProductsSelected}" id="cartProductId">

    <%--<c:otherwise>--%>
    <c:if test="${canGoogleRemarket == 'true'}">

        <!--------------------------------------------------------
        PLEASE INCLUDE THIS COMMENT ON THE WEB PAGE WITH THE TAG
        Remarketing tags may not be associated with personally identifiable information
        or placed on pages related to sensitive categories.  For more information on
        this, see http://google.com/ads/remarketingsetup
        ----------------------------------------------------------->
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
                pCategory = $("#secondaryLevelCategory").val();
                canSet = true;
            }else if(hkPageType == 'cart' || hkPageType == 'purchase'){
                prodId = $("#cartProductId").val();
                if(prodId && prodId.length > 0){
                    var firstChar = prodId.charAt(0);
                    prodId = prodId.substr(1,prodId.length - 1);
                }
            }else if(hkPageType == 'home'){
            }
            //if(canSet)
            {
                //alert( 'prodid:' +  prodId + 'pagetype:'  + hkPageType  + 'pCat:' + pCategory);
                var google_tag_params = {
                    prodid:  prodId.split(','),
                    pagetype: hkPageType,
                    pcat: pCategory,
                    use_case: 'retail'
                };
            }
        </script>

        <script type="text/javascript">
            /* <![CDATA[ */
            var google_conversion_id = 992091386;
            var google_conversion_label = "NUAUCK7figQQ-rmI2QM";
            var google_custom_params = window.google_tag_params;
            var google_remarketing_only = true;
            /* ]]> */
        </script>
        <script type="text/javascript">
            /* <![CDATA[ */
            var google_conversion_id = 988370835;
            var google_conversion_label = "6HxaCN3_1wQQk6-l1wM";
            var google_custom_params = window.google_tag_params;
            var google_remarketing_only = true;
            /* ]]> */
        </script>
        <script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
        </script>
        <noscript>
            <div style="display:inline;">
                <img height="1" width="1" style="border-style:none;" alt="" src="//googleads.g.doubleclick.net/pagead/viewthroughconversion/992091386/?value=0&amp;label=NUAUCK7figQQ-rmI2QM&amp;guid=ON&amp;script=0"/>
            </div>
        </noscript>
        
        <c:if test = "${topLevelCategory == 'eye'}">
        
        <!-- Google Code for Eye Master List -->
		<!-- Remarketing tags may not be associated with personally identifiable information or placed on pages related to sensitive categories. For instructions on adding this tag and more information on the above requirements, read the setup guide: google.com/ads/remarketingsetup -->
		<script type="text/javascript">
			/* <![CDATA[ */
			var google_conversion_id = 1011104799;
			var google_conversion_label = "LaMfCMGthAUQn_iQ4gM";
			var google_custom_params = window.google_tag_params;
			var google_remarketing_only = true;
			/* ]]> */
		</script>
			<script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
			</script>
		<noscript>
			<div style="display:inline;">
				<img height="1" width="1" style="border-style:none;" alt="" src="//googleads.g.doubleclick.net/pagead/viewthroughconversion/1011104799/?value=0&amp;label=LaMfCMGthAUQn_iQ4gM&amp;guid=ON&amp;script=0"/>
			</div>
	    </noscript>
        </c:if>
        
        <c:if test = "${topLevelCategory == 'sports' || topLevelCategory == 'beauty'}">
        
        <!-- Google Code for Sports Master List -->
		<!-- Remarketing tags may not be associated with personally identifiable information or placed on pages related to sensitive categories. For instructions on adding this tag and more information on the above requirements, read the setup guide: google.com/ads/remarketingsetup -->
		<script type="text/javascript">
			/* <![CDATA[ */
		var google_conversion_id = 1008616035;
		var google_conversion_label = "_t14CI361wQQ44T54AM";
		var google_custom_params = window.google_tag_params;
		var google_remarketing_only = true;
		/* ]]> */
		</script>
			<script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
			</script>
		<noscript>
			<div style="display:inline;">
				<img height="1" width="1" style="border-style:none;" alt="" src="//googleads.g.doubleclick.net/pagead/viewthroughconversion/1008616035/?value=0&amp;label=_t14CI361wQQ44T54AM&amp;guid=ON&amp;script=0"/></div>
                <img height="1" width="1" style="border-style:none;" alt="" src="//googleads.g.doubleclick.net/pagead/viewthroughconversion/988370835/?value=0&amp;label=6HxaCN3_1wQQk6-l1wM&amp;guid=ON&amp;script=0"/>
		</noscript>        
        
        </c:if>
        
    </c:if>
    <%--</c:otherwise>--%>
</s:layout-definition>
