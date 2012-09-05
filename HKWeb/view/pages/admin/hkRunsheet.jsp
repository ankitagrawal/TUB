<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
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
                $('#'+consignment).val(new_status);
                var new_status_text = $(this).text();
                $('#current-status-'+consignment).text(new_status_text);
                $('#new-'+consignment).val(new_status);
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

            $('.closeConfirmationDialogue').click(function(event){
                var confirm_action = confirm("Are you sure you want to close the runsheet?");
                if(confirm_action==false){
                    event.preventDefault();
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
                    <th>ID</th>
                    <th>AWB Number</th>
                    <th>CNN Number</th>
                    <th>Amount</th>
                    <th>Payment Mode</th>
                    <th>Reconciliation Id</th>
                    <th>Status</th>
                    <th>Action</th>

                </tr>
                </thead>
                <c:forEach items="${runsheetAction.runsheetConsignments}" var="consignment" varStatus="ctr">
                    <tr class="consignment-row">
                        <s:hidden class = "changed-consignment-list" name="changedConsignmentList[${ctr.index}]" value="" />
                        <td><s:hidden name="runsheetConsignments[${ctr.index}]" value="${consignment.id}"/>${consignment.id}</td>
                        <td>${consignment.awbNumber}</td>
                        <td>${consignment.cnnNumber}</td>
                        <td><fmt:formatNumber value="${consignment.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="2"/></td>
                        <td>${consignment.paymentMode}</td>
                        <td>${consignment.hkdeliveryPaymentReconciliation.id}</td>
                        <td><s:hidden class="consignment-status" id= "${consignment.id}" name="runsheetConsignments[${ctr.index}].consignmentStatus"
                                      value="${consignment.consignmentStatus.id}">
                            </s:hidden>
                            <span id="current-status-${consignment.id}">${consignment.consignmentStatus.status}</span>
                        <input type="hidden" class="old-status" id="old-${consignment.id}" value="${consignment.consignmentStatus.id}" />
                        <input type="hidden" class="new-status" id="new-${consignment.id}" value="${consignment.consignmentStatus.id}" />
                        </td>
                        <td>
                             <c:forEach items="${runsheetAction.consignmentStatuses}" var="consignmentStatus" varStatus="statusCtr">
                                 <s:link href="#" class="linkbutton consignment-status-button" id= "status-${consignment.id}" name="${consignmentStatus.id}">
                                    ${consignmentStatus .status}
                                     <c:if test="${statusCtr.index == 3}" >
                                         <br />
                                     </c:if>
                                 </s:link>&nbsp;&nbsp;
                             </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <c:if test="${runsheetAction.runsheet.runsheetStatus.id !=  20}" >
                <s:submit id="save-runsheet" name="saveRunsheet" value="Save runsheet" />
                <s:submit class="closeConfirmationDialogue" name="closeRunsheet" value="Close runsheet" />
                <s:submit class="markAllConfirmationDialogue" name="markAllDelivered" value="Mark all as delivered"/>
            </c:if>
        </s:form>
    </s:layout-component>
</s:layout-render>