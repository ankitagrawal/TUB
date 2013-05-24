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
        <div class="heading">L</div>
        <div class="container">



            <div class="txt-container border">
                <h2>L-Carnosine</h2>
                <p>A small peptide that contains two amino acids, beta-alanine and histidine. It is found in relatively
                    high concentrations in several body tissues, most notably in skeletal muscles, heart muscles and the
                    brain. Studies indicate that it possesses strong and specific antioxidant properties, protects
                    against radiation damage, improves heart function and promotes recovery from injuries. It inhibits
                    the damaging and pro-aging effects of carbohydrate consumption and has been shown to increase muscle
                    strength and endurance.</p>
            </div>



            <div class="txt-container border">
                <h2>Leucine</h2>

                <p>An essential amino acid and one of the three BCAA’s, it promotes healing of bone, skin, muscle tissue
                    and aids in increasing growth hormone production. Leucine affects various anabolic hormones, has
                    tremendous anti-catabolic effects, and is necessary for muscle protein synthesis.</p>
            </div>


            <div class="txt-container border">
                <h2>L-Norvaline</h2>
                <p>An analog of the branched chain amino acid valine, It inhibits the arginase enzyme, thus increasing arginine concentrations for enhanced conversion to nitric oxide.</p>
            </div>


            <div class="txt-container">
                <h2>Lysine</h2>
                <p>An essential amino acid, it is a necessary building block of protein.
                    It maintains proper nitrogen balance, aids in the production of antibodies, hormones, enzymes and tissue repair.</p>
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

