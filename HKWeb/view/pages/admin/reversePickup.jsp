<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="fedEx" value="<%=EnumCourier.FedEx.asCourier()%>"/>
<c:set var="fedExSurface" value="<%=EnumCourier.FedEx_Surface.asCourier()%>"/>
<c:set var="pickupNotValid" value="${pickupService.exceededPolicyLimit}"/>
<%

    String shippingOrderId = request.getParameter("shippingOrderId");
    request.setAttribute("shippingOrderId", shippingOrderId);
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.CourierPickupAction" event="pre" var="pickupService"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pickup Service">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">
          $(document).ready(function() {

	          if(${pickupNotValid}) {
		          alert("Pickup can be done only within 14 days after delivery.This has been exceeded.");
	          }
          });
    </script>
        
    </s:layout-component>


    <s:layout-component name="heading">Reverse Pickup Service</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.CourierPickupAction">
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

    </s:layout-component>
</s:layout-render>


