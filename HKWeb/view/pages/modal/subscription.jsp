<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.subscription.SubscriptionConstants" %>
<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/4/12
  Time: 12:20 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.subscription.SubscriptionAction" var="sa" event="pre"/>
<c:set var="productVariant" value="${sa.productVariant}"/>
<c:set var="product" value="${sa.product}"/>
<c:set var="sp" value="${sa.subscriptionProduct}"/>
<s:layout-render name="/layouts/modal.jsp">
    <s:layout-component name="heading">
        Subscription for ${product.name}
    </s:layout-component>

    <s:layout-component name="content">
        <div  id="subscription-container">
            <div id="subcriptionErrors"></div
            <%
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                String currentDate=simpleDateFormat.format(BaseUtils.getCurrentTimestamp());
            %>

            <div >
                <s:form beanclass="com.hk.web.action.core.subscription.AddSubscriptionAction" class="addSubscriptionForm">
                    <fieldset>
                        <br/>

                        <div style="width:400px; float: left;">
                            <table>
                                <tr>
                                    <td>
                                        frequency
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                    <td>
                                        <s:text name="subscription.frequencyDays" id="subscriptionFrequency" value="${sp.maxFrequencyDays}" style="width: 30px; height: 18px;"/>
                                        &nbsp;(${sp.minFrequencyDays}  - ${sp.maxFrequencyDays} days)
                                    </td>
                                </tr>
                                <tr><td>&nbsp;</td></tr>
                                <tr>
                                    <td>
                                        plan
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                    <td>
                                        <s:text name="subscription.subscriptionPeriodDays" id="subscriptionPeriod" value="<%=SubscriptionConstants.minSubscriptionDays%>" style="width: 30px; height: 18px;"/>
                                        &nbsp;(<%=SubscriptionConstants.minSubscriptionDays%> - <%=SubscriptionConstants.maxSubscriptionDays%> days)
                                    </td>
                                </tr>
                                <tr><td>&nbsp;</td></tr>
                                <tr>
                                    <td>
                                        start date
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                    <td>
                                        <s:text name="subscription.startDate" value="<%=currentDate%>" id="subscriptionStartDate" style="width: 90px; height: 22px;"/>
                                    </td>
                                </tr>
                                <tr><td>&nbsp;</td></tr>
                                <tr>
                                    <td>
                                        quantity
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                    <td>
                                        <select name="subscription.qtyPerDelivery" id="subscriptionQtyPerDelivery"  >
                                            <c:set var="tempQty" value="1"/>
                                            <%
                                                for(int i=1;i<=sa.getSubscriptionProduct().getMaxQtyPerDelivery();i++){
                                            %>
                                            <option value="<%=i%>"><%=i%></option>
                                            <%
                                                }
                                            %>

                                        </select> (per delivery)
                                    </td>
                                </tr>
                            </table>

                            <s:hidden name="subscription.productVariant" value="${productVariant.id}"/>

                            <br/> <br/>
                            <strong>Total Qty</strong> &nbsp;:&nbsp; <span class="hk num" id="totalQuantity" style="color: orangered;"></span>
                            <s:hidden class="qty" name="subscription.qty"  id="subscriptionQty" />
                            <br/>

                            <br/><br/>
                        </div>
                        <div >
                            <br/>
                            <table>
                                <tr>
                                    <td><b><strong>Single Unit Price:</strong></b> <br/></td>
                                </tr>
                                <tr>
                                    <td>
                                        MRP
                                    </td>
                                    <td>&nbsp;</td>
                                    <td>
                             <span class='cut num'>
                  Rs <fmt:formatNumber value="${productVariant.markedPrice}" maxFractionDigits="0"/>
                </span>  <br/><br/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Normal price
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                    <td>
                             <span class='cut num'  style="font-size: 14px;" :>
                  Rs <fmt:formatNumber value="${productVariant.hkPrice}"
                                       maxFractionDigits="0"/>
                </span>          <br/><br/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Subscription price
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                    <td>
                                        <span class='hk num' id="subscriptionPrice" style="font-size: 14px;" > </span>
                                    </td>
                                    <td>
                                        <span id="extraDiscount" ></span>  <br/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><br/><b><strong>Total Price:</strong></b></td> <td>&nbsp;</td>
                                    <td><br/><span class='hk num' id="totalSubscriptionPrice" style="font-size: 14px;" > </span></td>
                                </tr>
                            </table>

                            <br/> <br/>
                            <s:submit name="addSubscription" id="addSubscription" value="subscribe"   />
                        </div>
                    </fieldset>
                </s:form>
            </div>
            <span >Subscribe and save <fmt:formatNumber value="${sp.subscriptionDiscount180Days}" maxFractionDigits="2"/>  to   <fmt:formatNumber value="${sp.subscriptionDiscount360Days}" maxFractionDigits="2"/> &#37; extra.   </span>

            &nbsp; <span> <fmt:formatNumber value="${sp.subscriptionDiscount180Days}" maxFractionDigits="2"/>&#37; for <%=SubscriptionConstants.minSubscriptionDays%>-360 days and <fmt:formatNumber value="${sp.subscriptionDiscount360Days}" maxFractionDigits="2"/>&#37; for 360-<%=SubscriptionConstants.maxSubscriptionDays%> day plans. &nbsp; </span>
            <s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" event="pre" target="_blank">(click here) </s:link> to know more
            <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-ui.min.js"></script>
            <script type="text/javascript">
	            $(document).ready(function () {
		            function _addSubscription(res) {
			            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
			            <c:choose>
			            <c:when test="${sa.fromCart}">
				            location.reload();
			            </c:when>
			            <c:otherwise>
				            closeSubscriptionWindow();
				            $('.message .line1').html("<strong>" + res.data.name + "</strong> " + res.message);
				            $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");

				            show_sub_message();
			            </c:otherwise>
			            </c:choose>

			            } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
				            $('#subcriptionErrors').html(getErrorHtmlFromJsonResponse(res));
			            }
			            $('#gulal').show();
		            }

		            function show_sub_message() {
			            $('.message').css("top", "70px");
			            $('.message').animate({
				            opacity: 1
			            }, 500);
		            }

		            $('.addSubscriptionForm').ajaxForm({dataType: 'json', success: _addSubscription});


		            function closeSubscriptionWindow() {
			            $("#subscriptionWindow").jqmHide();
			            $("#addSubscriptionWindow").jqmHide();
		            }

		            $('#subscriptionPeriod,#subscriptionFrequency,#subscriptionQtyPerDelivery').change(function() {
			            var frequency = $('#subscriptionFrequency').val();
			            var qtyPerDelivery = $('#subscriptionQtyPerDelivery').val();
			            var subscriptionPeriod = $('#subscriptionPeriod').val();
			            var totalQty = Math.round(subscriptionPeriod / frequency) * qtyPerDelivery;
			            if (totalQty != null && totalQty != undefined && !isNaN(totalQty) && totalQty != Infinity) {
				            updateTotalQtyAndPrice(totalQty);
			            }
		            }).change();

		            function updateTotalQtyAndPrice(totalQty) {
			            var subscriptionPrice180 = Math.round(${productVariant.hkPrice}-${sp.subscriptionDiscount180Days} * ${productVariant.markedPrice}/100);
			            var subscriptionPrice360 = Math.round(${productVariant.hkPrice}-${sp.subscriptionDiscount360Days} * ${productVariant.markedPrice}/100);
			            var subscriptionPeriod = $('#subscriptionPeriod').val();
			            $('#totalQuantity').html('<b>' + totalQty + '</b>');
			            $('#subscriptionQty').attr('value', totalQty);
			            if (subscriptionPeriod < 360) {
				            $('#subscriptionPrice').html('<strong><b>Rs ' + subscriptionPrice180 + '</b></strong>');
				            $('#extraDiscount').html('(extra  ${sp.subscriptionDiscount180Days} &#37; off)');
				            $('#totalSubscriptionPrice').html('<strong><b>Rs ' + (subscriptionPrice180 * totalQty) + '</b></strong>');
			            } else {
				            $('#subscriptionPrice').html('<strong><b>Rs ' + subscriptionPrice360 + '</b></strong>');
				            $('#extraDiscount').html('(extra ${sp.subscriptionDiscount360Days} &#37; off)');
				            $('#totalSubscriptionPrice').html('<strong><b>Rs ' + (subscriptionPrice360 * totalQty) + '</b></strong>');
			            }
		            }

		            $('#subscriptionFrequency').jStepper({minValue:${sp.minFrequencyDays},maxValue:${sp.maxFrequencyDays} });
		            $('#subscriptionPeriod').jStepper({minValue:<%=SubscriptionConstants.minSubscriptionDays%>,maxValue:<%=SubscriptionConstants.maxSubscriptionDays%> });
		            $("#subscriptionStartDate").datepicker({ minDate: 0, maxDate: "+2M ",dateFormat : 'dd/mm/yy' });
	            });
            </script>
        </div>


    </s:layout-component>

</s:layout-render>
