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
        <div class="heading">G</div>
        <div class="container">






            <div class="txt-container border">
                <h2>Garcinia Cambogia</h2>
                <p>It acts as an appetite suppressant by naturally increasing the body's serotonin levels which are
                    the key chemicals that influence appetite. The end result is that you eat less calories each day,
                    that reduces bloating and weight gain. Garcinia extract helps block citrate lyase, an enzyme that
                    instructs your body to create fat from carbohydrates. Likewise, Garcinia Cambogia can provide a
                    tremendous boost in metabolism, causing your body to burn the fat you already have.</p>
            </div>


            <div class="txt-container border">
                <h2>Ginger Root Extract</h2>
                <p>It has antioxidant and anti-inflammatory properties. As an antioxidant, it helps boost immunity and
                    relieves symptoms of cold and flu. Its anti-inflammatory properties make it useful in treating pain
                    and inflammation caused by arthritis. Ginger root is also used to help treat upset stomach and
                    nausea.</p>
            </div>



            <div class="txt-container border">
                <h2>Ginseng</h2>
                <p>It strengthens the adrenal and reproductive glands. While enhancing immune function.
                    It also stimulates appetite and is beneficial for fatigue because it spares glycogen and increases the use of fatty acids as an energy source.  </p>
            </div>


            <div class="txt-container border">
                <h2>Glucosamine</h2>
                <p>A molecular building block that aids body builders and athletes with joint pain. It repairs and lubricates the cartilage around damaged joints.
                    It has been proven effective in easing osteoarthritis pain, rehabilitating cartilage, renewing synovial fluid and repairing joints that have been damaged.</p>
            </div>


            <div class="txt-container border">
                <h2>Glutamic Acid</h2>
                <p>A non-essential amino acid, it is important in the metabolism of sugars and fats.
                    It helps in the detoxification of ammonia and can be converted to conditionally essential amino acid glutamine.</p>
            </div>


            <div class="txt-container border">
                <h2>Glutamine</h2>
                <p>A conditionally essential amino acid, it is an abundant free-amino acid in the muscles.
                    It helps to build and maintain muscle and also enhances antioxidant protection. It is needed to sustain proper brain function and mental activity.</p>
            </div>


            <div class="txt-container border">
                <h2>Glycemic Index</h2>
                <p>Ranking system which rates the speed of carbohydrate absorption.</p>
            </div>




            <div class="txt-container border">
                <h2>Glycine</h2>
                <p>A non-essential amino acid, It improves glycogen storage, increases endogenous creatine levels through increasing creatine synthesis.
                    It also plays a key role in neurotransmitter release.</p>
            </div>



            <div class="txt-container border">
                <h2>Grapeseed Extract</h2>

                <p>A natural plant substance that has a concentrated source of anti-oxidants called OPCs. These antioxidants help protect cells from free radical damage and promote healthy circulation.
                    It is one of the few anti-oxidants that can penetrate the blood brain barrier to help protect the brain and nerve tissue.</p>
            </div>


            <div class="txt-container border">
                <h2>Green Tea Extract</h2>
                <p>A powerful antioxidant that contains high levels of polyphenols and EGCG.</p>
            </div>


            <div class="txt-container">
                <h2>Guarana Extract</h2>
                <p>Guarana is derived from the seeds of a South American tree. Because it is high in caffeine, Guarana has become a popular energy supplement.
                    When combined with other ingredients in a fat burner, it helps boost their efficacy.</p>
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

