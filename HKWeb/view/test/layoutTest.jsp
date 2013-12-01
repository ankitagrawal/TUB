<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/test/baseLayout.jsp">
  <s:layout-component name="title">asdfasdf</s:layout-component>
  <s:layout-component name="body">

    <div class="round-cont" style="width:695px;">
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE034"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE035"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE036"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="EYE037"/>
      <div align="right">
        <a class="more" href="${pageContext.request.contextPath}/eye/lenses">more>></a>
      </div>
      <div class="cl"></div>
    </div>




    <s:layout-component name="menu">
      
    </s:layout-component>
    


  </s:layout-component>
</s:layout-render>