<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.service.OrderStatusService" %>
<%@ page import="com.hk.pact.service.payment.PaymentService" %>
<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.pact.service.store.StoreService" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.EnumUserCodCalling" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction" var="actionQueueBean" event="pre"/>

<c:set var="orderStatusHold" value="<%=EnumOrderStatus.OnHold.getId()%>"/>
<c:set var="orderStatusPlaced" value="<%=EnumOrderStatus.Placed.getId()%>"/>

<c:set var="paymentStatusPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>
<c:set var="paymentStatusSuccess" value="<%=EnumPaymentStatus.SUCCESS.getId()%>"/>
<c:set var="paymentStatusOnDelivery" value="<%=EnumPaymentStatus.ON_DELIVERY.getId()%>"/>

<c:set var="paymentModeCod" value="<%=EnumPaymentMode.COD.getId()%>"/>
<c:set var="commentTypeOthers" value="<%=MasterDataDao.USER_COMMENT_TYPE_OTHERS_BASE_ORDER%>" />

<c:set var="thirdPartyCodCallFailed" value="<%=EnumUserCodCalling.THIRD_PARTY_FAILED.getId()%>"/>
<c:set var="paymentFailed" value="<%=EnumUserCodCalling.PAYMENT_FAILED.getId()%>"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Action Awaiting Queue">
<s:layout-component name="htmlHead">
    <%
        PaymentService paymentService =  ServiceLocatorFactory.getService(PaymentService.class);
        OrderStatusService orderStatusService = ServiceLocatorFactory.getService(OrderStatusService.class);
        ShippingOrderStatusService shippingOrderStatusService = ServiceLocatorFactory.getService(ShippingOrderStatusService.class);
        ShippingOrderLifecycleService shippingOrderLifecycleService = ServiceLocatorFactory.getService(ShippingOrderLifecycleService.class);
        StoreService storeService = ServiceLocatorFactory.getService(StoreService.class);
        pageContext.setAttribute("paymentModeList", paymentService.listWorkingPaymentModes());
        pageContext.setAttribute("paymentStatusList", paymentService.listWorkingPaymentStatuses());
        pageContext.setAttribute("orderStatusList", orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForActionQueue()));
        pageContext.setAttribute("shippingOrderStatusList", shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForActionQueue()));
        pageContext.setAttribute("shippingOrderLifecycleList", shippingOrderLifecycleService.getOrderActivities(EnumShippingOrderLifecycleActivity.getActivitiesForActionQueue()));
        pageContext.setAttribute("storeList", storeService.getAllStores());

        CategoryDao categoryDao = (CategoryDao)ServiceLocatorFactory.getService(CategoryDao.class);
        pageContext.setAttribute("categoryList", categoryDao.getPrimaryCategories());
    %>

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/jquery.effects.core.js"></script>
    <script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/jquery.effects.slide.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <style type="text/css">
        .fieldLabel {
            font-size: .8em;
            color: #999999;
            padding-top: 2px;
        }

        table.align_top tr td {
            vertical-align: top;

        }

        .headingLabel {
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .detailDiv {
            margin-bottom: 5px;
            border: 1px dotted;
            overflow: hidden;
            padding: 3px;
        }

        table.row_border tr {
            outline: 1px solid #cccccc;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function() {

            $('.flipWarehouse').click(function() {
                var proceed = confirm('Are you sure you want to flip warehouse?');
                if (!proceed) return false;

                var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        alert(res.message);
                        window.location.reload();
                    }
                });

                return false;
            });

            $('.manualEscalate').click(function() {
                var proceed = confirm('This link will escalate SO only if order can be processed');
                if (!proceed) return false;

                var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        alert(res.message);
                        window.location.reload();
                    }
                });

                return false;
            });

            $('.delieverSO').click(function() {
                var proceed = confirm('You should mark orders delieverd only if they have only drop ship products. Continue? ');
                if (!proceed) return false;

                var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        alert(res.message);
                        window.location.reload();
                    }
                });

                return false;
            });

        <%--
        Confirm cod order
        --%>
            $('.confirmCodLink').click(function() {
                var proceed = confirm('Are you sure?');
                if (!proceed) return false;

                var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        clickedLink.parents('td').find('.paymentStatusName').html(res.data.paymentStatus.name);
                        clickedLink.parents('td').find('.codOrderText').hide();
                        clickedLink.hide();
                        if (res.data.orderStatus.id != '<%=EnumOrderStatus.OnHold.getId()%>') {
                            clickedLink.parents('.orderRow').removeClass('highlight_cod_bgcolor').find('.lineItemCheckBox').removeAttr('disabled');
                        }
                    }
                });

                return false;
            });

            $('.splitBOLink').click(function() {

                var clickedLink = $(this);
                $.getJSON(clickedLink.attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        alert(res.message);
                        window.location.reload();
                    }
                });

                return false;
            });

        <%--
        Order status hold/unhold toggle functionality
        --%>
            $('.orderStatusLink').click(function() {
                var proceed = confirm('Are you sure?');
                if (!proceed) return false;

                var clickedLink = $(this);
                var clickedP = clickedLink.parents('p');
                clickedP.find('.orderStatusName').html($('#ajaxLoader').html());

                $.getJSON($(this).attr('href'), function(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        window.location.reload();
                    }
                });

                return false;
            });

            $('#liveSearchBox').keyup(function() {
                var searchString = $(this).val().toLowerCase();
                $('.orderRow').each(function() {
                    if ($(this).find('.basketCategory').text().toLowerCase().indexOf(searchString) == -1) {
                        $(this).hide();
                    } else {
                        $(this).show();
                    }
                });
            });

            $(':checkbox').filter(function() {
                    if(this.id.match(/raj/)){
                        if($("#" + this.id).is(":checked")){
                            $("#newBoxAdmin"+this.id).show("slide", { direction: "up" }, 300);
                        }
                    }
                    });

            $("input[type='checkbox']").change(function (event) {
                if($("#newBoxAdmin"+this.id).css("display") == "block"){
                    $("#newBoxAdmin"+this.id).hide("slide", { direction: "up" }, 300);
                }
                else if($("#newBoxAdmin"+this.id).css("display") == "none"){
                    $("#newBoxAdmin"+this.id).show("slide", { direction: "up" }, 300);
                }
            });
            $("div").click(function (event) {
                var index = this.id.search("div");
                var id = this.id.substring(index+3);
                if($("#newBoxAdminraj"+id).css("display") == "block"){
                }
                else if($("#newBoxAdminraj"+id).css("display") == "none"){
                    $("#newBoxAdminraj"+id).show("slide", { direction: "up" }, 300);
                    $("#raj" + id).prop('checked', true);
                }
            });
        });

    </script>
