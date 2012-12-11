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
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <span class="pages_link">2</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">Next →</a>

          </div>

        <div class="cl"></div>


         <div class="prodBoxes">


<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DENMAN9' productDesc="Large, barrel shaped, anti-static, heat retaining rollers for quicker, long-lasting curls"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='DIVO12' productDesc="Perfect hairbrush cleaning accessory, ensures no fluff is left behind on your paddle brush"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FABIN47' productDesc="Goodness of Papaya and Fuller’s Earth, deep cleanses and detoxifies the skin, leaving it firm and radiant."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='FESTGLAM1' productDesc="Super glossy and super fruity, It gives you a wet look gloss, hydrates lips and stays on for long."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GAT24' productDesc="No crunch, strong hold, non sticky wax for that hair statement"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GLD12' productDesc="Triple Blades with pivoting head, glides smoothly over skin. Aloe lubricating strip with Vitamin E reduces irritation and hydrates."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O007' productDesc="Oil free gel offers deep hydration to minimize fine lines and replenish dry skin. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O009' productDesc="Infused with herbal Mint essence, Wakame and Sea Fennel hydrates. Prevents chapping, feathering and smoothens fine lines"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O012' productDesc="Enriched with Watercress, Iceland moss and Pro-vitamin B, it conditions the skin, improves elasticity, hydrates & lightly scents."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O021' productDesc="Enriched with Vitamin B & E,nourishes, hydrates and rejuvenates skin."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='JSTHB23' productDesc="Night cream with natural ingredients, minimizes dark circles, stimulates blood circulation and nurtures skin effectively."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KHADI37' productDesc="Exfoliating and moisturizing scrub with rose essence, sloughs dry skin cells "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LOREAL39' productDesc="Non-greasy, non-sticky moisturizer that is quickly absorbed, sooths, recharges and hydrates skin after shaving"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='LYD1' productDesc="Goodness of Bhringraj, Amalaki, Neem & Gunja, this controls hair fall, removes dandruff, revitalizes and darkens hair."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI01' productDesc="Silky emulsion lightens spots, scars & fine lines and moisturizes skin. Can be used all over the body"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='MKRI02' productDesc="Highly potent whitening agents to lighten dark patches and complexion without irritation.. "/>

                

         </div>
        <div class="cl"></div>

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <span class="pages_link">2</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf04.jsp">4</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">Next →</a>

          </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

