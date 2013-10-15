<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.dto.pricing.PricingDto" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%--
Pass an attribute called pricingDto to render a table with pricing details
--%>
<%--<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction"/>--%>
<s:useActionBean beanclass="com.hk.web.action.core.order.OrderSummaryAction" var="orderSummary"/>
<s:layout-definition>
<%
    PricingDto pricingDto = (PricingDto) pageContext.getAttribute("pricingDto");
    Date orderDate = (Date) pageContext.getAttribute("orderDate");
    // this line does not mean anything, but simply gets idea to understand pricingDto's type, autocompletion works now.
    pageContext.setAttribute("pricingDto", pricingDto);
    pageContext.setAttribute("orderDate", orderDate);
    if (pricingDto != null && orderDate != null) {
%>

<div class="topFullContainer">
<div class='orderSummaryHeading'>
    <div class="productGrid">${fn:length(youBoughtText)>0 ? youBoughtText:'Product'} </div>

    <div class="prodQuantityGrid">Qty</div>
    <div class="prodDispatchGrid">Dispatch</div>
        <%--<span id="dispatchDateQuesMark" class="dispatchDateQuesMark">?</span>--%>
        <%--<span class="dispatchDateText2" style="font-size: 9px;">Delivery time would be extra</span>--%>

        <%--<div class="popUpDDate" id="popUpDDate">The dispatch date is when the product will be shipped from our--%>
            <%--warehouse. The delivery time would be extra and will vary according to your location.--%>

            <%--<span id="crossNew" style="position: relative;float: right;top: 12px;cursor: pointer;">X</span>--%>
            <%--<span class="arrowNew2"></span>--%>
        <%--</div>--%>
    <div class="prodPriceGrid">Price</div>
</div>
<div class='products_container' style="overflow: visible;min-height: 0px;clear: both;">
    <c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem" varStatus="ctr1">
        <c:set var="storeVariantBasic"
               value="${hk:getStoreVariantBasicDetails(invoiceLineItem.productVariant.id)}"/>

        <c:if
                test="${invoiceLineItem.comboInstance == null && invoiceLineItem.productVariant.paymentType.name != 'Postpaid'}">
            <div class='product newProductContainer' style="border-bottom-style: solid;height: auto;">
                <div class='img48' style="vertical-align:top;position: relative;float: left;width: 8%;margin-left: 10px;border: 1px solid #ccc;padding: 3px;">
                  <c:choose>
                            <c:when test="${storeVariantBasic.primaryImage.mlink!=null}">
                              <img class="prod48" src="${storeVariantBasic.primaryImage.mlink}"
                                   alt="${storeVariantBasic.name!=null?storeVariantBasic.name : invoiceLineItem.productVariant.product.name}"/>
                            </c:when>
                            <c:otherwise>
                              <c:choose>
                              <c:when test="${invoiceLineItem.productVariant.product.mainImageId != null}">
                              <hk:productImage imageId="${invoiceLineItem.productVariant.product.mainImageId}"
                              size="<%=EnumImageSize.TinySize%>"/>
                              </c:when>
                              <c:otherwise>
                              <img class="prod48"
                              src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${invoiceLineItem.productVariant.product.id}.jpg"
                              alt="${invoiceLineItem.productVariant.product.name}"/>
                              </c:otherwise>
                              </c:choose>
                            </c:otherwise>
                          </c:choose>
                </div>
                <div class='name' style="position: relative;float: left;width: 42%;font-size: 14px;">
                    <div style="padding:5px;width:80%;position: relative;float: left;word-wrap:break-word;">
                            ${storeVariantBasic.name!=null?storeVariantBasic.name : invoiceLineItem.productVariant.product.name}
                                <c:if test="${hk:equalsIgnoreCase(invoiceLineItem.productVariant.product.primaryCategory.name,'eye') and hk:equalsIgnoreCase(invoiceLineItem.productVariant.product.secondaryCategory.name,'lenses')}">
                                    <table style="display: inline-block; font-size: 11px;margin: 7px 0;">
                                        <c:forEach items="${invoiceLineItem.productVariant.productOptions}" var="productOption" varStatus="ctr">
                                            <tr>
                                                <c:if test="${hk:showOptionOnUI(productOption.name)}">
                                                    <td style="text-align: left;  padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fafafa;">${productOption.name}</td>
                                                    <td style="text-align: left; padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fff;">
                                                        <c:if test="${fn:startsWith(productOption.value, '-')}">
                                                            ${productOption.value}
                                                        </c:if>
                                                        <c:if test="${!fn:startsWith(productOption.value, '-')}">
                                                            &nbsp;${productOption.value}
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>
                                        <c:forEach items="${invoiceLineItem.cartLineItemExtraOptions}" var="extraOption">
                                            <tr>
                                                <td style="text-align: left;  padding: 0.3em 2em; border: 1px solid #f0f0f0;background: #fafafa;">${extraOption.name}</td>
                                                <td style="text-align: left; padding: 0.3em 2em;border: 1px solid #f0f0f0;background: #fff;">
                                                    <c:if test="${fn:startsWith(extraOption.value, '-')}">
                                                        ${extraOption.value}
                                                    </c:if>
                                                    <c:if test="${!fn:startsWith(extraOption.value, '-')}">
                                                        &nbsp;${extraOption.value}
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:set var="TH" value="TH"/>
                                        <c:set var="THBF" value="THBF"/>
                                        <c:set var="CO" value="CO"/>
                                        <c:set var="COBF" value="COBF"/>
                                        <c:if test="${not empty invoiceLineItem.cartLineItemConfig.cartLineItemConfigValues}">
                                            <tr>
                                                <td style="text-align: left; padding:5px; border: 1px solid #f0f0f0;background: #fafafa;">${invoiceLineItem.productVariant.product.name}</td>
                                                <td style="text-align: left; padding:3px;border: 1px solid #f0f0f0;background: #fff;">
                                                    Rs. ${invoiceLineItem.productVariant.hkPrice}</td>
                                            </tr>
                                            <c:forEach items="${invoiceLineItem.cartLineItemConfig.cartLineItemConfigValues}" var="configValue">
                                                <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
                                                <tr>
                                                    <c:set var="addParam" value="${variantConfigOption.additionalParam}"/>
                                                    <td style="text-align: left; padding:5px; border: 1px solid #f0f0f0;background: #fafafa;">${variantConfigOption.displayName}
                                                        : ${configValue.value}
                                                        <c:if test="${(addParam ne TH) or (addParam ne THBF) or (addParam ne CO) or (addParam ne COBF) }">
                                                            <c:if
                                                                    test="${fn:startsWith(variantConfigOption.name, 'R')==true}">
                                                                (R)
                                                            </c:if>
                                                            <c:if
                                                                    test="${fn:startsWith(variantConfigOption.name, 'L')==true}">
                                                                (L)
                                                            </c:if>
                                                        </c:if>
                                                    </td>
                                                    <td style="text-align: left; padding:3px;border: 1px solid #f0f0f0;background: #fff;">
                                                            <%--<c:set var="addParam" value="${variantConfigOption.additionalParam}"/>--%>
                                                        <c:choose>
                                                            <c:when test="${configValue.additionalPrice eq 0}">
                                                                included
                                                            </c:when>
                                                            <c:otherwise>
                                                                +Rs. ${configValue.additionalPrice}
                                                                <%--<c:if test="${(addParam eq TH) or (addParam eq THBF) or (addParam eq CO) or (addParam eq COBF) }">--%>
                                                                <%--/Eye--%>
                                                                <%--</c:if>--%>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>

                                    </table>
                                </c:if>

                            <%--${invoiceLineItem.productVariant.variantName}--%>
                    </div>
                    <div align="left" style="text-align:center;width:12%;padding: 5px;position: relative;float: left;">${invoiceLineItem.qty}</div>
                </div>

                    <%--HTML code for dispatch date--%>
                <div class="dispatchedDateNew2">
                    <div>${invoiceLineItem.productVariant.product.minDays}
                        - ${invoiceLineItem.productVariant.product.maxDays} days
                    </div>
                </div>


                <div class='price' style="position: relative;text-align: center;float: left;padding: 5px;width: 21%;right: 5px;">
                    <c:choose>
                        <c:when test="${invoiceLineItem.hkPrice == 0.0}">
                            <div style="position: relative;">
                                <span class="lineItemSubTotalMrp arialBlackBold" style="font-weight:bold;">Free!</span>
                            </div>
                        </c:when>
                        <c:otherwise>
                          <c:if test="${invoiceLineItem.markedPrice * invoiceLineItem.qty != invoiceLineItem.hkPrice * invoiceLineItem.qty}">
                            <div class="cut">
                              <div class="num lineItemSubTotalMrp arialGrayBold"
                                   style="position: relative;margin-bottom: 7px;"> Rs
                                <fmt:formatNumber value="${invoiceLineItem.markedPrice * invoiceLineItem.qty}"
                                                  pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
                            </div>

                          </c:if>
                            <div style="position: relative;">
                                <span class="lineItemSubTotalMrp fnt-sz16" style="color: #090">Rs <fmt:formatNumber
                                        value="${invoiceLineItem.hkPrice * invoiceLineItem.qty}"
                                        pattern="<%=FormatUtils.currencyFormatPattern%>"/>
                                </span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class='floatix'></div>
            </div>
        </c:if>
    </c:forEach>
    <c:set var="firstComboLineItem" value=""/>
    <c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem" varStatus="ctr1">
        <c:if test="${invoiceLineItem.comboInstance != null}">
            <c:if test="${!fn:contains(firstComboLineItem,invoiceLineItem.comboInstance.id)}">
               <c:set var="firstComboLineItem" value="${invoiceLineItem.comboInstance.combo}"/>
                <c:set var="firstComboLineItem" value="${firstComboLineItem} + ',' + ${invoiceLineItem.comboInstance.id} + ','" />
                <div class='product newProductContainer' style="border-bottom-style: solid;height: auto;">
                    <div class='img48' style="vertical-align:top;position: relative;float: left;width: 8%;margin-left: 10px;border: 1px solid #ccc;padding: 3px;">
                        <c:choose>
                            <c:when test="${invoiceLineItem.comboInstance.combo.mainImageId != null}">
                                <hk:productImage imageId="${invoiceLineItem.comboInstance.combo.mainImageId}"
                                                 size="<%=EnumImageSize.TinySize%>"/>
                            </c:when>
                            <c:otherwise>
                                <img class="prod48"
                                     src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${invoiceLineItem.comboInstance.combo.id}.jpg"
                                     alt="${invoiceLineItem.comboInstance.combo.name}"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class='name' style="position: relative;float: left;width: 42%;font-size: 14px;">
                        <div style="padding:5px;width:80%;position: relative;float: left;">
                        ${invoiceLineItem.comboInstance.combo.name}<br/>
                                    <c:forEach items="${invoiceLineItem.comboInstance.comboInstanceProductVariants}" var="comboVariant">
                                      <span style="font-size:10px;">${comboVariant.qty} x </span>
                                      <span style="font-size:10px;word-wrap:break-word;">${comboVariant.productVariant.product.name} - ${comboVariant.productVariant.optionsCommaSeparated}</span>
                                    </c:forEach>
                                </div>
                        <div align="left" style="text-align:center;width:12%;padding: 5px;position: relative;float: left;">${hk:getComboCount(invoiceLineItem)}</div>
                    </div>

                    <div class="dispatchedDateNew2"><div>${invoiceLineItem.productVariant.product.minDays} - ${invoiceLineItem.productVariant.product.maxDays} days</div></div>
                    <div class='price' style="position: relative;text-align: center;float: left;padding: 5px;width: 21%;right: 5px;">

                      <c:if test="${invoiceLineItem.comboInstance.combo.markedPrice * hk:getComboCount(invoiceLineItem) != invoiceLineItem.comboInstance.combo.hkPrice * hk:getComboCount(invoiceLineItem)}">
                        <div class="cut">
                          <div class="num lineItemSubTotalMrp arialGrayBold" style="position: relative;margin-bottom: 7px;">  Rs
                            <fmt:formatNumber value="${invoiceLineItem.comboInstance.combo.markedPrice * hk:getComboCount(invoiceLineItem)}"
                                              pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
                        </div>

                      </c:if>


                        <div style="position: relative;">
            <span class="lineItemSubTotalMrp fnt-sz16" style="color: #090">Rs <fmt:formatNumber
                    value="${invoiceLineItem.comboInstance.combo.hkPrice * hk:getComboCount(invoiceLineItem)}"
                    pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
                        </div>
                    </div>
                    <div class='floatix'></div>
                </div>

            </c:if>
        </c:if>
    </c:forEach>
</div>

<c:if test="${pricingDto.postpaidServicesTotal > 0.00}">
    <c:forEach items="${pricingDto.productLineItems}" var="invoiceLineItem" varStatus="ctr1">
        <c:set var="storeVariantBasic"
               value="${hk:getStoreVariantBasicDetails(invoiceLineItem.productVariant.id)}"/>

        <c:if test="${invoiceLineItem.productVariant.paymentType.name == 'Postpaid'}">
            <div class='product newProductContainer' style="border-bottom-style: solid;height: auto;">
                <div class='img48' style="vertical-align:top;position: relative;float: left;width: 8%;margin-left: 10px;border: 1px solid #ccc;padding: 3px;">

                  <c:choose>
                    <c:when test="${storeVariantBasic.primaryImage.mlink!=null}">
                      <img class="prod48" src="${storeVariantBasic.primaryImage.mlink}"
                           alt="${storeVariantBasic.name!=null?storeVariantBasic.name : invoiceLineItem.productVariant.product.name}"/>
                    </c:when>
                    <c:otherwise>
                      <c:choose>
                        <c:when test="${invoiceLineItem.productVariant.product.mainImageId != null}">
                          <hk:productImage imageId="${invoiceLineItem.productVariant.product.mainImageId}"
                                           size="<%=EnumImageSize.TinySize%>"/>
                        </c:when>
                        <c:otherwise>
                          <img class="prod48"
                               src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${invoiceLineItem.productVariant.product.id}.jpg"
                               alt="${invoiceLineItem.productVariant.product.name}"/>
                        </c:otherwise>
                      </c:choose>
                    </c:otherwise>
                  </c:choose>

                </div>
                <div class='name' style="position: relative;float: left;width: 42%;font-size: 14px;">
                    <div style="padding:5px;width:80%;position: relative;float: left;word-wrap:break-word">
                        ${storeVariantBasic.name!=null?storeVariantBasic.name : invoiceLineItem.productVariant.product.name}
                                     <br/>
                                    <%--${invoiceLineItem.productVariant.variantName}--%>
                    </div>
                    <div align="left" style="text-align:center;width:12%;padding: 5px;position: relative;float: left;">${invoiceLineItem.qty}</div>
                </div>
                <div class="dispatchedDateNew2">
                    <div>${invoiceLineItem.productVariant.product.minDays}
                        - ${invoiceLineItem.productVariant.product.maxDays} days
                    </div>
                </div>
                <div class='price' style="position: relative;text-align: center;float: left;padding: 5px;width: 21%;right: 5px;">

                  <c:if test="${invoiceLineItem.markedPrice * invoiceLineItem.qty != invoiceLineItem.hkPrice * invoiceLineItem.qty}">
                    <div class="cut">
                      <div class="num lineItemSubTotalMrp arialGrayBold"
                           style="position: relative;margin-bottom: 7px;"> Rs
                        <fmt:formatNumber value="${invoiceLineItem.markedPrice * invoiceLineItem.qty}"
                                          pattern="<%=FormatUtils.currencyFormatPattern%>"/></div>
                    </div>

                  </c:if>

                </div>
                <div style="position: relative;">
            <span class="lineItemSubTotalMrp fnt-sz16" style="color: #090">Rs <fmt:formatNumber
                    value="${invoiceLineItem.hkPrice * invoiceLineItem.qty}"
                    pattern="<%=FormatUtils.currencyFormatPattern%>"/></span>
                </div>
            </div>
            <div class='floatix'></div>
            </div>
        </c:if>
    </c:forEach>
</c:if>
</div>
<%--
<div class='orderSummaryHeading' style="margin-bottom: 50px;">
    <div class="deliveryDetails"> DELIVERY DETAILS</div>
    <ul>
        <li>
            - The time taken for delivery after dispatch from our warehouse varies with location.
        </li>
        <li>
            - For Metros: 1-3 business days
        </li>
        <li>
            - For Major Cities: 2-4 business days
        </li>
        <li>
            - For Other Town/Cities: 3-6 business days
        </li>
        <li>
            - For Rest of India Non Serviceable through Couriers: 7-15 business days (Delivery done by Indian Post)
        </li>
    </ul>
</div>--%>
<%
    }
%>
</s:layout-definition>
