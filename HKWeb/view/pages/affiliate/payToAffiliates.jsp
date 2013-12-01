<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateType" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateMode" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:layout-component name="content">
        <fieldset class="right_label">
            <legend>Search Affiliates</legend>
            <s:form beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" method="get"
                    autocomplete="false">
                <label>Name</label>
                <s:text name="name" style="width:150px"/>
                <label>Email</label>
                <s:text name="email" style="width:150px"/>
                <label>Website</label>
                <s:text name="websiteName" style="width: 100px;"/>
                <label>Type</label>
                <s:select name="affiliateType">
                    <option value="">Select</option>
                    <c:forEach items="<%=EnumAffiliateType.getAllAffiliateTypes()%>" var="aType">
                        <s:option value="${aType.id}">${aType.name}</s:option>
                    </c:forEach>
                </s:select>
                <label>Mode</label>
                <s:select name="affiliateMode">
                    <option value="">Select</option>
                    <c:forEach items="<%=EnumAffiliateMode.getAllAffiliateModes()%>" var="aMode">
                        <s:option value="${aMode.id}">${aMode.name}</s:option>
                    </c:forEach>
                </s:select>
                <label>Status</label>
                <s:select name="affiliateStatus">
                    <option value="">Select</option>
                    <c:forEach items="<%=EnumAffiliateStatus.getAllAffiliateStatus()%>" var="aStatus">
                        <s:option value="${aStatus.id}">${aStatus.name}</s:option>
                    </c:forEach>
                </s:select>
                <s:submit name="search" value="Search"/>
            </s:form>
        </fieldset>

        <table>
        <tr>
            <th>
                Name
            </th>
            <th>
                Email
            </th>
            <th>
                Website
            </th>
            <th>
                Total Amount
            </th>
            <th>
                Payable
            </th>
            <th>
                Plan
            </th>
            <th> Transaction</th>
            <th>Super Login</th>
            <th>Status</th>
            <th>CALL</th>
        </tr>

        <%--<s:form beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" autocomplete="off">--%>
            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${paymentAction}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${paymentAction}"/>
            <c:forEach items="${paymentAction.affiliatePaymentDtoList}" var="affiliateDetails" varStatus="ctr">
                <tr>
                    <td>
                            ${affiliateDetails.affiliate.user.name}
                    </td>
                    <td>
                            ${affiliateDetails.affiliate.user.email}
                    </td>
                    <td width="100px">
                            ${affiliateDetails.affiliate.websiteName}
                    </td>
                    <%--&lt;%&ndash;<s:hidden name="affiliatePaymentDtoList[${ctr.index}].affiliate" value="${affiliateDetails.affiliate}"/>&ndash;%&gt;--%>
                    <td>
                        <fmt:formatNumber value=" ${affiliateDetails.amount}"
                                          pattern="<%=FormatUtils.currencyFormatPattern%>"/>
                    </td>
                    <td>
                        <fmt:formatNumber value=" ${affiliateDetails.payableAmount}"
                                          pattern="<%=FormatUtils.currencyFormatPattern%>"/>
                    </td>
                    <td>
                        <s:link beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction"
                                event="showAffiliatePlan">Manage Plan
                            <s:param name="affiliate" value="${affiliateDetails.affiliate.id}"/>
                        </s:link>
                    </td>
                    <td>
                        <s:link beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction"
                                event="showAffiliateDetails">Transaction Details
                            <s:param name="affiliate" value="${affiliateDetails.affiliate.id}"/>
                        </s:link>
                    </td>
                    <td>
                        <s:link beanclass="com.hk.web.action.admin.user.AssumedLoginAction">
                            <s:param name="user" value="${affiliateDetails.affiliate.user.id}"/>
                            [Super login]
                        </s:link>
                    </td>
                    <td>${affiliateDetails.affiliate.affiliateStatus.name}</td>
                    <td>
                        <c:if test="${fn:length(affiliateDetails.affiliate.user.addresses) > 0}">
                            ${affiliateDetails.affiliate.user.addresses[0].phone}
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </table>

            <%--<div class="buttons"><s:submit name="save" value="Save"/></div>--%>
        <%--</s:form>--%>
    </s:layout-component>
</s:layout-render>