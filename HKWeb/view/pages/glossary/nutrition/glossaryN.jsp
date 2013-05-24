<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = SslUtil.isSecure();
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
        <div class="heading">N</div>
        <div class="container">



            <div class="txt-container border">
                <h2>N-Acetyl Cysteine</h2>
                <p>It exerts powerful antioxidant effects. NAC scavenges the body for, and neutralizes, harmful free radicals that can cause oxidative damage to muscle tissue, bodily organs and the DNA.
                    As an antioxidant, it reduces oxidative stress. NAC also boosts immune system function by acting as a glutathione pre-cursor.</p>
            </div>


            <div class="txt-container border">
                <h2>Niacin</h2>
                <p>Also known as Vitamin B3, Niacin is needed for proper circulation and healthy skin. It also aids in maintaining  nervous system health, digestion of carbs, protein and fat and lowering cholesterol.
                     Niacin deficiency results in depression, fatigue, insomnia, loss of appetite and inflammation.</p>
            </div>


            <div class="txt-container">
                <h2>Nutrient</h2>
                <p>The beneficial elements of food.</p>
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

