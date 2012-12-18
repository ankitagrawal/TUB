<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
    <s:layout-component name="content">
        <h2>${paymentAction.affiliate.user.name}</h2>

        <div class="reportBox">
            <s:form beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" method="get">
                <fieldset class="right_label">
                    <legend>Affiliate Due Amount Calculator</legend>
                    <ul>
                        <li>
                            <label>Start date</label>
                            <s:text class="date_input startDate" style="width:150px"
                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                        </li>

                        <li>
                            <label>End date</label>
                            <s:text class="date_input endDate" style="width:150px"
                                    formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                        </li>

                        <li>
                            <label>Net Amount Due</label>

                            <h3>${paymentAction.amount}</h3></li>
                        <li>
                            <s:hidden name="affiliate" value="${paymentAction.affiliate.id}"/>
                            <s:submit name="showAffiliateDetails" value="Calculate Affiliate Due Amount"/>
                        </li>
                    </ul>
                </fieldset>
            </s:form>
        </div>
        <br/>
        <h4>Checks Sent</h4>
        <table>
            <tr>
                <th>
                    Transaction Ref No.
                </th>
                <th>
                    Issue Date
                </th>
                <th>
                    Payment Mode
                </th>
                <th>
                    Amount Paid
                </th>
                <th>
                    TDS
                </th>
            </tr>
            <c:forEach items="${paymentAction.checkDetailsList}" var="checkDetails">
                <tr>
                    <td>
                            ${checkDetails.checkNo}
                    </td>
                    <td>
                            ${checkDetails.issueDate}
                    </td>
                    <td>
                            ${checkDetails.bankName}
                    </td>
                    <td>
                            ${checkDetails.affiliateTxn.amount + checkDetails.tds}
                    </td>
                    <td>
                            ${checkDetails.tds}
                    </td>
                </tr>
            </c:forEach>
        </table>

        <br/>

        <div class="buttons">
            <s:link beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" event="paymentDetails">Pay
                <s:param name="affiliate" value="${paymentAction.affiliate.id}"/>
            </s:link>
        </div>
    </s:layout-component>
</s:layout-render>