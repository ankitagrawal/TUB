<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="hk" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ page import="com.akube.framework.util.FormatUtils"%>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/_taglibInclude.jsp"%>
<s:useActionBean beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderUtilityAction" event="searchSO" var="shippingOrderUtil" />


<c:set var="shippingOrderStatusShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="shippingOrderStatusCancelled" value="<%=EnumShippingOrderStatus.SO_Cancelled.getId()%>"/>
<c:set var="shippingOrderStatusDelivered" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="shippingOrderStatusRTO" value="<%=EnumShippingOrderStatus.SO_RTO.getId()%>"/>
<c:set var="shippingOrderStatusRTOInitiated" value="<%=EnumShippingOrderStatus.RTO_Initiated.getId()%>"/>
<c:set var="shippingOrderStatusLost" value="<%=EnumShippingOrderStatus.SO_Lost.getId()%>"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	</s:layout-component>
	<s:layout-component name="content">
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp" />
		
		
		<div align="center"><label><font size="6px">Validate Shipping Orders</font></label></div><br><br><br>

		<s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderUtilityAction">
		<div align = "center">
			<table border="1">
				<tr>
					<td><strong>SO Gateway IDs :</strong><br /> <span style="font-size: .9em;">(comma separated values)</span>
					</td>
					<td><s:textarea name="gatewayOrderIds" rows="5" cols="30" style="height:50px;" /></td>
				</tr>
				<tr>
					<td colspan="2" align="center" style="text-align: center;"><strong>OR</strong></td>
				</tr>
				<tr>
					<td colspan="2"><label><strong>Start Date :</strong> </label>
					<s:text class="date_input startDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
							name="startDate" /> <label><strong>End Date :</strong></label>
					<s:text class="date_input endDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
							name="endDate" /></td>
				</tr>
				<tr>
				<td>
				<label><strong>SO Status: </strong></label><s:select name="shippingOrderStatus">
          		<option value="">Any status</option>
          		<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="shippingOrderStatusList" value="id"
                                     label="name"/>
        		</s:select>
				</td>
				<td><label><strong>SO Status: </strong></label></td>
				</tr>
				<tr>
					<td colspan="2" align="center" style="text-align: center;"><s:submit name="searchSO" value="Search SO" /></td>
				</tr>
			</table></div>
			</s:form>
			
			<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${shippingOrderUtil}"/>
    			<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${shippingOrderUtil}"/>
			<s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderUtilityAction" autocomplete="off">
			<div >
			<c:if test="${fn:length(shippingOrderUtil.shippingOrders)>0 }">
			<table border="1" style="width: 100%; float: left;">
			<tr>
				<th>S.No.</th>
				<th>SO Id</th>
				<th>Base Order Id</th>
				<th>SO Status</th>
				<th>Dispatch Date</th>
				<th>Booked On Bright</th>
				<th></th>
			</tr>
			<c:forEach items="${shippingOrderUtil.shippingOrders}" var="so"
                   varStatus="item">
			<tr>
			
				<td>${item.count}.</td>
				<td>(<s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" event="searchShippingOrder"
             	target="_blank">
       	 		<s:param name="shippingOrderGatewayId" value="${so.gatewayOrderId}"/>${so.id}
    				</s:link>)</td>
				<td><s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders"
                                            target="_blank">
                            <s:param name="orderId" value="${so.baseOrder}"/>${so.baseOrder.id}
                        </s:link></td>
				<td>${so.orderStatus.name}</td>
				<td>${so.targetDispatchDate }</td>
				<c:choose>
				<c:when test="${hk:bookedOnBright(so)}">
				<td>Yes</td>
				</c:when>
				<c:otherwise>
				<td>No</td>
				</c:otherwise>
				</c:choose>
				<td><input type="checkbox" dataId="${so.id}" class="shippingOrderDetailCheckbox"/></td>
				<s:hidden name="shippingOrderMarked[${item.count-1}]" value="${so.id}"/>
			</tr>
			</c:forEach>
			</table>
			<div id="hiddenShippingIds"></div>
			<div style="float:right"><input type="submit" value="Mark All" id="markAll"/></div>
			<div class="buttons" style="margin-left: 80%;"><s:submit name="validate" id="validateButton" class="soAction"
                                                             value="Validate Shipping Order(s)"/></div>
			</c:if>
			
			</div>
			
		</s:form>
		<script type="text/javascript">
    	$('.soAction').click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
            var shippingOrderDetailCheckbox = $(this);
            var isChecked = shippingOrderDetailCheckbox.attr('checked');
            if (isChecked) {
                $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
            }
        });
        return true;
    	});

    	$('#markAll').click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
            var shippingOrderDetailCheckbox = $(this);
            var isChecked = shippingOrderDetailCheckbox.attr('checked');
            shippingOrderDetailCheckbox.attr("checked", true);
        });
        return false;
    	});

		</script>

	</s:layout-component>

</s:layout-render>
