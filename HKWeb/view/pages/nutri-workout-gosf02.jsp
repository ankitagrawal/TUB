<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
    
<link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css" />


</s:layout-component>

<s:layout-component name="breadcrumbs">
    <div class='crumb_outer'>
        <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
        &gt;
        <span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

        <h1 class="title">
            Cyber Monday Great Online Shopping Festival
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

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">1</a>

            <span class="pages_link">2</span>



            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">Next →</a>


         </div>
          <div class="cl"></div>


        <div class="prodBoxes">


        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT639' productDesc='This product is premium whey that delivers a powerful blend of high-quality protein and a combination of Branched Chain Amino Acids that bodybuilders and athletes would look for'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1369' productDesc='Build strength and endurance with MuscleBlaze Creatine. It consistently delivers a powerful workout performance.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT420' productDesc='Custom made for serious body builders, build and maintain your muscles with confidence'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT893' productDesc='Ideal for fitness enthusiasts working for lean muscle gain, it is a rapid absorption formula that builds strength and stamina.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1468' productDesc='Packed with Glutamine for enhanced muscular recovery & preventing tissue loss or damage.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT890' productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT699' productDesc='Pre-workout energy amplifier and muscle toner. Enhances training results and activates muscle growth.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT699' productDesc='Increase strength by 18.6% with this pre workout formulation. Get rapid muscle pump and intense workouts. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT906' productDesc='Want better focus, higher endurance and power when you work out? Just pick MuscleTech Neurocore and you will never look back.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT906' productDesc='Packed with beta-alanine, L-citrulline, Creatine HCI and Geranium Rovertianum. Excellent strength supplement for strength training'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT318' productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT318' productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT318' productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT320' productDesc='Replenish your energy after intense workouts and fuel muscle building with the goodness of Nitric Oxide!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT320' productDesc='Enriched with essential amino acids, it acts as an energy booster and helps in muscle repair and recovery.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT335' productDesc='A product with a potent combination of essential vitamins and minerals. Improves the strength, immunity and energy levels of the body.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT507' productDesc='Produced from natural ingredients, this is a naturally wheat free and gluten free product. This is easy to prepare, 100% vegetable product.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1340' productDesc='High in protein and carbs, this helps in fast recovery and used as a low calorie snack'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1341' productDesc='An adipokine Stimulator that promotes fat mobilization and control hunger.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1348' productDesc='A metabolic stimulator has a blend of Siberian ginseng, yerba mate, guarana, green tea and caffeine which provide extra energy for your long workouts. '/>

        

         </div>

        <div class="cl"></div>

         <div class="pages">
             
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">1</a>

            <span class="pages_link">2</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">Next →</a>


         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

