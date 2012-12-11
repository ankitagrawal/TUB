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

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

         </div>
          <div class="cl"></div>


        <div class="prodBoxes">

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


         </div>

        <div class="cl"></div>

         <div class="pages">
             
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

