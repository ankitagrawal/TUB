<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.UserManageAddressAction" var="userBean"
                 event="showAddressBook"/>
<s:layout-render name="/layouts/defaultBeta.jsp">
  <s:layout-component name="heading">Addresses</s:layout-component>
  <s:layout-component name="lhsContent">
        <jsp:include page="myaccount-navBeta.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction">
      <div class='left2 my-acnt-ht' >
        <c:set var="addresses" value="${userBean.addresses}"/>
        <c:if test="${!empty addresses}">
          <c:set var="mainAddressId" value="${userBean.affiliate.mainAddressId}"/>
          <c:forEach var="address" items="${addresses}" varStatus="addressCount">
            <div class="address raj_address usr-add-cntnr" style="height: 200px; float:left;width: 250px;display:inline-block;margin-right: 10px;">
              <h5 class="name fnt-caps adresss-usr-name">${address.name}</h5>

                <div style="overflow-y: auto;max-height: 120px;overflow-x: hidden;">
                    <div class='street street1'>${address.line1}</div>
                    <c:if test="${hk:isNotBlank(address.line2)}">
                        <div class="street street2">${address.line2}</div>
                    </c:if>
                    <div class='city address-cityId'>${address.city}</div>
                    <div class='state'>${address.state}</div>
                    <div class='pin'>${address.pincode.pincode}</div>
                    <div class='phone'>${address.phone}</div>
                </div>
                <shiro:hasAnyRoles
                    name='<%=RoleConstants.HK_AFFILIATE + "," + RoleConstants.HK_AFFILIATE_UNVERIFIED%>'>
                  <c:choose>
                    <c:when test="${mainAddressId != address.id}">
                      <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction"
                              event="setAsDefaultAddress"
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
                </shiro:hasAnyRoles>
                <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="editUserAddresses"
                        class="edit btn btn-gray" style="width:65px;float:left; margin:10px 20px 0 0;">
                  <s:param name="address" value="${address.id}"/>
                  edit
                </s:link>
                <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="remove"
                        class="delete btn btn-gray" style="width:82px;float:left;margin:10px 20px 0 0;">
                  <s:param name="address" value="${address.id}"/>
                  delete
                </s:link>
            </div>
          </c:forEach>
        </c:if>
          <div class="tip " style="position: relative;clear: both;width: 225px;">
              <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="editUserAddresses"
                      class="btn btn-blue">Add New Address</s:link>
          </div>
      </div>

    </s:form>
  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function () {
    $('#myAddressesLink').addClass('selected');
  };
  $(document).ready(function () {
    $('.delete').click(function () {
      if (confirm('Are you sure you want to delete this address?')) {
        bool = true;
        return true;
      } else {
        return false;
      }
    });
  });
</script>
<style type="text/css">
    .rhsContent{margin-left: 20PX;
    width: 700PX;}
</style>