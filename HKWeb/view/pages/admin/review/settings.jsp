<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/9/13
  Time: 12:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.hk.pact.service.review.MailService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" var="rmsa"    />
<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:layout-component name="content">
        <s:messages/>
        <s:link beanclass="com.hk.web.action.admin.review.CreateMailTemplateAction">Create Mail Template</s:link>
        <s:form beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction">

            <fieldset class="right_label">
                <legend>Create Product Mail Settings</legend>
                <ul class="productList">
                    <li>
                        <label>Product ID</label>
                        <s:text name="product" />  (eg: NUT130)  <s:submit name="editProductSettings" value="edit" />
                    </li>
                    <li>
                        <label>Days To Mail After</label>
                        <s:text name="productReviewMail.timeWindowDays"  />
                    </li>
                    <li>
                        <label>Days To Review Again</label>
                        <s:text name="productReviewMail.daysToReviewAgain"  />
                    </li>
                    <li>
                        <label>Mail Type</label>
                        <s:select name="productReviewMail.mail">
                            <s:option value="">--select mail type--</s:option>
                            <hk:master-data-collection service="<%=MailService.class%>" serviceProperty="allMailType"
                                                       value="id" label="name"/>
                        </s:select>
                    </li>
                    <li>
                        <label>Test Email Id</label>
                        <s:text name="productReviewMail.testEmailId"  />
                    </li>
                    <li>
                        <label>is Review Mail Enabled</label>
                        <s:checkbox name="productReviewMail.isEnabled"/> &nbsp;&nbsp;
                        <c:if test="${rmsa.editSettings}">
                            <s:submit name="saveProductSettings" value="save"/>
                        </c:if>
                        <c:if test="${!rmsa.editSettings}">
                            <s:submit name="createProductSettings" value="create"/>
                        </c:if>

                    </li>
                </ul>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>