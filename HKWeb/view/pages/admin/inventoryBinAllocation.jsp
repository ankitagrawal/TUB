<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction" var="ibaa"/>

<s:layout-render name="/layouts/inventory.jsp" pageTitle="Search Batches for Barcode/VariantID">
    <s:layout-component name="heading">
        <h1 style="font-size:80px;margin-top:50px;width:1500px;height:100px">
            ASSIGN BIN
        </h1>
    </s:layout-component>
    <s:layout-component name="htmlHead">
        <style type="text/css">
            .messages li{
                font-size: 30px;background: #66ff66;color: white; font-size: 20px;
            }

            .error li {
                font-size: 30px;
                background: #ff0033;
                color: white;
                font-size: 20px;
            }
        </style>
    </s:layout-component>
    <div align="center">

        <c:if test="${param['saved']=='true'}">
         <div class="messages" style=" font-size: 30px;background: #66ff66;color: white; font-size: 20px;">  <s:messages key="generalMessages"/>  </div>
        </c:if>
        <c:if test="${param['saved']=='false'}">
            <div class="error" style="font-size: 30px;background: #ff0033;color: white; font-size: 20px;"><s:errors/></div>
        </c:if>
        <s:layout-component name="content">
        <div style="margin-top:100px" height="500px" align="center">
            <s:form beanclass="com.hk.web.action.admin.inventory.checkin.InventoryBinAllocationAction" autocomplete="off">
                <label><b>SCAN THE BAR CODE HERE</b></label>
                <br/><br/>
                <label>Product Barcode : </label>
                <s:text name="barcode" id="barcode" style="font-size:40px; padding:20px;height:30px;width:500px;"/><br/><br/>
                <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bin/Location : </label>
                <s:text name="location" id="location"
                        style="font-size:40px; padding:20px;height:30px;width:500px;"/><br/><br/>

                <div><s:submit name="save" value="SAVE"/>
                </div>
            </s:form>
        </div>
        </s:layout-component>
</s:layout-render>