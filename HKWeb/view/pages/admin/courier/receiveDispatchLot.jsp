<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.DispatchLotAction" var="dispatchLotBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Receive Dispatch Lot">

	<s:layout-component name="htmlHead">
		<script type="text/javascript">
			$(document).ready(function () {


				function stopRKey(evt) {
					var evt = (evt) ? evt : ((event) ? event : null);
					var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);

					/*Checking if key pressed is enter then add another row in awbTable body.*/
					if ((evt.keyCode == 13) && (node.type == "text")) {

						/*function to add new row.*/
						/*getting last row index using its attribute count*/
						var lastIndex = $('.lastRow').attr('count');
						if (!lastIndex) {
							lastIndex = -1;
						}
						/* removing the 'class' attribute of <tr> .Earlier it was 'lastRow lineItemRow',now removing 'lastRow' from it,so that
						 * its no more the last one.*/
						$('.lastRow').removeClass('lastRow');

						var nextIndex = eval(lastIndex + "+1");

						var newRowHtml =
								'<tr count="' + nextIndex + '" class="lastRow lineItemRow" id="awbTableTr' + nextIndex + '">' +
										'  <td>' +
										'    <input type="text" id="gatewayOrderIdList' + nextIndex + '" name="gatewayOrderIdList[' + nextIndex + ']"/>' +
										'  </td>' +
										'</tr>';

						/*appending the new row*/
						$('#awbTable').append(newRowHtml);
						var elmId = '#gatewayOrderIdList' + nextIndex;
						$(elmId).focus();
						return false;


					}
				}

				/*calling stopRKey on pressing any key.*/
				document.onkeypress = stopRKey;


				/*setting focus on first Awb Number text onbodyload*/
				$('#gatewayOrderIdList0').focus();

				/* reloading page on submitting it*/
				$('.reloadLink').click(function () {
					location.reload();
					return false;

				});


				/*function to delete last row.*/
				$('.removeRowButton').click(function () {

					/*Fetching last row index.*/
					var lastIndex = $('.lastRow').attr('count');

					/*checking if index is 0 then don't delete the row,else delete it.*/
					if (lastIndex == 0) {
						return false;
					} else {
						$('#awbTable tr:last').remove();
						/*After deleting the row,changing the class attribute from 'lineItemRow' to 'lastRow lineItemRow' to make it the last row. */
						var previousIndex = eval(lastIndex + "-1");
						var previousRow = "#awbTableTr" + previousIndex;
						$(previousRow).removeClass("lineItemRow").addClass("lastRow lineItemRow");
					}
					return false;
				});

			});

		</script>

		<style type="text/css">

			fieldset input[type="text"], input[type="text"] {
				font-size: 14px;
				padding: 2px;
				height: 18px;
				width: 200px;
				max-width: 300px;
			}

		</style>

	</s:layout-component>
	<s:layout-component name="heading">
		<h4>Receive Dispatch Lot: ${dispatchLotBean.dispatchLot.id}</h4>
	</s:layout-component>

	<s:layout-component name="content">
		<div class="hkDeliveryBox">
			<s:form beanclass="com.hk.web.action.admin.courier.DispatchLotAction">
				<fieldset class="top_label">
					<legend>Details</legend>
					<table>
						<tr>
							<td><label><strong>Docket Number</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.docketNumber}</td>

							<td><label><strong>Create Date</strong></label></td>
							<td><fmt:formatDate value="${dispatchLotBean.dispatchLot.createDate}" pattern="dd-MM-yyyy" /></td>

							<td><label><strong>Created By</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.createdBy.name}</td>

							<td><label><strong>Dispatch Date</strong></label></td>
							<td><fmt:formatDate value="${dispatchLotBean.dispatchLot.dispatchDate}" pattern="dd-MM-yyyy" /></td>

							<td><label><strong>Receiving Date</strong></label></td>
							<td><fmt:formatDate value="${dispatchLotBean.dispatchLot.receivingDate}" pattern="dd-MM-yyyy" /></td>

							<td><label><strong>Received By</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.receivedBy.name}</td>

							<td><label><strong>No. Of Shipments Sent</strong></label></td>
							<td>
								<c:choose>
									<c:when test="${dispatchLotBean.dispatchLot.noOfShipmentsSent == null}">
										0
									</c:when>
									<c:otherwise>
										${dispatchLotBean.dispatchLot.noOfShipmentsSent}
									</c:otherwise>
								</c:choose>
							</td>
						</tr>

						<tr>
							<td><label><strong>Courier</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.courier.name}</td>

							<td><label><strong>Receiving Zone</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.zone.name}</td>

							<td><label><strong>Source</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.source}</td>

							<td><label><strong>Destination</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.destination}</td>

							<td><label><strong>No. of mother bags</strong></label></td>
							<td>
								<c:choose>
									<c:when test="${dispatchLotBean.dispatchLot.noOfMotherBags == null}">
										0
									</c:when>
									<c:otherwise>
										${dispatchLotBean.dispatchLot.noOfMotherBags}
									</c:otherwise>
								</c:choose>
							</td>
							<td><label><strong>No. Of Shipments Received</strong></label></td>
							<td>
								<c:choose>
									<c:when test="${dispatchLotBean.dispatchLot.noOfShipmentsReceived == null}">
										0
									</c:when>
									<c:otherwise>
										${dispatchLotBean.dispatchLot.noOfShipmentsReceived}
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</table>
					<table>
						<tr>
							<td><label><strong>Remarks</strong></label></td>
							<td>${dispatchLotBean.dispatchLot.remarks}</td>
						</tr>
					</table>
				</fieldset>

				<fieldset class="right_label">
					<legend>Receive Shipments:</legend>
					<ul>
						<s:hidden name="dispatchLot" value="${dispatchLotBean.dispatchLot.id}"/>
						<li>
							<table border="1">
								<thead>
								<tr>
									<th style="width:200px;font-size:medium;">GATEWAY ORDER ID</th>
								</tr>
								</thead>

								<tbody id="awbTable">
								<tr count="0" class="lastRow lineItemRow" id="awbTableTr0">
									<td>
										<input type="text" id="gatewayOrderIdList0" name="gatewayOrderIdList[0]"/>
									</td>
								</tr>
								<c:forEach var="gatewayOrderIdList" items="${dispatchLotBean.gatewayOrderIdList}"
								           varStatus="ctr">
									<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
										<td>
												${gatewayOrderIdList}
										</td>
									</tr>

								</c:forEach>
								</tbody>
							</table>

						</li>
						<li>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="#" class="removeRowButton"
							   style="font-size:1.2em;color:blue;">Remove row</a>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="#" class="reloadLink"
							   style="font-size:1.2em;color:blue;">Reload</a>


						</li>
						<li>
							<s:submit id="submitButton" name="markShipmentsReceived"
							          value="Mark Shipments as Received"/>
						</li>
					</ul>
				</fieldset>
			</s:form>
		</div>

	</s:layout-component>
</s:layout-render>



