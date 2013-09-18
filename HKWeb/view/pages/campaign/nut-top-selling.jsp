<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.pages.ClearanceSaleAction" var="csaBean"/>

<%
  boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="Clearance Sale">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/14feb.css"
          rel="stylesheet" type="text/css"/>
  </s:layout-component>

  <s:layout-component name="breadcrumbs">
    <div class='crumb_outer'><s:link
        beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
      &gt; <span class="crumb last" style="font-size: 12px;">Clearance Sale</span>

      <h1 class="title">Clearance Sale</h1>
    </div>

  </s:layout-component>

  <s:layout-component name="metaDescription">Clearance Sale</s:layout-component>
  <s:layout-component name="metaKeywords">Clearance Sale</s:layout-component>


  <s:layout-component name="content">


    <!---- paste all content from here--->

    <div id="wrapper">
      <img class="main-banner" src="${pageContext.request.contextPath}/images/clearance/nut-top-selling.jpg"/>

      <div class="cl"></div>

      <c:forEach items="${csaBean.clearanceSaleProductList}" var="product">
        <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='${product.productId}'/>
      </c:forEach>

       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT304'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT326'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT131'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT468'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT694'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1794'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT680'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1264'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT471'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT364'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT300'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1147'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT681'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT125'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2138'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT130'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT122'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1368'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT472'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1146'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT420'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT319'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1367'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT333'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT922'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT525'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT730'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2974'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1171'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1162'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1600'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT3169'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1145'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT920'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT056'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1468'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT909'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT722'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT2125'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1821'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NUT1231'/>



      <div class="cl"></div>
      <div class="footer-ny">
        <p>© 2013 healthkart.com</p>
        <a href="https://twitter.com/healthkart"><img
            src="${pageContext.request.contextPath}/images/14feb/twitter-img.jpg"/></a>
        <a href="https://www.facebook.com/healthkart"><img
            src="${pageContext.request.contextPath}/images/14feb/fb-img.jpg"/></a>

      </div>

    </div>
    <!--wrapper close-->


    <c:if test="${not isSecure }">
      <iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
              scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
    </c:if>


  </s:layout-component>

  <s:layout-component name="menu">
  </s:layout-component>

</s:layout-render>





