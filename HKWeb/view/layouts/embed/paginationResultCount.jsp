<%@ page import="com.akube.framework.stripes.action.BasePaginatedAction" %>
<%@ page import="com.akube.framework.stripes.constant.PaginationConstants" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<%--
  Pass an attribute called paginatedBean to this layout to render the pagination
  paginatedBean should be an instance of BasePaginatedAction type
--%>
<s:layout-definition>
  <%
    Object basePaginatedActionObj = pageContext.getAttribute("paginatedBean");
    pageContext.setAttribute("paginatedBean", basePaginatedActionObj);
    String maxSisplayStr = (String) pageContext.getAttribute("maxDisplay");
    if (StringUtils.isBlank(maxSisplayStr)) {
      maxSisplayStr = "8";
    }
    Integer maxDisplay;
    try {
      maxDisplay = Integer.parseInt(maxSisplayStr);
    } catch (NumberFormatException e) {
      maxDisplay = 8;
    }

    if (basePaginatedActionObj != null && basePaginatedActionObj instanceof BasePaginatedAction) {
      BasePaginatedAction basePaginatedAction = (BasePaginatedAction) basePaginatedActionObj;
      int startResult = (basePaginatedAction.getPageNo() - 1)*basePaginatedAction.getPerPage() + 1;
      int endResult = (basePaginatedAction.getPageNo() - 1)*basePaginatedAction.getPerPage() + basePaginatedAction.getPerPage();
      if (endResult > basePaginatedAction.getResultCount()) endResult = basePaginatedAction.getResultCount();
  %>
  Showing <%=startResult%> - <%=endResult%> of <%=basePaginatedAction.getResultCount()%> results
  <%
  } else {
  %>
  No BasePaginatedAction object found with the attribute paginatedBean
  <%
    }
  %>
</s:layout-definition>