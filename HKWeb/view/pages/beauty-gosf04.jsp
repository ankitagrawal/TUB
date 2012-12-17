x<%@ page import="com.hk.constants.core.PermissionConstants" %>
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
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <span class="pages_link">4</span>


          </div>

        <div class="cl"></div>


         <div class="prodBoxes">
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SENRA4' productDesc="Cover cleavage effectively, an easy snap on for your bra. It is comfortable and washable."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='GATCOM2' productDesc='Gorgeous box of body, bath and shower delights, just for you.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TPIC01' productDesc="Natural Keratin fibers intertwine seamlessly for thicker and fuller hair, endures rain, wind & perspiration. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY138' productDesc="A potent combination of Peptide and Vitamin C to boost collagen production. It firms and regenerates skin"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BTY760' productDesc="Rose Geranium essential oil for ever-so-smooth skin"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O021' productDesc="Enriched with Vitamin B & E,nourishes, hydrates and rejuvenates skin."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='JSTHB23' productDesc="Night cream with natural ingredients, minimizes dark circles, stimulates blood circulation and nurtures skin effectively."/>
			<%-- <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PUCL3' productDesc="Long-lasting fusion of fruity and floral notes.Handbag-friendly size."/>--%>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VEDIC25' productDesc="Moisturizing gel with anti-oxidant nutrients, nourishes and maintains oil balance of the skin"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP09' productDesc="For faces seeking balance, nourishment and deep cleansing"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VNCP42' productDesc=" Rich skin nutrients that fades wrinkles, rejuvenates and provides volume and fullness to skin"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PROTV01' productDesc='3 Step System for a complete skincare solution. Helps prevent breakouts and promotes a healthier glowing skin.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O022' productDesc='A scrub infused with Bamboo and Sea Moss, for that bright and glowing skin'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ALVDA17' productDesc='Brimming with Grapeseed & Lavender, this oil is the definitive solution to your hairfall worries.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT81' productDesc='Solutions to shape, trim and tone body.  Reduces cellulite, firms skin and increases skin elasticity.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='ALVDA14' productDesc='Packed with the goodness of Polyphenois and Vitamin E, restores collagen and hydrates the skin. Your defence against acne.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VIVI05' productDesc='A lipstick with rich in purified waxes, it ensures perfect adherence, long lasting comfort, extra-shine & incredible smoothness.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT82' productDesc='For the star in your life, a selection of treatments to keep it stellar.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT80' productDesc='Diamonds are a girl’s best friend. Let it work its magic on your face this time.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT78' productDesc='Reduces hyper pigmentation, fades freckles, evens skin tone, improves skin clarity.'/>
			<%--<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='H2O006' productDesc='Gentle cleanser with Marine Botanicals and Sea Mineral Complex, cleans the skin and moisturizes with Provitamin B,.'/>--%>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KENT23' productDesc='13 rowed cushion paddle brush with ball tip quills for grooming, de-tangling and smoothing hair.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='KENT24' productDesc='9 rowed cushion paddle brush with ball tip quills. Best for combing your toddlers hair without hurting them.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT79' productDesc='For that beatific groomed look.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='VLCCFRT77' productDesc='A kit of soothing treats for hair and body.'/>			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NYAH4' productDesc='Refines pores, improves skin, reduces blemishes, evens tone and accelerates collagen production.'/>
        </div>
        <div class="cl"></div>
                                       
        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/beauty-gosf03.jsp">3</a>

            <span class="pages_link">4</span>


          </div>

    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

