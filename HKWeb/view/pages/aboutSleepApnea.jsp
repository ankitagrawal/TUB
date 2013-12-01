<%--
<%@ page import="mhc.pojo.FAQ" %>
<%@ page import="java.util.List" %>
<%@ page import="mhc.servlet.action.FAQAction" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp">
  <%
    String searchString = "";
    if (request.getParameter("que") != null) {
      searchString = request.getParameter("que");
    }
  %>
  <s:layout-component name="topCategory">home-devices</s:layout-component>
  <s:layout-component name="heading">
    <h1 class="orange strikeline">Sleep Apnea FAQs</h1>
  </s:layout-component>
  <s:layout-component name="left_col">
        <div>

          <p>

          <form name="faqForm" method="get" action="aboutSleepApnea.jsp"><input
              size="30" value="<%=searchString %>" class="inputBox" name="que"/>
            &nbsp; <input type="submit" value="Search"
                          class=""/>&nbsp; <input type="submit"
                                                  onclick="document.faqForm.que.value=''"
                                                  value="Reset"
                                                  class=""/>
          </form>
          </p>
        </div>

        <%
          int sNo = 0;
          List<FAQ> faqList = (new FAQAction()).getFAQs(searchString, "CPAP");
//          System.out.println("faqList Size - " + faqList.size());

          for (Iterator iterator = faqList.iterator(); iterator.hasNext();) {
            FAQ faq = (FAQ) iterator.next();
            sNo++;
        %>
        <p><h2><span id="qId<%=faq.getId() %>"><%=faq.getQuestion().replaceAll("'", "\'")%>
          </span></h2></p>

        <p><%=faq.getAnswer().replaceAll("'", "\'")%>
        </p>
        <%
          }
        %>
  </s:layout-component>
</s:layout-render>--%>
