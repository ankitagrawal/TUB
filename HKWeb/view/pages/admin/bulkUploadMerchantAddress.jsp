<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.ParseExcelAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.BulkUploadMerchantAddressAction">
            <h2>Merchant Name
                <s:text name="manufacturerName" size="100"/></h2>

            <h2>File to Upload
                <s:file name="fileBean" size="30"/></h2>

            <div class="buttons">
                <s:submit name="parse" value="Update"/>
            </div>
        </s:form>

    </s:layout-component>

</s:layout-render>