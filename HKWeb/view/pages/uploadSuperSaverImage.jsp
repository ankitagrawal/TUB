<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction" var="uploadBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="htmlHead">
        <style type="text/css">
            input[type="submit"] {
                font-size: 0.8em;
                margin: 10px;
            }
        </style>
    </s:layout-component>

    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
            File to Upload <s:file name="fileBean" size="30"/>

            <div class="clear"></div>

            <s:submit name="uploadSuperSaverImage" value="Upload Banner"/>
        </s:form>

        <s:form beanclass="com.hk.web.action.core.catalog.SuperSaversAction">
            <s:submit name="pre" value="Back to Super Savers"/>
        </s:form>
    </s:layout-component>
</s:layout-render>