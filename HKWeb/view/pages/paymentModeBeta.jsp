<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.payment.EnumPaymentType" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ include file="/layouts/_userData.jsp" %>

<s:useActionBean
        beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre"
        var="orderSummary"/>
<s:useActionBean beanclass="com.hk.web.action.core.payment.PaymentModeAction"
                 event="pre" var="paymentModeBean"/>

<%
    Double codMaxAmount = Double.parseDouble((String) ServiceLocatorFactory.getProperty(Keys.Env.codMaxAmount));
    Double codMinAmount = Double.parseDouble((String) ServiceLocatorFactory.getProperty(Keys.Env.codMinAmount));
    Double codCharges = Double.parseDouble((String) ServiceLocatorFactory.getProperty(Keys.Env.codCharges));
    Long defaultGateway = Long.parseLong((String) ServiceLocatorFactory.getProperty(Keys.Env.defaultGateway));
		boolean isSecure = WebContext.isSecure();
    pageContext.setAttribute("isSecure", isSecure);
%>
<c:set var="codMaxAmount" value="<%=codMaxAmount%>"/>
<c:set var="codMinAmount" value="<%=codMinAmount%>"/>
<c:set var="codCharges" value="<%=codCharges%>"/>
<c:set var="orderDate" value="<%=new DateTime().toDate()%>"/>
<c:set var="prePaidPaymentType" value="<%=EnumPaymentType.PrePaid.getId()%>"/>

<s:layout-render name="/layouts/checkoutLayout.jsp"
                 pageTitle="Payment Options">
<s:layout-component name="htmlHead">
    <script type="text/javascript"
            src="<hk:vhostJs/>/otherScripts/jquery.session.js"></script>
</s:layout-component>
<s:layout-component name="steps">
    <s:layout-render name="layouts/embed/_c" />
</s:layout-component>


<s:layout-component name="steps_content">
<div class='current_step_content step3'>
<jsp:include
        page="/includes/checkoutNotice.jsp"/>

<c:if test="${paymentModeBean.showFailureMessage}">
        <div class="paymentFailureMessageTop">
            <h4> We are extremely sorry but your payment could not be processed.</h4>
            <p> The reason for this might be a network error or a communication error between the bank and the payment gateway.</p>
            <p> The transaction id ${paymentModeBean.paymentFailureGatewayOrderId}. In case, any money has been deducted from your account please contact our customer care on 0124-4616444 or write to us at <a href="mailto:info@healthkart.com">info@healthkart.com</a></p>
            <p> We request you to please try the payment again with a different payment mode or a different card. Sometimes,
                trying with the same card might also work as you may have entered some details incorrectly.</p>
        </div>
 </c:if>

<div class='pre'>
            <div id="CODOption" style="display: none;">
                <h4>Your total billable amount is <strong class='num arialBold'> <fmt:formatNumber
                        value="${orderSummary.pricingDto.grandTotalPayable + orderSummary.codCharges}" type="currency"
                        currencySymbol="Rs "/> </strong></h4>
            </div>

            <div id="nonCODOption">
                <h4>Your total billable amount is <strong class='num arialBold'> <fmt:formatNumber
                        value="${orderSummary.pricingDto.grandTotalPayable}" type="currency"
                        currencySymbol="Rs "/> </strong></h4>
            </div>
    <h6>If you have any trouble during the payment process, call our
        helpline number <strong class='arialBlackBold'> 0124 - 4616444 </strong></h6>
</div>

<div class="alert messages" style="font-size: 14px; color: red">
    <s:errors/><s:messages key="generalMessages"/></div>

    <%--<div style="border: 1px solid gray; padding: 10px; background-color: darkred; color: white; text-align: center; font-size: 1.3em;">
      Our payment gateway is experiencing techincal problems. Please try cash on delivery. <br/>
      We are working on getting the payment gateway back up asap.
    </div>--%>

