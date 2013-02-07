<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Great Online Shopping Festival">

<s:layout-component name="htmlHead">
 <link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css" />


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

    <h1 class="title">
       Great Online Shopping Festival
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


        <div class="cl"></div>

      <div class="prodBoxes">
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HV003' productDesc="A facial sauna that keeps cold and cough away with flawless and unblemished skin."/>      	
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT034' productDesc="Savory crisps. great for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours"/>
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT033' productDesc="A snack bar that stabilizes sugar levels for up to 9 hours. Ideal for growing children, diabetics & fitness enthusiasts."/>
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT035' productDesc="Indulgent drizzles with just 2g. of sugar. Ideal for growing children, diabetic patients, and fitness enthusiasts"/>
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SWT036' productDesc="Velvety Shakes for growing children, diabetic patients and fitness enthusiasts that stabilizes sugar levels for 9 hours"/>
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN007' productDesc="Made of silver coated safety glass, this digital weighing scale comes with a convenient on-off function & large LCD display."/>
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB004' productDesc="Comes with simple 1 touch technology to display BP & pulse rate values with ease."/>
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc="Self-testing blood glucose strips with accurate results, for Diabetic Patients"/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM007' productDesc="A simple three-step, self-testing glucometer with storage capacity of up to 500 test results"/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM024' productDesc="Accurate results in just 5 minutes with auto coding technology. A glucometer with life time warranty."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS003' productDesc="Self-testing sensor comfort strips to determine blood glucose levels with greater accuracy"/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM040' productDesc="Accurate results in 5 minutes with auto coding technology, A glucometer with life time warranty and 25 testing-strips"/>				
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DL008' productDesc="Round Blood lancets with sterile tip, compatible with all major lancing devices."/>		
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM018' productDesc="Equipped with biosensor technology that gives fast and reliable results in just 9 seconds."/>
	  </div>

      <div class="cl"></div>
        


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

