<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.hkDelivery.EnumConsignmentStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Runsheet List">

    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction" var="runsheetAction"/>
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
    <s:layout-component name="heading">
        Edit Runsheet
    </s:layout-component>

    <s:layout-component name="content">
        <script>
        $(document).ready(function(){
            $('.consignment-status-button').click(function(event){
                event.preventDefault();
                var new_status = $(this).attr('name');
                var consignment = $(this).attr('id');
                consignment= consignment.substring(7);
	             var onHoldReason = $('#on-hold-reason-'+consignment);
                $('#'+consignment).val(new_status);
                var new_status_text = $(this).text();
                $('#current-status-'+consignment).text(new_status_text);
                $('#new-'+consignment).val(new_status);
	            if(new_status == <%=EnumConsignmentStatus.ShipmentOnHoldByCustomer.getId()%>){

		             onHoldReason.show();
	            }
	            else{
		            onHoldReason.hide();
	            }
            });

/*
            $('.consignment-status').change(function(){
                var cons_id = $(this).attr("id");
                var current_status = $('#'+cons_id).val();

                $('#new-'+cons_id).val(current_status);
            });
*/

            $('#save-runsheet').click(function(){
                createChangedConsignmentList();
            });

	        $('.popup-expected-amount').click(function(event){
		        var expected_amount = 0;
		        $('.consignment-row').each(function(index){
			        var payment_type = $(this).find('.payment-type').text();
			        var cons_status = $(this).find('.cons-status').text();
			        if(payment_type.toLowerCase() == "COD".toLowerCase()
					        && cons_status.toLowerCase().indexOf("<%=EnumConsignmentStatus.ShipmentDelivered.getStatus()%>".toLowerCase()) != -1){
						var current_amount_string= $(this).find('.amount').text();
						var current_amount = (parseFloat(current_amount_string.toString().replace(/\D+/g,''), 10))/100;
						expected_amount += current_amount;
			        }
					//check if reason is not null when shipment is on hold by customer
			        var onHoldReason = $(this).find('.on-hold-reason').val();
			        if(cons_status.indexOf("<%=EnumConsignmentStatus.ShipmentOnHoldByCustomer.getStatus()%>") != -1){
				        if(onHoldReason=="" || !onHoldReason || onHoldReason==" "){
					        alert("On hold by customer reason cannot be left blank, please select one !")
					        event.preventDefault();
					        return false;
				        }
			        }
		        });
		        alert("Current expected amount = "+expected_amount);
	        });

            $('.closeConfirmationDialogue').click(function(event){
                var confirm_action = confirm("Are you sure you want to close the runsheet?");
                if(confirm_action==false){
                    event.preventDefault();
                }
	            else{
	                createChangedConsignmentList();
                }
            });

            $('.markAllConfirmationDialogue').click(function(event){
                var confirm_action = confirm("Are you sure you want to Mark all consignment delivered?");
                if(confirm_action!=true){
                    event.preventDefault();
                }
            });

            function createChangedConsignmentList(){
                $('.consignment-row').each(function(index){
                    var consignment_id = $(this).find('.consignment-status').attr('id');
                    var old_status = $(this).find('.old-status').val();
                    var new_status = $(this).find('.new-status').val();
                     if(old_status != new_status){
                         $(this).find('.changed-consignment-list').val(consignment_id);
                     }
                    // alert('consignment: '+consignment_id+'   old: '+old_status+'  new:  '+new_status);
                });


            };

        });
        </script>

        <fieldset class="right_label">
        <legend>Runsheet</legend>
        <c:set var="onHoldByCustomerId" value="<%=EnumConsignmentStatus.ShipmentOnHoldByCustomer.getId()%>"/>
        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction">
            <s:hidden name="runsheet" value="${runsheetAction.runsheet.id}"/>

            <table>
                <tr>
                    <td><label>Runsheet ID:</label></td>
                    <td> ${runsheetAction.runsheet.id}</td>
                    <td><label>Hub: </label></td>
                    <td> ${runsheetAction.runsheet.hub.name}</td>
                    <td><label>COD Boxes: </label></td>
                    <td>${runsheetAction.runsheet.codBoxCount}</td>
                </tr>
                <tr>
                    <td><label>Expected amount:</label></td>
                    <td> ${runsheetAction.runsheet.expectedCollection}</td>
                    <td><label>Actual collected amount: </label></td>
                    <td><s:text name="runsheet.actualCollection"
                                value="${runsheetAction.runsheet.actualCollection}"/></td>
                    <td><label>Pre paid Boxes: </label></td>
                    <td>${runsheetAction.runsheet.prepaidBoxCount}</td>
                </tr>
                <tr>
                    <td><label>Agent:</label></td>
                    <td>
