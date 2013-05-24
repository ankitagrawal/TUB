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
        <div class="heading">V</div>
        <div class="container">




            <div class="txt-container border">
                <h2>Valine</h2>
                <p>An essential amino acid and  one of the three vital BCAA’s, it’s needed for muscle metabolism, tissue repair, and the maintenance of proper nitrogen balance.
                    It is found in high concentrations in muscle tissue.</p>
            </div>


            <div class="txt-container border">
                <h2>Vinpocetine</h2>
                <p>Found in the leaves of Vinca minor (lesser periwinkle) plant, it facilitates cerebral metabolism by improving blood flow to the brain,
                    boosting ATP production, and increasing utilization of glucose and oxygen by neurons.
                    Used as a cerebral vasodilator for enhancing mental alertness, it increases blood flow to the brain and improves its oxygenation.</p>
            </div>


            <div class="txt-container border">
                <h2>Vitamin A</h2>
                <p>A fat-soluble vitamin, it is an antioxidant that is required for the maintenance and repair of skin tissue. A deficiency of
                    Vitamin A can cause dry hair and skin, night blindness, frequent colds, other respiratory infections, and skin disorders including acne.</p>
            </div>


            <div class="txt-container border">
                <h2>Vitamin C</h2>
                <p>A water-soluble vitamin, it is an antioxidant required for at least 300 metabolic processes in the body including tissue growth and repair and adrenal gland function.
                    It acts as an immune booster and also helps in producing anti-stress hormones.
                    Deficiency of the same can cause poor wound healing, edema, extreme weakness and increase susceptibility to infection.</p>
            </div>



            <div class="txt-container border">
                <h2>Vitamin D</h2>
                <p>A fat-soluble vitamin, it  has the unique property of both a vitamin and a hormone. It is required for growth and maintenance of bones and teeth.
                    Vitamin D deficiency can lead to poor calcium and phosphorous absorption, leading to brittle bones.</p>
            </div>



            <div class="txt-container">
                <h2>Vitamin E</h2>
                <p>A fat-soluble vitamin, it is important in the prevention of cardiovascular disease.
                    It improves circulation necessary for tissue repair, promotes blood clotting, reduces scarring and blood pressure.
                    It also acts as an antioxidant by preventing cell damage and inhibiting free radical production. Vitamin E deficiency can result in infertility and free radical damage to cells.</p>
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

