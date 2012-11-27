<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Vendor Pickup Service</title></head>
<body>
<fieldset>
    <ul>
        <li>
            <label>Courier: </label>
            <s:select name="courierName" class="courier">
                <s:option value="">-Select Courier-</s:option>
                <s:option value="">FedEx</s:option>
            </s:select>
        </li>
        <li>
            <label>PO number: </label><input type="text" value=""/>
        </li>
        <li>
            <label>Pickup Time: </label><s:text class="date_input startDate startDateCourier" style="width:150px"
                                              formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                              name="startDate"/>
        </li>
    </ul>
</fieldset>
<s:submit name="Submit" value="Submit"/>
</body>
</html>