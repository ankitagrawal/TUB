<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.rtv.EnumRtvNoteStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.courier.EnumPickupStatus" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Rtv Note">
 <s:useActionBean beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" var="rtvNote"/>
<c:set var="createdStatus" value="<%=EnumRtvNoteStatus.Created.getName()%>"/>
    <s:layout-component name="htmlHead">
 <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
 <style>
 
 #closeButtonDiv{
	float: left;
	position: relative;
	left: 40%;
}
 </style>
 
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.lightbox-0.5.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
               $('#save').click(function(){
                  var rtvNoteStatus = parseFloat($('#rtvNoteStatus').val());
                   var rtvNoteStatusDB = ${rtvNote.rtvNote.rtvNoteStatus.id};
                   if(rtvNoteStatus < rtvNoteStatusDB){
                       alert("Rtv Note Status can't reverted back");
                       return false;
                   }
                   var courier = $('#allActiveCourier').val();
                   var pickupStatus = $('#pickupStatus').val();
                   var destinationAddress = $('#destinationAddress').val();
                   if(rtvNoteStatus >= 20){
                       if((courier == "") || (pickupStatus == "")){
                           alert("Please Select a Courier and pickup status");
                           return false;
                       }
                       if(destinationAddress == null || destinationAddress == ""){
                           alert("Please Enter Destination Address");
                           return false;
                       }
                   }
                   else{
                       if((courier!=null && courier!="")|| (pickupStatus!=null && pickupStatus!="") || (destinationAddress!=null && destinationAddress!="")){
                           if(courier == ""  || courier == null || pickupStatus == null || pickupStatus == "" || destinationAddress==null || destinationAddress == ""){
                               alert("Please Enter Courier, PickUp status and destination address!!");
                               return false;
                           }
                       }
                   }
               });
            });
        </script>
        </s:layout-component>

    <s:layout-component name="content">
 <h2>Purchase Order # ${rtvNote.purchaseOrderId}</h2>
 <h4 style="color:blue;">Extra Inventory # ${rtvNote.extraInventory.id}</h4>
