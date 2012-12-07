<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
<style type="text/css">


.navigation {
    margin: 20px 0;
    height: 25px;
    width: 960px;
    background-color: #333;
}

#nav, #nav ul {
    font-size: 14px;
    margin: 0;
    padding: 0;
    list-style-type: none;
    list-style-position: outside;
    position: relative;
    line-height: 1.5em;

}

#nav a {
    display: block;
    padding: 0px 5px;
    border: 1px solid #333;
    color: #fff;
    text-decoration: none;
    background-color: #333;
}

#nav a:hover {
    background-color: #fff;
    color: #333;
}

#nav li {
    margin-left: 20px;
    float: left;
    position: relative;
}

#nav ul {
    position: absolute;
    display: none;
    width: 12em;
    top: 1.5em;
}

#nav li ul a {
    margin-left: -20px;
    width: 12em;
    height: auto;
    float: left;
}

#nav ul ul {
    top: auto;
}

#nav li ul ul {
    left: 12em;
    margin: 0px 0 0 10px;
}

#nav li:hover ul ul, #nav li:hover ul ul ul, #nav li:hover ul ul ul ul {
    display: none;
}

#nav li:hover ul, #nav li li:hover ul, #nav li li li:hover ul, #nav li li li li:hover ul {
    display: block;
}

a, a:hover {
    border-bottom: none;
}

.cl {
    clear: both;
}

p, h3, h1, h2, h4 {
    margin: 0;
    padding: 0;
}

@font-face
{
    font-family: 'robotoregular'
;
    src: url('${pageContext.request.contextPath}/css/roboto-regular-webfont.eot')
;
    src: url('${pageContext.request.contextPath}/css/roboto-regular-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.svg#robotoregular') format('svg')
;
    font-weight: normal
;
    font-style: normal
;

}

@font-face
{
    font-family: 'roboto_ltregular'
;
    src: url('${pageContext.request.contextPath}/css/roboto-light-webfont.eot')
;
    src: url('${pageContext.request.contextPath}/css/roboto-light-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.svg#roboto_ltregular') format('svg')
;
    font-weight: normal
;
    font-style: normal
;

}

#pageContainer {
    margin: 15px auto;
    width: 960px;
    overflow: auto;
    padding-bottom: 260px;
    font-family: 'robotoregular';
    color: #231f20;
}

.product {
    width: 215px;
    border-bottom: none;
    text-align: left;
    float: left;
    margin: 20px 15px 0 10px;
    position: relative;
    height: 520px;
}

.product  .gosf-logo {
    position: absolute;
    top: 177px;
    right: 0px;
}

.product .img {
    border: 1px solid #CCC;
    width: 213px;
    height: 256px;
}

.product h3, .product h2, .product .bid {
    font-size: 13px;
    font-weight: normal;
    color: #333;
    padding: 0;
}

/*.product h3 { height:23px; }*/

.product .description {
    font-size: 12px;
    line-height: normal;
    font-weight: normal;
    color: #999;
    margin-bottom: 20px;
    padding: 0;
}

.product h3 a {
    font-size: 13px;
    color: #333;
    text-decoration: none;
}

.product h3 a:hover {
    font-size: 13px;
    color: #333;

}

.product .price {
    text-align: left;
    margin: 5px 0;
    font-size: 18px;
    color: #333;
    line-height: normal;
    font-family: 'roboto_ltregular';
}

.product .price span {
    font-size: 15px;
    color: #999;
    text-decoration: line-through;
    font-style: normal;
}

.product a.buynow {
    color: #000;
    display: inline-block;
    font-size: 18px;
    padding: 5px 15px;
    border: 1px #000 solid;
    text-decoration: none;
    margin-bottom: 10px;
}

.product .buynow a:hover {
    color: #000;
    text-decoration: none;
}

.product:hover {
    border-bottom: none;
}

.heading1 {
    border-bottom: 1px solid #c0c0c0;
    width: 960px;
    height: 40px;
    line-height: 40px;
    margin: 40px 0 10px 0;
    text-align: center;
    font-weight: bold;
    color: #666;
    font-size: 20px;
}

.product .img180 img {
    max-height: 180px;
    max-width: 180px;
}

.see-more {
    font-size: 13px;
    font-weight: normal;
    color: #999;
    margin-top: 20px;
    text-decoration: none;
}

.see-more a {
    color: #999;
    text-decoration: none;
}

.see-more a:hover {
    color: #000;
}


</style>


<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.2.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/menu-gosf.js"></script>


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
        <img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg"/>

        <div class="cl"></div>

        <jsp:include page="../includes/_menuGosf.jsp"/>

        <div class="cl"></div>


        <div class="heading1">SERVICES</div>
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

        



        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

