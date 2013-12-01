<%--
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.NewAddressAction" event="show" var="addressBean"/>
<s:layout-render name="/layouts/modal.jsp">
  <s:layout-component name="heading">New address</s:layout-component>
  <s:layout-component name="content">
    <div class="addressContainer">
      <div class="newAddress-errors"></div>

      <h2>Please enter a Shipping Address</h2>

      <s:form beanclass="com.hk.web.action.core.user.NewAddressAction" id="newAddressForm">
        <p class="gry sml">Field marked * are required.</p>
        <fieldset class="right_label short_label">
          <ul>
            <li><label>Name<b>*</b></label> <s:text name="address.name"/></li>
            <li><label>Address Line 1<b>*</b></label> <s:text name="address.line1"/></li>
            <li><label>Address Line 2</label> <s:text name="address.line2"/></li>
            <li><label>City<b>*</b></label> <s:text name="address.city"/></li>
            <li><label>State<b>*</b></label> <s:text name="address.state"/></li>
            <li><label>Pin<b>*</b></label> <s:text name="address.pincode.pincode"/></li>
            <li><label>Phone/Mobile<b>*</b></label> <s:text name="address.phone"/></li>
            <li><label>&nbsp;</label>
              <div class="buttons"><s:submit name="create" value="Save Address"/></div>
            </li>
          </ul>
        </fieldset>
      </s:form>
    </div>

    <div class="addressSuccessContainer" style="display:none;">
      <h2>New address has been added</h2>

      <p>Reloading the address book..</p>
    </div>

    <script type="text/javascript">
      $('#newAddressForm').ajaxForm({dataType: 'json', success: _saveAddress});

      function _saveAddress(res) {
        if (res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>') {
          $('.addressContainer').hide();
          $('.addressSuccessContainer').show();
          window.location.href = res.data.url;
        } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
          $('.newAddress-errors').html(getErrorHtmlFromJsonResponse(res));
        }
      }
    </script>

  </s:layout-component>

</s:layout-render>
--%>
