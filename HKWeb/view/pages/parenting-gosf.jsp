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


        <div class="heading1">PARENTING</div>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB046-01' productDesc="A Breast Pump designed to mimic natural expression of the mothers breast. It ensures even milk flow with minimal effort."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB047-01' productDesc="This electric breast pump gently massages the breasts first. It then mimics natural expression for more milk at any given time"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB060-01' productDesc="Built with an anti-colic valve, It ensures the baby ingests less air while feeding from the bottle."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB130-01' productDesc="Preserve your baby's soft and supple skin and natural radiance with this herbal baby lotion"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB144-01' productDesc="Read your child's temperature without ever touching. Get Farlin Instant Infrared Ear Thermometer "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB1510-01' productDesc="Get a smile on your kid's face with this beautifully imaginative bag that comes with a lunch casement and a bottle case"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB171-01' productDesc="Your child has all the fun eating with Pigeon Bowl and Weaning Spoon set."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB1891-01' productDesc="Your baby's bottle is warmer for longer with Little's Feeder Cover"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB1976-01' productDesc="This electric breast pump gently massages the breasts first. It then mimics natural expression for more milk at any given time"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB1977-01' productDesc="This specially designed feeding bottle has no BPA. Ideal for warming and storing milk too."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB2117-01' productDesc="Pregnancy puts a lot of stress on your back and tummy. This maternity belt ensures both comfort and safety"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB2132-01' productDesc="Prevent your child from inserting fingers in the power socket. Protect with Farlin Safety Guard For Socket"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB219-01' productDesc="Mix the right amount of milk powder. Go for Farlin Milk Powder Container"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB256-01' productDesc="Comb your baby's hair without hurting them, by Farlin Comb and Brush Set White"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB270-03' productDesc="Moisturize your baby's soft and delicate skin with Farlin Baby Wet Wipes "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB293-01' productDesc="For fast, safe, and comfortable temperature readings of your kid, we recommend Farlin's Flexible Tip Digital Thermometer"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB344-01' productDesc="Baby has fun bathing with Gentle Bath Cleanser, it smells great too. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB390-01' productDesc="Add extra fun to your kid's bath with these fun filled squirters"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB435-01' productDesc="Clean every nook and crany of the milk bottle and nipple with this cleaner, "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB454-01' productDesc="Make feeding time more natural and enjoyable with this sipper shaped like a mother's teat"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB455-01' productDesc="Teach your child how to drink without spilling with this No Spill Flip It Sipper"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB456-01' productDesc="Make your toddler learn how to hold and drink slowly without spilling. Get this sipper accompanied with gripper cups"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB459-01' productDesc="sdfdsafdsaf"/>
        



        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

