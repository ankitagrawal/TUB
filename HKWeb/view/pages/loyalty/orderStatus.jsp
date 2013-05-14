<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.hk.constants.catalog.image.EnumImageSize"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyFiles/css/bootstrap.css" rel="stylesheet">
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction" var="pla" />
 <c:set var="imageLargeSize" value="<%=EnumImageSize.LargeSize%>"/>
 <c:set var="imageMediumSize" value="<%=EnumImageSize.MediumSize%>"/>
 <c:set var="imageSmallSize" value="<%=EnumImageSize.TinySize%>"/>
 <c:set var="imageSmallSizeCorousal" value="<%=EnumImageSize.SmallSize%>"/>
<%
boolean isSecure = pageContext.getRequest().isSecure();
pageContext.setAttribute("isSecure", isSecure);
%>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
	<div class="mainContainer embedMarginTop50">
      <div class="container_16 clearfix">
        <div class="mainContent">
			<c:if test="${empty pla.errorMessage}">
			
		
          <div class="congratsText">
            Congratulations. Your order has been placed.
            <div class="floatRight"><a title="Redeem more points" href="<hk:vhostJs/>/loyaltypg" class="blue">
             Shop more from stellar store. 
            </a></div>
          </div>

          <div class="orderIdText">
            Your order ID is ${pla.order.gatewayOrderId}.
          </div>

          <div class="confirmationEmailText" >
                <p>You will shortly be getting a confirmation email. The Dispatch date for each product is mentioned below.
                The delivery time would be above that and the delivery date will vary according to your location.</p>
          </div>

          <div class="queryText">
                <p>For any query please call us: 0124-4616444 or you can drop us an email at info@healthkart.com with your Order ID.</p>
          </div>

	  <div class="step2 success_order_summary" >
          <h2 class="paymentH2">Order Summary</h2>
          <div class="topFullContainer">
            <div class="orderSummaryHeading">
                  <div class="productGrid">Product</div>
                  <div class="prodQuantityGrid">Qty</div>
                  <div class="prodPriceGrid">Points</div>
            </div>
          	
              <div class="products_container">
                  <c:forEach items="${pla.loyaltyProductList}" var="lp">
            		<div class="product newProductContainer" >
                    <div class="img48">
						<c:set var="imageId" value = "${lp.variant.product.mainImageId }" />
						<img src="${hk:getS3ImageUrl(imageSmallSize, imageId,isSecure)}" alt="${lp.variant.product.name}"/>
					</div>
                    <div class="prodname" >
                      ${lp.variant.product.name}
                    </div>
                    <div class="quantityValue">${lp.qty}</div>
                    <div class="price" >
                      <div class="hk">
                      </div>
                      <div class="num" > 
                        <span class="lineItemSubTotalMrp">${hk:roundNumberForDisplay(lp.points*lp.qty)}</span>
                      </div>
                    </div>
                    <div class="floatix"></div>
                  </div>
                 </c:forEach>
              </div>
                <div class="totals newTotals">
                  <div class="left">
                    <div class="shipping">
                      Shipping:
                    </div>
                      <div class="shipping">COD Charges</div>
                      <div class="total" >
                      Total Points:
                    </div>
                  </div>
                  <div class="right">
                    <div class="shipping num">
                      Rs 0.0
                    </div>
                      Rs 0.0
                      <div class="total num">
                      <strong >
                    	${hk:roundNumberForDisplay(pla.totalShoppingPoints)}    
                      </strong>
                    </div>
                  </div>
                </div>
              </div>
                <div class="orderSummaryHeading">
                    <div class="deliveryDetails"> DELIVERY DETAILS</div>
                    <ul>
                        <li>
                            - The time taken for delivery after dispatch from our warehouse varies with location.
                        </li>
                        <li>
                            - For Metroes: 1-3 business days
                        </li>
                        <li>
                            - For Major Cities: 2-4 business days
                        </li>
                        <li>
                            - For Other Town/Cities: 3-6 business days
                        </li>
                        <li>
                            - For Rest of India Non Serviceable through Couriers: 7-15 business days (Delivery done by Indian Post)
                        </li>
                    </ul>
                </div>
                <div class="orderShippedTo">
                    <h2 class="shipmentAddr">ORDER SHIPPED TO</h2>
                    <p>
                        <strong>${pla.shipmentAddress.name}</strong> <br>
                            ${pla.shipmentAddress.line1},
                            ${pla.shipmentAddress.line2},
                            ${pla.shipmentAddress.city} - ${pla.shipmentAddress.pincode}<br>
                            ${pla.shipmentAddress.state}, <span class="upc">${pla.shipmentAddress.country.name}</span><br>
                        <span class="sml lgry upc">Phone </span> ${pla.shipmentAddress.phone}<br>
                    </p>
                </div>
                </div>	
          
			</c:if>
		<c:if test="${not empty pla.errorMessage}">
			<div class="row">
				<div class="span12">
					<div class="alert alert-error">
						<strong>Couldn't process the order!&nbsp;&nbsp;&nbsp;</strong>
						<span>${pla.errorMessage}</span>
					</div>
				</div>
			</div>
		</c:if>

        </div>

      </div>
    </div>
		
	</stripes:layout-component>
</stripes:layout-render>

