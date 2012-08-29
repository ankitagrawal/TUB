<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="org.joda.time.DateTime" %>
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
    <s:layout-component name="steps_content">
        <div class='current_step_content step3'>
        <jsp:include
                page="/includes/checkoutNotice.jsp"/>

        <div class='pre'>
            <h4>Your total billable amount is <strong class='num'> <fmt:formatNumber
                    value="${order.amount}" type="currency"
                    currencySymbol="Rs "/> </strong></h4>
            <h6>If you have any trouble during the payment process, call our
                helpline number <strong class='red'> 0124 - 4551616 </strong></h6>
        </div>

        <div class="alert messages" style="font-size: 14px; color: red">
            <s:errors/><s:messages key="generalMessages"/></div>

        <div class='payment_container'>
            <div style="display: none;"><s:link
                    beanclass="com.hk.web.action.admin.SetInCookieAction"
                    id="setInCookieLink"/></div>
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
                        <s:hidden name="order" value="${order.id}"/>
                        <s:hidden name="bankId" value="70"/>
                        <p><label><s:radio name="paymentMode" value="<%=defaultGateway%>"/>VISA
                            &nbsp;</label> <img src="<hk:vhostImage/>/images/gateway/visa.jpg" height="30px">
                        </p>

                        <p><label><s:radio name="paymentMode" value="80"/>
                            MasterCard &nbsp;</label> <img
                                src="<hk:vhostImage/>/images/gateway/mastercard.jpg" height="30px">
                        </p>

                        <p><label><s:radio name="paymentMode" value="90"/>
                            Maestro &nbsp;</label> <img
                                src="<hk:vhostImage/>/images/gateway/maestro.gif" height="30px">
                        </p>

                        <p><label><s:radio name="paymentMode" value="80"/>
                            Citrus (Faster Checkout) &nbsp;</label> <img
                                src="<hk:vhostImage/>/images/gateway/citrus.png" height="30px">
                        </p>

                        <div style="float: right; width: 90%;"><s:submit
                                name="prepay" value="Make Payment >" class="button"
                                />
                        </div>
                    </s:form></div>
                    <div id="tabs_content3" class="tab_content" style="display: none;">
                        <s:form beanclass="com.hk.web.action.core.payment.RegisterOnlinePaymentAction"
                                method="post">
                            <s:hidden name="order" value="${order.id}"/>
                            <s:hidden name="paymentMode" value="<%=defaultGateway%>"/>

                            <div style="float: left; margin-left: 20px; line-height: 21px;">
                                <div class="paymentBox">
                                    <table width="100%">
                                        <c:forEach items="${paymentModeBean.bankList}" var="bank"
                                                   varStatus="idx">
                                            <c:if test="${idx.index%2 == 0}">
                                                <tr>
                                            </c:if>
                                            <td class="col"><s:radio name="bankId" value="${bank.id}"/>${bank.bankName}
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

    <s:layout-component name="analytics">
        <jsp:include page="/includes/_analytics.jsp"/>
    </s:layout-component>

    <s:layout-component name="zopim">
        <jsp:include page="/includes/_zopim.jsp"/>
    </s:layout-component>

</s:layout-render>