</s:layout-component>
<s:layout-component name="heading">
    <div class="actionQueue">Action Awaiting Queue</div>
</s:layout-component>
<s:layout-component name="content">
<fieldset style="margin: 10px;" class="top_label">
    <ul style="margin-top: 0px;">
        <div class="grouped grid_12">
            <s:form beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction" method="get" autocomplete="false">
                <li><label>Order ID</label> <s:text name="orderId"/> &nbsp; <label>Gateway Order ID</label> <s:text
                        name="gatewayOrderId" id="gatewayOrderId"/>
                    <label>Start
                        date</label><s:text class="date_input startDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
                    &nbsp;
                    <label>End
                        date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>

                    <label style="color:red; font-weight:bold;font-size:15px;">Use filters judiciously</label>
                </li>
                <li>
                    <label>BO Status</label>
                    <c:forEach items="${orderStatusList}" var="orderStatus" varStatus="ctr">
                        <label><s:checkbox name="orderStatuses[${ctr.index}]"
                                           value="${orderStatus.id}"/> ${orderStatus.name}</label>
                    </c:forEach>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <label>SO Status</label>
                    <c:forEach items="${shippingOrderStatusList}" var="shippingOrderStatus" varStatus="ctr">
                        <label><s:checkbox name="shippingOrderStatuses[${ctr.index}]"
                                           value="${shippingOrderStatus.id}"/> ${shippingOrderStatus.name}</label>
                    </c:forEach>
                    &nbsp;&nbsp;&nbsp;
                    <label>StoreId</label>
                    <c:forEach items="${storeList}" var="store" varStatus="ctr">
                        <label><s:checkbox name="storeId"
                                           value="${store.id}"/> ${store.prefix}</label>
                    </c:forEach>
                </li>
                <shiro:hasAnyRoles name="<%=RoleConstants.ACTION_QUEUE_MANAGER%>">
                    <li>
                        <label style="float:left;width:50px;">Payment Modes</label>

                        <div class="checkBoxList">
                            <c:forEach items="${paymentModeList}" var="paymentMode" varStatus="ctr">
                                <label><s:checkbox name="paymentModes[${ctr.index}]"
                                                   value="${paymentMode.id}"/> ${paymentMode.name}</label>
                                <br/>
                            </c:forEach>
                        </div>
                    </li>

                    <li><label style="float:left;width: 60px;">Payment Status</label>

                        <div class="checkBoxList">
                            <c:forEach items="${paymentStatusList}" var="paymentStatus" varStatus="ctr">
                                <label><s:checkbox name="paymentStatuses[${ctr.index}]"
                                                   value="${paymentStatus.id}"/> ${paymentStatus.name}</label>
                                <br/>
                            </c:forEach>
                        </div>
                    </li>

                    <%--
                                    <li><label style="float:left;width: 60px;">BO Category</label>

                                        <div class="checkBoxList">
                                            <c:forEach items="${categoryList}" var="category" varStatus="ctr">
                                                <label><s:checkbox name="categories[${ctr.index}]"
                                                                   value="${category.name}"/> ${category.displayName}</label>
                                                <br/>
                                            </c:forEach>
                                        </div>
                                    </li>
                    --%>

                    <li><label style="float:left;width: 60px;">SO Category</label>

                        <div class="checkBoxList">
                            <c:forEach items="${categoryList}" var="category" varStatus="ctr">
                                <label><s:checkbox name="basketCategories[${ctr.index}]"
                                                   value="${category.name}"/> ${category.displayName}</label>
                                <br/>
                            </c:forEach>
                        </div>
                    </li>
                </shiro:hasAnyRoles>

                <li>
                    <label style="float:left;width: 60px;">Drop Ship </label>

                    <div class="checkBoxList">
                        <s:select name="dropShip" value="">
                            <s:option value="">--Select--</s:option>
                            <s:option value="0">N</s:option>
                            <s:option value="1">Y</s:option>
                        </s:select>
                    </div>
                </li>
                <li>
                    <label style="float:left;width: 60px;">Has JIT</label>

                    <div class="checkBoxList">
                        <s:select name="containsJit" value="">
                            <s:option value="">--Select--</s:option>
                            <s:option value="0">N</s:option>
                            <s:option value="1">Y</s:option>
                        </s:select>
                    </div>
                    <div style="float:left;">Sort by
                        <shiro:hasAnyRoles name="<%=RoleConstants.ACTION_QUEUE_MANAGER%>">
                            <div><s:checkbox name="sortByScore"/>Order Score</div>
                            <div><s:checkbox name="sortByPaymentDate"/>Payment Date</div>
                        </shiro:hasAnyRoles>
                        <%--<div><s:checkbox name="sortByLastEscDate"/>Escalation Date</div>--%>
                        <div><s:checkbox name="sortByDispatchDate"/>Dispatch Date</div>
                        <div><s:checkbox name="b2bOrder"/>B2B Order</div>
                    </div>

                    <div style="float:left;">
                        COD Calling Status
                       <s:select name="codCallStatus" >
                           <s:option value="">------Select------</s:option>
                           <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="userCodCallStatus"
                                                      value="id" label="name"/>
                       </s:select>
                        </div>

                </li>

                <li>

                    <div class="checkBoxList" style="width: 100%;">
                        <c:forEach items="${shippingOrderLifecycleList}" var="shippingOrderLifecycleActivity"
                                   varStatus="ctr">
                                <div class="newBoxLabel">
                                    <s:checkbox id="raj${ctr.index}" style="position: relative;float: left;" name="shippingOrderLifecycleActivities[${ctr.index}]"
                                                   value="${shippingOrderLifecycleActivity.id}"/>
                                                   <div id="div${ctr.index}" style="position: relative;float: left;top: 3px;">${shippingOrderLifecycleActivity.name}</div>
                            <span style="margin-left:30px;">
                                <c:if test="${not empty hk:getReasonsByType(shippingOrderLifecycleActivity.name)}">
                                <div id="newBoxAdminraj${ctr.index}"  class="newBox">
                                <c:forEach items="${hk:getReasonsByType(shippingOrderLifecycleActivity.name)}" var="reason"
                                           varStatus="rctr1">
                                    <div class="newBoxItem">
                                    <label><s:checkbox name="reasons[${rctr1.index}]"
                                                       value="${reason.id}"/> ${reason.classification.primary}  ${reason.classification.secondary}</label>
                                        </div>
                                </c:forEach>
                                    </div>
                            </c:if>
                            </span>
                                </div>
                        </c:forEach>
                    </div>
                </li>

                <div class="buttons">
                    <s:submit name="pre" value="Search"/>
                    <%--<label style="color:red; font-weight:bold;font-size:15px;">${actionQueueBean.unsplitOrderCount} orders to split</label>--%>
                    <s:submit name="search" value="Search (No Default)"/>
                </div>
            </s:form>
        </div>
    </ul>
