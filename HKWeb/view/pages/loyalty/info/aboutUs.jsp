<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="com.akube.framework.util.BaseUtils"%>
<%@ page import="com.hk.constants.core.HealthkartConstants"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="com.hk.taglibs.Functions"%>
<%@ page import="java.util.Arrays"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-render name="/pages/loyalty/info/layoutStatic.jsp">
	<stripes:layout-component name="contents">
	<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyIntroductionAction" var="lia" />  
	
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
                  <span >Program Structure</span>
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
              <img alt="1" class="stellarLogo" src="${pageContext.request.contextPath}/pages/loyalty/resources/images/stellarLogo.png"/>
            </div>
            <div class="dottedLine"></div>
          </div>
	<c:set var="bronze" value="${lia.badgeList[1]}" />
		<c:set var="silver" value="${lia.badgeList[2]}" />
		<c:set var="gold" value="${lia.badgeList[3]}" />
		<c:set var="platinum" value="${lia.badgeList[4]}" />
	
          <div class="aboutContent">
            <p id="about">At HealthKart, we would like to give you more of what you enjoy and redeem to grab your favorite product from our Stellar Store. In fact, we are not shy to confess that this is the most fun part of our job, obviously next to seeing your happy experience while checking out!</p>
            <p>Here’s how it works- you’ll earn certain Loyalty points (depending upon your tier) every time you shop at our site. As you collect more points, you move up to bigger benefits. HealthKart Stellar features four reward levels namely (in ascending order)</p>
            <p class="embedMargin5">-${bronze.badgeName}</p>
            <p class="embedMargin5">-${silver.badgeName}</p>
            <p class="embedMargin5">-${gold.badgeName}</p>
            <p class="embedMargin5">-${platinum.badgeName}</p>

            <p>It is easy to opt-in. Register. Log in. Shop! Stack up Loyalty Points and unlock some very exciting rewards, indeed.</p>

            <h1 id="howItWorks" class="embedMarginTop50">PROGRAM STRUCTURE</h1>
				
			<p class="embedMarginTop40">•	For existing HealthKart customers</p>
			<p>After signing up for healthkartstellar, depending upon your order history of last 12 months, you will be assigned a particular benefit tier,
			 that is, either Bronze or Silver or Gold or Platinum (or none of the above). To show our profuse love you will begin with 15 points as bonus!</p>
			 
            <p class="embedMargin5">•	New HealthKart customers</p>
			<p>For all newbies, once you sign up to our loyalty program, you will be entered into a basic level. To be a part of our privileged group, 
			shop more than Rs. 1500 or above. At the beginning, as a token of appreciation you will be credited with 15 bonus points</p>
			
	        <p>On every purchase, you will earn loyalty points as follows:</p>
	        <p class="embedMargin5">For ${bronze.badgeName} level, you will earn ${bronze.loyaltyPercentage}% of total amount you spend while shopping. </p>
            <p class="embedMargin5">For ${silver.badgeName} level, you will earn ${silver.loyaltyPercentage}% of total amount you spend while shopping. </p>
            <p class="embedMargin5">For ${gold.badgeName} level, you will earn ${gold.loyaltyPercentage}% of total amount you spend while shopping. </p>
            <p class="embedMargin5">For ${platinum.badgeName} level, you will earn ${platinum.loyaltyPercentage}% of total amount you spend while shopping. </p>

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
                <div class="headRowValue"><div class="rowValue">TIER</div></div>
                <div class="headRowValue"><div class="rowValue">${bronze.badgeName}</div></div>
                <div class="headRowValue"><div class="rowValue">${silver.badgeName}</div></div>
                <div class="headRowValue"><div class="rowValue">${gold.badgeName}</div></div>
                <div class="headRowValue"><div class="rowValue">${platinum.badgeName}</div></div>
              </div>

              <div class="normalRowWithBorderBottom">
                <div class="headRowValue"><div class="rowValue">PECENTAGE EARNED ON TOTAL AMOUNT SPENT</div></div>
                <div class="headRowValue"><div class="rowValue">${bronze.loyaltyPercentage}%</div></div>
                <div class="headRowValue"><div class="rowValue">${silver.loyaltyPercentage}%</div></div>
                <div class="headRowValue"><div class="rowValue">${gold.loyaltyPercentage}%</div></div>
                <div class="headRowValue"><div class="rowValue">${platinum.loyaltyPercentage}%</div></div>
              </div>

              <div class="normalRowWithBorderBottom">
                <div class="headRowValue"><div class="rowValue">FREE NUTRITIONAL COUNSELLING SESSIONS PER ANNUM</div></div>
                <div class="headRowValue"><div class="rowValue">1 Sessions </div></div>
                <div class="headRowValue"><div class="rowValue">2 Sessions </div></div>
                <div class="headRowValue"><div class="rowValue">3 Sessions </div></div>
                <div class="headRowValue"><div class="rowValue">4 Sessions </div></div>
              </div>

              <div class="normalRowWithBorderBottom">
                <div class="headRowValue"><div class="rowValue">FREE GIFTS & SERVICES</div></div>
                <div class="headRowValue"><div class="rowValue">N/A</div></div>
                <div class="headRowValue"><div class="rowValue">N/A</div></div>
                <div class="headRowValue"><div class="rowValue">Applicable</div></div>
                <div class="headRowValue"><div class="rowValue">Applicable</div></div>
              </div>

            </div>            <!-- Table Ends -->

            <h1 id="faq" class="faq">FAQs <span>(Inquiring and loyal minds want to know)</span></h1>
            <h2 class="genQues">GENERAL QUESTIONS ABOUT THE PROGRAM</h2>
            
            <div class="qAndA">
              <p>Q: First things first. What is HealthKart Stellar?</p>
              A: HealthKart Stellar is our loyalty program. You get great benefits. Oh, yes, there are some goodies too. 
              <a href="${pageContext.request.contextPath}/loyaltypg" class="blue">Click here</a> to find out
              more.
            </div>

            <div class="qAndA">
              <p>Q: How do I become a member?</p>
              A: Just <a href="${pageContext.request.contextPath}/loyaltypg" class="blue">click here</a>, create your profile and hit submit. That's it. We will take care of the rest. 
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
	 		<p>Q: What will be my initial level?</p>
	 		 A: If you are already a healthkart customer, you will enjoy membership level based on your successful orders  (delivered orders) with a 
	 		 cumulative value of 1500 INR or above processed in the last 12 months. However, if you are a new customer with no orders processed to your name,
	 		   you will have no membership level until your order history value sums up to 1500 INR or above. 
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
             A: In simple words: shop more. The more you shop, the better your upgrade. Here's how: <br/>
             Once you gain by a level, you will be at that level  for the next 12 months, even if your expenditure is zero or a minimum 
             with us during that period. However, you will upgrade to the next higher level whenever your cumulative shopping value becomes eligible
              for the next upgrade in the existing 12-month period. As soon as you climb a level, you will be at that level for the next 12 months 
              from the date of upgrade.<br/>At the end of the said 12 months, cumulative value of your shopping amount from the last 12 months will
               be evaluated. You will then either upgrade, stay at the same level or regress to the respective levels, depending on your expenditure trend. 
            </div>
              
            <div class="qAndA removeBorderBottom">
              <p>Q: I have more questions. Who can help? </p>
              A: Call the HealthKart Customer Support for general questions about the healthkartstellar program, as well as for questions regarding your membership.
				Contact us 24X 7 at
				0124-4616444 or mail us your query at info@healthkart.com.
				You can also find us on Facebook and Twitter. We love connecting with you and are here to answer any questions or just enjoy hearing about how your day is going. 

            </div>
			
			
          </div>

        </div>

      </div>
    </div>
<!-- The about us page ends here -->
</stripes:layout-component>
</stripes:layout-render>