<%--
                        <s:select name="runsheet.agent" value="${runsheetAction.runsheet.agent.id}">
                            <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                       serviceProperty="HKDeliveryAgentList" value="id"
                                                       label="name"/>
                        </s:select>
--%>
                        ${runsheetAction.runsheet.agent.name}
                    </td>
                    <td><label>Remarks: </label></td>
                    <td><s:textarea name="runsheet.remarks" style="height:50px;"/></td>
                    <td><label>Status: </label></td>
                    <td><%--<s:select name="runsheet.runsheetStatus" value="${runsheetAction.runsheet.runsheetStatus.id}">
                        <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                   serviceProperty="runsheetStatusList" value="id"
                                                   label="status"/>
                    </s:select>--%>
                        ${runsheetAction.runsheet.runsheetStatus.status}
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>Distance Traveled (Kms): </label></td>
                    </td>
                    <td>
                        <s:text name="runsheet.distanceTraveled"
                                value="${runsheetAction.runsheet.distanceTraveled}"/>
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>

            </fieldset>

            <h3><strong>Consignments</strong></h3>
            <table class="zebra_vert">
                <thead>
                <tr>
                    <th>S. NO.</th>
                    <th>AWB Number</th>
                    <th>CNN Number</th>
                    <th>Amount</th>
                    <th>Payment Mode</th>
                    <th>Reconciliation Id</th>
                    <th>Status</th>
                    <th>Action</th>
	                <th>Reason</th>

                </tr>
                </thead>
                <c:forEach items="${runsheetAction.runsheetConsignments}" var="consignment" varStatus="ctr">
                    <tr class="consignment-row">
                        <s:hidden class = "changed-consignment-list" name="changedConsignmentList[${ctr.index}]" value="" />
                        <td><s:hidden name="runsheetConsignments[${ctr.index}]" value="${consignment.id}"/>${ctr.index+1}</td>
                        <td>${consignment.awbNumber}</td>
                        <td>${consignment.cnnNumber}</td>
                        <td class="amount"><fmt:formatNumber value="${consignment.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="2"/></td>
                        <td class="payment-type">${consignment.paymentMode}</td>
                        <td>${consignment.hkdeliveryPaymentReconciliation.id}</td>
                        <td class="cons-status"><s:hidden class="consignment-status" id= "${consignment.id}" name="runsheetConsignments[${ctr.index}].consignmentStatus"
                                      value="${consignment.consignmentStatus.id}">
                            </s:hidden>
                            <span id="current-status-${consignment.id}">${consignment.consignmentStatus.status}</span>
                        <input type="hidden" class="old-status" id="old-${consignment.id}" value="${consignment.consignmentStatus.id}" />
                        <input type="hidden" class="new-status" id="new-${consignment.id}" value="${consignment.consignmentStatus.id}" />
                        </td>
                        <td>
	                    
                             <c:forEach items="${runsheetAction.consignmentStatuses}" var="consignmentStatus" varStatus="statusCtr">
                                 <a href="#" class="linkbutton consignment-status-button" id= "status-${consignment.id}" name="${consignmentStatus.id}">
                                    ${consignmentStatus .status}
                                     <c:if test="${statusCtr.index == 3}" >
                                         <br />
                                     </c:if>
                                 </a>&nbsp;&nbsp;
                             </c:forEach>
                        </td>
	                    <td>
		                    <c:choose>
		                    <c:when test="${runsheetAction.consignmentOnHoldReason[consignment] !=null
		                    && consignment.consignmentStatus.id== onHoldByCustomerId}">
			                    <s:select id="" name="consignmentOnHoldReason[${consignment.id}]"
					                    class="on-hold-reason">
			                    <option value="">-Select Reason-</option>
				                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
				                                               serviceProperty="customerOnHoldReasonsForHkDelivery"
						                    />
			                    </s:select>
		                    </c:when>
		                    <c:otherwise>
			                    <s:select id="on-hold-reason-${consignment.id}" style="display:none;" name="consignmentOnHoldReason[${consignment.id}]"
					                    class="on-hold-reason">
			                    <option value="">-Select Reason-</option>
				                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
				                                               serviceProperty="customerOnHoldReasonsForHkDelivery"
						                    />
			                    </s:select>
		                    </c:otherwise>
		                    </c:choose>
	                    </td>
                    </tr>
                </c:forEach>
            </table>
            <c:if test="${runsheetAction.runsheet.runsheetStatus.id !=  20}" >
                <s:submit id="save-runsheet" name="saveRunsheet" value="Save runsheet" class="popup-expected-amount"/>
                <s:submit class="popup-expected-amount closeConfirmationDialogue" name="closeRunsheet" value="Close runsheet" />
                <s:submit class="popup-expected-amount markAllConfirmationDialogue" name="markAllDelivered" value="Mark all as delivered"/>
            </c:if>
        </s:form>
    </s:layout-component>
</s:layout-render>