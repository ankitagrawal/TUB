<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="sports offer">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/eye-style.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">sports Offers</span>

		<h1 class="title">sports Offers</h1>
	</div>

</s:layout-component>


<s:layout-component name="content">
<div class="sportsContainr">
    	<div class="content">
			<div class="rm_wrapper">
            	<div class="rm_nav">
					<a id="rm_next" href="#" class="rm_next"></a>
					<a id="rm_prev" href="#" class="rm_prev"></a>
				</div>
				<div id="rm_container" class="rm_container">
					<ul>
						<li data-images="rm_container_1" data-rotation="-15" style="margin-top:35px;"><img src="images/sl-pic1.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv1" /></li>
						<li data-images="rm_container_2" data-rotation="-5"><img src="images/sl-pic2.jpg" href="javascript:void(0)" onClick="window.open('http://www.healthkart.com/brand/sports/Carlton', '_self')"/></li>
						<li data-images="rm_container_3" data-rotation="5"><img src="images/sl-pic3.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv3" /></li>
						<li data-images="rm_container_4" data-rotation="15" style="margin-top:35px;"><img src="images/sl-pic4.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv4"/></li>
					</ul>
					<div id="rm_mask_left" class="rm_mask_left"></div>
					<div id="rm_mask_right" class="rm_mask_right"></div>
					<div id="rm_corner_left" class="rm_corner_left"></div>
					<div id="rm_corner_right" class="rm_corner_right"></div>
					<div style="display:none;">
						<div id="rm_container_1">
							<img src="images/sl-pic1.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv1" />
							<img src="images/sl-pic5.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'630', height:'340'} )" title="" contentID="crslDiv5" />
						</div>
						<div id="rm_container_2">
							<img src="images/sl-pic2.jpg" href="javascript:void(0)" onClick="window.open('http://www.healthkart.com/brand/sports/Carlton', '_self')"/>
							<img src="images/sl-pic8.jpg" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv6" />
						</div>
						<div id="rm_container_3">
							<img src="images/sl-pic3.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv3" />
							<img src="images/sl-pic11.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv7" />
						</div>
						<div id="rm_container_4">
							<img src="images/sl-pic4.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="crslDiv4" />
							<img src="images/sl-pic14.jpg" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'270'} )" title="" contentID="crslDiv8" />
						</div>
					</div>
				</div>
			</div>
		</div>

    <div class="row BannerOneOuter">
    	<div class="BannerOneInr" align="center"><a href="#"><img src="images/cricket-bat-offer-banner.jpg" usemap="#Map" border="0" />
            <map name="Map" id="Map">
              <area shape="rect" coords="-10,-4,877,155" href="sports-bats-offers-page.jsp" />
            </map>
   	  </a></div>
    </div>
    <div class="row" align="center"><img src="images/offer-banner1.jpg" usemap="#Map2" border="0" />
      <map name="Map2" id="Map2">
        <area shape="rect" coords="0,0,221,342" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv1" />
        <area shape="rect" coords="225,5,439,340" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'835', height:'400'} )" title="" contentID="myCotentDiv2" />
        <area shape="rect" coords="444,6,656,339" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv3" />
        <area shape="rect" coords="661,11,872,341" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv4" />
      </map>
  </div>
    <div class="row separatorLines" align="center"><img src="images/separator-line1.jpg" /></div>
  <div class="row" align="center"><img src="images/offer-banner2.jpg" usemap="#Map3" border="0" />
      <map name="Map3" id="Map3">
        <area shape="rect" coords="12,10,221,340" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv5" />
        <area shape="rect" coords="224,10,442,343" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv6" />
        <area shape="rect" coords="444,6,655,339" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv7" />
        <area shape="rect" coords="659,7,873,341" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv8" />
      </map>
  </div>
    <div class="row separatorLines" align="center"><img src="images/separator-line2.jpg" /></div>
    <div class="row separatorLines" align="center"><img src="images/offer-banner3.jpg" usemap="#Map4" border="0" />
      <map name="Map4" id="Map4">
        <area shape="rect" coords="14,2,225,337" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv9" />
        <area shape="rect" coords="232,7,437,340" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'630', height:'410'} )" title="" contentID="myCotentDiv10" />
        <area shape="rect" coords="444,7,657,347" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'630', height:'410'} )" title="" contentID="myCotentDiv11" />
        <area shape="rect" coords="663,8,875,342" href="javascript:void(0)" onClick="$.fn.cquery_customlightbox(this, {width:'400', height:'230'} )" title="" contentID="myCotentDiv12" />
      </map>
  </div>
    <div class="row">&nbsp;</div>
