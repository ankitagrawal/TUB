<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Runsheet List">

    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction" var="runsheetAction"/>
    <s:layout-component name="htmlHead">
    </s:layout-component>
    <s:layout-component name="heading">
        Preview Runsheet
    </s:layout-component>

    <s:layout-component name="content">
        <fieldset class="right_label">
        <legend>Runsheet Preview</legend>
        <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction">
            <table>
                <tr>
                    <td><label>Agent Name:</label></td>
                    <td> <s:hidden name="agent"/> ${runsheetAction.agent.name}</td>
                    <td></td>
                    <td><label>Total COD Boxes: </label></td>
                    <td>${runsheetAction.runsheet.codBoxCount}</td>
                    <td></td>
                    <td><label>Hub: </label></td>
                    <td><s:hidden name="hub"/> ${runsheetAction.hub.name} </td>
                </tr>
                <tr>
                    <td><label>Total Prepaid Boxes:</label></td>
                    <td> ${runsheetAction.runsheet.prepaidBoxCount}</td>
                    <td></td>
                    <td><label>Runsheet Status: </label></td>
                    <td>${runsheetAction.runsheet.runsheetStatus.status}</td>
                    <td></td>
                    <td><label>Create Date: </label></td>
                    <td><fmt:formatDate value="${runsheetAction.runsheet.createDate}" type="both" timeStyle="short"/></td>
                </tr>
            </table>

            </fieldset>
            <h3><strong>Consignments</strong></h3>
            <table class="zebra_vert">
                <thead>
                <tr>
                    <th>SL No.</th>
                    <th>AWB Number</th>
                    <th>CNN Number</th>
                    <th>Amount</th>
                    <th>Payment Mode</th>
                    <th>Address</th>
                    <th>Transfer to Agent</th>
                    <th>Action</th>
                </tr>
                </thead>
                <c:forEach items="${runsheetAction.consignmentDtoList}" var="consignmentDto" varStatus="ctr">
                    <tr id="consignmentRow${ctr.count}" class="consignment-row" >
                        <td>${ctr.count}</td>
                        <td>${consignmentDto.awbNumber}<s:hidden name="consignmentDtoList[${ctr.index}].awbNumber"/></td>
                        <td>${consignmentDto.cnnNumber}<s:hidden name="consignmentDtoList[${ctr.index}].cnnNumber"/></td>
                        <td><fmt:formatNumber value="${consignmentDto.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="0"/><s:hidden name="consignmentDtoList[${ctr.index}].amount"/></td>
                        <td>
                           ${consignmentDto.paymentMode}<s:hidden name="consignmentDtoList[${ctr.index}].paymentMode"/>
                        </td>
                        <td>${consignmentDto.address}<s:hidden name="consignmentDtoList[${ctr.index}].address"/></td>
                        <td><s:select name="consignmentDtoList[${ctr.index}].transferredToAgent.id" class="agentName">
                                <s:option value="${runsheetAction.agent.id}">-Select Agent-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="agentsWithOpenRunsheet" value="id"
                                                           label="name"/>
                            </s:select>
                        </td>
                        <td>
                            <a href="hkDeliveryWorksheet.jsp#" class="removeRowButton" id="removeRowButton"
                               style="font-size:1.2em;color:blue;">Remove Consignment</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <s:submit name="downloadDeliveryWorkSheet" id="runsheetDownloadButton" value="Download Runsheet" class="runsheetDownloadButton"></s:submit>
            <s:submit name="pre" id="downloadRunsheetAgainButton" value="Download Runsheet Again" class="downloadRunsheetAgainButton"></s:submit>
        </s:form>
    </s:layout-component>
</s:layout-render>
<script type="text/javascript">

    $(document).ready(function () {
       $('#downloadRunsheetAgainButton').hide();

       $('.removeRowButton').click(function(event){
          event.preventDefault();
           var row = $(this).closest('.consignment-row');
           row.slideUp();
           row.remove();
       });
   });


    $('.runsheetDownloadButton').click(function() {
    $('#runsheetDownloadButton').hide();
    $('#downloadRunsheetAgainButton').show();


  });
  </script>