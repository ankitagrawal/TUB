<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.reversePickup.EnumReverseAction" %>
<%@ page import="com.hk.constants.reversePickup.EnumReverseActionOnStatus" %>
<%@ page import="com.hk.constants.reversePickup.EnumCourierConstant" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.analytics.Reason" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.courier.Courier" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.reversePickup.EnumReversePickupType" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.reversePickup.ReversePickupAction" var="rev"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reverse Pickup Booking Screen">


<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

    <script type="text/javascript">
        $(document).ready(function () {

            var courierName = $('.selected-courier-name').val();
            if (courierName == '-1') {
                $('.other-courier').css("display", "inline");
            }

            $('.selected-courier-name').change(function () {
                var courierName = $(this).val();
                if (courierName == '-1') {
                    $('.other-courier').css("display", "inline");
                }
                else {
                    $('.courier-name').val(courierName);
                    $('.other-courier').css("display", "none");
                }
            });

            $('.courier-text').change(function () {
                var courierName = $(this).val();
                $('.courier-name').val(courierName);
            });
        });

    </script>

</s:layout-component>

<s:layout-component name="content">
<%
    MasterDataDao masterDataDao = ServiceLocatorFactory.getBean(MasterDataDao.class);
    List<Reason> reasonList = masterDataDao.getCustomerReasonForReversePickup();
    List<Courier> reversePickupCourierList = masterDataDao.getCouriersForReversePickup();
    pageContext.setAttribute("reversePickupCourierList", reversePickupCourierList);
    pageContext.setAttribute("customerReasonList", reasonList);

%>

<style type="text/css">
    .rline-items {
        border-collapse: collapse;
        margin: 1em auto;
    }

    .rline-items td {
        border-right: 1px solid #002737;
    }

    .rline-items td:last-child {
        border-right: none;
    }

    .btm-dshed {
        border-bottom: 1px dashed #C7CACC;
    }

    .btm-solid {
        border-bottom: 1px solid #002737;
    }

    td:last-child {
        border-right: none;
    }

    .heading p {
        text-align: center;
        font-size: 20px;
        color: #3379BB;
        font-weight: bolder;
        line-height: 18px;
    }

    .font label {
        font-size: 18px;
        color: #000000;
    }

    .courier-detail {
        margin: 4em 5em;
    }

    .saved td {
        background-color: #add8e6;
    }

    .disablrow td {
        background-color: #FFFAB2;
    }

</style>

<div style="height: 50px;">
    <c:choose>
        <c:when test="${rev.reversePickupOrder != null}">
            <div class="heading">
                <p>Edit Reverse Pickup No. ${rev.reversePickupOrder.reversePickupId}</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="heading">
                <p>Reverse Pickup Booking Screen</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>