</div>


<div id="myCotentDiv1" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKFREESCALE</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/sports/fitness-accessories" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv2" class="hideOffers">
	<div class="ppBx2">
    	<div class="row">
            <div class="offerTxt">
                <h3><span>Note:-</span> To get 25% off on Summer 69 tshirts, buy a pair of shoes from Nivea/Vector X/Kanton.</h3>
            </div>
        </div>
        <div class="dashedBrdr1"></div>
    	<div class="row">
            <div class="spPopUpImgBox">
                <p><img src="images/vector-X-shoes.jpg" alt="" /></p>
                <p class="spOfferProNames">Vector X Shoes</p>
                <p align="center"><a href="http://www.healthkart.com/sports/footwear?brand=Vector+X" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox">
                <p><img src="images/nivia-shoes.jpg" alt="" /></p>
                <p class="spOfferProNames">Nivia Shoes</p>
                <p align="center"><a href="http://www.healthkart.com/sports/footwear?brand=Nivia" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox">
                <p><img src="images/kanton-shoes.jpg" alt="" /></p>
                <p class="spOfferProNames">Kanton Shoes</p>
                <p align="center"><a href="http://www.healthkart.com/sports/footwear?brand=Kanton" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox" style="border:none;">
                <p><img src="images/summer69-tshirt.jpg" alt="" /></p>
                <p class="spOfferProNames">Summer 69 T-shirts</p>
                <p align="center"><a href="http://www.healthkart.com/sports/apparel?brand=Summer+69" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="dashedBrdr2"></div>
        <div class="row">
        	<div class="cpnBx2Left">Coupon Code</div>
        	<div class="cpnBx2Mid"><h1>HKSUMMER25</h1></div>
            <div class="cpnBx2Right">*Offer valid till stock lasts</div>
        </div>
        <div class="row">
        	<p></p>
            <p>For any query:- Please e-mail us at <a href="mailto:category.sports@healthkart.com" class="sportsEmail">category.sports@healthkart.com</a></p>
        </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv3" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKUSI</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/USI" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv4" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKGOLA</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/sports/footwear?brand=Gola" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv5" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKLDHW</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/sports/sports-equipment/boxing/punching-bags?brand=Lonsdale" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv6" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKYONEX</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/Yonex" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv7" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKFREETSHIRT</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/product/schiek-contoured-weight-lifting-belt-6-inch/SPT994" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv8" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKNEWFEEL15</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/Newfeel" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv9" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKBODYBALANCE</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/Body+Balance" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv10" class="hideOffers">
	<div class="ppBx2">
    	<div class="row">
            <div class="offerTxt">
                <h3><span>Note:-</span> With every pair of Nivea or Vector X shoes, get a pair of Slazenger socks free.</h3>
            </div>
        </div>
        <div class="dashedBrdr1"></div>
    	<div class="row">
            <div class="spPopUpImgBox">
                <p><img src="images/vector-X-shoes.jpg" alt="" /></p>
                <p class="spOfferProNames">Vector X Shoes</p>
                <p align="center"><a href="http://www.healthkart.com/sports/footwear?brand=Vector+X" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox">
                <p><img src="images/nivia-shoes.jpg" alt="" /></p>
                <p class="spOfferProNames">Nivia Shoes</p>
                <p align="center"><a href="http://www.healthkart.com/sports/footwear?brand=Nivia" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox" style="border:none;">
                <p><img src="images/slazenger-socks.jpg" alt="" /></p>
                <p class="spOfferProNames">Slazenger Socks</p>
                <p align="center"><a href="http://www.healthkart.com/product/slazenger-smc-115-full-terry-seamless-socks/SPT2384" target="_blank"><img src="images/view-this.jpg" alt="View This" /></a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="dashedBrdr2"></div>
        <div class="row">
        	<div class="cpnBx2Left">Coupon Code</div>
        	<div class="cpnBx2Mid"><h1>HKFREESOCKS</h1></div>
            <div class="cpnBx2Right">*Offer valid till stock lasts</div>
        </div>
        <div class="row">
        	<p></p>
            <p>For any query:- Please e-mail us at <a href="mailto:category.sports@healthkart.com" class="sportsEmail">category.sports@healthkart.com</a></p>
        </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv11" class="hideOffers">
	<div class="ppBx2">
    	<div class="row">
            <div class="offerTxt">
                <h3><span>Note:-</span> Buy a Shaker 40 Pro or a Body Fortress Shaker and get 50% off on Stacker Bottle</h3>
            </div>
        </div>
        <div class="dashedBrdr1"></div>
    	<div class="row">
            <div class="spPopUpImgBox">
                <p><img src="images/shaker40-pro.jpg" alt="" /></p>
                <p class="spOfferProNames">Shaker 40 Pro</p>
                <p align="center"><a href="http://www.healthkart.com/product/shaker-40-pro/NUT448" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox">
                <p><img src="images/body-fortress-shaker.jpg" alt="" /></p>
                <p class="spOfferProNames">Body Fortress Shaker</p>
                <p align="center"><a href="http://www.healthkart.com/product/body-fortress-all-in-one-shaker/NUT981" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox" style="border:none;">
                <p><img src="images/stacker-bottle3.jpg" alt="" /></p>
                <p class="spOfferProNames">Stacker Bottle</p>
                <p align="center"><a href="http://www.healthkart.com/product/cn-stacker-bottle/SPT3122" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="dashedBrdr2"></div>
        <div class="row">
        	<div class="cpnBx2Left">Coupon Code</div>
        	<div class="cpnBx2Mid"><h1>HKSTACKER50</h1></div>
            <div class="cpnBx2Right">*Offer valid till stock lasts</div>
        </div>
        <div class="row">
        	<p></p>
            <p>For any query:- Please e-mail us at <a href="mailto:category.sports@healthkart.com" class="sportsEmail">category.sports@healthkart.com</a></p>
        </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="myCotentDiv12" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKLIFTING</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/product/schiek-contoured-weight-lifting-belt-6-inch/SPT3493" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>





