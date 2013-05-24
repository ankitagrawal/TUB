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
        <div class="heading">I</div>
        <div class="container">



            <div class="txt-container border">
                <h2>Inosine</h2>
                <p>An intermediate in a number of purine nucleotide pathways that affect the ability of the muscle to work efficiently.
                    It is a precursor to adenosine, an important energy molecule, and plays an important role in energy production. </p>
            </div>



            <div class="txt-container border">
                <h2>Insulin</h2>

                <p>An exceptionally anabolic hormone used to regulate carb and fat metabolism.</p>
            </div>


            <div class="txt-container border">
                <h2>Iodine</h2>
                <p>A trace mineral, it helps to metabolize excess fat and entails healthy thyroid function. Its deficiency leads to hypothyroidism, fatigue and weight gain.</p>
            </div>


            <div class="txt-container">
                <h2>Isoleucine</h2>
                <p>An essential amino acid and one of the three BCAA’s, it regulates blood sugar, energy levels and aids in the healing and repair of muscle tissue.</p>
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

