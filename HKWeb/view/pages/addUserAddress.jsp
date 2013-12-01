<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.MyAccountAction" var="maa"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Account</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction" id="newAddressForm">
      <h4 class="strikeline">New Address</h4>

      <div style="margin-top:15px"></div>
      <div class="error" style="background-color:salmon; margin-top: 20px; padding: 5px;"></div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <s:hidden name="address.id"/>

      <div class="row">
        <s:label class="rowLabel" name="Name*"/>
        <s:text name="address.name" class="rowText"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Address Line1*"/>
        <s:text name="address.line1" class="rowText"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Address Line2"/>
        <s:text name="address.line2" class="rowText" id="line2"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="City*"/>
        <s:text name="address.city" class="rowText"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="State*"/>
        <s:text name="address.state" class="rowText"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Pincode*"/>
        <s:text name="address.pincode.pincode" class="rowText" id="pin"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Phone/Mobile*"/>
        <input type="text" class="rowText" value="+91-" style="width: 40px; border-right-width: 0;"
               readonly="readonly"/>
        <s:text name="address.phone" class="rowText" id="phone"
                style="margin-left: 0; border-left-width: 0; width: 250px;"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <s:submit name="saveAddress" value="Save" class="button" style="left: 50px;"/>
    </s:form>
  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myAddressesLink').style.fontWeight = "bold";
  };
  $(document).ready(function() {
    //    $('error').append("<strong>Please fix the following errors:</strong>");
    $('.error').hide();
    var err = 0;
    $('.button').click(function() {
      err = 0;
      $('.rowText').each(function() {
        if ($(this).attr('id') != 'line2') {
          if ($(this).val().trim() === "") {
            alert("Kindly fill all the fields specified!");
            err = 1;
          }
        }
        if (err) {
          return false;
        }
      });
      _validatePincode();
      _validateMobile();

      if (err) {
        $('.error').fadeIn();
        return false;
      } else {
        return true;
      }
    });
    //					var regEx = /^[a-z0-9_\+-]+(\.[a-z0-9_\+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*\.([a-z]{2,4})$/;
    //					var emailId = $('.emailId').val();
    //					if (!emailRegEx.test(emailId)) {
    //						alert("Enter correct email address.");
    //						return false;
    //					}

    $('#pin').change(function() {
      _validatePincode();
      if (err) {
        $('.error').fadeIn();
      }
    });

    $('#phone').change(function() {
      _validateMobile();
      if (err) {
        $('.error').fadeIn();
      }
    });

    $(document).click(function() {
      $('.error').fadeOut();
      $('.error').empty();
    });

    function _validatePincode() {
      var validRegEx = /[0-9]*/;
      var pincode = $('#pin').val();
      if (!validRegEx.test(pincode) || pincode.length != 6) {
        $('.error').append("<br/> Kindly enter a valid pincode!<br/>");
        err = 1;
      }
    }

    function _validateMobile() {
      var phone = $('#phone').val();
      if (phone.length < 8 || phone.length > 11) {
        $('.error').append("<br/> Kindly enter a valid phone number!<br/>");
        err = 1;
      }
    }
  });
</script>
<style type="text/css">
  .row {
    margin-top: 0;
    float: left;
    margin-left: 0;
    padding-top: 2px;
    padding-left: 26px;
  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    padding-left: 5px;
    width: 150px;
    height: 24px;
    margin-top: 5px;
    font-weight: bold;
  }

  .rowText {
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 30px;
    font: inherit;
  }
</style>