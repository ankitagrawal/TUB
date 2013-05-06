function validateAddressForm() {
	$("#error-row").hide();
	$("#error").html("");
	var form = document.forms["selectAddress"];
	var billingAddressName = form["address.name"].value;
	var billingAddressLine1 = form["address.line1"].value;
	var billingAddressCity = form["address.city"].value;
	var billingAddressState = form["address.state"].value;
	var billingAddressPin = form["pincode"].value;
	var billingAddressPhone = form["address.phone"].value;
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

	if (billingAddressPin == null || billingAddressPin == "")
	{
		alert("Pincode must be filled out");
		return false;
	} else {
		_validatePincode(billingAddressPin);
		if (err) {
			$("#error-row").show();
			return false;
		}
	}
	
	if (billingAddressPhone == null || billingAddressPhone == "") {
		alert("Phone/Mobile must be filled out");
		return false;
	} else {
		_validateMobile(billingAddressPhone);
		if (err) {
			$("#error-row").show();
			return false;
		}
	}
	
}

function _validatePincode(pincode) {
	err = 0;
	var validRegEx = /^[1-9]{1}[0-9]{5}$/;
	if (!validRegEx.test(pincode)) {
		$("#error").append("<br/> Kindly enter a valid  pincode!<br/>");
		err = 1;
	}

}

function _validateMobile(phone) {
	var validRegEx = /^[0-9,+\-]{9,}$/;
	if (!validRegEx.test(phone)) {
		$("#error").append("<br/> Kindly enter a valid phone number!<br/>");
		err = 1;
	}
}

