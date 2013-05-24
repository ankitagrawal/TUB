<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.inventory.EnumReconciliationType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" var="pa"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Reconciliation Voucher">
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<s:layout-component name="htmlHead">

    <%
        MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
        List<ReconciliationType> reconciliationTypeList = masterDataDao.getProductAuditedReconVoucherType();
        pageContext.setAttribute("reconciliationTypeList", reconciliationTypeList);
    %>


    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.addRowButton').click(function () {
                var lastIndex = $('.lastRow').attr('count');
                if (!lastIndex) {
                    lastIndex = -1;
                }
                $('.lastRow').removeClass('lastRow');

                var nextIndex = eval(lastIndex + "+1");
                var reconcilitionvalue = $('.reconciliationId').val();
                var rvlineItem = $('.rvitem').val();


                var reconciliationTypeOptions = '<select class="reconciliationType valueChange" name="rvLineItems[' + nextIndex + '].reconciliationType">';
                <c:forEach items="${reconciliationTypeList}" var="reconciliationTypeVar">
                reconciliationTypeOptions += '<option value="'+${reconciliationTypeVar.id}+
                '">' + "${reconciliationTypeVar.name}" + '</option>';
                </c:forEach>
                var label = '<label> Product Variant Audited </label>';
                var link = '<s:link  class ="singlesave button_orange" beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction" event="subtractInventoryForPVFromSingleBatch">Subtract</s:link>';

                var newRowHtml =
                        '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                                '  <td>' +
                                '    <input type="hidden" name="rvLineItems[' + nextIndex + '].id" />' +
                                '    <input type="text" class="variant" name="rvLineItems[' + nextIndex + '].productVariant"/>' +
                                '<input type="hidden" name="rvLineItems[' + nextIndex + '] value="' + rvlineItem + '"/>' +
                                '<input type="hidden" name="reconciliationVoucher" value="' + reconcilitionvalue + '"/>' +
                                '  </td>' +
                                '  <td class="pvDetails"></td>' +
                                '  <td>' +
                                '    <input type="text" id="quantity" name="rvLineItems[' + nextIndex + '].qty" />' +
                                '  </td>' +

                                '<td>' +
                                label +
                                '</select>' +
                                '<input type="hidden" value="finance" class="reconciliationTypeIdentifier"/>' +

                                '</td>' +
                                '<td>' +
                                '<input type="text" id="reconciliedqty" value="0" name="rvLineItems[' + nextIndex + '].reconciledQty" readonly="readonly" />' +
                                '</td>' +
                                '<td> ' +
                                link +
                                '</td>' +
                                '</tr>';

                $('#poTable').append(newRowHtml);

                return false;
            });

            $('.variant').live("change", function () {
                $('.error').hide();
                var variantRow = $(this).parents('.lineItemRow');
                var productVariantId = variantRow.find('.variant').val();
                var productVariantDetails = variantRow.find('.pvDetails');
                $.getJSON(
                        $('#pvInfoLink').attr('href'), {productVariantId:productVariantId, warehouse: ${whAction.setWarehouse.id}},
                        function (res) {
                            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                                variantRow.find('.mrp').val(res.data.variant.markedPrice);
                                variantRow.find('.costPrice').val(res.data.variant.costPrice);
                                productVariantDetails.html(
                                        res.data.product + '<br/>' +
                                                res.data.options
                                );
                            } else {
                                $('.variantDetails').html('<h2>' + res.message + '</h2>');
                            }
                        }
                );
            });


            $('.singlesave').live('click', function () {
                $('.error').hide();
                var curEle = $(this);
                var queryString = '';
                var sep = '';
                var qty = $('#quantity').val();
                var variant = $('.variant').val();
                if (qty == null || qty.trim() == '' || variant == null || variant.trim() == '') {
                    alert("ProductVariant/Qty/Batch are mandatory");
                    return false;
                }
                if (qty == 0) {
                    alert('Qty Can Not Be Zero');
                    return false;
                }
                $(this).parents('tr').find('input,select,textarea').each(function () {
                    if ($(this).attr('class') == 'reconciliationTypeIdentifier') {
                        return;
                    }
                    queryString = queryString + sep + $(this).attr('name') + '=' + escape($(this).attr('value'));
                    sep = '&';
                });
                var href = $('#reconForm').attr('action');
                curEle.css("display", "none");
                $('.addRowButton').hide();

                $.ajax({
                    type:"POST",
                    url:href + '?subtractInventoryForPVFromSingleBatch=',
                    data:queryString,
                    dataType:'json',
                    success:function (data) {
                        if (data.code == '<%=HealthkartResponse.STATUS_OK%>') {
                            var reconQty = '' + data.data.rvLineItem.reconciledQty;
                            var qty = '' + data.data.rvLineItem.qty;
                            if (reconQty == qty) {
                                curEle.parents('tr').css({"background-color":"#ccff99"});
                                curEle.parents('tr').find('input,select,textarea').each(function () {
                                    if ($(this).attr('type') == 'hidden') {
                                        $(this).replaceWith('');
                                    }
                                    else if ($(this).tagName == 'textarea') {

                                        $(this).replaceWith($(this).text());
                                    }
                                    else {
                                        if ($(this).attr('id') == 'reconciliedqty') {
                                            $(this).replaceWith('' + reconQty);
                                        }
                                        else {
                                            $(this).replaceWith($(this).val());
                                        }
                                    }
                                });
                                curEle.css("display", "none");

                            }

                            else {
                                $('#reconciliedqty').val('' + reconQty);
                                $('.singlesave').css("display", "block");
                            }

                            $('.error').empty();
                            $('.error').hide();
                        }

                        $('.addRowButton').show();
                        if (data.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                            $('.singlesave').css("display", "block");
                            $('.addRowButton').show();
                            $('.error').empty();
                            $('.error').html(data.message);
                            $('.error').show();
                        }
                    },
                    error:function onError() {
                        $('.singlesave').css("display", "block");
                        $('.addRowButton').show();
                        alert('Error in  Saving JSON');
                    }


                });
                return false
            });
            
            $('#excelUpload').live("click", function() {
                var filebean = $('#fileBean').val();
                if (filebean == null || filebean == '') {
                  alert('choose file');
                  return false;
                }
              });

        });

    </script>
