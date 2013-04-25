<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyFiles/css/bootstrap.css" rel="stylesheet">
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.PlaceOrderAction" var="pla" />

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
	<div class="mainContainer embedMarginTop50">
      <div class="container_16 clearfix">
        <div class="mainContent">
			<c:if test="${empty pla.errorMessage}">
			
		
          <div class="congratsText">
            Congratulations. Your order has been placed.
          </div>

          <div class="orderIdText">
            Your order ID is ${pla.order.gatewayOrderId}.
          </div>

          <div class="confirmationEmailText" style="border-bottom: 1px solid #ddd;">
                <p>You will shortly be getting a confirmation email. The Dispatch date for each product is mentioned below.
                The delivery time would be above that and the delivery date will vary according to your location.</p>
          </div>

          <div class="queryText">
                <p>For any query please call us: 0124-4502950 or you can drop us an email at info@healthkart.com with your Order ID.</p>
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

