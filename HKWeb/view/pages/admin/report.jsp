<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.BaseDao" %>
<%@ page import="com.hk.domain.order.ShippingOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportActionBean"/>
<%
    WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
    BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
    pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
	pageContext.setAttribute("soStatusList", baseDao.getAll(ShippingOrderStatus.class));
%>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

<s:layout-component name="htmlHead">

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $('#markAll').click(function() {
                if ($(this).attr("checked") == "checked") {
                    $('.lineItemCheckBox').each(function() {
                        this.checked = true;
                    })
                } else {
                    $('.lineItemCheckBox').each(function() {
                        this.checked = false;
                    })
                }
            });
        });
    </script>

    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

    <script type="text/javascript">
        $(document).ready(function() {
            $('.paymentModeSelect').change(function() {
                if ($('.paymentModeSelect').val() == '40') {
                    $('.codModeSelect').removeAttr('disabled');
                } else {
                    $('.codModeSelect').attr('disabled', 'disabled');
                }
            })
        });
    </script>

</s:layout-component>

<s:layout-component name="heading">Report Master : Orders# ${reportActionBean.totalOrders}
    &nbsp;|&nbsp; MRP:<fmt:formatNumber value="${reportActionBean.sumOfMrp}" type="currency" currencySymbol=" "
                                        maxFractionDigits="0"/>
    &nbsp;|&nbsp; HKPrice:<fmt:formatNumber value="${reportActionBean.sumOfHkPrice}" type="currency" currencySymbol=" "
                                            maxFractionDigits="0"/>
</s:layout-component>

<s:layout-component name="content">
<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <s:errors/>
        <fieldset class="right_label">
            <legend>Sales Reports</legend>
            <ul>

                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>

                <li>
                    <s:submit name="generateSalesByDateReport" value="Generate Sales-by-date Report"/>
                        <%--
                                  <s:submit name="generateDetailedSalesDailyReport" value="Generate Detailed Sales Report"/>
                                  <s:submit name="generateAccountingSalesExcel" value="Generate Accounting Sales Excel"/>
                                  <s:submit name="generateAccountingSalesExcelForBusy" value="Generate Sales Excel for Busy"/>
                                  <s:submit name="generateSalesByDateReportForShippedProducts"
                                            value="Generate Sales Report by Ship Date"/>
                        --%>

                </li>
            </ul>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <s:errors/>
        <fieldset class="right_label">
            <legend>Product Variant Sales Report</legend>
            <ul>
                <li>
                    <label>Start date</label>
                    <s:text class="date_input startDate" style="width:150px"
                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>

                <li>
                    <label>End date</label>
                    <s:text class="date_input endDate" style="width:150px"
                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>

                <li>
                    <label>Base Order Status</label>
                    <s:select name="orderStatus">
                        <option value="">Any order status</option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="orderStatusList"
                                                   value="id" label="name"/>
                    </s:select>
                </li>

                <li>
                    <label>Top Level Category</label>
                    <s:select name="topLevelCategory">
                        <option value="">All categories</option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                                   value="name" label="displayName"/>
                    </s:select>
                </li>

                <li>
                        <%--<s:submit name="generateSKUSalesExcel" value="Generate Excel"/>--%>
                    <s:submit name="generateOrderReportExcel" value="Generate Report"/>
                </li>
            </ul>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <s:errors/>
        <fieldset class="right_label">
            <legend>Performance Reports</legend>
            <ul>

                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>

                <li>
                    <s:submit name="generateMasterPerformanceReport" value="Generate Master Performance Report"/>
                        <%--&nbsp;<s:submit name="generateShipmentPerformaceReport" value="Generate Shipment Performace Report"/>--%>
                        <%-- &nbsp;<s:submit name="generateCODConfirmationReport" value="Generate CoD Confirmation Report"/>--%>

                </li>
            </ul>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <s:errors/>
        <fieldset class="right_label">
            <legend>CRM Report</legend>
            <ul>
                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>

                <li><label>Top Level Category</label><s:select name="topLevelCategory">
                    <option value="">All categories</option>
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                               value="name" label="displayName"/>
                </s:select></li>

                <li><s:submit name="generateCRMReport" value="Generate CRM Report"/>
                </li>
            </ul>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction">
        <s:errors/>
        <fieldset class="right_label">
            <legend>Courier Delivery Report</legend>
            <ul>
                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>

                <li><label>Courier Name</label><s:select name="courier">
                    &lt;%&ndash;<option value="">All Couriers</option>&ndash;%&gt;
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList"
                                               value="id" label="name"/>
                </s:select></li>

                <li><s:submit name="generateCourierReport" value="Generate Courier Report"/>
                </li>
            </ul>
        </fieldset>
    </s:form>
</div>


