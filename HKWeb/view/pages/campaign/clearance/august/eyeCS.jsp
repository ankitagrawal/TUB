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


      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1106'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1111'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1107'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1105'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1110'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1109'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE997'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1129'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1122'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1130'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1140'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1112'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1131'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1115'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1128'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1141'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1121'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1123'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1132'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE780'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1144'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE982'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE989'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE732'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE724'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE725'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE726'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE728'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1054'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1055'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1064'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1042'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1066'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1555'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1882'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE2065'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1618'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1614'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1612'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1627'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1607'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1611'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1599'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1602'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1596'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1581'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EYE1584'/>






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





