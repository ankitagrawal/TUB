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
                    <div style="margin-top:10px"></div>

                    <div class="row">
                        <s:label class="rowLabel" name="Affiliate Type"/>
                        <option value="">Select -</option>
                        <s:select name="affiliateType">
                            <c:forEach items="<%=EnumAffiliateType.getAllAffiliateTypes()%>" var="aType">
                                <s:option value="${aType.id}">${aType.name}</s:option>
                            </c:forEach>
                        </s:select>
                    </div>

                    <div class="clear"></div>
                    <div style="margin-top:10px"></div>

                    <div class="row">
                        <s:label class="rowLabel" name="Channel"/>
                        <s:select name="affiliateMode">
                            <option value="">Select -</option>
                            <c:forEach items="<%=EnumAffiliateMode.getAllAffiliateModes()%>" var="aMode">
                                <s:option value="${aMode.id}">${aMode.name}</s:option>
                            </c:forEach>
                        </s:select>
                    </div>

                </div>

                <div class="clear"></div>
                <div style="margin-top:10px"></div>

                <div class="categoryInfo" style="font-size:0.8em;">
                    <h4 class="strikeline">Business Modes</h4>

                    <br/><s:label class="rowLabel" name="Select Categories"/><br/><br/>

                    <div class="checkBoxList">
                        <c:forEach items="${categoryList}" var="category" varStatus="ctr">
                            <label><s:checkbox name="categories[${ctr.index}]"
                                               value="${category.name}"/> ${category.displayName}</label>
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
                        <s:label class="rowLabel" name="IFSC Code"/>
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