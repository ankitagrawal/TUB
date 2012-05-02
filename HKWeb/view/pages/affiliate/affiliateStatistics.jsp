<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.affiliate.AffiliateStatisticsAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Statistics</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>

    <s:layout-component name="htmlHead">
      <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
      <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
      <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div>
      <h4 class="strikeline">Traffic Count:- ${affiliateBean.affiliateTrafficCount}
      </h4>
      <s:form beanclass="com.hk.web.action.affiliate.AffiliateStatisticsAction">
        <s:errors/>

        <div class="label">
          Start Date
          <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
          End Date <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
          <s:submit name="getTrafficCount" value="Generate Traffic Report"/>
        </div>

        <div class="round-cont" style="width:650px;margin-top: 20px;">
          <c:if test="${!empty affiliateBean.affiliateTrafficDetails}">
            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 14px;">
              <tr style="font-size: 12px;">
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Time Stamp</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Target Url</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Referral Url</th>
              </tr>
              <c:forEach items="${affiliateBean.affiliateTrafficDetails}" var="trafficDetails">
                <tr style="border-bottom: 1px solid #f0f0f0;">
                  <td>
                    <fmt:formatDate value="${trafficDetails.userTimestamp}" type="both"/>
                  </td>
                  <td style=" padding: 10px;">
                      ${trafficDetails.targetUrl}
                  </td>
                  <td>
                      ${trafficDetails.referralUrl}
                  </td>
                </tr>
              </c:forEach>
            </table>
            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>
          </c:if>
          <c:if test="${empty affiliateBean.affiliateTrafficDetails}">
            <div class="label">No traffic Recorded Yet</div>
          </c:if>
        </div>

        <s:hidden name="affiliate" value="${affiliateBean.affiliate}"/>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myTrafficStats').style.fontWeight = "bold";
  };
</script>