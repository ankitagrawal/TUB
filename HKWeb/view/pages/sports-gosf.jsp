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

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/sports-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/sports-gosf02.jsp">Next →</a>

         </div>

        <div class="cl"></div>

        <div class="prodBoxes">

        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT026' productDesc="It is a light weight device that lets one exercise even while travelling. It improves the overall strength of the muscles. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT059' productDesc="It is a high quality mat that is non-slippery and has a soft finish.It helps perform effective exercises for best results."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT069' productDesc="It proves to be beneficial for all kinds of physical injuries be it back,hip or knee. It increases strength and muscle balance."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1321' productDesc="These unisex skates are ideal for a secure and fast ride.They offer great support and comfort."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1323' productDesc="They have 80mm wheels that helps one turn easily and its Lo-Balance Composite Frame offers great support."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1430' productDesc="This pull-up bar helps build an impressive upper body musculature.It makes exercising even more worthwhile. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1432' productDesc="it has 6resistance cables that makes exercisinga worthwhile experience.It adds comfort and vitality. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1445' productDesc="It's flat,straight surface makes it ideal for golf. It's greens is contourable."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1454' productDesc="This 4-in-1 soccer kit offers high intensity training by building fast feet, leg drive and first-step quickness. "/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1519' productDesc="It can be worn on the wrist.It can record and manipulate one's personalized goals."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1521' productDesc="It measures heartbeats,calories burnt and has the facility of reminders."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1522' productDesc="It has an alarm that measures heartbeat,calories burnt in accordance to weight,height and age."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1523' productDesc="It tabs average and maximum heart rate,calories burnt and offers target-based zone with bpm reading."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1524' productDesc="It monitors the heartbeat and calories burnt in accordance to an age-based target zone.It also has an in-built alarm."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1528' productDesc="It is a light-weight device that keeps a tab of running speed and distance covered.It is waterproof and shock absorbent."/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='SPT1563' productDesc=" It is an organized way of storing the dumbbells.It can lift and lower them at an optimal height as per one's convenience."/>


            </div>

        <div class="cl"></div>


                <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/sports-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/sports-gosf02.jsp">Next →</a>

         </div>



    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

