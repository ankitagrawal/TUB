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
    <div class="logo"><a href="index.jsp"><img src="images/hk-logo.png"/></a></div>
      <a href="http://www.healthkart.com/home-living"><img src="images/home-living.jpg"/>  </a>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAJ125'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BUT002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHI080'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SNF049'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='INA011'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAJ071'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FAB001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EVE005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SIN001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHI077'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BUT003'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PAN034'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAJ116'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAD004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAJ085'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MORP004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHI015'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WST001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EVE008'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MORP015'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAD004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAJ118'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BAJ075'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MORP038'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='INA040'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RUS047'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRES012'/>


    </div>
      <!--- box-product close -->

      <div class="cl"></div>
      <div class="footer-ny">
        <p>© 2013 healthkart.com</p>
        <a href="https://twitter.com/healthkart"><img
            src="${pageContext.request.contextPath}/images/14feb/twitter-img.jpg"/></a>
        <a href="https://www.facebook.com/healthkart"><img
            src="${pageContext.request.contextPath}/images/14feb/fb-img.jpg"/></a>

      </div>

    </div>
      <!--- wrapper close -->

  </s:layout-component>

</s:layout-render>

