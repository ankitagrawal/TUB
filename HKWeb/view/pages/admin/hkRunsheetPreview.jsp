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
                </tr>
                </thead>
                <c:forEach items="${runsheetAction.runsheetConsignments}" var="consignment" varStatus="ctr">
                    <s:hidden name="runsheetConsignments[${ctr.index}]" value="${consignment.id}"/>
                    <tr>
                        <td>${ctr.count}</td>
                        <td>${consignment.awbNumber}</td>
                        <td>${consignment.cnnNumber}</td>
                        <td><fmt:formatNumber value="${consignment.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="0"/></td>
                        <td>
                            <%--<c:choose>
                            <c:when test="${consignment.paymentMode eq 'COD'}">
                                COD
                            </c:when>
                                <c:otherwise>
                                Prepaid
                                </c:otherwise>
                            </c:choose>--%>
                           ${consignment.paymentMode}
                        </td>
                        <td>${consignment.address}</td>
                        <td><s:select name="agent" class="agentName">
                                <s:option value="-Select Agent-">-Select Agent-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="agentsWithOpenRunsheet" value="id"
                                                           label="name"/>
                            </s:select>
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
   });

    $('.runsheetDownloadButton').click(function() {
    $('#runsheetDownloadButton').hide();
    $('#downloadRunsheetAgainButton').show();


  });
  </script>