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
  <div class="jqmWindow" id="cartWindow2">
    <s:layout-render name="/layouts/modal.jsp">
      <s:layout-component name="heading">Prescription Details</s:layout-component>

      <div class="cart_error" id="cart_error2"></div>
      <s:layout-component name="content">
        <div class="checkboxError" style="margin-bottom:20px; font-size:1.2em; color:salmon;"></div>
        <table width="100%">
          <tr>
            <th></th>
              <%-- <th>Qty</th>--%>
            <c:forEach items="${product.productVariants[0].productExtraOptions}"
                       var="headerExtraOption">
              <th>${headerExtraOption.name}</th>
            </c:forEach>
          </tr>
          <s:form beanclass="com.hk.web.action.core.cart.AddToCartWithExtraOptionsAction" class="addToCartForm2">
            <c:set value="${product.productVariants[0]}" var="variant"/>
            <tr height="50px;">
              <s:hidden name="productLineItemWithExtraOptionsDtos[0].productVariant"
                        value="${variant.id}"/>
              <td>
                <label><s:checkbox
                    name="productLineItemWithExtraOptionsDtos[0].selected" class="checkbox"/>Left</label>
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
                <label><s:checkbox
                    name="productLineItemWithExtraOptionsDtos[1].selected" class="checkbox"/>Right</label>
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
              <c:forEach items="${product.productVariants[0].productExtraOptions}">
                <td></td>
              </c:forEach>
              <td>
                <div class="buttons sml left">
                  <s:submit name="addToCart" value="Place Order" class="addToCartButton button"
                            style="font-size: 1em;"/></div>
                <div class="progressLoader"><img
                    src="${pageContext.request.contextPath}/images/ajax-loader.gif"/>
                </div>
              </td>
            </tr>
          </s:form>
          <script type="text/javascript">
            $(document).ready(function() {
              function _addToCart2(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                  $('.message .line1').html("<strong>" + res.data.name + "</strong> is added to your shopping cart");
                } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                  $('#cart_error1').html(getErrorHtmlFromJsonResponse(res))
                      .slowFade(3000, 2000);
                } else if (res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>') {
                  window.location.replace(res.data.url);
                }
                $('.progressLoader').hide();
              }

              $('.addToCartForm2').ajaxForm({dataType: 'json', success: _addToCart2});
            });
            validateCheckbox = 1;
          </script>
        </table>
      </s:layout-component>
    </s:layout-render>
  </div>
</s:layout-definition>