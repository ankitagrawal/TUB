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


        <div class="heading1">PRE WORKOUT</div>

                 <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">Next →</a>


         </div>

        <div class="cl"></div>

        <div class="prodBoxes">
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT517'productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1366' productDesc='Rich in 100% pure anhydrous caffeien, am must have energy booster for your gym bag'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1363' productDesc='Enriched with pure L-Glutamine, it prevents from Ammonia in the brain and enhances immune function'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1364' productDesc='Increases nitric oxide levels for better muscle building pump vasodilation effect.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1135' productDesc='Ideal for overall body, stamina and strength growth, it helps improve protein synthesis and assists in muscle mass development.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1115' productDesc='Fine tune your metabolism and end gastro intestinal problems. Clinically proven to enhance vitality & productivity. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1394' productDesc='Boosts your energy and improves your stamina during your training sessions.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1394' productDesc='Boosts your energy and improves your stamina during your training sessions.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1397'productDesc='copy due'/>
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



         </div>

        <div class="cl"></div>

         <div class="pages">
             
            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">Next →</a>


         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

