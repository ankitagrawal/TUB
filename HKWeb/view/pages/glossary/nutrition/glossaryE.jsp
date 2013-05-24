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
        <div class="heading">E</div>
        <div class="container">






            <div class="txt-container border">
                <h2>EGCG</h2>
                <p>Epigallocatechin gallate (EGCG) is the ester of epigallocatechin and gallic acid, and is a type of catechin.
                    It is abundantly found in green tea and is a potent antioxidant that has therapeutic applications in the treatment of many disorders.</p>
            </div>


            <div class="txt-container border">
                <h2>Egg Protein</h2>
                <p>A high quality, all-purpose protein, egg protein has been a staple protein source for bodybuilders since the beginning of the sport. Today’s high quality egg white proteins are typically free of fat, cholesterol and lactose.
                    They’re also high in essential amino acids--especially the important sulfur-containing aminos.</p>
            </div>


            <div class="txt-container border">
                <h2>Essential Amino Acids (EAA)</h2>

                <p>Key amino acids that are called "essential" because they can only be provided through diet and
                    supplementation (they cannot be synthesized sufficiently in the human body).
                    They are crucial during the post-workout window for initiating new growth.
                    EAAs include the three BCAAs- isoleucine, leucine, and valine along with histidine, lysine,
                    methionine, phenylalanine, threonine and tryptophan. See also Animal Nitro or Animal Nitro G.</p>
            </div>


            <div class="txt-container border">
                <h2>Essential Fatty Acids (EFA)</h2>
                <p>Comprise omega-3 and omega-6 fatty acids which provide tremendous benefits to bodybuilders. Like EAAs, EFAs must be obtained through diet and are considered "essential".</p>
            </div>


            <div class="txt-container">
                <h2>Evodiamine</h2>
                <p>A bioactive alkaloid extract from a plant called Evodiae Fructus. Evodiamine assists in raising the body temperature, influences the metabolism of certain drugs and influences the secretion of catecholamines from the adrenal glands.
                    Evodiamine also stimulates the natural rate of burning fat.</p>
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

