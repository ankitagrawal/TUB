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





    
    <div class="heading1">DIABETES</div>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT033' productDesc=' A snack bar that stabilizes sugar levels for up to 9 hours.  Ideal for growing children, diabetic patients, and fitness enthusiasts.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT033' productDesc='A snack bar that stabilizes sugar levels for up to 9 hours.  Ideal for growing children, diabetic patients, and fitness enthusiasts.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT033' productDesc='A snack bar that stabilizes sugar levels for up to 9 hours.  Ideal for growing children, diabetic patients, and fitness enthusiasts.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM024' productDesc='Accurate results in just 5 minutes with auto coding technology. A glucometer with life time warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT033' productDesc='A snack bar that stabilizes sugar levels for up to 9 hours.  Ideal for growing children, diabetic patients, and fitness enthusiasts.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT035' productDesc='Indulgent drizzles with just 2g. of sugar. Ideal for growing children, diabetic patients, and fitness enthusiasts'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT035' productDesc='Indulgent drizzles with just 2g. of sugar. Ideal for growing children, diabetic patients, and fitness enthusiasts'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM040' productDesc='Accurate results in 5 minutes with auto coding technology, A glucometer with life time warranty and 25 testing-strips'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DL008' productDesc='Round Blood lancets with sterile tip, compatible with all major lancing devices.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT036' productDesc='Velvety Shakes for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM007' productDesc='A simple three-step, self-testing glucometer with storage capacity of up to 500 test results'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT034' productDesc='Savory crisps. great for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT034' productDesc='Savory crisps for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT034' productDesc='Savory crisps for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT036' productDesc='Velvety Shakes for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS003' productDesc='Self-testing sensor comfort strips to determine blood glucose levels with greater accuracy'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT033' productDesc='A snack bar that stabilizes sugar levels for up to 9 hours.  Ideal for growing children, diabetic patients, and fitness enthusiasts.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT034' productDesc='Savory crisps for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT034' productDesc='Savory crisps for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM018' productDesc='Equipped with biosensor technology that gives fast and reliable results in just 9 seconds.'/>


      <div class="cl"></div>

     


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

