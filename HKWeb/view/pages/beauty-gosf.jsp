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


        <div class="heading1">BEAUTY</div>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT311' productDesc="pratham is awesome"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AYUCR1' productDesc="Get rid of unwanted fat and regain body shape with enjoyable massages. Ayu Care Lavana Tailam makes fat loss enjoyable!"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC4' productDesc="Dissolves when in contact with warm skin, Lavender oil decreases melanin & clears stubborn hyper pigmentation spots and acne marks. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ORIY18' productDesc="Essence of Saw Palmetto, Pumpkin Seed Oil, Stinging Nettle and Ginseng extract, prevents pattern baldness and diffused hair loss."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC01' productDesc="Pure extracts of Saffron, Winter cherry & Turmeric diminish age spots, uneven pigmentation and freckles for an even skin tone. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY723' productDesc="Effective formula controls dandruff and maintains a healthy scalp to provide a complete hair care solution."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI10' productDesc="Exfoliating soap that makes skin soft and smooth. The ground Apricot and Mulberry keeps dry skin at bay. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC64' productDesc="Makeup and skincare in one, it easily blends, protects skin from UV rays and leaves an undetectable finish."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VAADI36' productDesc="Prevents premature ageing, wrinkling of skin, relaxes muscles, makes skin smooth and gives deeper sleep."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC21' productDesc="Antiseptic and gentle wash to diminish pigmentation, acne, open pores & oily skin. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ORIY19' productDesc="Essence of Grape Seed, Resveratrol, Pine Bark, Saffron & Green Tea, protects and lightens skin."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT22' productDesc="Incredibly soothing body lotion for dry &troubled skin"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC46' productDesc="Goodness of Bhringraj, Glycerin and Soy oil, conditions and hydrates dead locks. Absolute nourishment for chemically treated hair."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY679' productDesc="Natural Beewax and Olive oil for delectably soft lips."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GAT24' productDesc="No crunch, strong hold, non sticky wax for that hair statement"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OLY018' productDesc="Moisturize before bed and get firm skin and fight 7 signs of aging."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP18' productDesc="Exfoliating scrub with a mystical blend of Licorice, Neem & Tulsi "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY111' productDesc="Light restorative cream to banish dark circles  "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC07' productDesc="Before- bed cream with Saffron and Kumkumadi to refine, sooth and hydrate skin.   "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O007' productDesc="Oil free gel offers deep hydration to minimize fine lines and replenish dry skin. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY120' productDesc="Fight blemished skin with an arsenal of LHA and Bio-Assimilated Ceramide "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC2' productDesc="Lush moisturizer to rehydrate and sooth the thirstiest of skins"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='STVKC04' productDesc="Effective herbal treatment for acne-prone skin"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DIVO12' productDesc="Perfect hairbrush cleaning accessory, ensures no fluff is left behind on your paddle brush"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DENMAN9' productDesc="Large, barrel shaped, anti-static, heat retaining rollers for quicker, long-lasting curls"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC38' productDesc="Enriched with Lactic Acid, get rid of tanned skin, enhance fairness and achieve even skin tone"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PLMCL06' productDesc="Infused with Tea Tree, Lavender, Peppermint & Chamomile. Revitalizes, adds vitality, deep cleans and softens tresses."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VICHY4' productDesc="Effective weapon to fight against UV rays. Non sticky, ultra-protective emulsion with micro-sized filtering particles."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SHNZ06' productDesc="Extracts of Vetiver and Dates with Diamond and Tankana, purifies skin and improves cell regeneration"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY769' productDesc="Comfrey and Echinanea extracts, adds luster, cleanses, strengthens and builds hair volume. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY764' productDesc="Ground Walnut and Apricot exfoliates & rehydrates skin. Soothing Almond, Rosemary and Lemon oil detoxifies & rejuvenates dull skin.   "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FABIN47' productDesc="Goodness of Papaya and Fuller’s Earth, deep cleanses and detoxifies the skin, leaving it firm and radiant."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI07' productDesc="Caviar extracts for skin so soft, smooth and radiant, it glows from within."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC12' productDesc="Saffron extracts evens the skin color, hydrates, offers suppleness and protects against UV rays."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BRUT03' productDesc="This eau de toilette for men has a warm and spicy heart and woody base notes"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP31' productDesc="Infused with Sweet Almond and Avocado oil,  relieves stress, improves blood circulation and reduces muscle pains."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GLD12' productDesc="Triple Blades with pivoting head, glides smoothly over skin. Aloe lubricating strip with Vitamin E reduces irritation and hydrates."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O009' productDesc="Infused with herbal Mint essence, Wakame and Sea Fennel hydrates. Prevents chapping, feathering and smoothens fine lines"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O012' productDesc="Enriched with Watercress, Iceland moss and Pro-vitamin B, it conditions the skin, improves elasticity, hydrates & lightly scents."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC08' productDesc="Turmeric blended with Sandal and Jojoba, it removes age spots, hyper pigmentation, uneven tone & sun discoloration. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FESTGLAM1' productDesc="Super glossy and super fruity, It gives you a wet look gloss, hydrates lips and stays on for long."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI02' productDesc="Highly potent whitening agents to lighten dark patches and complexion without irritation.. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LYD1' productDesc="Goodness of Bhringraj, Amalaki, Neem & Gunja, this controls hair fall, removes dandruff, revitalizes and darkens hair."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LOREAL39' productDesc="Non-greasy, non-sticky moisturizer that is quickly absorbed, sooths, recharges and hydrates skin after shaving"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SENRA4' productDesc="Cover cleavage effectively, an easy snap on for your bra. It is comfortable and washable."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC15' productDesc="Saffron and Sandal essence work together to keep skin hydrated, refreshed and free from spots.  "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KHADI37' productDesc="Exfoliating and moisturizing scrub with rose essence, sloughs dry skin cells "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI01' productDesc="Silky emulsion lightens spots, scars & fine lines and moisturizes skin. Can be used all over the body"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY138' productDesc="A potent combination of Peptide and Vitamin C to boost collagen production. It firms and regenerates skin"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY760' productDesc="Rose Geranium essential oil for ever-so-smooth skin"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC27' productDesc="Protects and prevents further hair damage, gives life to damaged hair"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP09' productDesc="For faces seeking balance, nourishment and deep cleansing"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC25' productDesc="Moisturizing gel with anti-oxidant nutrients, nourishes and maintains oil balance of the skin"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PUCL3' productDesc="Long-lasting fusion of fruity and floral notes.Handbag-friendly size."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TPIC01' productDesc="Natural Keratin fibers intertwine seamlessly for thicker and fuller hair, endures rain, wind & perspiration. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP42' productDesc=" Rich skin nutrients that fades wrinkles, rejuvenates and provides volume and fullness to skin"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OLY019' productDesc="Anti-ageing cream that minimizes wrinkles & eye bags,moisturizes delicate under eye skin and diminishes spots"/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O021' productDesc="Enriched with Vitamin B & E,nourishes, hydrates and rejuvenates skin."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='JSTHB23' productDesc="Night cream with natural ingredients, minimizes dark circles, stimulates blood circulation and nurtures skin effectively."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC03' productDesc="Enriched with Brahmi, Neelibrigandi and Seaweed extract, stimulates hair growth, nourishes cuticles and prevents hair loss."/>
        


        <div class="cl"></div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

