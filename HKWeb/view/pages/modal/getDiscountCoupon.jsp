<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.discount.SendDiscountCouponAction" var="sdcActionBean" event="pre"/>
<s:layout-render name="/layouts/modal.jsp">
  <s:layout-component name="heading">
    Get Instant Discount Coupon
  </s:layout-component>

  <s:layout-component name="content">
    <div class='left'>

      <div class="sendCouponForm-errors"></div>
      <div style="display:none; background-color: #ffff99; padding: 5px;" id="sendCouponForm-messages" class="messages"></div>

      <div class='signup'>
        <s:form beanclass="com.hk.web.action.core.discount.SendDiscountCouponAction" id="sendCouponForm">
          <div class='label'>Name </div>
          <s:text name="name" placeholder="Enter your name" value="${sdcActionBean.name}"/>
          <div class='label'>Mobile</div>
          <s:text name="mobile" placeholder="Your mobile"/>
          <div class='label'>OR</div>
          <div class='label'>Email</div>
          <s:text name="email" placeholder="Your email" value="${sdcActionBean.email}"/>
          <br/><br/>
          <s:checkbox name="subscribe" checked="checked" id="subscribeCheckbox"/> I would like to receive offer updates in the future

          <s:hidden name="srcUrl" value="${sdcActionBean.srcUrl}"/>
          <s:hidden name="topLevelCategory" value="${sdcActionBean.topLevelCategory}"/>
          <s:submit name="getDiscountCoupon" value="Send me the coupon" class="button"/>

        </s:form>
      </div>
    </div>

    <script type="text/javascript">
      $('#sendCouponForm').ajaxForm({dataType: 'json', success: function(res) {
        if (res.code == 'ok') {
          $('#sendCouponForm-messages').html(res.message).fadeIn();
        } else if (res.code == 'error') {
          $('.sendCouponForm-errors').html(getErrorHtmlFromJsonResponse(res));
        }
      }});
      $('#subscribeCheckbox').attr('checked', 'checked');
    </script>
  </s:layout-component>

</s:layout-render>
