<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.pact.dao.catalog.combo.ComboDao" %>
<%@ page import="com.hk.domain.catalog.product.combo.Combo" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
<%
  ComboDao comboDao = (ComboDao)ServiceLocatorFactory.getService(ComboDao.class);
  String productId = (String) pageContext.getAttribute("productId");
  Combo combo = comboDao.get(Combo.class, productId);
  pageContext.setAttribute("combo", combo);
%>
<shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
  <s:link beanclass="com.hk.web.action.admin.catalog.product.CreateEditComboAction" event="pre" target="_blank" class="popup">Edit Combo
    <s:param name="combo" value="${combo.id}"/>
  </s:link>
</shiro:hasPermission>
<div>
<s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm" onsubmit="return validQty();">
<div class="buy_prod disp-inln">
    <div class="left_col mrgn-b-20">
        <div class='prices cont-lft' style="font-size: 14px;">
            <%--<div class='cut' style="font-size: 14px;">--%>
                <span class='num cut' style="font-size: 14px;">
                  Rs. <fmt:formatNumber value="${combo.markedPrice}" maxFractionDigits="0"/>
                </span>
            <%--</div>--%>
            <%--<div class="special green" style="font-size: 14px;">--%>
              | YOU SAVE
              <span><fmt:formatNumber
                      value="${combo.discountPercent*100}"
                      maxFractionDigits="0"/>%</span>
            <%--</div>--%>
            <p class='hk'>
                <span class='num' style="font-size: 1.8em;">
                  Now Rs. <fmt:formatNumber
                        value="${combo.hkPrice}"
                        maxFractionDigits="0"/>
                </span>
            </p>
        </div>
    </div>
    <div class="right_col">
        <c:choose>
            <c:when test="${!combo.outOfStock}">
                <s:submit name="addToCart" value="Buy Now" class="addToCartButton cta btn btn-blue cont-rht" style="margin-bottom:5px;"/>
                <div class="fnt-caps mrgn-t-5">Instock</div>
                <div class="fnt-sz9">DISPATCHED IN 1 - 3 DAYS</div>
            </c:when>
            <c:otherwise>
                <div class="btn btn-orange cont-rht"><b>Sold Out</b></div>
                <div class="fnt-caps mrgn-t-50">Out of stock</div>
            </c:otherwise>
        </c:choose>

            <%--<s:layout-render name="/layouts/embed/_hkAssistanceMessageForSingleVariant.jsp"/>--%>
    </div>
</div>
<div class="mrgn-b-10 brdr-b-sd pad-b-5 fnt-sz9">
    <c:choose>
        <c:when test="${product.codAllowed != null && !product.codAllowed}">
            <span class="fnt-light">Cash on Delivery Not Available</span>
        </c:when>
        <c:otherwise>
            <span class="fnt-light">Cash on Delivery Available</span>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${combo.hkPrice > 500}">
            | <span class="fnt-light"> This product is available for Free Shipping</span>
        </c:when>
        <c:otherwise>
            | <span class="fnt-light">Free Shipping if total order amount is Rs. 500 or above. Add Rs. 50 otherwise</span>
        </c:otherwise>
    </c:choose>
