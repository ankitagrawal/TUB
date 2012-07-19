<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.subscription.SubscriptionConstants" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/4/12
  Time: 12:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" var="sua" event="pre"/>
<c:set var="productVariant" value="${sua.subscription.productVariant}"/>
<c:set var="product" value="${sua.subscription.productVariant.product}"/>
<c:set var="sp" value="${sua.subscriptionProduct}"/>
<c:set var="subscription" value="${sua.subscription}" />
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
                <s:form beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" event="save" class="editSubscriptionForm">
                    <fieldset>
                        <br/>
                        <s:hidden name="subscription" value="${subscription.id}" id="subscriptionId"/>
                        <div style="width:400px; float: left;">
                            frequency : <s:text name="subscription.frequencyDays" id="subscriptionFrequency" value="${subscription.frequencyDays}" style="width: 30px; height: 18px;"/>
                            &nbsp;(min : ${sp.minFrequencyDays} days - max : ${sp.maxFrequencyDays} days)  <br/>  <br/>
                            period  &nbsp; &nbsp;  :   <s:text name="subscription.subscriptionPeriodDays" id="subscriptionPeriod" value="${subscription.subscriptionPeriodDays}" style="width: 30px; height: 18px;"/>
                            &nbsp;(min : <%=SubscriptionConstants.minSubscriptionDays%> days - max : <%=SubscriptionConstants.maxSubscriptionDays%> days)   <br/> <br/>


                            Start Date : <s:text name="subscription.startDate" value="${subscription.startDate}" formatPattern="<%=FormatUtils.uiDateFormatPattern%>" id="subscriptionStartDate" style="width: 90px; height: 22px;"/>
                            <br/> <br/>

                            <s:hidden name="subscription.productVariant" value="${productVariant.id}"/>

                            qty  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  :
                            <select name="subscription.qtyPerDelivery" id="subscriptionQtyPerDelivery"  >
                                <c:set var="tempQty" value="1"/>
                                <%
                                    for(int i=1;i<=sua.getSubscriptionProduct().getMaxQtyPerDelivery();i++){
                                %>
                                <option value="<%=i%>" <%if(sua.getSubscription().getQtyPerDelivery()==i){ %> selected="true"<% }%> ><%=i%></option>
                                <%
                                    }
                                %>

                            </select> (per delivery)
                                <%--<s:text class="qtyPerDelivery" name="subscription.qtyPerDelivery"  id="subscriptionQtyPerDelivery" style="width: 25px; height: 18px;"/>--%>
                            <br/> <br/>
                            <strong>Total Qty</strong> &nbsp;:&nbsp; <span class="hk num" id="totalQuantity" style="color: orangered;"></span>
                            <s:hidden class="qty" name="subscription.qty"  id="subscriptionQty" />
                            <br/>

                            <br/><br/>
                        </div>
                        <div >
                            <br/> <br/>
                            <br/>
                            <table>
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
                                        HK price
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                    <td>
                             <span class='hk num'  style="font-size: 14px;" :>
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
                             <span class='hk num' id="subscriptionPrice" style="font-size: 14px;" :>

                </span>
                                    </td>
                                    <td>
                                        <span id="extraDiscount" ></span>
                                    </td>
                                </tr>
                            </table>

                            <br/> <br/>
                            <s:submit name="save" value="Save"   />
                        </div>
                    </fieldset>
                </s:form>
            </div>
            <span >Subscribe and save <fmt:formatNumber value="${sp.subscriptionDiscount180Days}" maxFractionDigits="2"/>  to   <fmt:formatNumber value="${sp.subscriptionDiscount360Days}" maxFractionDigits="2"/> &#37; extra.   </span>
            &nbsp; <span> <fmt:formatNumber value="${sp.subscriptionDiscount180Days}" maxFractionDigits="2"/>&#37; for <%=SubscriptionConstants.minSubscriptionDays%>-360 days and <fmt:formatNumber value="${sp.subscriptionDiscount360Days}" maxFractionDigits="2"/>&#37; for 360-<%=SubscriptionConstants.maxSubscriptionDays%> day plans </span>
            <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-ui.min.js"></script>
            <script type="text/javascript">
                function _addSubscription(res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        closeSubscriptionWindow();
                        location.reload();
                    }else if(res.code == '<%=HealthkartResponse.STATUS_ERROR%>'){
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
                $('.editSubscriptionForm').ajaxForm({dataType: 'json', success: _addSubscription});

                //          $( "#subscriptionStartDate" ).datepicker({ minDate: 0, maxDate: "+2M ",dateFormat : 'dd/mm/yy',showOn: 'button' }).next('button').text('').button({icons:{primary : 'ui-icon-calendar'}});
                $("#subscriptionStartDate" ).datepicker({ minDate: 0, maxDate: "+2M ",dateFormat : 'dd/mm/yy' });
                function closeSubscriptionWindow(){
                    $("#subscriptionWindow").jqmHide();
                }

                $('#subscriptionPeriod,#subscriptionFrequency,#subscriptionQtyPerDelivery').change(function(){
                    var frequency=$('#subscriptionFrequency').val();
                    var qtyPerDelivery=$('#subscriptionQtyPerDelivery').val();
                    var subscriptionPeriod=$('#subscriptionPeriod').val();
                    var totalQty=Math.round(subscriptionPeriod/frequency)*qtyPerDelivery;
                    if(totalQty !=null && totalQty!=undefined && !isNaN(totalQty) && totalQty !=Infinity){
                        updateTotalQtyAndPrice(totalQty);
                    }
                }).change();

                function updateTotalQtyAndPrice(totalQty){
                    var subscriptionPrice180=Math.round(${productVariant.hkPrice}-${sp.subscriptionDiscount180Days}*${productVariant.markedPrice}/100);
                    var subscriptionPrice360=Math.round(${productVariant.hkPrice}-${sp.subscriptionDiscount360Days}*${productVariant.markedPrice}/100);
                    var subscriptionPeriod=$('#subscriptionPeriod').val();
                    $('#totalQuantity').html('<b>'+totalQty+'</b>');
                    $('#subscriptionQty').attr('value',totalQty);
                    if(subscriptionPeriod<360){
                        $('#subscriptionPrice').html('<strong><b>Rs '+subscriptionPrice180+'</b></strong>');
                        $('#extraDiscount').html('( ${sp.subscriptionDiscount180Days} &#37;)' );
                    }else{
                        $('#subscriptionPrice').html('<strong><b>Rs '+subscriptionPrice360+'</b></strong>');
                        $('#extraDiscount').html('( ${sp.subscriptionDiscount360Days} &#37;)' );
                    }
                }

                $('#subscriptionFrequency').jStepper({minValue:${sp.minFrequencyDays},maxValue:${sp.maxFrequencyDays} });
                $('#subscriptionPeriod').jStepper({minValue:<%=SubscriptionConstants.minSubscriptionDays%>,maxValue:<%=SubscriptionConstants.maxSubscriptionDays%> });
                /*$('#subscriptionQty')[0].value=Math.round($('#subscriptionPeriod').val()*$('#subscriptionQtyPerDelivery').val()/$('#subscriptionFrequency').val()) ;*/
            </script>
        </div>


    </s:layout-component>


</s:layout-render>
