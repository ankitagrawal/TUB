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


        <div class="heading1">PRE WORKOUT</div>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT517'productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1366' productDesc='Rich in 100% pure anhydrous caffeien, am must have energy booster for your gym bag'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1363' productDesc='Enriched with pure L-Glutamine, it prevents from Ammonia in the brain and enhances immune function'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1364' productDesc='Increases nitric oxide levels for better muscle building pump vasodilation effect.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1135' productDesc='Ideal for overall body, stamina and strength growth, it helps improve protein synthesis and assists in muscle mass development.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1115' productDesc='Fine tune your metabolism and end gastro intestinal problems. Clinically proven to enhance vitality & productivity. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1394' productDesc='Boosts your energy and improves your stamina during your training sessions.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1394' productDesc='Boosts your energy and improves your stamina during your training sessions.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1397'productDesc='copy due'/>

        <%--<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1382' productDesc='A pre-workout supplement, BSN HyperFX helps in enhancing energy, focus and stamina for extended workouts.'/>
          <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT799' productDesc='The goodness of vegetable nitrate extracts in it bumps up your endurance. The all natural pro biotic mix keeps the stomach healthy. Did we mention it's all natural?'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT798' productDesc='The pre-workout supplement that boosts your energy during your long exercise sessions.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT693' productDesc='A pre-workout supplement that combats muscle tenderness and fatigue and also builds your stamina.'/>--%>


        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1207' productDesc='A pre-workout formula, it provides pump results through bioactive peptide fraction and herbal extracts.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1289' productDesc='It provides quick energy for an improved performance. It contains carbohydrates for faster post-workout recovery.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1289' productDesc='Provides instant energy and nutrition during your workouts and fights muscle fatique'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT591' productDesc='Enriched with vitmain B9, it acts as an energy booster and improves your stamina before your workout sessions.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT592' productDesc='Concentrated Creatine Monohydrate, a pre-workout formula that ensures higher energy output, pump vascularity and enchanced focus'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT592' productDesc='Its a pre-workout supplement that provides excessive energy and strength to the body.It is ideal for both men and women.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT957' productDesc='It helps burn down the body fat to get a leaner body and also suppresses ones appetite. It is a unisex product.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1175' productDesc='Easy to swallow soft gels enriched with Tonalin CLA. It helps reduce body fat and increases muscle tone.  '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1278' productDesc='Containing HICA (alpha-hydroxy-isocaproic-acid), this aids muscle growth, helps in rapid recovery from workouts and reduces DOMS'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT377' productDesc='Boosts your energy, strengthens and stimulates blood circulation.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT641' productDesc='Gain muscle mass and improve muscle stamina. Can be consumed by those with food allergies'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT639' productDesc='This product is premium whey that delivers a powerful blend of high-quality protein and a combination of Branched Chain Amino Acids that bodybuilders and athletes would look for'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1369' productDesc='Build strength and endurance with MuscleBlaze Creatine. It consistently delivers a powerful workout performance.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT420' productDesc='Custom made for serious body builders, build and maintain your muscles with confidence'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT893' productDesc='Ideal for fitness enthusiasts working for lean muscle gain, it is a rapid absorption formula that builds strength and stamina.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1468' productDesc='Packed with Glutamine for enhanced muscular recovery & preventing tissue loss or damage.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT890' productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT699' productDesc='Pre-workout energy amplifier and muscle toner. Enhances training results and activates muscle growth.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT699' productDesc='Increase strength by 18.6% with this pre workout formulation. Get rapid muscle pump and intense workouts. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT906' productDesc='Want better focus, higher endurance and power when you work out? Just pick MuscleTech Neurocore and you will never look back.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT906' productDesc='Packed with beta-alanine, L-citrulline, Creatine HCI and Geranium Rovertianum. Excellent strength supplement for strength training'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT318' productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT318' productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT318' productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT320' productDesc='Replenish your energy after intense workouts and fuel muscle building with the goodness of Nitric Oxide!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT320' productDesc='Enriched with essential amino acids, it acts as an energy booster and helps in muscle repair and recovery.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT335' productDesc='A product with a potent combination of essential vitamins and minerals. Improves the strength, immunity and energy levels of the body.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT507' productDesc='Produced from natural ingredients, this is a naturally wheat free and gluten free product. This is easy to prepare, 100% vegetable product.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1340' productDesc='High in protein and carbs, this helps in fast recovery and used as a low calorie snack'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1341' productDesc='An adipokine Stimulator that promotes fat mobilization and control hunger.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1348' productDesc='A metabolic stimulator has a blend of Siberian ginseng, yerba mate, guarana, green tea and caffeine which provide extra energy for your long workouts. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1342' productDesc='Contains natural ingredients that boost Testosterone, helps in fat loss and gaining muscle size and strength'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1750' productDesc='Its creatine that separates the boys and the men. Pick Creapure to change your workouts forever.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT984' productDesc='Energy booster for inter-workout sessions that burns fat, helps in tissue recovery'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT984' productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT984' productDesc='Energizes your body, pumps your muscles and cut downs the extra fat'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT984' productDesc='A powerful mid-workout drink enriched with BCAAs in an ideal ratio. It is a carb and sugar free drink to energize the body, pump the muscles and cut down the extra fat'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT984' productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT984' productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT717' productDesc='A complete supplement, it supports the whole body and enhances energy levels. Meant for men with an active lifestyle.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1391' productDesc='This herbal tea, free from Caffeine eases tension and stress and is beneficial for the nervous system'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1270' productDesc='BCAAs help in muscle tissue recovery, enhanced absorption and increased lean muscle mass and strength'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1272' productDesc='Contains glutamine that helps in increasing growth hormone release and helps in enhancing muscle metabolism '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1798' productDesc='Get amplified focus and energy with this formulation of pure creatine. '/>




        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

