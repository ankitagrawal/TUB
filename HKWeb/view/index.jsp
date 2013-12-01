<%--
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="mhc.pojo.HHDProduct"%>
<%@page import="mhc.servlet.action.HHDProductAction"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>HealthKart: Home Health Devices Online | Buy BP Monitors,
CPAP, Nebulizer, Diabetes Meters India</title>
<meta name="description="
	content="HealthKart.com offers Diabetes products online. Buy BP Monitors, CPAP, Nebulizers, Diabetes Meters, Blood Glucose meters, Thermometers and many other quality products online. Order now from anywhere in India." />

<jsp:include page="/jsp/includes.jsp"></jsp:include>
<%
	DecimalFormat singledf = new DecimalFormat("#");
%>
<script type="text/javascript">

function goToAbout(){
	window.location.href = "<%=request.getContextPath()%>/jsp/aboutCompany.jsp";
}
function goToContact(){
	window.location.href = "<%=request.getContextPath()%>/jsp/aboutContactUs.jsp";
}

function getBMIPrompt(){
	var bmi = document.getElementById("bmi").value;
	if(bmi != ""){
		var xmlHttp=CreateXmlHttpObject();
		if (xmlHttp==null){
			alert ("Browser does not support HTTP Request")
			return
		} 
		var url="<%=request.getContextPath()%>/jsp/ajax/GetBMIPrompt.jsp"
		url=url+"?bmi="+bmi
		url=url+"&sid="+Math.random()
		xmlHttp.onreadystatechange = (function afterResponse(){
										if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete"){ 
											document.getElementById("bmiState").innerHTML = xmlHttp.responseText; 
										} })
		xmlHttp.open("GET",url,true)
		xmlHttp.send(null)
	}
}

function computeBMI(wt, ht, bmi){
	document.getElementById(bmi).value = "";
	
	var w = document.getElementById(wt).value;
	var h = document.getElementById(ht).value;		
	if(w !='' && h != ''){
		var hInMSqr = ((h/100)*(h/100));
		document.getElementById(bmi).value = (w/hInMSqr).toFixed(2) ;
	}	
}
</script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/otherScripts/jquery-1.3.1.min.js"></script>
<script type="text/javascript">

$(document).ready(function() {		
	
	//Execute the slideShow
	slideShow();

});

function slideShow() {

	//Set the opacity of all images to 0
	$('#gallery a').css({opacity: 0.0});
	
	//Get the first image and display it (set it to full opacity)
	$('#gallery a:first').css({opacity: 1.0});
	
	//Set the caption background to semi-transparent
	$('#gallery .caption').css({opacity: 0.7});

	//Resize the width of the caption according to the image width
	$('#gallery .caption').css({width: $('#gallery a').find('img').css('width')});
	
	//Get the caption of the first image from REL attribute and display it
	//$('#gallery .content').html($('#gallery a:first').find('img').attr('rel'))
	//.animate({opacity: 0.7}, 400);
	
	//Call the gallery function to run the slideshow, 6000 = change to next image after 6 seconds
	setInterval('gallery()',3000);
	
}

function gallery() {
	
	//if no IMGs have the show class, grab the first image
	var current = ($('#gallery a.show')?  $('#gallery a.show') : $('#gallery a:first'));

	//Get next image, if it reached the end of the slideshow, rotate it back to the first image
	var next = ((current.next().length) ? ((current.next().hasClass('caption'))? $('#gallery a:first') :current.next()) : $('#gallery a:first'));	
	
	//Get next image caption
	//var caption = next.find('img').attr('rel');	
	
	//Set the fade in effect for the next image, show class has higher z-index
	next.css({opacity: 0.0})
	.addClass('show')
	.animate({opacity: 1.0}, 1000);

	//Hide the current image
	current.animate({opacity: 0.0}, 1000)
	.removeClass('show');
	
	//Set the opacity to 0 and height to 1px
	$('#gallery .caption').animate({opacity: 0.0}, { queue:false, duration:0 }).animate({height: '1px'}, { queue:true, duration:300 });	
	
	//Animate the caption, opacity to 0.7 and heigth to 100px, a slide up effect
	$('#gallery .caption').animate({opacity: 0.7},100 ).animate({height: '100px'},500 );
	
	//Display the content
	//$('#gallery .content').html(caption);
}

</script>
</head>

<body class="yui-skin-sam">
<div class="pg-container">
<div>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/jsp/pg-hdr.jsp"></jsp:include></td>
	</tr>
</table>
</div>

