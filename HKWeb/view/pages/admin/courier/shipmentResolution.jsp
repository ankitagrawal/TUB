<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 14, 2013
  Time: 10:53:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.shipment.EnumShipmentServiceType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction" var="shipRes"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Shipment Resolution">
  <s:layout-component name="htmlHead">
      <script type="text/javascript">
        $(document).ready(function(){
          $('#search').click(function(){
                 var gateWayOrderId = $('#gatewayOrderId').val();
                  if(gateWayOrderId == "" || gateWayOrderId == null){
                      alert("Gateway Order can't be Empty");
                      return false;
                  }
              });
            $('#changeCourier').click(function(){
               var reasoning = $('#reasoning').val();
               var updateCourier = $('#updateCourier').val();
                if(reasoning == null || reasoning == ""){
                    alert("You must Select a reason to change Courier");
                    return false;
                }
                if(updateCourier == null || updateCourier == ""){
                    alert("Please select a courier");
                    return false;
                }
            });
            $('#cSSType').click(function(){
               var shipmentServiceType = $('#shipmentServiceType').val();
                if(shipmentServiceType == null || shipmentServiceType == ""){
                    alert("Please select shipment service type");
                    return false;
                }
            });
        });
      </script>
  </s:layout-component>

    <s:layout-component name="content">
     <s:form beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction">
          <fieldset>
          <label>Enter SO Gateway Id</label>
          <s:text name="gatewayOrderId" id="gatewayOrderId"/>
          <br>
          <s:submit name="search" value="Search" id="search"/>
          </fieldset>
          <div class="clear"></div>
         <fieldset>
         <c:if test="${shipRes.shippingOrderLifeCycles!=null and fn:length(shipRes.shippingOrderLifeCycles)>0}">
             <h2>Shipping Order Life Cycle for shipping Order # ${shipRes.shippingOrder.id}</h2>
              <table class="zebra_vert">
                  <tr>
                      <thead>
                       <th>S.No.</th>
                       <th>ID</th>
                       <th>ShippingOrderLifeCycleActivity</th>
                       <th>Activity By User</th>
                       <th>Activity Date</th>
                       <th>Comment</th>
                      </thead>
                  </tr>
                  <c:forEach items="${shipRes.shippingOrderLifeCycles}" var="shippingOrderLifeCycle" varStatus="ctr">
                  <tr>
                    <td>${ctr.index+1}.</td>
                    <td>${shippingOrderLifeCycle.id}</td>
                    <td>${shippingOrderLifeCycle.shippingOrderLifeCycleActivity.name}</td>
                    <td>${shippingOrderLifeCycle.user.name}</td>
                    <td>${shippingOrderLifeCycle.activityDate}</td>
                    <td>${shippingOrderLifeCycle.comments}</td>
                  </tr>
                  </c:forEach>
              </table>
         </c:if>
         </fieldset>
         <div class="clear"></div>

         <c:choose>
           <c:when test="${shipRes.shipment!=null}">
               <fieldset>
               <h2>Shipment Details</h2>
               <table class="zebra_vert">
                   <tr>
                       <thead>
                         <th>Shipment ID</th>
                         <th>Shipping Order ID</th>
                         <th>Pincode</th>
                         <th>SO Amount</th>
                         <th>AWB</th>
                         <th>Courier Name</th>
                         <th>Shipment Service Type</th>
                         <th>Create Date</th>
                       </thead>
                   </tr>
                   <tr>
                       <td>
                           ${shipRes.shipment.id}
                           <s:hidden name="shipment" value="${shipRes.shipment.id}"/>
                       </td>
                       <td>
                           <s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" event="searchShippingOrder">${shipRes.shippingOrder.id}
                                <s:param name="shippingOrderId" value="${shipRes.shippingOrder.id}"/>
                                <s:param name="shippingOrderGatewayId" value="${shipRes.shippingOrder.gatewayOrderId}"/>
                            </s:link>
                           <s:hidden name="shippingOrder" value="${shipRes.shippingOrder.id}"/>
                       </td>
                       <td>
                           ${shipRes.shippingOrder.baseOrder.address.pincode.pincode}
                       </td>
                       <td>
                           ${shipRes.shippingOrder.amount}
                       </td>
                       <td>
                           ${shipRes.shipment.awb.awbNumber}
                       </td>
                       <td>
                           ${shipRes.shipment.awb.courier.name}
                       </td>
                       <td>
                           ${shipRes.shipment.shipmentServiceType.name}
                       </td>
                       <td>
                           ${shipRes.shipment.createDate}
                       </td>
                   </tr>
               </table>
               </fieldset>
               <div class="clear"></div>
               <fieldset>
              <h2>Change Courier</h2>
               <br>
              Courier Name<s:select name="updateCourier" id="updateCourier">
                  <s:option value="">--Select--</s:option>
                  <c:forEach items="${shipRes.applicableCouriers}" var="courier">
                    <s:option value="${courier.id}">${courier.name}</s:option>
                  </c:forEach>
              </s:select>
               <br><br>
               Select Reason for Changing the courier
               <s:select name="reasoning" id="reasoning">
                   <s:option value="">-------Select-------</s:option>
                   <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allCourierChangeReason" value="name" label="name"/>
                </s:select>
               <br><br>
              Check Box if you want to preserve old Awb or leave Unchecked if you want to Discard it<s:checkbox name="preserveAwb"/>
               <br>
              <s:submit name="changeCourier" value="Save" id="changeCourier"/>
               </fieldset>
               <div class="clear"></div>
               <fieldset>
               <h2>Change Shipment Service Type</h2>
                   <br>
                 Shipment Service Type<s:select name="shipmentServiceTypeId" id="shipmentServiceType">
                 <s:option value="">--Select--</s:option>
                  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allEnumShipmentServiceTypes"
                                               value="id" label="name"/>
                 </s:select>
               <br><br>
                 <s:submit name="changeShipmentServiceType" value="Save" id="cSSType"/>
               </fieldset>
           </c:when>
             <c:otherwise>
                 <fieldset>
                 <c:if test="${shipRes.shippingOrder!=null}">
                 shipment is not created for Shipping Order # <span style="color:blue;">${shipRes.shippingOrder.id}</span>
                 Please Click on the below Button to create AWB
                 <div class="clear"></div>
                 <br><br>
                 <s:link beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction" event="generateAWB" class="button_orange">create AWB
                 <s:param name="shippingOrder" value="${shipRes.shippingOrder.id}"/>
                 </s:link>
                 <br><br>    
                 </c:if>
                 </fieldset>
             </c:otherwise>
         </c:choose>
     </s:form>
    </s:layout-component>
 </s:layout-render>