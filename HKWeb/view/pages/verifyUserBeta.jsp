<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.web.action.core.user.VerifyUserAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.user.VerifyUserAction" var="verifyUserBean"/>

<c:set var="outcome_alreadyVerified" value="<%=VerifyUserAction.outcome_alreadyVerified%>"/>
<c:set var="outcome_invalidLink" value="<%=VerifyUserAction.outcome_invalidLink%>"/>
<c:set var="outcome_linkExpired" value="<%=VerifyUserAction.outcome_linkExpired%>"/>
<c:set var="outcome_success" value="<%=VerifyUserAction.outcome_success%>"/>

<s:layout-render name="/layouts/defaultBeta.jsp" pageTitle="Account Verification">
    <s:layout-component name="heading">
            <h1>Account Verification </h1>
    </s:layout-component>
    <s:layout-component name="rhsContent">
    <div>
        <c:if test="${verifyUserBean.outcome == outcome_alreadyVerified}">
            <div class="alert-cntnr">
                <span class="icn-success "></span>
                <div class="alert-messages txt-cntr">
                    <div>
                        Your account is already active.
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${verifyUserBean.outcome == outcome_invalidLink}">
            <div class="err-cntnr">
                <span class="icn-warning-small"></span>

                <div>
                    Invalid Link.
                </div>



            </div>

        </c:if>
        <c:if test="${verifyUserBean.outcome == outcome_linkExpired}">
            <div class="err-cntnr">
                <span class="icn-warning-small"></span>
                <div>
                    Your activation link has expired. Please visit your account section to resend the activation e-mail
                </div>
            </div>

            <shiro:hasRole name="<%=RoleConstants.HK_USER%>">
                <div class="err-cntnr txt-cntr">
                    <span class="icn-warning-small"></span>

                    <div>
                        <p>Your account has already been activated!</p>
                    </div>

                </div>
            </shiro:hasRole>

            <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_NEEDS_ACTIVATION%>">
                <p>
                    You can request a fresh activation link by going to the
                    <s:link beanclass="com.hk.web.action.core.user.WelcomeAction"><strong>Activation
                        page</strong></s:link>.
                    If you are unable to receive the activation email then please check your spam/junk folder. For further help please write to us at info@healthkart.com
                </p>
            </shiro:hasAnyRoles>
        </c:if>
        <c:if test="${verifyUserBean.outcome == outcome_success}">
            <div class="alert-cntnr">
                <span class="icn-success "></span>

                <div class="alert-messages txt-cntr ">

                    <div>
                        Thanks! Your account has now been activated.
                    </div>


                </div>
            </div>
            <br/>

            <div class="btn btn-gray" style="display:inline-block" align="left"><s:link
                    beanclass="com.hk.web.action.core.cart.CartAction">PROCEED TO CART</s:link></div>
        </c:if>
        <br><br>
        <div class="btn btn-gray " style="display:inline-block" align="left"><s:link beanclass="com.hk.web.action.HomeAction" event="pre">
            &laquo; Go to home
        </s:link></div>


        <%--<shiro:notAuthenticated>

            <div class="btn btn-gray" align="left"><s:link beanclass="com.hk.web.action.core.auth.LoginAction" event="pre">
                Login
            </s:link></div>
        </shiro:notAuthenticated>
--%>
        <div style=""></div>

    </div>
    </s:layout-component>

</s:layout-render>
