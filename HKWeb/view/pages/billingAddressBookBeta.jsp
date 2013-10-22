<%@ page import="com.hk.pact.service.core.AddressService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.user.BillingAddressAction" event="pre" var="addressBean"/>
<s:useActionBean beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre" var="orderSummary"/>

<c:set var="issuerPaypal" value="5"/>

<s:layout-render name="/layouts/checkoutLayoutBeta.jsp" pageTitle="Select a Billing address">

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
    <s:layout-render name="/layouts/embed/_checkoutStripBeta.jsp" index="4" />
</s:layout-component>



<s:layout-component name="steps_content">
<div class="topHeadingBillingAddress">To proceed with payment using Paypal you would need to enter your billing details.</div>
  <div class='current_step_content'>

   <%
            AddressService addressService = ServiceLocatorFactory.getService(AddressService.class);
            pageContext.setAttribute("countryList", addressService.getAllCountry());
        %>

      <div class="billingAddressLeftNew2">
    <div class='left2' style="margin-left: 0;">
      <h3 class="arialBlackBold" style="text-align: left;">
        Use one of your saved Billing addresses
      </h3>

      <c:forEach items="${addressBean.billingAddresses}" var="billingAddress" varStatus="addressCount">
      <div class="address raj_address" style="width:300px;float:left;">
          <s:link beanclass="com.hk.web.action.core.user.BillingAddressAction" event="checkout" class="raj_addressLink" title="Click to use this address and proceed">
          <s:param name="billingAddressId" value="${billingAddress.id}"/>
          <s:param name="issuer" value="${issuerPaypal}"/>
          <s:param name="order" value="${orderSummary.order.id}"/>
            <h5 class="name fnt-caps">${billingAddress.name}</h5>

            <div class='street street1'>${billingAddress.line1}</div>
            <c:if test="${hk:isNotBlank(billingAddress.line2)}">
              <div class="street street2">${billingAddress.line2}</div>
            </c:if>
            <div class='city'>${billingAddress.city}</div>
            <div class='state'>${billingAddress.state}</div>

           <c:set var="countryId" value="${billingAddress.country.id}" />
              <input type="hidden" value="${countryId}" class="countryId">
           <div class='country'>${hk:getCountry(countryId).name}</div>

            <div class='pin'>${billingAddress.pin}</div>
            <div class='phone'>${billingAddress.phone}</div>
            <br/>
            <s:link beanclass="com.hk.web.action.core.user.BillingAddressAction" event="remove" style="display: inline-block;" class="btn btn-gray">
              <s:param name="billingAddressId" value="${billingAddress.id}"/>
                DELETE
            </s:link>

            <span style="display: inline-block;" class="btn btn-gray edit">EDIT</span>

              <div class="useAddressButton">
              Click to use this address
              and proceed

            </div>
        </s:link>
          </div>
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
          form.find("[name='address.state']").val(state.toUpperCase());
          $('select').val(countryId);
          form.find("input[type='text'][name='pin']").val(pin);
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
          <s:link beanclass="com.hk.web.action.core.payment.PaymentModeAction" class="btn btn-blue" style="width:150px;;">GO BACK</s:link>
          <div class="specialNew2" style="left: 0;margin-top: 10px;">
              Go back and change your payment mode
          </div>
   </div>

   <h3 class="shippingAddressheading arialBlackBold" style="position: relative;left: 19%;top: 5px;margin-bottom: 10px;">
       Or add a new Billing address
   </h3>
    <div class='right' style="width: 375px;float: right;padding: 0;border: 0;">


      <div class="addressContainer shipping_address">
        <div class="newAddress-errors">
          <s:errors/>
        </div>
        <div class="newAddress-errors alert messages"><s:messages key="generalMessages"/></div>
        <s:form beanclass="com.hk.web.action.core.user.BillingAddressAction" id="newAddressForm" onsubmit="return validateForm()" method="post" name="BillingAddressForm">
        <s:hidden name="issuer" value="${issuerPaypal}"/>

            <div class="row">
                <label class="rowLabel">Name <span class="aster">* </span></label>
                <s:text name="address.name" class="rowText" maxlength = "80"/>

            </div>
            <div class="clear"></div>

            <div class="row">
                <label class="rowLabel">Address Line 1 <span class="aster">* </span></label>
                <s:text name="address.line1" class="rowText" maxlength = "120"/>

            </div>
            <div class="clear"></div>

            <div class="row">
                <label class="rowLabel">Address Line 2 </label>
                <s:text name="address.line2" class="rowText" maxlength = "120"/>

            </div>
            <div class="clear"></div>

            <div class="row">
                <label class="rowLabel">City<span class="aster">* </span></label>
                <s:text name="address.city" class="rowText" maxlength = "60"/>

            </div>
            <div class="clear"></div>

            <div class="row">
                <label class="rowLabel">State<span class="aster">* </span></label>
                <s:text name="address.state" class="rowText" maxlength = "50"/>

            </div>
            <div class="clear"></div>

            <div class="row">
                <label class="rowLabel">Country<span class="aster">* </span></label>
                <s:select class="signUpInputNew" name="countryId" style="width:310px;height: 35px !important;">
                    <s:option value="">-select-</s:option>
                    <c:forEach items="${countryList}" var="country">
                        <s:option value="${country.id}">${country.name}</s:option>
                    </c:forEach>
                </s:select>
            </div>
            <div class="clear"></div>

            <div class="row">
                <label class="rowLabel">PIN Code<span class="aster">* </span></label>
                <s:text name="pin" class="rowText" maxlength = "20"/>
                <s:hidden name="address.pincode" value="-1"/>

            </div>
            <div class="clear"></div>

            <div class="row">
                <label class="rowLabel">Phone<span class="aster">* </span></label>
                <s:text name="address.phone" id="phoneNo" class="rowText" maxlength = "25"/>

            </div>
            <div class="clear"></div>
          <s:submit name="create" value="USE THIS ADDRESS AND CONTINUE" class="btn btn-blue" style="margin-bottom: 20px;"/>
          <div class="specialNew2">
            This address will be added to your address book and you will now be directed to the order confirmation page
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
        var billingAddressName = document.forms["BillingAddressForm"]["address.name"].value;
        var billingAddressLine1 = document.forms["BillingAddressForm"]["address.line1"].value;
        var billingAddressCity = document.forms["BillingAddressForm"]["address.city"].value;
        var billingAddressState = document.forms["BillingAddressForm"]["address.state"].value;
        var billingAddressPin = document.forms["BillingAddressForm"]["pin"].value;
        var billingAddressPhone = document.forms["BillingAddressForm"]["address.phone"].value;
        var billingAddressCountry = document.forms["BillingAddressForm"]["countryId"].value;
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
        <style type="text/css">
            .row {
                margin-top: 0;
                float: left;
                margin-left: 0;
                padding-top: 2px;
            }

            .rowLabel {
                display: block;
                padding-right: 5px;
                width: 150px;
                margin-top: 5px;
                margin-bottom: 5px;
            }

            .rowText {
                border-width: 0;
                padding-top: 0;
                padding-bottom: 0;
                font: inherit;
            }

            .error {
                position: relative;
                clear: both;
            }
            .aster{color: #ff0000;}
        </style>


    </div>

  </div>
</s:layout-component>

</s:layout-render>


