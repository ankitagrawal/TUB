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
        <div class="heading">F</div>
        <div class="container">






            <div class="txt-container border">
                <h2>Fast Protein</h2>
                <p>Fast proteins are digested very quickly and are superior to slow proteins when it comes to protein synthesis.
                    All forms of Whey Proteins are considered to be fast proteins when compared with other protein sources like Casein or Egg Protein.</p>
            </div>


            <div class="txt-container border">
                <h2>Fat</h2>
                <p>An important micronutrient. A gram of fat typically yields 9 calories.</p>
            </div>


            <div class="txt-container border">
                <h2>Flaxseed Oil</h2>

                <p>Rich in omega-3 essential fatty acids, several studies show that it can reduce pain, inflammation,
                    swelling and joints flexibility. It has also been found to lower cholesterol and triglyceride
                    levels. Flax seed is also one of the best sources for the essential fatty acid alpha linolenic.
                    Alpha linolenic acid is important for bodybuilders because it is known to enhance insulin
                    sensitivity within muscle cells.</p>
            </div>


            <div class="txt-container border">
                <h2>Folic Acid</h2>
                <p>Necessary for energy production, it strengthens the immune system and stimulates protein metabolism. Deficiency of the same results in digestive problems, fatigue, memory problems and weakness.</p>
            </div>


            <div class="txt-container">
                <h2>4-hydroxyisoleucine</h2>
                <p>An amino acid extracted from fenugreek seeds, this ingredient stimulates insulin secretion and helps control blood sugar levels.
                    It acts best when taken directly along with carbohydrates and creatine. </p>
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

