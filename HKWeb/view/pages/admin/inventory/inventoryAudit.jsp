<%@page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.audit.InventoryAuditAction" var="ibaa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Audit">

    <s:layout-component name="htmlHead">

        <script type="text/javascript">
            $(document).ready(function () {
                $("#firstLocation").focus();
                $("#firstLocation").bind('input propertychange', function() {
                    var location = $(this).val();
                    if (location.length > 0) {
                        $("#barcode").focus();
                    }
                });

                $("#barcode").bind('input propertychange', function() {
                    var location = $(this).val();
                    if (location.length > 0) {
                        $("#finalLocation").focus();
                    }
                });

                $("#finalLocation").change(function() {
                    $(".auditSave").click();
                });
            });
        </script>
    </s:layout-component>


    <s:layout-component name="content">
        <div style="margin-top:20px" height="500px" align="center">
            <s:form beanclass="com.hk.web.action.admin.inventory.audit.InventoryAuditAction" autocomplete="off"
                    id="locationMappingForm">
                <s:hidden name="warehouse" value="${ibaa.warehouse.id }"/>
                <label><strong>SCAN THE BAR CODE HERE</strong></label>
                <br><br>

                <table align="center">
                    <tr>
                        <td><label>Enter Location: </label></td>
                        <td><s:text name="firstLocation" id="firstLocation"
                                    style="font-size:20px; padding:20px;height:25px;width:400px;"/></td>
                    </tr>
                    <tr>
                        <td><label>Product Barcode: </label></td>
                        <td><s:text name="barcode" id="barcode"
                                    style="font-size:20px; padding:20px;height:25px;width:400px;"/></td>
                    </tr>
                    <tr>
                        <td><label>Enter Location Again: </label></td>
                        <td><s:text name="finalLocation" id="finalLocation"
                                    style="font-size:20px; padding:20px;height:25px;width:400px;"/></td>
                    </tr>
                </table>
                <s:submit name="save" value="Save" class="auditSave" style="visibility:hidden" />
            </s:form>
        </div>
    </s:layout-component>
</s:layout-render>