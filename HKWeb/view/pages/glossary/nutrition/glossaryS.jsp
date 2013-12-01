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
        <div class="heading">S</div>
        <div class="container">






            <div class="txt-container border">
                <h2>Selenium</h2>
                <p>A vital antioxidant, it protects the immune system and regulates the effects of thyroid hormone on fat metabolism. Deficiency symptoms of this mineral include exhaustion, growth impairment and sterility.</p>
            </div>


            <div class="txt-container border">
                <h2>Serine</h2>
                <p>A non-essential amino acid, it is needed for proper metabolism of fats and fatty acids. It is imperative for the growth of muscle and maintenance of a healthy immune system.
                    Widely used for synthesizing various proteins in the body, it is an integral component of the phospholipid- phosphatidyl serine.</p>
            </div>


            <div class="txt-container border">
                <h2>Shark Cartilage</h2>
                <p>A natural source of chondroitin sulfate and glucosamine, it is one of the most powerful anti-inflammatory agents and wound healing substances.</p>
            </div>


            <div class="txt-container border">
                <h2>Simple Carbs</h2>
                <p>Carbohydrates that are directly absorbed into the bloodstream and have an immediate affect on blood sugar. They can be incredibly anabolic if taken immediately post-workout.</p>
            </div>


            <div class="txt-container border">
                <h2>Slow Protein</h2>
                <p>Typically refers to micellar casein, slow proteins are digested more slowly and are superior to fast proteins when it comes to anti-catabolism. See "Micellar Casein".</p>
            </div>


            <div class="txt-container border">
                <h2>Smilax officinalis</h2>
                <p>This well-known herb contains plant sterols which appear to stimulate the body's metabolic processes. It increases energy, regulates hormones and acts as a diuretic.</p>
            </div>



            <div class="txt-container">
                <h2>Soy Protein Isolates</h2>
                <p>An ideal protein source for vegetarians, soy protein isolates are complete proteins that rank with the best in the Protein Digestibility Corrected Amino Acid Score (PDCAAS).
                    Soy protein isolates are typically free of fat, cholesterol and lactose. They’re rich in arginine, aspartic acid, glycine and histidine.</p>
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

