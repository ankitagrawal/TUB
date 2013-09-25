<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.reversePickup.EnumReverseAction" %>
<%@ page import="com.hk.constants.reversePickup.EnumReverseActionOnStatus" %>
<%@ page import="com.hk.constants.reversePickup.EnumCourierConstant" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.analytics.Reason" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.courier.Courier" %>
<%@ page import="com.hk.constants.reversePickup.EnumReversePickupStatus" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.reversePickup.ReversePickupAction" var="rev"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reverse Pickup Booking Screen">
<s:layout-component name="content">
<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
        $(document).ready(function () {

            $('.save-link').live("click", function () {
                var curEle = $(this);
                var queryString = '';
                var sep = '';
                $(this).parents('tr').find('input,select,textarea').each(function () {
                    queryString = queryString + sep + $(this).attr('name') + '=' + escape($(this).attr('value'));
                    sep = '&'
                });
                curEle.css("display", "none");
                var href = $('.mainform').attr('action');
                $.ajax({
                    url:href + '?changeCustomerActionAndStatus=',
                    type:'post',
                    data:queryString,
                    dataType:'json',
                    success:function (res) {
                        var listUrl = "${pageContext.request.contextPath}/admin/reversePickup/ReversePickupList.action" ;
                        var rppickupId = $('.rp-pickup-id').val();
                        location.href = listUrl + '?pre=&reversePickupId=' + rppickupId;
                    },
                    error:function onError() {
                        curEle.css("display", "inline");
                    }
                });
            });


            var courierName = $('.selected-courier-name').val();
            if (courierName == '-1') {
                $('.other-courier').css("display", "block");
            }

            $('.selected-courier-name').change(function () {
                var courierName = $(this).val();
                if (courierName == '-1') {
                    $('.other-courier').css("display", "block");
                }
                else {
                    $('.courier-name').val(courierName);
                    $('.other-courier').css("display", "none");
                }
            });

            $('.courier-text').change(function () {
                var courierName = $(this).val();
                $('.courier-name').val(courierName);
            })

        });

    </script>

</s:layout-component>

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
        background-color: #ffffe0;
    }

    .disablrow td {
        background-color: #FFFAB2;
    }

    .link {
        font-size: 15px;
        font-weight: bolder;
    }

</style>
<div style="height: 50px;" class="heading">
    <p>Edit Reverse Pickup No. ${rev.reversePickupOrder.reversePickupId}</p>
</div>
<div style="text-align: left; font-size: 13px;color: green;" class="msg">

</div>

