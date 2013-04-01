<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateMode" %>
<%@ page import="com.hk.constants.affiliate.EnumAffiliateType" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
<s:layout-component name="heading">Your Address</s:layout-component>
<%
    CategoryDao categoryDao = (CategoryDao) ServiceLocatorFactory.getService(CategoryDao.class);
    pageContext.setAttribute("categoryList", categoryDao.getPrimaryCategories());
%>

<s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>
</s:layout-component>

<s:layout-component name="rhsContent">
<s:form beanclass="com.hk.web.action.core.affiliate.AffiliateAccountAction">
    <s:errors/>
    <div>
    <shiro:hasRole name="<%=RoleConstants.HK_AFFILIATE_UNVERIFIED%>">
        <div class="prom yellow help" style="margin-bottom:20px; padding:5px;">
            <p class="lrg"><strong>Pending Affiliate Approval Account</strong><br/>
                To get it verified, please provide us with all the information in the form below</p>
        </div>
    </shiro:hasRole>
    <div class="basicInformation" style="font-size:0.8em;">
            <h4 class="strikeline"> Basic Information</h4>

            <div style="margin-top:15px"></div>

            <div class="row">
                <s:label class="rowLabel" name="Name"/>
                <s:label name="${affiliateBean.affiliate.user.name}" class="rowText"/>
            </div>

            <shiro:hasRole name="<%=RoleConstants.HK_AFFILIATE%>">

                <div class="clear"></div>
                <div style="margin-top:5px"></div>

                <div class="row">
                    <s:label class="rowLabel" name="Affiliate Code"/>
                    <label class="rowText">${affiliateBean.affiliate.code}</label>
                </div>

                <div class="clear"></div>
                <div style="margin-top:5px"></div>

                <div class="row">
                    <s:label class="rowLabel" name="Net Account Balance"/>
                    <label class="rowText">
                        Rs. <fmt:formatNumber value="${affiliateBean.affiliateAccountAmount}"
                                              pattern="<%=FormatUtils.currencyFormatPattern%>"/>
                    </label>
                </div>

                <div class="clear"></div>
                <div style="margin-top:5px"></div>

                <div class="row">
                    <s:label class="rowLabel" name="Payable Amount"/>
                    <label class="rowText">
                        Rs. <fmt:formatNumber value="${affiliateBean.affiliatePayableAmount}"
                                              pattern="<%=FormatUtils.currencyFormatPattern%>"/>
                    </label>
                </div>
            </shiro:hasRole>

            <div class="clear"></div>
            <div style="margin-top:5px"></div>

            <div class="row">
                <s:label class="rowLabel" name="Website Name"/>
                <s:text name="affiliate.websiteName" class="rowText"/>
            </div>

            <div class="clear"></div>
            <div style="margin-top:5px"></div>

            <div class="row">
                <s:label class="rowLabel" name="Affiliate Type"/>
                <s:select name="affiliate.affiliateType" class="rowText">
                    <option value="">Select -</option>
                    <c:forEach items="<%=EnumAffiliateType.getAllAffiliateTypes()%>" var="aType">
                        <s:option value="${aType.id}">${aType.name}</s:option>
                    </c:forEach>
                </s:select>
            </div>

            <div class="clear"></div>
            <div style="margin-top:5px"></div>

            <div class="row">
                <s:label class="rowLabel" name="Channel"/>
                <s:select name="affiliate.affiliateMode" class="rowText">
                    <option value="">Select -</option>
                    <c:forEach items="<%=EnumAffiliateMode.getAllAffiliateModes()%>" var="aMode">
                        <s:option value="${aMode.id}">${aMode.name}</s:option>
                    </c:forEach>
                </s:select>
            </div>

        </div>

        <div class="clear"></div>
        <div style="margin-top:10px"></div>

        <div class="contactInformation" style="width: 100%; margin-top: 5px; margin-bottom: 5px; float:left;">
            <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction">
                <h4 class="strikeline"> Contact Information</h4>

                <div style="margin-top: 10px"></div>

                <%--<c:set var="address" value="${affiliateBean.}"--%>

                <c:set var="addresses" value="${affiliateBean.affiliate.user.addresses}"/>

                <c:choose>
                    <c:when test="${!empty addresses}">
                        <c:choose>
                            <c:when test="${affiliateBean.affiliateDefaultAddress == null}">
                                <c:set var="address" value="${addresses[0]}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="address" value="${affiliateBean.affiliateDefaultAddress}"/>
                            </c:otherwise>
                        </c:choose>

                        <div>
                            <div style="float:left; font-size:small; width:70%">
                                <p>${address.line1} ${address.line2}</p>

                                <p>${address.city}</p>

                                <p>${address.state} ${address.pincode.pincode}</p>

                                <p>Phone: ${address.phone}</p>
                            </div>
                            <div style="float: right; font-size: 0.7em; margin-top:65px; margin-right:15px; width:20%">
                                <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction"
                                        event="manageAddresses"
                                        style="font-size:small; color:black; text-align:center;">View all addresses
                                </s:link>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div style="font-size: large;">&nbsp No contact information yet</div>
                        <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction" event="manageAddresses"
                                style="float:right; font-size:small; color:black; text-align:center;">Add Address
                        </s:link>
                    </c:otherwise>
                </c:choose>
            </s:form>
        </div>

        <div class="categoryInfo" style="font-size:0.8em;">
            <h4 class="strikeline">Business Modes</h4>

            <p>
                <strong>Select Categories</strong>
            </p>

            <div class="checkBoxList row">
                <c:forEach items="${categoryList}" var="category" varStatus="ctr">
                    <label>
                        <c:choose>
                            <c:when test="${hk:collectionContains(affiliateBean.categories, category)}">
                                <s:checkbox name="categories[${ctr.index}]" checked="checked"
                                            value="${category.name}"/>
                            </c:when>
                            <c:otherwise>
                                <s:checkbox name="categories[${ctr.index}]"
                                            value="${category.name}"/>
                            </c:otherwise>
                        </c:choose>
                         ${category.displayName}</label>
                    <br/>
                </c:forEach>
            </div>
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

            <div class="row">
                <s:label class="rowLabel" name="Bank Account Number"/>
                <s:text name="affiliate.bankAccountNumber" class="rowText"/>
            </div>

            <div class="clear"></div>
            <div style="margin-top:5px"></div>

            <div class="row">
                <label class="rowLabel">IFSC Code</label>
                <s:text name="affiliate.ifscCode" class="rowText"/>
            </div>

            <div class="clear"></div>
            <div style="margin-top:5px"></div>
        </div>

        <div class="clear"></div>
        <div style="margin-top:10px"></div>

        <div style="float: right; font-size: 0.7em; width: 65%; margin-top: 10px;">
            <s:submit name="saveAffiliatePreferences" value="Save Preferences" class="button_orange"/>
        </div>

        <div class="clear"></div>
        <div style="margin-top:10px"></div>

        <s:hidden name="affiliate" value="${affiliateBean.affiliate}"/>
    </div>
</s:form>
</s:layout-component>
</s:layout-render>

<script type="text/javascript">
    window.onload = function () {
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