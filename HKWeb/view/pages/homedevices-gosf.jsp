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





    
    <div class="heading1">HEALTH DEVICES</div>

   <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/homedevices-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/homedevices-gosf02.jsp">Next →</a>

         </div>

        <div class="cl"></div>

     
      <div class="prodBoxes">
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB005' productDesc='An automatic BP monitor with one-touch operation. It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RD001' productDesc='A waist shaper that supports the lumbar spine and corrects posture. Ideal for women who want to look slim and trim.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HR005' productDesc='A nebulizer that stimulates easy breathing. Ideal for asthmatic patients.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='RW007' productDesc='A lycra/far infrared wrist wrap that supports injuries.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WA002' productDesc='A sturdy, well-built walking stick. Perfect companion for the elderly.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB012' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX007' productDesc='A digital weighing scale that holds reading upto 5 seconds before it goes off.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TYNOR016' productDesc='A knee support made from neoprene that helps recover knee injuries.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB014' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='CERSUP004' productDesc='A pillow that counters stress and strain for patients suffering from cervical spondylosis.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SUN006' productDesc='A facial sauna that helps achieve flawless and unblemished skin.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ROSMAX010' productDesc='A light-weight stethoscope with a robust nylon cuff. '/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HB012' productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EQNX014' productDesc='An analog weighing scale with an anti-slip rubber top. It can weigh up to 120 kg.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='WRSTS002' productDesc='An elastic wrist wrap that supports injuries. it comes in different sizes.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='HICKS013' productDesc='A surgical dressing pad that helps clean blood and other impurities from wounds.'/>

     
     
     
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

