<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/loginLayout.jsp">

    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            function forgotPasswordPopup() {
                $('#forgotEmail').val($('#loginName').val());
                $('#forgotPasswordWindow').jqmShow();
            }
            function _sendNewPasswordEmail(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    $('#forgotPasswordWindow .msg').html(res.message);
                    $('#forgotPasswordWindow').jqmShow();
                } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                    $('#forgot-password-errors').html(getErrorHtmlFromJsonResponse(res));
                }
            }
            $(document).ready(function () {
                $('#forgotPassowrdForm').ajaxForm({dataType:'json', success:_sendNewPasswordEmail});
                $('#forgotPasswordWindow').jqm();
            });

        </script>
    </s:layout-component>
    <s:layout-component name="modal">
        <div class="jqmWindow" id="forgotPasswordWindow">
            <s:layout-render name="/layouts/modal.jsp">
                <s:layout-component name="heading">Forgot your password?</s:layout-component>
                <s:layout-component name="content">
                    <div id="forgot-password-errors"></div>

                    <div class="msg" style="text-align: center; padding: 2px 0 2px 0; font-size: 1em;"></div>

                    <div>
                        <s:form beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPassowrdForm">
                            <div style="text-align: center; padding: 5px 0 5px 0; font-size: 1em;">
                                Enter your email address:
                                <s:text id="forgotEmail" name="email"/>
                                <br/> <br/>
                                <span class="special" style="font-size: 12px;">(your password will be emailed to you at this address)</span>
                            </div>
                            <br>

                            <s:submit class="button_orange" name="forgotPassword" value="Send Password"/>
                        </s:form>

                    </div>
                </s:layout-component>
            </s:layout-render>
        </div>
    </s:layout-component>

    <s:layout-component name="checkoutStep">
        <div class='current_step_content login_page'>
            <s:errors/>
            <div class='left'>
                <h3>
                    First Time Visitor??<br> Become an Affiliate Now!!
                </h3>

                <div class='signup'>
                    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateAction">

                        <div class='label'>Name <span class='aster' title="this field is required">*</span></div>
                        <s:text name="name" placeholder="Enter your name"/>
                        <div class='label'>Email <span class='aster' title="this field is required">*</span></div>
                        <s:text name="email" placeholder="Your Email"/>
                        <div class='label'>Password <span class='aster' title="this field is required">*</span></div>
                        <s:password name="password" placeholder="Give it a safe password"/>
                        <div class='label'>Confirm Password <span class='aster' title="this field is required">*</span>
                        </div>
                        <s:password name="passwordConfirm" placeholder="Reenter the password"/>
                        <div class="label">Website Name<span class='aster' title="this field is required">*</span></div>
                        <s:text name="websiteName" placeholder="Your Domain Name"/>
                        <div class='label'>
                            <s:checkbox name="agreeToTerms"/>Agree to
                            <a href="${pageContext.request.contextPath}/pages/affiliate/affiliateTnC.jsp">terms and
                                conditions</a>
                            <span class='aster' title="this field is required">*</span>
                        </div>
                        <s:submit name="signup" value="Signup" class="button"/>
                    </s:form>
                </div>
            </div>
            <div class='or'>
                or
            </div>
            <div class='right'>
                <h3>
                    Already Healthkart User??
                    <br>Apply using an existing account!!
                </h3>

                <div class='login'>
                    <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateAction">

                        <div class="label">Registered Email</div>
                        <s:text name="email" id="loginName"/>
                        <div class="label">HK Password</div>
                        <s:password name="password"/>
                        <div class="label">Website Name<span class='aster' title="this field is required">*</span></div>
                        <s:text name="websiteName" placeholder="Your Domain Name"/>
                        <div class="label" style="margin-top:2px;margin-bottom:10px;"><s:checkbox name="rememberMe"/>&nbsp;Remember
                            Me
                        </div>
                        <a href="#" onclick="forgotPasswordPopup();">Forgot password?</a>

                        <div>
                            <s:submit name="loginExisting" value="Apply Now" class="button"/>
                        </div>
                    </s:form>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
