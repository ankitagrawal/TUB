<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Rakhi Gift Ideas">

<s:layout-component name="htmlHead">
  <style type="text/css">
	body { background:url(../images/rakhi/texture.gif); }
	.cl { clear:both; }
	#rakhiContainer { margin:15px auto; width:960px; overflow:auto; padding-bottom:260px; }
	.leftCol { width:465px; float:left; margin-top:35px; }
	.rightCol { width:465px; float:right; margin-top:35px; }
	.productThumb { margin-top:25px; padding:20px; float:left;  border:3px solid #fdb913; width:419px; overflow:auto; background:#fff; }
	.productThumb p { margin:0; padding:0; }
	.productThumb .tThumb { float:left; margin-top:20px; margin-right:23px; }
	.productThumb .tNum { background:url(../images/rakhi/number.gif) no-repeat; float:left; width:27px; height:27px; font-size:16px; color:#785500; margin-right:15px; text-align:center; line-height:27px; }
	.productThumb h3  { font-size:15px;  line-height:27px; margin-bottom:10px; font-weight:bold; }
	.productThumb h3 a { font-size:15px; color:#505050; border:none; }
	.productThumb h3 a:hover { color:#000; }
	.productThumb .tDesc { font-size:13px; background:#f0f0f0; border:1px solid #d9d9d9; width:214px; height:85px; float:right; padding:5px; margin-bottom:35px; line-height: 14px; }
	.productThumb .tPrice { font-size:16px; clear:right;  color:#2484c6; margin-bottom:10px; }
	.productThumb .tPrice span { font-size:12px; color:#a1a1a1; text-decoration:line-through; margin-right:15px;  }
	.productThumb .tSave { float:left; font-size:16px; color:#e73721; font-weight:bold; }
	.productThumb .tBuy { float:right; }
	.productThumb a { border:none; }

	#footer { margin:0 auto; width:960px; background: #1d456b; overflow:auto; border-top: 3px solid #f87500; }
	.contents {
    margin: 15px auto;
    margin-bottom: 0;
}


 .contents .column {
    display: inline;
    float: left;
    margin-left: 10px;
    margin-right: 10px;
    width: 150px;
}


 .contents .column h5 {
    color: white;
    font-weight: normal;
    font-size: 14px;
    line-height: 21px;
    border-bottom: 1px solid #f87500;
    text-shadow: 0 1px 0 rgba(0, 0, 0, 0.2);
    -moz-box-shadow: 0 1px 0 rgba(0, 0, 0, 0.2);
    -webkit-box-shadow: 0 1px 0 rgba(0, 0, 0, 0.2);
    -o-box-shadow: 0 1px 0 rgba(0, 0, 0, 0.2);
    box-shadow: 0 1px 0 rgba(0, 0, 0, 0.2);
}


 .contents .column ul {
    margin: 0;
    padding: 0;
    list-style: none;
}


 .contents .column ul li {
    margin-top: 0.45em;
    margin-bottom: 0.45em;
}


 .contents .column a {
    font-size: 10px;
    line-height: 15px;
    color: white;
    border-bottom-color: transparent;
}


.contents .column a:hover {
    border-bottom-color: #f87500;
}


	</style>

</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Rakhi Gifts</span>

    <h1 class="title">
      Buy Rakhi Gifts online
    </h1>
  </div>
  
</s:layout-component>

<s:layout-component name="metaDescription">Rakhi gifts from HealthKart.</s:layout-component>
<s:layout-component name="metaKeywords">rakhi gifts, rakhi gift ideas, special rakhi offers</s:layout-component>

<s:layout-component name="content">
<div class="cl"></div>
    <div id="rakhiContainer">
    <img src="${pageContext.request.contextPath}/images/rakhi/banner.jpg" width="960" height="349" alt="rakhi banner" />
    <div class="cl"></div>
    <!-- Left Column Starts-->
    <div class="leftCol">
      <img src="${pageContext.request.contextPath}/images/rakhi/sisters.gif" width="460" height="46" alt="Gift Ideas for Sisters" />
      <div class="cl"></div>
    	<div class="productThumb">
        <p class="tNum">1</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-women-1/RKHCMB5">For Your Darling Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-1.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Bath &amp; Body Works Body Lotion White Citrus + Victoria's Secret Shimmer Mist - Amber Romance
        </div>

        <p class="tPrice">
        <span>Rs 1,620</span>
        Rs 1,539
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-women-1/RKHCMB5"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">2</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-women-2/RKHCMB6">For Your Snow-White Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-2.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Saint Pure Sensual Merlot Whipped Face and Body Butter + Saint Pure Strawberries & Cream Luscious Face Soap + Saint Pure Cinnamon Spa Face and Body Sugar Scrub Cubes
        </div>

        <p class="tPrice">
        <span>Rs 2,970</span>
        Rs 2,822
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-women-2/RKHCMB6"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">3</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-women-3/RKHCMB7">For Your Peppy Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-3.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Maybelline The Colossal Kajal + Maybelline Color Senstational Lip Color + Maybelline Colorama Nail Color + Miners Glitzy Kitz Lipstick Pouch</div>
        <p class="tPrice">
        <span>Rs 1,100</span>
        Rs 1,045
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-women-3/RKHCMB7"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">4</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-women-4/RKHCMB8">For Your Diva Like Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-4.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">NYX Mosaic Powder Blush + NYX Brush On Lip Gloss + NYX Lip Lacquer Pot
        </div>

        <p class="tPrice">
        <span>Rs 1,725</span>
        Rs 1,639
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-women-4/RKHCMB8"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">5</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-women-5/RKHCMB9">For Your Rapunzel Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-5.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Remington CI19 Tong + Kent LPB1 Large Paddle Brush Floral Design
        </div>

        <p class="tPrice">
        <span>Rs 2,574</span>
        Rs 2,445
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-women-5/RKHCMB9"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">6</p>
        <h3><a href="http://www.healthkart.com/product/nutritious-rakhi-gift-2-young-sis/NUTCMB32">For Your Youthful Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-6.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">With this combo, help your sister maintain her charming youth
        </div>

        <p class="tPrice">
        <span>Rs 2,289</span>
        Rs 1,831
        </p>
        <p class="tSave">You Save 20%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/nutritious-rakhi-gift-2-young-sis/NUTCMB32"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">7</p>
        <h3><a href="http://www.healthkart.com/product/nutritious-rakhi-gift-for-40+-sis/NUTCMB33">For Your Elder Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-7.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Elegance knows no age. This Combo is meant for the maintenance of your classy sister.
        </div>

        <p class="tPrice">
        <span>Rs 2,998</span>
        Rs 2,398
        </p>
        <p class="tSave">You Save 20%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/nutritious-rakhi-gift-for-40%2B-sis/NUTCMB33"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">8</p>
        <h3><a href="http://www.healthkart.com/product/nutritious-rakhi-gift-2-teenage-sis/NUTCMB34">For Your Health Conscious Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-8.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Assure your chirpy sister doesn’t lose her good health. Make sure she gets proper nutrition with this combo.
        </div>

        <p class="tPrice">
        <span>Rs 1,479</span>
        Rs 1,183
        </p>
        <p class="tSave">You Save 20%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/nutritious-rakhi-gift-2-teenage-sis/NUTCMB34"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">9</p>
        <h3><a href="http://www.healthkart.com/product/kool-kidz-infant-musical-cozy-coupe-asst/BAB1261">For Your Baby Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-9.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">See your little sibling smiling with this cute, colourful and musical toy.</div>

        <p class="tPrice">
        <span>Rs 999</span>
        Rs 849
        </p>
        <p class="tSave">You Save 15%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/kool-kidz-infant-musical-cozy-coupe-asst/BAB1261"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">10</p>
        <h3><a href="http://www.healthkart.com/product/cobra-magnum/PP001">Protecting Your Sister Always</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-10.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Let your sister be her own cop. Protect her from the psychopaths.</div>

        <p class="tPrice">

        Rs 499
        </p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/cobra-magnum/PP001"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">11</p>
        <h3><a href="http://www.healthkart.com/product/casper-3-in-alarm/COP003">For Your Night Riding Hood</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-11.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Her safety is your priority. Protect her at all times with this 3-in-1 alarm.
        </div>

        <p class="tPrice">

        Rs 450
        </p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/casper-3-in-alarm/COP003"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">12</p>
        <h3><a href="http://www.healthkart.com/product/bremed-facial-sauna-with-aroma-inhaler-bd7100-steamer/BREMED023">Portable Beauty Parlour</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-12.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Gift your sister the luxury of beauty parlour at home.</div>

        <p class="tPrice">

        <span>Rs 2,250</span>
        Rs 1,699
        </p>
        <p class="tSave">You Save 24%</p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/bremed-facial-sauna-with-aroma-inhaler-bd7100-steamer/BREMED023"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">13</p>
        <h3><a href="http://www.healthkart.com/product/i-m-waist-shaper-nb-507/RD001">Desired Figure For Your Sister</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-13.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Make sure your sister achieves the figure she always desired.</div>

        <p class="tPrice">


        Rs 480
        </p>


        <p class="tBuy"><a href="http://www.healthkart.com/product/i-m-waist-shaper-nb-507/RD001"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">14</p>
        <h3><a href="http://www.healthkart.com/product/special-girl-boy-kids-combo/CMB-EYE02">For Your Li'l Miss India</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/sister-14.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Hotwheels AI1015 C104 + Barbie AI1014 C161<br />
        Pretty little sunglasses for pretty little faces.</div>

        <p class="tPrice">
        <span>Rs 3,200</span>
        Rs 960
        </p>
        <p class="tSave">You Save 70%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/special-girl-boy-kids-combo/CMB-EYE02"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

    </div>
    <!-- Left Column Ends-->

    <!-- Right Column Starts-->
    <div class="rightCol">
      <img src="${pageContext.request.contextPath}/images/rakhi/brothers.gif" width="460" height="46" alt="Gift Ideas for Brothers" />
      <div class="cl"></div>
    	<div class="productThumb">
        <p class="tNum">1</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-men-1/RKHCMB1">For Your High Maintenance Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-1.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Gillette Fusion ProGlide (Manual) + Gillette Fusion ProGlide Cartridges + Gillette Series Shaving Foam + Gillette Deodorant
        </div>

        <p class="tPrice">
        <span>Rs 2,207</span>
        Rs 2,097
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-men-1/RKHCMB1"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">2</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-men-2/RKHCMB2">For Your Handsome &amp; Sleek Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-2.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Andis NT1 Fast Cordless Personal Trimmer + Denim After Shave Lotion + Imperial Leather Men Energise Shower Gel + Park Avenue Body Deodorant- Classic Range
        </div>

        <p class="tPrice">
        <span>Rs 1,051</span>
        Rs 998
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-men-2/RKHCMB2"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">3</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-men-3/RKHCMB3">For Your Brand Conscious Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-3.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Adidas Aftershave + Adidas Men Combo-Victory League + Andis MNT3 Easy Trim Battery Operated Personal Trimmer
        </div>

        <p class="tPrice">
        <span>Rs 2,023</span>
        Rs 1,922
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-men-3/RKHCMB3"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">4</p>
        <h3><a href="http://www.healthkart.com/product/rakhee-combo-men-4/RKHCMB4">For Your Well Groomed Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-4.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Park Avenue Body Deodorant- Classic Range + Gatsby Body Shower Gel + Denim EDT
        </div>

        <p class="tPrice">
        <span>Rs 607</span>
        Rs 577
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/rakhee-combo-men-4/RKHCMB4"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">5</p>
        <h3><a href="http://www.healthkart.com/product/nutritious-gift-2-working-bro/NUTCMB35">For Your Workaholic Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-5.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Nature's Bounty Ultra Man High Potency + Vitamin Shoppe Korean Ginseng
        </div>

        <p class="tPrice">
        <span>Rs 2,599 </span>
        Rs 2,079
        </p>
        <p class="tSave">You Save 20%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/nutritious-gift-2-working-bro/NUTCMB35"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">6</p>
        <h3><a href="http://www.healthkart.com/product/nutritious-gift-2-body-bulider-bro/NUTCMB36">For Your Muscle Head Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-6.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">MusclePharm Combat Powder + HK Shaker &amp; Blender Bottle
        </div>

        <p class="tPrice">
        <span>Rs 5,599</span>
        Rs 4,479
        </p>
        <p class="tSave">You Save 20%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/nutritious-gift-2-body-bulider-bro/NUTCMB36"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">7</p>
        <h3><a href="http://www.healthkart.com/product/nutritious-gift-2-big-bro/NUTCMB37">For Your Elder Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-7.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Vitamin Shoppe Ultimate Men's 50+ Multivitamin + Osteo Bi Flex Advanced
        </div>

        <p class="tPrice">
        <span>Rs 4,598</span>
        Rs 3,678
        </p>
        <p class="tSave">You Save 20%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/nutritious-gift-2-big-bro/NUTCMB37"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <p class="tNum">8</p>
        <h3><a href="http://www.healthkart.com/product/kool-kidz-discover-sounds-camera-pink/BAB1298">For Your Little Brotographer</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-8.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">See your little angel becoming a skilled photographer
        </div>

        <p class="tPrice">
        <span>Rs 899</span>
        Rs 799
        </p>
        <p class="tSave">You Save 11%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/kool-kidz-discover-sounds-camera-pink/BAB1298"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">9</p>
        <h3><a href="http://www.healthkart.com/product/wallet-alarm/COP004">For Your Irresponsible Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-9.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Make sure your brother is safe; money wise.
        </div>

        <p class="tPrice">

        Rs 200
        </p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/wallet-alarm/COP004"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">10</p>
        <h3><a href="http://www.healthkart.com/product/littmann-special-colour-classic-ii-s.e-brass-copper/LITT005">For Your Doctor Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-10.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">A special gift for your doctor brother.
        </div>

        <p class="tPrice">
        <span>Rs 7,100</span>
        Rs 5,899
        </p>
        <p class="tSave">You Save 17%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/littmann-special-colour-classic-ii-s.e-brass-copper/LITT005"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">11</p>
        <h3><a href="http://www.healthkart.com/product/arm-sling-net/RF005">Supporting Brother-In-Need</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-11.jpg" width="170" height="170"  class="tThumb" />
        <div class="tDesc">Comfort your brother in the time of need.
        </div>

        <p class="tPrice">
         <span>Rs 250</span>
        Rs 200
        </p>
        <p class="tSave">You Save 20%</p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/arm-sling-net/RF005"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">12</p>
        <h3><a href="http://www.healthkart.com/product/super-discount%3A-reebok-and-tnf-sunglasses-combo/CMB-EYE03">For Your "Dabangg" Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-12.jpg" width="170" height="170" class="tThumb" />
        <div class="tDesc">Aviators never go out of fashion. A stylish gift this rakhee for your classy sibling.
        </div>

        <p class="tPrice">
        <span>Rs 4,998</span>
        Rs 1,599
        </p>
        <p class="tSave">You Save 68%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/super-discount%3A-reebok-and-tnf-sunglasses-combo/CMB-EYE03"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">13</p>
        <h3><a href="http://www.healthkart.com/product/adidas-men-combo-pure-game/ADIDAS14">For Your Party Freak Brother</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-13.jpg" width="170" height="170" class="tThumb" />
        <div class="tDesc">Gift your brother a heavy duty fragrance pack that lasts longer.
        </div>

        <p class="tPrice">
        <span>Rs 699</span>
        Rs 664
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/adidas-men-combo-pure-game/ADIDAS14"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <p class="tNum">14</p>
        <h3><a href="http://www.healthkart.com/product/auravedic-hair-loss-treatment-for-dry-hair/ARVDC27">Save Your Brother From Hair Loss</a></h3>
        <div class="cl"></div>
        <img src="${pageContext.request.contextPath}/images/rakhi/brother-14.jpg" width="170" height="170" class="tThumb" />
        <div class="tDesc">Be a caring sister so that he doesn’t lose his youthful looks.
        </div>

        <p class="tPrice">

        Rs 400
        </p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/auravedic-hair-loss-treatment-for-dry-hair/ARVDC27"><img src="${pageContext.request.contextPath}/images/rakhi/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>
    </div>
    <div class="cl"></div>
    <!-- Right Column Ends-->
    </div>
   

</s:layout-component>


</s:layout-render>