<div class='payment_container'><c:choose>
<c:when
        test="${orderSummary.pricingDto.grandTotalPayable == 0 && orderSummary.pricingDto.productLineCount > 0}">
    <s:form
            beanclass="com.hk.web.action.core.payment.FreeCheckoutConfirmAction"
            method="post">
        <div class="buttons"><s:submit name="confirm"
                                       value="Confirm Order" class="butt"
                                       disabled="${fn:length(orderSummary.pricingDto.outOfStockLineItems) > 0 ? 'true':'false'}"/>
        </div>
    </s:form>
</c:when>
<c:otherwise>

<%--<div style="visibility: hidden;" class="offer-banner"><img
                    src="<hk:vhostImage/>/images/banners/pay_online_banner.jpg">
                </div>--%>
<div style="display: none;"><s:link
        beanclass="com.hk.web.action.admin.SetInCookieAction"
        id="setInCookieLink"/></div>
<div class='outer'>
<div class='left_controls tabs'>
    <ul>
        <li class='selected' id="tab1">Credit/Debit Cards</li>
        <li id="tab3">Internet Banking</li>
        <shiro:lacksRole name="<%=RoleConstants.COD_BLOCKED%>">
            <c:if test="${orderSummary.order.offerInstance.offer.paymentType != prePaidPaymentType}">
                <li id="tab4" class="cod-mode">Cash on Delivery</li>
	            <shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
	              <li id="tab5">Cheque / Bank Deposit</li>
	            </shiro:hasRole>
	            <c:if test="${orderSummary.pricingDto.grandTotalPayable > 20000}">
                <li id="tab5">NEFT Deposit</li>
	            </c:if>
            </c:if>
        </shiro:lacksRole>
        <shiro:hasRole name="<%=RoleConstants.GOD%>">
            <li id="tab6">Counter Cash</li>
        </shiro:hasRole>
    </ul>
</div>
<div class='right_content'>
<div id="tabs_content1" class="tab_content"><s:form
        beanclass="com.hk.web.action.core.payment.PaymentAction" method="post">
    <s:hidden name="order" value="${orderSummary.order.id}" />

    <table><c:forEach items="${paymentModeBean.cardIssuers}" var="cardIssuer">
        <tr>
            <td style="padding: 4px;"><s:radio name="issuer" value="${cardIssuer.id}"
                         id="${cardIssuer.name}"/></td>
            <td style="padding: 4px;"><img src="<hk:vhostImage/>${hk:readIssuerImageIcon(cardIssuer.imageIcon, cardIssuer.name)}"
                                           height="30px" alt="gateway image">
            </td>
            <td style="padding: 4px;">${cardIssuer.name}<br/>
                <label style="font-size: .9em;font-weight: bold;color: #000000;">${cardIssuer.tagLine}</label>
            </td>

        </tr>
        <%--check for paypal, give it an id so that js can work--%>
    </c:forEach>
    </table>
    <div style="float: right; width: 90%;"><s:submit
            name="proceed" value="Make Payment" class="button makePayment signUpButtonNew" style="width: 125px;left: 0px !important;"
            disabled="${fn:length(orderSummary.pricingDto.outOfStockLineItems) > 0 ? 'true':'false'}" />
    </div>
</s:form></div>
<div id="tabs_content3" class="tab_content" style="display: none;">
    <s:form beanclass="com.hk.web.action.core.payment.PaymentAction"
            method="post">
        <s:hidden name="order" value="${orderSummary.order.id}" />

        <div style="float: left; margin-left: 20px; line-height: 21px;">
            <div class="paymentBox">
                <table width="100%">
                    <c:forEach items="${paymentModeBean.bankIssuers}" var="bankIssuer"
                               varStatus="idx">
                        <c:if test="${idx.index%2 == 0}">
                            <tr>
                        </c:if>
                        <td class="col"><s:radio name="issuer" value="${bankIssuer.id}" />${bankIssuer.name}
                        </td>
                        <c:if test="${idx.index%2 == 1 || idx.last}">
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
                <div class="floatfix"></div>
            </div>
        </div>
        <div style="float: right; width: 90%;"><s:submit
                name="proceed" value="Make Payment" class="button makePayment signUpButtonNew" style="width: 125px;left: 0px !important;"
                disabled="${fn:length(orderSummary.pricingDto.outOfStockLineItems) > 0 ? 'true':'false'}" />
        </div>
    </s:form></div>