<div class="pg-body">
<table width="980" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td colspan="2"><jsp:include page="/jsp/hhd-menu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td width="66%" valign="top">
		<table width="100%" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td width="100%" valign="top" class="">
				<div id="gallery"><a
					href="jsp/hhdProductDetails.jsp?itemCode=73" class="show"> <img
					src="<%=request.getContextPath()%>/images/banners/HHDPromoBanner1.png" />
				</a><a
					href="<%=request.getContextPath() %>/jsp/hhdProductDetails.jsp?itemCode=3">
				<img
					src="<%=request.getContextPath()%>/images/banners/HHDPromoBanner2.png" />
				</a> <a
					href="<%=request.getContextPath() %>/jsp/hhdProductDetails.jsp?itemCode=42">
				<img
					src="<%=request.getContextPath()%>/images/banners/HHDPromoBanner3.png" />
				</a> <a
					href="<%=request.getContextPath() %>/jsp/hhdProductDetails.jsp?itemCode=23">
				<img
					src="<%=request.getContextPath()%>/images/banners/HHDPromoBanner4.png" />
				</a></div>
				<div class="clear"></div>
				</td>
			<tr valign="top">

				<td width="100%" valign="top" colspan="3" class="bottomborder">
				<div class="topsellerbox" style="width: 100%"><span
					class="heading" style="width: 100%">Top Selling BP Monitors</span><a style="color: blue"
					href="<%=request.getContextPath()%>/jsp/hhdSupplies.jsp?l2=Blood Pressure&l3=BP Monitor&b=">(View
				All)</a> <%
 	List<HHDProduct> orderList1 = (new HHDProductAction())
 			.getTopOrderedProductList("BP Monitor");
 %>
				<div class="imgBox">
				<table width="100%">
					<tr>
						<%
							String itemName = "";
							String itemImage = "";
							Double price = 0.0;
							for (Iterator iterator = orderList1.iterator(); iterator.hasNext();) {
								HHDProduct prod = (HHDProduct) iterator.next();
								itemName = prod.getProduct();
								itemImage = prod.getImageURL();
						%>
						<td valign="top">
						<table width="175">
							<tr>
								<td width="175" height="96px" align="center"><a
									href="<%=request.getContextPath()%>/jsp/hhdProductDetails.jsp?itemCode=<%=prod.getId() %>"><img
									src="<%=request.getContextPath()%>/images/ProductImages/ProductImagesThumb/Diabetes/<%=itemImage %>" /></a>
								</td>
							</tr>
							<tr>
								<td align="center"><a
									style="color: blue; text-decoration: underline"
									href="<%=request.getContextPath()%>/jsp/hhdProductDetails.jsp?itemCode=<%=prod.getId() %>"><%=itemName%></a></td>
							</tr>

						</table>
						</td>
						<%
							}
						%>
					</tr>
				</table>
				</div>
				</td>
			</tr>

			<tr valign="top">

				<td width="100%" valign="top" colspan="3" class="bottomborder">
				<div class="topsellerbox" style="width: 100%"><span
					class="heading" style="width: 100%">Top Selling Respiratory Care Devices</span><a
					style="color: blue"
					href="<%=request.getContextPath()%>/jsp/hhdSupplies.jsp?l2=Respiratory Care&l3=&b=">(View
				All)</a> <%
 	List<HHDProduct> orderList2 = (new HHDProductAction())
 			.getTopOrderedLevel2ProductList("Respiratory Care");
 %>
				<div class="imgBox">
				<table width="100%">
					<tr>
						<%
							for (Iterator iterator = orderList2.iterator(); iterator.hasNext();) {
								HHDProduct prod = (HHDProduct) iterator.next();
								itemName = prod.getProduct();
								itemImage = prod.getImageURL();
						%>
						<td valign="top">
						<table width="175">
							<tr>
								<td width="175" height="96px" align="center"><a
									href="<%=request.getContextPath()%>/jsp/hhdProductDetails.jsp?itemCode=<%=prod.getId() %>"><img
									src="<%=request.getContextPath()%>/images/ProductImages/ProductImagesThumb/Diabetes/<%=itemImage %>" /></a>
								</td>
							</tr>
							<tr>
								<td align="center"><a
									style="color: blue; text-decoration: underline"
									href="<%=request.getContextPath()%>/jsp/hhdProductDetails.jsp?itemCode=<%=prod.getId() %>"><%=itemName%></a></td>
							</tr>

						</table>
						</td>
						<%
							}
						%>
					</tr>
				</table>
				</div>
				</td>
			</tr>

			<tr valign="top">

				<td width="100%" valign="top" colspan="3" class="">
				<div class="topsellerbox" style="width: 100%"><span
					class="heading" style="width: 100%">Top Selling Sleep Apnea Devices</span><a style="color: blue"
					href="<%=request.getContextPath()%>/jsp/hhdSupplies.jsp?l2=Sleep Apnea&l3=">(View
				All)</a> <%
 	List<HHDProduct> orderList3 = (new HHDProductAction())
 			.getTopOrderedLevel2ProductList("Sleep Apnea");
 %>
				<div class="imgBox">
				<table width="100%">
					<tr>
						<%
							for (Iterator iterator = orderList3.iterator(); iterator.hasNext();) {
								HHDProduct prod = (HHDProduct) iterator.next();
								itemName = prod.getProduct();
								itemImage = prod.getImageURL();
						%>
						<td valign="top">
						<table width="175">
							<tr>
								<td width="175" height="96px" align="center"><a
									href="<%=request.getContextPath()%>/jsp/hhdProductDetails.jsp?itemCode=<%=prod.getId() %>"><img
									src="<%=request.getContextPath()%>/images/ProductImages/ProductImagesThumb/Diabetes/<%=itemImage %>" /></a>
								</td>
							</tr>
							<tr>
								<td align="center"><a
									style="color: blue; text-decoration: underline"
									href="<%=request.getContextPath()%>/jsp/hhdProductDetails.jsp?itemCode=<%=prod.getId() %>"><%=itemName%></a></td>
							</tr>

						</table>
						</td>
						<%
							}
						%>
					</tr>
				</table>
				</div>
				</td>
			</tr>

		</table>
		</td>
		<td width="33%" valign="top">
		<table width="100%" style="">
			<tr>
				<td align="right"><a
					href="<%=request.getContextPath()%>/jsp/cod-faq.jsp?pt=hhd"><img
					src="<%=request.getContextPath()%>/images/COD.png" /></a></td>
			</tr>
			<tr>
				<td style="padding-left: 10px">
				<table class="sectionoutline">
					<tr>
						<td class="" valign="top">
						<div class="home-block">
						<h1>BMI</h1>
						<table class="sectionoutline" width="100%">
							<tr>
								<td align="left" class="label" width="75px">Weight:*</td>
								<td align="left"><input class="inputBox" size="1"
									onchange="isDouble(this); computeBMI('weight', 'height', 'bmi');getBMIPrompt();"
									id="weight" name="weight">(Kgs)</td>
							</tr>
							<tr>
								<td align="left" class="label">Height:*</td>
								<td align="left"><input class="inputBox" size="1"
									onchange="isDouble(this); computeBMI('weight', 'height', 'bmi');getBMIPrompt();"
									id="height" name="height">(cms)</td>
							</tr>

							<tr>
								<td align="left" class="label">BMI:*</td>
								<td align="left"><input class="inputBox" size="1"
									readonly="readonly" id="bmi" name="bmi">(kg/m<sup>2</sup>)</td>
							</tr>
							<tr>
								<td colspan="2" valign="top" id="bmiState"></td>
							</tr>
						</table>
						</div>
						</td>
					</tr>

					<tr>
						<td class="">
						<div class="home-block">
						<h1>FAQs About BP</h1>
						<div id="news" class="sectionoutline"><script
							type="text/javascript">
					var iframesrc="<%=request.getContextPath()%>/jsp/bp-faq.jsp"
					document.write('<iframe id="datamain" src="'+iframesrc+'" width="100%" height="200px" marginwidth="0" marginheight="0" hspace="0" vspace="0" 	frameborder="0" 	scrolling="no"></iframe>')
					</script></div>
						</div>
						</td>
					</tr>

					<tr>
						<td class="">
						<div class="home-block">
						<h1>FAQs About Sleep Apnea</h1>
						<div id="news" class="sectionoutline"><script
							type="text/javascript">
					var iframesrc="<%=request.getContextPath()%>/jsp/cpap-faq.jsp"
					document.write('<iframe id="datamain" src="'+iframesrc+'" width="100%" height="200px" marginwidth="0" marginheight="0" hspace="0" vspace="0" 	frameborder="0" 	scrolling="no"></iframe>')
					</script></div>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>

		</table>
		</td>
	</tr>
</table>
</div>

<div class="pg-footer"><jsp:include page="/jsp/pg-footer.jsp"></jsp:include></div>

</div>
</body>
</html>
--%>
