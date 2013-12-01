<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="eye offer">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/eye-style.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">eye Offers</span>

		<h1 class="title">eye Offers</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Eye Offers</s:layout-component>
<s:layout-component name="metaKeywords">Eye Offers</s:layout-component>


<s:layout-component name="content">
<div id="container">
	<div class="nRow"><img src="images/eye-page-banner1.jpg" alt=""/></div>
    <div class="nRow dHeight1"></div>
    <div class="eyePgBlueBx">
    	<div class="nRow eyeBlueBoxData">
        <h1>Summer Special</h1>
        <h2>Contemporary and refined designs for improved vision</h2>
        </div>
        <div class="nRow eyePgOffers"><img src="images/eye-page-offers.jpg" usemap="#Map" border="0" /></div>
        <div class="nRow eyePgLastBrdr">
            <p>Choice for all occasions; while you are at work or when you have to put on your dancing shoes</p>
      </div>
        <div class="nRow">&nbsp;</div>
  </div>
  	<div class="nRow">&nbsp;</div>
</div>
<map name="Map" id="Map">
            <area shape="poly" coords="84,119,67,122,47,118,41,99,32,33,37,2,62,6,141,27,163,33,168,97,171,122,113,127,100,137" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'820', height:'400'} )" title="" contentID="myCotentDiv1" />

            <area shape="poly" coords="101,363,71,286,11,288,1,126,184,150,186,272,109,282" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'820', height:'400'} )" title="" contentID="myCotentDiv4" />

            <area shape="poly" coords="255,252,227,183,187,178,176,62,307,85,313,179,257,178" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'820', height:'400'} )" title="" contentID="myCotentDiv2" />

      		<area shape="poly" coords="371,228,374,146,314,146,324,39,463,23,451,130,393,140" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'820', height:'380'} )" title="" contentID="myCotentDiv5" />

            <area shape="poly" coords="475,351,472,276,386,270,396,140,576,118,560,281,509,281" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'820', height:'400'} )" title="" contentID="myCotentDiv3" />

          </map>

<div id="myCotentDiv1" class="hideOffers">
	<div class="ppBxInr">
    	<div class="nRow">
            <div class="eyePopUpImgBox">
                <p><img src="images/199/pic-1.jpg" alt="" /></p>
                <p class="eyeOfferProNames">12 Black/Blue, Teenager Wayfarer Eyeglasses</p>
                <p class="eyeProPrice">Rs.199 <span>| 60% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/7-black-green-teenager-rectangular-eyeglasses/EYE1996" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/199/pic-2.jpg" alt="" /></p>
                <p class="eyeOfferProNames">14 Black/Red, Teenager Rectangular Eyeglasses</p>
                <p class="eyeProPrice">Rs.199 <span>| 60% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/7-black-green-teenager-rectangular-eyeglasses/EYE1982" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/199/pic-3.jpg" alt="" /></p>
                <p class="eyeOfferProNames">16 Black/Red, <br /> Teenager</p>
                <p class="eyeProPrice">Rs.199 <span>| 60% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/7-black-green-teenager-rectangular-eyeglasses/EYE1101" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox" style="border:none;">
                <p><img src="images/199/pic-4.jpg" alt="" /></p>
                <p class="eyeOfferProNames">7 Black,Teenager Rectangular Eyeglasses</p>
                <p class="eyeProPrice">Rs.199 <span>| 60% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/7-black-green-teenager-rectangular-eyeglasses/EYE1097" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="eyeVDashedBrdr1">&nbsp;</div>
		<div class="nRow">
         	<div class="eyeRightBx"><a href="http://www.healthkart.com/eye/eyeglasses?filterOptions[0]=11244&minPrice=199&maxPrice=2299"><img src="images/view-all.jpg" alt="View All" /></a></div>
        </div>
   	</div>
    <div class="cl"></div>
</div>


<div id="myCotentDiv2" class="hideOffers">
	<div class="ppBxInr">
    	<div class="nRow">
            <div class="eyePopUpImgBox">
                <p><img src="images/349/pic-1.jpg" alt="" /></p>
                <p class="eyeOfferProNames">XH 0290/0291 Silver/Red, Fidato Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.349 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/xh-1728-brown-fidato-aviator-sunglasses/EYE2036" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/349/pic-2.jpg" alt="" /></p>
                <p class="eyeOfferProNames">XH 0290/0291 Silver/Purple, Fidato Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.349 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/xh-1728-brown-fidato-aviator-sunglasses/EYE2035" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/349/pic-3.jpg" alt="" /></p>
                <p class="eyeOfferProNames">XH 0290/0291 Silver/Pink, Fidato Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.349 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/xh-1728-brown-fidato-aviator-sunglasses/EYE2034" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/349/pic-4.jpg" alt="" /></p>
                <p class="eyeOfferProNames">XH 1728 Brown, Fidato Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.349 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/xh-1728-brown-fidato-aviator-sunglasses/EYE2052" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>

            <div class="cl"></div>
   		</div>
        <div class="eyeVDashedBrdr1">&nbsp;</div>
		<div class="nRow">
         	<div class="eyeRightBx"><a href="http://www.healthkart.com/eye/sunglasses?filterOptions[0]=15472&minPrice=60&maxPrice=5000"><img src="images/view-all.jpg" alt="View All" /></a></div>
        </div>
   	</div>
    <div class="cl"></div>
