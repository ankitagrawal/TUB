<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.RequestCallbackAction" var="sdcActionBean" event="pre"/>
<s:layout-render name="/layouts/modal.jsp">
    <s:layout-component name="heading">
        Forgot Password
    </s:layout-component>

    <s:layout-component name="content">
        <div id="forgot-password-errors"></div>

        <div class="msg red" style="text-align: center; padding: 2px 0 2px 0; font-size: 1em;"></div>

        <div>
            <s:form beanclass="com.hk.web.action.core.user.ForgotPasswordAction" id="forgotPassowrdForm">
                <div style="text-align: center; padding: 5px 0 5px 0; font-size: 1em;">
                    Enter your email address:
                    <s:text id="forgotEmail" name="email"/>
                    <br/> <br/>
                    <span class="special" style="font-size: 12px;">(your password will be emailed to you at this address)</span>
                </div>
                <br>
                <%--
                        <div style="text-align: left; padding: 5px 0 5px 0; font-size: 1em;">Tell us you are human:
                          <%
                            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(HealthkartConstants.recaptchaPublicKey, HealthkartConstants.recaptchaPrivateKey, false);
                            out.print(captcha.createRecaptchaHtml(request.getParameter("error"), null));
                          %>
                        </div>--%>

                <s:submit id="forgotPassword" class="button_orange" name="forgotPassword" value="Send Password"/>
            </s:form>

        </div>
        <script type="text/javascript">
            function _sendNewPasswordEmail(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    $('#forgotPasswordWindow .msg').html(res.message);
                    $('#forgotPassword').hide();
//          $('#forgotPasswordWindow').jqmShow();
                } else if (res.code == '<%=HealthkartResponse.STATUS_ERROR%>') {
                    $('#forgot-password-errors').html(getErrorHtmlFromJsonResponse(res));
                }
            }

            $('#forgotPassowrdForm').ajaxForm({dataType: 'json', success: _sendNewPasswordEmail});
        </script>
    </s:layout-component>

</s:layout-render>
