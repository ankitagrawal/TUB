<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Special Offers on OneTouch and Splenda Products on World Diabetes Day">

<s:layout-component name="htmlHead">
  <style type="text/css">
		.cl { clear:both; }

@font-face {
				font-family: 'Esta';
				src: url('${pageContext.request.contextPath}/images/wdd/estaregular.eot');
				src: url('${pageContext.request.contextPath}/images/wdd/estaregular.eot?#iefix') format('embedded-opentype'),
						 url('${pageContext.request.contextPath}/images/wdd/estaregular.woff') format('woff'),
						 url('${pageContext.request.contextPath}/images/wdd/estaregular.ttf') format('truetype'),
						 url('${pageContext.request.contextPath}/images/wdd/estaregular.svg#estaregular') format('svg');
				font-weight: normal;
				font-style: normal;
		}
@font-face {
				font-family: '${pageContext.request.contextPath}/images/wdd/DidotHTF-11LightItalic';
				src: url('${pageContext.request.contextPath}/images/wdd/didothtf-11lightitalic.eot');
				src: url('${pageContext.request.contextPath}/images/wdd/didothtf-11lightitalic.eot?#iefix') format('embedded-opentype'),
						 url('${pageContext.request.contextPath}/images/wdd/didothtf-11lightitalic.woff') format('woff'),
						 url('${pageContext.request.contextPath}/images/wdd/didothtf-11lightitalic.ttf') format('truetype'),
						 url('${pageContext.request.contextPath}/images/wdd/didothtf-11lightitalic.svg#didothtf-11lightitalic') format('svg');
				font-weight: normal;
				font-style: normal;
		}
        p, form	{ margin:0px; padding:0;}
  .down	{ width:100%; clear:both;}
.clr	{ clear:both; height:1px; line-height:1px; font-size:1px;}
        a img, a, a:hover { border-bottom:none; }

.outer	{ width:960px; clear:both; text-align:left; margin:0 auto; padding-bottom:240px;}

.bgDiabetesHeader	{ background:url('${pageContext.request.contextPath}/images/wdd/bg-header.jpg') top left repeat-y; padding:10px 0px 12px 0px;}

