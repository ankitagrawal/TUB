<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:layout-component name="content">
        <fieldset>
            <legend>Upload Excel to Update B2B Price</legend>
            <br/>
            <span class="large gry">(VARIANT_ID, B2B_PRICE) as 2003 excel headers</span>
            <br/><br/>
            <s:form beanclass="com.hk.web.action.admin.catalog.ParseExcelAction">
                <h2>File to Upload: <s:file name="fileBean" size="30"/></h2>

                <div class="buttons">
                    <s:submit name="parseB2BPriceExcel" value="Update"/>
                </div>
            </s:form>
        </fieldset>

    </s:layout-component>

</s:layout-render>