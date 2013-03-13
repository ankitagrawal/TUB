<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.discount.OfferConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.web.action.core.discount.ApplyCouponAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.discount.ApplyCouponAction" var="couponBean"/>

<c:set var="error_role" value="<%=ApplyCouponAction.error_role%>"/>
<c:set var="error_alreadyUsed" value="<%=ApplyCouponAction.error_alreadyUsed%>"/>
<c:set var="error_alreadyApplied" value="<%=ApplyCouponAction.error_alreadyApplied%>"/>
<c:set var="error_alreadyReferrer" value="<%=ApplyCouponAction.error_alreadyReferrer%>"/>
<c:set var="error_referralNotAllowed" value="<%=ApplyCouponAction.error_referralNotAllowed%>"/>
<c:set var="error_couponExpired" value="<%=ApplyCouponAction.error_couponExpired%>"/>
<c:set var="error_freeVariantStockOver" value="<%=ApplyCouponAction.error_freeVariantStockOver%>"/>

<c:set var="infiniteQty" value="<%=OfferConstants.INFINITE_QTY%>"/>

<s:layout-render name="/layouts/modal.jsp" pageTitle="Coupon">

  <s:layout-component name="heading">Apply Coupon</s:layout-component>

  <s:layout-component name="content">
    <p class="lrg em">${couponBean.message}</p>
    
    <c:if test="${couponBean.success}">
      <table class="cont_2 offerTable">
        <thead>
        <tr>
          <th>Offer Description</th>
          <th>Valid till</th>
          <%--<th>Quantity</th>--%>
          <th>Coupon</th>
        </tr>
        </thead>
        <tr>
          <td width="200">
          ${couponBean.offerInstance.offer.description}
            <c:if test="${hk:isNotBlank(couponBean.offerInstance.offer.terms)}">
            <br/>
              <h3>Terms:</h3>
              ${hk:convertNewLineToBr(couponBean.offerInstance.offer.terms)}
            </c:if>
          </td>
          <td width="150"><fmt:formatDate value="${couponBean.offerInstance.endDate}"/></td>
          <%--<td>--%>
            <%--<span class="upc gry sml">${couponBean.offerInstance.offer.offerAction.qty}</span>--%>
          <%--</td>--%>
          <td width="100">
            <span class="upc gry sml">${couponBean.offerInstance.coupon.code != null ? couponBean.offerInstance.coupon.code : "No Coupon Code"}</span>
          </td>
        </tr>
      </table>
    </c:if>

    <c:if test="${couponBean.error != null}">
      <p>
       <%-- <c:if test="${couponBean.error eq error_role}">
          Possible cause:
          <shiro:notAuthenticated>You are not logged in to your HealthKart account</shiro:notAuthenticated>
          <shiro:hasAnyRoles name='<%=RoleConstants.HK_DEACTIVATED+","+RoleConstants.HK_UNVERIFIED%>'>
            You have not verified your email.

            <div class="box_alert_thin">
              <p>To verify your email, please click on the activation link sent to you via e-mail when signing up.</p>
              <br/>

              <p>This is needed as this offer is only applicable on verified accounts.</p>
              <br/>

              <p><strong>If you haven't received the mail,
                <s:link beanclass="com.hk.web.action.core.user.ResendAccountActivationLinkAction" event="pre" class="resendActivationEmailLink" style="text-decoration: underline;" >click here to resend it.</s:link>
              </strong> <br/>
                <span class="emailSendMessage alert" style="display:none;"></span>
                <br/>
              </p>

              <p style="display:none;" class="emailNotReceived">
                If you do not receive this email, please check your spam/bulk folder.
                <br/>Write to us at info@healthkart.com if you face problems.
              </p>
            </div>

          </shiro:hasAnyRoles>
          <shiro:hasAnyRoles name='<%=RoleConstants.TEMP_USER%>'>
            You are logged in as a guest user. Please log in to your HealthKart account.
          </shiro:hasAnyRoles>
          <shiro:hasAnyRoles name='<%=RoleConstants.HK_BLOCKED%>'>
            Your account has been blocked.
          </shiro:hasAnyRoles>
        </c:if>--%>
        <c:if test="${couponBean.error eq error_alreadyApplied}">
          The offer associated with this coupon has now been selected. <br/>

          <script type="text/javascript">
            // call pricing update
            $.getJSON($('#updatePricingLink').attr('href'), function(res) {updateTotals(res.data);});
          </script>
          <table class="cont_2 offerTable">
            <thead>
            <tr>
              <th>Offer Description</th>
              <th>Valid till</th>
              <%--<th>Quantity</th>--%>
              <th>Coupon</th>
            </tr>
            </thead>
            <tr>
              <td width="200">
                ${couponBean.offerInstance.offer.description}
                <c:if test="${hk:isNotBlank(couponBean.offerInstance.offer.terms)}">
                <br/>
                  <h3>Terms:</h3>
                  ${hk:convertNewLineToBr(couponBean.offerInstance.offer.terms)}
                </c:if>
              </td>
              <td width="150"><fmt:formatDate value="${couponBean.offerInstance.endDate}"/></td>
              <%--<td>--%>
                <%--<span class="upc gry sml">${couponBean.offerInstance.offer.offerAction.qty}</span>--%>
              <%--</td>--%>
              <td width="100">
                <span class="upc gry sml">${couponBean.offerInstance.coupon.code != null ? couponBean.offerInstance.coupon.code : "No Coupon Code"}</span>
              </td>
            </tr>
          </table>

          You can see all your offers by clicking on the "Select Available Offers" link.
        </c:if>
        <c:if test="${couponBean.error eq error_alreadyUsed}">
          You have already availed the discount using this coupon.
        </c:if>
        <c:if test="${couponBean.error eq error_alreadyReferrer}">
          You have already used someone else's referral coupon. You cannot use referral coupons from two different users.
        </c:if>
        <c:if test="${couponBean.error eq error_referralNotAllowed}">
          You have already signed up without your friend's referral. Sorry :)
        </c:if>
        <c:if test="${couponBean.error eq error_couponExpired}">
          The offer/coupon is expired. Sorry :)
        </c:if>
        <c:if test="${couponBean.error eq error_freeVariantStockOver}">
          Free variant stock is over. Sorry :)
        </c:if>
      </p>
    </c:if>

    <c:if test="${couponBean.success}">
    <script type="text/javascript">
      // call pricing update
      $.getJSON($('#updatePricingLink').attr('href'), function(res) {_updateTotals(res);});
    </script>
    </c:if>

    <%--<s:form beanclass="com.hk.web.action.core.discount.ApplyCouponAction">--%>
      <%--<table>--%>
        <%--<tr>--%>
          <%--<td><h3>Enter Coupon Code</h3></td>--%>
          <%--<td><s:text name="couponCode" style="padding: 3px; font-size: 1.2em;"/></td>--%>
          <%--<td><div class="buttons"><s:submit name="apply" value="Apply Coupon"/></div></td>--%>
        <%--</tr>--%>
      <%--</table>--%>
    <%--</s:form>--%>

    <script type="text/javascript">
      $('#activateAccount').jqm({trigger: '.activateAccountLink'});

      <%-- Re-Send Activation Link --%>
      $('.resendActivationEmailLink').click(function() {

        var clickedLink = $(this);
        var clickedP = clickedLink.parents('p');
        clickedP.find('.emailSendMessage').html($('#ajaxLoader').html()).show();
        $.getJSON(clickedLink.attr('href'), function(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            clickedP.find('.emailSendMessage').html(res.data.message).show();
            $('.emailNotReceived').show();
          }
        });
        return false;
      });
    </script>

  </s:layout-component>

</s:layout-render>
