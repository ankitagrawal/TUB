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

         <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/diabetes-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/diabetes-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>

        <div class="cl"></div>

      <div class="prodBoxes">
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HT014' productDesc="3-in-1 first aid kit, an essential for your medicine cabinet."/>
      	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HT013' productDesc="Well-equipped first aid kit ideal for office use."/>      	
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HT006' productDesc="A digital thermometer that gives accurate results in just 10 seconds."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='METRO001' productDesc="A facial sauna that unclog pores through deep perspiration."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN008' productDesc="A digital weighing scale that comes with 4 sensor, accurate weight measurement technology"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN014' productDesc="A nebulizer that stimulates easy breathing for asthmatic patients."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RD001' productDesc="A waist shaper that supports the lumbar spine and corrects posture. Ideal for women who want to look slim and trim."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ROSMAX010' productDesc="A light-weight stethoscope with a robust nylon cuff. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RW007' productDesc="A lycra/far infrared wrist wrap that supports injuries."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SCARE001' productDesc="A sphygmomanometer for accurate measurement of blood pressure levels."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SUN006' productDesc="A facial sauna that helps achieve flawless and unblemished skin."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TYNOR016' productDesc="A knee support made from neoprene that helps recover knee injuries."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WA002' productDesc="A sturdy, well-built walking stick. Perfect companion for the elderly."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WRSTS002' productDesc="An elastic wrist wrap that supports injuries. it comes in different sizes."/>
      </div>

      <div class="cl"></div>

        <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/diabetes-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/diabetes-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

