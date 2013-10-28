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
    <img src="images/diabetes.jpg"/>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIASOC001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FCDIA001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FCDIA003'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='UNI003'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='UNI002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT035'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA67'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA65'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA66'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT689'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT033'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT034'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SWT036'/>


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

