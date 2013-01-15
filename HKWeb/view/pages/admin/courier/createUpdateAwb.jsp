\<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 9, 2013
  Time: 1:34:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.courier.EnumAwbStatus" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction" var="cusa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create/Update AWB">
    <s:layout-component name="htmlHead">
      <script type="text/javascript">
          $(document).ready(function(){
             $('#save').click(function(){
                var awbNumber = $('#awbNumber').val();
                 if(awbNumber == null || awbNumber == ""){
                     alert("Tracking Number can't be left empty");
                     return false;
                 }
                 else{
                 $('#awbBarCode').attr('value',awbNumber);
                 }
             });
          });
          </script>
    </s:layout-component>
     <s:layout-component name="content">
    You are Creating AWB for :- <br/><br/> 
    Shipping Order Id # <h2 style="color:blue;"> ${cusa.shippingOrder.id} </h2>
    COD # <h2 style="color:blue;">${cusa.shippingOrder.COD}</h2>
    Warehouse # <h2 style="color:blue;">${cusa.shippingOrder.warehouse.name}</h2>
      <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateShipmentAction">
           <label>Enter Courier Id</label>
             <s:select name="awb.courier"  id="status">
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList"
                                               value="id" label="name"/>
                </s:select>
          <label>Enter Tracking Number</label>
          <s:text name="awb.awbNumber" id = "awbNumber"/>
           <s:hidden name="awb.awbBarCode" id= "awbBarCode" value=""/>
           <s:hidden name="awb.cod" value="${cusa.shippingOrder.COD}"/>
           <s:hidden name="awb.warehouse" value="${cusa.shippingOrder.warehouse}"/>
           <s:hidden name="awb.awbStatus" value="<%=EnumAwbStatus.Unused.getId()%>"/>
           <s:hidden name="shippingOrder" value="${cusa.shippingOrder.id}"/>
          <s:submit name="createAssignAwb" value="SAVE" id="save"/>
          </s:form>
    </s:layout-component>
    </s:layout-render>