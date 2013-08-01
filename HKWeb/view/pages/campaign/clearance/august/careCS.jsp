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


      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA015'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HEA004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NM009'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='GINNI007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR016'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRAC001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR008'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR038'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR037'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRAC002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CERSUP002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR021'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR042'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR035'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR031'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TYNOR009'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='TRAC002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='MED079'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN015'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='SUN019'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FUTURO006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH100'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH041'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OH098'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='RLF012'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED0042'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='BREMED0041'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='ECIGAR001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NM019'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NM007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='NM016'/>






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





