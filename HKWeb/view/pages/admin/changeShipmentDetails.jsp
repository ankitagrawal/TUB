

<%--<%@ page import="com.akube.framework.util.FormatUtils" %>--%>
<%--<%@ page import="com.hk.pact.dao.MasterDataDao" %>--%>
<%--<%@include file="/includes/_taglibInclude.jsp" %>--%>
<%--<%----%>
  <%--Created by IntelliJ IDEA.--%>
  <%--User: meenal--%>
  <%--Date: Apr 27, 2012--%>
  <%--Time: 2:36:59 PM--%>
  <%--To change this template use File | Settings | File Templates.--%>
<%----%>--%>
<%--<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit shipment details">--%>
  <%--<s:useActionBean beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction" var="csda"/>--%>
  <%--<s:layout-component name="heading">--%>
    <%--Edit Shipment Details--%>
  <%--</s:layout-component>--%>
  <%--<s:layout-component name="htmlHead">--%>

    <%--<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>--%>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>--%>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>--%>
    <%--<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>--%>

    <%--<script type="text/javascript">--%>
          <%--$(document).ready(function(){--%>
            <%--var comment;--%>
            <%--var comment1;--%>
            <%--$('#orderStatus').change(function(){--%>
               <%--comment=$(this).attr('id');--%>
               <%--comment1=$('#orderStatus option:selected').text();--%>
                         <%--});--%>
            <%--$('.button').click(function(){--%>
            <%--$('.comment').val($('.comment').val().concat(comment, ":", comment1, ", "));--%>
          <%--});--%>
          <%--});--%>
        <%--</script>--%>

      <%--<script type="text/javascript">--%>
          <%--$(document).ready(function() {--%>

              <%--$('.button').click(function(event) {--%>
                  <%--var tracking = $("#trackingId").val();--%>
                  <%--if (tracking == "" || tracking == null) {--%>
                       <%--$('.error').html("");--%>
                      <%--$('.error').append("Enter Tracking Id");--%>
                      <%--$('.error').show();--%>
                      <%--return false;--%>
                  <%--}--%>
                  <%--if (tracking.length > 20) {--%>
                      <%--$('.error').html("");--%>
                      <%--$('.error').append(" Tracking Id length can not be greater than 20");--%>
                      <%--$('.error').show();--%>
                      <%--return false;--%>
                  <%--}--%>

              <%--});--%>

          <%--});--%>

      <%--</script>--%>

    <%--<style type="text/css">--%>
      <%--.text {--%>
        <%--margin-left: 100px;--%>
        <%--margin-top: 20px;--%>
      <%--}--%>

      <%--.label {--%>
        <%--margin-top: 20px;--%>
        <%--float: left;--%>
        <%--margin-left: 10px;--%>
        <%--width: 150px;--%>
      <%--}--%>

    <%--</style>--%>

  <%--</s:layout-component>--%>
  <%--<s:layout-component name="content">--%>
      <%--<div  class="error" style= "background-color:salmon; width:380px; display:none;">--%>

    <%--</div>--%>
    <%--<div>--%>
      <%--<div style="float: left; width:40%">--%>
        <%--<s:form beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction">--%>
          <%--<fieldset class="top_label">--%>
            <%--<legend> Search Shipping Order</legend>--%>
            <%--<s:label name="gatewayOrderId" class="label">Gateway Order Id</s:label>--%>
            <%--<s:text name="gatewayOrderId" style="width:170px" class="text"/> <br/>--%>

            <%--<div class="clear"></div>--%>
            <%--<div style="margin-top:15px;"></div>--%>
            <%--<s:submit name="search" value="Search Order"/>--%>

          <%--</fieldset>--%>
        <%--</s:form>--%>
      <%--</div>--%>

      <%--<div class="clear"></div>--%>
      <%--<div style="margin-top:40px;"></div>--%>
      <%--<div style="float: left; width:40%">--%>
        <%--<c:if test="${csda.visible == true}">--%>
            <%--<s:form beanclass="com.hk.web.action.admin.shipment.ChangeShipmentDetailsAction" id="newFormForAWB">--%>
                <%--<s:hidden name="attachedCourier" value="${csda.shipment.courier}"/>--%>
            <%--<fieldset>--%>
              <%--<legend> Edit Courier Or Tracking Id</legend>--%>

              <%--<s:label name="gatewayOrderId" class="label">Gateway Order Id</s:label>--%>
              <%--<s:text name="gatewayOrderId" class="text fields" style="width:170px" readonly="readonly"/>--%>

              <%--<div class="clear"></div>--%>

              <%--<s:label name="courier" class="label">Courier</s:label>--%>
              <%--<s:select name="shipment.courier" value="${csda.shipment.courier}" class="text" id="courier">--%>
                <%--<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" label="name"--%>
                                           <%--value="id"/>--%>
              <%--</s:select>--%>

              <%--<div class="clear"></div>--%>
                <%--//todo ps need to rethink--%>
              <%--<!--TO DO we will change it later on change --  -->--%>
              <%--<s:label name="trackingId" class="label">Tracking Id</s:label>--%>
              <%--<s:text name="trackingId" id="trackingId" value="${csda.shipment.awb.awbNumber}" style="width:170px"--%>
                      <%--class="text fields"/>--%>

              <%--<div class="clear"></div>--%>

              <%--<s:label name="Delivery Date" class="label"/>--%>
              <%--<s:text name="shipment.deliveryDate" class="date_input startDate text fields" style="width:160px"--%>
                      <%--id="deliveryDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>--%>

              <%--<div class="clear"></div>--%>

              <%--<s:label name="returnDate" class="label">Return Date</s:label>--%>
              <%--<s:text name="shipment.returnDate" class="date_input startDate text fields" style="width:160px"--%>
                      <%--id="returnDate" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>--%>

              <%--<div class="clear"></div>--%>

              <%--<s:label name="orderStatus" class="label">Shipping Order Status</s:label>--%>
              <%--<s:select name="shippingOrder.orderStatus" value="${csda.shippingOrder.orderStatus}"  class="text"--%>
                        <%--style="width:170px" id="orderStatus">--%>
                <%--<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="SOStatusForShipmentDetailsList"--%>
                                           <%--label="name"--%>
                                           <%--value="id"/>--%>
              <%--</s:select>--%>

              <%--<div class="clear"></div>--%>
              <%--<div style="margin-top:15px;"></div>--%>
              <%--<s:text name="comments" class="comment"/>--%>
              <%--<s:hidden name="shippingOrder" value="shippingOrder"/>--%>
              <%--<s:hidden name="shipment" value="shipment"/>--%>
              <%--<s:hidden name="originalShippingOrderStatus" value="tempShippingOrderStatus"/>--%>
              <%--<s:submit name="save" value="Save Changes"  id ="s"class="button"/>--%>
            <%--</fieldset>--%>
          <%--</s:form>--%>
        <%--</c:if>--%>
      <%--</div>--%>
    <%--</div>--%>
  <%--</s:layout-component>--%>
<%--</s:layout-render>--%>

