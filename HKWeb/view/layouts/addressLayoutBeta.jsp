<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 17, 2013
  Time: 5:49:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.hk.domain.user.Address" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
<s:layout-component name="htmlHead">
  <script type="text/javascript">

    $(document).ready(function () {
      $("#cityselect").autocomplete({
        url: "${pageContext.request.contextPath}/core/autocomplete/AutoComplete.action?populateCity=&q="
      });

      $('.error').hide();
      var err = 0;

      $('.continue').click(function () {
        $('.error').empty();
        err = 0;
        $('.rowText').each(function () {
          if ($(this).attr('id') != 'line2') {
            if ($(this).val().trim() === "") {
              err = 1;
            }
          }
        });
        if (err == 1) {
          $('.error').append('<div class="errorContainer">Please fix the following errors:</div><ol class="errorList"><li class="errorMessage">Please Enter All fields</li></ol>');
        }

        if ($('#stateselect').val() == "") {
          if (err == 1) {
            $('.errorList').append('<li class="errorMessage">Select State</li>');
          } else {
            $('.error').append('<div class="errorContainer">Please fix the following errors:</div><ol class="errorList"><li class="errorMessage">Select State</li></ol>');
          }
          err = 1;
        }

        if (err) {
          $('.error').show();
          $('body').scrollTop(100);
          return false;
        }
        _validatePincode();
        _validateMobile();

        if (err) {
          $('.error').fadeIn(3000);
          $('.error').empty();
          return false;
        } else {
          return true;
        }
      });

      $('#phone').change(function () {
        _validateMobile();
        if (err) {
          $('.error').fadeIn(3000);
        }
      });

      $(document).click(function () {
        $('.error').fadeOut(3000);
        $('.error').empty();
      });

      function _validatePincode() {
        err = 0;
        var validRegEx = /^[1-9]{1}[0-9]{5}$/;
        var pincode = $('#pin').val();
        if (!validRegEx.test(pincode)) {
          if (err == 1) {
            $('.errorList').append('<li class="errorMessage">Kindly enter a valid  pincode!</li>');
          } else {
            $('.error').append('<div class="errorContainer">Please fix the following errors:</div><ol class="errorList"><li class="errorMessage">Kindly enter a valid  pincode!</li></ol>');
          }
          err = 1;
        }

      }

      function _validateMobile() {
        var phone = $('#phone').val();
        var validRegEx = /^[0-9,+\-]{9,}$/;
        if (!validRegEx.test(phone)) {
          if (err == 1) {
            $('.errorList').append('<li class="errorMessage">Kindly enter a valid phone number!</li>');
          } else {
            $('.error').append('<div class="errorContainer">Please fix the following errors:</div><ol class="errorList"><li class="errorMessage">Kindly enter a valid phone number!</li></ol>');
          }
          err = 1;
        }
      }

      $('#pin').blur(function (event) {
        var pin = $('#pin').val();
        _validatePincode();
        if (err) {
          $('.error').show();
          $('body').scrollTop(100);
        }
        else {
          $.getJSON($('#populateaddress').attr('href'), {pincode: pin}, function (responseData) {
            if (responseData.code == '<%=HealthkartResponse.STATUS_OK%>') {
              $('#stateselect').val(responseData.data.stateName);
              $('#cityselect').val(responseData.data.cityName);
            }
            else {
              $('#pin').val("");
              $('#stateselect').val("");
              $('#cityselect').val("");
              $('.error').empty();
              //alert("We don't Service to this pincode, please Enter a valid Pincode or Contact to Customer Care.");
              $('.error').append('<div class="errorContainer">Please fix the following errors:</div><ol class="errorList"><li class="errorMessage">We do not service this pincode. Please Enter Valid Pinocde</li><li class="errorMessage">OR Contact customer care.</li></ol>');
              $('.error').show();
              $('body').scrollTop(100);
            }
          });
        }
      });

    });
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
  </style>

</s:layout-component>

<s:layout-component name="modal"/>

<s:layout-component name="content">

  <div style="display:none;">
    <s:link beanclass="com.hk.web.action.core.autocomplete.AutoCompleteAction" event="populateAddress"
            id="populateaddress">
    </s:link>
  </div>

  <div class="error"></div>

  <div class="row">
    <s:label class="rowLabel" name="Name*"/>
    <s:text name="address.name" class="rowText"/>
  </div>

  <div class="clear"></div>

  <div class="row">
    <s:label class="rowLabel" name="Phone/Mobile*"/>
    <s:text name="address.phone" class="rowText" id="phone"/>
  </div>

  <div class="clear"></div>

  <div class="row">
    <s:label class="rowLabel" name="Address Line1*"/>
    <s:text name="address.line1" class="rowText" id="line1"/>
  </div>

  <div class="clear"></div>

  <div class="row">
    <s:label class="rowLabel" name="Address Line2"/>
    <s:text name="address.line2" class="rowText" id="line2"/>
  </div>

  <div class="clear"></div>

  <div class="row">
    <s:label class="rowLabel" name="Pincode*"/>
    <s:text name="address.pincode" class="rowText" id="pin" maxlength="6"/>
  </div>

  <div class="clear"></div>

  <div class="row">
    <s:label class="rowLabel" name="City*"/>
    <s:text name="address.city" class="rowText" id="cityselect" title="enter city name"/>
  </div>

  <div class="clear"></div>

  <div class="row">
    <s:label class="rowLabel" name="State*"/>
    <s:select name="address.state" id="stateselect" style="width:315px;">
      <s:option value=""> --------- Select State --------- </s:option>
      <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="stateList"
                                 value="name" label="name"/>
    </s:select>
  </div>

  <div class="clear"></div>

</s:layout-component>
</s:layout-definition>