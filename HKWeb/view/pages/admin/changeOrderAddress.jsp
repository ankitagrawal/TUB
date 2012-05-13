<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction" var="orderBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
  <s:layout-component name="heading">${orderBean.currentBreadcrumb.name}</s:layout-component>

  <s:layout-component name="content">

    Order Id: ${orderBean.order.id} <br/>
    User Name: ${orderBean.order.user.name} <br/>
    User Login: ${orderBean.order.user.login} <br/>

    <h2> Address: </h2>
    <table border="1">
      <c:set value="${orderBean.order}" var="order"/>
        <tr>
          <td width="200">
              ${orderBean.order.address.name}<br/>
              ${orderBean.order.address.line1}<br/>
              ${orderBean.order.address.line2}<br/>
              ${orderBean.order.address.city} - ${orderBean.order.address.pin}<br/>
              ${orderBean.order.address.state}<br/>
            Ph: ${orderBean.order.address.phone}<br/>
          </td>
          <td width="75">
            <s:link beanclass="com.hk.web.action.admin.address.AdminAddressListAction">
              Get from address book
              <s:param name="order" value="${orderBean.order.id}"/>
            </s:link>
          </td>
          <td width="75">
            <s:link beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction" event="edit">
              edit
              <s:param name="order" value="${orderBean.order.id}"/>
            </s:link>
          </td>
        </tr>
    </table>
    <br/>
    <s:form action="${orderBean.previousBreadcrumb.url}">
      <s:hidden name="order" value="${orderBean.order.id}"/>
      <s:submit name="show" value="Continue ..." class="btn"/>
    </s:form>

  </s:layout-component>

</s:layout-render>