<shiro:lacksRole name="<%=RoleConstants.COD_BLOCKED%>">
    <c:if test="${orderSummary.order.offerInstance.offer.paymentType != prePaidPaymentType}">
        <div id="tabs_content4" class="tab_content" style="display: none;">
            <c:set var="message"
                   value=" <h4 style=\"text-align: center;\">We are sorry Cash on Delivery is not available for your order</h4>"/>
            <c:set var="codFailureMap" value="${paymentModeBean.codFailureMap}"/>

            <c:choose>
                 <c:when test='${codFailureMap["MutipleRTOs"] == "Y"}'>
                    ${message}
                     <shiro:hasRole name="<%=RoleConstants.ADMIN%>">
                       <p>Due to multiple RTOs.</p>  
                     </shiro:hasRole>
                </c:when>
                <c:when test='${codFailureMap["CodOnAmount"] == "N" }'>
                    ${message}
                    <p>The net payable is not in the range of <strong>Rs. ${codMinAmount} -
                        Rs. ${codMaxAmount}</strong></p>
                </c:when>
                <c:when test='${codFailureMap["CodAllowedOnProduct"] == "N" }'>
                    ${message}
                    <p>COD is not allowed on the product :
                        <strong> ${codFailureMap["ProductName"]} </strong>. </p>
                </c:when>
                <c:when test='${codFailureMap["CodOnSubscription"] == "N" }'>
                    ${message}
                    <p>You have subscriptions in your cart</p>
                </c:when>
                <c:when test='${codFailureMap["OverallCodAllowedByPincodeProduct"] == "N" }'>
                    ${message}
                    <p>We do not provide cod services at your Pincode, Please contact customer care for any issues and concerns</p>
                </c:when>

                <c:otherwise>
                    <div class="grid_5" style="width: 100%;">
                        <h4>Order Total</h4>
                        <br/>

                        <div class="leftCOD">
                            <p>Order Total</p>
                            <p>COD Charges</p>
                            <p><strong class="orangeBold">Grand Total</strong></p>
                        </div>
                        <div class="rightCOD">
                            <p>
                                <fmt:formatNumber value="${orderSummary.pricingDto.grandTotalPayable}"
                                    currencySymbol="Rs. " type="currency"/>  (Inclusive of Shipping) <br/>
                            </p>
                            <p><fmt:formatNumber
                                    value="${orderSummary.codCharges}" currencySymbol="Rs. "
                                    type="currency"/> <br/>
                            </p>
                            <p>
                                <strong
                                    class="orangeBold"><fmt:formatNumber
                                    value="${orderSummary.pricingDto.grandTotalPayable + orderSummary.codCharges}"
                                    currencySymbol="Rs. " type="currency"/>
                                </strong><br/>
                            </p>
                        </div>
                    </div>
                    <h4 class="codContact">Contact Details</h4>

                    <p>Please verify the name and contact number of the person
                        who would receive this order. <br/>  <br/>
                        You will receive an automated call on your contact phone. Please take the call and respond as per instructions to verify
                        your order instantly. In case you miss the call, our agent will call you again to verify. Once verified, your order will go into processing.</p>
                    <s:form
                            beanclass="com.hk.web.action.core.payment.CodPaymentReceiveAction"
                            method="post">
                        <s:hidden name="order" value="${orderSummary.order}"/>

                        <div style="margin-bottom: 15px;">
                            <div class="label newLabel" style="width: 100px !important;">Contact Name</div>
                            <s:text class="signUpInputNew2" name="codContactName"
                                    value="${orderSummary.order.address.name}"/>
                        </div>
                        <div>
                            <div class="label newLabel" style="width: 100px !important;">Contact Phone</div>
                            <s:text class="signUpInputNew2" name="codContactPhone"
                                    value="${orderSummary.order.address.phone}" id="phoneNo"/>
                        </div>

                        <div class="buttons" style="font-size: 1.3em;"><br/>
                            <br/>
                            <s:submit  style="left: 90px !important;margin-top: 0px !important;" name="pre" value="PLACE ORDER"
                                      class="positive phoneValidation placeOrderButtonNew"/></div>
                        <br/>
                        <br/>

                        <div class="clear"></div>
                        <hr/>
                        <br/>

                        <h4>Terms and Conditions for Cash on Delivery</h4>

                        <p>Please note that COD orders will not be confirmed and shipped from our end untill we manually confirm the order on the phone number provided by you.<br/>
                            Also please ensure that the above person is available at the given location at all times.
                        </p>
                    </s:form>
                </c:otherwise>
            </c:choose>

        </div>
        <div id="tabs_content5" class="tab_content" style="display: none;">
            <h2 class="offer">Payment Details</h2>

            <div class="left" style="width: 415px;">
                <s:form
                    beanclass="com.hk.web.action.core.payment.ChequeCashPaymentReceiveAction"
                    method="post" id="paymentForm">
                <s:hidden name="order" value="${orderSummary.order}"/>
                <div style="margin-bottom: 10px;">
                    <div class="label newLabel2">Bank Name <span class="aster">*</span></div>
                    <s:text class="signUpInputNew2" name="bankName"/>
                </div>
                <div style="margin-bottom: 10px;">
                    <div class="label newLabel2">Bank Branch <span class="aster">*</span></div>
                    <td><s:text class="signUpInputNew2" name="bankBranch"/></td>
                </div>
                <div>
                    <div class="label newLabel2">City <span class="aster">*</span></div>
                    <td><s:text class="signUpInputNew2" name="bankCity"/></td>
                </div>
                <div class="label">Payment Mode <span class="aster">*</span></div>
                <label><s:radio name='paymentMode'
                                value='<%=EnumPaymentMode.NEFT.getId()%>' checked="checked"/>&nbsp;Online
                    Transfer(NEFT)</label>
                <br/>
	            <shiro:hasRole name="<%=RoleConstants.B2B_USER%>">
                <label><s:radio name='paymentMode'
                                value='<%=EnumPaymentMode.CashDeposit.getId()%>'/>&nbsp;Cash
                    Deposit</label>
                <br/>
                <label><s:radio name='paymentMode'
                                value='<%=EnumPaymentMode.ChequeDeposit.getId()%>'/>&nbsp;Cheque</label>
                <br/>
	            </shiro:hasRole>

                <div id="chequeno" style="display: none;">
                    <div class="label" id="#cheque">Cheque No.</div>
                    <s:text name="chequeNumber" maxlength="6"/></div>
                <script type="text/javascript">
                    $('label').click(function() {
                        selected = $('input[name="paymentMode"]:checked').val();
                        if (selected == 25) {
                            $('#chequeno').fadeIn(200);
                        }
                        else {
                            $('#chequeno').fadeOut(200);
                        }
                    });
                </script>
                <div class="label">Total Payable Amount</div>
                <div class="num"
                     style="font-size: 1.8em; font-weight: bold; color: #F87500;">
                    <fmt:formatNumber
                            value="${orderSummary.pricingDto.grandTotalPayable}"
                            currencySymbol="Rs. " type="currency"/></div>
                <div style="width: 50%;">
                    <s:submit name="pre" value="PLACE ORDER" class="button makePayment signUpButtonNew" style="font-size: 1.5em;width: 125px;left: 0px !important;margin: 0px !important;margin-top: 20px !important;"/></div>
            </s:form></div>
            <div class="right"
                 style="width: 30%; padding: 10px; line-height: 21px;">
                <h5>HealthKart Bank Account Details</h5>
                <br/>
                <strong>Name of A/C</strong> <br/>
                Aquamarine HealthCare Pvt. Ltd. <br/>
                <br/>
                <strong>Bank</strong> <br/>
                ICICI Bank, Dwarka Sector-5 Branch, New Delhi <br/>
                <br/>
                <strong>Type</strong> <br/>
                Current Account <br/>
                <br/>
                <strong>Account Number</strong> <br/>
                025005003746 <br/>
                <br/>
                <strong>RTGS/NEFT/IFSC Code</strong> <br/>
                ICIC0000250 <br/>
                <br/>
                <br/>
            </div>
        </div>
    </c:if>
