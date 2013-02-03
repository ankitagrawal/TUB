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
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">8</a>

            <span class="pages_link">9</span>

         </div>
         <div class="cl"></div>


        <div class="prodBoxes">
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1300' productDesc='Fight skin conditions including eczema, reduce inflammation, boosts immunity and regulate hormones.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT885' productDesc='A robust heart and vascular health and fight stress and aging.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1776' productDesc='An ayurvedic tonic that eradicates sexual weakness in both men and women.It boosts sexual libido and improves stamina.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1472' productDesc='Increase your sex drive and libido along with your spermcount, what more can you ask for?'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT947' productDesc='Infused with olive oil and natural antioxidants. It boosts the immunity of the body and keep ones heart healthy and strong.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1166' productDesc='Specially formulated with probiotics and green foods that helps promote healthy heart, bones and stronger immunity.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1282' productDesc='Fortified with vitamins and minerals that support metabolism and nutrient breakdown for an improved athletic performance and recovery.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1283' productDesc='It contains multivitamins that promote improved health in women. Also boosts energy and exercise recovery.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1314' productDesc='A fat burner that helps burn calories and supports increased metabolism before and after exercise.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT658' productDesc='Packed with essential vitamins and minerals that promote a healthy heart, immune system, eye, bone and nervous system.'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1252' productDesc='These Vegetarian capsules promote a healthy brain, heart and eyes.'/>	        
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1253' productDesc='These vegetarian capsules help promote a balanced cholesterol and triglyceride level and have virtually no side effects.'/>			
        </div>
        <div class="cl"></div>

         <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">8</a>

            <span class="pages_link">9</span>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>