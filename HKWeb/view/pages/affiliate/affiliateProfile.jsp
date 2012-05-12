<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Your Address</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div class="basicInformation" style="font-size:0.8em;">
    <h4 class="strikeline"> Basic Information</h4>

    <div style="margin-top:15px"></div>
    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction">
      <s:errors/>

      <div class="row">
        <s:label class="rowLabel" name="Name"/>
        <s:label name="${affiliateBean.affiliate.user.name}" class="rowText"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:5px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Affiliate Code"/>
        <s:label name="${affiliateBean.affiliate.code}" class="rowText"/>
      </div>

      <div class="clear"></div>
      <div style="margin-top:5px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Account Balance"/>
        <label class="rowText">
          Rs. <fmt:formatNumber value="${affiliateBean.affiliateAccountAmount}"
                                pattern="<%=FormatUtils.currencyFormatPattern%>"/>
        </label>
      </div>

      <div class="clear"></div>
      <div style="margin-top:5px"></div>

      <div class="row">
        <s:label class="rowLabel" name="Website Name"/>
        <s:text name="affiliate.websiteName" class="rowText"/>
      </div>
      <%--<div class="label">Name</div>--%>
      <%--${affiliateBean.affiliate.user.name}--%>
      <%--<div class="label">Your Affiliate code</div>--%>
      <%--${affiliateBean.affiliate.code}--%>
      <%--<div class="label">Currently the money in your account is Rs.</div>--%>
      <%--<fmt:formatNumber value="${affiliateBean.affiliateAccountAmount}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>--%>
      <%--</div>--%>
      <%--<div class="label">Website Name</div>--%>
      <%--<s:text name="affiliate.websiteName"/>--%>
      </div>

      <div class="clear"></div>
      <div style="margin-top:10px"></div>

      <div class="paymentInfo" style="font-size:0.8em;">
        <h4 class="strikeline">Payment Info</h4>

        <div style="margin-top:15px"></div>

        <div class="row">
          <s:label class="rowLabel" name="Cheque in favour of"/>
          <s:text name="affiliate.checkInFavourOf" class="rowText"/>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
          <s:label class="rowLabel" name="Pan Number"/>
          <s:text name="affiliate.panNo" class="rowText"/>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

          <%--<div class="label">Check in favour of</div>--%>
          <%--<s:text name="affiliate.checkInFavourOf"/>--%>
          <%--<div class="label">Pan Number</div>--%>
          <%--<s:text name="affiliate.panNo"/>--%>
      </div>

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
<style type="text/css">
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
    width: 150px;
    height: 24px;
    margin-top: 5px;
    font-weight: bold;
  }

  .rowText {
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 30px;
    font: inherit;
  }
</style>