</div>


<div id="myCotentDiv3" class="hideOffers">
	<div class="ppBxInr">
    	<div class="nRow">
            <div class="eyePopUpImgBox">
                <p><img src="images/599/pic-1.jpg" alt="" /></p>
                <p class="eyeOfferProNames">UV 3025 Black/Green, Urban Verve Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.599 <span>| 40% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/EYE2190" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/599/pic-2.jpg" alt="" /></p>
                <p class="eyeOfferProNames">UV 3025 Silver/Blue, Urban Verve Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.599 <span>| 40% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/EYE2197" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/599/pic-3.jpg" alt="" /></p>
                <p class="eyeOfferProNames">UV 3025 Silver/Purple, Urban Verve Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.599 <span>| 40% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/EYE2198" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/599/pic-4.jpg" alt="" /></p>
                <p class="eyeOfferProNames">UV 3025 Silver/Red, Urban Verve Aviator Sunglasses</p>
                <p class="eyeProPrice">Rs.599 <span>| 40% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/EYE2199" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="eyeVDashedBrdr1">&nbsp;</div>
		<div class="nRow">
         	<div class="eyeRightBx"><a href="http://www.healthkart.com/eye/sunglasses?filterOptions[0]=14419&minPrice=60&maxPrice=5000&productReferrerId=21&camp=urban_verve_599"><img src="images/view-all.jpg" alt="View All" /></a></div>
        </div>
   	</div>
    <div class="cl"></div>
</div>


<div id="myCotentDiv4" class="hideOffers">
	<div class="ppBxInr">
    	<div class="nRow">
            <div class="eyePopUpImgBox">
                <p><img src="images/750/pic-1.jpg" alt="" /></p>
                <p class="eyeOfferProNames">Titanium 3113 Black, Odysey Rectangular Eyeglasses</p>
                <p class="eyeProPrice">Rs.750 <span>| 56% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/titanium-3113-brown-odysey-rectangular-eyeglasses/EYE798" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/750/pic-2.jpg" alt="" /></p>
                <p class="eyeOfferProNames">Titanium 2020 Blue, Odysey Rectangular Eyeglasses</p>
                <p class="eyeProPrice">Rs.750 <span>| 56% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/titanium-3113-brown-odysey-rectangular-eyeglasses/EYE799" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/750/pic-3.jpg" alt="" /></p>
                <p class="eyeOfferProNames">Titanium 2020 Purple, Odysey Rectangular Eyeglasses</p>
                <p class="eyeProPrice">Rs.750 <span>| 56% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/titanium-3113-brown-odysey-rectangular-eyeglasses/EYE800" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/750/pic-4.jpg" alt="" /></p>
                <p class="eyeOfferProNames">Titanium 2020 Grey, Odysey Rectangular Eyeglasses</p>
                <p class="eyeProPrice">Rs.750 <span>| 56% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/titanium-3113-brown-odysey-rectangular-eyeglasses/EYE803" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="eyeVDashedBrdr1">&nbsp;</div>
		<div class="nRow">
         	<div class="eyeRightBx"><a href="http://www.healthkart.com/eye/eyeglasses?filterOptions[0]=11146&filterOptions[1]=10082&minPrice=199&maxPrice=2299"><img src="images/view-all.jpg" alt="View All" /></a></div>
        </div>
   	</div>
    <div class="cl"></div>
</div>


<div id="myCotentDiv5" class="hideOffers">
	<div class="ppBxInr">
    	<div class="nRow">
            <div class="eyePopUpImgBox">
                <p><img src="images/combo/pic-1.jpg" alt="" /></p>
                <p class="eyeOfferProNames">1+1 free combo 1</p>
                <p class="eyeProPrice">Rs.699 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/CMB-EYE19" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/combo/pic-2.jpg" alt="" /></p>
                <p class="eyeOfferProNames">1+1 free combo 2</p>
                <p class="eyeProPrice">Rs.699 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/CMB-EYE20" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/combo/pic-3.jpg" alt="" /></p>
                <p class="eyeOfferProNames">1+1 free combo 3</p>
                <p class="eyeProPrice">Rs.699 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/CMB-EYE20" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="eyePopUpImgBox">
                <p><img src="images/combo/pic-4.jpg" alt="" /></p>
                <p class="eyeOfferProNames">1+1 free combo 4</p>
                <p class="eyeProPrice">Rs.699 <span>| 65% Off</span></p>
                <p align="center"><a href="http://www.healthkart.com/product/82231-black-black-urban-verve-wayfarer-sunglasses/CMB-EYE21" class="eyeBuyNowBtn">BUY NOW</a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="eyeVDashedBrdr1">&nbsp;</div>
		<div class="nRow">
         	<div class="eyeRightBx"><a href="${pageContext.request.contextPath}/pages/offers/eye-page/eye-combo-page.jsp"><img src="images/view-all.jpg" alt="View All" /></a></div>
        </div>
   	</div>
    <div class="cl"></div>
</div>



<script src="js/cquery_customlightbox.js" type="text/javascript"></script>


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

</s:layout-render>
