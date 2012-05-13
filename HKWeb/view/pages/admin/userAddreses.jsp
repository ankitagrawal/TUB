<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.address.AdminAddressListAction" var="addressBean"/>
    <s:form beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction" method="post">
      <h2>Select Address: </h2>
      <table>
        <c:forEach items="${addressBean.addressList}" var="address" varStatus="addressCount">
          <tr>
            <td>
              <s:hidden name="order" value="${addressBean.order}"/>
              <s:radio name="address" value="${address.id}" checked="${address.selected}"/>
            </td>
            <td>
                ${address.name} <br/>
                ${address.line1}<br/>
                ${address.line2}<br/>
                ${address.city} - ${address.pin}<br/>
                ${address.state}<br/>
              Ph: ${address.phone}<br/>
            </td>
          </tr>
        </c:forEach>
      </table>
      <s:submit name="replace" value="submit" class="butt"/>
    </s:form>
    <s:link beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction">
      cancel
      <s:param name="order" value="${addressBean.order}"/>
    </s:link>
  </s:layout-component>

</s:layout-render>
