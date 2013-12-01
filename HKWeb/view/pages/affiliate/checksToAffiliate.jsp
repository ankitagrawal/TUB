<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
  <%--<s:layout-component name="heading">Transaction Details</s:layout-component>--%>
  <s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div>
      <div class="main-inn-right">
        <h4 class="strikeline">Transaction Details</h4>

        <div class="round-cont" style="width:650px;margin-top: 20px;">
          <c:if test="${!empty affiliateBean.checkDetailsList}">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 14px;">
              <tr style="font-size: 12px;">
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Transaction Reference No</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Issue Date</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Payment Mode</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Amount Due</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Amount Paid (After TDS)</th>
              </tr>
              <c:forEach items="${affiliateBean.checkDetailsList}" var="checkDetails">
                <tr style="border-bottom: 1px solid #f0f0f0;">
                  <td style=" padding: 10px;">
                      ${checkDetails.checkNo}
                  </td>
                  <td>
                    <fmt:formatDate value="${checkDetails.issueDate}" pattern="dd/MM/yyyy"/>
                  </td>
                  <td>
                      ${checkDetails.bankName}
                  </td>
                  <td>
                    <fmt:formatNumber value="${checkDetails.affiliateTxn.amount}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
                  </td>
                  <td>
                    <fmt:formatNumber value="${checkDetails.affiliateTxn.amount + checkDetails.tds}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
                  </td>
                </tr>
              </c:forEach>
            </table>
          </c:if>
          <c:if test="${empty affiliateBean.checkDetailsList}">
            <div class="label">None Transaction Yet</div>
          </c:if>
        </div>
      </div>
    </div>
  </s:layout-component>

</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myChecksLink').style.fontWeight = "bold";
  };
</script>