<c:set var="reconciled" value="<%=EnumRtvNoteStatus.Reconciled.getId()%>"/>
        <s:form beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction">
    <br><br>
   <h2>Rtv Note</h2>
        <table>
            <thead>
            <tr>
                <th>Rtv Note Id</th>
                <th>Rtv Note Status</th>
                <th>Created By</th>
                <th>Is debit to Supplier</th>
                <th>Reconciled</th>
                <th>Create Date</th>
                <th>Update Date</th>
                <th>Remarks</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${rtvNote.rtvNote!=null}">
                <tr>
                    <td> ${rtvNote.rtvNote.id}</td>
                    <td>
                        <c:choose>
                            <c:when test="${rtvNote.rtvNote.rtvNoteStatus.id eq reconciled}">
                               <h6 style="color:blue">${rtvNote.rtvNote.rtvNoteStatus.name}(Closed)</h6>
                                <s:hidden name="rtvStatusId" value="<%=EnumRtvNoteStatus.Reconciled.getId()%>" />
                            </c:when>
                            <c:otherwise>
                               <select name="rtvStatusId" id="rtvNoteStatus">
                                <option value="<%=EnumRtvNoteStatus.Created.getId()%>" ${rtvNote.rtvNote.rtvNoteStatus.id eq 10 ? 'selected':''}>Created</option>
                                <option value="<%=EnumRtvNoteStatus.SentToSupplier.getId()%>" ${rtvNote.rtvNote.rtvNoteStatus.id eq 20 ? 'selected':''}>Sent To Supplier</option>
                                <option value="<%=EnumRtvNoteStatus.Reconciled.getId()%>" ${rtvNote.rtvNote.rtvNoteStatus.id eq 40 ? 'selected':''}>Reconciled</option>
                               </select>
                            </c:otherwise>
                        </c:choose>
                        </td>
                    <td>${rtvNote.rtvNote.createdBy.name}</td>
                    <td>
                        <select name="debitToSupplier">
                           <option value="0" ${!rtvNote.rtvNote.debitToSupplier ? 'selected':''}>No</option>
                            <option value="1" ${rtvNote.rtvNote.debitToSupplier ? 'selected':''}>Yes</option>
                        </select>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${rtvNote.rtvNote.reconciled}">
                               <h6 style="color:blue">${rtvNote.rtvNote.reconciled ? 'Yes' : 'No'}(Closed)</h6>
                            </c:when>
                            <c:otherwise>
                                <select name="reconciled">
                                    <option value="0" ${!rtvNote.rtvNote.reconciled ? 'selected':''}>No</option>
                                    <option value="1" ${rtvNote.rtvNote.reconciled ? 'selected':''}>Yes</option>
                                </select>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${rtvNote.rtvNote.createDate}</td>
                    <td>${rtvNote.rtvNote.updateDate}</td>
                    <td><textarea name = "comments" rows="10" cols="10" style="height:60px; width:210px;">${rtvNote.rtvNote.remarks}</textarea></td>
                </tr>
            </c:if>
            </tbody>
        </table>
        <br><br>
        <div class="clear"></div>
        <h2>Courier Details</h2>
        <table border="1">
            <thead>
              <tr>
                  <th>Courier</th>
                  <th>Pickup Confirmation No.</th>
                  <th>Tracking No.</th>
                  <th>PickUp Status</th>
                  <th>Pickup Date</th>
                  <th>Destination Address</th>
                  <th>Shipping Charge On Vendor</th>
                  <th>Shipping Charge On HK</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                  <td>
                      <input type="hidden" name="courierPickupDetail.id" value="${rtvNote.courierPickupDetail.id}" >
                     <s:select name="courierPickupDetail.courier" id="allActiveCourier">
                         <s:option value="">--Select--</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="allActiveCourier" value="id" label="name"/>
                      </s:select>
                      <script type="text/javascript">
                          $('#allActiveCourier').val(${rtvNote.courierPickupDetail.courier.id});
                      </script>
                  </td>
                  <td>
                       <s:text name="courierPickupDetail.pickupConfirmationNo" value="${rtvNote.courierPickupDetail.pickupConfirmationNo}"/>
                  </td>
                  <td>
                      <s:text name="courierPickupDetail.trackingNo" value="${rtvNote.courierPickupDetail.trackingNo}"/>
                  </td>
                  <td>
                      <s:select name="pickupStatusId" id="pickupStatus">
                          <s:option value="">--Select--</s:option>
                          <s:option value="<%=EnumPickupStatus.OPEN.getId()%>"><%=EnumPickupStatus.OPEN.getName()%></s:option>
                          <s:option value="<%=EnumPickupStatus.CLOSE.getId()%>"><%=EnumPickupStatus.CLOSE.getName()%></s:option>
                      </s:select>
                      <script type="text/javascript">
                          $('#pickupStatus').val(${rtvNote.courierPickupDetail.pickupStatus.id});
                      </script>
                  </td>
                  <td><s:text class="date_input" id="pickupDate" formatPattern="yyyy-MM-dd" name="courierPickupDetail.pickupDate" value="${rtvNote.courierPickupDetail.pickupDate}"/></td>
                  <td>
                      <s:text name="destinationAddress" value="${rtvNote.rtvNote.destinationAddress}" id="destinationAddress" />
                  </td>
                  <td>
                      <s:text name="shippingChargeVendor" value="${rtvNote.shippingChargeVendor}" id="shippingCharge" />
                  </td>
                  <td>
                      <s:text name="shippingChargeHk" value="${rtvNote.shippingChargeHk}" id="shippingCharge" />
                  </td>
              </tr>
            </tbody>
        </table>
        <s:hidden name="rtvNoteId" value="${rtvNote.rtvNote.id}"/>
        <br><br>
        <div class="clear"></div>
    <h2>Rtv Note Line Items</h2>
        <table border="1">
            <thead>
            <tr>
                <th>S.No</th>
                <th>Rtv Note Line Item ID</th>
                <th>Variant ID</th>
                <th>Product Name</th>
                <th>MRP</th>
                <th>Cost Price</th>
                <th>Received QTY</th>
                <th>TAX</th>
                <th>Remarks</th>
            </tr>
            </thead>
            <tbody id="poTable">
            <c:if test="${rtvNote.rtvNoteLineItems!=null}">
                <c:forEach items="${rtvNote.rtvNoteLineItems}" var="rtvLineItem" varStatus="ctr">
                    <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                        <td>${ctr.index+1}.</td>
                        <td>
                          ${rtvLineItem.id}
                        </td>
                        <td>
                           ${rtvLineItem.extraInventoryLineItem.sku.productVariant.id}
                        </td>
                        <td>
                           ${rtvLineItem.extraInventoryLineItem.productName}
                        </td>
                        <td>
                           ${rtvLineItem.extraInventoryLineItem.mrp}
                        </td>
                        <td>
                            ${rtvLineItem.extraInventoryLineItem.costPrice}
                        </td>
                        <td>
                            ${rtvLineItem.extraInventoryLineItem.receivedQty}
                        </td>
                        <td>
                            ${rtvLineItem.extraInventoryLineItem.tax.name}
                        </td>
                        <td>
                            ${rtvLineItem.extraInventoryLineItem.remarks}
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
        <br/>
        <s:submit name="editRtvNote" value="SAVE" id="save" />
    </s:form>
    <div id="closeButtonDiv">
	<s:link beanclass="com.hk.web.action.admin.rtv.ExtraInventoryAction" event="rtvNotePrintPreview" Value="Print Rtv" class="button_green addToCartButton" target="_blank"> Print RTV
	<s:param name="rtvNoteId" value="${rtvNote.rtvNote.id}"/>
	</s:link>
	</div>
    </s:layout-component>
    </s:layout-render>