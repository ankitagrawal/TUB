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
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">Next →</a>

          </div>

        <div class="cl"></div>


         <div class="prodBoxes">
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY769' productDesc="Comfrey and Echinanea extracts, adds luster, cleanses, strengthens and builds hair volume. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FABIN47' productDesc="Goodness of Papaya and Fuller’s Earth, deep cleanses and detoxifies the skin, leaving it firm and radiant."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GLD12' productDesc="Triple Blades with pivoting head, glides smoothly over skin. Aloe lubricating strip with Vitamin E reduces irritation and hydrates."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC27' productDesc="Protects and prevents further hair damage, gives life to damaged hair"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BRUT03' productDesc="This eau de toilette for men has a warm and spicy heart and woody base notes"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC12' productDesc="Saffron extracts evens the skin color, hydrates, offers suppleness and protects against UV rays."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC08' productDesc="Turmeric blended with Sandal and Jojoba, it removes age spots, hyper pigmentation, uneven tone & sun discoloration. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FESTGLAM1' productDesc="Super glossy and super fruity, It gives you a wet look gloss, hydrates lips and stays on for long."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP31' productDesc="Infused with Sweet Almond and Avocado oil,  relieves stress, improves blood circulation and reduces muscle pains."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC03' productDesc="Enriched with Brahmi, Neelibrigandi and Seaweed extract, stimulates hair growth, nourishes cuticles and prevents hair loss."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O009' productDesc="Infused with herbal Mint essence, Wakame and Sea Fennel hydrates. Prevents chapping, feathering and smoothens fine lines"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LOREAL38' productDesc="Finally, an after shave blam that protects from drying effects and soothes your skin!"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LYD1' productDesc="Goodness of Bhringraj, Amalaki, Neem & Gunja, this controls hair fall, removes dandruff, revitalizes and darkens hair."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LOREAL34' productDesc="A Shaving Mousse for men with sensitive skin. No more irritation - just comfy and smooth shave!"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI01' productDesc="Silky emulsion lightens spots, scars & fine lines and moisturizes skin. Can be used all over the body"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI02' productDesc="Highly potent whitening agents to lighten dark patches and complexion without irritation.. "/>         
			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DLICMB04' productDesc="2-in1 set for those intense and smouldering eyes. Smudge-proof and stays for up to 6 hours."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MYBLN38' productDesc="Unique flexible wand lifts, separates and provides extra 300% fuller lashes. Contact lens safe and Ophthalmologically tested."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MYBLN37' productDesc="Must have in any girls vanity kit.  Hydrates lips for up to 8 hours."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='STVKC09' productDesc="Pamper your skin with the epitome in premium luxury. Boosts, protects, and re-balances anti-oxidants."/>

			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC10' productDesc="Rose oil, Saffron & Natural micro particles of Rosehip seeds nourishes, polishes and removes impurities gently."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BBW7' productDesc="Seductive floral scents for a fragrant lather. Enriched with Shea Butter, Vitamin E and Aloe Vera for softer, cleaner skin."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BWCH26' productDesc="No need to wrestle anymore to fit into a dress. Hide your flabs and flaunt your curves, get Bwitch Shapewear."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARMTR17' productDesc="Relaxing and reviving, nutrient-rich package to cleanse and beautify all skins types."/>
				
		</div>
        <div class="cl"></div>
        
         <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">Next →</a>

          </div>

    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