</div>
<table class="varnts-tbl">
  <s:hidden name="combo" value="${combo}"/>
  <s:hidden name="combo.qty" value="1"/>
  <c:set var="globalCtr" value="0"/>
  <c:forEach items="${combo.comboProducts}" var="comboProduct" varStatus="ctr">
    <tr class="specs-hdr-tr">
      <td><s:link class="fnt-bold" beanclass="com.hk.web.action.core.catalog.product.ProductAction">
        <s:param name="productId" value="${comboProduct.product.id}"/>
        <s:param name="productSlug" value="${comboProduct.product.slug}"/>${comboProduct.product.name}
      </s:link></td>
      <input type="hidden" class="comboProduct" value="${ctr.index}"/>
      <td class="fnt-bold" id="comboProduct[${ctr.index}].qty" align="right">${comboProduct.qty}</td>
    </tr>
    <c:choose>
      <c:when test="${!empty comboProduct.allowedProductVariants}">
        <c:choose>
          <c:when test="${fn:length(comboProduct.allowedInStockVariants) == 1}">
            <tr class="${ctr.index % 2 == 0 ? 'alt':''} top-brdr btm-brdr varnt-specs-tr">
               <td>
                <c:forEach items="${comboProduct.allowedInStockVariants[0].productOptions}" var="option">
                  ${option.name}:${option.value};
                </c:forEach></td>
              <td>
                <s:hidden name="productVariantList[${globalCtr}]" value="${comboProduct.allowedInStockVariants[0].id}"/>
                <s:hidden name="productVariantList[${globalCtr}].selected" value="true"/>
                <s:hidden name="productVariantList[${globalCtr}].qty" value="${comboProduct.qty}"
                          class="lineItemQty comboProduct[${ctr.index}]"/>
                <c:set var="globalCtr" value="${globalCtr + 1}"/>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach items="${comboProduct.allowedInStockVariants}" var="variant" varStatus="ctr2">
                <c:if test="${ctr2.first}">
                    <tr class="top-brdr varnt-specs-tr" >
                </c:if>
                <c:if test="${ctr2.last}">
                    <tr class="${ctr2.index%2==0? 'varnt-specs-tr btm-brdr':'btm-brdr varnt-specs-tr bg-gray'}" >
                </c:if>
                <c:if test="${!(ctr2.first || ctr2.last)}">
                    <tr class="${ctr2.index%2==0? 'varnt-specs-tr':' varnt-specs-tr bg-gray'}" >
                </c:if>
                <td>
                  <c:forEach items="${variant.productOptions}" var="option">
                    ${option.name}:${option.value};
                  </c:forEach>
                </td>
                <td style="padding:1px;">
                  <s:hidden name="productVariantList[${globalCtr}]" value="${variant.id}"/>
                  <s:hidden name="productVariantList[${globalCtr}].selected" value="true"/>
                  <c:choose>
                    <c:when test="${comboProduct.qty == 1}">
                      <c:choose>
                        <c:when test="${ctr2.index == 0}">
                          <s:hidden name="productVariantList[${globalCtr}].qty" value="${comboProduct.qty}" style="width:20px;padding:0px;"
                                    class="lineItemQty comboProduct[${ctr.index}] variant[${globalCtr}]"/>
                          <input type="radio" name="radio[${comboProduct.id}]" class="radio[${ctr.index}]"
                                 checked="checked" onchange="setVariantQty('comboProduct[${ctr.index}]', 'variant[${globalCtr}]')"/>
                        </c:when>
                        <c:otherwise>
                          <s:hidden name="productVariantList[${globalCtr}].qty" value="0" style="width:20px;padding:0px;"
                                    class="lineItemQty comboProduct[${ctr.index}] variant[${globalCtr}]"/>
                          <input type="radio" name="radio[${comboProduct.id}]" class="radio[${ctr.index}]"
                                 onchange="setVariantQty('comboProduct[${ctr.index}]', 'variant[${globalCtr}]')"/>
                        </c:otherwise>
                      </c:choose>
                    </c:when>
                    <c:otherwise>
                      <c:choose>
                        <c:when test="${ctr2.index == 0}">
                          <s:text name="productVariantList[${globalCtr}].qty" value="${comboProduct.qty}" size="1"
                                  onkeyup="isNumber(this);validateQty(this)"
                                  class="lineItemQty comboProduct[${ctr.index}]"
                                  style="width: 20px; padding:0px;" autocomplete="off"/>
                        </c:when>
                        <c:otherwise>
                          <s:text name="productVariantList[${globalCtr}].qty" value="0" size="1"
                                  onkeyup="isNumber(this);validateQty(this)"
                                  class="lineItemQty comboProduct[${ctr.index}]" style="width:20px;padding:0px;"
                                  autocomplete="off"/>
                        </c:otherwise>
                      </c:choose>
                    </c:otherwise>
                  </c:choose>

                  <c:set var="globalCtr" value="${globalCtr + 1}"/>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${fn:length(comboProduct.product.inStockVariants) == 1}">
            <tr class="${ctr.index % 2 == 0 ? 'alt':''} top-brdr btm-brdr varnt-specs-tr">
               <td>
                <c:forEach items="${comboProduct.product.inStockVariants[0].productOptions}" var="option">
                  ${option.name}:${option.value};
                </c:forEach></td>
              <td>
                <s:hidden name="productVariantList[${globalCtr}]"
                          value="${comboProduct.product.inStockVariants[0].id}"/>
                <s:hidden name="productVariantList[${globalCtr}].selected" value="true"/>
                <s:hidden name="productVariantList[${globalCtr}].qty" value="${comboProduct.qty}"
                          class="lineItemQty comboProduct[${ctr.index}]"/>
                <c:set var="globalCtr" value="${globalCtr + 1}"/>
              </td>
            </tr>

          </c:when>
          <c:otherwise>
            <c:forEach items="${comboProduct.product.inStockVariants}" var="variant" varStatus="ctr2">
                <c:if test="${ctr2.first}">
                    <tr class="${ctr.index % 2 == 0 ? 'alt':''} top-brdr varnt-specs-tr" >
                </c:if>
                <c:if test="${ctr2.last}">
                    <tr class="${ctr.index % 2 == 0 ? 'alt':''} ${ctr2.index%2==0? 'varnt-specs-tr btm-brdr':'btm-brdr varnt-specs-tr bg-gray'}" >
                </c:if>
                <c:if test="${!(ctr2.first || ctr2.last)}">
                    <tr class="${ctr.index % 2 == 0 ? 'alt':''} ${ctr2.index%2==0? 'varnt-specs-tr':' varnt-specs-tr bg-gray'}" >
                </c:if>
                <td>
                  <c:forEach items="${variant.productOptions}" var="option">
                    ${option.name}:${option.value};
                  </c:forEach></td>
                <td style="padding:1px;">
                  <s:hidden name="productVariantList[${globalCtr}]" value="${variant.id}"/>
                  <s:hidden name="productVariantList[${globalCtr}].selected" value="true"/>
                  <c:choose>
                    <c:when test="${ctr2.index == 0}">
                      <s:text name="productVariantList[${globalCtr}].qty" value="${comboProduct.qty}" size="1"
                              onkeyup="isNumber(this);validateQty(this)"
                              class="lineItemQty comboProduct[${ctr.index}]"
                              style="width:20px;padding:0px;" autocomplete="off"/>
                    </c:when>
                    <c:otherwise>
                      <s:text name="productVariantList[${globalCtr}].qty" value="0" size="1"
                              onkeyup="isNumber(this);validateQty(this)"
                              class="lineItemQty comboProduct[${ctr.index}]" style="width:20px;padding:0px;"
                              autocomplete="off"/>
                    </c:otherwise>
                  </c:choose>
                  <c:set var="globalCtr" value="${globalCtr + 1}"/>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>

      </c:otherwise>
    </c:choose>
    <tr><td>&nbsp;</td></tr>
  </c:forEach>
