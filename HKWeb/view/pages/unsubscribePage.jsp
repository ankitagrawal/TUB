<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Diwali Offers">

<s:layout-component name="htmlHead">
  <style type="text/css">
		.cl { clear:both; }

@font-face {
				font-family: 'Esta';
				src: url('${pageContext.request.contextPath}/images/diwali2012/estaregular.eot');
				src: url('${pageContext.request.contextPath}/images/diwali2012/estaregular.eot?#iefix') format('embedded-opentype'),
						 url('${pageContext.request.contextPath}/images/diwali2012/estaregular.woff') format('woff'),
						 url('${pageContext.request.contextPath}/images/diwali2012/estaregular.ttf') format('truetype'),
						 url('${pageContext.request.contextPath}/images/diwali2012/estaregular.svg#estaregular') format('svg');
				font-weight: normal;
				font-style: normal;
		}
@font-face {
				font-family: 'DidotHTF-11LightItalic';
				src: url('${pageContext.request.contextPath}/images/diwali2012/didothtf-11lightitalic.eot');
				src: url('${pageContext.request.contextPath}/images/diwali2012/didothtf-11lightitalic.eot?#iefix') format('embedded-opentype'),
						 url('${pageContext.request.contextPath}/images/diwali2012/didothtf-11lightitalic.woff') format('woff'),
						 url('${pageContext.request.contextPath}/images/diwali2012/didothtf-11lightitalic.ttf') format('truetype'),
						 url('${pageContext.request.contextPath}/images/diwali2012/didothtf-11lightitalic.svg#didothtf-11lightitalic') format('svg');
				font-weight: normal;
				font-style: normal;
		}
.down	{ width:100%; clear:both;}
.clr	{ clear:both; height:1px; line-height:1px; font-size:1px;}

.outer	{ margin:15px auto; width:950px; overflow:auto; clear:both; padding-bottom:240px; }

