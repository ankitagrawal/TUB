<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/errorLayout.jsp" pageTitle="Error 500 | Server Error" errorCode="500">


  <s:layout-component name="catalog">

    
    <div class='catalog_filters'>
      <div class='box'>
        <h5 class='heading1'>
          Product Categories
        </h5>
        <s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>
        <ul>
          <c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">
            <li><a href="${pageContext.request.contextPath}${topMenuNode.url}">${topMenuNode.name}</a></li>
          </c:forEach>
        </ul>
      </div>
    </div>

    <div class='catalog'>
      <h1>Unhandled Error / Page Not Found</h1>

      <p>
        You may have typed the address incorrectly or you may have used
        an outdated link.
        <br>
        If you found a broken link from another site or from our site,
        please <a href="mailto:info@healthkart.com">email us</a>.
      </p>
    </div>

  </s:layout-component>

</s:layout-render>
