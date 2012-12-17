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

            <a class="next"  href="${pageContext.request.contextPath}/pages/homedevices-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/homedevices-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>

        <div class="cl"></div>

     
      <div class="prodBoxes">
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WA002' productDesc="A sturdy, well-built walking stick. Perfect companion for the elderly."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FRSTA004' productDesc="A must have first aid kit that contains all that is necessary."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB012' productDesc="A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX015' productDesc="A digital weighing scale that comes with an auto on & off function. It has a capacity to hold up to 150 kg."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN008' productDesc="A digital weighing scale that comes with 4 sensor, accurate weight measurement technology"/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SCARE001' productDesc="A sphygmomanometer for accurate measurement of blood pressure levels."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX013' productDesc="An analog weighing scale with an anti-slip rubber top. It can hold up to 130 kg."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX014' productDesc="An analog weighing scale with an anti-slip rubber top. It can weigh up to 120 kg."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX007' productDesc="A digital weighing scale that holds reading upto 5 seconds before it goes off."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WRSTS002' productDesc="An elastic wrist wrap that supports injuries. it comes in different sizes."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TYNOR016' productDesc="A knee support made from neoprene that helps recover knee injuries."/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN014' productDesc="A nebulizer that stimulates easy breathing for asthmatic patients."/>				        
      </div>

                   <div class="cl"></div>

     <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/homedevices-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/homedevices-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>

     


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

