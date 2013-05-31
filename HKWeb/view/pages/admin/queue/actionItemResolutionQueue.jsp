<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderStatusService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.impl.service.queue.BucketService" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.domain.order.ShippingOrder" %>
<%@ page import="com.hk.pact.dao.BaseDao" %>
<%@ page import="com.hk.domain.queue.Bucket" %>
<%@ page import="com.hk.domain.queue.ActionTask" %>
<%@ page import="com.hk.constants.queue.EnumActionTask" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.pact.dao.user.UserDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" var="actionItemQueueBean"/>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD" var="actionItemBean" event="view"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Action Item Resolution Queue">
<s:layout-component name="htmlHead">
    <%
        BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
        UserDao userDao = ServiceLocatorFactory.getService(UserDao.class);

        pageContext.setAttribute("bucketList", baseDao.getAll(Bucket.class));
        pageContext.setAttribute("currentATList", baseDao.getAll(ActionTask.class));
        pageContext.setAttribute("previousATList", baseDao.getAll(ActionTask.class));
    %>

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
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

        table tr.row_border {
            outline: 1px solid #cccccc;
        }

        .row_shipped td {
            background-color: #b8ffcf;
        }

        .bucketContainer {
            display: none;
            border: 2px solid #ccc;
            width: 300px;
            height: 100px;
            overflow-y: scroll;
        }

        .searchBuckets{
            position: relative;
            float: left;
            margin-right: 20px;
        }

        .topFilterBucketContainer{
            position:relative;
            float:left;
            width:50%;
        }
        .bucketFileterContainer{
            position: relative;
            float:left;
        }
    </style>
</s:layout-component>
<s:layout-component name="heading">Action Item Resolution Queue</s:layout-component>
<s:layout-component name="content">

<fieldset class="top_label">
    <ul>
        <div class="grouped grid_12">
            <s:form beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" method="get"
                    autocomplete="false">
                <div style="width:1100px; margin:10px;">
                    <label>SO Order ID </label> <s:text name="shippingOrderId"/>
                    <%--<label>Buckets </label>--%>
                    <%--<s:select name="buckets">--%>
                        <%--<option value="">Any Bucket</option>--%>
                        <%--<c:forEach items="${bucketList}" var="bucket">--%>
                            <%--<s:option value="${bucket.id}">${bucket.name}</s:option>--%>
                        <%--</c:forEach>--%>
                    <%--</s:select>--%>
                    <label>Current AT </label>
                    <s:select name="currentActionTasks">
                        <option value="">Any CAT</option>
                        <c:forEach items="${currentATList}" var="currentAT">
                            <s:option value="${currentAT.id}">${currentAT.name}</s:option>
                        </c:forEach>
                    </s:select>
                    <label>Previous AT </label>
                    <s:select name="previousActionTasks">
                        <option value="">Any PAT</option>
                        <c:forEach items="${previousATList}" var="previousAT">
                            <s:option value="${previousAT.id}">${previousAT.name}</s:option>
                        </c:forEach>
                    </s:select>
                </div>
                <div style="width:1120px; margin:20px;">
                    <div class="topFilterBucketContainer">
                    <label>Push Start
                        Date </label><s:text class="date_input startDate" style="width:150px"
                                             formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                             name="startPushDate"/>
                    <label>Push End
                        Date </label><s:text class="date_input endDate" style="width:150px"
                                             formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                             name="endPushDate"/>
                    </div>
                    <div class="bucketFileterContainer">
                        <a href="javascript:void(0)" class="searchBuckets" id="anchorBuckets"> Show Buckets  </a>
                        <div id="bucketCont" style="position:relative;float:left;" class="bucketContainer">
                            <div class="mainBuckets">
                                <c:forEach items="${bucketList}" var="bucket" varStatus="ctr">
                                    <label>
                                        <c:choose>
                                            <c:when test="${fn:contains(actionItemQueueBean.userBuckets,bucket)}">
                                                <input type="checkbox" name="buckets[${ctr.index}].selected"
                                                       checked="checked"/> ${bucket.name}
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" name="buckets[${ctr.index}].selected"/> ${bucket.name}
                                            </c:otherwise>
                                        </c:choose>
                                        <s:hidden name="buckets[${ctr.index}].id" value="${bucket.id}"/>
                                    </label>
                                    <br/>
                                </c:forEach>
                            </div>
                    </div>
                </div>
                </div>

                <s:submit style="margin:0 0 10px 25px;" name="search" value="Search"/>
            </s:form>
        </div>
    </ul>
