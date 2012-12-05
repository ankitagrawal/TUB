<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
  <style type="text/css">




/*  tab css   */

#tabs-min {
	background: transparent;
	font-family: 'whitney_htf_mediumregular';
	border: none;
	margin-top:20px;
}

#tabs-min ul a, #tabs-min ul a:hover{
	background: transparent;
	font-size:16px;
	font-family: 'whitney_htf_mediumregular';
}

#tabs-min h3 a, #tabs-min h3 a:hover{
	font-size:13px;
	font-family: 'whitney_htf_mediumregular';
}

#tabs-min .ui-widget-header {
	background: transparent;
	border: none;
	border-bottom: 1px solid #c0c0c0;
	-moz-border-radius: 0px;
	-webkit-border-radius: 0px;
	border-radius: 0px;
}
#tabs-min .ui-state-default {
	background: transparent;
	border: none;
}
#tabs-min .ui-state-active {
	background: transparent url(${pageContext.request.contextPath}/images/GOSF/uiTabsArrow.png) no-repeat bottom center;
	border: none;
}
#tabs-min .ui-state-default a {
	color:#666666;
}
#tabs-min .ui-state-active a {
	color: #459E00;
}

.ui-tabs { zoom: 1; }
.ui-tabs .ui-tabs-nav { list-style: none; position: relative; padding: .2em .2em 0; }
.ui-tabs .ui-tabs-nav li { position: relative; float: left; border-bottom-width: 0 !important; margin: 0 .2em -1px 0; padding: 0; }
.ui-tabs .ui-tabs-nav li a { float: left; text-decoration: none; padding: .5em 1em; }
.ui-tabs .ui-tabs-nav li.ui-tabs-selected { padding-bottom: 1px; border-bottom-width: 0; }
.ui-tabs .ui-tabs-nav li.ui-tabs-selected a, .ui-tabs .ui-tabs-nav li.ui-state-disabled a, .ui-tabs .ui-tabs-nav li.ui-state-processing a { cursor: text; }
.ui-tabs .ui-tabs-nav li a, .ui-tabs.ui-tabs-collapsible .ui-tabs-nav li.ui-tabs-selected a { cursor: pointer; } /* first selector in group seems obsolete, but required to overcome bug in Opera applying cursor: text overall if defined elsewhere... */
.ui-tabs .ui-tabs-panel { display: block; border-width: 0; background: none; }
.ui-tabs .ui-tabs-hide { display: none !important; }


      /*
* jQuery UI CSS Framework
* Copyright (c) 2009 AUTHORS.txt (http://jqueryui.com/about)
* Dual licensed under the MIT (MIT-LICENSE.txt) and GPL (GPL-LICENSE.txt) licenses.
*/

/* Layout helpers
----------------------------------*/
.ui-helper-hidden { display: none; }
.ui-helper-hidden-accessible { position: absolute; left: -99999999px; }
.ui-helper-reset { margin: 0; padding: 0; border: 0; outline: 0; line-height: 1.3; text-decoration: none; font-size: 100%; list-style: none; }
.ui-helper-clearfix:after { content: "."; display: block; height: 0; clear: both; visibility: hidden; }
.ui-helper-clearfix { display: inline-block; }
/* required comment for clearfix to work in Opera \*/
* html .ui-helper-clearfix { height:1%; }
.ui-helper-clearfix { display:block; }
/* end clearfix */
.ui-helper-zfix { width: 100%; height: 100%; top: 0; left: 0; position: absolute; opacity: 0; filter:Alpha(Opacity=0); }


/* Interaction Cues
----------------------------------*/
.ui-state-disabled { cursor: default !important; }


/* Icons
----------------------------------*/

/* states and images */
.ui-icon { display: block; text-indent: -99999px; overflow: hidden; background-repeat: no-repeat; }


/* Misc visuals
----------------------------------*/

/* Overlays */
.ui-widget-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }

/*  tab css close   */

            a, a:hover{border-bottom:none;}
      .cl {
clear:both;}

p, h3, h1, h2, h4 {margin:0; padding:0;}

 @font-face
            {
                font-family: 'whitney_htf_bookregular'
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.svg#whitney_htf_bookregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }

            @font-face
            {
                font-family: 'whitney_htf_scbold'
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.svg#whitney_htf_scbold') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }
            @font-face
            {
                font-family: 'whitney_htf_mediumregular'
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.svg#whitney_htf_mediumregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }

                 @font-face
            {
                font-family: 'whitney-book_scregular'
            ;
                src: url('${pageContext.request.contextPath}/css/whitney-booksc-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/whitney-booksc-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitney-booksc-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitney-booksc-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitney-booksc-webfont.svg#whitney-book_scregular') format('svg')
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
                font-family: 'whitney_htf_bookregular';
                color:#231f20;
            }
      .product {
	width:215px;
    text-align:left;
	float:left;
	margin:20px 15px 0 10px;
	position:relative;
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
	margin:5px 0;
    padding:0;
	}
  .product h3 { height:23px; }

.product h3 a{
font-size:13px;
	color:#333;
	text-decoration:none;
	}

	.product h3 a:hover{
        font-size:13px;
	color:#333;

	}

.product .price {text-align:left; margin:5px 0; font-size:18px; color:#333; line-height:normal; font-family: 'whitney-book_scregular';}

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



<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
<script type="text/javascript">
$(function() {
	$('div.tabs').tabs();
});
</script>

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

<div id="tabs-min" class="tabs">
	<ul>
		<li style="margin-left:230px;"><a href="#tabs-min-1" name="tabs-min">Yesterdays Deals Remaining</a></li>
		<li><a href="#tabs-min-2">Todays Deals</a></li>
		<li><a href="#tabs-min-3">Upcoming Deals</a></li>
	</ul>
	<div id="tabs-min-1">

<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>

        <div class="cl"></div>
        <div class="see-more"><a href="#"> See more ››</a></div>


</div>


	<div id="tabs-min-2">

<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>

        <div class="cl"></div>
        <div class="see-more"><a href="#"> See more ››</a></div>


</div>




	<div id="tabs-min-3">

<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>

        <div class="cl"></div>
        <div class="see-more"><a href="#"> See more ››</a></div>


</div>

</div>
<div class="cl"></div>
<!-- tab close-->

<div class="heading1">Category Based Prime Deal</div>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<div class="cl"></div>

              <div class="cl"></div>
        <div class="see-more"><a href="#"> See more ››</a></div>



<div class="heading1">Logical Bundle</div>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<div class="cl"></div>

                  <div class="cl"></div>
        <div class="see-more"><a href="#"> See more ››</a></div>




<div class="heading1">Miscellaneous</div>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007'/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311'/>

                   <div class="cl"></div>
        <div class="see-more"><a href="#"> See more ››</a></div>
     


</div>

</s:layout-component>


</s:layout-render>

