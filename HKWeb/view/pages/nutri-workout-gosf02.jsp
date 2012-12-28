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

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">← Previous</a>

             <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">1</a>

            <span class="pages_link">2</span>



            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">3</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-workout-gosf03.jsp">Next →</a>


         </div>
          <div class="cl"></div>


        <div class="prodBoxes">
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1272' productDesc='Contains glutamine that helps in increasing growth hormone release and helps in enhancing muscle metabolism'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1366' productDesc='Rich in 100% pure anhydrous caffeine, this is a must have energy booster for your gym bag'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1798' productDesc='Get amplified focus and energy with this formulation of pure creatine. '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT507' productDesc='Produced from natural ingredients, this is a naturally wheat free and gluten free product. This is easy to prepare, 100% vegetable product.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT377' productDesc='Boosts your energy, strengthens and stimulates blood circulation.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1207' productDesc='A pre-workout formula, it provides a quick pump through bioactive peptide fraction and herbal extracts.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1750' productDesc='Its creatine that separates the boys and the men. Pick Creapure to change your workouts forever.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT591' productDesc='Enriched with vitmain B9, it acts as an energy booster and improves your stamina before your workout sessions.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1289' productDesc='It provides quick energy for an improved performance. It contains carbohydrates for faster post-workout recovery.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT799' productDesc='The goodness of vegetable nitrate extracts in it bumps up your endurance. The all natural pro biotic mix keeps the stomach healthy. Did we mention its all natural?'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT699' productDesc='Pre-workout energy amplifier and muscle toner. Enhances training results and activates muscle growth.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1341' productDesc='An adipokine Stimulator that promotes fat mobilization and control hunger.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1364' productDesc='Increases nitric oxide levels for better muscle building pumps,along with vasodilation.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1394' productDesc='Boosts your energy and improves your stamina during your training sessions.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1348' productDesc='A metabolic stimulator has a blend of Siberian ginseng, yerba mate, guarana, green tea and caffeine which provide extra energy for your long workouts.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT798' productDesc='The pre-workout supplement that boosts your energy during your long exercise sessions.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1397' productDesc='Benefit from the goodness of recently discovered SAAs. Let science do the heavy lifting for you, while you gain mass!'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1342' productDesc='Contains natural ingredients that boost Testosterone, helps in fat loss and gaining muscle size and strength'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1391' productDesc='This herbal tea, free from Caffeine eases tension and stress and is beneficial for the nervous system'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1342' productDesc='Contains natural ingredients that boost Testosterone, helps in fat loss and gaining muscle size and strength'/>			
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

