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


      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR08'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR04'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX65'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX24'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX69'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR11'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LBDR03'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NEOV21'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX60'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX5'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX47'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX31'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX48'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LTHB48'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX68'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX55'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX64'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NYX26'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL76'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL81'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='LOREAL80'/>
       <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY491'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV26'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY493'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV28'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV31'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='REV27'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BTY500'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SHNZ33'/>





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





