<%@ page import="com.hk.pact.dao.BaseDao" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.courier.BoxSize" %>
<%@ page import="com.hk.constants.shipment.EnumPicker" %>
<%@ page import="com.hk.constants.shipment.EnumPacker" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.courier.SearchOrderAndEnterCourierInfoAction"  var="shipmentQueueBean"/>
<%
  BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
  MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
  pageContext.setAttribute("boxSizeList", baseDao.getAll(BoxSize.class));
//  pageContext.setAttribute("courierList", masterDataDao.getCourierList());
//   pageContext.setAttribute("groundShippedCourierList", masterDataDao.getGroundShippedCourierList());
%>

<c:set var="commentTypeDelivery" value="<%= MasterDataDao.USER_COMMENT_TYPE_DELIVERY_BASE_ORDER %>" />

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
      <script type="text/javascript">
          $(document).ready(function() {

	          var commentType = $('#commentType').val();
	          if(commentType == ${commentTypeDelivery}) {
		          alert("User Instruction : " + $('#userComments').val());
	          }

              $('.weight').keyup(function() {
                  var weight = $('.weight').val();
                  if (weight > 5) {
                      confirm("Box Weight entered is " + weight + " Kgs. Do you wanna continue with the same?");
                  }
              });

              $("#shipmentbutton").click(function(event) {
                  var tracking = $('.tracking').val();
                  if (tracking == "" || tracking == null) {
                       $('.error').html("");
                      $('.error').append("Enter Tracking Id");
                      $('.error').show();
                      return false;
                  }
                  if (tracking.length > 20) {
                      $('.error').html("");
                      $('.error').append(" Tracking Id length can not be greater than 20");
                      $('.error').show();
                      return false;
                  }

              });


          });
      </script>

  </s:layout-component>
  <s:layout-component name="heading">Enter Tracking Details for Packed Orders</s:layout-component>
  <s:layout-component name="content">
	<input type="hidden" id="commentType" value="${shipmentQueueBean.shippingOrder.baseOrder.commentType}">
	<input type="hidden" id="userComments" value="${shipmentQueueBean.shippingOrder.baseOrder.userComments}">
    <div  class="error" style= "background-color:salmon; width:380px; display:none;">       

    </div>
    <c:choose>
      <c:when test="${shipmentQueueBean.shippingOrder == null}">
        <div height="500px" align="center">
          <s:form beanclass="com.hk.web.action.admin.courier.SearchOrderAndEnterCourierInfoAction" method="get"
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
          <s:form  beanclass="com.hk.web.action.admin.courier.SearchOrderAndEnterCourierInfoAction">
              <s:hidden name="shipment" value="${shipmentQueueBean.shipment.id}"/>
                <s:hidden name="suggestedCourier" value="${shipmentQueueBean.suggestedCourier}"/>
             <c:if test="${! empty shipmentQueueBean.availableCouriers}">
              <div style="margin-top:5px;margin-bottom:5px;font-size:.9em"><A></A>Available Couriers:
              <c:forEach items="${shipmentQueueBean.availableCouriers}" var="courier">
                ${courier.name},
              </c:forEach>
              </div>
            </c:if>

            <s:hidden name="shippingOrder" value="${shipmentQueueBean.shippingOrder}"/>
	         <label>Picker:</label><s:select name="shipment.picker">
		        <c:forEach items="<%=EnumPicker.getAll()%>" var="pType">
			        <s:option value="${pType.name}">${pType.name}</s:option>
		        </c:forEach>
	        </s:select>
	          <label>Packer:</label><s:select name="shipment.packer">
		        <c:forEach items="<%=EnumPacker.getAll()%>" var="pType">
			        <s:option value="${pType.name}">${pType.name}</s:option>
		        </c:forEach>
	        </s:select>
            <label>Box Size:</label>
            <s:select name="shipment.boxSize">
              <c:forEach var="box" items="${boxSizeList}">
                <s:option value="${box.id}">${box.name}</s:option>
              </c:forEach>
            </s:select>
            <label>Box Weight(Kgs):</label><s:text name="shipment.boxWeight" size="5" class="weight"/>
            <label>Tracking ID:</label><s:text class="tracking" name="trackingId"/>
            <label>Courier</label>
           <%--<c:if test="${!shipmentQueueBean.groundShipped}">--%>
            <s:select name="shipment.courier" id="courier" value="${shipmentQueueBean.suggestedCourier.id}">
              <c:forEach var="courier" items="${shipmentQueueBean.availableCouriers}">
                <s:option value="${courier.id}">${courier.name}</s:option>
              </c:forEach>
            </s:select>
            <%--</c:if>--%>
            <%--<c:if test="${shipmentQueueBean.groundShipped}">--%>
            <%--<s:select name="shipment.courier" id="courier" value="${shipmentQueueBean.suggestedCourier.id}">--%>
              <%--<c:forEach var="courier" items="${shipmentQueueBean.List}">--%>
                <%--<s:option value="${courier.id}">${courier.name}</s:option>--%>
              <%--</c:forEach>--%>
            <%--</s:select>--%>
            <%--</c:if>--%>
            <c:if test="${shipmentQueueBean.suggestedCourier != null}">
              <label style="margin-top:5px;margin-bottom:5px;color:green;">Suggested Courier:  <b>${shipmentQueueBean.suggestedCourier.name}</b></label>
            </c:if>
              <label>Approx Weight (By System)</label> ${shipmentQueueBean.approxWeight}

              <div class="buttons" style="margin-left: 90%;"><s:submit id="shipmentbutton" name="saveShipmentDetails" value="Save"/></div>

               <div style="margin:5px;color:red;font-size:18px;">
                    <c:if test="${shipmentQueueBean.shippingOrder.baseOrder.commentType == commentTypeDelivery}">
              	        User Instructions: ${shipmentQueueBean.shippingOrder.baseOrder.userComments}
                    </c:if>
              </div>
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
