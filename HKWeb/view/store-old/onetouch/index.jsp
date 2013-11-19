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
                 pageTitle="OneTouch">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/14feb.css"
          rel="stylesheet" type="text/css"/>

    <style>
      .onetouch {
        font-size: 13px;
        margin-top: 45px; float: none; clear: both;

      }

      h2 {
        font-size: 16px;
        margin-bottom: 10px;
        font-weight: bold;

      }

      .onetouch p{
        margin-bottom: 10px;
      }

      .onetouch ol {
        list-style-type: upper-roman;
        list-style-position:inside;
        padding: 0px;
        margin: 0;
      }

      .onetouch ol li{
        margin-bottom: 8px;
      }

    </style>

  </s:layout-component>

  <s:layout-component name="breadcrumbs">
    <div class='crumb_outer'><s:link
        beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
      &gt; <span class="crumb last" style="font-size: 12px;">OneTouch</span>

      <h1 class="title">OneTouch</h1>
    </div>

  </s:layout-component>

  <s:layout-component name="metaDescription">OneTouch</s:layout-component>
  <s:layout-component name="metaKeywords">OneTouch</s:layout-component>


  <s:layout-component name="content">


    <!---- paste all content from here--->

    <div id="wrapper">
      <img class="main-banner" src="${pageContext.request.contextPath}/store/onetouch/one-touch.jpg"/>

      <div class="cl"></div>

      <c:forEach items="${csaBean.clearanceSaleProductList}" var="product">
        <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='${product.productId}'/>
      </c:forEach>

          <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM014'/>
          <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS020'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DL004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS006'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DL005'/>

       <div class="cl"></div>
      <div class="onetouch">
        <h2 style="font-size: 16px">OneTouch Products: </h2>
        <p>Blood  glucose monitoring is very essential for managing diabetes. <b>OneTouch</b> is a brand  that has been making the life of a diabetic easier by means of its top quality  blood glucose monitoring devices. The company that fathers this brand was  founded in 1896 in Milpitas, California. The diabetes management brand OneTouch  is at present used by over 3 million people worldwide for the efficiency,  simplicity and accuracy of the products it manufactures. </p>
        <p><b>OneTouch  products</b> are now recommended by physicians and pharmacists as well. The brand  provides all that you need for monitoring your blood glucose such as blood  glucose meters, testing strips, lancing device and blood glucose management  software.</p>
        <p>Onetouch  glucose meters are of the following types: OneTouch UltraMini, OneTouch Ultra2,  OneTouch UltraSmart and OneTouch Ultra Select. </p>
        <ol>
          <li>The OneTouch UltraMini meter is so small that your purse or pocket can easily  accommodate it. It displays recent results with the help of its 500-test memory</li>
          <li>The OneTouch Ultra2 meter comes with large screen, large memory, download port  and provision to display average results for 7, 14 or 30 days. This glucose  meter further allows you to provide meal flags so as to find out the impact of  food on your glucose readings.</li>
          <li>The OneTouch UltraSmart meter does the clear charting of your blood glucose  results</li>
          <li>The OneTouch Ultra Select meter delivers accurate results in just 5 seconds.</li>
        </ol>
        <p>OneTouch  Ultra blue test strip checks your blood sample doubly and the OneTouch Ultra  Control Solution ensures the proper functioning of the meters and strips.
        OneTouch  Lancing devices come in 3 varieties: UltraSoft lancets, UltraSoft Adjustable  Blood Sampler and OneTouch Delica Lancing system.
        The  OneTouch Glucose management software transfers data from your meter to your  computer to enable you to study the trends.   </p>
        <p>Buy OneTouch products to control your blood glucose  better. </p>
      </div>

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