<div id="crslDiv1" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKSUUNTO</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/Suunto" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="crslDiv3" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKFREEBAND</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/Polar" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="crslDiv4" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKWILDCRAFT</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/Wildcraft" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="crslDiv5" class="hideOffers">
	<div class="ppBx2">
    	<div class="row">
            <div class="offerTxt">
                <h3>Great Discounts on Slazenger, GM & SG</h3>
            </div>
        </div>
        <div class="dashedBrdr1"></div>
    	<div class="row">
            <div class="spPopUpImgBox">
                <p><img src="images/slazenger.jpg" alt="" /></p>
                <p class="spOfferProNames">Slazenger</p>
                <p class="spProDiscounts">Up to 35% off</p>
                <p align="center"><a href="http://www.healthkart.com/brand/sports/Slazenger" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox">
                <p><img src="images/gm.jpg" alt="" /></p>
                <p class="spOfferProNames">GM</p>
                <p class="spProDiscounts">Up to 20% off</p>
                <p align="center"><a href="http://www.healthkart.com/brand/sports/GM" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="spPopUpImgBox" style="border:none;">
                <p><img src="images/sg.jpg" alt="" /></p>
                <p class="spOfferProNames">SG</p>
                <p class="spProDiscounts">Up to 15% off</p>
                <p align="center"><a href="http://www.healthkart.com/brand/sports/SG" target="_blank"><img src="images/order-now.jpg" alt="Order Now" /></a></p>
            </div>
            <div class="cl"></div>
   		</div>
        <div class="dashedBrdr2"></div>
    	<div class="row">&nbsp;</div>
    </div>
</div>

<div id="crslDiv6" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKYONEX15</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/Yonex" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="crslDiv7" class="hideOffers">
	<div class="ppBx1">
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKFITNESS500</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/sports/fitness-equipment" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<div id="crslDiv8" class="hideOffers">
	<div class="ppBx1">
    	<div class="row offerTxt" align="center"><h3>Spend Rs.1000 or more on Muscleblaze and get 20% off on your total cart value.</h3></div>
		<div class="row" align="center">To avail the offer, use this coupon code.</div>
    	<div class="row">
   		<div class="couponCodeBx1">
        	<h5>Coupon Code</h5>
            <h1>HKMB20</h1>
            <p>*Offer valid till stock lasts</p>
    	</div>
  	</div>
    	<div class="row" align="center">
        <a href="http://www.healthkart.com/brand/sports/MuscleBlaze" target="_blank"><img src="images/order-now-big-btn.jpg" alt="Order Now" border="0" /></a>
    </div>
    	<div class="cl"></div>
    </div>
</div>

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.transform-0.9.3.min.js"></script>
<script type="text/javascript" src="js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="js/jquery.RotateImageMenu.js"></script>
<script src="js/cquery_customlightbox.js" type="text/javascript"></script>
</s:layout-component>

</s:layout-render>