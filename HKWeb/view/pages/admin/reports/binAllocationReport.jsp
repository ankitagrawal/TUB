<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Location Report">
    <s:layout-component name="content">
        <div class="reportBox" style="height: 113px;">
            <div class="clear"> </div>
            <s:form beanclass="com.hk.web.action.report.BinAllocationReport">
                <s:errors/>
                <fieldset >
                    <legend style="font-size:10;">All Product Allocation Reports</legend>
                    <div style="height:10px;"></div>
                    <s:submit name="generateBinAllocationExcel" value="Download Report"/>
                </fieldset>
            </s:form>

        </div>
    </s:layout-component>
</s:layout-render>