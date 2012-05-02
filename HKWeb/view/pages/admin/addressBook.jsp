<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="web.action.admin.SelectAddressAction" event="pre" var="addressBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="heading">${addressBean.currentBreadcrumb.name}</s:layout-component>

  <s:layout-component name="content">

    <s:form beanclass="web.action.admin.SelectAddressAction" method="get">
      <s:hidden name="order" value="${addressBean.order.id}"/>
      <table class="cont item_list_table">
        <c:forEach items="${addressBean.addresses}" var="address" varStatus="addressCount">
          <tr>
            <s:hidden name="addresses[${addressCount.index}]" value="${address.id}"/>
            <td>
              <h3>
                <s:checkbox name="addresses[${addressCount.index}].selected" class="cbox"/>&nbsp; ${address.name}</h3>
            </td>
            <td>${address.line1}
              <c:if test="${not empty address.line2}">
                ${address.line2},
              </c:if>
              <br/>
                ${address.city} - ${address.pin},
                ${address.state}<br/>
              <span class="sml lgry upc">Phone </span>${address.phone}<br/></td>
          </tr>
        </c:forEach>
      </table>
      <s:submit name="selectAddress" value="Continue"/>
    </s:form>
    <s:link beanclass="web.action.admin.CheckPaymentAction" event="show">
      Cancel
      <s:param name="order" value="${addressBean.order.id}"/>
    </s:link>

  </s:layout-component>

</s:layout-render>
