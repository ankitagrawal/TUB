<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.TryOnXmlsUploadAction" var="xmlBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:layout-component name="content">

        <s:form beanclass="com.hk.web.action.core.catalog.EyeGlassesFeedAction">
            <h2>Category to Generate<s:text name="category" size="30"/></h2>

            <div class="buttons">
                <s:submit name="pre" value="Generate"/>
            </div>
        </s:form>

        <s:form beanclass="com.hk.web.action.admin.catalog.TryOnXmlsUploadAction">
            <h2>File to Upload
                <s:file name="fileBean" size="30"/></h2>

            <h2>Category to Upload <s:select name="category">
                <s:option value="Spectacles">Spectacles</s:option>
                <s:option value="Sunglasses">Sunglasses</s:option>
            </s:select></h2>

            <div class="buttons">
                <s:submit name="upload" value="Update"/>
            </div>
        </s:form>

    </s:layout-component>

</s:layout-render>