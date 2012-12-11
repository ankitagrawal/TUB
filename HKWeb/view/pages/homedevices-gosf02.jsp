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
    

     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HP070' productDesc='A light-weight stethoscope with a robust nylon cuff. '/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HICKS013' productDesc='A surgical dressing pad that helps clean blood and other impurities from wounds.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN014' productDesc='A nebulizer that stimulates easy breathing for asthmatic patients.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB006' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='METRO001' productDesc='A facial sauna that unclog pores through deep perspiration.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HT006' productDesc='A digital thermometer that gives accurate results in just 10 seconds.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FRSTA004' productDesc='A must have first aid kit that contains all that is necessary.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OMRN008' productDesc='A digital weighing scale that comes with 4 sensor, accurate weight measurement technology'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SCARE001' productDesc='A sphygmomanometer for accurate measurement of blood pressure levels.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX015' productDesc='A digital weighing scale that comes with an auto on & off function. It has a capacity to hold up to 150 kg.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX013' productDesc='An analog weighing scale with an anti-slip rubber top. It can hold up to 130 kg.'/>
     
     
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

