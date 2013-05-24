<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-glossary.jsp"
                 pageTitle="HealthKart Glossary">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/glossary.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">HealthKart Glossary</span>

		<h1 class="title">HealthKart Glossary</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">HealthKart Glossary</s:layout-component>
<s:layout-component name="metaKeywords">HealthKart Glossary</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

    <%@include file="/includes/_menuGlossary.jsp" %>





    <div class="main-container">
        <div class="heading">M</div>
        <div class="container">



            <div class="txt-container border">
                <h2>Magnesium</h2>
                <p>A mineral necessary in energy production, it regulates blood pressure, plays a role in bone formation,
                    and aids in carbohydrate and mineral metabolism. Deficiency of this mineral result in poor digestion, rapid heartbeat, high blood pressure, and chronic fatigue.</p>
            </div>



            <div class="txt-container border">
                <h2>Magnesium Creatine Chelate</h2>
                <p>A patented form of creatine bound to magnesium, it is clinically shown to boost strength significantly after a use of just 2 weeks.</p>
            </div>


            <div class="txt-container border">
                <h2>Manganese</h2>
                <p>A mineral necessary for protein and fat metabolism, healthy immune system, energy production and blood sugar regulation.
                    It is also used in the formation of cartilage and synovial fluid of joints. Its deficiency can lead to muscle contractions, tremors, memory loss and rapid pulse.</p>
            </div>


            <div class="txt-container border">
                <h2>Methionine</h2>
                <p>An Essential amino acid, it assists in the breakdown of fats, helps diminish muscle weakness, and is a good source of sulfur which drives away of free radicals.
                    Involved in methyl-group metabolism, the synthesis of carnitine, creatine and catecholamines, it also serves as a substrate for protein synthesis.</p>
            </div>


            <div class="txt-container border">
                <h2>Methylxanthines</h2>
                <p>A group of alkaloids that are commonly used for their effects as stimulants, these derivatives include caffeine, theophylline and theobromine.</p>
            </div>


            <div class="txt-container border">
                <h2>Micellar Protein</h2>
                <p>A “slow” protein, or sustained-released protein derived from milk, micellar casein is digested slowly
                    and provides a steady stream of amino acids into the body. Many bodybuilders use micellar casein
                    prior to bedtime for this reason. Unlike a “fast” protein such as whey which is better at triggering
                    protein synthesis, micellar casein is better at preventing muscle catabolism.</p>
            </div>


            <div class="txt-container border">
                <h2>MSM</h2>
                <p>A natural form of organic sulfur found in all living organisms, MSM is a natural compound that
                    provides the chemical links needed to form and maintain connective tissue. It provides the chemical
                    links needed to create collagen, which forms and holds the molecular structure of connective tissue.
                    It also forms the disulfide bonds in connective tissue that are the links in the chains that form
                    cartilage. MSM also aids in relieving pain and inflammation.</p>
            </div>


            <div class="txt-container border">
                <h2>Mycozyme</h2>
                <p>A digestive enzyme that breaks down complex carbohydrates and starches.</p>
            </div>


            <div class="txt-container border">
                <h2>Micronutrients</h2>
                <p>Nutritional elements like vitamins and mineral that are needed only in small amounts. </p>
            </div>


            <div class="txt-container">
                <h2>Multivitamin</h2>
                <p>A foundational supplement designed with a specific formulation of vitamins, minerals and herbs used to supplement daily diet and support hard training. See Animal Pak.</p>
            </div>






        </div>
    </div>



    <div class="footer01">© 2013 healthviva.com All Rights Reserved</div>







<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

<s:layout-component name="menu">
</s:layout-component>

</s:layout-render>

