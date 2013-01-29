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
                 var courier = $('#status').val();
                 if(awbNumber == null || awbNumber == "" || (awbNumber.length >16 && awbNumber.length <8)){
                     alert("Tracking Number can't be left empty or Length must be between 8 to 16");
                     return false;
                 }
                 else{
                 $('#awbBarCode').attr('value',awbNumber);
                 }
                  if(courier == null || courier == ""){
                      alert("Please select a courier");
                      return false;
                  }
             });
          });
          </script>
    </s:layout-component>
     <s:layout-component name="content">
    <fieldset>
    You are Creating AWB for :- <br/><br/> 
    Shipping Order Id # <span style="color:blue;"> ${cusa.shippingOrder.id} </span>
    <br><br>
    COD # <span style="color:blue;">${cusa.shippingOrder.COD}</span>
    <br><br>
    Warehouse # <span style="color:blue;">${cusa.shippingOrder.warehouse.name}</span>
    </fieldset>
      <s:form beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction">
          <fieldset>
           <label>Enter Courier Id</label>
             <s:select name="awb.courier"  id="status">
                 <s:option value="">--Select--</s:option>
                  <c:forEach items="${cusa.applicableCouriers}" var="courier">
                      <s:option value="${courier.id}">${courier.name}</s:option>
                  </c:forEach>
                </s:select>
          <label>Enter Tracking Number</label>
          <s:text name="awb.awbNumber" id = "awbNumber" style="width:180px;height:25px;"/>
           <s:hidden name="awb.awbBarCode" id= "awbBarCode" value=""/>
           <s:hidden name="awb.cod" value="${cusa.shippingOrder.COD}"/>
           <s:hidden name="awb.warehouse" value="${cusa.shippingOrder.warehouse}"/>
           <s:hidden name="awb.awbStatus" value="<%=EnumAwbStatus.Unused.getId()%>"/>
           <s:hidden name="shippingOrder" value="${cusa.shippingOrder.id}"/>
          <s:submit name="createAssignAwb" value="SAVE" id="save"/>
          </fieldset>
          </s:form>
    </s:layout-component>
    </s:layout-render>