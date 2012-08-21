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
                    <tr>
                        <td><s:hidden name="runsheetConsignments[${ctr.index}]" value="${consignment.id}"/>${consignment.id}</td>
                        <td>${consignment.awbNumber}</td>
                        <td>${consignment.cnnNumber}</td>
                        <td><fmt:formatNumber value="${consignment.amount}" type="currency" currencySymbol=" "
                                              maxFractionDigits="0"/></td>
                        <td>${consignment.paymentMode}</td>
                        <td>${consignment.paymentReconciliation.id}</td>
                        <td><s:select name="runsheetConsignments[${ctr.index}].consignmentStatus"
                                      value="${consignment.consignmentStatus.id}">
                            <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                       serviceProperty="consignmentStatusList" value="id"
                                                       label="status"/>
                        </s:select></td>
                        <td>

                        </td>
                    </tr>
                </c:forEach>
            </table>

            <s:submit name="saveRunsheet" value="Save runsheet" />
            <s:submit name="closeRunsheet" value="Close runsheet" />
            <s:submit name="markAllDelivered" value="Mark all as delivered"/>
        </s:form>
    </s:layout-component>
</s:layout-render>