<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
  <style type="text/css">

  
.navigation {
	margin:20px 0;
	height:25px;
	width:960px;
	background-color:#333;}

#nav, #nav ul{
    font-size:14px;
margin:0;
padding:0;
list-style-type:none;
list-style-position:outside;
position:relative;
line-height:1.5em;

}

#nav a{
display:block;
padding:0px 5px;
border:1px solid #333;
color:#fff;
text-decoration:none;
background-color:#333;
}

#nav a:hover{
background-color:#fff;
color:#333;
}

#nav li{
margin-left:20px;
float:left;
position:relative;
}

#nav ul {
position:absolute;
display:none;
width:12em;
top:1.5em;
}



#nav li ul a{
margin-left:-20px;
width:12em;
height:auto;
float:left;
}

#nav ul ul{
top:auto;
}

#nav li ul ul {
left:12em;
margin:0px 0 0 10px;
}

#nav li:hover ul ul, #nav li:hover ul ul ul, #nav li:hover ul ul ul ul{
display:none;
}
#nav li:hover ul, #nav li li:hover ul, #nav li li li:hover ul, #nav li li li li:hover ul{
display:block;
}




            a, a:hover{border-bottom:none;}
      .cl {
clear:both;}

p, h3, h1, h2, h4 {margin:0; padding:0;}

 @font-face
            {
                font-family: 'robotoregular'
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-regular-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-regular-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.svg#robotoregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }

            @font-face
            {
                font-family: 'roboto_ltregular'
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-light-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-light-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.svg#roboto_ltregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }


 #pageContainer {
                margin: 15px auto;
                width: 960px;
                overflow: auto;
                padding-bottom: 260px;
                font-family: 'robotoregular';
                color:#231f20;
            }


      .product {
	width:215px;
          border-bottom:none;
    text-align:left;
	float:left;
	margin:20px 15px 0 10px;
	position:relative;
          height:520px;
	}

.product  .gosf-logo { position:absolute; top:177px; right:0px;	}

.product .img{
	border:1px solid #CCC;
	width:213px;
	height:256px;
	}




.product h3, .product h2, .product .bid{
	font-size:13px;
	font-weight:normal;
	color:#333;
    padding:0;
	}
  /*.product h3 { height:23px; }*/

.product .description{
	font-size:12px;
    line-height:normal;
	font-weight:normal;
	color:#999;
	margin-bottom:20px;
    padding:0;
	}

.product h3 a{
font-size:13px;
	color:#333;
	text-decoration:none;
	}

	.product h3 a:hover{
        font-size:13px;
	color:#333;

	}

.product .price {text-align:left; margin:5px 0; font-size:18px; color:#333; line-height:normal; font-family: 'roboto_ltregular';}

.product .price span{font-size:15px; color:#999; text-decoration:line-through; font-style:normal;}

.product a.buynow {color:#000; display:inline-block; font-size:18px; padding:5px 15px; border:1px #000 solid; text-decoration:none; margin-bottom:10px;}
.product .buynow a:hover{color:#000; text-decoration:none; }
.product:hover {border-bottom:none;}

    

.heading1 {
	border-bottom:1px solid #c0c0c0;
	width:960px;
	height:40px;
	line-height:40px;
	margin:40px 0 10px 0;
	text-align:center;
	font-weight:bold;
	color:#666; font-size:20px;}
.product .img180 img { max-height:180px; max-width:180px; }

.see-more {font-size:13px;
	font-weight:normal;
	color:#999;
	margin-top:20px;
    text-decoration:none;
  }

.see-more a{
	color:#999;
    text-decoration:none;
  }

  .see-more a:hover{
	color:#000;
  }


</style>


<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.2.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/menu-gosf.js"></script>


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

    <h1 class="title">
       Cyber Monday Great Online Shopping Festival
    </h1>
  </div>
  
</s:layout-component>

<s:layout-component name="metaDescription">Google Cyber Monday</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>



<s:layout-component name="content">





  <!---- paste all content from here--->

 <div id="pageContainer">
<img src="${pageContext.request.contextPath}/images/GOSF/banner.jpg" />
<div class="cl"></div>

<jsp:include page="../includes/_menuGosf.jsp"/>

<div class="cl"></div>





    
    <div class="heading1">PRIME DEALS</div>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PW006' productDesc="Test your pregnancy in just 2 minutes with this Instant Ovulation Kit"/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP002' productDesc="Protect yourself with this powerful pepper formula spray."/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL002' productDesc="Figure out the best two days you will be most fertile. Grab this I-Sure Ovulation Strip to get pregnant for sure. "/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP001' productDesc="Protect yourself with this powerful pepper formula spray."/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE005' productDesc='These lenses made with a slight tint correct the vision. The presence of Hilaficon B and non-ionic B Lens material comfort the eyes and facilitates smooth lens-lid interaction.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KNOCK001' productDesc="Protect yourself with this powerful pepper formula spray."/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB005' productDesc='An automatic BP monitor with one-touch operation. It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NIC001' productDesc="Nicorette reins in your constant urge to smoke. Quit smoking for sure."/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH101' productDesc="Powerful electric toothbrush to clean your teeth faster and better."/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE004' productDesc='These lenses help in vision correction. Also capable of blocking UVA and UVB rays, they also protect the eyes.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM024' productDesc='Accurate results in just 5 minutes with auto coding technology. A glucometer with life time warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM007' productDesc='A simple three-step, self-testing glucometer with storage capacity of up to 500 test results'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT986' productDesc='A supplement that provides Creating for enhancing stamina and building muscles. Ideal for intense workouts.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT469' productDesc=' Rich in L-Carnitine, it burns your extra fat, reduces hunger pangs, improves metablosim, and reduces cholestrol.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT130' productDesc='Provides 11g of Leucine and 13g of additional BCAAs per serving. Ideal for rapid muscle gain and recovery.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1600' productDesc='Suitable for people who want to lose weight, it helps burn fat, maintain stamina and improve metabolism.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1369' productDesc='Build strength and endurance with MuscleBlaze Creatine. It consistently delivers a powerful workout performance.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RD001' productDesc='A waist shaper that supports the lumbar spine and corrects posture. Ideal for women who want to look slim and trim.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT412' productDesc='A supplement that improves the nutrient absorption power in adults and boosts energy levels. Ideal for senior citizens.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1368' productDesc='Designed for people who want to gain mass. Packed with 52gm of high quality protein blend and 9gm of added fiber.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc="Put munchies at bay with healthy fats and great taste in 5 amazing flavors. Get 30 gms of protein in every bite!"/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE035' productDesc='Disposable, colored lenses to offer extra depth to the eyes and enhance beauty. They are soft on the eyes and can be worn for an entire month.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOS001' productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1367' productDesc='Tailored for fitness enthusiasts engaged in heavy workouts, it has 25gm of 100% Whey Protein blend and EAAs per serving.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT905' productDesc='For bodybuilders who want to gain mass. Infused with 1000 calories, 70g proteins and 10g Branched-Chain Amino Acids.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007' productDesc='Built using a special material Aergel, these lenses help in curbing spherical aberration'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB014' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1165' productDesc='Ideal for making health drinks on the move, HK Shaker & Blender Bottle makes lump-free drinks anytime, anywhere.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB006' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT893' productDesc='Ideal for fitness enthusiasts working for lean muscle gain, it is a rapid absorption formula that builds strength and stamina.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE018' productDesc='Ligh blue colored lenses made from Lacreo technology which locks water ingredient in the lens for extra comfort. Blocks upto 97% UVB and 83% UVA.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1367' productDesc='Tailored for fitness enthusiasts engaged in heavy workouts, it has 25gm of 100% Whey Protein blend and EAAs per serving.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AYUCR4' productDesc="Unleash the vitality within and experience out of the world intimacy with Ayu Care Vaji Tailam"/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Keep muchies at bay with Oh Yeah Bars! Get the goodness of healthy fats and low sugar in 1 go; in 5 amazing flavors. '/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT335' productDesc='A product with a potent combination of essential vitamins and minerals. Improves the strength, immunity and energy levels of the body.'/>


                   <div class="cl"></div>

     


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

