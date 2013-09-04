<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.UserManageAddressAction" var="umaa"/>
<s:layout-render name="/layouts/defaultBeta.jsp">
<s:layout-component name="heading">Your Account</s:layout-component>
<s:layout-component name="lhsContent">
  <jsp:include page="myaccount-navBeta.jsp"/>
</s:layout-component>
<c:set var="countryId" value="80"/>
<s:layout-component name="rhsContent">
       <c:choose>
         <c:when test="${umaa.address ==null}">
           <h4 class="strikeline">New Address</h4>
         </c:when>
         <c:otherwise>
           <h4 class="strikeline">Edit Address</h4>
         </c:otherwise>
       </c:choose>
     <%--<script type="text/javascript">--%>
      <%--$(document).ready(function(){--%>
          <%--$("#cityselect").autocomplete({--%>
              <%--url:"${pageContext.request.contextPath}/core/autocomplete/AutoComplete.action?populateCity=&q="--%>
          <%--});--%>

          <%--$('.error').hide();--%>
          <%--var err = 0;--%>

          <%--$('.button').click(function() {--%>
              <%--$('.error').empty();--%>
              <%--err = 0;--%>
              <%--$('.rowText').each(function() {--%>
                  <%--if ($(this).attr('id') != 'line2') {--%>
                      <%--if ($(this).val().trim() === "") {--%>
                          <%--err = 1;--%>
                      <%--}--%>
                  <%--}--%>
              <%--});--%>
              <%--if(err ==1){--%>
                  <%--$('.error').append("<br/> Kindly Enter all Fields  <br/>");--%>
              <%--}--%>

              <%--if($('#stateselect').val() == ""){--%>
                  <%--$('.error').append("<br/> Select State  <br/>");--%>
                  <%--err = 1;--%>
              <%--}--%>

              <%--if (err) {--%>
                  <%--$('.error').show();--%>
                  <%--return false;--%>
              <%--}--%>
              <%--_validatePincode();--%>
              <%--_validateMobile();--%>

              <%--if (err) {--%>
                  <%--$('.error').fadeIn(3000);--%>
                  <%--$('.error').empty();--%>
                  <%--return false;--%>
              <%--} else {--%>
                  <%--return true;--%>
              <%--}--%>
          <%--});--%>

          <%--$('#phone').change(function() {--%>
              <%--_validateMobile();--%>
              <%--if (err) {--%>
                  <%--$('.error').fadeIn(3000);--%>
              <%--}--%>
          <%--});--%>

          <%--$(document).click(function() {--%>
              <%--$('.error').fadeOut(3000);--%>
              <%--$('.error').empty();--%>

          <%--});--%>

          <%--function _validatePincode() {--%>
              <%--err = 0;--%>
              <%--var validRegEx = /^[1-9]{1}[0-9]{5}$/;--%>
              <%--var pincode = $('#pin').val();--%>
              <%--if (!validRegEx.test(pincode)) {--%>
                  <%--$('.error').append("<br/> Kindly enter a valid  pincode!<br/>");--%>
                  <%--err = 1;--%>
              <%--}--%>

          <%--}--%>

          <%--function _validateMobile() {--%>
              <%--var phone = $('#phone').val();--%>
              <%--var validRegEx = /^[0-9,+\-]{9,}$/;--%>
              <%--if (!validRegEx.test(phone)) {--%>
                  <%--$('.error').append("<br/> Kindly enter a valid phone number!<br/>");--%>
                  <%--err = 1;--%>
              <%--}--%>
          <%--}--%>

          <%--$('#pin').blur(function(event) {--%>
              <%--var pin = $('#pin').val();--%>
              <%--_validatePincode();--%>
              <%--if (err) {--%>
                  <%--$('.error').show();--%>
              <%--}--%>
              <%--else {--%>
                  <%--$.getJSON($('#populateaddress').attr('href'), {pincode:pin}, function(responseData) {--%>
                      <%--if (responseData.code == '<%=HealthkartResponse.STATUS_OK%>') {--%>
                          <%--$('#stateselect').val(responseData.data.stateName);--%>
                          <%--$('#cityselect').val(responseData.data.cityName);--%>
                      <%--}--%>
                      <%--else {--%>
                          <%--$('#pin').val("");--%>
                          <%--$('#stateselect').val("");--%>
                          <%--$('#cityselect').val("");--%>
                          <%--$('.error').empty();--%>
                          <%--alert("We don't Service to this pincode, please Enter a valid Pincode or Contact to Customer Care.");--%>
                          <%--$('.error').html("<br/>We do not service this pincode.<br/>Please enter a valid Pincode<br/>OR <br/>Contact customer care.<br/><br/>");--%>
                          <%--$('.error').show();--%>
                      <%--}--%>
                  <%--});--%>
              <%--}--%>
          <%--});--%>
         <%--form = $('#addressForm');--%>
                        <%--form.find("input[type='text'][name='address.name']").val(${umaa.address.name});--%>
                        <%--form.find("input[type='text'][name='address.line1']").val(${umaa.address.line1});--%>
                        <%--if (${umaa.address.line2!=null or fn:length(umaa.address.line2)>0}) {--%>
                            <%--form.find("input[type='text'][name='address.line2']").val(${umaa.address.line2});--%>
                        <%--}--%>
                        <%--form.find("input[type='text'][name='address.city']").val(${umaa.address.city});--%>
                        <%--form.find("[name='address.state']").val(${umaa.address.state});--%>
                        <%--form.find("input[type='text'][name='address.pincode']").val(${umaa.address.pincode.pincode});--%>
                        <%--form.find("input[type='text'][name='address.phone']").val(${umaa.address.phone});--%>
                    <%--});--%>
  <%--</script>--%>
  <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction" id="addressForm">
      <s:layout-render name="/layouts/addressLayoutBeta.jsp" />
      <s:hidden name="countryId" value="${countryId}"/>
      <s:submit name="saveAddress" value="Save Address" class="btn btn-blue continue" />
  </s:form>
</s:layout-component>
</s:layout-render>