</shiro:lacksRole>
<shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_ADMINS%>">
<div id="tabs_content6" class="tab_content" style="display: none;">
    <h2 class="offer">Payment Details</h2>

    <div class="left" style="padding-left: 20px;"><s:form
            beanclass="com.hk.web.action.core.payment.CounterCashPaymentReceiveAction"
            method="post" id="paymentForm">
        <s:hidden name="order" value="${orderSummary.order}"/>
        <s:hidden name="paymentMode"
                  value="<%=EnumPaymentMode.COUNTER_CASH.getId()%>"/>
        <div class="label">Total Payable Amount</div>
        <div class="num"
             style="font-size: 1.8em; font-weight: bold; color: #F87500;">
            <fmt:formatNumber
                    value="${orderSummary.pricingDto.grandTotalPayable}"
                    currencySymbol="Rs. " type="currency"/></div>
        <div style="width: 50%; float: right;"><s:submit name="pre"
                                                         value="PLACE ORDER" class="button makePayment placeOrderButtonNew"
                                                         style="font-size: 1.5em;"/></div>
    </s:form></div>

</div>
</div>
</shiro:hasAnyRoles>
<c:set var="url" value="${pageContext.request.contextPath}/core/user/BillingAddress.action" />
<script type="text/javascript">
    $(document).ready(function() {
         
        $('.tab_content').hide();
        $('.tab_content').first().show();
        $('.tabs ul li').click(function() {
            $('.tabs ul li').removeClass('selected');
            $(this).addClass('selected');
            /*if(this.id == "tab4" && ${false} && ${orderSummary.pricingDto.grandTotalPayable < 1000.0}){
             $('.offer-banner').css("visibility", "visible");
             $.getJSON(
             $('#setInCookieLink').attr('href'), {wantedCOD: "true"},
             function(res) {
             if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
             } else {
             }
             }
             );
             }else{
             $('.offer-banner').css("visibility", "hidden");
             }*/
            var selected = $(this).attr('id').replace('tab', 'tabs_content');
            $.session("selected-tab", $(this).attr('id'));
            $('.tab_content').hide();
            $('#' + selected).fadeIn(200);

        });
          $("#tab1").click(function(){
              $("#CODOption").hide();
              $("#nonCODOption").show();
              $("input:radio:checked").attr('checked', false);
          });
        $('#tab1').trigger('click');
        $("#tab5").click(function(){
            $("#CODOption").hide();
            $("#nonCODOption").show();
              $("input:radio:checked").attr('checked', false);
          });
        $("#tab3").click(function(){
            $("#CODOption").hide();
            $("#nonCODOption").show();
              $("input:radio:checked").attr('checked', false);
          });
        $("#tab4").click(function(){
            $("#CODOption").show();
            $("#nonCODOption").hide();
        });
        $("#tab2").click(function(){
            $("#CODOption").hide();
            $("#nonCODOption").show();
        });
        $("#tab6").click(function(){
            $("#CODOption").hide();
            $("#nonCODOption").show();
        });
        if ($.session("selected-tab")) {
            var sTab = $.session("selected-tab");
            $('.tabs ul li').removeClass('selected');
            $('#' + sTab).addClass('selected');
            /*if(sTab == "tab4" && ${false} && ${orderSummary.pricingDto.grandTotalPayable < 1000.0}){
             $('.offer-banner').css("visibility", "visible");
             $.getJSON(
             $('#setInCookieLink').attr('href'), {wantedCOD: "true"},
             function(res) {
             if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
             } else {
             }
             }
             );
             }else{
             $('.offer-banner').css("visibility", "hidden");
             }*/
            var selected = $('#' + sTab).attr('id').replace('tab', 'tabs_content');
            $('.tab_content').hide();
            $('#' + selected).fadeIn(200);
        }

        $('.makePayment').click(function disablePaymentButton() {
            $(this).css("display", "none");
            if($('#Paypal').is(':checked')){
              location.href = '${url}';
              return false;
            }
        });

        $('.phoneValidation').click(function() {
            var phone = $('#phoneNo').val();
            var phoneRegEx = /^((\+91)?[0-9]{10,13}?)$/;
            if (!phoneRegEx.test(phone)) {
                alert("Please enter a valid phone number (+91xxxxxxxxxx).");
                return false;
            }
            else {
                $(this).css("display", "none");
            }
        });
    });
