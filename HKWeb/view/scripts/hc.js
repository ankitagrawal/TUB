function CreateXmlHttpObject(){ 
	var objXMLHttp=null
	if (window.XMLHttpRequest){
		objXMLHttp=new XMLHttpRequest()
	}else if (window.ActiveXObject){
		objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
	}
	return objXMLHttp
}

function findPos(obj) {
	var curleft = curtop = 0;
	while (obj) {
		curleft += obj.offsetLeft
		curtop += obj.offsetTop
		obj = obj.offsetParent
	}
	return { left:curleft, top:curtop };
}

/* IE-8 and lower support for trim() in String */
if(typeof String.prototype.trim !== 'function') {
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, ''); 
  }
}


/* Basic Scipts for Suggestion Box */
var selectedIndex = 0;

//Need to set in calling functions.
var list;

function navList(dir){			
	selectedIndex += (dir == "down") ? 1 : -1;
	li = list.getElementsByTagName("li");
	if (selectedIndex < 1) selectedIndex =  li.length;
	if (selectedIndex > li.length) selectedIndex =  1;
	navListItem(selectedIndex);
};

function navListItem(index){	
	selectedIndex = index;
	li = list.getElementsByTagName("li");
	for(var i=0;i<li.length;i++){
		li[i].className = (i==(selectedIndex-1)) ? "selected" : "";
	};
};

/* Needs to be over ride as per requirement*/
/*
function selectList(elm){
	li = list.getElementsByTagName("li");	
	a = li[selectedIndex-1].getElementsByTagName("a")[0];
	var value = a.innerHTML; 
	elm.value = value;
	clearList();
	elm.blur();
};	
*/
function clearList(){
	if(list){
		list.style.display = "none";
		list.innerHTML = "";
		selectedIndex = 0;
	};
};		
	
function getKeyCode(e){
	var code;
	if (!e) var e = window.event;
	if (e.keyCode) code = e.keyCode;
	return code;
};

/* Products */
function suggestProducts(elm, context){ 
	list = document.getElementById("searchList");
	list.style.display = "block";
	var prod = elm.value; 
	var xmlHttpSearchProduct=CreateXmlHttpObject();
	if (xmlHttpSearchProduct==null){
		alert ("Browser does not support HTTP Request")
		return
	} 
	var url= context + "/jsp/ajax/GetSuggestedProducts.jsp"
	url=url+"?product="+escape(prod)
	url=url+"&level1Category="+document.getElementById("level1Category").value;
	url=url+"&sid="+Math.random()
	xmlHttpSearchProduct.onreadystatechange= (function stateChangedForSuggestedProducts(){ 
									if (xmlHttpSearchProduct.readyState==4 || xmlHttpSearchProduct.readyState=="complete"){										
										list.innerHTML = xmlHttpSearchProduct.responseText;
										elm.parentNode.appendChild(list);
										
										elm.onkeypress = function(e){				
											var key = getKeyCode(e);									
											if(key == 13){ // enter
												selectProductFromList(elm);
												selectedIndex = 0;
												return false;
											};	
											
											if(key == 9){  // tab												
												selectedIndex = 0;
												clearList();
											}	
										};
										
										elm.onkeyup = function(e){			
											var key = getKeyCode(e);									
											switch(key){
											case 27:  // esc
												elm.value = "";
												selectedIndex = 0;
												clearList();
												break;				
											case 38: // up
												navList("up");
												break;
											case 40: // down
												navList("down");		
												break;	
											default:
												suggestProducts(elm, context);
												break;			
											};
										};
									} 
								} ) 
	xmlHttpSearchProduct.open("GET",url,true)
	xmlHttpSearchProduct.send(null)
}
function selectProductFromList(elm){
	li = list.getElementsByTagName("li");	
	a = li[selectedIndex-1].getElementsByTagName("a")[0];
	inp = li[selectedIndex-1].getElementsByTagName("input");
	setProduct(inp[0].value, inp[1].value, inp[2].value);
	clearList();
	elm.blur();
};

function setProduct(type, name, id){ 
	if(document.getElementById('searchItem')){
		document.getElementById('searchItem').value = name;
		document.getElementById('itemType').value = type;
	}else if(document.getElementById('product0')){
		document.getElementById('product0').value = name;
	}else if(document.getElementById('prodName')){
		document.getElementById('prodName').value = name;
	}
	clearList();	
}

/* Validate Login Name */
function validateLoginName(login){
	document.getElementById("loginAvailable").innerHTML = "&nbsp;";
	document.getElementById("loginAvailable").className = "";
	var xmlHttp=CreateXmlHttpObject();
	if (xmlHttp==null){
		alert ("Browser does not support HTTP Request")
		return
	} 
	var consultingDoctor = '3';
	var url="../jsp/ajax/ValidateLoginName.jsp"
	url=url+"?login="+escape(login)
	url=url+"&sid="+Math.random()
	xmlHttp.onreadystatechange=(function stateChangedForLoginNameCheck(){ 
									if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){ 
										var resp = xmlHttp.responseText;
										if(login.length == 0)
											document.getElementById("loginAvailable").innerHTML = "&nbsp;";
										else 
											document.getElementById("loginAvailable").innerHTML = resp;
										if(resp.trim() == "Available"){										
											document.getElementById("loginAvailable").className = "available";
										}else if(resp.trim() == "Unavailable"){
											document.getElementById("loginAvailable").className = "unavailable";
										}
									} 
								} ) 
	xmlHttp.open("GET",url,true)
	xmlHttp.send(null)
}