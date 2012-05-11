<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="mhc.common.constants.EnumCourier" %>
<%@ page import="mhc.service.dao.BoxSizeDao" %>
<%@ page import="app.bootstrap.guice.InjectorFactory" %>
<%@ page import="mhc.web.json.HealthkartResponse" %>
<%@ page import="mhc.common.constants.order.EnumOrderStatus" %>
<%@ page import="mhc.common.constants.EnumCartLineItemType" %>
<%@ page import="mhc.common.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="mhc.web.action.admin.SearchOrderAndEnterCourierInfoAction" var="shipmentQueueBean"/>
<%
  BoxSizeDao boxSizeDao = InjectorFactory.getInjector().getInstance(BoxSizeDao.class);
  MasterDataDao masterDataDao = InjectorFactory.getInjector().getInstance(MasterDataDao.class);
  pageContext.setAttribute("boxSizeList", boxSizeDao.listAll());
  pageContext.setAttribute("courierList", masterDataDao.getCourierList());
%>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	  <script type="text/javascript">
		  $(document).ready(function() {
			  $('.weight').keyup(function() {
				  var weight = $('.weight').val();
				  if (weight > 5) {
					  confirm("Box Weight entered is " + weight + " Kgs. Do you wanna continue with the same?");
				  }
			  });
		  });
	  </script>

  </s:layout-component>
  <s:layout-component name="heading">Enter Tracking Details for Packed Orders</s:layout-component>
  <s:layout-component name="content">

    <c:choose>
      <c:when test="${shipmentQueueBean.shippingOrder == null}">
        <div height="500px" align="center">
          <s:form beanclass="mhc.web.action.admin.SearchOrderAndEnterCourierInfoAction" method="get"
                  autocomplete="false">
            <label>Search Order:</label>
            <br/><br/>
            <s:text name="gatewayOrderId" id="gatewayOrderId" size="50"
                    style="font-size:16px; padding:5px;height:30px;width:300px;"/>
            <br/>
            <br/>

            <s:submit name="searchOrders" value="Search"/>

          </s:form>
        </div>
        <script language=javascript type=text/javascript>
          $('#gatewayOrderId').focus();
        </script>
      </c:when>
      <c:otherwise>
        <fieldset class="top_label">
          <s:form beanclass="mhc.web.action.admin.SearchOrderAndEnterCourierInfoAction">
              <s:hidden name="shipment" value="${shipmentQueueBean.shipment.id}"/>
             <c:if test="${! empty shipmentQueueBean.availableCouriers}">
              <div style="margin-top:5px;margin-bottom:5px;font-size:.9em">Available Couriers:
              <c:forEach items="${shipmentQueueBean.availableCouriers}" var="courier">
                ${courier.name},
              </c:forEach>
              </div>
            </c:if>

            <s:hidden name="shippingOrder" value="${shipmentQueueBean.shippingOrder}"/>
            <label>Box Size:</label>
            <s:select name="shipment.boxSize">
              <c:forEach var="box" items="${boxSizeList}">
                <s:option value="${box.id}">${box.name}</s:option>
              </c:forEach>
            </s:select>
            <label>Box Weight(Kgs):</label><s:text name="shipment.boxWeight" size="5" class="weight"/>
            <label>Tracking ID:</label><s:text name="shipment.trackingId"/>
            <label>Courier</label>
            <s:select name="shipment.courier" id="courier" value="${shipmentQueueBean.suggestedCourier.id}">
              <c:forEach var="courier" items="${courierList}">
                <s:option value="${courier.id}">${courier.name}</s:option>
              </c:forEach>
            </s:select>
            <c:if test="${shipmentQueueBean.suggestedCourier != null}">
              <label style="margin-top:5px;margin-bottom:5px;color:green;">Suggested Courier:  <b>${shipmentQueueBean.suggestedCourier.name}</b></label>
            </c:if>

            <div class="buttons" style="margin-left: 90%;"><s:submit name="saveShipmentDetails" value="Save"/></div>
          </s:form>
        </fieldset>
      </c:otherwise>
    </c:choose>

      <c:if test="${shipmentQueueBean.shippingOrder != null}">
        <c:choose>
          <c:when test="${! hk:allItemsCheckedOut(shipmentQueueBean.shippingOrder)}">
            <div align="center" class="prom yellow help" style="height:30px; font-size:20px; color:red; font-weight:bold;">
              ATTENTION: All Units of Line Items (Order) are not checked out. Please insure they are checked out.
            </div>
          </c:when>
          <c:otherwise>
            <div align="center" class="prom yellow help"
                 style="height:30px; font-size:20px; color:green; font-weight:bold;">
              All Units of Line Items (Order) are checked out. Order is ready to be packed.
            </div>
          </c:otherwise>
        </c:choose>

      </c:if>
     <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                     shippingOrders="${shipmentQueueBean.shippingOrderList}"/>
</s:layout-component>
</s:layout-render>
