<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.pages.ClearanceSaleAction" var="csaBean"/>

<%
  boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Diwali Sale">

  <s:layout-component name="htmlHead">
    <link type="text/css" rel="stylesheet" href="default.css"/>
    <link href="${pageContext.request.contextPath}/css/14feb.css"
          rel="stylesheet" type="text/css"/>
  </s:layout-component>

  <s:layout-component name="header">
  </s:layout-component>

  <s:layout-component name="menu">
  </s:layout-component>


  <!--- product list start -->

  <s:layout-component name="content">

    <div id="wrapper">
    <div class="logo"><a href="http://www.healthkart.com/"><img src="images/hk-logo.png"/></a></div>
      <div class="cl"></div>
      <a href="index.jsp"><img class="banner-ds" src="images/banner-ds.jpg"/></a>

      <a href="http://www.healthkart.com/health-nutrition"><img src="images/HealthNutrition.jpg"/> </a>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HNUT236'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HNUT369'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HNUT266'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HNUT234'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HNUT235'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HNUT265'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1118'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1038'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2930'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1083'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1129'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2023'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2008'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1236'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1124'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1033'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1121'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT534'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT558'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1513'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1043'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HNUT235'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1117'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1114'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT954'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT411'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1134'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1408'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1325'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1101'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT663'/>


    </div>
      <!--- box-product close -->

      <%@include file="footer-ds.jsp" %>

    </div>
      <!--- wrapper close -->

  </s:layout-component>

</s:layout-render>

