<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.courier.EnumCourier" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.ReversePickupCourierAction" event="pre" var="pickupAction"/>
<c:set var="apiCallCouriers" value="<%=EnumCourier.getFedexCouriers()%>"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pickup Service">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">
            <%--$(document).ready(function() {--%>
            <%--$('#selectedCourier').change(function() {--%>
            <%--var courierVal = $('#selectedCourier').val();--%>
            <%--var courier = parseInt(courierVal);--%>
            <%--var courierList = ${apiCallCouriers};--%>

            <%--if (jQuery.inArray(courier, courierList) != '-1') {--%>
            <%--$('.manualConfNoField').hide();--%>
            <%--$('.manualTrckNoField').hide();--%>
            <%--} else {--%>
            <%--$('.manualConfNoField').show();--%>
            <%--$('.manualTrckNoField').show();--%>
            <%--}--%>
            <%--});--%>

            <%--$('#checkSubmit').click(function() {--%>
            <%--if ($('.manualTrckNoField').is(':visible') && $('.manualConfNoField').is(':visible')) {--%>
            <%--var trackingNo = $('#trackingNo').val();--%>
            <%--var confNo = $('#pickupConfNo').val();                        --%>
            <%--if ((trackingNo == null || trackingNo == "") && (confNo == null || confNo==  "")) {--%>
            <%--alert("Please enter a tracking/confirmation number, as given by courier");--%>
            <%--return false;--%>
            <%--}--%>
            <%--}--%>

            <%--});--%>
            <%--});--%>
        </script>

    </s:layout-component>


    <s:layout-component name="heading">Schedule Reverse Pickup Service</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.ReversePickupCourierAction">
            <s:errors/>
            <fieldset>
                <ul>
                    <li>
                        <label>Courier</label>
                        <s:select name="selectedCourier" id="selectedCourier">
                            <s:option value="">-Select Courier-</s:option>
                            <c:forEach items="${pickupAction.availableCouriers}" var="courier">
                                <s:option value="${courier.id}">${courier.name}</s:option>
                            </c:forEach>
                        </s:select>
                    </li>
                    <li>
                        <label>SO Gateway Id :</label>${pickupAction.shippingOrderId}
                        <s:hidden name="shippingOrderId" value="${shippingOrderId}" style="width:120px"/>

                    </li>

                    <li>
                        <label>Pickup Time</label><s:text class="date_input startDate startDateCourier"
                                                          style="width:150px"
                                                          formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                          name="pickupDate"/>
                    </li>
                </ul>
            </fieldset>
            <s:param name="reverseOrderId" value="${pickupAction.reverseOrderId}"/>
            <s:submit name="submit" value="Submit" id="checkSubmit"/>
            <s:submit name="cancel" value="Cancel" id="checkSubmit"/>
        </s:form>

    </s:layout-component>
</s:layout-render>


