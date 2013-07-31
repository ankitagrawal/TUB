<%@ page import="com.hk.domain.courier.Courier" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.domain.analytics.Reason" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.reversePickup.EnumReverseAction" %>
<%@ page import="com.hk.constants.reversePickup.EnumWarehouseQAStatus" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.reversePickup.RPWarehouseCheckinAction" var="rpw"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reverse Pickup Booking Screen">
<s:layout-component name="htmlHead">
    <%
        MasterDataDao masterDataDao = ServiceLocatorFactory.getBean(MasterDataDao.class);
        List<Reason> reasonList = masterDataDao.getCustomerReasonForReversePickup();
        List<Reason> warehouseReceivedConditionList = masterDataDao.getWarehouseReceivedCondition();
        List<Courier> reversePickupCourierList = masterDataDao.getCouriersForReversePickup();
        pageContext.setAttribute("reversePickupCourierList", reversePickupCourierList);
        pageContext.setAttribute("customerReasonList", reasonList);
        pageContext.setAttribute("warehouseReceivedConditionList", warehouseReceivedConditionList);
    %>
    <style type="text/css">
        .btm-dshed {
            border-bottom: 1px dashed #C7CACC;
        }

        .btm-solid {
            border-bottom: 1px solid #002737;
        }

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

        .search-box {
            margin: 1em auto;
            text-align: center;
        }

        .margin-2 {
            margin: 2em auto;

        }

        .margin-7 {
            margin: 7em auto;

        }

        .download-link {
            color: red;
            font-size: 10px;
            font-weight: bolder;
        }

        .check-in {
            margin: 4em auto;
            text-align: center;
        }

        .heading p {
            text-align: center;
            font-size: 20px;
            color: #3379BB;
            font-weight: bolder;
            line-height: 18px;
        }
    </style>

    <script type="text/javascript">
        $(document).ready(function () {
            $('.barcode-scan').focus();
            var alreadyScannedCodeList = [];
            $('.barcode-scan').live("change", function () {
                var scannedVal = $(this).val();
                if (scannedVal == null || scannedVal.trim() == '') {
                    return false;
                }
//                if ($.inArray(scannedVal, alreadyScannedCodeList) > -1) {
//                    alert('Barcode Already Scanned');
//                    return false;
//                }
//                alreadyScannedCodeList.push(scannedVal);

                $(this).attr("disable", "disable");
                var rpOrderId = $('.rpvalue').val();
                $.getJSON(
                        $('.barcode-link').attr('href'), {scannedBarcode:scannedVal, reversePickupOrder:rpOrderId, scannedBarcodeList:alreadyScannedCodeList},
                        function (res) {
                            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                                var test = scannedVal;
                                var rcvItemId = res.data.lineItemId;
                                var extraCode = true;
                                $('.barcode-text').each(function (index) {
                                    var lineId = $(this).attr("lineItemId");
                                    var checkedIn = $(this).attr("checkedIn");
                                    var currentBarcode = $(this).val();
                                    if ((currentBarcode == null || currentBarcode == '') && lineId == rcvItemId && checkedIn != 'true') {
                                        extraCode = false;
                                        $(this).val("" + scannedVal);
                                        return false;
                                    }
                                });
                                if (extraCode) {
                                    $('.errordiv').empty();
                                    $('.errordiv').html('<h2>RP order created for qty ' + ' , all are  already scanned. ' +
                                            'this is extra Item for same product </h2>');
                                    $('.errordiv').css("display", "block");
                                }
                            }
                            else {
//                                alreadyScannedCodeList.pop(scannedVal);
                                $('.errordiv').empty();
                                $('.errordiv').html('<h2>' + res.message + '</h2>');
                            }
                            $(this).removeAttr("disabled", "disabled");
                        }
                );

            });

            $('.save-rp').live("click", function () {
                var curEle = $(this);
                var queryString = '';
                var sep = '';
                $(this).parents('tr').find('input,select,textarea').each(function () {
                    queryString = queryString + sep + $(this).attr('name') + '=' + escape($(this).attr('value'));
                    sep = '&'
                });
                var href = $('.mainform').attr('action');
                $.ajax({
                    url:href + '?checkInRPLineItem=',
                    type:'post',
                    data:queryString,
                    dataType:'json',
                    success:function (res) {
                        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                            curEle.parents('tr').css({"background-color":"#ccff99"});
                            curEle.parents('tr').find('input,select,textarea').each(function () {
                                if ($(this).attr('type') == 'hidden') {
                                    $(this).replaceWith('');
                                }
                                else if ($(this).tagName == 'textarea') {
                                    $(this).replaceWith($(this).text());
                                }
                                else {
                                    $(this).replaceWith($(this).val());
                                }
                            });
                            curEle.css("display", "none");
                            curEle.parents('tr').find('.download-link').css("display", "none");
                        }
                        else {
                            curEle.css("display", "block");
                            curEle.parents('tr').find('.download-link').css("display", "block");
                            $('.errordiv').empty();
                            $('.errordiv').html('<h2>' + res.message + '</h2>');
                        }
                    },
                    error:function onError() {
                        alert('Error In submitting request');
                    }

                });
            });


        });
    </script>

