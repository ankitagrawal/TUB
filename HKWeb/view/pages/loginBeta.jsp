<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.action.core.auth.LoginAction" %>
<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>
<c:set var="source_checkout" value="<%=LoginAction.SOURCE_CHECKOUT%>"/>
<%
    String vanillaForumUrl = (String) ServiceLocatorFactory.getProperty(Keys.Env.vanillaForumUrl);
    pageContext.setAttribute("vanillaForumUrl", vanillaForumUrl);
%>
<s:layout-render name="/layouts/loginLayoutBeta.jsp" pageTitle="Login to HealthKart.com">
    <s:layout-component name="checkoutStep">
        <div class='current_step_content' style="font-size: 14px; margin-top: 15px; padding-top: 0;">
            <div class='right' style="margin: 0 auto;float: none;">
                <h4 class="mrgn-b-20">
                    SIGN IN
                </h4>

                <div class='login'>
                    <s:form beanclass="com.hk.web.action.core.user.SignupAction" name="signup">
                      <input type="hidden" name="redirectUrl" value="${param['redirectUrl']}" />
                      <input type="hidden" id="signupEmail" name="signupEmail" value=""/>
                    </s:form>
                    <s:form beanclass="com.hk.web.action.core.auth.LoginAction" name="signin">
                        <s:errors/>

                        <fieldset class="mrgn-b-20">
                            <label class="" for="loginName">Email address</label>
                            <s:text class="signUpInputNew" name="email" tabindex="10" id="loginName"/>

                            <label class="radio mrgn-b-5">
                                <input type="radio" name="type" class="auto-adjust check-empty" value="new"/> I am a new
                                user
                            </label>

                            <label class="radio">
                                <input type="radio" name="type" class="auto-adjust check-empty" value="old"
                                       checked="checked"/> I am an
                                existing user, I have password
                            </label>
                            <br>

                            <label class="">Password</label>
                            <s:password class="signUpInputNew" name="password" tabindex="20"/>

                            <div style="float: left;">
                                <s:submit name="login" value="Sign In" class="btn btn-blue"/>
                                <s:submit name="createNew" class="btn btn-blue" style="display:none;" value="Create an account"/>

                            </div>
                            <div style="float: right;" name="forgot-link-cntnr">
                                <span class="icn icn-sqre-blue"></span>
                                <s:link beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPasswordLink"
                                        style="color: #0091d7;">forgot password</s:link>
                            </div>
                        </fieldset>
                        <s:hidden name="redirectUrl"
                                  value="${param['redirectUrl']}"/>
                        <s:hidden name="source"/>
                    </s:form>
                </div>
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="endScripts">
        <script type="text/javascript">
            $(document).ready(function () {
                $('[name=createNew]').click(function (event) {
                    var doSubmit;
                    var trgtForm = $('form[name=signup]');
                    var emailid = $('#loginName').val();
                    var errorMsg = '<div class="errorContainer">Please fix the following errors:</div><ol class="errorList"><li class="errorMessage">Email is a required field</li></ol>';
                    event.preventDefault();
                    console.log(emailid);
                    if (emailid == null || $.trim(emailid)   == '') {
                        $('.errorContainer').remove();
                        $('.errorList').remove();
                        $('form[name=signin]').prepend(errorMsg);
                        doSubmit = false;
                    }
                    else {
                        $('.errorContainer').remove();
                        $('.errorList').remove();
                        doSubmit = true;
                    }

                    if (doSubmit != false) {
                        $('#signupEmail').val(emailid);
                        trgtForm.submit();
                    }
                });
                function newView() {
                    $('[name=password]').attr("disabled", "disabled");
                    $('[name=createNew]').show();
                    $('[name=login]').hide();
                    $('[name=forgot-link-cntnr]').hide();


                }
                function oldView() {
                    $('[name=password]').removeAttr("disabled");
                    $('[name=createNew]').hide();
                    $('[name=login]').show();
                    $('[name=forgot-link-cntnr]').show();
                }
                $('[name=type]').change(function () {
                    if ($(this).val() == 'new') {
                        newView();
                    } else {
                        oldView();
                    }
                })

            });
        </script>
    </s:layout-component>
</s:layout-render>
