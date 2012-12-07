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


        <div class="heading1">BARS AND FAT BURNERS</div>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1361'productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT469' productDesc=' Rich in L-Carnitine, it burns your extra fat, reduces hunger pangs, improves metablosim, and reduces cholestrol.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1384' productDesc='Get enhanced energy for improved workouts and killer metabolic support for amazing results. The best rely on BSN Hypershred!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1256' productDesc='Promotes extensive muscle building and amplifies results'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1226' productDesc=' A supplement that boosts energy levels and fortifies your metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1225' productDesc='A perfect fat burner and energy booster, it contains essential compounds that break down existing fat.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1561' productDesc=' Contains powerful blend of Acetyl-L-Carnitine and CoQ10 which helps in fat loss and optimizes metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1513' productDesc='Stress relieving tea that re-vitalizes, and reduces weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT120' productDesc='A perfect product for fat loss, it targets stored body fat, controls appetite and accelerates metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1287' productDesc='Pre workout multi dimensional formula for mproved metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1600' productDesc='Suitable for people who want to lose weight, it helps burn fat, maintain stamina and improve metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1789' productDesc='An anabolic mineral support formula which delivers sound sleep to professional athletes, body builders and wrestlers. It promotes increased testosterone levels and complete muscle growth and recovery'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1151' productDesc='Boost your gym performance with MuscleTech Hydroxystim. Enhance your focus and blaze through your fitness goals. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Keep muchies at bay with Oh Yeah Bars! Get the goodness of healthy fats and low sugar in 1 go; in 5 amazing flavors. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT711' productDesc='A mix of rare nuts, seeds, raisins, Gooseberrys and Dark Chocolate. A healthy answer to your 4 O Clock hunger pangs.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT651' productDesc='Contains essential proteins that provide overal health and builds strong bones and muscles.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT651'productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT651' productDesc='Packed with essential proteins that strengthens muscles and bones and also keeps you healthy '/>





        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

