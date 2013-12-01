
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" var="rmsa"    />
<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:layout-component name="content">
        <s:messages/>

        <s:form beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction">

            <fieldset class="right_label">
                <legend>Create Product Mail Settings</legend>
                <s:hidden name="editSettings" value="${rmsa.editSettings}"/>
                <c:if test="${rmsa.editSettings}">
                    <s:hidden name="productReviewMail" value="${rmsa.productReviewMail.id}"/>
                </c:if>
                <ul class="productList">
                    <li>
                        <label>Product ID*</label>
                        <c:if test="${rmsa.editSettings}">
                        <span style= "color:#ff0000;">${rmsa.product}</span>
                        <s:hidden name="product" value="${rmsa.product}"/><%--(eg: NUT130)  <s:submit name="editProductSettings" value="edit" />--%>
                        </c:if>
                        <c:if test="${!rmsa.editSettings}">
                            <s:text name="product"/>
                        </c:if>
                    </li>
                    <li>
                        <label>Days To Mail After Delivery*</label>
                        <s:text name="productReviewMail.timeWindowDays"  />
                    </li>
                    <li>
                        <label>Days To Review Again*</label>
                        <s:text name="productReviewMail.daysToSendReviewMailAgain"  />
                    </li>
                    <li>
                        <label>Mail Type*</label>
                        <s:select name="productReviewMail.mail">
                            <s:option value="">----Select Mail Template---</s:option>
                            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allMailType"
                                                       value="id" label="name"/>
                        </s:select>
                    </li>
                    <li>
                        <label>Test Email Id*</label>
                        <s:text name="productReviewMail.testEmailId"  maxlength="50"/>
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
        <s:link beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction">BACK </s:link>

        <%--<s:form beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction" >
            <label>Order ID</label>
            <s:text name="order" /><s:submit name= "test" value="User Entry" />
        </s:form>--%>

    </s:layout-component>
</s:layout-render>