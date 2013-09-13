<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" var="cpa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Payment Seeker">
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component name="content">
        <div>
            &nbsp;&nbsp;&nbsp;&nbsp;Payment Seeker (Currently works for ICICI/Citrus/EBS/Icici via Citrus (Credit debit cards only)
        </div>
        <s:form beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
            <fieldset style="width:45%;">
                <br>
                <table>
                    <tr>
                        <td><label>Enter Gateway Order Id</label></td>
                        <td><s:text name="gatewayOrderId" id = "gatewayOrderId" style="width:180px;height:25px;"/></td>
                    </tr>

                <%--<label>Start date</label>
                <s:text class="date_input startDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="txnStartDate"/>

                <label>End Date</label>
                <s:text class="date_input endDate" id = "txnEndDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="txnEndDate"/>

                <label>Enter Merchant Id</label>
                <s:text name="merchantId" id = "merchantId" style="width:180px;height:25px;"/>
                <br> <br>
                <label>Enter Payment Id (EBS)</label>
                <s:text name="paymentId" id = "paymentId" style="width:180px;height:25px;"/>--%>
                    <tr>
                        <td><label>Enter Amount</label></td>
                        <td><s:text name="amount" id = "amount" style="width:180px;height:25px;"/></td>
                    </tr>
                    <tr>
                        <td><label>Enter Reason for Refund</label></td>
                        <td><s:select name="refundReason" style="width:185px;height:28px;">
                            <s:option value="">-- Select --</s:option>
                            <c:forEach items="${cpa.refundReasons}" var="reason">
                                <s:option value="${reason.id}"> ${reason.classification.primary} - ${reason.classification.secondary}</s:option>
                            </c:forEach>
                        </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Comments </td>
                        <td><s:textarea name="reasonComments" cols="5" rows="2" id="reasonCom" style="width:320px;height:80px;"/> </td>
                    </tr>
                </table>
                <br>
<%--                <s:submit name="seekPayment" value="Seek" id="save"/>
                <s:submit name="refundPayment" value="Refund " id="refund"/>
                <s:submit name="searchTransactionByDate" value="Historical List" id="searchTransactionByDate"/>
                <s:submit name="capturePayment" value="Capture  (EBS)" id="capture"/>
                <s:submit name="cancelPayment" value="Cancel (EBS)" id="cancel"/>
                &lt;%&ndash;<shiro:hasRole name="<%=RoleConstants.GOD%>">&ndash;%&gt;
                <s:submit name="bulkSeekPayment" value="Bulk Seek" id="bulkSeek"/>
                &lt;%&ndash;</shiro:hasRole>&ndash;%&gt;--%>
                <s:submit name="seekPayment" value="Seek" id="save"/>
                <s:submit name="refundPayment" value="Refund " id="refund"/>
                <s:submit name="updatePayment"  value="Update" id="update"/>
            </fieldset>
        </s:form>

        <c:if test="${not empty cpa.hkPaymentResponseList}">
        <c:set var="count" value="1" />
            <table>
                <thead>
                <th> S No. </th>
                <th>Gateway Order Id </th>
                <th>Transaction Type </th>
                <th>Amount </th>
                <th>Payment Status </th>
                <th>Response Message </th>
                <th>Root Reference No </th>
                <th>Authentication Code </th>
                <th>Gateway </th>
                <th>Error Log </th>
                </thead>
                <tbody>
                <c:forEach  items="${cpa.hkPaymentResponseList}" var="response">
                    <tr>
                        <td>${count}</td>
                        <td>${response.gatewayOrderId}</td>
                        <td>${response.transactionType}</td>
                        <td>${response.amount}</td>
                        <td>${response.HKPaymentStatus.name}</td>
                        <td>${response.responseMsg}</td>
                        <td>${response.rrn}</td>
                        <td>${response.authIdCode}</td>
                        <td>${response.gateway.name}</td>
                        <td>${response.errorLog}</td>
                    </tr>
                    <c:set var="count" value="${count+1}" />
                </c:forEach>
                </tbody>
        </table>
    </c:if>


        <c:if test="${not empty cpa.payment}">
            <c:set var="count" value="1" />
            <table>
                <thead>
                <th>Gateway Order Id </th>
                <th>Transaction Type </th>
                <th>Amount </th>
                <th>Payment Status </th>
                <th>Response Message </th>
                <th>Root Reference No </th>
                <th>Gateway </th>
                <th>Payment Date </th>
                <th>Error Log </th>
                <th>Parent </th>
                </thead>
                <tbody>
                <tr>
                    <td>${cpa.payment.gatewayOrderId}</td>
                    <td>${cpa.payment.transactionType}</td>
                    <td>${cpa.payment.amount}</td>
                    <td>${cpa.payment.paymentStatus.name}</td>
                    <td>${cpa.payment.responseMessage}</td>
                    <td>${cpa.payment.rrn}</td>
                    <td>${cpa.payment.gateway.name}</td>
                    <td>${cpa.payment.createDate}</td>
                    <td>${cpa.payment.errorLog}}</td>
                    <td>${cpa.payment.parent.gatewayOrderId}</td>
                </tr>
                </tbody>
            </table>
        </c:if>

       <%-- <c:forEach items="${cpa.bulkHkPaymentResponseList}" var="responseMap">
            <c:forEach items="${responseMap}" var="content">
                For : ${content.key}  <br/>
                <c:forEach items="${content.value}" var="response">
                    Gateway Order Id --> ${response.gatewayOrderId}  <br/>
                    Transaction Type --> ${response.transactionType}<br/>
                    Amount --> ${response.amount} <br/>
                    Payment Status --> ${response.HKPaymentStatus.name}<br/>
                    Response Message --> ${response.responseMsg}  <br/>
                    Root Reference No --> ${response.rrn}<br/>
                    Gateway --> ${response.gateway.name}<br/>
                    Error Log --> ${response.errorLog}<br/>
                    <br/>
                </c:forEach>
                <br/>
            </c:forEach>

        </c:forEach>--%>

        <%--<c:if test="${not empty cpa.paymentStatus}">
            Status --> ${cpa.paymentStatus.name}
        </c:if>--%>


    </s:layout-component>
</s:layout-render>