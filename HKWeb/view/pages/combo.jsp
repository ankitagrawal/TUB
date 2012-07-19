<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="productBean"/>

<s:layout-render name="/layouts/genericG.jsp" pageTitle="Super Savers">
<s:layout-component name="htmlHead">
</s:layout-component>

<s:layout-component name="content">

<c:set var="productCombo" value="${productBean.product}"/>
<div style="text-align:center; margin:10px 0 10px 0;">
    <hk:superSaverImage imageId="${productBean.imageId}" size="<%=EnumImageSize.Original%>"
                        alt="${productCombo.name}"/>
</div>

<div class="clear"></div>
<div style="margin-top:10px;"></div>

<div class="contentDiv">
    <h1>${productCombo.name}</h1>

    <div style="margin-top:5px;"></div>

    <h4>Brand: ${productCombo.brand} | Dispatched in: ${productCombo.minDays} - ${productCombo.maxDays} working
        days</h4>

    <div style="margin-top:5px;"></div>

    Description:
    <p>${productCombo.description}</p>
</div>

<div class="clear"></div>
<div style="margin-top:10px;"></div>

<div class="container_24 containerDiv">
    <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
        <c:set var="combo" value="${productBean.combo}"/>
        <s:hidden name="combo" value="${combo}"/>
        <s:hidden name="combo.qty" value="1"/>

        <c:forEach items="${combo.comboProducts}" var="comboProduct" varStatus="productCtr">
            <c:set var="product" value="${comboProduct.product}"/>

            <div class="grid_24 productDiv">
                <div class="grid_23 comboProduct">
                    <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction">
                        <s:param name="productId" value="${product.id}"/>
                        <s:param name="productSlug" value="${product.slug}"/>
                        <h5>${product.name}</h5>
                    </s:link>
                </div>

                <div class="clear"></div>
                <div style="margin-top:10px;"></div>

                <c:choose>
                    <c:when test="${!empty comboProduct.allowedProductVariants}">
                        <c:set var="inStockVariants" value="${comboProduct.allowedInStockVariants}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="inStockVariants" value="${comboProduct.product.inStockVariants}"/>
                    </c:otherwise>
                </c:choose>

                <div class="grid_23 optionsDiv">
                    <fieldset>
                        <legend>&nbsp;&nbsp;Available Options</legend>
                        <c:forEach items="${inStockVariants}" var="variant" varStatus="variantCtr">
                            <c:choose>
                                <c:when test="${fn:length(product.productVariants) == 1}">
                                    <c:set var="idForImage" value="${product.mainImageId}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="idForImage" value="${variant.mainImageId}"/>
                                </c:otherwise>
                            </c:choose>


                            <div class="grid_4 options">
                                <c:choose>
                                    <c:when test="${idForImage != null}">
                                        <hk:productImage imageId="${idForImage}"
                                                         size="<%=EnumImageSize.MediumSize%>"
                                                         alt="${product.name}" class="imageTag"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src='<hk:vhostImage/>/images/ProductImages/ProductImagesThumb/test/${variant.id}.jpg'
                                             alt="${product.name}" class="imageTag"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:forEach items="${variant.productOptions}" var="option">
                                    ${option.name}:${option.value};
                                </c:forEach>      
                                <input type="hidden"  class="variantIdValue" idx="${productCtr.index}"  value="${variant.id}"/>
                                <input type="hidden" class="selValue" idx="${productCtr.index}"  value="true"/>
                                <input type="hidden" class="qtyValue" idx="${productCtr.index}"  value="0" />
                            </div>
                        </c:forEach>
                    </fieldset>

                    <div style="float:right;">
                        <div class="grid_1 arrowDiv">
                            <img src="<hk:vhostImage/>/images/arrow/black.jpg" alt="chosen"
                                 style="height:20px; width:25px;"/>
                        </div>

                        <fieldset style="float:right;">
                            <legend>&nbsp;&nbsp;Seleted Option</legend>
                            <div class="grid_4 result">
                            </div>
                        </fieldset>
                    </div>
                </div>
            </div>
        </c:forEach>

        <div class="clear"></div>
        <div style="margin-top:15px;"></div>

        <div class="grid_24">
            <div class="progressLoader"><img src="${pageContext.request.contextPath}/images/ajax-loader.gif"/></div>

            <div class="buyDiv">
                <div class="left_col" style="float:left;">
                    <div class='prices' style="font-size: 14px;">
                        <div class='cut' style="font-size: 14px;">
                <span class='num' style="font-size: 14px;">
                  Rs <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
                </span>
                        </div>
                        <div class='hk' style="font-size: 16px;">
                            Our Price
                <span class='num' style="font-size: 20px;">
                  Rs <fmt:formatNumber
                        value="${combo.hkPrice}"
                        maxFractionDigits="0"/>
                </span>
                        </div>
                        <div class="special green" style="font-size: 14px;">
                            you save
              <span style="font-weight: bold;"><fmt:formatNumber
                      value="${combo.discountPercent*100}"
                      maxFractionDigits="0"/>%</span>
                        </div>
                    </div>
                </div>
                <div class="right_col" style="float:right;">
                    <c:choose>
                        <c:when test="${hk:isComboInStock(combo)}">
                            <s:submit name="addToCart" value="Buy Now" class="addToCartButton cta"/>
                        </c:when>
                        <c:otherwise>
                            <div align="center"><a class="button_orange"><b>Sold Out</b></a></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="clear"></div>
        <div style="margin-top:10px;"></div>
    </s:form>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('.progressLoader').hide();

        //        $('.optionsDiv').css({
        //            marginLeft:($('.grid_20').width() - $('.optionsDiv').outerWidth()) / 2
        //        });

        $('.options').click(function() {
            //        $(this).parents('.productDiv').find('.activeImg').each(function() {
            //          $(this).removeClass("activeImg");
            //        });
            //            $(this).addClass("activeImg");

           
            
            var resultDiv = $(this).parents('.optionsDiv').find('.result');
            resultDiv.html($(this).html());

            var idx = resultDiv.find('.variantIdValue').attr("idx");
            resultDiv.find('.variantIdValue').attr("name","productVariantList["+ idx + "]");
            resultDiv.find('.selValue').attr("name","productVariantList["+ idx + "].selected");
            resultDiv.find('.qtyValue').attr("name","productVariantList["+ idx + "].qty");
            
            resultDiv.find('.qtyValue').val("1");
        });

        function _addToCart(res) {
            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                $('.message .line1').html("<strong>" + res.data.name + "</strong> has been added to your shopping cart");
                $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
                $('.progressLoader').hide();

                show_message();
            } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                alert(res.message);
                $('.progressLoader').hide();
            }
        }

        $(".message .close").click(function() {
            hide_message();
        });
        $(document).click(function() {
            hide_message();
        });

        function hide_message() {
            $('.message').animate({
                top: '-170px',
                opacity: 0
            }, 100);
        }

        function show_message() {
            $('.message').css("top", "70px");
            $('.message').animate({
                opacity: 1
            }, 500);
        }

        $('.addToCartForm').ajaxForm({dataType: 'json', success: _addToCart});

        $('.addToCartButton').click(function() {
            var isResultEmpty = 0 ;
            $('.result').each(function() {
                if ($(this).text().trim() === "") {
                    isResultEmpty = 1;
                    alert('Please select one of the options available for all the products in the combo!');
                    return false;
                }
            });
            return !isResultEmpty;
        });
    });
