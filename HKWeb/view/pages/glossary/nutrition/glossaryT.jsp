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
        <div class="heading">T</div>
        <div class="container">






            <div class="txt-container border">
                <h2>Taurine</h2>
                <p>A conditionally essential amino acid,  it’s not incorporated into muscle proteins. Taurine is the second most abundant free-amino acid in the muscle after glutamine.
                    It elicits cell volumization through its effect on osmoregulation, thereby influencing anabolic processes.</p>
            </div>


            <div class="txt-container border">
                <h2>Testosterone</h2>
                <p>The principal male hormone and the most basic anabolic steroid.</p>
            </div>


            <div class="txt-container border">
                <h2>Thiamin</h2>
                <p>Also known as Vitamin B1, it enhances circulation, carbohydrate metabolism and digestion. It plays a crucial role in brain function and has positive effects on energy, appetite and growth.
                    Deficiency of the same results in loss of appetite, muscle atrophy, poor circulation and general weakness.</p>
            </div>


            <div class="txt-container border">
                <h2>Threonine</h2>
                <p>An essential amino acid, it helps in maintaining proper protein balance in the body. It enhances the immune system by aiding in the production of antibodies.</p>
            </div>


            <div class="txt-container border">
                <h2>Tricreatine Malate</h2>
                <p>It is a compound made from creatine monohydrate and malic acid.
                    The substance is so named because the compound is made from three creatine molecules attached to one molecule of malic acid.</p>
            </div>


            <div class="txt-container border">
                <h2>Trytophan</h2>
                <p>An essential amino acid, it is  used by the brain to produce serotonin and melatonin which are responsible for normal sleep.
                    It is also important in enhancing the release of growth hormone.</p>
            </div>



            <div class="txt-container border">
                <h2>Turmeric</h2>
                <p>Extracted from the dried root of the plant curcuma longa, its active ingredient is curcumin.
                    It helps to fight free radicals, protects the liver against toxins, aids circulation and has strong anti-inflammatory properties.</p>
            </div>



            <div class="txt-container">
                <h2>Tyrosine</h2>
                <p>A non-essential amino acid, it is needed for the production of various neurotransmitters which regulate mood, stimulate metabolism and the nervous system.
                    It is also important in the formation of active thyroid hormones.</p>
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