<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <fieldset class="right_label">
            <legend>Sales Report By Product</legend>
            <ul>
                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>
                <li>
                    <label>
                        Product Ids
                    </label><s:text name="productIdListCommaSeparated" style="width:200px"/>
                </li>
                <li>
                    (Enter Ids separated by comma)
                </li>
            </ul>
            <s:submit name="generateInventorySoldByDate" value="Generate Report"/>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <%--<s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">--%>
        <%--<fieldset class="right_label">--%>
            <%--<legend>Stock Ledger By Variant</legend>--%>
            <%--<ul>--%>
                <%--<li>--%>
                    <%--<label>Start--%>
                        <%--date</label><s:text class="date_input startDate" style="width:150px"--%>
                                            <%--formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>--%>
                <%--</li>--%>
                <%--<li>--%>
                    <%--<label>End--%>
                        <%--date</label><s:text class="date_input endDate" style="width:150px"--%>
                                            <%--formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>--%>
                <%--</li>--%>
                <%--<li>--%>
                    <%--<label>--%>
                        <%--Variant Ids--%>
                    <%--</label><s:text name="productIdListCommaSeparated" style="width:200px"/>--%>
                <%--</li>--%>
                <%--<li>--%>
                    <%--(Enter Ids separated by comma)--%>
                <%--</li>--%>
                <%--<li>--%>
                    <%--<label>--%>
                        <%--Warehouse--%>
                    <%--</label>--%>

                    <%--<s:select name="warehouse" style="height:30px;font-size:1.2em;padding:1px;">--%>
                        <%--<c:forEach items="${whList}" var="wh">--%>
                            <%--<s:option value="${wh.id}">${wh.name}</s:option>--%>
                        <%--</c:forEach>--%>
                    <%--</s:select>--%>

                <%--</li>--%>
            <%--</ul>--%>
            <%--<s:submit name="generateStockReport" value="Generate Report"/>--%>
        <%--</fieldset>--%>
    <%--</s:form>--%>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <fieldset class="right_label">
            <legend>Expiry Alert Report</legend>
            <ul>
                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>
                <li>
                    <label>
                        Warehouse
                    </label>

                    <s:select name="warehouse" style="height:30px;font-size:1.2em;padding:1px;">
                        <c:forEach items="${whList}" var="wh">
                            <s:option value="${wh.id}">${wh.identifier}</s:option>
                        </c:forEach>
                    </s:select>

                </li>
            </ul>
            <s:submit name="generateExpiryAlertReport" value="Generate Report"/>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <fieldset class="right_label">
            <legend>RTO Report</legend>
            <ul>
                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>
                <li>
                    <label>
                        Warehouse
                    </label>

                    <s:select name="warehouse" style="height:30px;font-size:1.2em;padding:1px;">
                        <c:forEach items="${whList}" var="wh">
                            <s:option value="${wh.id}">${wh.identifier}</s:option>
                        </c:forEach>
                    </s:select>

                </li>
            </ul>
            <s:submit name="generateRTOReport" value="Generate Report"/>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <fieldset class="right_label">
            <legend>Reconciliation Voucher Report</legend>
            <ul>
                <li>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>
                <li>
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>
                <li>
                    <label>
                        Variant Ids
                    </label><s:text name="productIdListCommaSeparated" style="width:200px"/>
                </li>
                <li>
                    (Enter Ids separated by comma)
                </li>
                <li>
                    <label>
                        Warehouse
                    </label>

                    <s:select name="warehouse" style="height:30px;font-size:1.2em;padding:1px;">
                        <c:forEach items="${whList}" var="wh">
                            <s:option value="${wh.id}">${wh.identifier}</s:option>
                        </c:forEach>
                    </s:select>

                </li>
            </ul>
            <s:submit name="generateReconciliationVoucherReport" value="Generate Report"/>
        </fieldset>
    </s:form>
</div>

<div class="reportBox">
    <s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
        <s:errors/>
        <fieldset class="right_label">
            <legend>Purchase Order Report (Variant Id Optional)</legend>
            <ul>
                <li>
                    <label>Start date</label>
                    <s:text class="date_input startDate" style="width:150px"
                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                </li>

                <li>
                    <label>End date</label>
                    <s:text class="date_input endDate" style="width:150px"
                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
                </li>

                <li>
                    <label>
                        Variant Ids
                    </label><s:text name="productIdListCommaSeparated" style="width:200px"/>
                </li>
                <li>
                    <label>
                        Warehouse
                    </label>

                    <s:select name="warehouse" style="height:30px;font-size:1.2em;padding:1px;">
                        <c:forEach items="${whList}" var="wh">
                            <s:option value="${wh.id}">${wh.identifier}</s:option>
                        </c:forEach>
                    </s:select>

                </li>
                <li>
                    <s:submit name="generatePOReportByVariant" value="Generate Report"/>
                </li>
            </ul>
        </fieldset>
    </s:form>
</div>


<div class="reportBox">
	<s:form beanclass="com.hk.web.action.report.ReportAction" target="_blank">
		<s:errors/>
		<fieldset class="right_label">
			<legend>Shipping Order Status Report</legend>
			<ul>

				<li>
					<label>
						SO Status
					</label><s:select name="shippingOrderStatus" style="height:30px;font-size:1.2em;padding:1px;">
					<c:forEach items="${soStatusList}" var="sos">
						<s:option value="${sos.id}">${sos.name}</s:option>
					</c:forEach>
				</s:select>
				</li>
				<li>
					<s:submit name="generateReportBySOStatus" value="Generate Report"/>
				</li>
			</ul>
		</fieldset>
	</s:form>
</div>

</s:layout-component>
</s:layout-render>
