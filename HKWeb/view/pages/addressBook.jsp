<%@ page import="com.akube.framework.gson.JsonUtils" %>
<%@ page import="mhc.common.constants.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="mhc.web.action.SelectAddressAction" event="pre" var="addressBean"/>
<s:useActionBean beanclass="mhc.web.action.CartAction" var="cartAction" event="pre"/>

<s:layout-render name="/layouts/checkoutLayout.jsp" pageTitle="Select a shipping address">

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
      }                                                                           l
    }


  </script>
</s:layout-component>

<s:layout-component name="modal">
  <div class="jqmWindow" id="jqmWindowNewAddress"><h1>Loading..</h1></div>
</s:layout-component>

<s:layout-component name="steps">
  <div class='steps'>
    <div class='step current_step'>
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
    <div class='step'>
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

    <div class='left2'>
      <h3 style="text-align: left;">
        Use one of your saved addresses &darr;
      </h3>

      <c:forEach items="${addressBean.addresses}" var="address" varStatus="addressCount">
        <s:link beanclass="mhc.web.action.SelectAddressAction" event="checkout" title="Click to use this address and proceed">
          <s:param name="selectedAddress" value="${address.id}"/>
          <div class="address" style="position: relative;">
            <h5 class="name">${address.name}</h5>

            <div class='street street1'>${address.line1}</div>
            <c:if test="${hk:isNotBlank(address.line2)}">
              <div class="street street2">${address.line2}</div>
            </c:if>
            <div class='city'>${address.city}</div>
            <div class='state'>${address.state}</div>
            <div class='pin'>${address.pin}</div>
            <div class='phone'>${address.phone}</div>
            <br/>
            <s:link beanclass="mhc.web.action.SelectAddressAction" event="remove" class="delete" onclick="return confirm('Are you sure you want to delete this address?')">
              <s:param name="deleteAddress" value="${address.id}"/>
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
        $('.edit').click(function() {
          form = $('#newAddressForm');
          addressBlock = $(this).parents('.address');
          name = addressBlock.find('.name').text();
          street1 = addressBlock.find('.street1').text();
          street2 = addressBlock.find('.street2').text();
          city = addressBlock.find('.city').text();
          state = addressBlock.find('.state').text();
          pin = addressBlock.find('.pin').text();
          phone = addressBlock.find('.phone').text();
          id = addressBlock.find('.address_id').val();
          form.find("input[type='text'][name='address.name']").val(name);
          form.find("input[type='text'][name='address.line1']").val(street1);
          if (street2) {
            form.find("input[type='text'][name='address.line2']").val(street2);
          }
          form.find("input[type='text'][name='address.city']").val(city);
          form.find("input[type='text'][name='address.state']").val(state);
          form.find("input[type='text'][name='address.pin']").val(pin);
          form.find("input[type='text'][name='address.phone']").val(phone);
          form.find("input[type='hidden'][name='address.id']").val(id);
        });
        $(document).ready(function() {
	        $('.address').hover(
			        function() {
				        $(this).children('.hidden').slideDown(100);
				        $(this).children('.edit').click(function() {
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
	        $('.addressValidation').click(function() {
		        var pincodeRegEx = /^([0-9]{6})$/;
		        var pincode = $('.pincode').val();
		        if (!pincodeRegEx.test(pincode)) {
			        alert("Please enter a valid (6 digit) Pincode.");
			        return false;
		        }
	        });
        });

      </script>


    </div>
    <div class='or'>
      or
    </div>
    <div class='right'>
      <h3>
        Add a new shipping address
      </h3>

      <div class="addressContainer shipping_address">
        <div class="newAddress-errors">
          <s:errors/>
        </div>
        <div class="newAddress-errors alert messages"><s:messages key="generalMessages"/></div>
        <s:form beanclass="mhc.web.action.NewAddressAction" id="newAddressForm">
          <s:hidden name="address.id"/>
          <span class="aster special">(Fields marked * are required.)</span>

          <div class='label'>Name<span class="aster">*</span></div>
          <s:text name="address.name"/>
          <div class='label'>Address Line 1<span class="aster">*</span></div>
          <s:text name="address.line1"/>
          <div class='label'>Address Line 2</div>
          <s:text name="address.line2"/>
          <div class='label'>City<span class="aster">*</span></div>
          <s:text name="address.city"/>
          <div class='label'>State<span class="aster">*</span></div>
          <s:text name="address.state"/>
          <div class='label'>PIN Code<span class="aster">*</span></div>
          <s:text name="address.pin" class="pincode" maxlength="6"/>
          <div class='label'>Phone / Mobile<span class="aster">*</span></div>
          <s:text name="address.phone"/>
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
        /*$('#newAddressForm').ajaxForm({dataType: 'json', success: _saveAddress});

         function _saveAddress(res) {
         if (res.code == '<%--<%=HealthkartResponse.STATUS_REDIRECT%>--%>') {
         $('.addressContainer').hide();
         $('.addressSuccessContainer').show();
         window.location.href = res.data.url;
         } else if (res.code == '<%--<%=HealthkartResponse.STATUS_ERROR%>--%>') {
         $('.newAddress-errors').html(getErrorHtmlFromJsonResponse(res));
         }
         }*/
      </script>


    </div>
  </div>
</s:layout-component>
<s:layout-component name="zopim">
  <jsp:include page="/includes/_zopim.jsp"/>
</s:layout-component>

<s:layout-component name="footer"> </s:layout-component>

</s:layout-render>


