<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
    <s:layout-component name="heading">Affiliate Offer Program</s:layout-component>

    <s:layout-component name="lhsContent">
        <jsp:include page="../myaccount-nav.jsp"/>
    </s:layout-component>

    <s:layout-component name="rhsContent">
        <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction">
            <s:errors/>
            <div>
                <div class="basicInformation" style="font-size:0.8em;">
                    <h4 class="strikeline"> Affiliate Program</h4>

                    <div class="row">
                        <s:label class="rowLabel" name="Affiliate Code"/>
                        <label class="rowText">${affiliateBean.affiliate.code}</label>
                    </div>

                    <div class="clear"></div>
                    <div style="margin-top:5px"></div>

                    <div class="row">
                        <s:label class="rowLabel" name="Affiliate Offer"/>
                        <s:label name="${affiliateBean.offer.description}" class="rowText"/>
                    </div>

                    <div class="clear"></div>
                    <div style="margin-top:5px"></div>

                    <div class="row">
                        <s:label class="rowLabel" name="Weekly Coupon Download Limit"/>
                        <s:label name="${affiliateBean.affiliate.weeklyCouponLimit}" class="rowText"/>
                    </div>

                    <div class="clear"></div>
                    <div style="margin-top:5px"></div>

                    <div class="row">
                        <s:label class="rowLabel" name="Affiliate Retention Time"/>
                        <s:label name="${affiliateBean.affiliate.validDays}" class="rowText"/>
                    </div>

                </div>

                <div class="clear"></div>
                <div style="margin-top:10px"></div>

                <div style="float: right; font-size: 0.7em; width: 65%; margin-top: 10px;">
                    <s:submit name="generateAffiliateCoupons" value="Download Affiliate Coupons" class="button_orange"/>
                </div>

                <div class="clear"></div>
                <div style="margin-top:10px"></div>

                <s:hidden name="affiliate" value="${affiliateBean.affiliate}"/>
            </div>
        </s:form>
    </s:layout-component>
</s:layout-render>

<script type="text/javascript">
    window.onload = function() {
        document.getElementById('affiliateShowCouponScreen').style.fontWeight = "bold";
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