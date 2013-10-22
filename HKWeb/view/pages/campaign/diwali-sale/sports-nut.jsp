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
    <img src="images/sports-nut.jpg"/>


    <%@include file="menu.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1823'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1231'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2122'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1340'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1749'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1534'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT521'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1635'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3082'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1417'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1812'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT524'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1540'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1837'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1332'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1359'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3083'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1206'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2866'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2853'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2144'/>


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

