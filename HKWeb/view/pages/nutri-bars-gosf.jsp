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


        <div class="heading1">BARS AND FAT BURNERS</div>

        <div class="pages">
        <span class="pages_link">1</span>
        <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-bars-gosf02.jsp">2</a>
        <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-bars-gosf02.jsp">Next →</a>
         </div>
        
         <div class="cl"></div>

        <div class="prodBoxes">
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1361'productDesc='copy due'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT469' productDesc=' Rich in L-Carnitine, it burns your extra fat, reduces hunger pangs, improves metablosim, and reduces cholestrol.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1384' productDesc='Get enhanced energy for improved workouts and killer metabolic support for amazing results. The best rely on BSN Hypershred!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1256' productDesc='Promotes extensive muscle building and amplifies results'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1226' productDesc=' A supplement that boosts energy levels and fortifies your metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1225' productDesc='A perfect fat burner and energy booster, it contains essential compounds that break down existing fat.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1561' productDesc=' Contains powerful blend of Acetyl-L-Carnitine and CoQ10 which helps in fat loss and optimizes metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1513' productDesc='Stress relieving tea that re-vitalizes, and reduces weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT120' productDesc='A perfect product for fat loss, it targets stored body fat, controls appetite and accelerates metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1287' productDesc='Pre workout multi dimensional formula for mproved metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1600' productDesc='Suitable for people who want to lose weight, it helps burn fat, maintain stamina and improve metabolism.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1173' productDesc='With blend of  essential amino acids and branched chain amino acids, it acts as a meal supplement which helps in loosing weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1789' productDesc='An anabolic mineral support formula which delivers sound sleep to professional athletes, body builders and wrestlers. It promotes increased testosterone levels and complete muscle growth and recovery'/>
        

        </div>


        <div class="cl"></div>
        

        <div class="pages">
        <span class="pages_link">1</span>
        <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-bars-gosf02.jsp">2</a>
        <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-bars-gosf02.jsp">Next →</a>
         </div>



    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

