<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.affiliate.AffiliateAccountAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Address</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div>
    <h4 class="strikeline"> Basic Information</h4>
    <s:form beanclass="web.action.affiliate.AffiliateAccountAction">
      <s:errors/>
      <div class="label">Name</div>
      ${affiliateBean.affiliate.user.name}
      <div class="label">Your Affiliate code</div>
      ${affiliateBean.affiliate.code}
      <div class="label">Currently the money in your account is Rs.</div>
      <fmt:formatNumber value="${affiliateBean.affiliateAccountAmount}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
      </div>
      <div class="label">Website Name</div>
      <s:text name="affiliate.websiteName"/>
      <div style="margin-top: 50px;">
      <h4 class="strikeline">Payment Info</h4>

      <div class="label">Check in favour of</div>
      <s:text name="affiliate.checkInFavourOf"/>
      <div class="label">Pan Number</div>
      <s:text name="affiliate.panNo"/>
      <div style="float: right; font-size: 0.7em; width: 65%; margin-top: 10px;">
        <s:submit name="saveAffiliatePreferences" value="Save Changes" class="button_orange"/>
      </div>
      <s:hidden name="affiliate" value="${affiliateBean.affiliate}"/>
    </s:form>
    
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('affiliateAccountLink').style.fontWeight = "bold";
  };
</script>