</script>
<div class='floftfix'></div>

</div>
</c:otherwise>
</c:choose></div>
</div>
<c:if test="${orderSummary.trimCartLineItems!=null && fn:length(orderSummary.trimCartLineItems) >0}">
    <c:set var="comboInstanceIds"  value=""/>
    <c:set var="comboInstanceIdsName" value="" />
    <script type="text/javascript">
          $(document).ready(function () {
              ShowDialog(true);
//              e.preventDefault();
              $('.button_green').live('click',function(){
                  $(this).hide();
                  HideDialog();
              });

          function ShowDialog(modal)
          {
              $("#overlay2").show();
              $("#dialog2").fadeIn(300);

              if (modal)
              {
                  $("#overlay2").unbind("click");
              }
          }

          function HideDialog()
          {

              $("#overlay2").hide();
              $("#dialog2").fadeOut(300);
          }
          });
    </script>
     </c:if>

 <c:if test="${not isSecure }">
	  <iframe src="" id="vizuryTargeting" scrolling="no" width="1"
	          height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>


	  <script type="text/javascript">
		  var vizuryLink = "http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e400";
		  var user_hash;
		  <c:forEach items="${paymentModeBean.order.productCartLineItems}" var="cartLineItem" varStatus="liCtr">
		  vizuryLink += "&pid${liCtr.count}=${cartLineItem.productVariant.product.id}&catid${liCtr.count}=${cartLineItem.productVariant.product.primaryCategory.name}&quantity${liCtr.count}=${cartLineItem.qty}";
		  user_hash = "${user_hash}";
		  </c:forEach>

		  vizuryLink += "&currency=INR&section=1&level=3";
		  document.getElementById("vizuryTargeting").src = vizuryLink + "&uid="+user_hash;
	  </script>
  </c:if>
