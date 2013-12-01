<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Breast Feeding Week">

<s:layout-component name="htmlHead">
  <style type="text/css">
		.cl { clear:both; }
	#feedingContainer { margin:15px auto; width:960px; overflow:auto; clear:both; }
	.leftCol { width:465px; float:left; margin-top:35px; }
	.rightCol { width:465px; float:right; margin-top:35px; }
	.productThumb { margin-bottom:25px; padding:20px; float:left;  border:1px solid #efefef; width:423px; overflow:auto; background:#fff; }
	.productThumb p { margin:0; padding:0; }
	.productThumb .tThumb { float:left; margin-right:23px; }
	.productThumb h3  { font-size:14px;  margin-bottom:10px; font-weight:normal; }
	.productThumb h3 a { font-size:14px; color:#505050; border:none; }
	.productThumb h3 a:hover { color:#000; }
	.productThumb .tDesc { font-size:13px; background:#f0f0f0; border:1px solid #d9d9d9; width:214px; height:85px; float:right; padding:5px; margin-bottom:35px; line-height: 14px; }
	.productThumb .tPrice { font-size:16px; clear:right;  color:#2484c6; margin-bottom:10px; }
	.productThumb .tPrice span { font-size:12px; color:#a1a1a1; text-decoration:line-through; margin-right:15px;  }
	.productThumb .tSave { font-size:16px; color:#e73721; font-weight:bold; height:30px; }
	.productThumb .tBuy { margin-top:10px;  }
	.productThumb a { border:none; }
      .feedContent { clear:both; }
      .feedContent p { font-size:12px; text-align:justify; padding-left:0; }
        </style>


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Breast Feeding Week</span>

    <h1 class="title">
      Breast Feeding Week
    </h1>
  </div>
  
</s:layout-component>

<s:layout-component name="metaDescription">Breast Feeding Week Special Products from HealthKart.</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>

<s:layout-component name="content">
<div id="feedingContainer">
    <img src="${pageContext.request.contextPath}/images/breastfeeding/banner.jpg" width="960" height="220" alt="breast feeding banner" />
    <div class="cl"></div>
    <div class="feedContent">
        <p>Probably the most cherished moment in the journey of motherhood is feeding your baby. Breast Milk is undoubtedly the richest source of nutrition for the baby. It contains proteins, carbohydrates, minerals and other nutrients which are necessary for the baby’s overall growth and nourishment.</p>
        <p>The initial milk produced is often known as colustrum containing immunoglobuline lgA which make sure that your baby’s immune system functions properly. Cow’s milk does not contain sufficient Vitamin E or essential fatty acids & has protein, sodium and potassium in amounts which may hamper the infant’s kidney. Thus, there is no doubt that mother’s milk is the best source of nutrition to the baby.</p>
        <p>HealthKart is celebrating World Breast Feeding Week to herald the magnificence of motherhood. We believe that mother’s milk has no substitute and is vital for an infant's growth & development. With every parenting related order, we will give out free gifts such as a complete book on breast feeding, a sample nipple cream and a complementary pen.</p>
        <p>Come celebrate the joy of motherhood with us!</p>
    </div>
    <!-- Left Column Starts-->
    <div class="leftCol">

    	<div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB446.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/medela-contact-nipple-shields/BAB446">Medela Contact Nipple Shields</a></h3>


        <p class="tPrice">

        Rs 900
        </p>
        <p class="tSave"></p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/medela-contact-nipple-shields/BAB446"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
       <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB058.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/pigeon-disposable-breast-pads/BAB058">Pigeon Disposable Breast Pads</a></h3>


        <p class="tPrice">
        <span>Rs 50</span>
        Rs 45</p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/pigeon-disposable-breast-pads/BAB058"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB050.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/farlin-breast-shield/BAB050">Farlin Breast Shield</a></h3>


        <p class="tPrice">
        <span>Rs 210</span>
        Rs 189
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/farlin-breast-shield/BAB050"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
         <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB414.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/philips-avent-nipple-protector/BAB414">Philips AVENT Nipple Protector</a></h3>


        <p class="tPrice">
        <span>Rs 295</span>
        Rs  266
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/philips-avent-nipple-protector/BAB414"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
         <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB047.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/farlin-electric-breast-pump-kit/BAB047">Farlin Electric Breast Pump Kit</a></h3>


        <p class="tPrice">
        <span>Rs 2,750</span>
        Rs 2,499
        </p>
        <p class="tSave">You Save 9%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/farlin-electric-breast-pump-kit/BAB047"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB444.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/medela-disposable-bra-pads/BAB444">Medela Disposable Bra Pads</a></h3>


        <p class="tPrice">

        Rs 575
        </p>
        <p class="tSave"></p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/medela-disposable-bra-pads/BAB444"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB382.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/nuby-breast-express-breast-pump/BAB382">Nuby Breast Express Breast Pump</a></h3>
        <p class="tPrice">
        <span>Rs 775</span>
        Rs 599
        </p>
        <p class="tSave">You Save 23%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/nuby-breast-express-breast-pump/BAB382"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB1045.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/tolly-joy-baby-nipple-box/BAB1045">Tolly Joy Baby Nipple Box </a></h3>


        <p class="tPrice">
        <span>Rs 115</span>
        Rs  98
        </p>
        <p class="tSave">You Save 15%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/tolly-joy-baby-nipple-box/BAB1045"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB055.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/pigeon-manual-breast-pump/BAB055">Pigeon Manual Breast Pump </a></h3>
        <p class="tPrice">
        <span>Rs 1,850</span>
        Rs 1,665
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/pigeon-manual-breast-pump/BAB055"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
       <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB057.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/pigeon-nipple-shield/BAB057">Pigeon Nipple Shield
</a></h3>


        <p class="tPrice">
        <span>Rs 195</span>
        Rs 180
        </p>
        <p class="tSave">You Save 8%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/pigeon-nipple-shield/BAB057"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
       <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB447.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/medela-washable-bra-pads/BAB447">Medela Washable Bra Pads
</a></h3>

        <p class="tPrice">

        Rs 1,150
        </p>
         <p class="tSave"></p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/medela-washable-bra-pads/BAB447"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB401.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/philips-avent-ultra-comfort-breast-pads/BAB401">Philips AVENT Ultra Comfort Breast Pads
</a></h3>


        <p class="tPrice">

        <span>Rs 595</span>
        Rs 536
        </p>
        <p class="tSave">You Save 10%</p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/philips-avent-ultra-comfort-breast-pads/BAB401"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
         <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB788.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/mee-mee-maternity-nursing-pads/BAB788">Mee Mee Maternity Nursing Pads
</a></h3>


        <p class="tPrice">

        <span>Rs 145</span>
        Rs 123
        </p>
        <p class="tSave">You Save 15%</p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/mee-mee-maternity-nursing-pads/BAB788"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB054.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/pigeon-silent-electric-breast-pump/BAB054">Pigeon Silent Electric Breast Pump
</a></h3>


        <p class="tPrice">
        <span>Rs 7,900</span>
        Rs 6,899
        </p>
        <p class="tSave">You Save 13%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/pigeon-silent-electric-breast-pump/BAB054"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB156.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/pigeon-breast-care-gel/BAB156">Pigeon Breast Care Gel

</a></h3>


        <p class="tPrice">
        <span>Rs 935</span>
        Rs 842
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/pigeon-breast-care-gel/BAB156"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
      </div>

    </div>
    <!-- Left Column Ends-->

    <!-- Right Column Starts-->
    <div class="rightCol">


    	<div class="productThumb">
       <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB049.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/farlin-disposable-breast-pad/BAB049">Farlin Disposable Breast Pad</a></h3>
        <p class="tPrice">
        <span>Rs 450</span>
        Rs 363</p>
        <p class="tSave">You Save 20%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/farlin-disposable-breast-pad/BAB049"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
       <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB304.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/chicco-antibacterial-breast-pads/BAB304">Chicco Antibacterial Breast Pads</a></h3>
        <p class="tPrice">
        <span>Rs 299</span>
        Rs 269
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/chicco-antibacterial-breast-pads/BAB304"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB306.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/chicco-sure-safe-nursing-cleansing-wipes/BAB306">Chicco Sure-Safe Nursing Cleansing Wipes </a></h3>
        <p class="tPrice">
        <span>Rs 249</span>
        Rs 224
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/chicco-sure-safe-nursing-cleansing-wipes/BAB306"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB046.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/farlin-effort-saving-manual-breast-pump/BAB046">Farlin Effort-Saving Manual Breast Pump </a></h3>


        <p class="tPrice">
        <span>Rs 1,695</span>
        Rs 1,526
        </p>
        <p class="tSave">You Save 5%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/farlin-effort-saving-manual-breast-pump/BAB046"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB443.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/medela-breastmilk-bottles/BAB443">Medela Breastmilk Bottles </a></h3>


        <p class="tPrice">

        Rs 890
        </p>
        <p class="tSave"></p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/medela-breastmilk-bottles/BAB443"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB045.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/farlin-manual-breast-pump/BAB045">Farlin Manual Breast Pump</a></h3>

        <p class="tPrice">
        <span>Rs 1,195</span>
        Rs 1,076
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/farlin-manual-breast-pump/BAB045"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB399.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/philips-avent-manual-breast-pump/BAB399">Philips AVENT Manual Breast Pump</a></h3>
        <p class="tPrice">
        <span>Rs 2,295</span>
        Rs 2,099
        </p>
        <p class="tSave">You Save 8%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/philips-avent-manual-breast-pump/BAB399"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>


        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB1084.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/tolly-joy-nipple/BAB1084">Tolly Joy Nipple </a></h3>

        <p class="tPrice">
        <span>Rs 145</span>
        Rs 123
        </p>
        <p class="tSave">You Save 15%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/tolly-joy-nipple/BAB1084"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB216.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/farlin-free-direction-manual-breast-pump/BAB216">Farlin Free Direction Manual Breast Pump
</a></h3>


        <p class="tPrice">

        <span>Rs 1,695</span> Rs 1,495
        </p>
        <p class="tSave">You Save 12%</p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/farlin-free-direction-manual-breast-pump/BAB216"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB154.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/pigeon-breast-pump-after-breast-feeding/BAB154">Pigeon Breast Pump (After Breast Feeding)
</a></h3>


        <p class="tPrice">
        <span>Rs 365</span>
        Rs 328
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/pigeon-breast-pump-after-breast-feeding/BAB154"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB448.jpg" width="170" height="170"  class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/medela-breastmilk-bottles-with-imprint/BAB448">Medela Breastmilk Bottles With Imprint
</a></h3>


        <p class="tPrice">

        Rs 975
        </p>
        <p class="tSave"></p>

        <p class="tBuy"><a href="http://www.healthkart.com/product/medela-breastmilk-bottles-with-imprint/BAB448"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB305.jpg" width="170" height="170" class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/chicco-natural-feeling-adjustable-breast-pump-0-bpa/BAB305">Chicco Natural Feeling Adjustable Breast Pump 0% BPA
</a></h3>


        <p class="tPrice">
        <span>Rs 1,999</span>
        Rs 1,799
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/chicco-natural-feeling-adjustable-breast-pump-0-bpa/BAB305"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
         <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB056.jpg" width="170" height="170" class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/pigeon-battery-operated-breast-pump-and-feeding-set/BAB056">Pigeon Battery Operated Breast Pump and Feeding Set</a></h3>


        <p class="tPrice">
        <span>Rs 2,555</span>
        Rs 2,300
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/pigeon-battery-operated-breast-pump-and-feeding-set/BAB056"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>

        <div class="productThumb">
        <img src="${pageContext.request.contextPath}/images/breastfeeding/BAB048.jpg" width="170" height="170" class="tThumb" />
        <h3><a href="http://www.healthkart.com/product/farlin-manual-breast-pump/BAB048">Farlin Manual Breast Pump
</a></h3>


        <p class="tPrice">
        <span>Rs 325</span>
        Rs 292
        </p>
        <p class="tSave">You Save 10%</p>
        <p class="tBuy"><a href="http://www.healthkart.com/product/farlin-manual-breast-pump/BAB048"><img src="${pageContext.request.contextPath}/images/breastfeeding/buynow.gif" width="89" height="26" alt="Buy Now" /></a></p>
        </div>
    </div>
    <div class="cl"></div>
    <!-- Right Column Ends-->
    </div>

</s:layout-component>


</s:layout-render>

