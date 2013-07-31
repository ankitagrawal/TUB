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
    <link href="${pageContext.request.contextPath}/css/clearanceAug.css"
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
      <jsp:include page="/includes/_menuClearanceAug.jsp"/>


      <c:forEach items="${csaBean.clearanceSaleProductList}" var="product">
        <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='${product.productId}'/>
      </c:forEach>


      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI011'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HR037'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='VKR001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP064'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO030'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP067'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO028'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP069'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP065'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP063'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO026'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP068'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP062'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP057'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NISCO025'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HP066'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HICKS013'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='WA022'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HB035'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED026'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED0030'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED0033'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRSTA018'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GO003'/>




      <jsp:include page="/includes/_footerClearanceAug.jsp"/>

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





