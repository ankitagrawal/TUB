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


        <div class="heading1">PERSONAL CARE</div>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PW006' productDesc="Test your pregnancy in just 2 minutes with this Instant Ovulation Kit"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP002' productDesc="Protect yourself with this powerful pepper formula spray."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL002' productDesc="Figure out the best two days you will be most fertile. Grab this I-Sure Ovulation Strip to get pregnant for sure. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP001' productDesc="Protect yourself with this powerful pepper formula spray."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KNOCK001' productDesc="Protect yourself with this powerful pepper formula spray."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NIC001' productDesc="Nicorette reins in your constant urge to smoke. Quit smoking for sure."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOS001' productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH006' productDesc="Get a  cleaner tongue with this carefully tested cleaner blister"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH101' productDesc="dfdasfdsf"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL004' productDesc="keep your hands germ free with this anti-bacterial hand sanitizer "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP091' productDesc="Switch to Roots Hair Builder for thick and shiny hair instantly"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP004' productDesc="This personal safety alert device warns you when someone tries to tamper with things that belong to you"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOS001' productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='COP002' productDesc="afdsadf"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OPT001' productDesc="Ideal for oral and underarm use"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PPS009' productDesc="Keep your panties dry during summers and when you have vaginal discharge. Try these hygienic panty liners."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH044' productDesc="This handy and foldable toothbrush with diamond rounded filaments cleans even the space between your teeth"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS020' productDesc="Sensational pleasure for your partner with long lasting dotted condoms "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOR002' productDesc="Infused with carbamide peroxide, it cleans your teeth and makes them more white and shiny."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH078' productDesc="Refreshes your room with fragrance of sandal and jasmine kill all bad odors in your living room"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TP001' productDesc="Made of cotton and rayon, Perfect for those with high mentrual flow and hate changing napkins constantly."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH067' productDesc="Cleans your teeth and gums, reduces chances of plaque and maintains your oral health."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH033' productDesc="Ensure the best possible protection for your teeth with this sonic toothbrush "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH063' productDesc="Ensure your kid's dental health with easier and deep cleansing vibrations. Only with Trisa Sonic Junior"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP070' productDesc="For a thorough and gentle cleansing of your skin, use Bel Premium Cotton Balls, with Aloe Vera and Provitamin B5"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS050' productDesc="Climax for men makes sure the partner is left screaming"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AT002' productDesc="Designed with high absorbent mechanism, it is easy,comfortable and hygienic to wear."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH068' productDesc="Maintains your oral hygiene and makes your smile more beautiful."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH069' productDesc="Keeps your teeth clean and shiny."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PA004' productDesc="An ideal product for women who face incontinence during and after pregnancy and patients who have been through vaginal surgery"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP091' productDesc="Switch to Roots Hair Builder for thick and shiny hair instantly"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH092' productDesc="Wipe out all the plaque problems with this 2-in-1 toothbrush, that cleans and massages your teeth and gums"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LIF003' productDesc="Easy to use refill pack for your hand wash dispenser"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOS001' productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH052' productDesc="A fun toothbrush for you little one, Trisa Junior Toothbrush"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PF049' productDesc="Gel heel shields that provide comfort and pain relief for those who constantly have to wear high heels"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MOB002' productDesc="dfkjsjdfk"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH047' productDesc="For a gentle and efficient cleansing of teeth, get this Trisa toothbrush with flexible heads that absorb excess pressure"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH054' productDesc="due"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PF044' productDesc="For maximum foot protection, use this Neat Feat Diabetic Self Moulding Sole "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT001' productDesc="Neat Feat 3B Action Cream is a perfect pick for curing sweat rash and chafing "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT003' productDesc="Rehydrate and repair the damaged and dried heels of your feet with this foot and heel balm "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT010' productDesc="Give comfort and softness to your tired and worn out feet with Neat Feat Foot Soap Scrub "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH037' productDesc="dsfsaddf"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH047' productDesc="For a gentle and efficient cleansing of teeth, get this Trisa toothbrush with flexible heads that absorb excess pressure"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AT003' productDesc="Attends Slip Regular, with an in-built odour system checks unpleasant smell,ensures comfort, hygiene, and security"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='JS003' productDesc="Brush better with JSB Electronic Toothbrush,"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MIDAS005' productDesc="Refresh your home and office with the natural and rich fragrance of Lovin Air Freshener "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH094' productDesc="Care for your gums and teeth and keep your dental health intact with this plaque cleaner "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH095' productDesc="afdsafdf"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT001' productDesc="Neat Feat 3B Action Cream is a perfect pick for curing sweat rash and chafing"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT005' productDesc="This foot and leg moisturizer, with a rich moisturizing formula for dry skin and cracked heels"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NEAT006' productDesc="Neat Feat roll on Deodoriser aids in preventing foot odour"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OH097' productDesc="With soft bristles for effective cleaning of plaque and bacteria"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP091' productDesc="Switch to Roots Hair Builder for thick and shiny hair instantly"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AIR005' productDesc="Freshen up your room with this air freshener that comes in great fragrances"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE005' productDesc="Disk Roller Body Massager is the answer to your muscle pain and extra body fat"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE001' productDesc="This face and body massager delivers minute acoustic vibrations, curing muscle and joint pain "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE002' productDesc="FaceMate's Dual Wave Ultrasonic Face Massager revitalizes your skin using safe ultrasonic technology "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE003' productDesc="Relax your muscle and energize your body with this multi-purpose warm massager"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DE004' productDesc="Get a wrinkle free, healthy and  a firmer skin and no side effects with this muscle stimulator "/>



        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

