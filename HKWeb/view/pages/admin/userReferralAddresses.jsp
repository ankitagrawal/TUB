<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.user.UserReferrralsAddressesAction" var="addressBean"/>
    <s:form beanclass="com.hk.web.action.admin.user.UserReferrralsAddressesAction" method="post">
      <h2>Previous Referral Addresses</h2>
      <c:forEach items="${addressBean.referredUsers}" var="refUser" varStatus="userCount">
        <c:forEach items="${refUser.addresses}" var="address" varStatus="addressCount">
          <table>
            <tr>
              <td>
                  ${address.name} <br/>
                  ${address.line1}<br/>
                  ${address.line2}<br/>
                  ${address.city} - ${address.pincode.pincode}<br/>
                  ${address.state}<br/>
                Ph: ${address.phone}<br/>
              </td>
            </tr>
          </table>
        </c:forEach>
        <hr/>
      </c:forEach>
    </s:form>
  </s:layout-component>
</s:layout-render>