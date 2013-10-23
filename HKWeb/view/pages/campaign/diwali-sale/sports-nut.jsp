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
    <img src="images/sports-nutrition.jpg"/>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1604'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2865'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1161'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT725'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1157'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1162'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1159'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT471'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1821'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1225'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1327'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3083'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2866'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1395'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1723'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT487'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1741'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1415'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1287'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2060'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT986'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1839'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1722'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1804'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2151'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1842'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1115'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1337'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2150'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1751'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2144'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2022'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1266'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1830'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2862'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1263'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2019'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT888'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1244'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1159'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2134'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1348'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1901'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2662'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1178'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1812'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2132'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT487'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1415'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1839'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1741'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1287'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2060'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1365'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2119'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1115'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2056'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1804'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1245'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1213'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT365'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1838'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2866'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2976'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1217'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1211'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT907'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT920'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1369'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2655'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT670'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1540'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2929'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1147'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2099'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT922'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1222'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2869'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2135'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1229'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1214'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2926'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2927'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2928'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2050'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2049'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT482'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT893'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT376'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT891'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1645'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1383'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1642'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1718'/>




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

