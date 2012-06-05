<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliateStatisticsAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Statistics</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>

    <s:layout-component name="htmlHead">
      <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
      <link href="<hk:vhostCss/>/css/new.css" rel="stylesheet" type="text/css"/>
      <%--<link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>--%>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
      <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div>
      <h4 class="strikeline">Traffic Count:- ${affiliateBean.affiliateTrafficCount}</h4>

      <div style="margin-top:15px;"></div>

      <fieldset class="traffic">
        <legend>GENERATE TRAFFIC REPORT</legend>

        <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateStatisticsAction">
          <s:errors/>
          <div style="margin-top:15px;"></div>

          <div class="row">
            <s:label class="rowLabel" name="Start Date"/>
            <div class="rowText">
              <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
            </div>
          </div>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>

          <div class="row">
            <s:label class="rowLabel" name="End Date"/>
            <div class="rowText">
              <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
            </div>
          </div>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>

          <div class="row">
            <s:submit name="getTrafficCount" value="Generate Traffic Report" style="font-size:0.9em;"/>
          </div>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>

          <s:hidden name="affiliate" value="${affiliateBean.affiliate}"/>
        </s:form>
      </fieldset>


      <div class="clear"></div>
      <div style="margin-top:15px;"></div>

      <fieldset class="traffic">
        <legend>TRAFFIC DETAILS</legend>

        <div style="margin-top:15px;"></div>

        <div class="row">
          <c:choose>
            <c:when test="${!empty affiliateBean.affiliateTrafficDetails}">
              <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
              <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>

              <div style="margin-top:10px;"></div>

              <table class="cont footer_color" style="font-size: 14px;">
                <tr>
                  <th>Time Stamp</th>
                  <th>Target Url</th>
                  <th>Referral Url</th>
                </tr>
                <c:forEach items="${affiliateBean.affiliateTrafficDetails}" var="trafficDetails">
                  <tr>
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

              <div style="margin-top:10px;"></div>

              <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
              <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>
            </c:when>
            <c:otherwise>
              <s:label name="No traffic Recorded Yet"/>
            </c:otherwise>
          </c:choose>
        </div>

        <div class="clear"></div>
        <div style="margin-top:15px;"></div>

      </fieldset>
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myTrafficStats').style.fontWeight = "bold";
  };
</script>
<style type="text/css">
  table {
    width: 100%;
    margin-bottom: 10px;
    margin-top: 5px;
    border: 1px solid;
    border-collapse: separate;
  }

  table th {
    background: #f0f0f0;
    padding: 5px;
    text-align: left;
  }

  table td {
    padding: 5px;
    text-align: left;
    font-size: 0.9em;
  }

  .row {
    margin-top: 0;
    float: left;
    margin-left: 0;
    padding-top: 2px;
    padding-left: 26px;
  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    padding-left: 5px;
    width: 100px;
    height: 24px;
    margin-top: 5px;
  }

  .rowText {
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 30px;
    font: inherit;
  }

  fieldset.traffic {
    background: #fafaee;
    border-radius: 5px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    margin: 1em;
  }

  fieldset.traffic legend {
    display: block;
    color: #f87500;
    background: white;
    padding: 0 0.5em;
  }
</style>