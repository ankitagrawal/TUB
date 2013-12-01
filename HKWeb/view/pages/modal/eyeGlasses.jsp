<%@ page import="com.hk.domain.catalog.product.Product" %>
<%@ page import="com.hk.pact.dao.catalog.product.ProductDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
 <%
     Product product = (Product) pageContext.getAttribute("product");
     pageContext.setAttribute("product", product);
 %>

<div class="jqmWindow" id="eyeGlassWindow">
    <s:layout-render name="/layouts/modal.jsp">
      <s:layout-component name="heading">Eye Glass Details</s:layout-component>

      <div class="cart_error" id="cart_error2"></div>
      <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
          <s:hidden name="productVariantList[0]" value="${product.productVariants[0].id}"/>
          <s:hidden name="productVariantList[0].qty" value="1" class="lineItemQty"/>
          <s:hidden name="productVariantList[0].selected" value="true"/>
          <table>
            <tr><th></th><th>SPH</th><th>CYL</th><th>Axis</th><th>Add.(Bi-focal) Power</th><th>Pulp. Dist</th></tr>
            <tr><td>L</td><td><input style="width:100px"/></td><td><input style="width:100px"/></td><td><input
                style="width:100px"/></td><td><input style="width:100px"/></td><td><input style="width:100px"/></td></tr>
            <tr><td>R</td><td><input style="width:100px"/></td><td><input style="width:100px"/></td><td><input
                style="width:100px"/></td><td><input style="width:100px"/><td><input style="width:100px"/></td></tr>
            <tr><td colspan="2">Thickness</td>
              <td colspan="4"><select>
              <option>1.56 CR</option>
            </select></td></tr>
            <tr><td colspan="2">Coating</td>
              <td colspan="4"><select>
              <option>Standard Anti-Glare</option>
            </select></td></tr>
            <tr><td colspan="6"><s:submit name="addToCart" value="Place Order"
                                          class="addToCartButton cta button_green"/></td></tr>
          </table>

        </s:form>
      </s:layout-component>
    </s:layout-render>
  </div>
  </s:layout-definition>