</s:layout-component>
<s:layout-component name="content">
    <div class="heading" style="height: 30px;">
        <p>Reverse PickUp CheckIn
            <c:if test="${rpw.reversePickupOrder != null}">
                ${rpw.reversePickupOrder.reversePickupId}
            </c:if>
        </p>
    </div>
    <div class="errordiv" style="margin: 0 auto; color:#ff0000; font-size:13px;">
        <span style="color:red;">${rpw.errorMessage}</span>
    </div>
    <c:if test="${rpw.showSearchBox}">
        <div class="search-box">
            <s:form beanclass="com.hk.web.action.admin.reversePickup.RPWarehouseCheckinAction">
                <label>Enter ReversePickupId</label>
                <s:text name="reversePickupId"/>
                <s:submit name="search" value="search"/>
            </s:form>
        </div>
    </c:if>


    <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.reversePickup.RPWarehouseCheckinAction" class="barcode-link"
                event="verifyScannedBarcode"></s:link>
    </div>
    <div class="clear"></div>
    <c:if test="${rpw.reversePickupOrder != null}">
        <div class="margin-2">
            <s:form beanclass="com.hk.web.action.admin.reversePickup.RPWarehouseCheckinAction" class="mainform">
                <s:hidden name="reversePickupOrder" value="${rpw.reversePickupOrder.id}" class="rpvalue"/>
                <div>
                    <table class="rline-items">
                        <thead>
                        <tr>
                            <th>Unit No.</th>
                            <th></th>
                            <th>Variant Id</th>
                            <th>Product Name</th>
                            <th>Reason For Return</th>
                            <th>Barcode</th>
                            <th>Warehouse Condition</th>
                            <th>Warehouse Comment</th>
                            <th>Warehouse Remark</th>
                            <th>Barcode</th>
                            <th>Action</th>
                            <th>Courier Name/AWB</th>
                        </tr>
                        </thead>
                        <c:set value="${fn:length(rpw.reversePickupOrder.rpLineItems)}" var="listSize"/>
                        <c:set value="0" var="prevLineId"/>
                        <c:set value="1" var="unitNo"/>
                        <c:forEach items="${rpw.reversePickupOrder.rpLineItems}" var="rplineitem" varStatus="ctr">
                            <c:set value="${ctr.index}" var="index"/>
                            <c:set value="${rplineitem.lineItem.id}" var="currentLineId"/>
                            <tr class="${prevLineId == currentLineId || ctr.first ? 'btm-dshed' : 'btm-solid'}">
                                <td>
                                        <%-- set hidden varibales --%>
                                    <s:hidden name="rpLineItems[${index}]" value="${rplineitem.id}"/>
                                    <s:hidden name="reversePickupOrder" value="${rpw.reversePickupOrder.id}"/>
                                    <c:set value="flase" var="valueSet"/>
                                    <c:if test="${prevLineId == currentLineId || ctr.first}">
                                        ${unitNo}
                                        <c:set value="true" var="valueSet"/>
                                        <c:set value="${unitNo + 1}" var="unitNo"/>
                                    </c:if>
                                    <c:if test="${(prevLineId != currentLineId && !ctr.first)}">
                                        <c:set value="1" var="unitNo"/>
                                        <c:if test="${!valueSet}">
                                            ${unitNo}
                                        </c:if>
                                    </c:if>
                                </td>
                                <td>
                                    <c:set value="${rplineitem.lineItem.sku.productVariant}" var="productVariant"/>
                                    <div class='img48' style="vertical-align:top;">
                                        <c:choose>
                                            <c:when test="${productVariant.product.mainImageId != null}">
                                                <div style="display: none;">
                                                    <hk:productImage imageId="${productVariant.product.mainImageId}"
                                                                     size="<%=EnumImageSize.MediumSize%>"
                                                                     id="mediumImage"/>
                                                </div>

                                                <a href="#" class="hkProductLightbox">
                                                    <hk:productImage imageId="${productVariant.product.mainImageId}"
                                                                     size="<%=EnumImageSize.TinySize%>"/>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/images/ProductImages/ProductImagesOriginal/${productVariant.product.id}.jpg"
                                                   class="lightbox">
                                                    <img class="prod48"
                                                         src="${pageContext.request.contextPath}/images/ProductImages/ProductImagesThumb/${productVariant.product.id}.jpg"
                                                         alt="${productVariant.product.name}"/></a>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                </td>
                                <td>${rplineitem.lineItem.sku.productVariant.id}</td>
                                <td>${rplineitem.lineItem.sku.productVariant.product.name}
                                        ${rplineitem.lineItem.sku.productVariant.optionsPipeSeparated}</td>
                                <td>
                                    <c:set var="rpReturnId" value="${rplineitem.customerReasonForReturn.id}"/>
                                    <c:forEach items="${customerReasonList}" var="reason">
                                        <c:if test="${reason.id == rpReturnId}">
                                            ${reason.classification.primary}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td>
                                    <s:text name="rpLineItems[${index}].itemBarcode"
                                            disabled="${rplineitem.warehouseReceivedCondition != null? 'disabled' : ''}"
                                            checkedIn="${rplineitem.warehouseReceivedCondition != null}"
                                            lineItemId="${rplineitem.lineItem.id}" class="barcode-text"/>

                                </td>
                                <td>
                                    <s:select name="rpLineItems[${index}].warehouseReceivedCondition"
                                              disabled="${rplineitem.warehouseReceivedCondition != null? 'disabled' : ''}"
                                              value="${rplineitem.warehouseReceivedCondition}">
                                        <s:option value="">--Select-- </s:option>
                                        <c:forEach items="${warehouseReceivedConditionList}" var="wreason">
                                            <s:option value="${wreason.id}">${wreason.classification.primary}</s:option>
                                        </c:forEach>
                                    </s:select>
                                </td>
                                <td>
                                    <s:select name="rpLineItems[${index}].warehouseComment"
                                              disabled="${rplineitem.warehouseReceivedCondition != null? 'disabled' : ''}"
                                              value="${rplineitem.warehouseComment}">
                                        <s:option value="">--Select-- </s:option>
                                        <c:forEach items="<%=EnumWarehouseQAStatus.values()%>" var="status">
                                            <s:option value="${status.id}">${status.name}</s:option>
                                        </c:forEach>
                                    </s:select>
                                </td>
                                <td>
                                    <s:textarea style="width: 300px; height: 60px;"
                                                disabled="${rplineitem.warehouseReceivedCondition != null? 'disabled' : ''}"
                                                name="rpLineItems[${index}].warehouseRemark"
                                                value="${rplineitem.warehouseRemark}"/>
                                </td>
                                <td>
                                    <c:if test="${rplineitem.warehouseReceivedCondition == null}">
                                        <s:link class="download-link"
                                                beanclass="com.hk.web.action.admin.reversePickup.RPWarehouseCheckinAction"
                                                event="downloadBarcode"> (Download Barcode)
                                            <s:param name="lineItem" value="${rplineitem.lineItem.id}"/>
                                            <s:param name="reversePickupId"
                                                     value="${rpw.reversePickupOrder.reversePickupId}"/>
                                        </s:link>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${rplineitem.warehouseReceivedCondition == null}">
                                        <a class="save-rp" href="javascript:void(0)"> (Save)</a>
                                    </c:if>
                                </td>

                                <c:if test="${ctr.first}">
                                    <td rowspan="${listSize}">
                                        Courier Name : ${rplineitem.reversePickupOrder.courierName} <br>
                                        AWB :${rplineitem.reversePickupOrder.trackingNumber}<br>
                                        PickUp Time : <fmt:formatDate
                                            value="${rplineitem.reversePickupOrder.pickupTime}"
                                            type="both" timeStyle="short"/>
                                    </td>
                                </c:if>
                            </tr>
                            <c:set var="prevLineId" value="${currentLineId}"/>
                        </c:forEach>
                    </table>
                </div>

                <div class="check-in">
                    <s:submit name="completeWarehouseCheckIn" value="Mark Checked"/>
                </div>
            </s:form>

            <div class="margin-7">
                <div style="float: left;">
                    <label>Scan Barcode Here</label>
                    <s:form partial="true" beanclass="com.hk.web.action.admin.reversePickup.RPWarehouseCheckinAction">
                        <s:text name="scannedBarcode" class="barcode-scan" title="scan barcode here"/>
                    </s:form>
                </div>

            </div>
        </div>
    </c:if>
</s:layout-component>

</s:layout-render>