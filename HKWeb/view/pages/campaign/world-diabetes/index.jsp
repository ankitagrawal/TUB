<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.pages.ClearanceSaleAction" var="csaBean"/>

<%
  boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-ny2013.jsp"
                 pageTitle="World Diabetes Day">

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
      <img src="images/wdd-banner.jpg"/>
      <div class="cl"></div>



        <div class="heading-wd">Meters </div>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM008'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM014'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM007'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM003'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM033'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM041'/>
      <div class="cl"></div>



      <div class="heading-wd">Testing Supplies</div>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS001'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS005'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS020'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS010'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS012'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS002'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DS045'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='FRIO002'/>
      <div class="cl"></div>






      <div class="heading-wd">Weight Management</div>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV021'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HV020'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='EQNX015'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN008'/>

      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN013'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='HW004'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN011'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='OMRN012'/>
      <div class="cl"></div>





      <div class="heading-wd">Sweet and Savory </div>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA26'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA67'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA66'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA65'/>
      <div class="cl"></div>




      <div class="heading-wd">Combo Offers</div>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA01'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA32'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA59'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA46'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA40'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA55'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA48'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA20'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='DM019'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA47'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA37'/>
      <s:layout-render name="/layouts/embed/_productThumb200ny2013.jsp" productId='CMB-DIA07'/>
      <div class="cl"></div>





      <%@include file="footer-ds.jsp" %>

    </div>
    <!--- wrapper close -->

  </s:layout-component>

</s:layout-render>

