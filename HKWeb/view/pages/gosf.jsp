<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = pageContext.getRequest().isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Great Online Shopping Festival">

<s:layout-component name="htmlHead">


	<link href="${pageContext.request.contextPath}/css/gosf.css"
	      rel="stylesheet" type="text/css"/>

</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Cyber
		Monday</span>

		<h1 class="title">Great Online Shopping Festival</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Google Cyber Monday</s:layout-component>
<s:layout-component name="metaKeywords">Google Cyber Monday</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

<div id="pageContainer"><img
		src="${pageContext.request.contextPath}/images/GOSF/banner.jpg" alt="gosf banner"/>

<div class="cl"></div>

<jsp:include page="../includes/_menuGosf.jsp"/>

<div class="cl"></div>


<div class="heading1">PRIME DEALS</div>
<div class="prodBoxes">
	<s:layout-render
			name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001'
			productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='PW006'
	                 productDesc="Test your pregnancy in just 2 minutes with this Instant Ovulation Kit"/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='PP002'
	                 productDesc="Protect yourself with this powerful pepper formula spray."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='PHL002'
	                 productDesc="Figure out the best two days you will be most fertile. Grab this I-Sure Ovulation Strip to get pregnant for sure. "/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='PP001'
	                 productDesc="Protect yourself with this powerful pepper formula spray."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE005'
	                 productDesc='These lenses made with a slight tint correct the vision. The presence of Hilaficon B and non-ionic B Lens material comfort the eyes and facilitates smooth lens-lid interaction.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='KNOCK001'
	                 productDesc="Protect yourself with this powerful pepper formula spray."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='DS001'
	                 productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='HB005'
	                 productDesc="An automatic BP monitor with one-touch operation. It comes with one-year warranty."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NIC001'
	                 productDesc="Nicorette reins in your constant urge to smoke. Quit smoking for sure."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='OH101' productDesc=""/> 
    <s:layout-render
		name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE004'
		productDesc='These lenses help in vision correction. Also capable of blocking UVA and UVB rays, they also protect the eyes.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='DS001'
	                 productDesc="Self-testing blood glucose strips with accurate results"/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='DM024'
	                 productDesc='Accurate results in just 5 minutes with auto coding technology. A glucometer with life time warranty.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='DM007'
	                 productDesc='A simple three-step, self-testing glucometer with storage capacity of up to 500 test results'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT986'
	                 productDesc='A supplement that provides Creating for enhancing stamina and building muscles. Ideal for intense workouts.'/>
	



</div>



<div class="cl"></div>
     <div id="pagerTrigger"></div>

</div>


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

<s:layout-component name="menu">
</s:layout-component>

</s:layout-render>

