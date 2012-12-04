<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="fedEx" value="<%=EnumCourier.FedEx.asCourier()%>"/>
<c:set var="fedExSurface" value="<%=EnumCourier.FedEx_Surface.asCourier()%>"/>
<%

    String shippingOrderId = request.getParameter("shippingOrderId");
    request.setAttribute("shippingOrderId", shippingOrderId);
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.CourierPickupServiceAction" event="pre" var="pickupService"/>
<c:set var="pickupNotValid" value="${pickupService.exceededPolicyLimit}"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pickup Service">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">
          $(document).ready(function() {

	          if(${pickupNotValid}) {
		          alert("Pickup cannot be done. Delivery has exceeded ");
	          }
          });
    </script>
        
    </s:layout-component>


    <s:layout-component name="heading">Reverse Pickup Service</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.CourierPickupServiceAction">
          <s:errors/>
        <fieldset>
            <ul>
                <li>
                    <label>Courier</label>
                    <s:select name="courierId" class="courier">
                        <s:option value="">-Select Courier-</s:option>
                        <s:option value="${fedEx.id}">${fedEx.name}</s:option>
                    </s:select>
                </li>
                <li>
                   <label>SO Gateway Id :</label><s:text name="shippingOrderId" value="${shippingOrderId}"/>

                </li>
                <li>
                    <label>Pickup Time</label><s:text class="date_input startDate startDateCourier" style="width:150px"
                                                      formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                      name="pickupDate"/>
                </li>
            </ul>
        </fieldset>
        <s:submit name="submit" value="Submit"/>
        </s:form>
        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pickupService}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pickupService}"/>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>SO Gateway Order Id</th>
                <th>Courier</th>
                <th>Pickup Confirmation No.</th>
                <th>Pickup Date</th>
                <th>Pickup Status</th>
                <th>Reconciliation Status</th>
                <th>User</th>
                <th>Actions</th>
            </tr>
            </thead>
            <c:forEach items="${pickupService.pickupRequestsList}" var="pickupRequest">
                <tr>
                    <td>${pickupRequest.shippingOrder.gatewayOrderId}</td>
                    <td>${pickupRequest.courier.name}</td>
                    <td>${pickupRequest.pickupConfirmationNo}}</td>
                    <td>${pickupRequest.pickupDate}</td>
                    <td>${pickupRequest.pickupStatus}</td>
                    <td>${pickupRequest.reconciliationStatus.name}</td>
                    <td>${pickupRequest.user.name}</td>
                    <%--<td>--%>
                        <%--<s:link beanclass="com.hk.web.action.admin.catalog.SupplierManagementAction" event="createOrEdit">Edit--%>
                            <%--<s:param name="supplier" value="${supplier.id}"/></s:link>--%>
	                    <%--<c:if test="${supplier.active}">--%>
		                    <%--<br/>--%>
		                    <%--<s:link beanclass="com.hk.web.action.admin.inventory.CreatePurchaseOrderAction">Create PO--%>
			                    <%--<s:param name="supplier" value="${supplier.id}"/></s:link>--%>
		                    <%--<br/>--%>
		                    <%--<s:link beanclass="com.hk.web.action.admin.inventory.DebitNoteAction" event="view">--%>
			                    <%--Raise Debit Note--%>
			                    <%--<s:param name="supplier" value="${supplier.id}"/></s:link>--%>
	                    <%--</c:if>--%>
                    <%--</td>--%>
                </tr>
            </c:forEach>
        </table>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pickupService}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pickupService}"/>
    </s:layout-component>
</s:layout-render>


