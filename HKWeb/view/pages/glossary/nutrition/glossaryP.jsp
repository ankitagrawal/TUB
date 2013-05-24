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
        <div class="heading">P</div>
        <div class="container">



            <div class="txt-container border">
            <h2>Pancreatin</h2>
            <p>A proteolytic digestive enzyme used as an aid in digesting protein. It also stimulates protein synthesis and tissue repair.</p>
        </div>


            <div class="txt-container border">
                <h2>Pantothenic Acid</h2>
                <p>Also known as Vitamin B5, it is often referred to as stress vitamin. It plays an important role in the production of adrenal hormones, aids in converting protein,
                    carbs and fats to energy, and is involved in the production of neurotransmitters. Its deficiency can lead to fatigue and nausea.</p>
            </div>


            <div class="txt-container border">
                <h2>Papain</h2>
                <p>A proteolytic digestive enzyme derived from papaya fruit, it is used as an aid in digesting protein. It also helps to stimulate protein synthesis and repair.</p>
            </div>


            <div class="txt-container border">
                <h2>Pepsin</h2>
                <p>An enzyme produced by the walls of the stomach, it breaks down proteins during digestion. It requires a strongly acidic environment such as that present in the stomach.
                    It breaks down large protein molecules into smaller protein molecules (smaller polypeptides) and is therefore a protease – an enzyme that breaks down a protein.</p>
            </div>


            <div class="txt-container border">
                <h2>Phosphorous</h2>
                <p>A mineral needed for bone-tooth health and cell growth, it assists the body in the utilization of food for energy and vitamin absorption.
                    Phosphorus deficiency can lead to bone pain, fatigue, weakness and weight changes.</p>
            </div>


            <div class="txt-container border">
                <h2>Potassium</h2>
                <p>A mineral that maintains heart rhythm, proper muscle contraction, and aids in the transport of nutrients across the cell membrane.
                    Signs of deficiency include glucose intolerance, growth impairment, muscular fatigue and weakness, and insomnia.</p>
            </div>


            <div class="txt-container border">
                <h2>Phenylalanine</h2>
                <p>An essential amino acid that crosses the blood brain barrier and has a direct effect on brain chemistry.
                    It boosts memory and learning, elevates mood while decreasing appetite and pain.</p>
            </div>


            <div class="txt-container border">
                <h2>Phosphatidylcholine</h2>
                <p>Necessary for the body's natural production of acetycholine, it provides the essential spark to nerve endings throughout the body,
                    conducting nervous impulses and ensuring that the brain receives body's messages.</p>
            </div>


            <div class="txt-container border">
                <h2>Proline</h2>
                <p>A non-essential amino acid, it aids in the production of collagen,
                    helps in the healing of cartilage, strengthens  joints, tendons and heart muscle. It works with Vitamin C to promote healthy connective tissue.</p>
            </div>


            <div class="txt-container border">
                <h2>Protein</h2>
                <p>An important macronutrient composed of amino acids linked together. A gram of protein yields 4 calories.</p>
            </div>


            <div class="txt-container border">
                <h2>Protogen A</h2>
                <p>Also known as alpha lipoic acid, it is such a powerful antioxidant that some researchers have dubbed it as the "universal antioxidant." ALA has shown that
                    it works in conjunction with vitamins C and E to help improve their effectiveness in combating free radicals.</p>
            </div>



            <div class="txt-container">
                <h2>Pyridoxine</h2>
                <p>Also known as Vitamin B6, it is involved in more bodily functions than any other nutrient.
                    It affects both physical and mental health and is necessary for the absorption of amino acids and fats. Its deficiency can result in acne, hair loss and stunted growth.</p>
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

