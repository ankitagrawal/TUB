<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
 <link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css" />


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

    <h1 class="title">
       Cyber Monday Great Online Shopping Festival
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

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/diabetes-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/diabetes-gosf02.jsp">Next →</a>

         </div>

        <div class="cl"></div>

      <div class="prodBoxes">

        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='CERSUP004' productDesc="A pillow that counters stress and strain for patients suffering from cervical spondylosis."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DL008' productDesc="Round Blood lancets with sterile tip, compatible with all major lancing devices."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM007' productDesc="A simple three-step, self-testing glucometer with storage capacity of up to 500 test results"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM018' productDesc="Equipped with biosensor technology that gives fast and reliable results in just 9 seconds."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM024' productDesc="Accurate results in just 5 minutes with auto coding technology. A glucometer with life time warranty."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DM040' productDesc="Accurate results in 5 minutes with auto coding technology, A glucometer with life time warranty and 25 testing-strips"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS001' productDesc="Self-testing blood glucose strips with accurate results, for Diabetic Patients"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DS003' productDesc="Self-testing sensor comfort strips to determine blood glucose levels with greater accuracy"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX007' productDesc="A digital weighing scale that holds reading upto 5 seconds before it goes off."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX013' productDesc="An analog weighing scale with an anti-slip rubber top. It can hold up to 130 kg."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX014' productDesc="An analog weighing scale with an anti-slip rubber top. It can weigh up to 120 kg."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX015' productDesc="A digital weighing scale that comes with an auto on & off function. It has a capacity to hold up to 150 kg."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FRSTA004' productDesc="A must have first aid kit that contains all that is necessary."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB005' productDesc="An automatic BP monitor with one-touch operation. It comes with one-year warranty."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB006' productDesc="A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB012' productDesc="A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB014' productDesc="A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HICKS013' productDesc="A surgical dressing pad that helps clean blood and other impurities from wounds."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HP070' productDesc="A light-weight stethoscope with a robust nylon cuff. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HR005' productDesc="A nebulizer that stimulates easy breathing. Ideal for asthmatic patients."/>

       

          </div>

      <div class="cl"></div>

        <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/diabetes-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/diabetes-gosf02.jsp">Next →</a>

         </div>


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

