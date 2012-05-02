<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.affiliate.AffiliateManageAddressAction" var="affiliateBean" event="showAddressBook"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Account</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div class='left2'>
      <div>
        <s:form beanclass="web.action.affiliate.AffiliateManageAddressAction">

          <c:if test="${!empty affiliateBean.addresses}">
            <h3 style="text-align: left;">
              Use one of your saved addresses &darr;
            </h3>
            <c:forEach var="address" items="${affiliateBean.addresses}" varStatus="addressCount">
              <div class="address">
                <h5 class="name">${address.name}</h5>

                <div class='street street1'>${address.line1}</div>
                <c:if test="${hk:isNotBlank(address.line2)}">
                  <div class="street street1">${address.line2}</div>
                </c:if>
                <div class='city'>${address.city}</div>
                <div class='state'>${address.state}</div>
                <div class='pin'>${address.pin}</div>
                <div class='phone'>${address.phone}</div>
                </br>
                <s:checkbox name="addresses[${addressCount.index}].selected"/>
                <s:link beanclass="web.action.affiliate.AffiliateManageAddressAction" event="setAsDefaultAddress" class="save" onclick="return confirm('Your cheque will sent here!!')">
                  <s:param name="address" value="${address.id}"/>
                  <s:param name="affiliate" value="${affiliateBean.affiliate.id}"/>
                  (Set as Default)
                </s:link>
                <s:link beanclass="web.action.affiliate.AffiliateManageAddressAction" event="remove" class="delete" onclick="return confirm('Are you sure you want to delete this address?')">
                  <s:param name="address" value="${address.id}"/>
                  (delete)
                </s:link>
              </div>
            </c:forEach>
          </c:if>
          <div class='tip'>
            <s:link beanclass="web.action.affiliate.AffiliateManageAddressAction" event="addNewAddress">Add New Address</s:link>
          </div>
        </s:form>
      </div>
    </div>
  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myAddressesLink').style.fontWeight = "bold";
  };
</script>