<div>
<s:form beanclass="com.hk.web.action.admin.reversePickup.ReversePickupAction" class="mainform">
<s:hidden name="shippingOrder" value="${rev.shippingOrder.id}"/>
<s:hidden name="reversePickupOrder" value="${rev.reversePickupOrder.id}"/>
<table class="rline-items">
    <thead>
    <tr>
        <th>Unit No.</th>
        <th style="width: 9%;">Variant Id</th>
        <th>Product Name</th>
        <th>Check For Return</th>
        <th>Reason For Return</th>
        <th>CS Action</th>
        <%--<th>Action On Status</th>--%>
        <th>CS Action Status</th>
        <th>Customer Care Comment</th>
    </tr>

    </thead>
    <c:set var="index" value="0" scope="page"/>
    <c:forEach items="${rev.shippingOrder.lineItems}" var="lineItem" varStatus="lineCount">

        <c:set value="${lineCount.index + 1}" var="serialNo"/>
        <c:set value="1" var="unitNo"/>
        <c:set value="${lineItem.qty}" var="lineItemQty"/>
        <c:set value="0" var="savedLength"/>

        <!-- Loop for LineItem Already saved in this RP order !-->
        <c:set value="${rev.rpLineItemsSavedMap[lineItem.id]}" var="rpLineSavedList"/>
        <c:if test="${rpLineSavedList != null}">
            <c:set value="${fn:length(rpLineSavedList)}" var="savedLength"/>
            <c:forEach items="${rpLineSavedList}" var="savedRpLineItem" varStatus="ctr">
                <tr class="${ ctr.last && savedLength == lineItemQty ? 'btm-solid' : 'btm-dshed' } saved  "
                    id="selected">
                    <td>${unitNo}</td>
                    <td>${lineItem.sku.productVariant.id}</td>
                    <td>${savedRpLineItem.lineItem.sku.productVariant.product.name}  ${savedRpLineItem.lineItem.sku.productVariant}
                            ${savedRpLineItem.lineItem.sku.productVariant.optionsPipeSeparated}
                        <input type="hidden" name="rpLineItems[${index}].lineItem" value="${lineItem.id}">
                    </td>
                    <td><input type="checkbox" name="checkBoxIndexList[${index}]" value="${index}" checked="checked"
                               class="checkedbox"/>
                    </td>
                    <td>
                        <s:select name="rpLineItems[${index}].customerReasonForReturn"
                                  value="${savedRpLineItem.customerReasonForReturn.id}" class="reason-entered-select">
                            <s:option value="">--Select-- </s:option>
                            <c:forEach items="${customerReasonList}" var="reason">
                                <s:option value="${reason.id}">${reason.classification.primary}</s:option>
                            </c:forEach>
                        </s:select>
                    </td>
                    <td>
                        <s:select name="rpLineItems[${index}].actionTaken"
                                  value="${savedRpLineItem.actionTaken}">
                            <s:option value="">--Select-- </s:option>
                            <c:forEach items="<%=EnumReverseAction.getAllReversePickAction()%>"
                                       var="actionTaken">
                                <s:option value="${actionTaken.id}">${actionTaken.name}</s:option>
                            </c:forEach>
                        </s:select>
                    </td>
               <%--     <td>
                        <s:select name="rpLineItems[${index}].actionTakenOnStatus"
                                  value="${savedRpLineItem.actionTakenOnStatus}">
                            <s:option value="">--Select -- </s:option>
                            <c:forEach
                                    items="<%=EnumReverseActionOnStatus.getAllReverseActionOnStatus()%>"
                                    var="actionTakenOnStatus">
                                <s:option
                                        value="${actionTakenOnStatus.id}">${actionTakenOnStatus.name}</s:option>
                            </c:forEach>
                        </s:select>
                    </td>--%>
                    <td>
                       ${savedRpLineItem.customerActionStatus.primary}
                        <s:hidden name="rpLineItems[${index}].customerActionStatus"
                                  value="${savedRpLineItem.customerActionStatus}"/>

                    </td>
                    <td>
                        <s:textarea style="width: 300px; height: 60px;"
                                    name="rpLineItems[${index}].customerComment"
                                    value="${savedRpLineItem.customerComment}" class="reason-entered-area"/>
                    </td>
                </tr>
                <c:set var="index" value="${index+1}" scope="page"/>
                <c:set var="unitNo" value="${unitNo+1}"/>
            </c:forEach>
        </c:if>


        <!-- Loop for LineItems Saved in Another RP order of same SO , they should be displayed readOnly !-->
        <c:set value="0" var="disabledLength"/>
        <c:set value="${rev.rpLineDisabledMap[lineItem.id]}" var="rpLineDisabledList"/>
        <c:if test="${rpLineDisabledList != null}">
            <c:set value="${fn:length(rpLineDisabledList)}" var="disabledLength"/>
            <c:forEach items="${rpLineDisabledList}" var="disabledRpLineItem" varStatus="ctr">
                <tr class="${ ctr.last && (savedLength + disabledLength)== lineItemQty ? 'btm-solid' : 'btm-dshed' } disablrow "
                    id="disable">
                    <td>${unitNo}</td>
                    <td>${lineItem.sku.productVariant.id}</td>
                    <td>${disabledRpLineItem.lineItem.sku.productVariant.product.name}    ${disabledRpLineItem.lineItem.sku.productVariant}
                              ${disabledRpLineItem.lineItem.sku.productVariant.optionsPipeSeparated}
                    </td>
                    <td><input type="checkbox" name="checkBoxIndexList[${index}]" value="${index}" disabled="disabled"
                               checked="checked"/></td>
                    <td>
                            ${disabledRpLineItem.customerReasonForReturn.classification.primary}
                    </td>
                    <td>
                            ${disabledRpLineItem.actionTaken.primary}
                    </td>
                    <%--<td>
                        <c:forEach
                                items="<%=EnumReverseActionOnStatus.getAllReverseActionOnStatus()%>"
                                var="actionTakenOnStatus">
                            <c:if test="${disabledRpLineItem.actionTakenOnStatus == actionTakenOnStatus.id}">
                                ${actionTakenOnStatus.name}
                            </c:if>
                        </c:forEach>
                    </td>--%>
                    <td>
                         ${disabledRpLineItem.customerActionStatus.primary}
                    </td>
                    <td> ${disabledRpLineItem.customerComment} </td>
                </tr>
                <c:set var="unitNo" value="${unitNo+1}"/>
            </c:forEach>
        </c:if>


        <!-- Loop for Line Items Of So which are not Reverse Picked !-->
        <c:set value="${lineItemQty - (savedLength + disabledLength)}" var="unpickedQty"/>
        <c:forEach begin="1" end="${unpickedQty}" varStatus="ctr">
            <tr class="${ctr.last? 'btm-solid':'btm-dshed'} check " id="unpicked ">
                <td>${unitNo}</td>
                <td>${lineItem.sku.productVariant.id}</td>
                <td>${lineItem.sku.productVariant.product.name}  ${lineItem.sku.productVariant}
                        ${lineItem.sku.productVariant.optionsPipeSeparated}
                    <input type="hidden" name="rpLineItems[${index}].lineItem" value="${lineItem.id}">
                </td>
                <td><input type="checkbox" name="checkBoxIndexList[${index}]" value="${index}" class="checkedbox"/></td>
                <td><s:select name="rpLineItems[${index}].customerReasonForReturn" class="reason-entered-select">
                    <s:option value="">--Select-- </s:option>
                    <c:forEach items="${customerReasonList}" var="reason">
                        <s:option value="${reason.id}">${reason.classification.primary}</s:option>
                    </c:forEach>
                </s:select>
                </td>
                <td>
                    <s:select name="rpLineItems[${index}].actionTaken">
                        <s:option value="">--Select-- </s:option>
                        <c:forEach items="<%=EnumReverseAction.getAllReversePickAction()%>"
                                   var="actionTaken">
                            <s:option value="${actionTaken.id}">${actionTaken.name}</s:option>
                        </c:forEach>
                    </s:select>
                </td>
               <%-- <td>
                    <s:select name="rpLineItems[${index}].actionTakenOnStatus">
                        <s:option value="">--Select-- </s:option>
                        <c:forEach items="<%=EnumReverseActionOnStatus.getAllReverseActionOnStatus()%>"
                                   var="actionTakenOnStatus">
                            <s:option
                                    value="${actionTakenOnStatus.id}">${actionTakenOnStatus.name}</s:option>
                        </c:forEach>
                    </s:select>
                </td>--%>
                <td>

                </td>
                <td>
                    <s:textarea style="width: 300px; height: 60px;"
                                name="rpLineItems[${index}].customerComment" class="reason-entered-area"/>
                </td>
            </tr>
            <c:set var="index" value="${index+1}" scope="page"/>
            <c:set var="unitNo" value="${unitNo+1}"/>
        </c:forEach>

    </c:forEach>
