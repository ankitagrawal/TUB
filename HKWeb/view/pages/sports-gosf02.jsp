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


        <div class="heading1"> SPORTS AND FITNESS</div>

        <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/sports-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/sports-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>

        <div class="cl"></div>

        <div class="prodBoxes">
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT958' productDesc="It offers a complete body workout especially biceps toning.It is made of quality materials with enough padding."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1321' productDesc="These unisex skates are ideal for a secure and fast ride.They offer great support and comfort."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1323' productDesc="They have 80mm wheels that helps one turn easily and its Lo-Balance Composite Frame offers great support."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1445' productDesc="It's flat,straight surface makes it ideal for golf. It's greens is contourable."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1454' productDesc="This 4-in-1 soccer kit offers high intensity training by building fast feet, leg drive and first-step quickness. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1519' productDesc="It can be worn on the wrist.It can record and manipulate one's personalized goals."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1528' productDesc="It is a light-weight device that keeps a tab of running speed and distance covered.It is waterproof and shock absorbent."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1723' productDesc="It is a rotation belt that helps build strength while leaping or changing directions.This cord stretches upto 20feet."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1718' productDesc="It is a durable,light-weight tool that adds intensity to jump training drill.It is ideal for foot speed training."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT472' productDesc="It is a fine quality TT bat that offers great grip and a high control of 88 with a fast speed of 90.It helps enhance one's performance."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT502' productDesc="It is a spacious yet stylish bag that is made from high quality materials.It has adjustable straps and is durable."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT513' productDesc="It is a flexible,light in weight racket with a strong grip.It has an appropriate string and frame density."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT910' productDesc="It is one of the best designed rackets made up of finest materails that offers maximum comfort along with a great grip."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT914' productDesc="It is an extremely light in weight racket with perfect dimension that offers maximum durabilty,power and balance."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT951' productDesc="It is a convenient way of exercising that enables one to perform around 21 exercises,within the comforts of home."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT960' productDesc="It is a specially designed, light-weight bench that is ideal for getting toned stomach muscles. "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT2297' productDesc='This program offers a 60 day workout session by best trainers along with training and nutrition DVDs. It helps reduce weight and maximize muscle activation.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT2259' productDesc='It offers a powerful strike and balanced hitting. This bat is made up of high quality materials that give a good pick-up.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1800' productDesc='It is a compact, stationary home-fitness system that promotes overall strength training with 100 possible exercises'/>	
		</div>

        <div class="cl"></div>


                 <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/sports-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/sports-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>



    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

