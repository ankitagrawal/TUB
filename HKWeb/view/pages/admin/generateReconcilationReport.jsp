<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>`
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.GenerateReconcilationReportAction" var="reportActionBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

	<s:layout-component name="htmlHead">

		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

		<script type="text/javascript">
			$(document).ready(function() {
				$('.paymentModeSelect').change(function() {
					if ($('.paymentModeSelect').val() == '40') {
						$('.codModeSelect').removeAttr('disabled');
					} else {
						$('.codModeSelect').attr('disabled', 'disabled');
					}
				});

				$('.requiredFieldValidator').click(function() {
					var paymentMode = $('.uploadPaymentMode').find('option:selected');
					if(paymentMode.text() == "-Select-") {
						alert('Please select the payment mode');
						return false;
					}
				});

				$('.downloadPaymentMode').change(function() {
					var paymentMode = $('.downloadPaymentMode').find('option:selected');
					if(paymentMode.text() == "COD") {
						$('#shippingOrderId').attr("disabled", false);
						$('#gatewayOrderId').attr("disabled", false);
						$('.courierSelect').attr("disabled", false);
						$('#baseOrderId').attr("disabled", true);
						$('#baseGatewayOrderId').attr("disabled", true);

					} else{
						$('#baseOrderId').attr("disabled", false);
						$('#baseGatewayOrderId').attr("disabled", false);
						$('#shippingOrderId').attr("disabled", true);
						$('#gatewayOrderId').attr("disabled", true);
						$('.courierSelect').val("-All-");
						$('.courierSelect').attr("disabled", true);
					}

				});

				$('.diffFieldValidator').click(function() {
					var paymentMode = $('.downloadPaymentMode').find('option:selected');
					if(paymentMode.text() == "-Select-") {
						alert('Please select the payment mode');
						return false;
					}
				});

			});
		</script>

	</s:layout-component>

	<s:layout-component name="content">
		<div class="reportBox">
			<s:form beanclass="com.hk.web.action.report.GenerateReconcilationReportAction" >
				<s:errors/>
				<fieldset class="right_label">
					<legend>Reconciliation Reports</legend>
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
							<label>Payment Mode:</label>
							<s:select name="paymentProcess">
								<s:option value="all">All</s:option>
								<s:option value="cod">COD</s:option>
								<s:option value="techprocess">TechProcess</s:option>
							</s:select>
						</li>

						<li>
							<label>Shipping Order Status</label>
							<s:select name="shippingOrderStatusId">
								<s:option value="">All</s:option>
								<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="SOStatusForReconcilation" value="id"
								                           label="name"/>
							</s:select>
						</li>

						<li>
							<label>Warehouse</label>
							<s:select name="warehouseId">
								<s:option value="0">All</s:option>
								<s:option value="1">Gurgaon</s:option>
								<s:option value="2">Mumbai</s:option>
							</s:select>
						</li>
						<li>
							<label>Courier</label>
							<s:select name="courier" class="codModeSelect">
								<s:option value="">-All-</s:option>
								<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
								                           label="name"/>
							</s:select>
						</li>
						<li>
							<s:submit name="generateReconilationReport" value="Generate-Reconcilation-Report" />
						</li>
					</ul>
				</fieldset>
			</s:form>
		</div>

		<div class="reportBox">
			<s:form beanclass="com.hk.web.action.report.GenerateReconcilationReportAction" >
				<fieldset class="right_label">
					<legend>Update Reconcile Status</legend>
					<br>
					<span class="large">(GATEWAY ORDER ID, RECONCILED, AMOUNT) as excel headers</span>
					<ul>
						<li>
							<label>Payment Mode:</label>
							<s:select name="paymentProcess" class="uploadPaymentMode" style="width: 100">
								<s:option value="all">-Select-</s:option>
								<s:option value="cod">COD</s:option>
								<s:option value="techprocess">Prepaid</s:option>
							</s:select>
						</li>
						<li>
							<h3>File to Upload: <s:file name="fileBean" size="30" id="uploadFile"/></h3>

						</li>
						<li>
							<s:submit name="parse" value="Upload reconciliation status" class="requiredFieldValidator"/>
						</li>
					</ul>
				</fieldset>
			</s:form>
		</div>

		<div class="reportBox">
			<s:form beanclass="com.hk.web.action.report.GenerateReconcilationReportAction" >
				<fieldset class="right_label">
					<legend>Difference in Reconcilation Amount Report</legend>
					<ul>
						<li>
							<label>Payment Mode:</label>
							<s:select name="paymentProcess" class="downloadPaymentMode" style="width: 100">
								<s:option value="all">-Select-</s:option>
								<s:option value="cod">COD</s:option>
								<s:option value="techprocess">Prepaid</s:option>
							</s:select>
						</li>
						<li>
							<label>SO Gateway ID </label><s:text name="gatewayOrderId" id="gatewayOrderId"/>
						</li>
						<li>
							<label>SO Order ID </label> <s:text name="shippingOrderId" id="shippingOrderId"/>
						</li>
						<li>
							<label>BO Gateway ID </label><s:text name="baseGatewayOrderId" id="baseGatewayOrderId"/>
						</li>
						<label>BO Order ID </label> <s:text name="baseOrderId" id="baseOrderId"/>
						</li>
						<li>
							<label>Courier</label>
							<s:select name="courier" class="courierSelect">
								<s:option value="">-All-</s:option>
								<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
							</s:select>
						</li>

						<li>
							<label>Start Date </label> <span class="aster">*</span> <s:text class="date_input startDate" style="width:150px"
							                                  formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate" id="startDateId"/>
						</li>
						<li>
							<label>End Date </label> <span class="aster">*</span> <s:text class="date_input endDate" style="width:150px"
							                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate" id="endDateId"/>
						</li>

						<li>
							<s:submit name="downloadPaymentDifference" value="Download Diff Report" class="diffFieldValidator"/>
						</li>
					</ul>
				</fieldset>
			</s:form>
		</div>

	</s:layout-component>
</s:layout-render>
