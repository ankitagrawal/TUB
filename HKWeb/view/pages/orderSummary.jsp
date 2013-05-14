<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.dto.pricing.PricingDto" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.web.action.core.cart.AddToCartAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%
  Double codMaxAmount = Double.parseDouble((String)ServiceLocatorFactory.getProperty(Keys.Env.codMaxAmount));
  Double codMinAmount = Double.parseDouble((String)ServiceLocatorFactory.getProperty(Keys.Env.codMinAmount));
  Double codCharges = Double.parseDouble((String)ServiceLocatorFactory.getProperty(Keys.Env.codCharges));
   PricingDto pricingDto = (PricingDto) pageContext.getAttribute("pricingDto");

%>
<c:set var="codMaxAmount" value="<%=codMaxAmount%>"/>
<c:set var="codMinAmount" value="<%=codMinAmount%>"/>
<c:set var="codCharges" value="<%=codCharges%>"/>
<c:set var="orderDate" value="<%=new DateTime().toDate()%>"/>


<s:layout-render name="/layouts/checkoutLayout.jsp" pageTitle="Order Summary">
  <s:layout-component name="htmlHead">
	  <script type="text/javascript">
		  $(document).ready(function() {

			 $('.requiredFieldValidator').click(function() {
				 if( $.trim($('#userComments').val()) != '' && ! $('.commentType').is(':checked') ) {
					 alert('Please select the type of Comment');
					 return false;
				 }
				 if( $.trim($('#userComments').val()) == '' && $('.commentType').is(':checked') ) {
					 $('.commentType').attr("checked", false);
				 }

			 });

		  });

	  </script>


  </s:layout-component>

    <s:layout-component name="modal">
    <div class="jqmWindow" style="display:none;" id="notifyMeWindow"></div>
</s:layout-component>

  <s:layout-component name="steps">
    <div class='steps'>
      <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" style="margin-top: 0; margin-bottom: 0;">
        <div class='step prev_step'>
          <h2>Step 1</h2>

          <div class='small'>
            Select shipping address
          </div>
        </div>
      </s:link>

      <div class='step current_step'>
        <h2>Step 2</h2>

        <div class='small'>
          Confirm your order
        </div>
      </div>
      <div class='step'>
        <h2>Step 3</h2>

        <div class='small'>
          Choose Payment Method
        </div>
      </div>
    </div>
  </s:layout-component>

  <s:layout-component name="steps_content">
    <s:useActionBean beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre" var="orderSummary"/>
    <div class='current_step_content step2'>
<%----%>
    <jsp:include page="/includes/checkoutNotice.jsp"/>
      
    <%--<c:if test="${orderSummary.availableCourierList == null}">--%>
      <%--<div align="center" style="color:red; font-size:1.2em;">This pincode is serviced only through Speed Post. Delivery may take 5-7 days</div>--%>
    <%--</c:if>--%>
      
    <h3>
      You selected
    </h3>
    <s:layout-render name="/layouts/embed/orderSummaryTableDetailed.jsp" pricingDto="${orderSummary.pricingDto}" orderDate="${orderDate}"/>
    <script type="text/javascript">

      $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>${orderSummary.pricingDto.productLineCount}</span> item in<br/>your shopping cart");

    </script>
    <div class='right_container address_box'>
      <div class='title'>
        <h5>
          To be shipped to
        </h5>
      </div>
      <div class='detail'>
        <div class='name'>
            ${orderSummary.order.address.name}
        </div>
        <div class='address'>
            ${orderSummary.order.address.line1}
          <c:if test="${hk:isNotBlank(orderSummary.order.address.line2)}">
            <br/>
            ${orderSummary.order.address.line2}
          </c:if>
          <br/>
            ${orderSummary.order.address.city}
          <br/>
            ${orderSummary.order.address.state}
          <br/>
            ${orderSummary.order.address.pincode.pincode}
          <br/>
            ${orderSummary.order.address.phone}
        </div>
      </div>
        <span class="small">
          <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" style="color: #888; float: right;">
            (change) </s:link>
        </span>
    </div>
    <c:if test="${orderSummary.redeemableRewardPoints > 0}">
      <div class="right_container" style="left: 40px;">
        <div class="title">
          <h5>
            REDEEM REWARD POINTS
          </h5>

          <p>
            <c:choose>
              <c:when test="${orderSummary.useRewardPoints}">
                You are using your reward points for payment.<br/>
                You have
                <strong><fmt:formatNumber value="${orderSummary.redeemableRewardPoints}" type="currency" currencySymbol=" "/></strong> reward points.<br/>
                Reward points left after this order =
                <strong><fmt:formatNumber value="${orderSummary.redeemableRewardPoints - orderSummary.pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=" "/></strong><br/>
                <s:link beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre">
                  <s:param name="useRewardPoints" value="false"/>
                  <span>Don't Redeem Points</span>
                </s:link>
              </c:when>
              <c:otherwise>
                You have
                <strong><fmt:formatNumber value="${orderSummary.redeemableRewardPoints}" type="currency" currencySymbol=" "/></strong> reward points.<br/>
                You can use these points to pay for your order.<br/>
                <s:link beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre">
                  <s:param name="useRewardPoints" value="true"/>
                  <span>Redeem Points</span>
                </s:link>
              </c:otherwise>
            </c:choose>
          </p>
        </div>
      </div>
    </c:if>
    <div class="right_container" style="float: right; margin-right:50px;">
      <div class="title">
        <h5>
          Instructions if any (e.g Preferred Delivery Time/Flavour Needed)
        </h5>
        <s:form beanclass="com.hk.web.action.core.order.OrderSummaryAction" method="post">
        <s:hidden name="order" value="${orderSummary.order.id}"/>
        <s:textarea name="order.userComments" id="userComments" rows="2" cols="20" style="width:175px;height:110px"/>
          <%--<div class="title">
            <h5>
              Confirm order
            </h5>
          </div>--%>
	      <div class="comment_type">
		      <br><s:radio value="1" name="order.commentType" class="commentType"/> Packing Type
		      <br><s:radio value="2" name="order.commentType" class="commentType"/> Delivery Type
		      <br><s:radio value="3" name="order.commentType" class="commentType"/> Others
	      </div>
          <div style="margin:10px; padding-top:10px">
            <div class="buttons">
              <s:submit name="orderReviewed" value="Make Payment" class="requiredFieldValidator"/>
            </div>
          </div>

      </div>
      </s:form>

    </div>
    <c:choose>
    <c:when test="${orderSummary.groundShippedItemPresent && !(orderSummary.groundShippingAllowed)}">
      <script type="text/javascript">
          $(document).ready(function () {
              ShowDialog(true);
              e.preventDefault();
              $('.classClose').click(function (e)
          {
             
              HideDialog();
              e.preventDefault();
          });
              $('.button_green').click(function(){
                  $(this).hide();
              });
          });

          function ShowDialog(modal)
          {
              $("#overlay").show();
              $("#dialog").fadeIn(300);

              if (modal)
              {
                  $("#overlay").unbind("click");
              }
          }

          function HideDialog()
          {

              $("#overlay").hide();
              $("#dialog").fadeOut(300);
          }
      </script>
     </c:when>
      <c:otherwise>
      <c:if test="${orderSummary.trimCartLineItems!=null and fn:length(orderSummary.trimCartLineItems) > 0}">
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
     </c:otherwise>
      </c:choose>
  </s:layout-component>
