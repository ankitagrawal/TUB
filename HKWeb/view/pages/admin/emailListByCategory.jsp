<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.email.EmailListByCategoryAction" event="pre" var="mailBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="EmailListByCategoryAction">
    <s:layout-component name="heading">Emailing List</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.email.EmailListByCategoryAction" method="get" renderFieldsPresent="false"
                renderSourcePage="false">
            <fieldset class="top_label">
                <ul>
                    <div class="grouped">
                        <li><label>Category Name </label> <s:text name="category"/></li>
                    </div>
                </ul>
                <div class="buttons"><s:submit name="getMailingListAsCSV" value="Get mailing list CSV"/></div>
                <div class="buttons"><s:submit name="getAllMailingListAsCSV" value="Get complete mailing list CSV"/> </div>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>