<div>
    <s:form beanclass="com.hk.web.action.admin.reversePickup.ReversePickupAction" class="mainform">
        <s:hidden name="reversePickupOrder" value="${rev.reversePickupOrder.id}" class="rp-value"/>
        <s:hidden name="reversePickupId" value="${rev.reversePickupOrder.reversePickupId}" class="rp-pickup-id"/>
        <table class="rline-items">
            <thead>
            <tr>
                <th>S.No.</th>
                <th>Unit No.</th>
                <th>Product Name</th>
                <th>Reason For Return</th>
                <th>CS Action</th>
                <%--<th>Action On Status</th>--%>
                <th>CS Action Status</th>
                <th>Customer Care Comment</th>
                <th style="padding:10px">Action</th>
            </tr>

            </thead>
            <c:set var="index" value="0" scope="page"/>
            <c:set value="<%=EnumReversePickupStatus.RPU_Picked.getId()%>" var="rpuPickedId"/>
            <c:set value="<%=EnumReverseAction.Approved.getId()%>" var="approvedId"/>
            <c:set value="<%=EnumReverseAction.Pending_Approval.getId()%>" var="pendingId"/>
            <c:set value="${rev.reversePickupOrder.reversePickupStatus.id}" var="rpStatus"/>
            <c:forEach items="${rev.shippingOrder.lineItems}" var="lineItem" varStatus="lineCount">
                <c:set value="0" var="savedLength"/>
                <c:set value="${rev.rpLineItemsSavedMap[lineItem.id]}" var="rpLineSavedList"/>
                <c:if test="${rpLineSavedList != null}">
                    <c:set value="${fn:length(rpLineSavedList)}" var="savedLength"/>
                    <!-- Loop for RPLineItem !-->
                    <c:forEach items="${rpLineSavedList}" var="savedRpLineItem" varStatus="ctr">
                        <tr class="${ ctr.last ? 'btm-solid' : 'btm-dshed' } saved  ">
                            <input type="hidden" name="rpLineItems[${index}]" value="${savedRpLineItem.id}"/>
                            <input type="hidden" name="reversePickupOrder"
                                   value="${savedRpLineItem.reversePickupOrder.id}"/>
                            <c:if test="${ctr.first}">
                                <td rowspan="${savedLength}">${ctr.index+1}</td>
                            </c:if>
                            <td>${ctr.index+1}</td>
                            <td>${savedRpLineItem.lineItem.sku.productVariant.product.name} ${savedRpLineItem.lineItem.sku.productVariant}
                                    ${savedRpLineItem.lineItem.sku.productVariant.optionsPipeSeparated}</td>
                            <td>
                                <s:select name="rpLineItems[${index}].customerReasonForReturn"
                                          value="${savedRpLineItem.customerReasonForReturn.id}"
                                          class="reason-entered-select"
                                          disabled="${savedRpLineItem.customerActionStatus.id == approvedId? 'disabled' : ''}">
                                    <s:option value="">--Select-- </s:option>
                                    <c:forEach items="${customerReasonList}" var="reason">
                                        <s:option value="${reason.id}">${reason.classification.primary}</s:option>
                                    </c:forEach>
                                </s:select>
                            </td>
                            <td>
                                <s:select name="rpLineItems[${index}].actionTaken"
                                          value="${savedRpLineItem.actionTaken.id}"
                                          disabled="${savedRpLineItem.customerActionStatus.id == approvedId? 'disabled' : ''}">
                                    <s:option value="">--Select-- </s:option>
                                    <c:forEach items="<%=EnumReverseAction.getAllReversePickAction()%>"
                                               var="actionTaken">
                                        <s:option value="${actionTaken.id}">${actionTaken.name}</s:option>
                                    </c:forEach>
                                </s:select>
                            </td>
                            <%--<td>--%>
                                <%--<s:select name="rpLineItems[${index}].actionTakenOnStatus"--%>
                                          <%--value="${savedRpLineItem.actionTakenOnStatus}"--%>
                                          <%--disabled="${savedRpLineItem.customerActionStatus == approvedId? 'disabled' : ''}">--%>
                                    <%--<s:option value="">--Select -- </s:option>--%>
                                    <%--<c:forEach--%>
                                            <%--items="<%=EnumReverseActionOnStatus.getAllReverseActionOnStatus()%>"--%>
                                            <%--var="actionTakenOnStatus">--%>
                                        <%--<s:option--%>
                                                <%--value="${actionTakenOnStatus.id}">${actionTakenOnStatus.name}</s:option>--%>
                                    <%--</c:forEach>--%>
                                <%--</s:select>--%>
                            <%--</td>--%>
                            <td>
                                ${savedRpLineItem.customerActionStatus.primary}
                            </td>
                            <td>
                                <s:textarea style="width: 300px; height: 60px;"
                                            name="rpLineItems[${index}].customerComment"
                                            value="${savedRpLineItem.customerComment}" class="reason-entered-area"
                                            disabled="${savedRpLineItem.customerActionStatus.id == approvedId? 'disabled' : ''}"/>
                            </td>
                            <td>
                                <c:if test="${savedRpLineItem.customerActionStatus == null || savedRpLineItem.customerActionStatus.id == pendingId}">
                                    <a href="javascript:void(0)" class="save-link" style="color: green;" >
                                        <span class="link">(Save)</span> <br><br><br>
                                    </a>
                                </c:if>
                                <c:if test="${savedRpLineItem.customerActionStatus == null || savedRpLineItem.customerActionStatus.id == pendingId}">
                                    <s:link beanclass="com.hk.web.action.admin.reversePickup.ReversePickupAction"
                                            style="color:red;"
                                            event="deleteRpLineItem"> <span class="link">(Delete)</span>
                                        <s:param name="reversePickupOrder" value="${rev.reversePickupOrder.id}"/>
                                        <s:param name="rpLineItem" value="${savedRpLineItem.id}"/>
                                    </s:link> <br><br>
                                </c:if>
                            </td>
                        </tr>
                        <c:set var="index" value="${index+1}"/>
                    </c:forEach>
                </c:if>
            </c:forEach>
        </table>


        <div class="clear" style="margin: 1em auto"></div>

        <c:set value="<%=EnumReversePickupStatus.Return_QC_Checkin.getId()%>" var="returnCheckId"/>
        <c:set value="<%=EnumCourierConstant.HealthKart_Managed.getId()%>" var="hkartCourierId"/>
        <c:set value="<%=EnumCourierConstant.Customer_Managed.getId()%>" var="custCourierId"/>

        <c:set value="${rev.reversePickupOrder.courierManagedBy}" var="rpCourier"/>

        <c:if test="${rpCourier == null || (( rpCourier == hkartCourierId &&  rpStatus < rpuPickedId) || (rpCourier == custCourierId && rpStatus < returnCheckId))}">

            <%-- Edit Courier Details until courier is marked packed or recieved --%>
            <div>
                <div class="font courier-detail">
                    <div style="float: left">
                        <c:set value="<%=EnumCourierConstant.HealthKart_Managed.getId()%>" var="hkManaged"/>
                        <input type="radio" name="reversePickupOrder.courierManagedBy"
                               value="${hkManaged}"  ${rev.reversePickupOrder.courierManagedBy == hkManaged ? 'checked':'' }><label>HK
                        Managed Courier</label>
                        <br><br>
                        <c:set value="<%=EnumCourierConstant.Customer_Managed.getId()%>" var="customerManaged"/>
                        <input type="radio" name="reversePickupOrder.courierManagedBy"
                               value="${customerManaged}"
                               class="" ${rev.reversePickupOrder.courierManagedBy == customerManaged ? 'checked':''} ><label>Customer
                        Managed Courier</label>
                    </div>
                    <div style="float: right;">
                        <div>
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

                <div class="courier-detail">
                    <s:submit name="saveApprovedReversePickup" value="Save" class="saveButton"/>
                    <span style="margin-left:100px;"><s:submit name="cancelReversePickUpChanges" value="Cancel"/></span>
                </div>
            </div>
        </c:if>
    </s:form>

</div>

</s:layout-component>


</s:layout-render>