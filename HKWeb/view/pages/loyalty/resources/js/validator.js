function validateAddressForm() {
	var form = document.forms["selectAddressForm"];
	var billingAddressName = form["address.name"].value;
	var billingAddressLine1 = form["address.line1"].value;
	var billingAddressCity = form["address.city"].value;
	var billingAddressState = form["address.state"].value;
	var billingAddressPin = form["BillingAddressForm"]["pin"].value;
	var billingAddressPhone = form["BillingAddressForm"]["address.phone"].value;
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
	if (billingAddressPhone == null || billingAddressPhone == "") {
		alert("Phone/Mobile must be filled out");
		return false;
	} else {
		_validateMobile(billingAddressPhone);
	}
}

function _validatePincode() {
	err = 0;
	var validRegEx = /^[1-9]{1}[0-9]{5}$/;
	var pincode = $('#pin').val();
	if (!validRegEx.test(pincode)) {
		$('#error').append("<br/> Kindly enter a valid  pincode!<br/>");
		err = 1;
	}

}

function _validateMobile(phone) {
	var validRegEx = /^[0-9,+\-]{9,}$/;
	if (!validRegEx.test(phone)) {
		$('#error').append("<br/> Kindly enter a valid phone number!<br/>");
		err = 1;
	}
}

$('#pin').blur(function(event) {
	var pin = $('#pin').val();
	_validatePincode();
	if (err) {
		$('#error').show();
	} else {
		$.getJSON($('#populateaddress').attr('href'), {pincode:pin}, function(responseData) {
			if (responseData.code == '<%=HealthkartResponse.STATUS_OK%>') {
				$('#stateSelect').val(responseData.data.stateName);
				$('#citySelect').val(responseData.data.cityName);
			}
			else {
                $('#pin').val("");
				$('#stateSelect').val("");
				$('#citySelect').val("");
				$('#error').empty();
                alert("We don't Service to this pincode, please Enter a valid Pincode or Contact to Customer Care.");
				$('#error').html("<br/>We do not service this pincode.<br/>Please enter a valid Pincode<br/>OR <br/>Contact customer care.<br/><br/>");
				$('#error').show();
			}
		});
	}
});
