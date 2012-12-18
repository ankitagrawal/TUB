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
        <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-bars-gosf.jsp">← Previous</a>
        <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-bars-gosf.jsp">1</a>
        <span class="pages_link">2</span>

         </div>
           <div class="cl"></div>
        
        <div class="prodBoxes">        
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1561' productDesc='Contains powerful blend of Acetyl-L-Carnitine and CoQ10 which helps in fat loss and optimizes metabolism.'/>       
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1287' productDesc='Pre-workout multi dimensional formula for improved metabolism.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1226' productDesc='A supplement that boosts energy levels and fortifies your metabolism.'/>			
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1151' productDesc='Boost your gym performance with MuscleTech Hydroxystim. Enhance your focus and blaze through your fitness goals. '/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT640' productDesc='A high-quality protein blend that is free of hydrogenated fats and aspartame'/>        
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT716' productDesc='Keep muchies at bay with Oh Yeah Bars! Get the goodness of healthy fats and low sugar in 1 go; in 5 amazing flavors. '/>	        		        
	<!--         <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT711' productDesc='A mix of rare nuts, seeds, raisins, Gooseberrys and Dark Chocolate. A healthy answer to your 4 O Clock hunger pangs.'/> -->        
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT651' productDesc='Contains essential proteins that provide overal health and builds strong bones and muscles.'/>	        
	        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT651' productDesc='Packed with essential proteins that strengthens muscles and bones and also keeps you healthy '/>
        </div>


        <div class="cl"></div>
        

        <div class="pages">
        <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-bars-gosf.jsp">← Previous</a>
        <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-bars-gosf.jsp">1</a>
        <span class="pages_link">2</span>

         </div>



    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

