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
        <img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg"/>

        <div class="cl"></div>

        <jsp:include page="../includes/_menuGosf.jsp"/>

        <div class="cl"></div>


        <div class="heading1">SERVICES</div>
         <div class="prodBoxes">
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER208' productDesc="Access from 226 cities. Over 1200 nutritionist counsel you on 4300+ diabetes diet plan."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER233' productDesc="24X 7 confidential medical advice through phone and internet"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER228' productDesc="CBT, LFT, Lipid Profile, KFT, Thyroid Test, Diabetes Test all in one."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER229' productDesc="Avail the economical  service and keep your well-being under a healthy vigil."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER275' productDesc="Set your boundaries racing for perfection with unmatched service. Reasonable 1 month, 3 month and 6 month subscriptions. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER260' productDesc="Test for Vitamin B-12 level, Vitamin D3 level, Magnesium range and Complete Blood Count"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER262' productDesc="Offers scanned checkup to identify early symptoms of dengue in order to take appropriate actions accordingly."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER254' productDesc="Accurate diagnosis for Total Cholesterol level, TG Serum, HDL & LDL Cholesterol, VLDL Ratio, LDL/HDL Raito and Cholesterol/HDI Ratio."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER245' productDesc="Lose weight fast, the easy way."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER170' productDesc="Why wait to lose weight? Online personalised diet plans for you."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER019' productDesc="Measurable results are visible with  customized diet plans"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER004' productDesc="Wide range of tests for men to assess your health and provide suitable directions to avert them."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER004' productDesc="Provides overall physical examination and consultation especially for women by best gynecologists "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER207' productDesc="It is Mumbai's 1st Online Booking Facility for Blood Testing & ECG. It offers convenient and hassle free services."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER155' productDesc="It offers mini check-ups including blood count,uric acid,sugar level etc.It is well organized and gives accurate results."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER148' productDesc="It offers ayurveda treatments like massage therapies,facials and steam bath to provide mental as well as physical relief."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER212' productDesc="It specialises in monitoring the blood sugar levels,offering monthly tests.It provides best services possible."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER211' productDesc="It offers various services like monitoring BP,height,BMI,liver,kidney functioning and many more;providing accurate results."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER077' productDesc="It is an online heart care management program providing diet plans and counselling with monthly follow ups by experts."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER232' productDesc="It provides elderly care,post-hospitalisation services and palliative care along with a regular monitoring from home."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER244' productDesc="This card can offer quality medical services at economical rates.It also guides one about healthcare related issues. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER255' productDesc="It is 24/7 online healthcare service provider that offers diet plans and expert nutritional advice,at cost-effective rates."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER210' productDesc="It specialises in diabetes check-ups like managing Type-II and Pre-diabetic conditions offering best services possible."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER250' productDesc="It helps monitor and manage thyroid by it's convenient and speedy services,to give accurate results by best services possible."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SER259' productDesc="This health service provider has best doctors working towards promoting healthcare and wellness especially for old parents."/>
        
        

          </div>

        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

