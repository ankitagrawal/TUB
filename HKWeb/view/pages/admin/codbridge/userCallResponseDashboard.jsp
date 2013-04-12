<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.hkbridge.UserCallResponseSummaryAction" var="usercallcod"
                 event="pre"/>


<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Summary By Knowlarity And Effort Bpo">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.lightbox-0.5.js"></script>
        <style type="text/css">
            .width {
                width: 600px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#submitbutton').click(function () {
                    var endDate = $('#startDateId').val();
                    var startDate = $('#endDateId').val();
                    if (endDate == null || endDate == '' || endDate == 'yyyy-mm-dd hh:mm:ss' || startDate == null || startDate == '' || startDate == 'yyyy-mm-dd hh:mm:ss') {
                        alert('enter start and end date');
                        return false;
                    }
                    $('#submitbutton').hide();

                })

            });


        </script>

    </s:layout-component>

    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.hkbridge.UserCallResponseSummaryAction">
            <fieldset class="right_label">
                <legend>Order Call Summary</legend>
                <ul>

                    <li>
                        <label>Start Date </label> <span class="aster">*</span>
                        <s:text class="date_input startDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"
                                id="startDateId"/>
                    </li>
                    <li>
                        <label>End Date </label> <span class="aster">*</span>
                        <s:text class="date_input endDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"
                                id="endDateId"/>
                    </li>
                </ul>
            </fieldset>
            <s:submit name="getUserCallResponseSummary" id="submitbutton" value="Search"/>
        </s:form>

        <c:if test="${usercallcod.totalCODCount != 0}">
            <div>
                <fieldset>
                    <legend> Knowlarity COD Summary</legend>
                    <ol>
                        <li>
                            <label> Total COD Order Sent</label>
                                ${usercallcod.totalCODCount}
                        </li>


                        <li>
                            <label> COD Confirmed By Knowlarity</label>
                                ${usercallcod.totalKnowlarityCODConfirmedCount}

                        </li>

                        <li>
                            <label>COD Cancelled By Knowlarity</label>
                                ${usercallcod.totalKnowlarityCODCancelledCount}

                        </li>

                        <li>
                            <label> Total Order Missed By Knowlarity</label>
                            <c:set var="missedByKnowlarity"
                                   value="${usercallcod.totalCODCount  -  (usercallcod.totalKnowlarityCODConfirmedCount + usercallcod.totalKnowlarityCODCancelledCount) }"/>
                                ${missedByKnowlarity}
                        </li>
                    </ol>
                </fieldset>

            </div>

            <div class="clear">
            </div>
            <br/>

            <div class="width">
                <fieldset>
                    <legend> Effort BPO COD Summary</legend>
                    <ol>
                        <li>
                            <label> Total COD Order Sent</label>
                                ${usercallcod.totalEfforBpoCODCount}

                        </li>

                        <li>
                            <label> COD confirmed By Effort Bpo</label>
                                ${usercallcod.totalEfforBpoCODConfirmedCount}
                        </li>

                        <li>
                            <label> COD Cancelled By Effort Bpo</label>
                                ${usercallcod.totalEfforBpoCODCancelledCount}
                        </li>


                    </ol>
                </fieldset>
            </div>

            <div class="clear">
            </div>

            <div class="width">
                <fieldset>
                    <legend> Effort BPO Payment Failure Summary</legend>
                    <ol>
                        <li>
                            <label> Payment Failure Case Handled By Effort Bpo</label>
                                ${usercallcod.totalEfforBpoPaymentFailureCount}

                        </li>
                    </ol>
                </fieldset>
            </div>
        </c:if>
    </s:layout-component>


</s:layout-render>