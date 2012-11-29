<%@ page import="com.akube.framework.gson.JsonUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.service.core.AddressService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.user.BillingAddress" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.user.BillingAddressAction" event="pre" var="addressBean"/>
<%--<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction" event="pre"/>--%>
<s:useActionBean beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre" var="orderSummary"/>

<s:layout-render name="/layouts/checkoutLayout.jsp" pageTitle="Select a Billing address">

<s:layout-component name="htmlHead">
  <script type="text/javascript">
    var noAddress = false;

    $(document).ready(function() {
      // selected address boxes should have a different bg-color
      $('input.cbox').click(addressColor).each(addressColor);

      $('#guestEmail').labelify({labelledClass: 'input_tip'});

    });
    function _closeAddress(hash) {
      hash.o.fadeOut();
      hash.w.hide();
      hash.w.html('<h1>Loading..</h1>');
    }

    function addressColor() {
      if ($(this).attr('checked')) {
        $(this).parents('tr').addClass('item_selected');
      } else {
        $(this).parents('tr').removeClass('item_selected');
      }
    }

  </script>
    <%-- use attribute selectors to apply a specify styles  --%>
    <style type="text/css">
        select.error {
            background: none repeat scroll 0 0 #FFFAFA;
            border: 1px solid #CC0000;
        }
 </style>
</s:layout-component>

<s:layout-component name="modal">
  <div class="jqmWindow" id="jqmWindowNewAddress"><h1>Loading..</h1></div>
</s:layout-component>

<s:layout-component name="steps">
  <div class='steps'>
    <div class='step'>
      <h2>Step 1</h2>

      <div class='small'>
        Select A shipping address
      </div>
    </div>
    <div class='step'>
      <h2>Step 2</h2>

      <div class='small'>
        Confirm your order
      </div>
    </div>
    <div class='step current_step'>
      <h2>Step 3</h2>

      <div class='small'>
        Choose Payment Method
      </div>
    </div>
  </div>
