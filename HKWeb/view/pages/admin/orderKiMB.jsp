<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.*" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="web.action.admin.order.OrderKiMBAction" var="orderAdmin" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Search">

    <s:layout-component name="heading">${orderAdmin.currentBreadcrumb.name}</s:layout-component>
    <s:layout-component name="htmlHead">
      <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
      <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
      <script type="text/javascript">
        $(document).ready(function() {
          $('.addRowButton').click(function() {

            var lastIndex = $('.lastRow').attr('count');
            if (!lastIndex) {
              lastIndex = -1;
            }
            $('.lastRow').removeClass('lastRow');

            var nextIndex = eval(lastIndex + "+1");
            var newRowHtml =
                '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                '  <td>' +
                '    <input type="hidden" name="lineItems[' + nextIndex + '].id" />' +
                '    <input type="text" class="variant" name="lineItems[' + nextIndex + '].productVariant"/>' +
                '  </td>' +
                '  <td class="product">' +
                '  </td>' +
                '  <td>' +
                '    <input type="text" class="qty" name="lineItems[' + nextIndex + '].qty"/>' +
                '  </td>' +
                '  <td>' +
                '    <input class="mrp" type="text" name="lineItems[' + nextIndex + '].markedPrice" readonly="readOnly"/>' +
                '  </td>' +
                '  <td>' +
                '    <input class="hkPrice" type="text" name="lineItems[' + nextIndex + '].hkPrice" readonly="readOnly"/>' +
                '  </td>' +
                '  <td>' +
                '    <input class="costPrice" type="text" name="lineItems[' + nextIndex + '].costPrice" readonly="readOnly"/>' +
                '  </td>' +
                '  <td>' +
                '    <input class="discountOnHkPrice" type="text" name="lineItems[' + nextIndex + '].discountOnHkPrice" readonly="readOnly"/>' +
                '  </td>' +
                '  <td>' +
                '  </td>' +
                '  <td>' +
                '  </td>' +
                '  <td>' +
                '  </td>' +
                '  <td>' +
                '  </td>' +
                '  <td>' +
                '  </td>' +
                '  <td>' +
                '    <input class="selected" type="checkbox" name="lineItems[' + nextIndex + '].selected"/>' +
                '  </td>' +
                '</tr>';

            $('#poTable').append(newRowHtml);

            return false;
          });

          $('.variant').live("change", function() {
            var variantRow = $(this).parents('.lineItemRow');
            var productVariantId = variantRow.find('.variant').val();
            $.getJSON(
                $('#pvInfoLink').attr('href'), {productVariantId: productVariantId},
                function(res) {
                  if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    variantRow.find('.product').html(res.data.product);
                    variantRow.find('.qty').val(1);
                    variantRow.find('.discountOnHkPrice').val(0.0);
                    variantRow.find('.mrp').val(res.data.variant.markedPrice);
                    variantRow.find('.hkPrice').val(res.data.variant.hkPrice);
                    variantRow.find('.costPrice').val(res.data.variant.costPrice);
/*
                    $('.variantDetails').html(
                        '<table>' +
                        '<tr><td>ID:</td><td>' + res.data.variant.id + '</td></tr>' +
                        '<tr><td>Name:</td><td>' + res.data.product + '</td></tr>' +
                        '<tr><td>Variant:</td><td>' + res.data.options + '</td></tr>' +
                        '<tr><td>MRP:</td><td>' + res.data.variant.markedPrice + '</td></tr>' +
                        '<tr><td>HK Price:</td><td>' + res.data.variant.hkPrice + '</td></tr>' +
                        '<tr><td>Cost Price:</td><td>' + res.data.variant.costPrice + '</td></tr>' +
                        '</table>'
                        );
*/
                  } else {
                    $('.variantDetails').html('<h2>Invalid Variant Id</h2>');
                  }
                }
                );
          });

        });
      </script>
    </s:layout-component>

    <s:layout-component name="content">
        <div style="display: none;">
          <s:link beanclass="web.action.admin.order.OrderKiMBAction" id="pvInfoLink" event="getPVDetails"></s:link>
        </div>
        <div style="display:inline;float:left;">
        <s:errors/>
        <s:form beanclass="web.action.admin.order.OrderKiMBAction" method="get" autocomplete="false">
            <fieldset class="top_label">
                <ul>
                    <div class="grouped">
                        <li><label>Order ID</label> <s:text name="orderId" style="width: 100px;"/></li>
                        <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId"/></li>
                    </div>
                </ul>
                <div class="buttons"><s:submit name="getMB" value="Order Ki MB"/></div>
            </fieldset>
        </s:form>

        <c:if test="${orderAdmin.order != null}">
            <s:form beanclass="web.action.admin.order.OrderKiMBAction">
            <table class="cont" width="100%" id="poTable">
                <thead>
                <tr>
                    <th>PV</th>
                    <th>Product</th>
                    <th>Qty</th>
                    <th>MRP</th>
                    <th>HK Price</th>
                    <th>Cost Price</th>
                    <th>Discount on HK-Price</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th>Tracking ID</th>
                    <th>Courier</th>
                    <th>Ship Date</th>
                    <th>Selected</th>
                </tr>
                </thead>

                    <c:forEach items="${orderAdmin.order.lineItems}" var="lineItem" varStatus="ctr">
                        <s:hidden name="lineItems[${ctr.index}]" value="${lineItem.id}"/>
                        <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                            <td>
                                <s:text name="lineItems[${ctr.index}].productVariant" value="${lineItem.productVariant.id}" class="variant"/>
                            </td>
                            <td>
                                 ${lineItem.productVariant.product.name}
                            </td>
                            <td>
                                <s:text name="lineItems[${ctr.index}].qty" value="${lineItem.qty}"/>
                            </td>
                            <td>
                                <s:text name="lineItems[${ctr.index}].markedPrice" value="${lineItem.markedPrice}"/>
                            </td>
                            <td>
                                <s:text name="lineItems[${ctr.index}].hkPrice" value="${lineItem.hkPrice}"/>
                            </td>
                            <td>
                                <s:text name="lineItems[${ctr.index}].costPrice" value="${lineItem.costPrice}" readonly="readonly"/>
                            </td>
                            <td>
                                <s:text name="lineItems[${ctr.index}].discountOnHkPrice"
                                        value="${lineItem.productVariant.discountOnHkPrice}"/>
                            </td>
                            <td>
                                <s:select name="lineItems[${ctr.index}].lineItemType"
                                          value="${lineItem.lineItemType.id}">
                                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                               serviceProperty="lineItemTypeList" label="name"
                                                               value="id"/>
                                </s:select>
                            </td>
                            <td>
                                <s:select name="lineItems[${ctr.index}].lineItemStatus"
                                          value="${lineItem.lineItemStatus.id}">
                                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                               serviceProperty="lineItemStatusList" label="name"
                                                               value="id"/>
                                </s:select>
                            </td>
                            <td>
                                    ${lineItem.trackingId}
                            </td>
                            <td>
                                    ${lineItem.courier.name}
                            </td>
                            <td>
                                    ${lineItem.shipDate}
                            </td>
                            <td>
                                    <s:checkbox name="lineItems[${ctr.index}].selected"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <div class="variantDetails info"></div>
                    <br/>
                    </table>
                    <s:hidden name="orderId" value="${orderAdmin.orderId}"/>
                    <a href="orderKiMB.jsp#" class="addRowButton">Add new row</a>
                    <s:submit name="deleteSelectedLineItems" value="Delete Selected Line Items"/>
                    <s:submit name="addShippingLineItem" value="Add Shipping Line Item"/>
                    <s:submit name="createRewardPointLineItem" value="Add Reward Point Line Item"/>
                    <s:submit name="addProductLineItem" value="Add Product Line Item"/>
                    <s:submit name="saveLineItems" value="Save Line Items"/>
                </s:form>
            <table>
                <thead>
                <tr>
                    <th>Order Amt</th>
                    <th>Order Gateway Order ID</th>
                    <th>Order Payment ID</th>
                    <th>Order Status</th>
                    <th>Payment ID</th>
                    <th>Payment Amt</th>
                    <th>Gateway Order ID</th>
                    <th>Payment Mode</th>
                    <th>Payment Status</th>
                    <th>Selected</th>
                </tr>
                </thead>
            <s:form beanclass="web.action.admin.order.OrderKiMBAction">
                <c:forEach items="${orderAdmin.paymentList}" var="payment" varStatus="ctr">
                    <tr>
                        <td>
                            <s:text name="order.amount"/>
                        </td>
                        <td>
                            <s:text name="order.gatewayOrderId"/>
                        </td>
                        <td>
                            <s:text name="order.payment"/>
                        </td>
                        <td>
                            <s:select name="order.orderStatus"
                                      value="${orderAdmin.order.orderStatus.id}">
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="orderStatusList" label="name"
                                                           value="id"/>
                            </s:select>
                        </td>
                        <td>
                            <s:text name="paymentList[${ctr.index}].id"/>
                        </td>
                        <td>
                            <s:text name="paymentList[${ctr.index}].amount"/>
                        </td>
                        <td>
                            <s:text name="paymentList[${ctr.index}].gatewayOrderId"/>
                        </td>
                        <td>
                            <s:select name="paymentList[${ctr.index}].paymentMode"
                                      value="${orderAdmin.order.payment.paymentMode.id}">
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="paymentModeList" label="name" value="id"/>
                            </s:select>
                        </td>
                        <td>
                            <s:select name="paymentList[${ctr.index}].paymentStatus"
                                      value="${orderAdmin.order.payment.paymentStatus.id}">
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="paymentStatusList" label="name" value="id"/>
                            </s:select>
                        </td>
                        <td>
                                <s:checkbox name="paymentList[${ctr.index}].selected"/>
                        </td>
                    </tr>
                    <s:hidden name="paymentList[${ctr.index}]" value="${orderAdmin.payment.id}"/>
                </c:forEach>
            </table>
                <s:hidden name="order" value="${orderAdmin.order.id}"/>
                <s:hidden name="orderId" value="${orderAdmin.orderId}"/>
                <s:submit name="savePayments" value="Save Payments"/>
            </s:form>
        </c:if>

    </s:layout-component>

</s:layout-render>
