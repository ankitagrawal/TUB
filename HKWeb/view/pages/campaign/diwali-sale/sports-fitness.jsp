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
      <a href="http://www.healthkart.com/sports"><img src="images/sports-fitness.jpg"/> </a>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1145'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1164'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT4780'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT4794'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT059'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1260'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1264'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2036'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2038'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3680'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1430'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT1431'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3591'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3611'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT3614'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT109'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT115'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT4138'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT4138'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT906'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT907'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT917'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2297'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT2363'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT952'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT954'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SPT081'/>




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

