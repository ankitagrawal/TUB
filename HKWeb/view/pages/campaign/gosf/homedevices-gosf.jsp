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





    
    <div class="heading1">HEALTH DEVICES</div>

   <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/homedevices-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/homedevices-gosf02.jsp">Next →</a>

         </div>

        <div class="cl"></div>

     
      <div class="prodBoxes">
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB005' productDesc="An automatic BP monitor with one-touch operation. It comes with one-year warranty."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RD001' productDesc="A waist shaper that supports the lumbar spine and corrects posture. Ideal for women who want to look slim and trim."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB014' productDesc="A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB006' productDesc="A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HR005' productDesc="A nebulizer that stimulates easy breathing. Ideal for asthmatic patients."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='CERSUP004' productDesc="A pillow that counters stress and strain for patients suffering from cervical spondylosis."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HT006' productDesc="A digital thermometer that gives accurate results in just 10 seconds."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RW007' productDesc="A lycra/far infrared wrist wrap that supports injuries."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SUN006' productDesc="A facial sauna that helps achieve flawless and unblemished skin."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HICKS013' productDesc="A surgical dressing pad that helps clean blood and other impurities from wounds."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ROSMAX010' productDesc="A light-weight stethoscope with a robust nylon cuff. "/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HP070' productDesc="A light-weight stethoscope with a robust nylon cuff. "/>		      	
      </div>

                   <div class="cl"></div>

     <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/homedevices-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/homedevices-gosf02.jsp">Next →</a>

         </div>

     


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

