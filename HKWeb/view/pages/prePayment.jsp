<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.payment.RegisterOnlinePaymentAction"
                 event="pre" var="paymentModeBean"/>
<c:set var="order" value="${paymentModeBean.order}"/>
<%
    Long defaultGateway = Long.parseLong((String) ServiceLocatorFactory.getProperty(Keys.Env.defaultGateway));
%>
<c:set var="orderDate" value="<%=new DateTime().toDate()%>"/>

<s:layout-render name="/layouts/checkoutLayout.jsp"
                 pageTitle="Payment Options">
    <s:layout-component name="htmlHead">
        <script type="text/javascript"
                src="${pageContext.request.contextPath}/otherScripts/jquery.session.js"></script>
    </s:layout-component>
<%--    <%
        DateTime dateTime = new DateTime();
        Date endOfOfferDate = new Date(new DateTime(2011, 12, 31, 23, 59, 59, 59).getMillis());
    %>

    <%
        if (dateTime.isBefore(endOfOfferDate.getTime())) {
    %>
    <div class="siteNotice">
        <div style="border-top: 5px solid #ff9999; border-bottom: 5px solid #ff6666; height: 24px; padding-top: 6px; font-size: 1em;">
            <strong>Pay Online Now</strong> : Another 2.5 % Cash Back
                <span style="background-color: #ccff00;">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%>
                </strong> remaining</span>
        </div>
    </div>
    <%
        }
    %>--%>
    <s:layout-component name="steps">
        <div class='steps_prepay'>
            <c:set var="codPaymentModeId" value="<%=EnumPaymentMode.COD.getId()%>"/>
            <c:if test="${order != null}">
                <c:if test="${order.payment.paymentMode.id == codPaymentModeId  && order.amount < 1500 && order.amount > 250}">
                    <div>
                        <img src="${pageContext.request.contextPath}/images/banners/pay_online_banner5.jpg">
                    </div>
                </c:if>
            </c:if>
        </div>
    </s:layout-component>
    <s:layout-component name="steps_content">
        <div class='current_step_content step3'>
        <jsp:include
                page="/includes/checkoutNotice.jsp"/>

        <div class='pre'>
            <h4>Your total billable amount is <strong class='num'> <fmt:formatNumber
                    value="${order.amount}" type="currency"
                    currencySymbol="Rs "/> </strong> for Order Id <strong>${order.gatewayOrderId}</strong></h4>
            <h6>If you have any trouble during the payment process, call our
                helpline number <strong class='red'> 0124 - 4616444 </strong></h6>
        </div>

        <div class="alert messages" style="font-size: 14px; color: red">
            <s:errors/><s:messages key="generalMessages"/></div>

        <div class='payment_container'>
            <div class='outer'>
                <div class='left_controls tabs'>
                    <ul>
                        <li class='selected' id="tab1">Credit/Debit Cards</li>
                        <li id="tab3">Internet Banking</li>
                    </ul>
                </div>
                <div class='right_content'>
                    <div id="tabs_content1" class="tab_content"><s:form
                            beanclass="com.hk.web.action.core.payment.RegisterOnlinePaymentAction" method="post">
                        <s:hidden name="order" value="${order.id}" />
                        <s:hidden name="paymentMode" value="<%=defaultGateway%>" />

                        <c:forEach items="${paymentModeBean.cardIssuers}" var="cardIssuer">
                            <p><label><s:radio name="issuer" value="${cardIssuer.id}"/>${cardIssuer.name}
                                &nbsp;</label> <img src="<hk:vhostImage/>/images/gateway/${cardIssuer.name}.jpg" height="30px" alt="gateway image">
                            </p>
                        </c:forEach>

                        <div style="float: right; width: 90%;"><s:submit
                                name="prepay" value="Make Payment >" class="button"
                                />
                        </div>
                    </s:form></div>
                    <div id="tabs_content3" class="tab_content" style="display: none;">
                        <s:form beanclass="com.hk.web.action.core.payment.RegisterOnlinePaymentAction"
                                method="post">
                            <s:hidden name="order" value="${order.id}"/>
                            <s:hidden name="paymentMode" value="<%=defaultGateway%>" />

                            <div style="float: left; margin-left: 20px; line-height: 21px;">
                                <div class="paymentBox">
                                    <table width="100%">
                                        <c:forEach items="${paymentModeBean.bankIssuers}" var="bankIssuer"
                                                   varStatus="idx">
                                            <c:if test="${idx.index%2 == 0}">
                                                <tr>
                                            </c:if>
                                            <td class="col"><s:radio name="issuer" value="${bankIssuer.id}" />${bankIssuer.name}
                                            </td>
                                            <c:if test="${idx.index%2 == 1 || idx.last}">
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </table>
                                    <div class="floatfix"></div>
                                </div>
                            </div>
                            <div style="float: right; width: 90%;"><s:submit
                                    name="prepay" value="Make Payment >" class="button makePayment"/>
                            </div>
                        </s:form></div>
                    <script type="text/javascript">
                        $(document).ready(function () {
                            $('.tab_content').hide();
                            $('.tab_content').first().show();
                            $('.tabs ul li').click(function () {
                                $('.tabs ul li').removeClass('selected');
                                $(this).addClass('selected');
                                var selected = $(this).attr('id').replace('tab', 'tabs_content');
                                $.session("selected-tab", $(this).attr('id'));
                                $('.tab_content').hide();
                                $('#' + selected).fadeIn(200);

                            });

                            if ($.session("selected-tab")) {
                                var sTab = $.session("selected-tab");
                                $('.tabs ul li').removeClass('selected');
                                $('#' + sTab).addClass('selected');
                                var selected = $('#' + sTab).attr('id').replace('tab', 'tabs_content');
                                $('.tab_content').hide();
                                $('#' + selected).fadeIn(200);
                            }

                            $('.makePayment').click(function disablePaymentButton() {
                                $(this).css("display", "none");
                            });
                        });
                    </script>
                    <div class='floatfix'></div>

                </div>
            </div>
        </div>
    </s:layout-component>

</s:layout-render>

