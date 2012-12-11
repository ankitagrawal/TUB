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
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC01' productDesc="Pure extracts of Saffron, Winter cherry & Turmeric diminish age spots, uneven pigmentation and freckles for an even skin tone. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC03' productDesc="Enriched with Brahmi, Neelibrigandi and Seaweed extract, stimulates hair growth, nourishes cuticles and prevents hair loss."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC07' productDesc="Before- bed cream with Saffron and Kumkumadi to refine, sooth and hydrate skin.   "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC08' productDesc="Turmeric blended with Sandal and Jojoba, it removes age spots, hyper pigmentation, uneven tone & sun discoloration. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC15' productDesc="Saffron and Sandal essence work together to keep skin hydrated, refreshed and free from spots.  "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ARVDC27' productDesc="Protects and prevents further hair damage, gives life to damaged hair"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='AYUCR1' productDesc="Get rid of unwanted fat and regain body shape with enjoyable massages. Ayu Care Lavana Tailam makes fat loss enjoyable!"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BRUT03' productDesc="This eau de toilette for men has a warm and spicy heart and woody base notes"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY111' productDesc="Light restorative cream to banish dark circles  "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY120' productDesc="Fight blemished skin with an arsenal of LHA and Bio-Assimilated Ceramide "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY138' productDesc="A potent combination of Peptide and Vitamin C to boost collagen production. It firms and regenerates skin"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY679' productDesc="Natural Beewax and Olive oil for delectably soft lips."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY723' productDesc="Effective formula controls dandruff and maintains a healthy scalp to provide a complete hair care solution."/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY760' productDesc="Rose Geranium essential oil for ever-so-smooth skin"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY764' productDesc="Ground Walnut and Apricot exfoliates & rehydrates skin. Soothing Almond, Rosemary and Lemon oil detoxifies & rejuvenates dull skin.   "/>

                

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

