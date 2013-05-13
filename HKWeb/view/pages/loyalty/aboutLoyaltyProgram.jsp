<%@ page import="com.akube.framework.util.BaseUtils"%>
<%@ page import="com.hk.constants.core.HealthkartConstants"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page import="com.hk.taglibs.Functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
<stripes:layout-component name="contents">
	<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca" />  
      <script type="text/javascript">
        $(document).ready(function(){
          
          /*Fixing the left nav*/
          $(document).scroll(function(e) {
            if($(this).scrollTop() > $(".priceFilterContainerOne").height() + 100) {
              $(".priceFilterContainerOne").css({"position" : "fixed", "top" : "5px"});
            }
            else {
              $(".priceFilterContainerOne").css("position", "relative");
            }
          });
          /*Left nav fixing ends here*/

          /*code for moving the navigation to certain div*/
           function moveTo(elementId){
            console.log(elementId);
            var $element = $("#" + elementId);
            $('html, body').animate({scrollTop: $element.offset().top}, 1500);
          };

          $("#aboutStellar").click(function(){
            moveTo("about");
          });

          $("#hwItWorks").click(function(){
            moveTo("howItWorks");
          });

          $("#benefitsLevel").click(function(){
            moveTo("benefits");
          });

          $("#faqs").click(function(){
            moveTo("faq");
          });

          $("#top").click(function(){
            moveTo("header");
          });

          $("#bottom").click(function(){
            moveTo("footer");
          });
         
        });
      </script> 

    <div class="mainContainer">
      <div class="container_16 clearfix">
        <div class="grid_4 leftBlock">
          <div class="embedMarginTop108"></div>
          <div class="priceFilterContainerOne">
            <div id="top" class="sorting makeCursor">TOP</div>
              <div class="brandsContainer1">
                <div id="aboutStellar" class="priceRange">
                  <span >About stellar</span>
                </div>
                <div id="hwItWorks" class="priceRange">
                  <span >How it works</span>
                </div>
                <div id="benefitsLevel" class="priceRange">
                  <span >Benefits levels</span>
                </div>
                <div id="faqs" class="priceRange">
                  <span >FAQs</span>
                </div>
              </div>
 			<div id="bottom" class="bottomText makeCursor">BOTTOM</div>
          </div>
        </div>
        <div class="grid_12 mainContent">
          <div id="productCategory" class="grid_12 embedMarginBottom40">
            <div class="dottedLine"></div>
            <div class="productCategoryText">
              <img alt="1" class="stellarLogo" src="<hk:vhostJs/>/pages/loyalty/resources/images/stellarLogo.png"/>
            </div>
            <div class="dottedLine"></div>
          </div>
		<c:set var="bronze" value="${lca.badgeList[1]}" />
		<c:set var="silver" value="${lca.badgeList[2]}" />
		<c:set var="gold" value="${lca.badgeList[3]}" />
		<c:set var="platinum" value="${lca.badgeList[4]}" />
	
          <div class="aboutContent">
            <p id="about">At HealthKart, we would like to give you more of what you enjoy and redeem to grab your favorite product from our Stellar Store. In fact, we are not shy to confess that this is the most fun part of our job, obviously next to seeing your happy experience while checking out!</p>
            <p>Here’s how it works- you’ll earn ‘A’ Lumens every time you shop at our site. As you collect more points, you move up to bigger benefits. HealthKart Stellar features four reward levels namely (in ascending order)</p>
            <p class="embedMargin5">-${bronze.badgeName}</p>
            <p class="embedMargin5">-${silver.badgeName}</p>
            <p class="embedMargin5">-${gold.badgeName}</p>
            <p class="embedMargin5">-${platinum.badgeName}</p>

            <p>It is easy to opt-in. Register. Log in. Shop! Stack up Lumens and unlock some very exciting rewards, indeed.</p>

            <h1 id="howItWorks" class="embedMarginTop50">HOW IT WORKS</h1>

            <p>Depending upon your order history of first or last 12 months, you will be assigned a particular level, that is, either Bronze or Silver or Gold or Platinum, and will be credited with 0 Lumens.</p>
            <p>On every purchase,</p>
	        <p class="embedMargin5">For Bronze level, you get ${bronze.loyaltyPercentage} Lumen for every rupee you spend.</p>
            <p class="embedMargin5">For Silver level, you are awarded ${silver.loyaltyPercentage} Lumens for every rupee you spend.</p>
            <p class="embedMargin5">For Gold level, you are awarded ${gold.loyaltyPercentage} Lumens for every rupee you spend.</p>
            <p class="embedMargin5">For Platinum level, you are awarded ${platinum.loyaltyPercentage} Lumens for every rupee you spend.</p>

            <h1 id="benefits" class="embedMarginTop50">BENEFIT LEVELS</h1>

            <!-- Table starts -->
            <div class="tableHistory embedMarginTop40">
              <div class="headingRow">
                <div class="headRowValue"><div class="rowValue">ANNUAL SPEND</div></div>
                <div class="headRowValue">Rs.${bronze.minScore} - Rs.${bronze.maxScore}</div>
                <div class="headRowValue">Rs.${silver.minScore} - Rs.${silver.maxScore}</div>
                <div class="headRowValue">Rs.${gold.minScore} - Rs.${gold.maxScore}</div>
                <div class="headRowValue">Rs.${platinum.minScore} and above</div>
              </div>

              <div class="normalRowWithBorderBottom">
                <div class="headRowValue"><div class="rowValue">POINTS EARNED PER RUPEE SPENT</div></div>
                <div class="headRowValue"><div class="rowValue">${bronze.loyaltyPercentage/100}</div></div>
                <div class="headRowValue"><div class="rowValue">${silver.loyaltyPercentage/100}</div></div>
                <div class="headRowValue"><div class="rowValue">${gold.loyaltyPercentage/100}</div></div>
                <div class="headRowValue"><div class="rowValue">${platinum.loyaltyPercentage/100}</div></div>
              </div>

              <div class="normalRowWithBorderBottom">
                <div class="headRowValue"><div class="rowValue">FREE NUTRITIONAL COUNSELLING SESSIONS</div></div>
                <div class="headRowValue"><div class="rowValue">1 Session worth Rs.1000</div></div>
                <div class="headRowValue"><div class="rowValue">1 Session worth Rs.2000 per 6 months</div></div>
                <div class="headRowValue"><div class="rowValue">1 Session worth Rs.4000 per 3 months</div></div>
                <div class="headRowValue"><div class="rowValue">1 Session worth Rs.5000 per 2 months</div></div>
              </div>

              <div class="normalRowWithBorderBottom">
                <div class="headRowValue"><div class="rowValue">FREE GIFTS & SERVICES</div></div>
                <div class="headRowValue"><div class="rowValue">N/A</div></div>
                <div class="headRowValue"><div class="rowValue">N/A</div></div>
                <div class="headRowValue"><div class="rowValue">Applicable</div></div>
                <div class="headRowValue"><div class="rowValue">Applicable</div></div>
              </div>

              <div class="normalRowWithBorderBottom removeBorderBottom">
                <div class="headRowValue"><div class="rowValue">MOVE UP THE SERVICE QUEUE</div></div>
                <div class="headRowValue"><div class="rowValue">Yes</div></div>
                <div class="headRowValue"><div class="rowValue">Yes</div></div>
                <div class="headRowValue"><div class="rowValue">Yes</div></div>
                <div class="headRowValue"><div class="rowValue">Yes</div></div>
              </div>
            </div>
            <!-- Table Ends -->

            <h1 id="faq" class="faq">FAQs <span>(Inquiring and loyal minds want to know)</span></h1>
            <h2 class="genQues">GENERAL QUESTIONS ABOUT THE PROGRAM</h2>
            
            <div class="qAndA">
              <p>Q: First things first. What is HealthKart Stellar?</p>
              A: HealthKart Stellar is our loyalty program. You get great benefits. Oh, yes, there are some goodies too. Click here to find out
              more.
            </div>

            <div class="qAndA">
              <p>Q: First things first. What is HealthKart Stellar?</p>
              A: HealthKart Stellar is our loyalty program. You get great benefits. Oh, yes, there are some goodies too. Click here to find out
              more.
            </div>

            <div class="qAndA">
              <p>Q: First things first. What is HealthKart Stellar?</p>
              A: HealthKart Stellar is our loyalty program. You get great benefits. Oh, yes, there are some goodies too. Click here to find out
              more.
            </div>

            <div class="qAndA">
              <p>Q: First things first. What is HealthKart Stellar?</p>
              A: HealthKart Stellar is our loyalty program. You get great benefits. Oh, yes, there are some goodies too. Click here to find out
              more.
            </div>

            <div class="qAndA">
              <p>Q: First things first. What is HealthKart Stellar?</p>
              A: HealthKart Stellar is our loyalty program. You get great benefits. Oh, yes, there are some goodies too. Click here to find out
              more.
            </div>

            <div class="qAndA removeBorderBottom">
              <p>Q: First things first. What is HealthKart Stellar?</p>
              A: HealthKart Stellar is our loyalty program. You get great benefits. Oh, yes, there are some goodies too. Click here to find out
              more.
            </div>

          </div>

        </div>

      </div>
    </div>

<!-- The about us page ends here -->
</stripes:layout-component>
</stripes:layout-render>

