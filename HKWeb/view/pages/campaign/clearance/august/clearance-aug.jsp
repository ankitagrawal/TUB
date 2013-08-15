<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.pages.ClearanceSaleAction" var="csaBean"/>

<%
	boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Clearance Sale">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/clearanceAug.css"
          rel="stylesheet" type="text/css"/>
  </s:layout-component>

  <s:layout-component name="breadcrumbs">
    <div class='crumb_outer'><s:link
        beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
      &gt; <span class="crumb last" style="font-size: 12px;">Clearance Sale</span>

      <h1 class="title">Clearance Sale</h1>
    </div>

  </s:layout-component>

  <s:layout-component name="metaDescription">Clearance Sale</s:layout-component>
  <s:layout-component name="metaKeywords">Clearance Sale</s:layout-component>


  <s:layout-component name="content">


    <!---- paste all content from here--->

    <div id="wrapper">
      <jsp:include page="/includes/_menuClearanceAug.jsp"/>


      <c:forEach items="${csaBean.clearanceSaleProductList}" var="product">
        <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='${product.productId}'/>
      </c:forEach>




      <div class="container01">


        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/snCS.jsp"><img class="space-r box1" src="${pageContext.request.contextPath}/images/clearance/sprotsNutrition.jpg" /></a>
        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/eyeCS.jsp"><img class="box1" src="${pageContext.request.contextPath}/images/clearance/eye.jpg" /></a>
        <div class="cl"></div>


        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/diabetesCS.jsp"><img class="space-r box1" src="${pageContext.request.contextPath}/images/clearance/Diabtese.jpg" /></a>
        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/careCS.jsp"><img class="space-r box1" src="${pageContext.request.contextPath}/images/clearance/Personalcare.jpg" /></a>
        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/fitnessCS.jsp"><img class="box1" src="${pageContext.request.contextPath}/images/clearance/sports.jpg" /></a>
        <div class="cl"></div>


        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/hnCS.jsp"><img class="space-r box1" src="${pageContext.request.contextPath}/images/clearance/healthNutrition.jpg" /></a>
        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/devicesCS.jsp"><img class="box1" src="${pageContext.request.contextPath}/images/clearance/HealthDevices.jpg" /></a>
        <div class="cl"></div>



        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/parentingCS.jsp"><img class="space-r box1" src="${pageContext.request.contextPath}/images/clearance/parenting.jpg" /></a>
        <a href="${pageContext.request.contextPath}/pages/campaign/clearance/august/beautyCS.jsp"><img class="box1" src="${pageContext.request.contextPath}/images/clearance/beauty.jpg" /></a>


      </div>


      <jsp:include page="/includes/_footerClearanceAug.jsp"/>

    </div>
    <!--wrapper close-->


    <c:if test="${not isSecure }">
      <iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
              scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
    </c:if>


  </s:layout-component>

  <s:layout-component name="menu">
  </s:layout-component>

</s:layout-render>