</fieldset>


<s:form beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" name="bucketsForm" class="bucketsForm"
        autocomplete="off">

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${actionItemQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${actionItemQueueBean}"/>
    <div class="clear"></div>
    <div class="clear"></div>
    <table class="align_top" width="100%" cellspacing="1">
        <thead>
        <tr>
            <th style="text-align:center">Action Item Details</th>
            <th style="text-align:center">Shipping Order Details</th>
        </tr>
        </thead>
        <c:forEach items="${actionItemQueueBean.actionItems}" var="actionItem" varStatus="ctr">

            <tr class="${ctr.index % 2 == 0 ? '' : 'alt'} addressRow orderRow">
                <td width="30%" style="border:1px solid darkgoldenrod; padding:3px;">
                    <div id="div1"><h2></h2>
                        Action Item : ${actionItem.id} <br>
                        Current Buckets :
                      <span class="current-buckets">
                    <c:forEach items="${actionItem.buckets}" var="bucket">
                        <%--<label><s:checkbox name="buckets[${ctr.index}].checked"--%>
                        <%--disabled="true"/> ${bucket.name}</label--%>
                        <label class="abc"> ${bucket.name} </label> ,
                    </c:forEach>
                          </span>
                    </div>
                    <div>
                            <%--<a href="javascript:void(0)" id="editBuckets"> edit action item--%>
                            <%--<s:param name="actionItem" value="${actionItem.id}"/></a>--%>

                        (<s:link beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD" event="view"
                                 class="editBuckets">
                        <s:param name="actionItem" value="${actionItem.id}"/> Edit Action Item
                    </s:link>)
                    </div>
                      
                    <div class="bucketContainer">
                    <div class="checkBoxList">

                    </div>
                    <input type="hidden" name="actionItem" value="${actionItem.id}" class="actItemId"/>
                    <input type="button" class="saveBuckets" value="Save"/>



                    </div>

                    <div>
                        <input type="hidden" class="selActItemId" name="actionItem" value="${actionItem.id}"/>
                        PAT : ${actionItem.previousActionTask.name} <br>
                        CAT : <s:select class="actionTask" name="currentActionTask"
                                        value="${actionItem.currentActionTask.id}">
                        <c:forEach items="${currentATList}" var="enumActionTask">
                            <s:option value="${enumActionTask.id}">${enumActionTask.name}</s:option>
                        </c:forEach>
                    </s:select>
                        <s:submit name="updateTask" value="Update Task" class="updateTask"/>

                            <%--(<s:link beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" event="updateTask"--%>
                            <%--class="updateTask button_orange">--%>
                            <%--<s:param name="actionItem" value="${actionItem.id}"/>--%>
                            <%--<s:param name="currentActionTask" value="${actionItem.currentActionTask.id}"/> Update Task--%>
                            <%--</s:link>)--%>
                    </div>

                    <shiro:hasPermission name="<%=PermissionConstants.ACTION_ITEM_RESOLVER%>">
                        <div>
                            Current Priority
                            <s:select name="priorityId" value="${actionItem.priority}">
                                <c:forEach var="i" begin="1" end="10">
                                    <s:option value="${i}">${i}</s:option>
                                </c:forEach>
                            </s:select>
                            <s:submit name="setPriority" value="Change Priority"/>
                        </div>

                        <div>
                            Status : ${actionItem.trafficState.name}
                        </div>

                        ( <s:link beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD"
                                  event="flagActionItems">
                        <s:param name="actionItem" value="${actionItem.id}"/>
                        Mark Flag
                    </s:link>)


                        (<s:link beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD"
                                 event="changeTrafficState">
                        <s:param name="actionItem" value="${actionItem.id}"/>
                        Assign Trafic State
                    </s:link>)

                        (<s:link beanclass="com.hk.web.action.admin.queue.action.ActionItemCRUD"
                                 event="changeBulkTrafficState">
                        Change Bulk Traffic State
                    </s:link>)
                    </shiro:hasPermission>
                        <%--<div class="floatleft">--%>
                        <%--Target Dispatch : <fmt:formatDate value="${actionItem.shippingOrder.targetDispatchDate}"--%>
                        <%--type="date"/>--%>
                        <%--</div>--%>
                </td>
                <td width="70%" style="border:1px solid darkgreen; padding:3px;">
                        ${actionItem.shippingOrder.id}
                    <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                                     shippingOrder="${actionItem.shippingOrder}"
                                     isActionQueue="true"/>
                </td>
            </tr>
        </c:forEach>
    </table>

    <div id="hiddenShippingIds"></div>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${actionItemQueueBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${actionItemQueueBean}"/>
    <div style="display:inline;float:left;">
            <%--<s:submit name="moveToActionAwaiting" class="shippingOrderActionBtn" value="Move Back to Action Awaiting"/>--%>
    </div>
    <div style="display:none">
        <s:link beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" event="saveBuckets"
                id="actionItemRes"></s:link>
    </div>
    <div style="display:none">
        <s:link beanclass="com.hk.web.action.admin.queue.ActionItemResolutionQueueAction" event="updateTask"
                id="updateTasks"></s:link>
    </div>

</s:form>
<script type="text/javascript">
$(document).ready(function() {

    $('.bckts').live('click', function() {
        //                alert(this.checked);
        if (!this.checked) {
            $(this).removeAttr('checked');
        } else {
            $(this).attr('value', true);
        }
    });


    $('.searchBuckets').click(function () {
       if($("#bucketCont").css("display")=="block"){
        $("#bucketCont").hide();
        $("#anchorBuckets").text("Show Buckets");   
       }
       else{
        $("#bucketCont").show();
        $("#anchorBuckets").text("Hide Buckets");     
       }
    });


    $('.saveBuckets').click(function() {
        var parent = $(this).parents(".bucketContainer");
        var ele = parent;
        var actionItem = ele.find("[name=actionItem]") ;
        var actionItemId = ele.children('.actItemId').val();
        var bucket = {selected :false,id :0};
        var chldEle = {};
        ele.find('label').each(function() {
            var chld = $(this);

            chld.find('input').each(function() {
                var iEle = $(this);

                if (iEle.attr('type') == 'checkbox') {
                    //  console.log(iEle.is(':checked'));
                    chldEle[iEle.attr('name')] = 'false';
                    if (iEle.is(':checked')) {
                        chldEle[iEle.attr('name')] = 'true';
                        console.log('saki naka' + iEle.attr('name'));
                    }
                } else {
                    chldEle[iEle.attr('name')] = iEle.val();

                    console.log('checking values' + iEle.val());
                }
            }

                    );
        });
        chldEle[actionItem.attr('name')] = actionItemId;
        //            alert(actionItemId );
        //            var buckets = new Array();
        //            var i =0;
        //               $(this).parents(".bucketContainer").find('label').each(function(){
        //                       var selected = $(this).children('.bckts').is(':checked');
        //                       var bucketId = $(this).children('.bcktId').val();
        //                   if(selected !=null && bucketId!=null){
        //                       var bucket= {selected : selected, id : bucketId};
        //                       buckets[i++] = bucket;
        //                   }
        //               });
        //            alert(buckets);
        $.ajax({
            url:$('#actionItemRes').attr('href'),
            data : chldEle,
            type: 'post',
            dataType: 'json',
            success : function(res) {

                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    var str = '';
                    for (i in res.data.name) {
                        str += res.data.name[i].name + ' ,';
                    }
                    ele.parents('.addressRow').find('.current-buckets').html(str);

                } else {
                    //                      alert("hello");
                }
            }
        });
    <%--$.getJSON($('#actionItemRes').attr('href'),{buckets: chldEle, actionItem: actionItemId},function(res){--%>
    <%--if(res.code == <%=HealthkartResponse.STATUS_OK%>){--%>
    <%--alert("sab sahi chal gaya");--%>
    <%--} else{--%>
    <%--alert("hello");--%>
    <%--}--%>
    <%--});--%>
    });

    $('.shippingOrderActionBtn').click(function () {
        var index = 0;
        $('.shippingOrderDetailCheckbox').each(function () {
            var shippingOrderDetailCheckbox = $(this);
            var isChecked = shippingOrderDetailCheckbox.attr('checked');
            if (isChecked) {
                var reasonId = '.shippingOrderReason_' + $(this).attr('dataId');
                var reason = $(reasonId);
                $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[' + index + ']" value="' + $(this).attr('dataId') + '"/>');
                $('#hiddenShippingIds').append('<input type="hidden" name="shippingOrderList[' + index + '].reason" value="' + reason.val() + '"/>');
                index++;
            }
        });
        return true;
    });

    $('.editBuckets').click(function() {
        var elem = $(this);
        $.getJSON($(this).attr('href'), function(res) {
            var trgt = elem.parent().siblings('.bucketContainer').children('.checkBoxList');
            trgt.html('');
            _editBuckets(res, elem, trgt);
        });
        return false;
    });


    $('.updateTask').click(function() {
        var elem = $(this);
        var actionItemId = $(this).parent().children('.selActItemId').val();
        var actionTaskId = $(this).parent().children('.actionTask').val();
        $.ajax({
            url:$('#updateTasks').attr('href'),
            data: {actionItem : actionItemId, currentActionTask : actionTaskId},
            type: 'post',
            dataType: 'json',
            success : function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    $(elem).siblings().show();
                    alert(res.message + " for Action Item : " + actionItemId);
                }

            }
        });
        return false;
    });


    function _saveBuckets(res, elem) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            $(elem).siblings().show();
        }
    }


    function _editBuckets(res, elem, trgt) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            // $(elem).siblings().show();

            var checkedBuckets = res.data.name;
            var checkedBucketsId = "";
            for (var i = 0; i < checkedBuckets.length; i++) {
                checkedBucketsId += "[" + checkedBuckets[i].id + "],";
            }

            var allBuckets = "";
        <c:forEach items="${actionItemBean.buckets}" var="bucket" varStatus="ctr">
            var bucketId = "[${bucket.id}]";
            if (checkedBucketsId.indexOf(bucketId) != -1) {
                //                    alert("buckets id " + bucketId );
                allBuckets += '<label><input type="checkbox" class="bckts" name="buckets[${ctr.index}].selected" checked="checked" />';
            } else {
                allBuckets += '<label><input type="checkbox" class="bckts" name="buckets[${ctr.index}].selected" />';
            }
            allBuckets += '${bucket.name}' +
                          '<input type="hidden" class="bcktId" name="buckets[${ctr.index}].id" value="${bucket.id}"/>' +
                          '</label>' +
                          '<br/>';
        </c:forEach>
            //alert(allBuckets);
            trgt.append(allBuckets);
            
            trgt.parents(".bucketContainer").show();
            //                               alert(buckets.get(0).name);
            //                               $("#div1").text("Buckets-->" + checkedBucket.length);
        }

    }
});
</script>

</s:layout-component>
</s:layout-render>
