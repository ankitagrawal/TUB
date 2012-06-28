<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.UserManageAddressAction" var="umaa"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Account</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">

    <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction">
      <c:set var="address" value="${umaa.address}"/>
      <c:choose>
        <c:when test="${address ==null}">
          <h4 class="strikeline">New Address</h4>
        </c:when>
        <c:otherwise>
          <h4 class="strikeline">Edit Address</h4>
        </c:otherwise>
      </c:choose>


      <div style="margin-top:15px"></div>
      <div class="error" style="background-color:salmon; margin-top: 20px; padding: 5px;"></div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <s:hidden name="address.id" value="${address.id}"/>

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
        <s:select name="address.state"
                  style="width:310px;float: left;padding-top: 0;padding-bottom: 0;margin-left: 30px;font: inherit;">
          <c:forEach items="<%=StateList.stateList%>" var="state">
            <s:option value="${state}">${state}</s:option>
          </c:forEach>
        </s:select>
          <%--<s:text name="address.state" class="rowText"/>--%>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Pincode*"/>
        <s:text name="address.pin" class="rowText" id="pin"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Phone/Mobile*"/>
        <s:text name="address.phone" value="${address.phone}" class="rowText" id="phone"/>
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
      var validRegEx = /^[1-9]{1}[0-9]{5}$/;
      var pincode = $('#pin').val();
      if (!validRegEx.test(pincode)) {
        $('.error').append("<br/> Kindly enter a valid pincode!<br/>");
        err = 1;
      }
    }

    function _validateMobile() {
      var phone = $('#phone').val();
      var validRegEx = /^[0-9,+\-]{9,}$/;
      if (!validRegEx.test(phone)) {
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