</s:layout-component>

<s:layout-component name="content">
    <div>
        <c:if test="${pa.errorMessage != null}">
            <span style="color: #ff0000; font-size: 13px;"> ${pa.errorMessage} </span>
        </c:if>
    </div>
    <div style="display: none;">
        <s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink"
                event="getPVDetails">
        </s:link>
    </div>


    <div>
    </div>
    <h2>Product Variant Audit</h2>

    <h2>RV No # ${pa.reconciliationVoucher.id}</h2>
    <s:form id="reconForm" beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">
        <s:hidden class="reconciliationId" name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
        <s:hidden name="errorMessage" value=""/>
        <table>
            <tr>
                <td>Reconciliation Date</td>
                <td>
                        ${pa.reconciliationVoucher.reconciliationDate}
                </td>
            </tr>
            <tr>
                <td>Remarks<br/><span class="sml gry">(eg. XXX)</span></td>
                <td>${pa.reconciliationVoucher.remarks}</td>
            </tr>
            <tr>
                <td>For Warehouse</td>
                <td>
                        ${pa.reconciliationVoucher.warehouse.identifier}
                </td>
            </tr>
        </table>

        <div class="error"
             style="display:none;background-color:salmon;font-size:12; margin-top: 20px; padding: 5px;width:550px;"></div>


        <table border="1">
            <thead>
            <tr>
                <th>VariantID</th>
                <th>Details</th>
                <th>Qty<br/>Only(+)</th>
                <th>Reconciliation Type</th>
                <th>Reconcilied Qty</th>

            </tr>
            </thead>
            <tbody id="poTable">
            <c:forEach var="rvLineItem" items="${pa.reconciliationVoucher.rvLineItems}" varStatus="ctr">
                <c:set var="productVariant" value="${rvLineItem.sku.productVariant}"/>
                <c:choose>
                    <c:when test="${(rvLineItem.reconciledQty == 0) || (rvLineItem.reconciledQty < rvLineItem.qty)}">
                        <tr count="${ctr.index}" id="rowno" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                            <s:hidden name="rvLineItems[${ctr.index}]" class="rvitem" value="${rvLineItem.id}"/>
                            <s:hidden name="rvLineItems[${ctr.index}].reconciliationVoucher"
                                      value="${rvLineItem.reconciliationVoucher.id}"/>
                            <td>
                                <s:text name="rvLineItems[${ctr.index}].productVariant" class="variant"
                                        value="${productVariant.id}"/>
                            </td>
                            <td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
                            </td>
                            <td><s:text name="rvLineItems[${ctr.index}].qty" id="quantity" value="${rvLineItem.qty}"/>
                            </td>
                            <c:set var="pBAname" value="<%=EnumReconciliationType.ProductVariantAudited.getName()%>"/>
                            <td class="reconciliationType">
                                <input type="hidden" value="finance"
                                       class="reconciliationTypeIdentifier"/>
                                <c:set var="productAuditedTypeName"
                                       value="<%=EnumReconciliationType.ProductVariantAudited.getName()%>"/>
                                    ${productAuditedTypeName}
                            </td>

                            <td><s:text name="rvLineItems[${ctr.index}].reconciledQty" id="reconciliedqty"
                                        value="${rvLineItem.reconciledQty}" readonly="readonly"/>
                            </td>
                            <td>
                                <s:link class="singlesave button_orange"
                                        beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction"
                                        event="subtractInventoryForPVFromSingleBatch">Subtract</s:link>
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <tr style="background-color:#ccff99;">
                            <td>
                                    ${productVariant.id}
                            </td>
                            <td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
                            </td>
                            <td>${rvLineItem.qty}
                            </td>
                            <td>${rvLineItem.reconciliationType.name}
                            </td>
                            <td>${rvLineItem.reconciledQty}</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </tbody>
        </table>
        <div class="variantDetails info"></div>
        <br/>
        <a href="editReconciliationVoucher.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
    </s:form>


    <div>
        <shiro:hasRole name="<%=RoleConstants.WH_MANAGER%>">
            <s:form beanclass="com.hk.web.action.admin.inventory.ReconciliationVoucherAction">
                <s:hidden name="reconciliationVoucher" value="${pa.reconciliationVoucher.id}"/>
                <s:hidden name="errorMessage" value=""/>
                <fieldset>
                    <legend>Upload Excel To Subtract By Variant</legend>
                    <br/>
                    (VARIANT_ID, QTY) as excel headers
                    <br/><br/>

                    <h2>File to Upload: <s:file id="fileBean" name="fileBean" size="30"/></h2>

                    <div class="buttons">
                        <s:submit id="excelUpload" name="uploadSubtractExcelForProductAuditedForSingleBatch" value="Upload"/>
                    </div>

                </fieldset>
            </s:form>
        </shiro:hasRole>
    </div>
    <style type="text/css">
        #reconciliedqty {
            border: none;
            width: 13px;
        }

    </style>
</s:layout-component>

</s:layout-render>