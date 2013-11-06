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
      <a href="http://www.healthkart.com/parenting"> <img src="images/parenting.jpg"/>    </a>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0416'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0417'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BAB0418'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2405'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2409'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB380'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB2338'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB292'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB293'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAB1812'/>


    </div>
      <!--- box-product close -->

      <%@include file="footer-ds.jsp" %>

    </div>
      <!--- wrapper close -->

  </s:layout-component>

</s:layout-render>

