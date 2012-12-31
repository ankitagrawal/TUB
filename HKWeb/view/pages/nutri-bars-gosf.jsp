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
		<div class="pages">               
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-bars-gosf02.jsp">2</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/nutri-bars-gosf02.jsp">Next →</a>
         </div>

        <div class="heading1">BARS AND FAT BURNERS</div>

        

        <div class="prodBoxes">        
<!-- 	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT469' productDesc='Rich in L-Carnitine, it burns your extra fat, reduces hunger pangs, improves metablosim, and reduces cholestrol.'/> -->			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1600' productDesc='Suitable for people who want to lose weight, it helps burn fat, maintain stamina and improve metabolism.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1151' productDesc='Boost your gym performance with MuscleTech Hydroxystim. Enhance your focus and blaze through your fitness goals. '/>			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT890' productDesc='An advanced and safe supplement for fat loss. Enhance  energy and keep your appetite in check.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1384' productDesc='Powerful fatburner that helps manage weight, improve your physical performance and get a toned body.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1341' productDesc='An adipokine stimulator that promotes fat mobilization and helps in hunger control. '/>			
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT2019' productDesc='An advanced and safe supplement for fat loss. Enhance  energy and keep your sugar in check.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1151' productDesc='An energy booster that stimulates ones mental focus,help lose excess flab to get a well-toned body.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT910' productDesc='This fatburner is a lipotropic and metabolic complex that includes essential nutrients and helps one lose weight naturally'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1538' productDesc='It is a pre-workout supplement that provides a boost in energy, keeps the body hydrated and improves workout performance'/>									
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT120' productDesc='A perfect product for fat loss, it targets stored body fat, controls appetite and accelerates metabolism.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1225' productDesc='A perfect fat burner and energy booster, it contains essential compounds that break down existing fat.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1256' productDesc='Promotes extensive muscle building and amplifies results'/>
<!-- 			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1361' productDesc='Want to lose fat, get lean, boost energy and reduce water? Pick APIs Polythermex Hardcore and astound the world. '/> -->
 			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT651' productDesc='Contains essential proteins that provide overal health and builds strong bones and muscles.'/>
<!-- 			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT711' productDesc='A mix of rare nuts, seeds, raisins, Gooseberrys and Dark Chocolate. A healthy answer to your 4 O Clock hunger pangs.'/> -->
        </div>


        <div class="cl"></div>
        
		<div class="heading1">BARS AND FAT BURNERS</div>

         <div class="pages">               
	        <span class="pages_link">1</span>
			<a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-bars-gosf02.jsp">2</a>
			<a class="next"  href="${pageContext.request.contextPath}/pages/nutri-bars-gosf.jsp"> Next→</a>
         </div>
		
        



    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

