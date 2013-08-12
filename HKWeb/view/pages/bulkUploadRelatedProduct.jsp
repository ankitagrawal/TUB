<%--
  Created by IntelliJ IDEA.
  User: Rajesh Kumar
  Date: 5/30/13
  Time: 10:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="content">
        <div style="text-align: center;"><h2>BULK UPLOAD RELATED PRODUCT</h2></div>
        <s:form beanclass="com.hk.web.action.admin.catalog.product.BulkUploadRelatedProductAction">
            <div>
                <fieldset>
                    <legend><h1>Upload Related Product By Excel</h1></legend>
                    <br>
                    <label>File to Upload:</label><s:file name="fileBean" size="30"/>
                    <br>
                    <br>
                    <s:submit name="save" value="Save"/>
        </s:form>
        <p>Columns in Excel - PRODUCT_ID,RELATED_PRODUCTS(Separated by "|" )</p>
        </fieldset>
        </div>

    </s:layout-component>
</s:layout-render>