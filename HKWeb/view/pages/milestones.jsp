<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp" pageTitle="Milestones">
   <s:layout-component name="topCategory">baby</s:layout-component>
  <s:layout-component name="lhsContent">
     <jsp:include page="babytools-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div class="main-inn-right">
      <table border="0" width="100%">
        <tr>
          <td width="100%" style="text-align: justify;" valign="top">

            Each baby has a unique growth pattern - a baby who sits up weeks
            before her peers might be one of the last to learn how to crawl -
            that's why the charts allow for variations in stages of
            development. The master chart below is a high level snapshot of
            commonly exhibited milestones. If you need detailed milestones,
            refer our age milestone section - <span class="regular"><a
              href="<%=request.getContextPath()%>/pages/stage1.jsp">1 to 6
            months</a>, <a href="<%=request.getContextPath()%>/pages/stage2.jsp">
            7 to 12 months</a>, <a
              href="<%=request.getContextPath()%>/pages/stage3.jsp">13 to 18
            months</a>, <a href="<%=request.getContextPath()%>/pages/stage4.jsp">
            19 to 24 months</a>, <a
              href="<%=request.getContextPath()%>/pages/stage5.jsp"> 25 to 30
            months</a> and <a href="<%=request.getContextPath()%>/pages/stage6.jsp">31
            to 36 months</a></span>.<br/>
            <br/>
            The reference below should be used to gain insight into what
            you're observing in your baby today and to preview what you can
            look forward to in the months ahead. The charts shouldn't be
            interpreted as signs of worry - each chart is meant as a guide,
            not as a source of concern.
          </td>
        </tr>
        <tr>
          <td valign="top"><img width="100%"
                                src="<%=request.getContextPath()%>/images/MilestoneGraph.png"/>
          </td>
        </tr>
      </table>
    </div>
  </s:layout-component>
</s:layout-render>