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
        <div class="heading">B</div>
        <div class="container">



            <div class="txt-container border">
                <h2>Basal Metabolic Rate (BMR)</h2>
                <p>BMR is the energy expended by the body at rest. It depends on factors such as weight, height, age, gender, genetics etc.</p>
            </div>


            <div class="txt-container border">
                <h2>Betaine HCL</h2>
                <p>A non-essential nutrient and a source of hydrochloric acid, it is a naturally occurring chemical in the stomach that helps digest food by breaking up fats and proteins. In particular, it is necessary for adequate absorption of protein, calcium, vitamin B12 and iron.</p>
            </div>

            <div class="txt-container border">
                <h2>Bioflavonoids</h2>
                <p>They enhance absorption of Vitamin C and protect the structure of capillaries besides improving circulation, lowering cholesterol levels, and stimulating bile production.</p>
            </div>

            <div class="txt-container border">
                <h2>Bioperine</h2>
                <p>A standardized extract from the fruit of Piper nigrum L (black pepper), it contains 95 percent of piperine. It is said to increase nutrient absorption and utilization along the gastrointestinal tract.</p>
            </div>

            <div class="txt-container border">
                <h2>Biotin</h2>
                <p>It aids in cell growth, fatty acid production, metabolism of carbs, fats and protein, as well as in the absorption of  vitamins from the B family (i.e., Vitamins B1, B2, …, B12).
                    It may prevent hair loss and relieve muscle pain while its deficiency may result in hair loss, high blood sugar, muscle pain and insomnia. </p>
            </div>

            <div class="txt-container border">
                <h2>Bloating</h2>
                <p>Bloating is a puffed up/swollen appearance of the body.  This usually occurs due to water retention in tissues. </p>
            </div>

            <div class="txt-container border">
                <h2>Body Mass Index (BMI)</h2>
                <p>A measurement of total body fat based on height and weight. By BMI standards, bodybuilders would be considered obese due to a higher concentration of muscle mass.</p>
            </div>


            <div class="txt-container border">
                <h2>Boswellia</h2>
                <p>Acts an anti-inflammatory, much like the conventional non-steroidal anti-inflammatory drugs used for inflammatory conditions. The extract, boswellic acid, is thought to exert anti-inflammatory effects by preventing the breakdown of connective tissue and by increasing blood supply to joint tissues.</p>
            </div>


            <div class="txt-container border">
                <h2>Branched Chain Amino Acids (BCAA)</h2>
                <p>The anti-catabolic trio of L-valine, L-leucine and L-isoleucine, BCAAs make up three of the “essential amino acids”.</p>
            </div>


            <div class="txt-container">
                <h2>Bromelain</h2>
                <p>Derived from the plant of pineapple, it is a proteolytic enzyme (enzyme capable of digesting protein). Bromelain is an anti-inflammatory agent and is helpful in healing minor injuries, particularly sprains, muscle injuries and accompanying swelling & tenderness.</p>
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