</script>
<style type="text/css">
    .comboProduct {
        text-align: left;
        background: none repeat scroll 0 0 #EEEEEE;
        padding: 5px;
    }

    .options {
        border: 1px solid darkgray;
        border-radius: 0.5em;
        background: #EEEEEE;
        padding: 10px 0;
        height: 120px;
    }

    .result {
        border: 2px solid darkgray;
        border-radius: 0.5em;
        background: #EEEEEE;
        padding: 10px 0;
        height: 120px;
    }

    div.arrowDiv {
        text-align: center;
        padding-top: 80px;
        height: 20px;
        width: 25px;
        opacity: 0.4;
    }

    fieldset {
        border: 1px solid gainsboro;
        float: left;
        padding: 5px;
        border-radius: 0.5em;
    }

    div.productDiv {
        text-align: center;
        margin: 10px 0;
    }

    div.containerDiv {
        text-align: center;
    }

    div.contentDiv {
        margin: 10px 5px;
    }

    .options:hover {
        border: solid 1px #CCC;
        -moz-box-shadow: 1px 1px 5px #999;
        -webkit-box-shadow: 1px 1px 5px #999;
        box-shadow: 1px 1px 5px #999;
        -webkit-transform: scale(1.25);
        -moz-transform: scale(1.25);
        position: relative;
        z-index: 3;
    }

    .options img {
        -webkit-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
        -moz-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
    }

    .result img {
        -webkit-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
        -moz-box-shadow: 0 3px 6px rgba(0, 0, 0, .5);
    }

    imageTag {
        width: 120px;
        height: 120px;
    }

    .buyDiv {
        font-size: 1em;
        text-align: right;
        float: right;
    }

    .buyDiv .right_col {
        text-align: right;
        float: right;
    }
</style>
</s:layout-component>
</s:layout-render>