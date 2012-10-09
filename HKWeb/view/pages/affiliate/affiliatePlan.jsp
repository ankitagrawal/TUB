<%@ page import="com.hk.domain.affiliate.AffiliateStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction" var="paymentAction"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="content">
    <h2>
        ${paymentAction.affiliate.user.name}
    </h2>
    <br/>

    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliatePaymentAction">
      <s:errors/>
        <div class="row">
            <label class="rowLabel">Affiliate Code</label>
            <label class="rowText">${paymentAction.affiliate.code}</label>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
            <label class="rowLabel">Affiliate Mode</label>
            <label class="rowText">${paymentAction.affiliate.affiliateMode}</label>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
            <label class="rowLabel">Affiliate Type</label>
            <label class="rowText">${paymentAction.affiliate.affiliateType}</label>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
            <label class="rowLabel">Affiliate Status</label>
            <s:select name="affiliate.affiliateStatus" value="${paymentAction.affiliate.affiliateStatus.id}">
                <c:forEach items="<%=EnumAffiliateStatus.getAllAffiliateStatus()%>" var="affiliateStatus">
                    <s:option value="${affiliateStatus.id}">${affiliateStatus.name}</s:option>
                </c:forEach>
            </s:select>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
            <label class="rowLabel">Affiliate Api Key</label>
            <label class="rowText">${paymentAction.affiliate.apiKey}</label>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
            <label class="rowLabel">Valid Days</label>
            <s:text name="affiliate.validDays" value="${paymentAction.affiliate.validDays}"/>
        </div>

        <div class="row">
            <label class="rowLabel">Affiliate Offer</label>
            <label class="rowText">${paymentAction.affiliate.offer.description}</label>
            <s:text name="offer" value="${paymentAction.affiliate.offer.id}"/>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>

        <div class="row">
            <label class="rowLabel">Weekly Coupon Limit</label>
            <s:text name="affiliate.weeklyCouponLimit" value="${paymentAction.affiliate.weeklyCouponLimit}"/>
        </div>

        <div class="clear"></div>
        <div style="margin-top:5px"></div>
      <table>
      <thead>
      <tr>
        <th>Affiliate Category</th>
        <th>Overview</th>
        <th>Commission 1st Transaction</th>
        <th>Commission Latter Transaction</th>
      </tr>
      </thead>
      <c:forEach items="${paymentAction.affiliateCategoryCommissionList}" var="affiliateCategoryCommission" varStatus="ctr">
        <s:hidden name="affiliateCategoryCommissionList[${ctr.index}].id" value="${affiliateCategoryCommission.id}"/>
        <s:hidden name="affiliateCategoryCommissionList[${ctr.index}].affiliate" value="${affiliateCategoryCommission.affiliate}"/>
        <s:hidden name="affiliateCategoryCommissionList[${ctr.index}].affiliateCategory" value="${affiliateCategoryCommission.affiliateCategory.affiliateCategoryName}"/>
        <tr>
          <td>${affiliateCategoryCommission.affiliateCategory.affiliateCategoryName}</td>
          <td>${affiliateCategoryCommission.affiliateCategory.overview}</td>
          <td>
            <s:text name="affiliateCategoryCommissionList[${ctr.index}].commissionFirstTime" value="${affiliateCategoryCommission.commissionFirstTime}"/></td>
          <td>
            <s:text name="affiliateCategoryCommissionList[${ctr.index}].commissionLatterTime" value="${affiliateCategoryCommission.commissionLatterTime}"/></td>
        </tr>
      </c:forEach>
      </table>
        <s:hidden name="affiliate" value="${paymentAction.affiliate.id}"/>
        <div class="buttons"><s:submit name="savePlan" value="Save Changes"/></div>
    </s:form>
  </s:layout-component>
</s:layout-render>