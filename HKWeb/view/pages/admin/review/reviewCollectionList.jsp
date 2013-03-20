<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" var="rmsa"  />
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Review Collection List">

   <s:layout-component name="heading">
        Review Collection List
    </s:layout-component>

    <s:layout-component name="content">
        <s:messages/>
        <s:link beanclass="com.hk.web.action.admin.review.CreateMailTemplateAction" >Create/Search Mail Templates</s:link>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <s:link beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" event="create">Create Product Settings</s:link>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <s:link beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" event="sendDueEmail">Send Due Emails</s:link><br><br>
        <s:form beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" >
        <fieldset class="right_label">
            <legend>Search Product</legend>
                <label>Product ID:</label><s:text name="product"/>
                <label>Mail Type</label>
                <s:select name="mail">
                    <s:option value="">----Select Mail Template---</s:option>
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allMailType"
                                               value="id" label="name"/>
                </s:select>
                 <s:submit name="pre" value="Search Product"/>
            </s:form>
        </fieldset>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rmsa}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rmsa}"/>

        <table class="zebra_vert">
            <thead>
            <tr>
                <th>Product ID</th>
                <th>Create Date</th>
                <th>Created By</th>
                <th>Days to Mail After</th>
                <th>Days to Review Again</th>
                <th>Mail Type</th>
                <th>Test Email id</th>
                <th>Review Mail Enabled</th>
                <th>Last Update Date</th>
                <th>Last Updated By</th>
                <th>Actions</th>
            </tr>
            </thead>
            <c:forEach items="${rmsa.productReviewMailList}" var="productReviewMail">
                <tr>
                    <td>${productReviewMail.product}</td>
                    <td><fmt:formatDate value="${productReviewMail.createDt}" type="both" timeStyle="short"/></td>
                    <td>${productReviewMail.createdBy.name}</td>
                    <td>${productReviewMail.timeWindowDays}</td>
                    <td>${productReviewMail.daysToSendReviewMailAgain}</td>
                    <td>${productReviewMail.mail.name}</td>
                    <td>${productReviewMail.testEmailId}</td>
                    <td>${productReviewMail.isEnabled}</td>
                    <td><fmt:formatDate value="${productReviewMail.updateDt}" type="both" timeStyle="short"/></td>
                    <td>${productReviewMail.lastUpdatedBy.name}</td>
                    <td>
                        <s:link beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" event ="editProductSettings" >Edit/View
                            <s:param name="productReviewMail" value="${productReviewMail.id}"/></s:link>
                        &nbsp;
                        <s:link beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" event="deleteProductSettings">Delete
                            <s:param name="productReviewMail" value="${productReviewMail.id}"/></s:link>
                        <s:link beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" event="sendTestEmail"> Send Test Email
                            <s:param name="product" value="${productReviewMail.product}"/></s:link>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rmsa}"/>
        <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rmsa}"/>
    </s:layout-component>
</s:layout-render>