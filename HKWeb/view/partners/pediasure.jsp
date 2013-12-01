<style>
#main-content-right{
border: solid green 0px;
width:300px;
height:130px;
float:left;
position:relative;
left:22px;
}


#top-img{
border: solid red 0px;
float:left;
width:368px;
height:18px;
background:url('http://farasbee.com/pediasure/form_top2.png') no-repeat;
}

#mid-img{
border: solid red 0px;
float:left;
width:368px;
height:330px;
margin-top:-3px;
background:url('http://farasbee.com/pediasure/form_mid2.png') repeat-y;
}


#captchaimg{
text-align:center;
clear:both;
}


#subinput{
background:url('http://farasbee.com/pediasure/submit.jpg') no-repeat;
width:170px;
height:40px;
border:0px;
}

#bottom-img{
background:url('http://farasbee.com/pediasure/form_bot2.png') no-repeat;
width:368px;
height:30px;
float:left;
}
</style>
<script language="javascript" type="text/javascript">

<!--
function isNumberKey(evt)
{
	 var charCode = (evt.which) ? evt.which : event.keyCode
	 if (charCode > 31 && (charCode < 48 || charCode > 57))
	    return false;

	 return true;
}
//-->

function validate()
{
	var name_str=document.enquiry.name.value;
	var security=document.enquiry.security_code.value;
        var phone = document.enquiry.contactno.value;
	var phone_length=phone.length;

	var email_id=document.enquiry.emailid.value;
	var atpos=email_id.indexOf("@");
	var dotpos=email_id.lastIndexOf(".");
	
	var iChars = "*|,\":<>[]{}`\';()@&$#%0123456789";

	if (document.enquiry.name.value=="Name...") {
		alert("Please Enter your Name");
		return false;
	}

	for (var i = 0; i < name_str.length; i++) {
		if (iChars.indexOf(name_str.charAt(i)) != -1){
			alert ("Name contains special characters or number!"); 
			return false;
		}
	}
	
 	if(document.enquiry.emailid.value=="Email...") {
		alert("Please Enter your Email");
		return false;
	}

	if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email_id.length)
	{
		alert("Not a valid e-mail address");
	  	return false;
	}
	
	if (document.enquiry.contactno.value=="Phone...") {
		alert("Please Enter your Phone number");
		return false;
	}
	
	
	if(isNaN(phone)||phone.indexOf(" ")!=-1)
	{
		alert("Enter numeric value")
		return false; 
	}
	
	if ((phone_length < 10) || (phone_length > 10) )
	{
		alert("Enter 10 characters");
		return false;
	}
	  
        if (document.enquiry.country.value=="City...") {
		alert("Please Enter your City");
		return false;
	}
        if (document.enquiry.subject.value=="Subject...") {
		alert("Please Enter Subject");
		return false;
	}
	if (document.getElementById('message').value=="Message...")
	{
		alert("Please Enter Message");
		return false;
	}	
	if (security=="Enter 5 digit code here..."){
		alert("Please fill Captcha");
		return false;
	}
	else
	{
		return true;
	}
}
</script>
<div id="main-content-right">
			
			<div id="top-img">				
			</div>
			
			<div id="mid-img">
				
				<div id="form-content"> 
					<span style="color:#9E1E99; margin:5px 0 0 25px;;font-size:13px;text-align:center;font-weight:bold;font-family:Arial,Helvetica,sans-serif;"> &nbsp;Register Now For A Free Nutrition Counseling<br>  &nbsp;&nbsp;For Your Child With Nutrition Experts from PediaSure.
					</span>
                        
					<div style="padding-left:80px;position:relative; top:20px; font-size:12px;" id="form">
						<form id="enquiry" name="enquiry" method="post" action="http://farasbee.com/pediasure/contact.php">
									<span style="color:#43156F;font-family:Arial,Helvetica,sans-serif;font-size:12px;float:left;font-weight:bold">Name:</span>	<input type="text" onfocus="if(this.value=='Name...') this.value='';" onblur="if(this.value=='') this.value='Name...';" id="name" value="Name..." style="float:left;margin-left:14px" name="name"><br><br><br>	
									<span style="color:#43156F;font-family:Arial,Helvetica,sans-serif;font-size:12px;float:left;font-weight:bold">Email:</span> <input type="text" onfocus="if(this.value=='Email...') this.value='';" onblur="if(this.value=='') this.value='Email...';" value="Email..." id="emailid" style="float:left;margin-left:14px" name="emailid"><br><br><br>
									<span style="color:#43156F;font-family:Arial,Helvetica,sans-serif;font-size:12px;float:left;font-weight:bold">Phone:</span><input type="text" onfocus="if(this.value=='Phone...') this.value='';" onblur="if(this.value=='') this.value='Phone...';" onkeypress="return isNumberKey(event)" value="Phone..." id="contactno" style="float:left;margin-left:10px" name="contactno"><br><br><br>
									<span style="color:#43156F;font-family:Arial,Helvetica,sans-serif;font-size:12px;float:left;font-weight:bold">City:</span><input type="text" onfocus="if(this.value=='City...') this.value='';" onblur="if(this.value=='') this.value='City...';" value="City..." id="country" style="float:left;margin-left:25px" name="country">
									<div id="captchaimg"> <img alt="captcha" src="http://farasbee.com/pediasure/CaptchaSecurityImages.php?width=100&amp;height=40&amp;characters=5"> </div> 
									<input type="text" onfocus="if(this.value=='Enter 5 digit code here...') this.value='';" onblur="if(this.value=='') this.value='Enter 5 digit code here...';" value="Enter 5 digit code here..." name="security_code" id="security_code">
							<div id="sub">
							<input type="submit" onclick="return validate();" value="" id="subinput"> 
							</div>
						</form>
					</div>
					</div>
					</div>
					<div id="bottom-img">
					</div>
					</div>=