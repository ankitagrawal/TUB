<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.shipment.ParseCourierDeliveryStatusExcelAction">
        <fieldset>
            <legend>
                <h1> Upload Courier Delivery Status by Excel </h1>
            </legend>
            <br>
            <label>File to Upload: &nbsp;&nbsp;&nbsp;&nbsp;</label><s:file name="fileBean" size="30"/>
            <br>
            <br>
            <label>Click to see Courier Ids:</label>&nbsp;&nbsp;<s:link
                beanclass="com.hk.web.action.admin.shipment.ParseCourierDeliveryStatusExcelAction" target="_blank"
                event="displayCourierIdList">Courier List</s:link>
            <br>

            <div class="buttons">
                <s:submit name="parse" value="Update"/>
            </div>
            <br>

            <p><b>Columns in Excel </b>- S No, CNNO, Ref. No ,Courier Id, Customer, Code, Booking D, No. Of Pieces ,Dest.
                Pincode, Destination ,Status , Delivered Date, Remarks </p>
        </fieldset>
    </s:form>

  </s:layout-component>

</s:layout-render>