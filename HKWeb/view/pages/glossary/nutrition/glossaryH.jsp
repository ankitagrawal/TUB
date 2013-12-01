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
        <div class="heading">H</div>
        <div class="container">



            <div class="txt-container border">
                <h2>Histidine</h2>
                <p>An essential amino acid, It is  necessary in the growth and repair of tissues.
                    It protects the body from radiation damage and is important in stimulating the secretion of gastric juices necessary for proper digestion.</p>
            </div>



            <div class="txt-container border">
                <h2>Hormone</h2>

                <p>A substance produced by a gland or a similar tissue that is released into the bloodstream and regulates body processes such as growth or metabolism.</p>
            </div>


            <div class="txt-container border">
                <h2>Hyaluronic Acid</h2>
                <p>A protein that occurs naturally throughout the human body and is concentrated in the synovial joint fluids.
                    Hyaluronic acid belongs to a family of proteins known as glycosaminoglycans and is a key component of cartilage.
                    It is used to cushion the body from impact, to lubricate joints, and to protect joints from chronic inflammation. It can also heal damaged joint tissue. </p>
            </div>


            <div class="txt-container">
                <h2>HMB</h2>
                <p>(Beta-hydroxy beta-methylbutyrate) as a dietary supplement is said to promote muscle mass and muscle recovery.
                    Supplementing with HMB can give the body an advantage for recovery by minimizing the amount of protein that is broken down in the muscles after exercise.</p>
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

