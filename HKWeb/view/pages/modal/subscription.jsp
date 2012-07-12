<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.subscription.SubscriptionConstants" %>
<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/4/12
  Time: 12:20 PM
  To change this template use File | Settings | File Templates.
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

              frequency(days): <s:text name="subscription.frequencyDays" id="subscriptionFrequency" value="${sp.minFrequencyDays}" style="width: 30px; height: 18px;"/>
              &nbsp;(min : ${sp.minFrequencyDays} days - max : ${sp.maxFrequencyDays} days)  <br/>  <br/>
              subscription period(days): <s:text name="subscription.subscriptionPeriodDays" id="subscriptionPeriod" value="180" style="width: 30px; height: 18px;"/>
              &nbsp;(min : <%=SubscriptionConstants.minSubscriptionDays%> days - max : <%=SubscriptionConstants.maxSubscriptionDays%> days)   <br/> <br/>


           Start Date: <s:text name="subscription.startDate" value="<%=currentDate%>" id="subscriptionStartDate" style="width: 90px; height: 22px;"/>
            <br/> <br/>

            <s:hidden name="subscription.productVariant" value="${productVariant.id}"/>

            qty per delivery:
            <select name="subscription.qtyPerDelivery" id="subscriptionQtyPerDelivery"  >
              <c:set var="tempQty" value="1"/>
              <%
                for(int i=1;i<=sa.getSubscriptionProduct().getMaxQtyPerDelivery();i++){
              %>
              <option value="<%=i%>"><%=i%></option>
             <%
                }
              %>

            </select>
            <%--<s:text class="qtyPerDelivery" name="subscription.qtyPerDelivery"  id="subscriptionQtyPerDelivery" style="width: 25px; height: 18px;"/>--%>
            &nbsp; &nbsp; &nbsp; &nbsp;
            total qty:<span id="totalQuantity"></span>
            <s:hidden class="qty" name="subscription.qty"  id="subscriptionQty" />

            <s:submit name="addSubscription" value="subscribe"  />
          </fieldset>
        </s:form>
      </div>
      <span >Subscribe and save <fmt:formatNumber value="${sp.subscriptionDiscount180Days}" maxFractionDigits="2"/>  to   <fmt:formatNumber value="${sp.subscriptionDiscount360Days}" maxFractionDigits="2"/> &#37; extra.   </span>
      &nbsp; <span> <fmt:formatNumber value="${sp.subscriptionDiscount180Days}" maxFractionDigits="2"/>&#37; for <%=SubscriptionConstants.minSubscriptionDays%>-360 days and <fmt:formatNumber value="${sp.subscriptionDiscount360Days}" maxFractionDigits="2"/>&#37; for 360-<%=SubscriptionConstants.maxSubscriptionDays%> day plans </span>
      <script type="text/javascript">
        function _addSubscription(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            closeSubscriptionWindow();
            $('.message .line1').html("<strong>" + res.data.name + "</strong> has been added to your shopping cart");
            $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
            $('.progressLoader').hide();

            show_sub_message();
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
        $('.addSubscriptionForm').ajaxForm({dataType: 'json', success: _addSubscription});

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
            if(totalQty !=null && totalQty!=undefined && !isNaN(totalQty)){
                updateTotalQty(totalQty);
            }
        }).change();

        function updateTotalQty(totalQty){
            $('#totalQuantity').html('<b>'+totalQty+'</b>');
            $('#subscriptionQty').attr('value',totalQty);
        }

        $('#subscriptionFrequency').jStepper({minValue:${sp.minFrequencyDays},maxValue:${sp.maxFrequencyDays} });
        $('#subscriptionPeriod').jStepper({minValue:180,maxValue:450 });
        /*$('#subscriptionQty')[0].value=Math.round($('#subscriptionPeriod').val()*$('#subscriptionQtyPerDelivery').val()/$('#subscriptionFrequency').val()) ;*/
      </script>
    </div>


  </s:layout-component>

</s:layout-render>
