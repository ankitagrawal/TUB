<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliateInsightsAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Insights</s:layout-component>
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
      <h4 class="strikeline">Orders Count:- ${affiliateBean.affiliateReferredOrdersCount}
      </h4>
      <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateInsightsAction">
        <s:errors/>

        <div class="label">
          Start Date
          <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
          End Date <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
          <s:submit name="getReferredOrderDetails" value="Generate Insights"/>
        </div>

        <div class="round-cont" style="width:650px;margin-top: 20px;">
          <c:if test="${!empty affiliateBean.affiliateTxnOrdersList}">
            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 14px;">
              <tr style="font-size: 12px;">
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Time Stamp</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Order Amount</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Amount Credited</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Order Status</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">City</th>
              </tr>
              <c:forEach items="${affiliateBean.affiliateTxnOrdersList}" var="txn">
                <tr style="border-bottom: 1px solid #f0f0f0;">
                  <td>
                    <fmt:formatDate value="${txn.date}" type="both"/>
                  </td>
                  <td style=" padding: 10px;">
                      ${txn.order.amount}
                    <s:link beanclass="com.hk.web.action.core.referral.ReferredOrderDetailsAction">
                      <s:param name="order" value="${txn.order.id}"/>
                      [Break-Up]
                    </s:link>
                  </td>
                  <td>
                    <c:if test="${txn.affiliateTxnType.id ne 10}">
                      <div class='prices' style="font-size: 12px;margin-left:10px;margin-bottom:5px">
                        <div class='cut' style="font-size: 12px;">
                            <span class='num' style="font-size: 12px;">
                              Rs <fmt:formatNumber value="${txn.amount}" maxFractionDigits="0"/>
                            </span>
                        </div>
                      </div>
                    </c:if>
                    <c:if test="${txn.affiliateTxnType.id eq 10}">
                      <div class='prices' style="font-size: 12px;margin-left:10px;margin-bottom:5px">
                        <div class='hk' style="font-size: 12px;margin-top:0px">
                            <span class='num' style="font-size: 12px;;text-decoration:none">
                              Rs <fmt:formatNumber value="${txn.amount}" maxFractionDigits="0"/>
                            </span>
                        </div>
                      </div>
                    </c:if>
                  </td>
                  <td>
                      ${txn.affiliateTxnType.name}
                  </td>
                  <td>
                      ${txn.order.address.city}
                  </td>
                </tr>
              </c:forEach>
            </table>
            <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
            <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>
          </c:if>
          <c:if test="${empty affiliateBean.affiliateTxnOrdersList}">
            <div class="label">None Orders Placed Yet</div>
          </c:if>
        </div>

        <s:hidden name="affiliate" value="${affiliateBean.affiliate}"/>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myReferredOrders').style.fontWeight = "bold";
  };
</script>