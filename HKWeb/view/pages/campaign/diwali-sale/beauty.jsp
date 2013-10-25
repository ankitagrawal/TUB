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
    <img src="images/beauty.jpg"/>


    <%@include file="menu-ds.jsp" %>

    <div class="box-product">

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DBRH10'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD177'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX60'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX60'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARMTR21'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA32'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA10'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX65'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX65'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX24'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX24'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BRESN61'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARMTR20'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TSS02'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX9'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX61'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX51'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARMTR14'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX41'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PROTV03'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PROTV02'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX23'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX23'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX1'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX1'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX1'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX1'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX6'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX6'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BASCAR105'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX33'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX24'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA24'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB02'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX21'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='KNAD14'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA68'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE37'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SLFLR2'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE38'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB12'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB05'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB07'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VEDIC39'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIWLI02'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIWLI01'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA70'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE41'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIWLI05'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ARVDC34'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SENRA7'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA13'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA15'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIWLI04'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB20'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PHILI35'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM11'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VTRE39'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WDFR32'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY143'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AYUCR1'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI60'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-BTY01'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BEVCMB01'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='PARKAV12'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WDFR41'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIWLI03'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='AVRA39'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='JLM47'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WDFR35'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MNFCR18'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VAADI63'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DIWLI06'/>



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

