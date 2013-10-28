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
      <a href="http://www.healthkart.com/sports-nutrition"><img src="images/sports-nutrition.jpg"/> </a>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1891'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1837'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1135'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3082'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1840'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2063'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT524'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT725'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1540'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1359'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT967'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3175'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1417'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1219'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2933'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1220'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1711'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2865'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2929'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1823'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2091'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT922'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1162'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1903'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1847'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2923'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2152'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1266'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2147'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1268'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2148'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT521'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2657'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT909'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1159'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1231'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1720'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2154'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1724'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1764'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2142'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1287'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1604'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1263'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1368'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1271'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3178'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1269'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1833'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1272'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1853'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2931'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1842'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2932'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3083'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT987'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1838'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT486'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2128'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT525'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1327'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT482'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1340'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1157'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1350'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1161'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2663'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1397'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT517'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1699'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2975'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1723'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1812'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2058'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1344'/>



    </div>
      <!--- box-product close -->

      <%@include file="footer-ds.jsp" %>

    </div>
      <!--- wrapper close -->

  </s:layout-component>

</s:layout-render>

