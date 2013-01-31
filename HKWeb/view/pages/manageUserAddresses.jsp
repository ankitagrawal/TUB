<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.UserManageAddressAction" var="userBean" event="showAddressBook"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">My Account</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction">
      <div class='left2' style="width:100%">
        <div class="address" style="font-size: 0.813em; border: 0">
          <c:set var="addresses" value="${userBean.addresses}"/>
          <c:if test="${!empty addresses}">
            <c:set var="mainAddressId" value="${userBean.affiliate.mainAddressId}"/>
            <c:forEach var="address" items="${addresses}" varStatus="addressCount">

              <h5 class="name">${address.name}</h5>

              <div class='street street1'>${address.line1}</div>
              <c:if test="${hk:isNotBlank(address.line2)}">
                <div class="street street1">${address.line2}</div>
              </c:if>
              <div class='city'>${address.city}</div>
              <div class='state'>${address.state}</div>
              <div class='pin'>${address.pincode.pincode}</div>
              <div class='phone'>${address.phone}</div>
              <p>
                <shiro:hasAnyRoles
                    name='<%=RoleConstants.HK_AFFILIATE + "," + RoleConstants.HK_AFFILIATE_UNVERIFIED%>'>
                  <c:choose>
                    <c:when test="${mainAddressId != address.id}">
                      <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="setAsDefaultAddress"
                              class="save" onclick="return confirm('Your cheque will sent here!!')">
                        <s:param name="address" value="${address.id}"/>
                        <s:param name="affiliate" value="${userBean.affiliate.id}"/>
                        (Set as Default)
                      </s:link>
                    </c:when>
                    <c:otherwise>
                      <strong>(Default Address Set)</strong>
                    </c:otherwise>
                  </c:choose>
                  <%--<s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="setAsDefaultAddress"--%>
                  <%--class="save" onclick="return confirm('Your cheque will sent here!!')">--%>
                  <%--<s:param name="address" value="${address.id}"/>--%>
                  <%--<s:param name="affiliate" value="${userBean.affiliate.id}"/>--%>
                  <%--(Set as Default)--%>
                  <%--</s:link>--%>
                </shiro:hasAnyRoles>

                <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="editUserAddresses" class="edit">
                  <s:param name="address" value="${address.id}"/>
                  (edit)
                </s:link>

                <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="remove" class="delete"
                        onclick="return confirm('Are you sure you want to delete this address?')">
                  <s:param name="address" value="${address.id}"/>
                  (delete)
                </s:link>
              </p>

            </c:forEach>

          </c:if>

          <div class='tip' style="float:right;">
            <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="editUserAddresses">Add New Address</s:link>
          </div>
        </div>
      </div>
    </s:form>
  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myAddressesLink').style.fontWeight = "bold";
  };
</script>