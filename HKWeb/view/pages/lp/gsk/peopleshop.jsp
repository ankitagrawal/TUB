<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default100.jsp" pageTitle="GSK PeopleShop">

  <s:layout-component name="content">

    <div>
      <img src="${pageContext.request.contextPath}/pages/lp/gsk/images/banner_gsk02.jpg" alt="PeopleShop">
    </div>

    <div class="products">
      <h2>
        Horlicks
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT775"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT779"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT781"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT778"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT780"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT777"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT970"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT971"/>
    </div>

  <div class="clear"></div>

  <div class="products" style="float:left;">
    <h2>
      Boost
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT774"/>
  </div>

    <div class="products" style="float:left;">
      <h2>
        Foodles
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT776"/>
    </div>

    <div class="products" style="float:left;">
      <h2>
        Iodex
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PRS004"/>
    </div>

    <div class="products" style="float:left;">
      <h2>
        Eno
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="PP009"/>
    </div>

    <div class="products" style="float:left;">
      <h2>
        Sensodyne
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="OH032"/>
    </div>

  </s:layout-component>

</s:layout-render>