</table>

<div class="clear" style="margin: 1em auto"></div>

<div class="font courier-detail">
    <div style="float: left">
        <label> Booking Type </label>
        <s:select name="reversePickupOrder.reversePickupType" value="${rev.reversePickupOrder.reversePickupType.name}">
            <s:option value="">--Select-- </s:option>
            <c:forEach items="<%=EnumReversePickupType.getAllRPTypeList()%>" var="reversePickupType">
                <s:option value="${reversePickupType.id}">${reversePickupType.name}</s:option>
            </c:forEach>
        </s:select>
    </div>
</div>

<shiro:hasPermission name="<%=PermissionConstants.SCHEDULE_REVERSE_PICKUP%>">

<div class="clear" style="margin: 1em auto"></div>

<div class="font courier-detail">
    <div style="float: left">

        <c:set value="<%=EnumCourierConstant.HealthKart_Managed.getId()%>" var="hkManaged"/>
        <input type="radio" name="reversePickupOrder.courierManagedBy"
               value="${hkManaged}" ${rev.reversePickupOrder.courierManagedBy == hkManaged ? 'checked':''}><label>HK
        Managed Courier</label>
        <br><br>
        <c:set value="<%=EnumCourierConstant.Customer_Managed.getId()%>" var="customerManaged"/>
        <input type="radio" name="reversePickupOrder.courierManagedBy"
               value="${customerManaged}"
               class="" ${rev.reversePickupOrder.courierManagedBy == customerManaged ? 'checked':''} ><label>Customer
        Managed Courier</label>
        <input type="hidden" name="${rev.reversePickupOrder.amount}" value=""/>
    </div>
    <div style="float: right;">
        <div style="margin-bottom: 1em;">
            <label>Courier Name</label>
            <c:set value="false" var="showCourierInDropDown"/>

            <select class="selected-courier-name">
                <c:if test="${rev.reversePickupOrder.courierName == null}">
                    <option value="">--Select Courier--</option>
                    <c:set value="true" var="showCourierInDropDown"/>
                </c:if>
                <!-- Either courier is selected on drop down or shown in text box !-->
                <c:forEach items="${reversePickupCourierList}" var="courier">
                    <c:choose>
                        <c:when test="${rev.reversePickupOrder.courierName == courier.name}">
                            <option value="${courier.name}" selected="selected">${courier.name}</option>
                            <c:set value="true" var="showCourierInDropDown"/>
                        </c:when>
                        <c:otherwise>
                            <option value="${courier.name}">${courier.name}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:choose>
                    <c:when test="${showCourierInDropDown}">
                        <option value="-1">others</option>
                    </c:when>
                    <c:otherwise>
                        <option value="-1" selected="selected">others</option>
                    </c:otherwise>
                </c:choose>
            </select>
            <s:hidden name="reversePickupOrder.courierName" class="courier-name"/>
        </div>
        <div style="display:none; margin-left: 150px;" class="other-courier">
            <input type="text" style="width:140px;" value="${rev.reversePickupOrder.courierName}"
                   class="courier-text"/>
        </div>

    </div>
</div>

<div class="clear" style="margin: 1em auto"></div>
<div class="font courier-detail">
    <label> Pick Up Time </label> <s:text class="date_input startDate" style="width:150px"
                                          value="${rev.reversePickupOrder.pickupTime}"
                                          formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                          name="pickupDate"/>
</div>

</shiro:hasPermission>

<div class="courier-detail">
    <c:choose>
        <c:when test="${rev.pendingApprovalEditMode}">
            <s:submit name="saveModifiedReversePickup" value="Save" class="saveButton"/>
        </c:when>
        <c:otherwise>
            <s:submit name="createReversePickUp" value="Save" class="saveButton"/>
        </c:otherwise>
    </c:choose>
    <span style="margin-left:100px;"><s:submit name="cancelReversePickUpChanges" value="Cancel"/></span>
</div>

</s:form>
</div>

</s:layout-component>


</s:layout-render>