</fieldset>

<s:form beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction" autocomplete="off">
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${actionQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${actionQueueBean}"/>

    <div class="clear"></div>
    <div class="clear"></div>
    <table class="align_top" width="100%" cellspacing="1">
        <thead>
        <tr>
            <th style="text-align:center">Order Details</th>
            <th style="text-align:center">Shipping Order Details</th>
        </tr>
        </thead>
        <c:forEach items="${actionQueueBean.orderList}" var="order" varStatus="ctr">
            <%pageContext.setAttribute("isSafeOrder", true);%>
            <tr class="${ctr.index % 2 == 0 ? '' : 'alt'} addressRow orderRow">
                <td width="40%" style="border:1px solid darkgoldenrod; padding:3px;">
                    <div id="orderDetails-${order.id}" class="detailDiv">
                        <div class="headingLabel">Order Details: ${order.store.prefix}
                          <s:link beanclass="com.hk.web.action.admin.crm.OrderDetailsAction"
                                  style="float:right;background:#EEE;padding:3px;color:black;border:2px solid #AAA" target="_blank">
                            <s:param name="gatewayOrderId" value="${order.gatewayOrderId}"/>
                            View Details
                          </s:link> </div>
                        <div class="floatleft">
                            Gateway Order Id: <strong>${order.gatewayOrderId}</strong>
                            <span style="margin-left:30px;"> Basket category: <strong>${order.basketCategory}</strong></span>
                        </div>
                        <c:if test="${order.targetDispatchDate != null}">
                            <div class="floatleft" style="color:red">
                                Target Dispatch Date: <fmt:formatDate value="${order.targetDispatchDate}" type="date"/>
                                <span style="margin-left:30px;"><strong>(${hk:periodFromNow(order.targetDispatchDate)})</strong></span>
                            </div>
                        </c:if>
                        <div class="floatright">
                            (<s:link beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" event="pre" target="_blank">
                            <s:param name="order" value="${order}"/> Invoice
                        </s:link>) (<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders"
                                            target="_blank">
                            <s:param name="orderId" value="${order}"/> Search order
                        </s:link>)
                        </div>
                        <div class="clear"></div>
                        <div class="floatleft">
                            <span class="orderStatusName">Order Status: <strong>${order.orderStatus.name}</strong></span>
                            <c:choose>
                                <c:when test="${order.orderStatus.id == orderStatusHold}">
                                    <s:link beanclass="com.hk.web.action.admin.order.OrderOnHoldAction" event="unHoldOrder"
                                            title="Unhold Order" class="orderStatusLink onHoldStatusLink">
                                        <s:param name="order" value="${order.id}"/>
                                        <img src="<hk:vhostImage/>/images/admin/icon_unhold.png" alt="Unhold Order" title="Unhold Order"/>
                                    </s:link>
                                </c:when>
                                <c:otherwise>
                                    <s:link beanclass="com.hk.web.action.admin.order.OrderOnHoldAction" event="holdOrder"
                                            title="Put Order on Hold"
                                            class="orderStatusLink normalStatusLink">
                                        <s:param name="order" value="${order.id}"/>
                                        <img src="<hk:vhostImage/>/images/admin/icon_hold.png" alt="Put Order on Hold"
                                             title="Put Order on Hold"/>
                                    </s:link>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="floatright">
                            <c:if test="${order.orderStatus.id == orderStatusPlaced}">
                                (<s:link beanclass="com.hk.web.action.admin.order.split.BulkOrderSplitterAction"
                                         event="splitSingleOrder" class="splitBOLink">
                                Split Order
                                <s:param name="order" value="${order}"/>
                            </s:link>)
                            </c:if>
                            (<s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
                            Order Lifecycle
                            <s:param name="order" value="${order}"/>
                        </s:link>)
                            (<s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
                            <c:if test="${!empty hk:orderComments(order)}">
                                <text style="color:#f88; font-weight:bold">Comments!</text>
                            </c:if>
                            <c:if test="${empty hk:orderComments(order)}">Add a comment</c:if>
                            <s:param name="order" value="${order}"/>
                        </s:link>)
                        </div>
                    </div>
                    <div class="clear"></div>
                    <div id="paymentDetails-${order.id}" class="detailDiv">
                        <div class="headingLabel">Payment Details:</div>
                        <div class="floatleft">
                            Total Amount: <strong>Rs.<fmt:formatNumber value="${order.payment.amount}"
                                                                       pattern="<%=FormatUtils.currencyFormatPattern%>"/></strong>
              <span class="paymentStatusName or"
                    style="margin-left:30px;">Status: ${order.payment.paymentStatus.name}</span>
                        </div>
                        <div class="floatright">
                            <%--<c:if test="${order.payment.paymentStatus.id == paymentStatusPending}">--%>
                                <c:choose>
                                    <c:when test="${order.payment.paymentMode.id == paymentModeCod}">
                                        <c:choose>
                                            <c:when test="${order.userCodCall != null && order.userCodCall.callStatus  != paymentFailed }">
                                                ${order.userCodCall.remark}
                                                <c:if test="${order.userCodCall.callStatus == thirdPartyCodCallFailed}">
                                                    <c:if test="${order.payment.paymentStatus.id == paymentStatusPending}">
                                                        (<s:link beanclass="com.hk.web.action.admin.payment.VerifyCodAction" class="confirmCodLink">
                                                        <s:param name="order" value="${order.id}"/>
                                                        Confirm COD
                                                    </s:link>)

                                                    </c:if>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${order.payment.paymentStatus.id == paymentStatusPending}">
                                                    (<s:link beanclass="com.hk.web.action.admin.payment.VerifyCodAction" class="confirmCodLink">
                                                    <s:param name="order" value="${order.id}"/>
                                                    Confirm COD
                                                </s:link>)
                                                </c:if>
                                            </c:otherwise>

                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${order.payment.paymentStatus.id == paymentStatusPending}">
                                            (<s:link beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
                                            Update as successful
                                            <s:param name="order" value="${order.id}"/>
                                        </s:link>)
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            <%--</c:if>--%>
                        </div>
                        <div class="clear"></div>
                        <div class="floatleft">
                            Date: <fmt:formatDate value="${order.payment.paymentDate}" type="both"/>
                            <span style="margin-left:30px;">Mode: ${order.payment.paymentMode.name}</span>
                            <c:if test="${order.payment.gateway != null}">
                                <span style="margin-left:30px;">Gateway: ${order.payment.gateway.name}</span>
                            </c:if>
                        </div>
                        <div class="clear"></div>
                        <c:if test="${order.payment.paymentMode.id == paymentModeCod}">
                            <div class="floatlet" style="font-size:13px;font-weight:bold;margin-bottom:5px;margin-top:5px;">Cod
                                Contact
                            </div>
                            <div class="clear"></div>
                            <div class="floatleft">Name ${order.payment.contactName}</div>
                            <div class="floatright" style="margin-right:20px;font-weight:bold;">
                                Phone ${order.payment.contactNumber}</div>
                        </c:if>
                    </div>
                    <div class="clear"></div>
                    <c:choose>
                        <c:when test="${order.priorityOrder}">
                            <div id="userDetails-${order.id}" class="detailDiv" style="background:#F6CECE;">
                        </c:when>
                        <c:otherwise>
                            <div id="userDetails-${order.id}" class="detailDiv">
                        </c:otherwise>
                    </c:choose>

                    <div class="headingLabel">User Details:</div>
                    <div id="userContactDetails">
                        <div class="floatleft">
                            Name : <span class="or">${order.user.name}</span>
                        </div>
                        <div class="floatright">
                            Email: (<s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
                            <s:param name="userFilterDto.login" value="${order.user.login}"/>
                            ${order.user.login}
                        </s:link>)
                        </div>
                        <div class="clear"></div>
                        <div class="floatleft" style="margin-top:3px;">
                            Processed Orders# ${hk:getProcessedOrdersCount(order.user)}
                        </div>
                        <div class="floatright">
                            (<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders"
                                     target="_blank"><s:param
                                name="email" value="${order.user.login}"/>See previous orders</s:link>)
                        </div>
                        <div class="clear"></div>
                        <div class="floatleft" style="margin-top:3px;">
                            ${order.address.name}<br/>
                            ${order.address.line1}<br/>
                            <c:if test="${hk:isNotBlank(order.address.line2)}">${order.address.line2}<br/></c:if>
                        </div>
                        <div class="floatright">
                            <span class="or">${order.address.city} - ${order.address.pincode.pincode}</span> ( ${order.address.state} )<br/>
                            <span class="or"> Ph. ${order.address.phone}</span>
                        </div>
                    </div>
                    </div>
                    <div class="clear"></div>
                    <c:if test="${order.commentType == commentTypeOthers}">
                        <div id="userComments-${order.id}" class="detailDiv" style="margin-top:3px;">
	                        <div class="headingLabel">User Instructions:</div>
            <span style="word-wrap:break-word">
		            ${order.userComments}
            </span>
                        </div>
                    </c:if>
                </td>

                <c:set var="shippingOrders" value="${hk:getActionAwaitingSO(order)}"/>
                <c:choose>
                    <c:when test="${not empty shippingOrders}">
                        <td width="60%" style="border:1px solid darkgreen; padding:3px;">
                            <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                                             shippingOrders="${shippingOrders}" isActionQueue="true"  />

                        </td>
                    </c:when>
                    <c:otherwise>
                        <td width="60%" style="border:1px solid red; padding:3px;">
                            Need to split order manually could not be split automatically
                            <br/><br/><strong>
                            (<s:link beanclass="com.hk.web.action.admin.order.split.SplitBaseOrderAction" class="splitBOLinkManually"><s:param
                                name="baseOrder" value="${order}"/>Split order</s:link>)
                        </strong>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
    </table>

    <div id="hiddenShippingIds"></div>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${actionQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${actionQueueBean}"/>

    <div class="buttons" style="margin-left: 80%;"><s:submit name="escalate" id="escalateButton"
                                                             value="Escalate to packing / DropShip queue"/></div>
</s:form>
<script type="text/javascript">
    $('#escalateButton').click(function() {
        $('.shippingOrderDetailCheckbox').each(function() {
            var shippingOrderDetailCheckbox = $(this);
            var isChecked = shippingOrderDetailCheckbox.attr('checked');
            if (isChecked) {
                $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[]" value="' + $(this).attr('dataId') + '"/>');
            }
        });
        return true;
    });

    $('.splitBOLink').click(function disableSplitBOLink(){
        $(this).css("display", "none");
    });

    /*$('.lineItemCheckBox').click(function() {
     $(this).parent().parent("tr").toggleClass('highlight');
     });*/
</script>
</s:layout-component>
</s:layout-render>
