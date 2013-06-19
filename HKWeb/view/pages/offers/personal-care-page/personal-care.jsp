<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="personal-care Offer">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/personal-care.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Beauty Offers</span>

		<h1 class="title">Beauty Offers</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Beauty Offers</s:layout-component>
<s:layout-component name="metaKeywords">Beauty Offers</s:layout-component>


<s:layout-component name="content">

    <div class="container">
    	<div class="nRow"><img src="images/personal-care-page-banner.jpg" alt="Taking care of your personal needs for a better hygiene" /></div>
        <div class="nRow pCarePageSec1">
      	  <h1><a href="http://www.healthkart.com/personal-care/sexual-wellness">Sexual Wellness</a></h1>
            <h2><a href="http://www.healthkart.com/personal-care/sexual-wellness">Sex is not just about physical pleasure; it must also include safety & wellbeing.</a>
            <br />
          	Prevent infection and unwanted pregnancy.</h2>
      </div>
        <div class="nRow"><img src="images/sexual-wellness.jpg" alt="Sexual Wellness Proucts" usemap="#Map" border="0" />
          <map name="Map" id="Map">
            <area shape="poly" coords="19,119,21,21,166,20,849,1,856,108" href="http://www.healthkart.com/personal-care/sexual-wellness/condoms" />
            <area shape="poly" coords="8,221,7,120,846,126,844,232" href="http://www.healthkart.com/personal-care/sexual-wellness/accessories" />
            <area shape="poly" coords="30,324,32,223,880,250,869,354" href="http://www.healthkart.com/personal-care/sexual-wellness/fertility" />
          </map>
        </div>
        <div class="deviderGLine">&nbsp;</div>
        <div class="nRow pCarePageSec2">
        	<h1><a href="http://www.healthkart.com/personal-care/women">Women</a></h1>
            <h2><a href="http://www.healthkart.com/personal-care/women">Covering it all! You are important for us so is your health and safety.</a></h2>
      </div>
        <div class="nRow"><img src="images/women-care.jpg" alt="Women Care Products" usemap="#Map2" border="0" />
          <map name="Map2" id="Map2">
            <area shape="poly" coords="5,112,7,7,859,3,859,106" href="http://www.healthkart.com/personal-care/women/self-defense" />
            <area shape="poly" coords="30,218,29,114,871,125,869,226" href="http://www.healthkart.com/personal-care/women/ovulation-kits" />
            <area shape="poly" coords="3,321,1,217,844,245,842,345" href="http://www.healthkart.com/personal-care/women/pregnancy" />
          </map>
        </div>
        <div class="deviderGLine">&nbsp;</div>
      <div class="nRow pCarePageSec3">
        	<h1><a href="http://www.healthkart.com/personal-care/oral-hygiene">Oral Hygiene</a></h1>
            <h2><a href="http://www.healthkart.com/personal-care/oral-hygiene">Prevent dental problems and keep your mouth clean and fresh.</a></h2>
      </div>
        <div class="nRow"><img src="images/oral-hygiene.jpg" alt="Oral Hygiene Products" usemap="#Map3" border="0" />
          <map name="Map3" id="Map3">
            <area shape="poly" coords="15,109" href="#" />
            <area shape="poly" coords="13,111,13,7,865,3,866,107" href="http://www.healthkart.com/personal-care/oral-hygiene/electronic-toothbrushes" />
            <area shape="poly" coords="2,220,1,117,853,119,851,225" href="http://www.healthkart.com/personal-care/oral-hygiene/manual-toothbrushes" />
            <area shape="poly" coords="34,324,34,219,878,239,874,343" href="http://www.healthkart.com/personal-care/oral-hygiene/interdental-brush" />
          </map>
      </div>
        <div class="deviderGLine">&nbsp;</div>
      <div class="nRow pCarePageSec4">
          	<h1><a href="http://www.healthkart.com/personal-care/foot-care-pedicure">Foot Care / Pedicure</a></h1>
            <h2><a href="http://www.healthkart.com/personal-care/foot-care-pedicure">Cosmetic and therapeutic treatment offering proper care for your feet.</a></h2>
          </div>
        <div class="nRow"><img src="images/foot-care-pedicure.jpg" width="884" height="347" usemap="#Map4" border="0" />
          <map name="Map4" id="Map4">
            <area shape="poly" coords="21,116,19,12,871,1,870,106" href="http://www.healthkart.com/personal-care/foot-care-pedicure/fresh-beautiful-feet" />
            <area shape="poly" coords="2,228,1,124,855,122,851,228" href="http://www.healthkart.com/personal-care/foot-care-pedicure/insoles" />
            <area shape="poly" coords="25,337,27,231,880,239,881,344" href="http://www.healthkart.com/personal-care/foot-care-pedicure/nail-care" />
          </map>
        </div>
        <div class="nRow">&nbsp;</div>
    </div>
    <c:if test="${not isSecure }">
    	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
    	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
    </c:if>


    </s:layout-component>

    </s:layout-render>

