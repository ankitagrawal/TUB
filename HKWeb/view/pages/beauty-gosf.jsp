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


        <div class="heading1">BEAUTY</div>

         <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">Next →</a>

          </div>

        <div class="cl"></div>


         <div class="prodBoxes">
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY586' productDesc='Ayurvedic formula helps break down fat, strengthens the skin dermis and tones the tummy in 3-6 weeks.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AYUCR1' productDesc="Get rid of unwanted fat and regain body shape with enjoyable massages. Ayu Care Lavana Tailam makes fat loss enjoyable!"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLC001' productDesc='A non greasy oil infused with aromatic herbs. It fights cellulite build-up,  moisturized and firms up the skin'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC4' productDesc="Dissolves when in contact with warm skin, Lavender oil decreases melanin & clears stubborn hyper pigmentation spots and acne marks. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ORIY18' productDesc="Essence of Saw Palmetto, Pumpkin Seed Oil, Stinging Nettle and Ginseng extract, prevents pattern baldness and diffused hair loss."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BASCAR61' productDesc='Soothes exhausted eyes, calms senses and treats sinuses & aches.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BASCAR42' productDesc='Perfect travelling companion kit to ensure you sleep well even in flights and waiting lounges'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GLD16' productDesc='Your intimates needs a razor with a small head for a close and tender shave. Infused with Aloe and tea tree oil'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY560' productDesc='Reduces the effect of UV rays on skin, moisturizes and softens the skin, gives you that radiant look'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC01' productDesc="Pure extracts of Saffron, Winter cherry & Turmeric diminish age spots, uneven pigmentation and freckles for an even skin tone. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY723' productDesc="Effective formula controls dandruff and maintains a healthy scalp to provide a complete hair care solution."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI10' productDesc="Exfoliating soap that makes skin soft and smooth. The ground Apricot and Mulberry keeps dry skin at bay. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VAADI36' productDesc="Prevents premature ageing, wrinkling of skin, relaxes muscles, makes skin smooth and gives deeper sleep."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT22' productDesc="Incredibly soothing body lotion for dry &troubled skin"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ORIY19' productDesc="Essence of Grape Seed, Resveratrol, Pine Bark, Saffron & Green Tea, protects and lightens skin."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC21' productDesc="Antiseptic and gentle wash to diminish pigmentation, acne, open pores & oily skin. "/>
			<!-- <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GAT24' productDesc="No crunch, strong hold, non sticky wax for that hair statement"/> -->
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC46' productDesc="Goodness of Bhringraj, Glycerin and Soy oil, conditions and hydrates dead locks. Absolute nourishment for chemically treated hair."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ORIY16' productDesc='Fortified with amino acids that nourishes hair, improves its strength and reduces hairfall'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY679' productDesc="Natural Beewax and Olive oil for delectably soft lips."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MNFCR23' productDesc="Give yourself the gift of soft, smooth radiant skin with this Spa set!"/>
			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARMTR18' productDesc="An invigorating experience crafted to suit all skin types, this skin-whitening facial kit leaves you absolutely fabulous."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARMTR4' productDesc="Bid good bye to acne, zits, soreness and inflammation caused after waxing or threading. Multipurpose non-sticky gel for all skin types."/>
         </div>
        <div class="cl"></div>
        
         <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">Next →</a>

          </div>

    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

