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
      if (basePaginatedAction.getPageCount() > 1) {
  %>
  <div class="pagi">
    <div class="links">
        <%-- Previous link if applicable --%>
      <%
        if (basePaginatedAction.getPageNo() > 1) {
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=basePaginatedAction.getPageNo()-1%>" pageText="&larr; Previous"/>
      <%
        }
      %>

        <%-- This renders the middle pages --%>

      <%
        if (basePaginatedAction.getPageCount() > maxDisplay) {
          // See the location of the current page.
          // if it is close to the first page by lesser than max-4 pages then show 1 to max-2 .. last-1, last
          // if it is close to the last page by lesser than max-4  pages then show 1,2 .. last-(max-2) to last
          // if neither.. then show 1,2 .. current-(max/2-1) to current+(max/2+1) .. last-1, last
          if (basePaginatedAction.getPageNo() < (maxDisplay - 4)) {
            // Case 1
            for (int pageNo = 1; pageNo <= (maxDisplay - 2); pageNo++) {
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=pageNo%>" pageText="<%=pageNo%>"/>
      <%
        }
      %>

      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=basePaginatedAction.getPageCount()-1%>" pageText="<%=basePaginatedAction.getPageCount()-1%>"/>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=basePaginatedAction.getPageCount()%>" pageText="<%=basePaginatedAction.getPageCount()%>"/>
      <%
      } else if ((basePaginatedAction.getPageCount() - basePaginatedAction.getPageNo()) < (maxDisplay - 4)) {
        // Case 2
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="1" pageText="1"/>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="2" pageText="2"/>

      <%
        for (int pageNo = (basePaginatedAction.getPageCount() - maxDisplay + 2); pageNo <= basePaginatedAction.getPageCount(); pageNo++) {
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=pageNo%>" pageText="<%=pageNo%>"/>
      <%
        }
      } else {
        // Case 3
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="1" pageText="1"/>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="2" pageText="2"/>
      <%--<li>..</li>--%>
      <%
        for (int pageNo = basePaginatedAction.getPageNo() - (maxDisplay / 2 - 2); pageNo <= basePaginatedAction.getPageNo() + (maxDisplay / 2 - 2); pageNo++) {
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=pageNo%>" pageText="<%=pageNo%>"/>
      <%
        }
      %>

      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=basePaginatedAction.getPageCount()-1%>" pageText="<%=basePaginatedAction.getPageCount()-1%>"/>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=basePaginatedAction.getPageCount()%>" pageText="<%=basePaginatedAction.getPageCount()%>"/>
      <%
        }

      } else {
        // very easy, render all pages!
        for (int pageNo = 1; pageNo <= basePaginatedAction.getPageCount(); pageNo++) {
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=pageNo%>" pageText="<%=pageNo%>"/>
      <%
          }
        }
      %>

        <%-- Next link if applicable --%>
      <%
        if (basePaginatedAction.getPageNo() < basePaginatedAction.getPageCount()) {
      %>
      <s:layout-render name="/layouts/embed/_pageNoLink.jsp" paginatedBean="${paginatedBean}" pageNo="<%=basePaginatedAction.getPageNo()+1%>" pageText="Next &rarr;"/>
      <%
        }
      %>
    </div>
  </div>
  <%
    }
  %>
  <span class="perPage-span" style="display:none;"><%=basePaginatedAction.getPerPage()%></span>
  <%
  } else {
  %>
  No BasePaginatedAction object found with the attribute paginatedBean
  <%
    }
  %>

</s:layout-definition>