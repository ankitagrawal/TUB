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
        <div class="heading">A</div>
        <div class="container">



            <div class="txt-container border">
                <h2>Adenosine Triphosphate (ATP)</h2>
                <p>Energy molecule of the cell responsible for muscular contraction.</p>
            </div>


            <div class="txt-container border">
                <h2>Alanine</h2>
                <p>A non-essential amino acid, it helps transfer nitrogen in the body. It also aids metabolism of glucose and guards against the build-up of toxic substances released when muscle protein is broken down.</p>
            </div>

            <div class="txt-container border">
                <h2>Amino Acids</h2>
                <p>They are the building blocks of protein.</p>
            </div>

            <div class="txt-container border">
                <h2>Anabolic</h2>
                <p>A process that tends toward building up muscles through growth and cell differentiation. It involves synthesis of complex molecules through metabolic pathways that construct them from smaller units.</p>
            </div>

            <div class="txt-container border">
                <h2>Androgenic</h2>
                <p>Pertains to male sex hormones and masculine characteristics.</p>
            </div>

            <div class="txt-container border">
                <h2>Arginine Alpha-Ketoglutarate</h2>
                <p>Formed by combining two molecules of Arginine from amino acid and one molecule of alpha-ketoglutarate. Because AKG is involved in amino acid synthesis and protein availability, many athletes consume AKG to increase muscle mass and strength. It aids in dilating blood vessels, reducing blood pressure, and replicating the activity of nitroglycerine which is needed to produce nitric oxide.</p>
            </div>

            <div class="txt-container">
                <h2>Aspartic Acid</h2>
                <p>A non-essential amino acid, it helps in removing toxins from the bloodstream. It also aids in the production of immunoglobulins and antibodies which protect the immune system.</p>
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