</s:layout-render>

<div id="overlay" class="web_dialog_overlay"></div>
   <div id="dialog" class="web_dialog">

  <s:form beanclass="com.hk.web.action.core.cart.CartAction" rel="noFollow">
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
                   <b>The following items cannot be delivered at your pincode. </b>
               </td>
           </tr>
           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>            

               <c:forEach items="${orderSummary.pricingDto.productLineItems}" var="invoiceLineItem"
                              varStatus="ctr1">
                 <c:if test="${invoiceLineItem.productVariant.product.groundShipping}"  >
                   <tr>
                       <div class='product' style="border-bottom-style: solid;">
                         <td style="padding-left: 15px;">
                           <div class='img48' style="width: 48px; height: 48px; display: inline-block; text-align: center; vertical-align: top;">
                               <c:choose>
                                   <c:when test="${invoiceLineItem.productVariant.product.mainImageId != null}">
                                       <hk:productImage imageId="${invoiceLineItem.productVariant.product.mainImageId}"
                                                        size="<%=EnumImageSize.TinySize%>"/>
                                   </c:when>
                                   <c:otherwise>
                                       <img class="prod48"
                                            src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${invoiceLineItem.productVariant.product.id}.jpg"
                                            alt="${invoiceLineItem.productVariant.product.name}"/>
                                   </c:otherwise>
                               </c:choose>
                           </div>
                         </td>
                         <td>
                           <div class='name'>
                               <table width="70%">
                                   <tr>
                                       <td>
                                               ${invoiceLineItem.productVariant.product.name} <br/>

                                               ${invoiceLineItem.productVariant.variantName}
                                           <%--<c:set var="${invoiceLineItem.qty}" value="0"/>--%>
                                       </td>
                                   </tr>
                               </table>
                           </div>
                         </td>                      

                       </div>
                   </tr>
                 </c:if>
                 </c:forEach>


           <tr>
               <td>&nbsp;</td>
               <td>&nbsp;</td>
           </tr>
           <tr>

           </tr>
           <tr>
               <td colspan="2" style="text-align: center;">
                   <s:link beanclass="com.hk.web.action.core.cart.CartAction" event="removeGroundShippedItem" class=" button_green"
                                                           style="width: 220px; height: 16px; align_right ">Remove and Proceed
                       <s:param name="order" value="${orderSummary.order}"/>                         
                   </s:link>

               </td>
           </tr>
       </table>
   </s:form>
   </div>


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

                 <c:if test="${orderSummary.sizeOfCLI > 0}">
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