</s:layout-component>


<s:layout-component name="analytics">
  <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="" allCategories="" brand="" isProd="<%=false%>"/>
</s:layout-component>

</s:layout-render>

<div id="overlay2" class="web_dialog_overlay"></div>
   <div id="dialog2" class="web_dialog">

       <table style="width:100%; border: 0px;" cellpadding="3" cellspacing="0">
           <tr>
               <td colspan="2" class="web_dialog_title" style="color:#444;">Oops! We are sorry.</td>
               <td class="web_dialog_title align_right">
                   <%--<a href="#" id="btnClose" class="classClose">Close</a>                   --%>
               </td>
           </tr>
           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>
           <tr>
               <td colspan="3" style="padding-left: 15px;">
                   <b>The following items have been removed due to insufficient inventory</b>
               </td>
           </tr>
           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>
               <c:forEach items="${orderSummary.trimCartLineItems}" var="cartLineItem" varStatus="ctr1">
                   <tr>
                       <div class='product' style="border-bottom-style: solid;">
                         <td style="padding-left: 15px;">
                           <div class='img48' style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
                               <c:choose>
                                   <c:when test="${cartLineItem.comboInstance!=null}">
                                       <c:if test="${!fn:contains(comboInstanceIds, cartLineItem.comboInstance.id)}">
                                           <c:set var="comboInstanceIds"  value="${cartLineItem.comboInstance.id}+','+${comboInstanceIds}"/>
                                           <c:choose>
                                               <c:when test="${cartLineItem.comboInstance.combo.mainImageId != null}">
                                                   <hk:productImage imageId="${cartLineItem.comboInstance.combo.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
                                               </c:when>
                                               <c:otherwise>
                                                   <img class="prod48"
                                                        src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.comboInstance.combo.id}.jpg"
                                                        alt="${cartLineItem.comboInstance.combo.name}"/>
                                               </c:otherwise>
                                           </c:choose>
                                       </c:if>
                                   </c:when>
                                   <c:otherwise>
                                       <c:choose>
                                           <c:when test="${cartLineItem.productVariant.product.mainImageId != null}">
                                               <hk:productImage imageId="${cartLineItem.productVariant.product.mainImageId}" size="<%=EnumImageSize.TinySize%>"/>
                                           </c:when>
                                           <c:otherwise>
                                               <img class="prod48"
                                                    src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${cartLineItem.productVariant.product.id}.jpg"
                                                    alt="${cartLineItem.productVariant.product.name}"/>
                                           </c:otherwise>
                                       </c:choose>
                                   </c:otherwise>
                               </c:choose>
                           </div>
                         </td>
                         <td>
                           <div class='name'>
                               <table width="100%">
                                   <tr>
                                       <td>
                                             <c:choose>
                                                  <c:when test="${cartLineItem.comboInstance!=null}">
                                                    <c:if test="${!fn:contains(comboInstanceIdsName, cartLineItem.comboInstance.id)}">
                                                        <c:set var="comboInstanceIdsName"  value="${cartLineItem.comboInstance.id}+','+${comboInstanceIdsName}"/>
                                                        ${cartLineItem.comboInstance.combo.name} <br/>
                                                     </c:if>
                                                  </c:when>
                                                  <c:otherwise>
                                                       ${cartLineItem.productVariant.product.name}
                                                  </c:otherwise>
                                               </c:choose>
                                       </td>
                                   </tr>
                               </table>
                           </div>
                         </td>

                       </div>
                   </tr>
                 </c:forEach>


           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>
           <tr>

           </tr>
           <tr>
               <td colspan="2" style="text-align: center;">

                 <c:if test="${fn:length(orderSummary.order.cartLineItems) > 0}">
                   <a class="button_green" style="width:120px; height: 18px;">Continue</a>
                     </td><td>
                   </c:if>
                   <s:link beanclass="com.hk.web.action.core.cart.CartAction" class=" button_green"
                           style="width: 160px; height: 18px;">Back to Shopping
                   </s:link>
               </td>
           </tr>
       </table>
   </div>