</s:layout-component>
<s:layout-component name="steps_content">
  <div class='current_step_content'>

    <%--<h3 style="text-align: center; margin-bottom: 20px; color: #888;">
      What address should we ship your order to?
    </h3>--%>

   <%
            AddressService addressService = ServiceLocatorFactory.getService(AddressService.class);
            pageContext.setAttribute("countryList", addressService.getAllCountry());
        %>

    <div class='left2'>
      <h3 style="text-align: left;">
        Use one of your saved Billing addresses &darr;
      </h3>

      <c:forEach items="${addressBean.billingAddresses}" var="billingAddress" varStatus="addressCount">
        <s:link beanclass="com.hk.web.action.core.user.BillingAddressAction" event="checkout" title="Click to use this address and proceed">
          <s:param name="billingAddressId" value="${billingAddress.id}"/>
           <s:param name="bankId" value="70"/>
          <s:param name="paymentMode" value="85"/>
          <s:param name="order" value="${orderSummary.order.id}"/>  
          <%--<s:param name="billingAddress.id"/>  --%>
          <div class="address" style="position: relative;">
            <h5 class="name">${billingAddress.name}</h5>

            <div class='street street1'>${billingAddress.line1}</div>
            <c:if test="${hk:isNotBlank(billingAddress.line2)}">
              <div class="street street2">${billingAddress.line2}</div>
            </c:if>
            <div class='city'>${billingAddress.city}</div>
            <div class='state'>${billingAddress.state}</div>

           <c:set var="countryId" value="${billingAddress.countryId}" />
              <input type="hidden" value="${countryId}" class="countryId">
           <div class='country'>${hk:getCountry(countryId).name}</div>


            <div class='pin'>${billingAddress.pin}</div>
            <div class='phone'>${billingAddress.phone}</div>
            <br/>
            <s:link beanclass="com.hk.web.action.core.user.BillingAddressAction" event="remove" class="delete">
              <s:param name="billingAddressId" value="${billingAddress.id}"/>
              (delete)
            </s:link>

            <span class="edit">(edit)</span>

            <div class="hidden help yellow">
              Click to use this address
              and proceed &rarr;

            </div>
          </div>
        </s:link>
      </c:forEach>
      <script type="text/javascript">
       $(document).ready(function() {
        var bool = false;
        $('.edit').click(function() {
          form = $('#newAddressForm');
          addressBlock = $(this).parents('.address');
          name = addressBlock.find('.name').text();
          street1 = addressBlock.find('.street1').text();
          street2 = addressBlock.find('.street2').text();
          city = addressBlock.find('.city').text();
          state = addressBlock.find('.state').text();
//          country = addressBlock.find('.country').text();
            countryId = addressBlock.find('.countryId').val();
          pin = addressBlock.find('.pin').text();
          phone = addressBlock.find('.phone').text();
          id = addressBlock.find('.address_id').val();
          form.find("input[type='text'][name='address.name']").val(name);
          form.find("input[type='text'][name='address.line1']").val(street1);
          if (street2) {
            form.find("input[type='text'][name='address.line2']").val(street2);
          }
          form.find("input[type='text'][name='address.city']").val(city);
//          form.find("input[type='text'][name='address.state']").val(state);
          form.find("[name='address.state']").val(state.toUpperCase());
//          form.find("[name='address.countryId']").val(country.toUpperCase());

//           form.find("input[type='text'][name='address.countryId']").val();
            $('select').val(countryId);
            form.find("input[type='text'][name='address.pin']").val(pin);
          form.find("input[type='text'][name='address.phone']").val(phone);
          form.find("input[type='hidden'][name='address.id']").val(id);
        });
           $('.delete').click(function() {
               if (confirm('Are you sure you want to delete this address?')) {
                   bool = true;
                   return true;
               } else {
                   return false;
               }
           });

           $('.address').hover(
			        function() {
				        $(this).children('.hidden').slideDown(100);
				        $(this).children('.edit').click(function() {
					        return false;
				        });
                        $(this).children('.delete').click(function() {
                          if(bool) return true;
                            else
                           return false;
                        });
			        },
			        function() {
				        $(this).children('.hidden').slideUp(50);
			        }
			        );
	        $('.address').click(function() {
		        var add_url = $(this).children('a').attr('href');
		        document.location.href = add_url;
	        });
        });

      </script>


    </div>
    <div class='or'>
      or
    </div>
    <div class='right'>
      <h3>
        Add a new Billing address
      </h3>

      <div class="addressContainer shipping_address">
        <div class="newAddress-errors">
          <s:errors/>
        </div>
        <div class="newAddress-errors alert messages"><s:messages key="generalMessages"/></div>
        <s:form beanclass="com.hk.web.action.core.user.BillingAddressAction" id="newAddressForm"
                onsubmit="return validateForm()" method="post" name="BillingAddressForm">
        <s:hidden name="bankId" value="70"/>
        <s:hidden name="paymentMode" value="85"/>
        <s:hidden name="address.id"/>
        <span class="aster special">(Fields marked * are required.)</span>

          <div class='label'>Name<span class="aster">*</span></div>
          <s:text name="address.name" maxlength = "80"/>
          <div class='label'>Address Line 1<span class="aster">*</span></div>
          <s:text name="address.line1" maxlength = "120"/>
          <div class='label'>Address Line 2</div>
          <s:text name="address.line2" maxlength = "120"/>
          <div class='label'>City<span class="aster">*</span></div>
          <s:text name="address.city" maxlength = "60"/>
          <div class='label'>State<span class="aster">*</span></div>
          <s:text name="address.state" maxlength = "50"/>
           <div class='label'>Country<span class="aster">*</span></div>
          <s:select name="address.countryId" style="width:310px;">
              <s:option value="">-select-</s:option>
              <c:forEach items="${countryList}" var="country">
                  <s:option value="${country.id}">${country.name}</s:option>
           </c:forEach>
          </s:select>

            <%--<s:select name="country" value="${countr}">--%>
                <%--<s:option value="">-Select-</s:option>--%>
                <%--<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allCountry" value="id" label="name"/>--%>
            <%--</s:select>--%>

          <div class='label'>PIN Code<span class="aster">*</span></div>
          <s:text name="address.pin" class="pincode" maxlength="20"/>
          <div class='label'>Phone / Mobile<span class="aster">*</span></div>
          <s:text name="address.phone" id="phoneNo" maxlength = "25"/>
          <s:submit name="create" value="Use this address and continue >" class="button addressValidation" style="left: 50px;"/>
          <div class="special" style="text-align: right;">
            Proceed to Order Confirmation <br/>(This address will be added to your address book so you can use it later)
          </div>
        </s:form>
      </div>

      <div class="addressSuccessContainer" style="display:none;">
        <h2>New address has been added</h2>

        <p>Reloading the address book..</p>
      </div>

      <script type="text/javascript">
           function validateForm()
    {
//        var numbers = /^[0-9]+$/;
        var billingAddressName = document.forms["BillingAddressForm"]["address.name"].value;
        var billingAddressLine1 = document.forms["BillingAddressForm"]["address.line1"].value;
        var billingAddressCity = document.forms["BillingAddressForm"]["address.city"].value;
        var billingAddressState = document.forms["BillingAddressForm"]["address.state"].value;
        var billingAddressPin = document.forms["BillingAddressForm"]["address.pin"].value;
        var billingAddressPhone = document.forms["BillingAddressForm"]["address.phone"].value;
        var billingAddressCountry = document.forms["BillingAddressForm"]["address.countryId"].value;
        if (billingAddressName == null || billingAddressName == "")
        {
            alert("Name must be filled out");
            return false;
        }
        if (billingAddressLine1 == null || billingAddressLine1 == "")
        {
            alert("Address Line 1 must be filled out");
            return false;
        }
        if (billingAddressCity == null || billingAddressCity == "")
        {
            alert("City must be filled out");
            return false;
        }
        if (billingAddressState == null || billingAddressState == "")
        {
            alert("State must be filled out");
            return false;
        }

         if ( billingAddressCountry == "")
        {
            alert("Country must be Selected ");
            return false;
        }

        if (billingAddressPin == null || billingAddressPin == "")
        {
            alert("Pincode must be filled out");
            return false;
        }
        if (billingAddressPhone == null || billingAddressPhone == "")
        {
            alert("Phone/Mobile must be filled out");
            return false;
        }
    }
      </script>


    </div>
  </div>
</s:layout-component>
<s:layout-component name="zopim">
  <jsp:include page="/includes/_zopim.jsp"/>
</s:layout-component>

<s:layout-component name="footer"> </s:layout-component>

</s:layout-render>


