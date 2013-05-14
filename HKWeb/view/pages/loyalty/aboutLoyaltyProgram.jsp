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
            <p>Here’s how it works- you’ll earn ‘A’ Loyalty Points every time you shop at our site. As you collect more points, you move up to bigger benefits. HealthKart Stellar features four reward levels namely (in ascending order)</p>
            <p class="embedMargin5">-${bronze.badgeName}</p>
            <p class="embedMargin5">-${silver.badgeName}</p>
            <p class="embedMargin5">-${gold.badgeName}</p>
            <p class="embedMargin5">-${platinum.badgeName}</p>

            <p>It is easy to opt-in. Register. Log in. Shop! Stack up Loyalty Points and unlock some very exciting rewards, indeed.</p>

            <h1 id="howItWorks" class="embedMarginTop50">HOW IT WORKS</h1>

            <p>Depending upon your order history of first or last 12 months, you will be assigned a particular level, that is, either Bronze or Silver or Gold or Platinum, and will be credited with 15 Loyalty Points as bonus.</p>
            <p>On every purchase,</p>
	        <p class="embedMargin5">For Bronze level, you get ${bronze.loyaltyPercentage} Loyalty Point for every rupee you spend.</p>
            <p class="embedMargin5">For Silver level, you are awarded ${silver.loyaltyPercentage} Loyalty Points for every rupee you spend.</p>
            <p class="embedMargin5">For Gold level, you are awarded ${gold.loyaltyPercentage} Loyalty Points for every rupee you spend.</p>
            <p class="embedMargin5">For Platinum level, you are awarded ${platinum.loyaltyPercentage} Loyalty Points for every rupee you spend.</p>

            <h1 id="benefits" class="embedMarginTop50">BENEFIT LEVELS</h1>

            <!-- Table starts -->
            <div class="tableHistory embedMarginTop40">
              <div class="headingRow">
                <div class="headRowValue"><div class="rowValue">ANNUAL SPEND</div></div>
                <div class="headRowValue">Rs.${hk:roundNumberForDisplay(bronze.minScore)} - Rs.${hk:roundNumberForDisplay(bronze.maxScore)}</div>
                <div class="headRowValue">Rs.${hk:roundNumberForDisplay(silver.minScore)} - Rs.${hk:roundNumberForDisplay(silver.maxScore)}</div>
                <div class="headRowValue">Rs.${hk:roundNumberForDisplay(gold.minScore)} - Rs.${hk:roundNumberForDisplay(gold.maxScore)}</div>
                <div class="headRowValue">Rs.${hk:roundNumberForDisplay(platinum.minScore)} and above</div>
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
              <p>Q: I forgot my password. Now what? </p>
              A: If you have any unanswered queries, please feel free to contact the HealthKart Customer Support at 0124-4616444. 
            </div>

            <div class="qAndA">
              <p>Q: Can I transfer my points to my friends? </p>
              A: No, points are non-transferrable.
            </div>

           <div class="qAndA">
              <p>Q: Where can I find my current level status?</p>
              A: Your current level status is displayed on the top of the HealthKart home page. 
              As well as, on healthkartstellar home page under “My Accounts”. 
              This page also carries information about total points accumulated, date of expiry and order history.
            </div>
	
			<div class="qAndA">
              <p>Q: Can I transfer my points to my friends? </p>
              A: No, points are non-transferrable.
            </div>
			
			<div class="qAndA">
              <p>Q: How much time does it take to update my information? </p>
              A: As soon as you make any purchase and it gets successfully delivered, your information will be automatically updated. 
            </div>
			
			<div class="qAndA">
              <p>Q: Is there an expiry date for my loyalty points? </p>
              A: Your points do expire within 2 years, from the date you receive them. Please note, if you convert them to rewards points, they will expire in 6 months. 
            </div>

            <div class="qAndA">
              <p>Q: What are Reward Points? </p>
              A: Rewards points can be described as currency for HealthKart site. To explain the process, when loyalty points are converted to redeem points,
               they are credited to your HK account. With these points you can shop anything from HeathKart. If the product is of higher value than the points,
                that much will be discounted from the total amount due. You will need to pay the rest for successful delivery. 
            </div>

            <div class="qAndA">
              <p>Q: Does it mean that my loyalty points can be converted to reward points? </p>
              A: Yes
            </div>

            <div class="qAndA">
              <p>Q: How can I earn more points? </p>
              A: All you have to do is shop and order a product from the HealthKart website. You will automatically earn some points; depending upon the level you are placed. 
            </div>

            <div class="qAndA">
              <p>Q: How can I redeem my points?</p>
              A: As soon as you make any purchase and it gets successfully delivered, your information will be automatically updated. 
              Stellar Store is one shop store which has products ranging from all categories at HealthKart. The currency here is only loyalty points.
               For your favorite goodie, click on add to my cart and check out. During check out, the points will be deducted from the total.
            </div>            
           
            <div class="qAndA">
              <p>Q: I am interested to pick a product from the Stellar Store but I am short of points? What should I do? </p>
              A: The only currency at the Stellar Store is points. Unfortunately you can’t exchange them in lieu of money.
               If you are interested in a purchase but short of them, you need to shop further on HealthKart and earn more points. 
            </div>
           
            <div class="qAndA">
              <p>Q: How to upgrade from one level to the other? </p>
              A: Awaited. 
            </div>
              
            <div class="qAndA removeBorderBottom">
              <p>Q: I have more questions. Who can help? </p>
              A: Call the HealthKart Customer Support for general questions about the healthkartstellar program, as well as for questions regarding your membership.
				Contact us 24X 7 @
				0124-4616444 or mail us your query @ info@healthkart.com.
				You can also find us on Facebook and Twitter. We love connecting with you and are here to answer any questions or just enjoy hearing about how your day is going. 

            </div>
			
			
          </div>

        </div>

      </div>
    </div>

<!-- The about us page ends here -->
</stripes:layout-component>
</stripes:layout-render>