<style type="text/css">

     .web_dialog_overlay
   {
      position: fixed;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      height: 100%;
      width: 100%;
      margin: 0;
      padding: 0;
      background: #000000;
      opacity: .15;
      filter: alpha(opacity=15);
      -moz-opacity: .15;
      z-index: 101;
      display: none;
   }
   .web_dialog
   {
      display: none;
      position: fixed;
      width: 450px;
      /*height: 400px;*/
      top: 50%;
      left: 50%;
      margin-left: -265px;
      margin-top: -180px;
      /*background-color: #ffffff;*/
      background-color: white;
      /*border: 2px solid #336699;*/
      padding: 0px;
      z-index: 102;
      font-family: Verdana;
      font-size: 10pt;
      color: #333;
      box-shadow: 0 0 15px rgba(0, 0, 0, 0.9), 0 0 5px rgba(0, 0, 0, 0.5), 0 0 10px rgba(0, 0, 0, 0.7), 0 0 25px rgba(0, 0, 0, 0.3);
   }
   .web_dialog_title
   {
      /*border-bottom: solid 2px #336699;*/
      /*background-color: #336699;*/
     font-size: 16px;
     font-weight: bold;
     padding: 5px;
      background-color: #f2f7fb;
      color: White;
      font-weight:bold;
   }
   .web_dialog_title a
   {
      color: White;
      text-decoration: none;
   }
   .align_right
   {
      text-align: right;
   }

   </style>

<script type="text/javascript">
  /*
  $(document).ready(function(){
     $('.contactMobile').blur(function validateMobile(){
       var mobile = this.value;
       if()
     });
  });*/
  
</script>
