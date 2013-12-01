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


        <div class="heading1">DIETARY SUPPLEMENTS</div>

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <span class="pages_link">2</span>



            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf09.jsp">9</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">Next →</a>


         </div>
        <div class="cl"></div>

        <div class="prodBoxes">
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1369' productDesc='Build strength and endurance with MuscleBlaze Creatine. It consistently delivers a powerful workout performance.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT893' productDesc='Ideal for fitness enthusiasts working for lean muscle gain, it is a rapid absorption formula that builds strength and stamina.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT335' productDesc='A product with a potent combination of essential vitamins and minerals. Improves the strength, immunity and energy levels of the body.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT717' productDesc='A complete supplement, it supports the whole body and enhances energy levels. Meant for men with an active lifestyle.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT318' productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT420' productDesc='Custom made for serious body builders, build and maintain your muscles with confidence'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT906' productDesc='Want better focus, higher endurance and power when you work out? Just pick MuscleTech Neurocore and youll never look back.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1135' productDesc='Ideal for overall body, stamina and strength growth, it helps improve protein synthesis and assists in muscle mass development.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT320' productDesc='Replenish your energy after intense workouts and fuel muscle building with the goodness of Nitric Oxide!'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT693' productDesc='Get high focus and skin bursting pumps with the worlds most coveted pre workout formulation'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT984' productDesc='Energy booster for inter-workout sessions that burns fat, helps in tissue recovery'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT592' productDesc='Concentrated Creatine Monohydrate, a pre-workout formula that ensures higher energy output, pump vascularity and enchanced focus'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1270' productDesc='BCAAs help in muscle tissue recovery, enhanced absorption and increased lean muscle mass and strength'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1468' productDesc='Packed with Glutamine for enhanced muscular recovery & preventing tissue loss or damage.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1363' productDesc='Enriched with pure L-Glutamine, it prevents from Ammonia in the brain and enhances immune function'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1278' productDesc='Containing HICA (alpha-hydroxy-isocaproic-acid), this aids muscle growth, helps in rapid recovery from workouts and reduces DOMS'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1115' productDesc='Fine tune your metabolism and end gastro intestinal problems. Clinically proven to enhance vitality & productivity. '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT957' productDesc='It helps burn down the body fat to get a leaner body and also suppresses ones appetite.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1175' productDesc='Easy to swallow soft gels enriched with Tonalin CLA. It helps reduce body fat and increases muscle tone.  '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT641' productDesc='Gain muscle mass and improve muscle stamina. Can be consumed by those with food allergies.'/>									
        </div>

        <div class="cl"></div>

        

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <span class="pages_link">2</span>



            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">8</a>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf09.jsp">9</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">Next →</a>


         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

