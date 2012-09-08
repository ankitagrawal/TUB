<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit shipment details">

	<s:useActionBean beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction" var="csda"/>
	<c:set var="delivered" value="<%=EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus()%>"/>
	<c:set var="lost" value="<%=EnumShippingOrderStatus.SO_Lost.asShippingOrderStatus()%>"/>
	<c:set var="shipped" value="<%=EnumShippingOrderStatus.SO_Shipped.asShippingOrderStatus()%>"/>
	<s:layout-component name="heading">
		Change Shipping Order Status
	</s:layout-component>


	<s:layout-component name="htmlHead">

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
		<script type="text/javascript">

	 $(document).ready(function() {
        var startDate = "";
        var endDate = "";
        var courierName="";
        $('.verifyData').click(function() {
          startDate = $('.startDateCourier').val();
          endDate = $('.endDateCourier').val();
          return _checkDateValidity();
        });
          function _checkDateValidity() {
              if (startDate == "yyyy-mm-dd hh:mm:ss" || endDate == "yyyy-mm-dd hh:mm:ss")
              {
                  alert("Please enter both dates.")
                  return false;
              } else if (startDate > endDate) {
                  alert("End Date cannot be less than Start Date.");
                  return false;
              } else {
                  return true;
              }
          }
      });


		</script>

		<style type="text/css">
			.text {
				margin-left: 100px;
				margin-top: 20px;
			}

			.label {
				margin-top: 20px;
				margin-left: 10px;
				width: 150px;
			}



			/*.button {*/
				/*-moz-border-radius: 5px 5px 5px 5px;*/
				/*-webkit-border-radius: 5px 5px 5px 5px;*/
				/*box-shadow: 5px 5px 5px rgba(68, 68, 68, 0.6);*/
				/*background-color: #ff9933;*/
				/*color: white;*/
				/*font-size: inherit;*/
				/*display: inline;*/
				/*font-size: 1.3em;*/
				/*font-weight: bold;*/
				/*margin-left: 5px;*/
				/*min-width: 4em;*/
				/*padding: 7px 22px 5px 21px;*/
				/*font-family: serif;;*/
			/*}*/

			.button2 {
				margin-left: 200px; 				
			}

		</style>

	</s:layout-component>
	<s:layout-component name="content">
		<div class="error" style="background-color:salmon; width:380px; display:none;">

		</div>

		<div>
			<div style="float: left; width:40%">
				<s:form beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction">
					<fieldset class="top_label">
						<legend> Search Shipping Order</legend>
						<s:label name="gatewayOrderId" class="label">Gateway Order Id</s:label>
						<s:text name="gatewayOrderId" style="width:170px" class="text"/> <br/>
						 <div class="divider"> </div>
						<div class="clear"></div>
						<div style="margin-top:15px;"></div>
						<s:submit name="search" value="Search Order"/>

					</fieldset>
				</s:form>
			</div>
			<div class="clear"></div>
			<div style="margin-top:40px;"></div>
			<div style="float: left; width:40%">
				<c:if test="${csda.shippingOrder.orderStatus.id  >= shipped.id}">
				 <c:if test="${csda.visible}">
					<div>
						<fieldset>
							<ul>
								<li>
									<s:label name="gatewayOrderId" class="label">Gateway Order Id : </s:label>
										${csda.shippingOrder.gatewayOrderId}
								</li>
								<li>
									<s:label name="courier" class="label">Courier</s:label>
										${csda.shipment.courier.name}
								</li>
								<li>
									<s:label name="trackingId" class="label">Tracking Id</s:label>
										${csda.shipment.awb.awbNumber}
								</li>

								<li>
									<s:label name="Delivery Date" class="label"/>
									<s:format formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
									          value="${csda.shippingOrder.shipment.deliveryDate}"/>
								</li>

								<li>
									<s:label name="returnDate" class="label">Shipped Date</s:label>
									<s:format formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
									          value="${csda.shippingOrder.shipment.shipDate}"/>
								</li>

								<li>
									<s:label name="orderStatus" class="label">Shipping Order Status</s:label>
										${csda.shippingOrder.orderStatus.name}
								</li>

							</ul>
						</fieldset>
					</div>
					 </c:if>
				</c:if>
					<div class="clear"></div>
				<c:if test="${csda.shippingOrder.orderStatus.id == shipped.id }">

					<s:form beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction" id="newFormForAWB">
						<s:param name="originalShippingOrderStatus" value="${csda.shippingOrder.orderStatus}"/>
						<s:param name="shippingOrder" value="${csda.shippingOrder}"/>
						<s:param name="shipment" value="${csda.shipment}"/>
					   <div>
						  <div >
						<s:label name="Delivery Date" class="label"/>
						<s:text  name="shipment.deliveryDate" class="date_input startDate text fields"
						        style="width:160px"
						        id="deliveryDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
						 </div>
						<%--<div class="clear"></div>						--%>
						<%--<s:label name="returnDate" class="label">Return Date</s:label>--%>
						<%--<s:text name="shipment.returnDate" class="date_input startDate text fields" style="width:160px" readonly="true"--%>
						        <%--id="returnDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>--%>
						 <%--</div>--%>
						
						<div class="clear" style="height:50px;"></div>


						<div>
							<s:submit name="markDelivered" value="Delivered" />
							 <s:submit name="markLost" value="Lost" style="margin-left:300px;" />

						</div>
						</s:form>
					</c:if>
					<div class="clear"></div>

			</div>
		</div>

	</s:layout-component>
</s:layout-render>















