<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <%
    String freebieBanner = (String) pageContext.getAttribute("freebieBanner");
    pageContext.setAttribute("freebieBanner", freebieBanner);
  %>
  <c:if test="${freebieBanner != null && freebieBanner != ''}">
    <div class="lineItemRow product">
      <img class="freebieBanner" id="freebieBanner" src="${freebieBanner}"/>
    </div>
  </c:if>
</s:layout-definition>