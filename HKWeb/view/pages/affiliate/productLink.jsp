<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductAction" var="pa"/>

<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Customise Product Link</s:layout-component>
  <s:layout-component name="rhsContent">
    <div>
      <h4 class="strikeline">Product Name:- ${pa.product.name}
      </h4>
      <s:form beanclass="com.hk.web.action.core.catalog.product.ProductAction">
        <s:errors/>
        <div class="round-cont" style="width:650px;margin-top: 20px;">
            <%--
                      <div class="label">
                        <s:radio value="${pa.anchor}" name="anchor"/>
                        <label>Enter Achor Tag</label> <s:text name="anchor" value="${pa.anchor}"/>
                      </div>
            --%>
          <div class="label">
            Choose one of the Button Links and Click Customize to get the html code
          </div>
          <div class="label">
            Button Link
            <s:radio value="buy_button_1" name="anchor"/>
            <img src='${pageContext.request.contextPath}/images/icons/buy_button_1.png'/>
            <s:radio value="buy_button_2" name="anchor"/>
            <img src='${pageContext.request.contextPath}/images/icons/buy_button_2.png'/>
            <s:radio value="buy_button_3" name="anchor"/>
            <img src='${pageContext.request.contextPath}/images/icons/buy_button_3.png'/>
          </div>
          <div class="label">
            <s:hidden name="product" value="${pa.product.id}"/>
            <s:submit name="getProductAffiliateLink" value="Customise Link"/>
          </div>
          <div class="label">
            <label>Copy the following code and paste it in your website</label>
          </div>
          <div class="label">
            <s:textarea id="productLinkForAffiliate" name="productLinkForAffiliate" rows="3" cols="50" value="${pa.productLinkForAffiliate}"/>
          </div>
          <div class="label">
            <a href="javascript:void(0);" onclick="document.getElementById('productLinkForAffiliate').focus();document.getElementById('productLinkForAffiliate').select();">Select
              This Code</a>
          </div>
          <div class="label">
            Sneak View
          </div>
          <div class="label">
            <c:choose>
              <c:when test="${fn:startsWith(pa.anchor,'buy_button')}">
                <a href="${pageContext.request.contextPath}/product/${pa.product.slug}/${pa.product.id}?affid=${pa.affiliate.code}">
                  <img class='prod320'
                       src='${pageContext.request.contextPath}/images/icons/${pa.anchor}.png'/>
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/product/${pa.product.slug}/${pa.product.id}?affid=${pa.affiliate.code}">${pa.anchor}</a>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
        <s:hidden name="product" value="${pa.product.id}"/>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>