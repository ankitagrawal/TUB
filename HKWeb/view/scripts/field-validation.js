var WEIGHT_LOWER=29;
var WEIGHT_HIGHER = 250;
var HEIGHT_LOWER=100;
var HEIGHT_HIGHER =200;

function calBMI(patientWeight,patientHeight,bmi){
	$(bmi).value = "";
	if($(patientWeight).value != '' && $(patientWeight).value > WEIGHT_HIGHER){
           alert("Out of Range - Please re-check values.");
           $(patientWeight).value ='';
           $(patientWeight).focus();
    }else if($(patientWeight).value != '' && $(patientWeight).value < WEIGHT_LOWER){
	    	alert("Out of Range - Please re-check values.");
	        $(patientWeight).value ='';
	        $(patientWeight).focus();
    }
	
	if($(patientHeight).value != '' && $(patientHeight).value > HEIGHT_HIGHER){
        alert("Out of Range - Please re-check values.");
        $(patientHeight).value ='';
        $(patientHeight).focus();
	 }else if($(patientHeight).value != '' && $(patientHeight).value < HEIGHT_LOWER){
		    	alert("Out of Range - Please re-check values.");
		    	$(patientHeight).value ='';
		    	$(patientHeight).focus();
	 }
	 
	var w = $(patientWeight).value;
	var h = $(patientHeight).value;		
	if(w !='' && h != ''){
		var hInMSqr = ((h/100)*(h/100));
		$(bmi).value = (w/hInMSqr).toFixed(2) ;
	}	
}

function checkLimit(l, h, elm){ 
	if(parseFloat(l) > parseFloat(elm.value) || parseFloat(h) < parseFloat(elm.value)){
		alert("Out of range");
		elm.value = "";
	}		
}

function isDouble(elm){
   var strString=elm.value;
   var strValidChars = "0123456789.";
   var strChar;
   var blnResult = true;
   var resultString;
   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         alert("Invalid value");
         elm.select();		                    	
	  	 elm.focus();	
	  					 				
	  	 resultString = strString.split(strString.charAt(i));
	  					 				 
	  	 elm.value= resultString[0];    
         }
      }
   return blnResult;
}
   
   
function isValidMMYY(elm){
   var strString=elm.value;
   var strValidChars = "0123456789/";
   var strChar;
   var blnResult = true;
   var resultString;
   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         alert("Invalid value");
         elm.select();		                    	
	  	 elm.focus();	
	  					 				
	  	 resultString = strString.split(strString.charAt(i));
	  					 				 
	  	 elm.value= resultString[0];    
         }
      }
   return blnResult;
}
   
  
function isNumber(elm){
   var strString=elm.value;
   var strValidChars = "0123456789";
   var strChar;
   var blnResult = true;
   var resultString;
   if (strString.length == 0) 
   		return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         alert("Invalid value");
         elm.select();		                    	
	  	 elm.focus();	
	  					 				
	  	 resultString = strString.split(strString.charAt(i));
	  					 				 
	  	 elm.value= resultString[0];    
         }
      }
   return blnResult;
}
  
function isValidMonth(elm){
   	var inMonth = elm.value;
    if (inMonth.length < 3){ 
    alert("Invalid month");
    elm.value="";
    return false;
    }
    var myRegExp=inMonth.toLowerCase();
	var months = "jan feb mar apr may jun jul aug sep oct nov dec";
	var matchPos = months.search(myRegExp);

	if(matchPos != -1){
	}
	else{
	alert("Invalid month");
	elm.focus();
	elm.select();
	elm.value="";	
	}
}
   
function isValidMMMYYYY(elm){
	if(elm.value != ""){
		var elmAr = elm.value.split('/');
		var myRegExp = elmAr[0].toLowerCase();
		var months = "jan feb mar apr may jun jul aug sep oct nov dec";
		var matchPos = months.search(myRegExp);
	
		if(matchPos != -1){
		}
		else{
			alert("Invalid month");
			elm.value = '';
			elm.focus();
		}
		
		if(1900 > parseFloat(elmAr[1]) || 2200 < parseFloat(elmAr[1])){
			alert("Out of range");
			elm.value = '';
			elm.focus();
		}
	}
}


function isValidDDMMMYYYY(elm){
	if(elm.value != ""){
		var elmAr = elm.value.split('/');
		var date = elmAr[0].toLowerCase();
		if(0 > parseFloat(elmAr[0]) || 31 < parseFloat(elmAr[0])){
			alert("Invalid date");
			elm.value = '';
			elm.focus();
		}
		var myRegExp = elmAr[1].toLowerCase();
		var months = "jan feb mar apr may jun jul aug sep oct nov dec";
		var matchPos = months.search(myRegExp);
	
		if(matchPos != -1){
		}
		else{
			alert("Invalid month");
			elm.value = '';
			elm.focus();
		}
		
		if(2000 > parseFloat(elmAr[2]) || 2200 < parseFloat(elmAr[2])){
			alert("Out of range");
			elm.value = '';
			elm.focus();
		}
	}
}

function isValidDDMMYYYY(elm){
	if(elm.value != ""){
		var elmAr = elm.value.split('/');
		var date = elmAr[0].toLowerCase();
		if(0 > parseFloat(elmAr[0]) || 31 < parseFloat(elmAr[0])){
			alert("Invalid date");
			elm.value = '';
			elm.focus();
		}
		if(0 > parseFloat(elmAr[1]) || 12 < parseFloat(elmAr[1])){
			alert("Invalid month");
			elm.value = '';
			elm.focus();
		}
		
		if(1900 > parseFloat(elmAr[2]) || 2100 < parseFloat(elmAr[2])){
			alert("Out of range");
			elm.value = '';
			elm.focus();
		}
	}
}


function validateMobile(elm){
	if(elm.length < 10){
		alert("Mobile number must be 10 digit.")
		return false;
	}
}

function validEmail(str) {
	var at="@"
	var dot="."
	var lat=str.indexOf(at)
	var lstr=str.length
	var ldot=str.indexOf(dot)
	if (str.indexOf(at)==-1){
	   alert("Invalid E-mail ID")
	   return false
	}

	if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
	   alert("Invalid E-mail ID")
	   return false
	}

	if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
	    alert("Invalid E-mail ID")
	    return false
	}

	 if (str.indexOf(at,(lat+1))!=-1){
	    alert("Invalid E-mail ID")
	    return false
	 }

	 if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
	    alert("Invalid E-mail ID")
	    return false
	 }

	 if (str.indexOf(dot,(lat+2))==-1){
	    alert("Invalid E-mail ID")
	    return false
	 }
	
	 if (str.indexOf(" ")!=-1){
	    alert("Invalid E-mail ID")
	    return false
	 }

	 return true					
}