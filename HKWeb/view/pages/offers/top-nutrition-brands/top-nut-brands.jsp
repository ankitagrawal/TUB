<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Top Nutrition Brands">

<s:layout-component name="htmlHead">
  <style type="text/css">
    img {border:none;}

    body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,blockquote,th,td,p {
      margin:0;
      padding:0;
    }


    ol,ul {
      list-style:none;
    }


    .cl {clear:both;}


    #container-tnb {
      padding:20px 0;
      margin:0 auto;
      width:960px;
    }


    #container-tnb img{
      border: none;
      box-shadow: 1px 1px 3px rgba(0,0,0,0.5);
      -webkit-box-sizing: border-box;
      -moz-box-sizing: border-box;
      -o-box-sizing: border-box;
      -ms-box-sizing: border-box;
      box-sizing: border-box;
      -webkit-transition: all 0.2s ease-in-out;
      -moz-transition: all 0.2s ease-in-out;
      -o-transition: all 0.2s ease-in-out;
      -ms-transition: all 0.2s ease-in-out;
      -webkit-transition: all 0.2s ease-in-out;
    }

    #container-tnb img:hover,
    #container-tnb img.glow {
      border: 0;
      box-shadow: 6px 6px 6px rgba(0,0,0,0.3);
    }

   a{
      border: none;

    }

   a:hover{
      border: none;

    }

    #container-tnb ul {
      margin-top:20px;}



    #container-tnb ul li{
      margin-right:20px;
      display:inline;
      float:left;}


    #container-tnb ul li:last-child{
      margin-right:0px;;
      display:inline;
      float:left;}



    .margin-t20 {
      margin-top:20px;}

        </style>


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Top Nutrition Brands</span>

    <h1 class="title">
     Best Offers On Top Nutrition Brands
    </h1>
  </div>

</s:layout-component>

<s:layout-component name="metaDescription">Special Diwali Offers from HealthKart.com</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>

<s:layout-component name="content">
  <div class="cl"></div>
  <div id="container-tnb"> <a href="http://www.healthkart.com/brand/health-nutrition/Vitamin+Shoppe"><img src="images/Vitamin-Shoppe.jpg" /></a>

    <ul>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/HealthViva"><img src="images/healthviva.jpg" /></a></li>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/Twinlab"><img src="images/twinlab.jpg" /></a></li>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/Organic+India"><img src="images/Organic-India.jpg" /></a></li>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/Health+Aid"><img src="images/Health-Aid.jpg" /></a></li>
    </ul>
    <div class="cl"></div>
    <ul>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/Abbott"><img src="images/Abbott.jpg" /></a></li>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/Nature%27s+Herbs"><img src="images/Natures-Herbs.jpg"/></a></li>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/Natrol"><img src="images/Natrol.jpg"/></a></li>
      <li><a href="http://www.healthkart.com/brand/health-nutrition/JAY"><img src="images/Jay.jpg" /></a></li>
    </ul>
    <a href="http://www.healthkart.com/brand/health-nutrition/Nature%27s+Bounty"><img class="margin-t20" src="images/Nature's-Bounty.jpg" /></a>

  </div>
    <!-- Container Ends-->


  
</s:layout-component>


</s:layout-render>

