<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 8, 2013
  Time: 5:57:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction" var="cusa"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create/Update Shipment">
  <s:layout-component name="htmlHead">
      <script type="text/javascript">
          $(document).ready(function(){
              var estimatedWeight = ${cusa.estimatedWeight};
              $('#search').click(function(){
                 var gateWayOrderId = $('#gatewayOrderId').val();
                  if(gateWayOrderId == "" || gateWayOrderId == null){
                      alert("Gateway Order can't be Empty");
                      return false;
                  }
              });
              $('#validate').click(function(){
                 var boxSize = $('#boxSize').val();
                  var boxWeight = $('#boxWeight').val();
                   var weightDiff = 0.2;
                   var minWeight = (1-weightDiff)*estimatedWeight;
                   var maxWeight = (1+weightDiff)*estimatedWeight;
                   if(boxSize == "" || boxSize == null || boxWeight == "" || boxWeight == null){
                       alert("Box Size or Box Weight can't be Left Empty");
                       return false;
                   }
                  else if(parseFloat(boxWeight)< minWeight || parseFloat(boxWeight) > maxWeight){
                     alert("Box Weight is Out of Range");
                       return false;
                   }
                  else if(isNaN(boxSize)){
                       alert("Box Size must contain only numbers");
                       return false;
                   }
              });
          });
      </script>
      </s:layout-component>
     <s:layout-component name="content">
      <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction">
         <label>Enter Gateway Id</label>
          <s:text name="gatewayOrderId" id="gatewayOrderId"/>
          <s:submit name="searchShipment" value="Search" id="search"/>
          <c:if test="${cusa.shipment!=null}">
               <table class="zebra_vert">
                 <tr>
                     <th>AWB</th>
                     <th>Shiiping Order</th>
                     <th>Shipment Service Type</th>
                     <th>Box Size</th>
                     <th>Box Weight</th>
                     <th>Packer Name</th>
                     <th>Picker Name</th>
                 </tr>
                   <tr>
                       <td>
                           ${cusa.shipment.awb.id}
                           <s:hidden name="shipment.awb" value="${cusa.shipment.awb.id}"/>
                       </td>
                       <td>
                           ${cusa.shipment.shippingOrder.id}
                           <s:hidden name="shipment.shippingOrder" value="${cusa.shipment.shippingOrder.id}"/>
                       </td>
                       <td>
                           ${cusa.shipment.shipmentServiceType.name}
                           <s:hidden name="shipment.shipmentServiceType" value="${cusa.shipment.shipmentServiceType}"/>
                       </td>
                       <td>
                           <label>Enter Size in cm(Centimeter)</label>
                          <s:text name="shipment.boxSize" id = "boxSize" value="${cusa.shipment.boxSize}"/>
                       </td>
                       <td>
                           <label>Enter Weight in KG</label>
                           <s:text name="shipment.boxWeight" id = "boxWeight" value="${cusa.shipment.boxWeight}"/>
                       </td>
                       <td>
                           <label>Enter Packer Name</label>
                           <s:text name="shipment.packer" value="${cusa.shipment.packer}"/>
                       </td>
                       <td>
                           <label>Enter Picker Name</label>
                           <s:text name="shipment.picker"  value="${cusa.shipment.picker}"/>
                       </td>
                   </tr>
                </table>
                  <s:submit name="updateShipment" value="SAVE" id="validate"/>
          </c:if>
      </s:form>
     </s:layout-component>
    </s:layout-render>