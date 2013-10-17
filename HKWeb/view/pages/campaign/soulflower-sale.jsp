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
                 pageTitle="Soulflower Offers">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/14feb.css"
          rel="stylesheet" type="text/css"/>
  </s:layout-component>

  <s:layout-component name="breadcrumbs">
    <div class='crumb_outer'><s:link
        beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
      &gt; <span class="crumb last" style="font-size: 12px;">Soulflower Offers</span>

      <h1 class="title">Soulflower Offers</h1>
    </div>

  </s:layout-component>

  <s:layout-component name="metaDescription">Soulflower Offers</s:layout-component>
  <s:layout-component name="metaKeywords">Soulflower Offers</s:layout-component>


  <s:layout-component name="content">


    <!---- paste all content from here--->

    <div id="wrapper">
      <img class="main-banner" src="${pageContext.request.contextPath}/images/clearance/soulflower-banner.jpg"/>

      <div class="cl"></div>

      <c:forEach items="${csaBean.clearanceSaleProductList}" var="product">
        <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='${product.productId}'/>
      </c:forEach>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR13'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR14'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR15'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR6'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR59'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR34'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR35'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR36'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR37'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR39'/>



      <div class="cl"></div>
      <div class="footer-ny">
        <p>© 2013 healthkart.com</p>
        <a href="https://twitter.com/healthkart"><img
            src="${pageContext.request.contextPath}/images/14feb/twitter-img.jpg"/></a>
        <a href="https://www.facebook.com/healthkart"><img
            src="${pageContext.request.contextPath}/images/14feb/fb-img.jpg"/></a>

      </div>

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





