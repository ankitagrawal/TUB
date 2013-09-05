<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.PasswordResetAction" var="resetBean" event="pre"/>
<s:layout-render name="/layouts/loginLayoutBeta.jsp" pageTitle="Reset Password | Healthkart.com">
    <s:layout-component name="checkoutStep">
        <div class='current_step_content' style="font-size: 14px; margin-top: 15px; padding-top: 0;">
            <div class='left' style="margin: 0 auto;float: none;">
                <h4 class="mrgn-b-20">
                    CREATE A NEW PASSWORD
                </h4>
                <div class='reset-pwd'>
                    <s:form beanclass="com.hk.web.action.core.user.PasswordResetAction" method="post">
                        <s:errors/>
                        <s:hidden name="token" value="${resetBean.token}"/>
                        <fieldset class="mrgn-b-20">
                            <label>Password <span class='aster' title="this field is required">*</span></label>
                            <s:password name="password" class="signUpInputNew"/>
                            <label>Confirm Password <span class='aster' title="this field is required">*</span></label>
                            <s:password class="signUpInputNew" name="passwordConfirm"/>
                        <s:submit style="float: left;" name="reset" value="Save Changes" class="btn btn-blue"/>
                        </fieldset>
                    </s:form>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
