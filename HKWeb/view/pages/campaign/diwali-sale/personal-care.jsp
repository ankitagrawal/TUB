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
      <a href="http://www.healthkart.com/personal-care"><img src="images/Personalcare.jpg"/>  </a>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AYUCR4'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV011'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MOS001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV008'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV022'/>


      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='3M001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ROM006'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP004'/>


      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AMRON013'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JUNO001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RD001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH064'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR011'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF020'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WRSTS002'/>



    </div>
      <!--- box-product close -->

      <%@include file="footer-ds.jsp" %>

    </div>
      <!--- wrapper close -->

  </s:layout-component>

</s:layout-render>

