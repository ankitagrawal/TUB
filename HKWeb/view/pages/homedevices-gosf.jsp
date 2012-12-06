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
<img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg" />
<div class="cl"></div>

     <jsp:include page="../includes/_menuGosf.jsp"/>

<div class="cl"></div>





    
    <div class="heading1">HOME DEVICES</div>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB005' productDesc='An automatic BP monitor with one-touch operation. It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RD001' productDesc='A waist shaper that supports the lumbar spine and corrects posture. Ideal for women who want to look slim and trim.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HR005' productDesc='A nebulizer that stimulates easy breathing. Ideal for asthmatic patients.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RW007' productDesc='A lycra/far infrared wrist wrap that supports injuries.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WA002' productDesc='A sturdy, well-built walking stick. Perfect companion for the elderly.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB012' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX007' productDesc='A digital weighing scale that holds reading upto 5 seconds before it goes off.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TYNOR016' productDesc='A knee support made from neoprene that helps recover knee injuries.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB014' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='CERSUP004' productDesc='A pillow that counters stress and strain for patients suffering from cervical spondylosis.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SUN006' productDesc='A facial sauna that helps achieve flawless and unblemished skin.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ROSMAX010' productDesc='A light-weight stethoscope with a robust nylon cuff. '/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB012' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX014' productDesc='An analog weighing scale with an anti-slip rubber top. It can weigh up to 120 kg.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WRSTS002' productDesc='An elastic wrist wrap that supports injuries. it comes in different sizes.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HICKS013' productDesc='A surgical dressing pad that helps clean blood and other impurities from wounds.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HP070' productDesc='A light-weight stethoscope with a robust nylon cuff. '/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HICKS013' productDesc='A surgical dressing pad that helps clean blood and other impurities from wounds.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN014' productDesc='A nebulizer that stimulates easy breathing for asthmatic patients.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB006' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='METRO001' productDesc='A facial sauna that unclog pores through deep perspiration.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HT006' productDesc='A digital thermometer that gives accurate results in just 10 seconds.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FRSTA004' productDesc='A must have first aid kit that contains all that is necessary.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN008' productDesc='A digital weighing scale that comes with 4 sensor, accurate weight measurement technology'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SCARE001' productDesc='A sphygmomanometer for accurate measurement of blood pressure levels.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX015' productDesc='A digital weighing scale that comes with an auto on & off function. It has a capacity to hold up to 150 kg.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX013' productDesc='An analog weighing scale with an anti-slip rubber top. It can hold up to 130 kg.'/>
     
     


                   <div class="cl"></div>

     


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