</table>
<div class="progressLoader"><img src="${pageContext.request.contextPath}/images/ajax-loader.gif"/></div>

<script type="text/javascript">

  function validQty() {
    var cpElms = document.getElementsByClassName("comboProduct");
    for (var i = 0; i < cpElms.length; i++) {
      var cpQty = Math.round(document.getElementById("comboProduct[" + cpElms[i].value + "].qty").innerHTML);
      var cpvElms = document.getElementsByClassName("comboProduct[" + i + "]");
      var cpvQty = 0;
      for (var j = 0; j < cpvElms.length; j++) {
        cpvQty += Math.round(cpvElms[j].value);
      }
      if (cpvQty != cpQty) {
        alert("Inappropriate selection of qty");
        return false;
      }
    }
    return true;
  }

  function setVariantQty(resetElms, setElm) {
    //alert(resetElms+":"+setElm);
    var cpavElms = document.getElementsByClassName(resetElms);
     for (var i = 0; i < cpavElms.length; i++) {
        cpavElms[i].value = 0;
     }
    document.getElementsByClassName(setElm)[0].value = 1;
    /*$('.'+resetElms).each(function(){
      $(this).val(0);
    })
    $('.'+setElm).val(1);*/
  }

  function validateQty(elm) {
    var secondClass = $(elm).attr('class').split(' ')[1];
    var maxQty = Math.round(document.getElementById(secondClass + '.qty').innerHTML);
    var cpElms = document.getElementsByClassName(secondClass);
    var netQty = 0;
    for (var i = 0; i < cpElms.length; i++) {
      netQty += Math.round(cpElms[i].value);
      while (netQty > maxQty) {
        cpElms[i].value = Math.round(cpElms[i].value) - 1;
        netQty -= 1;
        if (netQty == maxQty)
          break;
      }
      while (netQty < maxQty && cpElms.length == 1) {
        cpElms[i].value = Math.round(cpElms[i].value) + 1;
        netQty += 1;
        if (netQty == maxQty)
          break;
      }
    }
  }

  function isNumber(elm) {
    var strString = elm.value;
    var strValidChars = "0123456789";
    var strChar;
    var blnResult = true;
    var resultString;
    if (strString.length == 0)
      return false;

    //  test strString consists of valid characters listed above
    for (i = 0; i < strString.length && blnResult == true; i++)
    {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
      {
        blnResult = false;
        alert("Invalid value");
        elm.select();
        elm.focus();

        resultString = strString.split(strString.charAt(i));

        elm.value = 0;
      }
    }
    return blnResult;
  }
</script>
</s:form>
</div>

</s:layout-definition>