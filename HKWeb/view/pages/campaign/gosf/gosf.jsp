<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
	boolean isSecure = SslUtil.isSecure();
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

    <%@include file="/includes/_menuGosf.jsp" %>

<div class="cl"></div>


<div class="heading1">PRIME DEALS</div>
<div class="prodBoxes">
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RD001' />
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1035' productDesc='These classic square sunglasses protect your eyes from harmful UV rays and dust. These are highly durable, made from unbreakable plastic with scratch free lenses.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HV003' productDesc='A facial sauna that keeps cold and cough away. Side effects include flawless and unblemished skin.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1600' productDesc='Suitable for people who want to lose weight, it helps burn fat, maintain stamina and improve metabolism.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT034' productDesc="Savory crisps. great for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours"/>
    <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT033' productDesc="A snack bar that stabilizes sugar levels for up to 9 hours. Ideal for growing children, diabetics & fitness enthusiasts."/>
    <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT035' productDesc="Indulgent drizzles with just 2g. of sugar. Ideal for growing children, diabetic patients, and fitness enthusiasts"/>
    <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT036' productDesc="Velvety Shakes for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours"/>

	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
    <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB004' productDesc="Comes with simple 1 touch technology to display BP & pulse rate values with ease."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PW006' productDesc="Test your pregnancy in just 2 minutes with this Instant Ovulation Kit"/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT2101' productDesc="Wear your attitude while you train, for fitness is your religion."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL002' productDesc="Figure out the best two days you will be most fertile. Grab this I-Sure Ovulation Strip to get pregnant for sure. "/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP001' productDesc="Protect yourself with this powerful pepper formula spray."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE005' productDesc='These lenses made with a slight tint correct the vision. The presence of Hilaficon B and non-ionic B Lens material comfort the eyes and facilitates smooth lens-lid interaction.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KNOCK001' productDesc="Protect yourself with this powerful pepper formula spray."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc='Self-testing blood glucose strips with accurate results, for Diabetic Patients'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB005' productDesc="An automatic BP monitor with one-touch operation. It comes with one-year warranty."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NIC001' productDesc="Nicorette reins in your constant urge to smoke. Quit smoking for sure."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH101' productDesc="Get the best head! Choose between soft or medium. Sparkling teeth are here forever."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE004' productDesc='These lenses help in vision correction. Also capable of blocking UVA and UVB rays, they also protect the eyes.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc="Self-testing blood glucose strips with accurate results"/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM024' productDesc='Accurate results in just 5 minutes with auto coding technology. A glucometer with life time warranty.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM007' productDesc='A simple three-step, self-testing glucometer with storage capacity of up to 500 test results'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT986' productDesc='A supplement that provides Creating for enhancing stamina and building muscles. Ideal for intense workouts.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH006' productDesc="Get a  cleaner tongue with this carefully tested cleaner blister"/>
	<!--<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT469' productDesc=' Rich in L-Carnitine, it burns your extra fat, reduces hunger pangs, improves metablosim, and reduces cholestrol.'/>
	-->
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT130' productDesc='Provides 11g of Leucine and 13g of additional BCAAs per serving. Ideal for rapid muscle gain and recovery.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1369' productDesc='Build strength and endurance with MuscleBlaze Creatine. It consistently delivers a powerful workout performance.'/>


	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT131' productDesc='Get the purest whey protein money can buy. All this with no carbs whatsoever. '/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT412' productDesc='A supplement that improves the nutrient absorption power in adults and boosts energy levels. Ideal for senior citizens.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1368' productDesc='Designed for people who want to gain mass. Packed with 52gm of high quality protein blend and 9gm of added fiber.'/>
</div>



<div class="cl"></div>


</div>


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

<s:layout-component name="menu">
</s:layout-component>

</s:layout-render>