.h1		{ font-size:70px; line-height:85px; color:#231f20; font-family:'Esta'; font-weight:normal; text-align:center; margin:0px; padding:0px;}
h3		{ font-size:30px; line-height:35px; color:#231f20; font-family:'Esta'; font-weight:normal; text-align:center; margin:0px; padding:0px;}

.dL		{ float:left; width:auto; padding:40px 0px 0px 200px; font-size:30px; line-height:35px; color:#231f20; font-family:'Esta'; font-weight:normal; text-align:center; }
.dM		{ float:left; width:auto; font-size:170px; padding:0px 7px 0px 5px; padding-top:35px; color:#007dc5; font-weight:bold;}
.dR		{ float:left; width:auto;  padding:40px 10px 0px 0px; float:left;}

.line1	{ width:950px; clear:both; margin:0 auto; height:1px; line-height:1px; background:#949698;}


.ht1	{ height:50px; clear:both;}
.ht2	{ height:20px; clear:both;}

.des	{ font-family:'Esta'; font-size:19px; color:#231f20; line-height:24px;}
.pad1	{ padding:20px 130px 20px 130px;}
.pad2	{ padding:20px 200px 20px 200px;}

.proPic	{ width:240px; float:left; font-family: 'Esta'; color:#231f20; font-size:17px; line-height:20px;}
.prpCopy{ width:720px; float:left; font-family: 'Esta'; color:#231f20; font-size:17px; line-height:20px;}
.proCopyLeft	{ width:520px; float:left;}
.proCopyRight	{ width:auto; float:left; text-align:right;}

.redTxt		{ color:#951c00;}
.redTxt span{ color:#951c00; text-decoration:line-through}
.blueTxt	{ color:#0f1c55; font-weight:bold; line-height:30px;}

.splOrdrBtn1{ position:absolute; width:200px; height:36px; margin:-5px 0px 0px 500px;}
.buyNowBtn1	{ position:absolute; width:120px; height:36px; margin:33px 0px 0px 547px;}


.onetouch		{ width:480px; float:left; padding:0px 0px 0px 0px;}
.onetouch img	{ margin-bottom:10px;}
.splenda		{ width:420px; float:left;}
.splenda img	{ margin-bottom:10px;}

.otSPTxt	{ font-family:'Esta'; font-size:19px; color:#231f20; line-height:24px;}

ul.bullet1			{ margin:0px; padding:0px; list-style:none; font-size:17px;}
ul.bullet1 li		{ line-height:15px; padding:0px 0px 10px 20px; background:url('${pageContext.request.contextPath}/images/wdd/bullet1.jpg') 0px 7px no-repeat;}
ul.bullet1 li span	{font-size:13px;}


        </style>


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">World Diabetes Day</span>

    <h1 class="title">
     Special Offers on OneTouch and Splenda Products
    </h1>
  </div>
  
</s:layout-component>

<s:layout-component name="metaDescription">Special Offers on OneTouch and Splenda Products from HealthKart.</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>

<s:layout-component name="content">
   <div class="outer">
	<div class="down">&nbsp;</div>
    <div class="down"><a href="http://www.healthkart.com/diabetes" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/header.jpg" alt="World Diabetes Day, Nov.14th, Join the Blue Circle, HealthKart Celebrates World Diabetes Day with OneTouch and Splenda" border="0" /></a></div>
    <div class="pad1 des" align="center">Bring back the balance in your life by cutting down glucose levels. Find yourself amid a breath of relief with exclusive diabetes combos which aid easy monitoring of blood sugar levels & controlling it effectively.
    </div>
    <div class="clr">&nbsp;</div>
    <div class="line1">&nbsp;</div>
    <div class="down">&nbsp;</div>
    <div class="down">
   	  <div class="proPic">
      <p><a href="http://www.healthkart.com/product/beurer-talking-upper-arm-blood-pressure-monitor-bm-19/CMB-DIA29" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/ultra2.jpg" border="0" /></a></p>
      <br />
      <p class="redTxt">MRP = <span>Rs. 3,400</span></p>
      <p class="redTxt">HealthKart Price = Rs. 2,545</p>
      <p class="redTxt"><strong>You Save = Rs. 855 (25%)</strong></p>
      </div>
      <div class="prpCopy">
      			<div class="down">Invest more in merry-making while you break free from the hassled clinic visits with OneTouch Ultra 2 World Diabetes Day Combo. Bring home the super convenient self-monitoring feature with OneTouch Ultra 2 meter with 25 free lancets and 10 free strips. Also enjoy 25 additional strips in the combo pack. With up to 500 test storage capacity, its unique function lets you assess the effect of your food on your glucose levels. Wear-off the unsweetened spell this festive season with Splenda No Calorie Sweetener combined in this combo.<br /><br />
                 </div>

      			 <div class="down">
                 	<div class="splOrdrBtn1"><a href="http://www.healthkart.com/product/beurer-talking-upper-arm-blood-pressure-monitor-bm-19/CMB-DIA29" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/special-offer.jpg" border="0"  alt="special Offer" /></a></div>
                   	<div class="buyNowBtn1"><a href="http://www.healthkart.com/product/beurer-talking-upper-arm-blood-pressure-monitor-bm-19/CMB-DIA29" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/buy-now.jpg" border="0"  alt="Buy Now" /></a></div>
                 <p><span class="blueTxt">Contains:</span></p>
                 	<ul class="bullet1">
                    	<li>Johnson & Johnson OneTouch Ultra 2 Meter</li>
                    	<li>Johnson & Johnson OneTouch UltraSoft 25 Lancets</li>
                    	<li>Johnson & Johnson OneTouch Ultra 10 Test Strips <span>(Included with Ultra 2 Meter)</span></li>
                        <li>Johnson & Johnson OneTouch Ultra 25 Test Strips</li>
                    	<li>Splenda No Calorie Sweetener 300 + 300<span> (Short Expiry Product - Exp Date: March 2013)</span></li>
                    </ul>
                 </div>



        </div>
    </div>

    <div class="down">&nbsp;</div>
    <div class="line1">&nbsp;</div>
    <div class="down">&nbsp;</div>

    <div class="down">
    	<div class="proPic">
        <p><a href="http://www.healthkart.com/product/beurer-talking-upper-arm-blood-pressure-monitor-bm-19/CMB-DIA30" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/selectsimple.jpg" border="0" /></a></p>
        <p class="redTxt">MRP = <span>Rs. 2,175</span></p>
        <p class="redTxt">HealthKart Price = Rs. 1,999</p>
        <p class="redTxt"><strong>You Save = Rs. 176 (8%)</strong></p>
        </div>
      <div class="prpCopy">
      				<div class="down">
                    <p>Make a brilliant choice this WORLD DIABETES DAY! Add pride to your life with Johnson & Johnson OneTouch SelectSimple Meter with 25 free lancets  and 10 free strips. Also enjoy 50 additional strips in the combo pack. Enjoy the advantages of a glucometer from the ease of your home. This exciting offer is combined with Splenda No Calorie Sweetener to let you savor the delicacies this festive season.</p>
      				<br />
                    </div>
                    <div class="down">
                    <div class="splOrdrBtn1"><a href="http://www.healthkart.com/product/beurer-talking-upper-arm-blood-pressure-monitor-bm-19/CMB-DIA30" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/special-offer.jpg" border="0"  alt="special Offer" /></a></div>
                   	<div class="buyNowBtn1"><a href="http://www.healthkart.com/product/beurer-talking-upper-arm-blood-pressure-monitor-bm-19/CMB-DIA30" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/buy-now.jpg" border="0"  alt="Buy Now" /></a></div>
                   	<ul class="bullet1">
                            <p><span class="blueTxt">Contains:</span></p>
                            <li>Johnson & Johnson OneTouch SelectSimple Meter</li>
                            <li>Johnson & Johnson OneTouch UltraSoft 25 Lancets</li>
                             <li>Johnson & Johnson OneTouch Select Simple 10 Test Strips<span> (Included with SelectSimple Meter)</span></li>
                            <li>Johnson & Johnson OneTouch Select Simple Test 50 Strips</li>
                        	<li>Splenda No Calorie Sweetener 300 + 300 <span>(Short Expiry Product - Exp Date: March 2013)</span></li>
                        </ul>
                   </div>

        </div>
    </div>
    <div class="down">&nbsp;</div>
    <div class="line1">&nbsp;</div>
    <div class="down">&nbsp;</div>
    <div class="down">
    	<div class="onetouch" align="center">
        		<p><a href="http://www.healthkart.com/brand/diabetes/OneTouch" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/onetouch2.jpg" alt="OneTouch" border="0" /></a></p>
                <p><a href="http://www.healthkart.com/brand/diabetes/OneTouch" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/buy-now.jpg" alt="Buy Now" border="0" /></a></p>
                <span class="otSPTxt">OneTouch offers an expansive range of blood glucose monitors,
                strips and lancets, serving you to monitor diabetes effectively.</span>
    	</div>

      <div class="splenda" align="center">
			<p><a href="http://www.healthkart.com/brand/diabetes/Splenda" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/splenda2.jpg" alt="splenda" border="0" /></a></p>
            <p><a href="http://www.healthkart.com/brand/diabetes/Splenda" target="_blank"><img src="${pageContext.request.contextPath}/images/wdd/buy-now.jpg" alt="Buy Now" border="0" /></a></p>
           	  <span class="otSPTxt">Splenda, world's trusted range of sweeteners <br />
   	    and diabetic  products which makes every sweet moment yours.</span>
           </div>
    </div>
    <div class="down">&nbsp;</div>
  	<div class="line1">&nbsp;</div>
    <div class="clr">&nbsp;</div>
    <div class="pad1 des" align="center">Entertain no compromises with your taste buds this World Diabetes Day.</div>
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
<!-- AddThis Button END -->
</s:layout-component>


</s:layout-render>