.blueHeadings, h2	{ font-size:30px; line-height:36px; color:#0093d8; font-family:'DidotHTF-11LightItalic';  font-weight:normal; text-align:center; margin:0px; padding:0px;}


.greyHeadings, h3	{ font-size:58px; line-height:66px; color:#77787b; font-family:'DidotHTF-11LightItalic'; font-weight:normal; text-align:center; margin:0px; padding:0px;}
.greyHeadings, h3 a			{ font-size:58px;  color:#77787b; text-decoration:none; border:none;}
.greyHeadings, h3 a:hover	{ color:#77787b; text-decoration:none;}


.proImage			{ float:left;}

.proData			{ float:left;}
.proData .txt		{ clear:both; font-family:"Times New Roman", Times, serif; font-size:20px; color:#221f1f; line-height:24px; padding-bottom:10px;}
.proData .btmBrdr	{ background:#cbcdcf; height:2px; line-height:2px;}

.proData .price1		{ float:left; text-align:right; width:200px; font-size:54px; line-height:60px; color:#0093d8;
						  font-family:'DidotHTF-11LightItalic'; font-weight:normal; margin-top:15px;}
.proData .price1	span { font-size:22px; line-height:24px; color:#221f1f; }




.proData .discountRed			{ width:100px; position:absolute; color:#515254; font-size:22px; line-height:24px; color:#221f1f;
							  	  font-family: Didot HTF; font-weight:normal;}
.proData .discountRed a			{ color:#d6611d; text-decoration:none;}
.proData .discountRed a:hover	{ color:#d6611d; text-decoration:none;}


.proData .buyTxt			{ float:right; width:140px; height:90px; margin-top:15px; line-height:90px; margin-right:30px; background: url("${pageContext.request.contextPath}/images/diwali2012/bg-buy-this-now.png") center center no-repeat; font-size:18px; text-transform:uppercase;
							  text-align:center; font-family:"Times New Roman", Times, serif; color:#0063b0;}
.proData .buyTxt a			{ font-size:18px; color:#0063b0; text-decoration:none;}
.proData .buyTxt a:hover	{ color:#0063b0; text-decoration:none;}


.pad1			{ text-align:center; padding:20px 0px 10px 0px;}
.extendTxt		{ text-align:center; margin:0 auto; padding:10px 0px 30px 0px;  font-family:"Times New Roman", Times, serif; font-size:20px; color:#221f1f; line-height:24px;}
.htTen			{ height:10px; line-height:10px;}


.extendTxtNew		{ font-family:"Times New Roman", Times, serif; font-size:20px; color:#221f1f; line-height:24px; float:left; width:300px;}
.extTxtCenter	{ text-align:center; font-family:"Times New Roman", Times, serif; font-size:20px; color:#221f1f; line-height:24px; padding:20px 150px 20px 150px;}

        </style>


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Diwali Offers</span>

    <h1 class="title">
     Diwali Special Offers
    </h1>
  </div>
  
</s:layout-component>

<s:layout-component name="metaDescription">Special Diwali Offers from HealthKart.com</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>

<s:layout-component name="content">
<div class="outer">
	<div class="down"><img src="${pageContext.request.contextPath}/images/diwali2012/be-the-festival.jpg" alt="Be The Festival" border="0" /></div>
  	<div class="down">&nbsp;</div>

  	<h2>Ideas for a Stylish Diwali</h2>
  <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  <h3><a href="http://www.healthkart.com/product/it-takes-two-to-tango!/CMB-EYE101" target="_blank">It takes Two to Tango!</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 30px;"><a href="http://www.healthkart.com/product/it-takes-two-to-tango!/CMB-EYE101" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/it-takes-two-to-tango.jpg" alt="It Takes Two to Tango" border="0" /></a></div>
    	<div class="proData" style="width:450px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Celebrate in style withTH50004 Brown, Touch for women and the 82231 Black /Blue, Spark
for men.</div>
            <div class="btmBrdr" style="width:380px;">&nbsp;</div>

            <div class="price1">Rs.1,395<br />
            	<span>70% off</span>
          </div>
            <div class="buyTxt" style="margin-right:50px;"><a href="http://www.healthkart.com/product/it-takes-two-to-tango!/CMB-EYE101" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/velocity-gold-united/CMB-EYE100" target="_blank">Mother & Daughter, Partners in Crime</a></h3>
    <div class="down">
 	  <div class="proData" style="width:450px; padding:10px 0px 0px 70px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">For the mothers and daughters out there who want to step out in style, nothing else will make heads turn like the classic Idee S1650 C1 black sunglasses and the contemporary 1074 C2, Opium silver shades.</div>
            <div class="btmBrdr" style="width:400px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:20px;">Rs.2,969<br />
            	<span>40% off</span>
            </div>
            <div class="buyTxt" style="margin-right:10px;"><a href="http://www.healthkart.com/product/velocity-gold-united/CMB-EYE100" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/velocity-gold-united/CMB-EYE100" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/mother-and-daughter-partners-in-crime.jpg" alt="Mother and Daughter Partners in Crime" border="0"/></a></div>
  </div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/active-men-bath-and-body-essentials/DLICMB01" target="_blank">Active Men Bath and Body Essentials</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 20px;"><a href="http://www.healthkart.com/product/active-men-bath-and-body-essentials/DLICMB01" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/active-men-bath-and-body-essentials.jpg" alt="Active Men Bath and Body Essentials" border="0" /></a></div>
    	<div class="proData" style="width:450px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">AXE Talc, Nivea Shaving Foam, Denim After Shave Lotion & Adidas Shower Gel. A collection
for men on the go.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1" style="width:170px;">Rs.606<br />
            	<span>5% off</span>
            </div>
          	<div class="buyTxt" style="margin-right:50px;"><a href="http://www.healthkart.com/product/active-men-bath-and-body-essentials/DLICMB01" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/fabindia-s-candles-not-included-set/DLICMB02" target="_blank">Fabindia 'Candles not Included' Set</a></h3>
    <div class="down">
 	  <div class="proData" style="width:400px; padding:10px 0px 0px 70px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">You need to bring your own candles and maybe a book for this exotic set of bath and body goodies from Fabindia. We have an Orange Neroli Bathing Bar, a Shea Butter Bathing Bar, Fabindia Lip Butter, Face Wash, Shampoo
& Body Butter for that long dip in the tub.</div>
            <div class="btmBrdr" style="width:360px; float:right;">&nbsp;</div>

            <div class="price1">Rs.745</div>
            <div class="buyTxt" style="margin-right:10px;"><a href="http://www.healthkart.com/product/fabindia-s-candles-not-included-set/DLICMB02" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/fabindia-s-candles-not-included-set/DLICMB02" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/fabindia-candles-not-included-set.jpg" alt="Fabindia Fandles Not Include Set" border="0"/></a></div>
  </div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/feet-in-the-clouds/DLICMB03" target="_blank">Feet in the Clouds Set</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 50px;"><a href="http://www.healthkart.com/product/feet-in-the-clouds/DLICMB03" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/feet-in-the-clouds-set.jpg" alt="Feet in the Clouds Set" border="0" /></a></div>
    	<div class="proData" style="width:450px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Vaadi Herbals Pedicure Manicure Kit, Faces Nail Enamel and Faces Foot Favorites Lavender Insoles for some really happy feet.</div>
            <div class="btmBrdr" style="width:400px;">&nbsp;</div>

            <div class="price1" style="width:165px;">Rs.454<br />
            	<span>5% off</span>
            </div>
            <div class="buyTxt" style="margin-right:70px;"><a href="http://www.healthkart.com/product/feet-in-the-clouds/DLICMB03" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/maybelline-combo-for-women/RKHCMB7" target="_blank">Maybelline Party All Night Set</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 70px;"><a href="http://www.healthkart.com/product/maybelline-combo-for-women/RKHCMB7" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/maybelline-party-all-night-set.jpg" alt="Maybelline Party All Night Set" border="0" /></a></div>
   	  <div class="proData" style="width:470px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Maybelline Eye Colossal Kajal, Maybelline Color Senstational Lip Color, Maybelline Colorama Nail Color with Miners Glitzy Kitz Lipstick Pouch. Own the night!</div>
            <div class="btmBrdr" style="width:430px;">&nbsp;</div>

            <div class="price1">Rs.1045<br />
            	<span>5% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/maybelline-combo-for-women/RKHCMB7" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
 	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/manufaktura-mojito-home-perfume/MNFCR39" target="_blank">From the Czech Republic, with Love</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 60px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Manufaktura Mojito Home Perfume. One dab of refreshing mint and orange extracts scent swirls around you for hours, keeping you calm, grounded and bright, all at the same time. Mist over your body, day or night, for portable deodorizing when you're on the move.</div>
            <div class="btmBrdr" style="width:430px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:20px;">Rs.1,500</div>
            <div class="buyTxt" style="margin-right:20px;"><a href="http://www.healthkart.com/product/manufaktura-mojito-home-perfume/MNFCR39" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/manufaktura-mojito-home-perfume/MNFCR39" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/from-the-czech-republic-with-love.jpg" alt="From the Czech Republic, with Love" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/denim-after-shave-lotion/ALV1" target="_blank">Get fresh with Arjun Rampal</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 70px;"><a href="http://www.healthkart.com/product/denim-after-shave-lotion/ALV1" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/get-fresh-with-arjun-rampal.jpg" alt="Get fresh with Arjun Rampal" border="0" /></a></div>
    	<div class="proData" style="width:470px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Alive by Arjun Rampal Golden Sunrise. Golden Sunrise EDT (Unisex) is a citrus fragrance with a
pleasant smell. It is a mixture of natural fruit extracts, including Lemon, Bergamot and Mandarin.</div>
            <div class="btmBrdr" style="width:430px;">&nbsp;</div>

            <div class="price1">Rs.1,800</div>
            <div class="buyTxt" style="margin-right:50px;"><a href="http://www.healthkart.com/product/denim-after-shave-lotion/ALV1" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
  <div class="down" style="height:30px;">&nbsp;</div>

  <h2>Ideas for a fitter Diwali</h2>
  <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  	<h3><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2500" target="_blank">A Beautiful Kit for the Beautiful Game</a></h3>
  <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 50px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2500" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/a-beautiful-kit-for-the-beautiful-game.jpg" alt="A Beautiful Kit for the Beautiful Game" border="0" /></a></div>
   	  <div class="proData" style="width:430px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Kipsta F500 Football with VX Destiny Football Shoes. A great pair to get intimate with the greatest game ever.</div>
            <div class="btmBrdr" style="width:400px;">&nbsp;</div>

            <div class="price1">Rs.1,709<br />
            	<span>40% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2500" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
    </div>
 	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2507" target="_blank">Workout In Style</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 100px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Adidas Training Gloves (Velcro Closure-Red Line) along with Invincible T-11 T Shirt and Invincible G-105 Shorts. A gear with zero compromise on performance or on style.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:20px;">Rs.2,240<br />
            	<span>41% off</span>
            </div>
            <div class="buyTxt" style="margin-right:10px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2507" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2507" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/workout-in-style.jpg" alt="Workout In Style" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2506" target="_blank">Upper Body Strengtheners</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 30px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2506" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/upper-body-strengtheners.jpg" alt="Upper Body Strengtheners" border="0" /></a></div>
    	<div class="proData" style="width:430px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Get your muscles toned the way you want with Adidas Swivel Push Up Bar and the rugged
and portable Tunturi Doorway Chinning Bar.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1">Rs.3,861<br />
            	<span>37% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2506" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/i-against-i-lonsdale-boxing-pack/SPT2509" target="_blank">I against I, flesh of my flesh,
      <br />
  and mind of my mind</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 100px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Two of a kind but one won't survive. Upto the challenge? Then may we present the Lonsdale Club Bag Mitt, Heavy Punch Bag and Punchbag Set. Train harder for only you can better yourself!</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:30px;">Rs.2,616<br />
            	<span>32% off</span>
            </div>
            <div class="buyTxt" style="margin-right:10px;"><a href="http://www.healthkart.com/product/i-against-i-lonsdale-boxing-pack/SPT2509" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/i-against-i-lonsdale-boxing-pack/SPT2509" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/lonsdale-club-bag.jpg" alt="against I, flesh of my flesh,
and mind of my mind" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2504" target="_blank">Be the King of Your Court</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 50px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2504" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/be-the-king-of-your-court.jpg" alt="Be the King of Your Court" border="0" /></a></div>
    	<div class="proData" style="width:430px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Prince T22 Team Triple Squash Bag with TF Quake Squash Racket and Double Dot Yellow Rebel Squash Balls gets you all set for that campaign.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1">Rs.3,103<br />
            	<span>55% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2504" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2503" target="_blank">Serve with Pride</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 100px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Titanium 3000 Tennis Racket from HEAD is a brilliant racket for its price. It’s lightweight yet resilient. Along with HEAD tennis balls, it makes a formidable combination. Up for any challenge, be it clay or grass.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:20px;">Rs.2,521<br />
            	<span>36% off</span>
            </div>
            <div class="buyTxt" style="margin-right:15px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2503" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2503" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/serve-with-pride.jpg" alt="Serve with Pride" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2502" target="_blank">Style and Substance</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 10px 0px 30px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2502" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/style-and-substance.jpg" alt="Style and Substance" border="0" /></a></div>
    	<div class="proData" style="width:400px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Gola Kink Womens Shoes and the Invincible W-105 Tank Top makes a formidable pair. For a woman of
style and substance!</div>
            <div class="btmBrdr" style="width:400px;">&nbsp;</div>

            <div class="price1">Rs.2,572<br />
            	<span>34% off</span>
            </div>
            <div class="buyTxt" style="margin-right:0px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2502" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2501" target="_blank">Just Run, Till the Day Comes</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 70px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">What you leave behind, what you choose to be. And whatever they say, your soul is unbreakable. Invincible T-3 T Shirt and Gola Revive Men’s Shoes, for those who believe in taking the road less travelled.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:30px;">Rs.2,273<br />
            	<span>35% off</span>
            </div>
            <div class="buyTxt" style="margin-right:0px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2501" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/invincible-w-116-reflective-capri/SPT2501" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/just-run-till-the-day-comes.jpg" alt="Just Run, Till the Day Comes" border="0"/></a></div>
  	</div>

    <div class="down" style="height:50px;">&nbsp;</div>
  <h2>Celebrate Life, with Extend Nutrion</h2>
  <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  <div class="extTxtCenter">Extend is an internationally acclaimed line of products that helps you
control your sugar for upto 9 hours. Formulated by world renowned diabetes experts,
Extend holds 17 international patents and a brilliant taste to go!</div>
	<div class="down" style="padding:10px 0px 30px 0px;" align="center"><a href="http://www.healthkart.com/brand/nutrition/extend" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/extend-products.jpg" alt="Extend" border="0" /></a></div>
  	<h3><a href="http://www.healthkart.com/product/extend-wholesome-combo/CMB-DIA26" target="_blank">The Wholesome Option</a></h3>
    <div class="down htTen">&nbsp;</div>
    <div class="down">
   	  <div class="extendTxtNew" style="width:350px; padding:10px 10px 0px 90px; text-align:right;">Extend Savory Crisps, Indulgent Drizzles and
Delightful Bars, in a neat little pack. Perfect for fitness fanatics and diabetics alike.</div>
    	<div class="proData" style="width:400px; padding-top:15px;">
            <div class="btmBrdr" style="width:380px;">&nbsp;</div>
            <div class="price1">Rs.1,050<br />
            	<span>30% off</span>
            </div>
            <div class="buyTxt" style="margin-right:20px;"><a href="http://www.healthkart.com/product/extend-wholesome-combo/CMB-DIA26" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down" style="height:50px;">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/extend-savory-crisps-combo/CMB-DIA24" target="_blank">The Savory Crisps Option</a></h3>
    <div class="down htTen">&nbsp;</div>
    <div class="down">
    	<div class="proData" style="width:410px; padding:15px 0px 0px 120px;">
            <div class="btmBrdr" style="width:380px; float:right;">&nbsp;</div>
            <div class="price1" style="margin-left:10px;">Rs.1,050<br />
            	<span>30% off</span>
            </div>
            <div class="buyTxt" style="margin-right:10px;"><a href="http://www.healthkart.com/product/extend-savory-crisps-combo/CMB-DIA24" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
        <div class="extendTxtNew" style="width:300px; float:left; padding:7px 10px 0px 15px;">A pack of 6 Extend Savory Crisps. Keep hunger and sugar at bay!</div>
 	</div>
    <div class="down" style="height:50px;">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/extend-indulgent-drizzles-combo/CMB-DIA25" target="_blank">The Indulgent Drizzles Option</a></h3>
    <div class="down htTen">&nbsp;</div>
    <div class="down">
   	  <div class="proImage extendTxt" style="width:350px; padding:10px 10px 0px 70px; text-align:right;">Extend Indulgent Drizzles in a pack of 6.
Keep within reach of your mother and child. We are serious about this.</div>
    	<div class="proData" style="width:400px; padding-top:15px;">
            <div class="btmBrdr" style="width:400px;">&nbsp;</div>
            <div class="price1">Rs.1,050<br />
            	<span>30% off</span></div>
            <div class="buyTxt" style="margin-right:20px;"><a href="http://www.healthkart.com/product/extend-indulgent-drizzles-combo/CMB-DIA25" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down" style="height:50px;">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/extend-delighful-bars-combo/CMB-DIA23" target="_blank">The Delightful Bars Option</a></h3>
    <div class="down htTen">&nbsp;</div>
    <div class="down">
    	<div class="proData" style="width:380px; padding:15px 0px 0px 170px;">
            <div class="btmBrdr" style="width:380px;">&nbsp;</div>
            <div class="price1">Rs.1,050<br />
            	<span>30% off</span>
            </div>
            <div class="buyTxt" style="margin-right:0px;"><a href="http://www.healthkart.com/product/extend-delighful-bars-combo/CMB-DIA23" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
        <div class="extendTxtNew" style="width:300px; padding:7px 10px 0px 10px;">A pack of 6 Delightful Bars. No more sugar spikes
or the crashes after the 4pm snack. Guaranteed!</div>
 	</div>
  <div class="down" style="height:50px;">&nbsp;</div>
  <h2>The Festive Glow</h2>
  <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/vibes-unisex-:beauty-packages-across-delhi-kolkata-hyderabad-bangalore/CMBS-132" target="_blank">This Diwali, Indulge Yourself with Vibes</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 100px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Avail amazing Unisex Beauty Packages and Lotions from globally trusted brands, all under one roof that too at discounted rates.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:30px;">Rs.994<br />
            	<span>87% off</span>
            </div>
            <div class="buyTxt" style="margin-right:0px;"><a href="http://www.healthkart.com/product/vibes-unisex-:beauty-packages-across-delhi-kolkata-hyderabad-bangalore/CMBS-132" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/vibes-unisex-:beauty-packages-across-delhi-kolkata-hyderabad-bangalore/CMBS-132" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/vibes.jpg" alt="This Diwali, Indulge Yourself with Vibes" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/the-weight-monitor-online-weight-loss-diet-program-with-bodyfuelz-fat-burner/CMB130" target="_blank">Complete Package for Weight-loss</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 130px;"><a href="http://www.healthkart.com/product/the-weight-monitor-online-weight-loss-diet-program-with-bodyfuelz-fat-burner/CMB130" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/the-weight-monitor.jpg" alt="Complete Package for Weight-loss" border="0" /></a></div>
    	<div class="proData" style="width:430px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">This Diwali, Look gorgeous! Get slimmer with Ishi Khosla's Online Weight-loss Program. Also avail an effective & strong Bodyfuelz fat burner with this Combo.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1">Rs.2,954<br />
            	<span>26% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/the-weight-monitor-online-weight-loss-diet-program-with-bodyfuelz-fat-burner/CMB130" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/apollo-clinic:-master-health-check-up-valid-across-delhi-ncr-only/CMBS-135" target="_blank">Health & Bulk</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 100px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Stay Healthy & Fit this Diwali with a Complete Health Check-up with Medall & Bulk-up appropriately with MuscleBlaze Mass Gainer.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:30px;">Rs.2,362<br />
            	<span>25% off</span>
            </div>
            <div class="buyTxt" style="margin-right:0px;"><a href="http://www.healthkart.com/product/apollo-clinic:-master-health-check-up-valid-across-delhi-ncr-only/CMBS-135" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/apollo-clinic:-master-health-check-up-valid-across-delhi-ncr-only/CMBS-135" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/medall.jpg" alt="Overall Wellness / Health & Bulk" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/diabetes-management-plan-+-contour-pack-of-meter-strips-lancing-device/SER120" target="_blank">Manage Diabetes with Ease</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 80px;"><a href="http://www.healthkart.com/product/diabetes-management-plan-+-contour-pack-of-meter-strips-lancing-device/SER120" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/contour.jpg" alt="Manage Diabetes with Ease" border="0" /></a></div>
    	<div class="proData" style="width:430px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Enjoy the Benefits of this Great Custom-Designed Combo to cure Diabetes. Avail Chi Health's Diabetes Management Program along with a Complete pack of Glucometer, strips and lancent device by Bayer.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1">Rs.2,289<br />
            	<span>26% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/diabetes-management-plan-+-contour-pack-of-meter-strips-lancing-device/SER120" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/the-weight-monitor-online-weight-loss-diet-program-with-bodyfuelz-fat-burner/CMBS-134" target="_blank">The Gift of Health and Fitness</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 100px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">This Diwali, gift your loved ones a special package designed for people with Diabetes and cardiac problems. The combo also contains snacks for diabetics from the amazing brand Extend which facilitates a fit and healthy living.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:30px;">Rs.925<br />
            	<span>50% off</span>
            </div>
            <div class="buyTxt" style="margin-right:0px;"><a href="http://www.healthkart.com/product/the-weight-monitor-online-weight-loss-diet-program-with-bodyfuelz-fat-burner/CMBS-134" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/the-weight-monitor-online-weight-loss-diet-program-with-bodyfuelz-fat-burner/CMBS-134" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/onquest.jpg" alt="The Gift of Health and Fitness" border="0"/></a></div>
  	</div>
    <div class="down" style="height:50px;">&nbsp;</div>
    <h2>Ideas for those little devils in the house</h2>
    <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  <h3><a href="http://www.healthkart.com/product/kindergarten-bag-of-goodies/CMB-BAB0356" target="_blank">Kindergarten Bag of Goodies</a></h3>
    <div class="down">
    	<div class="proImage" style="padding:7px 20px 0px 50px;"><a href="http://www.healthkart.com/product/kindergarten-bag-of-goodies/CMB-BAB0356" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/kindergarten-bag-of-goodies.jpg" alt="Kindergarten Bag of Goodies" border="0"/></a></div>
   	  <div class="proData" style="width:440px; padding-top:10px;">
        	<div class="txt" style="padding-bottom:30px;">A Smash <strong><i>‘rubbish free’</i></strong> lunchbox, a Smash 3D Hippo School Bag and Chhota Bheem Vol.61 ‘The Hiding Game’ is waiting to be picked up. This is all your little devil needs for a little picnic under the tree.</div>
            <div class="btmBrdr" style="width:400px;">&nbsp;</div>

            <div class="price1" style="width:160px;">Rs.749 <br /><span>40% off</span>
            </div>
            <div class="buyTxt" style="margin-right:60px;"><a href="http://www.healthkart.com/product/kindergarten-bag-of-goodies/CMB-BAB0356" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/johnson-s-baby-care-collection-5/BAB128" target="_blank">Johnson's Baby Care Collection</a></h3>
    <div class="down">
  	 	<div class="proData" style="width:400px; padding:10px 0px 0px 120px;">
        	<div class="txt" align="right" style="padding-bottom:10px;">The Johnson's Baby Care Collection is a simple solution to all your baby’s bathing and grooming needs. This convenient combo pack has Johnson's Baby Powder, Johnson's No More Tear Shampoo, Baby Lotion, Baby Cream, Hair Oil, Baby Oil,
Baby Soap, Buds, DVD and Booklet.</div>
            <div class="btmBrdr" style="width:380px; float:right;">&nbsp;</div>

            <div class="price1" style="width:160px">Rs.495<br />
            	<span>1% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/johnson-s-baby-care-collection-5/BAB128" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/johnson-s-baby-care-collection-5/BAB128" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/johnsons-baby-care-collection.jpg" alt="Johnson's Baby Care Collection" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
  <h3><a href="http://www.healthkart.com/product/baby-feeding-hygeine-essentials/CMB-BAB0355" target="_blank">Baby Feeding Hygeine Essentials</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:0px 0px 0px 30px;"><a href="http://www.healthkart.com/product/baby-feeding-hygeine-essentials/CMB-BAB0355" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/baby-feeding-hygeine-essentials.jpg" alt="Baby Feeding Hygeine Essentials" border="0" /></a></div>
    	<div class="proData" style="width:450px; padding-top:10px;">
        	<div class="txt" style="padding-bottom:30px;">Pigeon Bottle & Nipple Brush with Pigeon Bottle Nipple & Vegetable's Cleanser Liquid
and Mee Mee Baby Wrapper/Blanket. A great combo to keep your little devil safe and warm.</div>
            <div class="btmBrdr" style="width:400px;">&nbsp;</div>

            <div class="price1" style="width:160px;">Rs.489<br />
            	<span>15% off</span></div>
            <div class="buyTxt" style="margin-right:50px;"><a href="http://www.healthkart.com/product/baby-feeding-hygeine-essentials/CMB-BAB0355" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down" style="height:50px;">&nbsp;</div>
  <h2>Ideas for expecting Mommies who need all the care</h2>
  <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  <h3><a href="http://www.healthkart.com/product/pigeon-maternity-adjustable-belt/BAB1801" target="_blank">Maternity Adjustable Belt</a></h3>
    <div class="down">
  	 	<div class="proData" style="width:450px; padding:10px 10px 0px 80px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">In the second trimester, it becomes difficult to support the abdomen region because of the additional weight of the baby. Pigeon Maternity Adjustable Belt has been specially designed to provide extra support
during those crucial months so that you can get the required amount of stability and safety.</div>
            <div class="btmBrdr" style="width:430px; float:right;">&nbsp;</div>

            <div class="price1">Rs.1,256<br />
            	<span>10% off</span></div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/pigeon-maternity-adjustable-belt/BAB1801" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/pigeon-maternity-adjustable-belt/BAB1801" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/maternity-adjustable-belt.jpg" alt="Maternity Adjustable Belt" border="0"/></a></div>
  	</div>
    <div class="down" style="height:50px;">&nbsp;</div>
  <h2>Gift Ideas for your gracefully matured loved ones</h2>
  <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  <h3><a href="http://www.healthkart.com/product/karma-walking-stick-ws-131/KARM010" target="_blank">Karma Walking Stick WS-131</a></h3>
  <div class="down">
    	<div class="proImage" style="padding:0px 30px 0px 100px;"><a href="http://www.healthkart.com/product/karma-walking-stick-ws-131/KARM010" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/karma-walking-stick.jpg" alt="Karma Walking Stick" border="0" /></a></div>
    	<div class="proData" style="width:450px; padding-top:10px;">
        	<div class="txt" style="padding-bottom:30px;">A perfect companion for those long walks, Karma walking stick is strong and durable.
But it’s not just that, it folds nice and easy, and is extremely light because of its special plastic composite.</div>
            <div class="btmBrdr" style="width:400px; clear:both;">&nbsp;</div>

          <div class="price1" style="width:170px;">Rs.512<br />
          <span>20% off</span>
          </div>

          <div class="buyTxt" style="margin-right:50px;"><a href="http://www.healthkart.com/product/karma-walking-stick-ws-131/KARM010" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
  </div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/aasaan-senior-mobile-phone/MOB001" target="_blank">Aasaan Senior Mobile Phone</a></h3>
    <div class="down">
  	  <div class="proData" style="width:400px; padding:10px 0px 0px 150px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">A rugged phone with a big numberpad, the Aasan phone from iBall is ideal for those
who find it difficult to see or are easily overwhelmed by technology. A perfect example of calling made easy.</div>
            <div class="btmBrdr" style="width:380px; float:right;">&nbsp;</div>

            <div class="price1">Rs.2,700<br />
            <span>10% off</span></div>
            <div class="buyTxt" style="margin-right:20px;"><a href="http://www.healthkart.com/product/aasaan-senior-mobile-phone/MOB001" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/aasaan-senior-mobile-phone/MOB001" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/aasaan-senior-mobile-phone.jpg" alt="Aasaan Senior Mobile Phone" border="0"/></a></div>
  </div>

  <div class="down" style="height:50px;">&nbsp;</div>
  <h2>Ideas for a Diwali full of vitality</h2>
  <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
  <h3><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB100" target="_blank">Healthier, Glowing Skin and Hair</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:0px 20px 0px 30px;"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB100" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/healthier-glowing-skin-and-hair.jpg" alt="Healthier Glowing Skin and Hair" border="0" /></a></div>
    	<div class="proData" style="width:450px; padding-top:10px;">
        	<div class="txt" style="padding-bottom:30px;">Nature's Bounty Hair, Skin and Nails, Organic India Original Tulsi Tea & Vista Nutritions Vitamin-E tablets together make a great idea to get your hair and skin glow all the more. Do it the organic way and keep yourself healthy for long.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1">Rs.1,499<br />
            	<span>14% off</span>
            </div>
            <div class="buyTxt" style="margin-right:40px;"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB100" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB99" target="_blank">Complete Family Multivitamin Kit</a></h3>
    <div class="down">
  	 	<div class="proData" style="width:400px; padding:10px 0px 0px 120px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Even if you eat healthy, it's likely you're still not getting all the vitamins and minerals your body requires. Here’s an idea for taking care of that. Supractiv Complete Man, Organic India Women's Well Being Capsules & Revital Senior.</div>
            <div class="btmBrdr" style="width:370px; float:right;">&nbsp;</div>

            <div class="price1" style="width:180px;">Rs.500<br />
            	<span>14% off</span>
            </div>
            <div class="buyTxt" style="margin-right:10px;"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB99" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB99" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/complete-family-multivitamin-kit.jpg" alt="Complete Family Multivitamin Kit" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB98" target="_blank">The Healthier Breakfast Set</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:0px 20px 0px 50px;"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB98" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/the-healthier-breakfast-set.jpg" alt="The Healthier Breakfast Set" border="0" /></a></div>
    	<div class="proData" style="width:450px; padding-top:10px;">
        	<div class="txt" style="padding-bottom:30px;">Twinings English Breakfast Tea, GAIA Muesli Fruit & Nut and Nourish Organic Honey Roasted
Almonds make the most important meal of the day a healthier one. A great idea to go along with the fresh fruits you take after your morning jog!</div>
            <div class="btmBrdr" style="width:430px;">&nbsp;</div>

            <div class="price1">Rs.1,000<br />
            <span>9% off</span></div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB98" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB97" target="_blank">Tea Time for Healthy Toddlers?</a></h3>
    <div class="down">
  	 	<div class="proData" style="width:450px; padding:10px 0px 0px 50px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Good things come in small bags this time. Tea bags, to be precise. Traditional Medicinals Tummy Comfort herbal tea, Traditional Medicinals Throat Coat herbal tea &Traditional Medicinals Cold Care herbal tea come packed
with age old remedies to help build your toddler’s immune system early.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:10px;">Rs.1501<br />
            <span>13% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB97" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/twinings-english-breakfast-tea/NUTCMB97" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/tea-time-for-healthy-toddlers.jpg" alt="Tea Time For Healthy Toddlers" border="0"/></a></div>
  	</div>
  <div class="down" style="height:50px;">&nbsp;</div>
  <h2>Be prepared this Diwali</h2>
     <div class="down pad1"><img src="${pageContext.request.contextPath}/images/diwali2012/floral.jpg" alt="Floral" border="0" /></div>
    <h3><a href="http://www.healthkart.com/product/omron-hem-7111-onetouch-ultra-2-meter/CMB-HD002" target="_blank">Dynamic Duo!</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 100px;"><a href="http://www.healthkart.com/product/omron-hem-7111-onetouch-ultra-2-meter/CMB-HD002" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/omron-HEM-and-onetouch-ultra-meter.jpg" alt="Omron HEM-7111 & OneTouch Ultra 2 Meter" border="0" /></a></div>
    	<div class="proData" style="width:430px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">A fantastic combo for those suffering from Hypertension or Diabetes. You can comfortably keep a record on your Sugar level & BP rate while staying at home.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1">Rs.2,982<br />
            	<span>40% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/omron-hem-7111-onetouch-ultra-2-meter/CMB-HD002" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
    <div class="down">&nbsp;</div>
     <h3><a href="http://www.healthkart.com/product/omron-hem-7200-accu-chek-active-meter/CMB-HD003" target="_blank">Combat High Blood Pressure & Diabetes</a></h3>
    <div class="down">
 	  	<div class="proData" style="width:450px; padding:10px 0px 0px 60px;">
        	<div class="txt" align="right" style="padding-bottom:30px;">Own Omron BP Monitor and Accu-Check Active meter and keep a check on your Blood Pressure and High Sugar Levels without visiting the doctor.</div>
            <div class="btmBrdr" style="width:410px; float:right;">&nbsp;</div>

            <div class="price1" style="margin-left:30px;">Rs.3,850<br />
            	<span>35% off</span>
            </div>
            <div class="buyTxt" style="margin-right:0px;"><a href="http://www.healthkart.com/product/omron-hem-7200-accu-chek-active-meter/CMB-HD003" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
      </div>
    	<div class="proImage" style="padding:0px 0px 0px 20px;"><a href="http://www.healthkart.com/product/omron-hem-7200-accu-chek-active-meter/CMB-HD003" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/omron-HEM-and-accu-chek-ctive-meter.jpg" alt="Combat High Blood Pressure & Diabetes" border="0"/></a></div>
  	</div>
    <div class="down">&nbsp;</div>
    <h3><a href="http://www.healthkart.com/product/equinox-irresistible-offer-free-equinox-body-fat-monitor-with-equinox-bp-monitor/CMB-HD005" target="_blank">Restore Your Health with Equinox</a></h3>
    <div class="down">
   	  <div class="proImage" style="padding:10px 20px 0px 50px;"><a href="http://www.healthkart.com/product/equinox-irresistible-offer-free-equinox-body-fat-monitor-with-equinox-bp-monitor/CMB-HD005" target="_blank"><img src="${pageContext.request.contextPath}/images/diwali2012/equinox-irresistible-offer.jpg" alt="Omron HEM-7111 & OneTouch Ultra 2 Meter" border="0" /></a></div>
    	<div class="proData" style="width:430px; padding-top:15px;">
        	<div class="txt" style="padding-bottom:30px;">Avail this Amazing Combo to Keep a track of your Blood Pressure Level. Also monitor the Fat Gained because of the number of Sweets You take in, this Festive season.</div>
            <div class="btmBrdr" style="width:420px;">&nbsp;</div>

            <div class="price1">Rs.1,740<br />
            	<span>50% off</span>
            </div>
            <div class="buyTxt"><a href="http://www.healthkart.com/product/equinox-irresistible-offer-free-equinox-body-fat-monitor-with-equinox-bp-monitor/CMB-HD005" target="_blank">buy this now</a></div>
            <div class="clr">&nbsp;</div>
        </div>
 	</div>
  <div class="down">&nbsp;</div>
</div>

 <div class="cl"></div>

    <!-- Container Ends-->

 <!-- AddThis Button BEGIN -->
<div class="addthis_toolbox addthis_floating_style addthis_counter_style" style="right:10px;top:50px;">
<a class="addthis_button_facebook_like" fb:like:layout="box_count"></a>
<a class="addthis_button_tweet" tw:count="vertical"></a>
<a class="addthis_button_google_plusone" g:plusone:size="tall"></a>
<a class="addthis_counter"></a>
</div>
<script type="text/javascript" src="http://s7.addthis.com/js/300/addthis_widget.js#pubid=ra-509261de45f6e306"></script>
<!-- AddThis Button END --    >
  
</s:layout-component>


</s:layout-render>

