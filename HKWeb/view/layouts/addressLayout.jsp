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

		$(document).ready(function() {
			$("#cityselect").autocomplete({
				url:"${pageContext.request.contextPath}/core/autocomplete/AutoComplete.action?populateCity=&q="
			});

		$('.error').hide();
			var err = 0;

     		$('.button').click(function() {
                 $('.error').empty();
				err = 0;
				$('.rowText').each(function() {
					if ($(this).attr('id') != 'line2') {
						if ($(this).val().trim() === "") {
							err = 1;
						}
					}
				});
                if(err ==1){
                    $('.error').append("<br/> Kindly Enter all Fields  <br/>");
                }

                if($('#stateselect').val() == ""){
				$('.error').append("<br/> Select State  <br/>");
							err = 1;
				}

				if (err) {
					$('.error').show();
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

			$('#phone').change(function() {
				_validateMobile();
				if (err) {
					$('.error').fadeIn(3000);
				}
			});

			$(document).click(function() {
				$('.error').fadeOut(3000);
				$('.error').empty();

			});

			function _validatePincode() {
				err = 0;
				var validRegEx = /^[1-9]{1}[0-9]{5}$/;
				var pincode = $('#pin').val();
				if (!validRegEx.test(pincode)) {
					$('.error').append("<br/> Kindly enter a valid  pincode!<br/>");
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

			$('#pin').blur(function(event) {
				var pin = $('#pin').val();
				_validatePincode();
				if (err) {
					$('.error').show();
				}
				else {
					$.getJSON($('#populateaddress').attr('href'), {pincode:pin}, function(responseData) {
						if (responseData.code == '<%=HealthkartResponse.STATUS_OK%>') {
							$('#stateselect').val(responseData.data.stateName);
							$('#cityselect').val(responseData.data.cityName);
						}
						else {
                            $('#pin').val("");
							$('#stateselect').val("");
							$('#cityselect').val("");
							$('.error').empty();
                            alert("We don't Service to this pincode, please Enter a valid Pincode or Contact to Customer Care.");
							$('.error').html("<br/>We do not service this pincode.<br/>Please enter a valid Pincode<br/>OR <br/>Contact customer care.<br/><br/>");
							$('.error').show();
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
			font: inherit;
            width: 250px !important;
		}
	</style>

</s:layout-component>
<s:layout-component name="modal"/>
<s:layout-component name="content">
	 <div style="display:none;">
            <s:link beanclass="com.hk.web.action.core.autocomplete.AutoCompleteAction" event="populateAddress" id="populateaddress">
            </s:link>
        </div>
	<div style="margin-top:15px"></div>
	<div class="error" style="background-color:salmon; width:280px; margin-top: 10px; padding: 5px;"></div>
	<div class="clear"></div>
	<div style="margin-top:10px"></div>
	<div class="row" style="padding-left:initial;">
		<s:label class="rowLabel widthInitial newLabel2" name="Name*"/>
		<s:text name="address.name"  class="signUpInputNew2 rowText"/>
	</div>

	<div class="clear"></div>
	<div style="margin-top:10px;"></div>


	<div class="row" style="padding-left:initial;">
		<s:label class="rowLabel widthInitial newLabel2" name="Address Line1*"/>
		<s:text name="address.line1" class="signUpInputNew2 rowText" id="line1"/>
	</div>

	<div class="clear"></div>
	<div style="margin-top:10px;"></div>


	<div class="row" style="padding-left:initial;">
		<s:label class="rowLabel widthInitial newLabel2" name="Address Line2"/>
		<s:text class="signUpInputNew2" name="address.line2" id="line2" style="width: 250px !important;"/>
	</div>

	<div class="clear"></div>
	<div style="margin-top:10px;"></div>


	<div class="row" style="padding-left:initial;">
		<s:label class="rowLabel widthInitial newLabel2" name="Pincode*"/>
		<s:text name="address.pincode" class="signUpInputNew2 rowText" id="pin" maxlength="6"/>
	</div>

	<div class="clear"></div>
	<div style="margin-top:10px"></div>

	<div class="row" style="padding-left:initial;">
		<s:label class="rowLabel widthInitial newLabel2" name="City*"/>
		<s:text style="width:300px;float: left;padding-top: 0;padding-bottom: 0;font: inherit;" name="address.city" class="signUpInputNew2 rowText" id="cityselect" title="enter city name"/>
	</div>

	<div class="clear"></div>
	<div style="margin-top:10px"></div>

	<div class="row" style="padding-left:initial;">
		<s:label class="rowLabel widthInitial newLabel2" name="State*"/>

		<s:select class="signUpInputNew2" name="address.state" id="stateselect" style="width:260px;float: left;padding-top: 0;padding-bottom: 0;font: inherit;" >
			<s:option value=""> ----------------- Select State --------------- </s:option>
			<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="stateList"
			                           value="name" label="name"/>
		</s:select>
	</div>


	<div class="clear"></div>
	<div style="margin-top:10px"></div>

	<div class="row" style="padding-left:initial;">
		<s:label class="rowLabel widthInitial newLabel2" name="Phone/Mobile*"/>
		<s:text name="address.phone" class="signUpInputNew2 rowText" id="phone"/>
	</div>
        <div class="clear"></div>
        <br>
</s:layout-component>

</body>
</html>
</s:layout-definition>