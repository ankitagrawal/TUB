<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.service.user.UserProfileService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.order.OrderDetailsAction" var="oa"/>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa" event="pre"/>

<s:layout-render name="/layouts/defaultBeta.jsp">
<s:layout-component name="heading">My Account</s:layout-component>
<s:layout-component name="centralContent">

    <%--breadcrumbs begins--%>
    <div class="hk-breadcrumb-cntnr mrgn-bt-10">
            <span>
               <s:link beanclass="com.hk.web.action.HomeAction">Home</s:link>
            </span>
        <span>&raquo;</span>
        <span class="txt-blue fnt-bold">Account</span>
    </div>
    <%--breadcrumbs ends--%>

    <shiro:hasRole name="<%=RoleConstants.HK_UNVERIFIED%>">
        <div class="err-cntnr">
            <span class="icn-warning-small"></span>

            <div>
                <ul>
                    <li>Your email id is not verified, kindly click on the link sent to your mail to apply offer coupons.
                        <s:link beanclass="com.hk.web.action.core.user.ResendAccountActivationLinkAction" event="pre"
                                class="resendActivationEmailLink">click here</s:link> to resend the email.
                    </li>
                </ul>
                <p class="emailSendMessage alert" style="display: none; font-weight:bold;"></p>
                <p style="display:none;" class="emailNotReceived">
                    If you do not receive this email, please check your spam/bulk folder.
                    Write to us at info@healthkart.com if you face problems.
                </p>
            </div>
            <span class="icn icn-close2 remove-error"></span>
        </div>

        <script type="text/javascript">

            <%-- Re-Send Activation Link --%>
            $('.resendActivationEmailLink').click(function() {

                var clickedLink = $(this);
                var clickedP = clickedLink.parents('div');
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
    </shiro:hasRole>

</s:layout-component>

<s:layout-component name="lhsContent">
  <jsp:include page="myaccount-navBeta.jsp"/>
  <%
  UserProfileService userProfileService = ServiceLocatorFactory.getService(UserProfileService.class);
  %>

  <c:set var="productVariants" value="<%=userProfileService.getRecentlyOrderedProductVariantsForUser(maa.getUser())%>"/>
  <c:set var="recentOrders" value="<%=userProfileService.getOrdersForUserSortedByDate(maa.getUser())%>"/>
  <c:set var="addresses" value="${maa.addresses}"/>

  <fieldset>
    <c:if test="${!empty productVariants}">
      <ul style="list-style: none; line-height: 25px;max-width: 220px;word-break: break-word; margin-top: 25px;">
        <s:label name="Recently Ordered Items" />
        <s:form beanclass="com.hk.web.action.core.cart.AddToCartAction" class="addToCartForm">
          <fieldset>
            <c:forEach items="${productVariants}" var="variant" varStatus="ctr">
                <c:set var="storeVariantBasic" value="${hk:getStoreVariantBasicDetails(variant.id)}"/>
                <div class="cont footer_color" width="100%" style="font-size: smaller;">
                <div style="border-bottom: 1px solid #f0f0f0;">
                  <s:hidden name="productVariantList[${ctr.index}]" value="${variant.id}"/>
                  <s:checkbox name="productVariantList[${ctr.index}].selected" class="lineItemCheckBox"/>
                  <s:hidden name="productVariantList[${ctr.index}].qty" value="1" class="lineItemQty"/>
                  <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" class="prod_link">
                    <s:param name="productId" value="${variant.product.id}"/>
                    <s:param name="productSlug" value="${variant.product.slug}"/>
                    <%--${variant.product.name}--%>
                    ${storeVariantBasic.name}
                  </s:link>
                </div>
              </div>
            </c:forEach>
            <div align="center" style="text-align: left;margin-top:15px;">
              <s:submit name="addToCart" value="Place Order" class="addToCartButton btn btn-blue"/>
            </div>

          </fieldset>
        </s:form>

      </ul>
    </c:if>
  </fieldset>
</s:layout-component>

<s:layout-component name="rhsContent">




<div class="mrgn-l-40 my-acnt-ht">

<div class="basicInformation" style="float:left; margin-top: 5px; margin-bottom: 5px; width: 100%;">
  <s:form beanclass="com.hk.web.action.core.user.MyAccountAction">
    <s:errors/>
    <h2 class="strikeline"> Basic Information</h2>

    <div style="float:left; width:100% ">

      <div class="row">
        <label class="rowLabel">Name</label>
        <label class="rowText">${maa.user.name}</label>
      </div>

      <div class="clear"></div>

      <div class="row">
        <label class="rowLabel">Email id</label>
        <label class="rowText">${maa.user.email}</label>
      </div>

      <div class="clear"></div>

      <shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
        <s:hidden name="b2bUserDetails" value="${maa.b2bUserDetails.id}"/>
        <s:hidden name="b2bUserDetails.user" value="${maa.user}"/>
        <div class="row">
          <label class="rowLabel">TIN#</label>
          <label class="rowText">
            <c:choose>
              <c:when test="${maa.b2bUserDetails.tin != null}">
                ${maa.b2bUserDetails.tin}
              </c:when>
              <c:otherwise>
                --
              </c:otherwise>
            </c:choose>
          </label>
        </div>

        <div class="clear"></div>

        <div class="row">
          <label class="rowLabel">DL Number</label>
          <label class="rowText">
            <c:choose>
              <c:when test="${maa.b2bUserDetails.dlNumber != null}">
                ${maa.b2bUserDetails.dlNumber}
              </c:when>
              <c:otherwise>
                --
              </c:otherwise>
            </c:choose>
          </label>
        </div>

        <div class="clear"></div>
      </shiro:hasRole>

      <div class="row">
        <label class="rowLabel">Gender</label>
        <label class="rowText">
          <c:choose>
            <c:when test="${maa.user.gender != null}">
              ${maa.user.gender}
            </c:when>
            <c:otherwise>
              --
            </c:otherwise>
          </c:choose>
        </label>
      </div>
        <div class="clear"></div>
        <div class="row">
            <label class="rowLabel">DOB</label>
            <label class="rowText" style="">
                <c:choose>
                    <c:when test="${maa.user.birthDate != null}">
                        <%--<s:label class="rowText" name="<%=FormatUtils.getFormattedDateForUserEnd(maa.getUser().getBirthDate())%>" style=" font-size: 0.8em;"/>--%>
                        <%=FormatUtils.getFormattedDateForUserEnd(maa.getUser().getBirthDate())%>
                    </c:when>
                    <c:otherwise>
                        --
                    </c:otherwise>
                </c:choose>
            </label>
        </div>

    </div>
    <div class="clear"></div>

      <s:hidden name="user" value="${maa.user}"/>
      <div style="float:left; width:100%; text-align: right">
        <s:submit class="btn btn-blue dis" style="display:inline-block; " name="editPassword" value="Change Password"/>
        <s:submit class="btn btn-blue" style="display:inline-block; " name="editBasicInformation" value="Edit Profile"/>
      </div>
  </s:form>
</div>

<div class="contactInformation" style="width: 100%; margin-top: 20px; margin-bottom: 5px; float:left;">
  <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction">
    <h2 class="strikeline"> Contact Information</h2>

    <%--<c:if test="${!empty addresses}">--%>
    <c:set var="address" value="${maa.affiliateDefaultAddress}"/>
    <%--</c:if>--%>

    <c:choose>
      <c:when test="${!empty addresses}">
        <c:choose>
          <c:when test="${address == null}">
            <c:choose>
              <c:when test="${!(empty recentOrders || (recentOrders[0].address.deleted == true))}">
                <c:set var="address" value="${recentOrders[0].address}"/>
              </c:when>
              <c:otherwise>
                <c:set var="address" value="${addresses[0]}"/>
              </c:otherwise>
            </c:choose>
          </c:when>
        </c:choose>

        <div>
          <div style="font-size:small; width:100%; margin-top: 15px;">
            <p>${address.line1} ${address.line2}</p>

            <p>${address.city}</p>

            <p>${address.state} ${address.pincode.pincode}</p>

            <p>Phone: ${address.phone}</p>
          </div>
          <div style=" width:100%; text-align: right; ">
            <s:link class="btn btn-gray" beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="manageAddresses"
                    style="font-size:small; color:black; text-align:center;display:inline-block;">View all addresses
              <%--<s:param name="address.id" value="${address.id}"/>--%>
              <%--<s:param name="user" value="${maa.user}"/>--%>
            </s:link>
          </div>
        </div>
      </c:when>
      <c:otherwise>
        <div style="font-size: small;">&nbsp No contact information yet</div>
        <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="manageAddresses"
                style="float:right; font-size:small; color:black; text-align:center;">Add Address
        </s:link>
      </c:otherwise>
    </c:choose>
  </s:form>
</div>

<div class="recentOrders" style="width:100%; margin-top: 5px; margin-bottom: 5px; float:left;">
  <div style="margin-top: 10px"></div>
  <c:if test="${!empty recentOrders}">
    <s:form beanclass="com.hk.web.action.core.user.MyAccountAction">
      <h2 class="strikeline"> Recent Orders</h2>
        <table class="order-tbl">
            <tr class="order-specs-hdr btm-brdr">
                <th class="fnt-bold">Order Id</th>
                <th class="fnt-bold">Order Date</th>
                <th class="fnt-bold">Invoices</th>
                <th class="fnt-bold">Order Status</th>
            </tr>
            <tbody>
                <c:forEach items="${recentOrders}" end="2" var="order" varStatus="ctr">
                <c:if test="${ctr.first}">
                <tr class="order-tr top-brdr">
                    </c:if>
                    <c:if test="${ctr.last}">
                <tr class="${ctr.index%2==0? 'order-tr btm-brdr':'order-tr btm-brdr bg-gray'}">
                    </c:if>
                    <c:if test="${!(ctr.first || ctr.last)}">
                <tr class="${ctr.index%2==0? 'order-tr':'order-tr bg-gray'}">
                    </c:if>
                    <td>
                        ${order.gatewayOrderId}
                      <s:link beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" target="_blank">
                        <s:param name="order" value="${order}"/>
                        (View Order)
                      </s:link>
                    </td class="border-td">
                    <td>
                      <fmt:formatDate value="${order.payment.paymentDate}" pattern="dd/MM/yyyy"/>
                    </td>
                    <td class="border-td">
                      <c:set var="shippingOrders" value="${order.shippingOrders}"/>
                      <c:choose>
                        <c:when test="${!empty shippingOrders}">
                          <c:forEach items="${shippingOrders}" var="shippingOrder">
                            <%--<p>--%>
                            <s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" event="pre" target="_blank">
                              <s:param name="shippingOrder" value="${shippingOrder.id}"/>
                              R-${shippingOrder.id}
                            </s:link>
                            <%--</p>--%>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>
                          <%--<p>--%>
                          <s:link beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" event="pre" target="_blank">
                            <s:param name="order" value="${order.id}"/>
                            R-${order.id}
                          </s:link>
                          <%--</p>--%>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                        ${order.orderStatus.name}
                      <s:link beanclass="com.hk.web.action.core.order.OrderDetailsAction" event="pre" target="_blank">
                        <s:param name="order" value="${order.id}"/>
                        (View Order Details)
                      </s:link>
                    </td>
                </tr>
                </c:forEach>
            <tbody>
      </table>
    </s:form>
  </c:if>
</div>
</div>
</s:layout-component>
</s:layout-render>
<script type="text/javascript">
  window.onload = function() {
      $('#myAccountLink').addClass('selected');
  };

  $(document).ready(function() {

    $('.addToCartButton').click(function(e) {
      var check = 0;
      $('.lineItemCheckBox').each(function() {
        if ($(this).attr("checked") == "checked") {
          check = 1;
        }
      });
      if (!check) {
        alert("Please select the product(s) to be ordered!");
        return false;
      }
      else {
        show_message();
        e.stopPropagation();
      }
    });

      $('.remove-error').click(function () {
          $(this).parent('.err-cntnr').remove();
      });
      $('.remove-success').click(function () {
          $(this).parent('.alert-cntnr').remove();
      });

      $(".message .close").click(function() {
      hide_message();
      location.reload(true);
    });

    $(document).click(function() {
      hide_message();
    });

    $('.addToCartForm').ajaxForm({dataType: 'json', success: _addToCart});
    $('.addToCartForm2').ajaxForm({dataType: 'json', success: _addToCart2});
    $('.addToCartButton').click(function() {
      $(this).parents('td').find('.progressLoader').show();
      $('#cartWindow').jqm();
    });

    $(".top_link, .go_to_top").click(function(event) {
      event.preventDefault();
      $('html,body').animate({scrollTop:($(this.hash).offset().top - 45)}, 300);
    });

    function hide_message() {
      $('.message').animate({
        top: '-170px',
        opacity: 0
      }, 100);
    }

    function show_message() {
      $('.message').css("top", "70px");
      $('.message').animate({
        opacity: 1
      }, 500);
    }

    function _addToCart(res) {
      if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
        $('.lineItemCheckBox').each(function() {
          if ($(this).attr("checked") == "checked") {
            this.checked = false;
            this.disabled = true;
          }
        });
        $('.message .line1').html("<strong>" + res.data.addedProducts + "</strong> has been added to your shopping cart");
        $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
        //        $('.progressLoader').hide();
      } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
        $('#cart_error1').html(getErrorHtmlFromJsonResponse(res))
            .slowFade(3000, 2000);
      } else if (res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>') {
        window.location.replace(res.data.url);
      }
      $('.progressLoader').hide();
    }

    function _addToCart2(res) {
      if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
        $('.lineItemCheckBox').each(function() {
          if ($(this).attr("checked") == "checked") {
            this.checked = false;
            this.disabled = true;
          }
        });
        $('.message .line1').html("<strong>" + res.data.name + "</strong> is added to your shopping cart");
      } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
        $('#cart_error1').html(getErrorHtmlFromJsonResponse(res))
            .slowFade(3000, 2000);
      } else if (res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>') {
        window.location.replace(res.data.url);
      }
      $('.lineItemCheckBox').each(function() {
        if ($(this).attr("checked") == "checked") {
          this.checked = false;
          $(this).attr("disable") = true;
        }
      });
      $('.progressLoader').hide();
    }

  });
</script>
<style type="text/css">
  .row {
    margin-top: 0;
    float: left;
    margin-left: 0;
    padding-top: 2px;

  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    width: 70px;
    height: 24px;
    margin-top: 5px;
  }

  .rowText {
    float: left;
    padding-right: 5px;
    padding-left: 5px;
    height: 24px;
    margin-top: 5px;
  }

  p {
    margin-top: 2px;
    margin-bottom: 2px;
  }

</style>