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


        <div class="heading1"> SPORTS AND FITNESS</div>
                <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT069'productDesc="It proves to be beneficial for all kinds of physical injuries be it back,hip or knee. It increases strength and muscle balance."/>
                <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1430' productDesc="This pull-up bar helps build an impressive upper body musculature.It makes exercising even more worthwhile. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT069' productDesc="It proves to be beneficial for all kinds of physical injuries be it back,hip or knee. It increases strength and muscle balance."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT954' productDesc="It is a light-weight exercising bike that helps reduce weight.It has an LCD screen showing calories burnt and pulse rate."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT059' productDesc="It is a high quality mat that is non-slippery and has a soft finish.It helps perform effective exercises for best results."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT952' productDesc="This stationary exercising machine has an LCD Display showing speed and calories burnt.It helps one reduce weight."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT026' productDesc="It is a light weight device that lets one exercise even while travelling. It improves the overall strength of the muscles. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT959' productDesc="It helps strengthen the back,tone side abs and torso.It is ideal for toning the belly and is easily portable."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1523' productDesc="It tabs average and maximum heart rate,calories burnt and offers target-based zone with bpm reading."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1432' productDesc="it has 6resistance cables that makes exercisinga worthwhile experience.It adds comfort and vitality. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1702' productDesc="It is a 6-pack bag that can carry food items and drinks,while travelling.It has separate compartments for different items."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1524' productDesc="It monitors the heartbeat and calories burnt in accordance to an age-based target zone.It also has an in-built alarm."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT422' productDesc="It is a professional 15-rung ladder with a non-slippery surface that helps one practice foot work.It is firm and durable."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT957' productDesc="This exercising bench comes with right kind of elevation and adjustbale height that promotes an overall body workout."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1521' productDesc="It measures heartbeats,calories burnt and has the facility of reminders."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1522' productDesc="It has an alarm that measures heartbeat,calories burnt in accordance to weight,height and age."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1563' productDesc=" It is an organized way of storing the dumbbells.It can lift and lower them at an optimal height as per one's convenience."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1718' productDesc="It is a durable,light-weight tool that adds intensity to jump training drill.It is ideal for foot speed training."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1725' productDesc="It has adjustable bands that leads to 40 pounds of resistance while jumping.This strengthens the lower body muscles."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT421' productDesc="It is an adjustable trainer that improves lateral strength.It helps tone the hip,thigh and calf muscles."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT423' productDesc="It is a speed training device with adjustable belts that helps increase the sprint speed;providing strength,power and stronger muscles."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT424' productDesc="It an be attached to the waist and pulled like a sled,for stronger muscles.It can also be used for back and leg training."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT493' productDesc="This fine quality racket offers great grip and support.It is convenient and ideal for an improved performance."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT958' productDesc="It offers a complete body workout especially biceps toning.It is made of quality materials with enough padding."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1321' productDesc="These unisex skates are ideal for a secure and fast ride.They offer great support and comfort."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1323' productDesc="They have 80mm wheels that helps one turn easily and its Lo-Balance Composite Frame offers great support."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1519' productDesc="It can be worn on the wrist.It can record and manipulate one's personalized goals."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1323' productDesc="They have 80mm wheels that helps one turn easily and its Lo-Balance Composite Frame offers great support."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1445' productDesc="It's flat,straight surface makes it ideal for golf. It's greens is contourable."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1454' productDesc="This 4-in-1 soccer kit offers high intensity training by building fast feet, leg drive and first-step quickness. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1521' productDesc="It measures heartbeats,calories burnt and has the facility of reminders."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1528' productDesc="It is a light-weight device that keeps a tab of running speed and distance covered.It is waterproof and shock absorbent."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1723' productDesc="It is a rotation belt that helps build strength while leaping or changing directions.This cord stretches upto 20feet."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT472' productDesc="It is a fine quality TT bat that offers great grip and a high control of 88 with a fast speed of 90.It helps enhance one's performance."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT502' productDesc="It is a spacious yet stylish bag that is made from high quality materials.It has adjustable straps and is durable."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT513' productDesc="It is a flexible,light in weight racket with a strong grip.It has an appropriate string and frame density."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT908' productDesc="It is made of fine quality materials that makes it strong and light-weight.It is durable and offers stability."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT910' productDesc="It is one of the best designed rackets made up of finest materails that offers maximum comfort along with a great grip."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT914' productDesc="It is an extremely light in weight racket with perfect dimension that offers maximum durabilty,power and balance."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT951' productDesc="It is a convenient way of exercising that enables one to perform around 21 exercises,within the comforts of home."/>
                <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT960' productDesc="It is a specially designed, light-weight bench that is ideal for getting toned stomach muscles. "/>


        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

