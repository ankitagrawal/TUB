<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca"/>

<div id="prod_list" class="grid_18">
  <c:forEach items="${lca.productList}" var="lp">
      <div class="product_list_box">
        <s:layout-render name="/pages/loyalty/embed/_productListG.jsp" lpId="${lp.id}" lpv="${lp}"></s:layout-render>
      </div>
  </c:forEach>
</div>
