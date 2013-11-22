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
      <a href="http://www.healthkart.com/health-devices"><img src="images/HD.jpg"/> </a>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV021'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV020'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV010'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB005'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB039'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB038'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB042'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP021'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DG006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX015'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRAJ003'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB023'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AS006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX009'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB025'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX010'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JSB024'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRSTA015'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX012'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ZEPTER007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PRAJ006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB061'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU083'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RSTR009'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX013'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV003'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN006'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEU075'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRSTA016'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BIOMRCA004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HMEDIC003'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RSTR013'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX017'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KARM021'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRSTA012'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA016'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='COMODE005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO033'/>





    </div>
      <!--- box-product close -->

      <%@include file="footer-ds.jsp" %>

    </div>
      <!--- wrapper close -->

  </s:layout-component>

</s:layout-render>

