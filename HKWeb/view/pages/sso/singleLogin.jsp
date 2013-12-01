<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp"%>
<%@ page import="com.hk.web.HealthkartResponse" %>
<c:set var="httpPath" value="${pageContext.request.contextPath}" />
<s:useActionBean beanclass="com.hk.web.action.core.auth.SSOLoginAction" var="sla" />
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Healthkart Login</title>
    <link type="text/css" rel="stylesheet" href="${httpPath}/css/single_login.css" />
    <script type="text/javascript" src="${httpPath}/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="${httpPath}/js/jquery.hkCommonPlugins.js"></script>
</head>
<body>
<div class='loaderContainer'>
    <div class='loaderCircle'></div>
</div>
<div id="container">
    <div class="logo_block">
        <img src="${httpPath}/images/hkp-logo.jpg" alt="HealthKart Plus Logo" border="0">
        <img src="${httpPath}/images/hk_logo.png" alt="HealthKart Logo" border="0">
        <div id="newToHealthkart" class="rightcontent">
            New to HealthKart? &nbsp; <a id="newCustomer" class="yellow-button">CREATE AN ACCOUNT</a>
        </div>
        <div id="alreadyHaveAccount" class="rightcontent" style="display: none;">
           Already have an account? &nbsp;<a id="oldCustomer" class="yellow-button">SIGN IN HERE</a>
        </div>
    </div>

    <div>
        <div id="loginbox" class="formbox">


            <div class="formcontainer">
                <div class="content">
                    <h3 class="heading">
                        SIGN IN
                    </h3>

                <c:choose>
                    <c:when test="${!empty sla.hkApiUser}">
                         You can sign in to ${sla.hkApiUser.name} using your healthkart.com account.
                    </c:when>
                    <c:otherwise>
                         Please enter the following details to sign into your account
                    </c:otherwise>
                </c:choose>
                </div>
                <div class="errors"></div>
            <s:form id="signin_form" method="post" event="login" beanclass="com.hk.web.action.core.auth.SSOLoginAction" class="form">

                Email <br/>
                <s:text type="text" placeholder="Email" name="userLogin" id="loginName" tabindex="10" size="50" maxlength="50" value=""/>
                <br/>
                Password <br/>
                <s:text type="password" placeholder="Password" name="password" id="password"  tabindex="20" size="50" maxlength="50" value=""/>
                <br/>

                <s:submit tabindex="30" name="login" id="login"  class="submit" value="Sign in"/>
                <s:link beanclass="com.hk.web.action.core.user.SSOForgotPasswordAction" id="forgotPasswordLink" tabindex="40" class="yellow-button">forgot password?</s:link>
                <s:hidden name="login" value="login"/>
                <s:hidden name="apiKey" value="${sla.apiKey}"/>
                <s:hidden name="redirectUrl" value="${sla.redirectUrl}"/>

            </s:form>

            </div>
            <div class="clear"></div>
            <div class="footer">
            <span class="bottom">
                <c:choose>
                <c:when test="${!empty sla.hkApiUser}">
                    <a class="terms" href="${sla.hkApiUser.terms}">Terms of Use</a>
                </c:when>
                <c:otherwise>
                    <a class="terms" href="${httpPath}/pages/termsAndConditions.jsp">Terms of Use</a>
                </c:otherwise>
            </c:choose>| &copy; 2013 HealthKart.com</span>
            </div>
        </div>
        <div id="signupbox" class="formbox" style="display: none;">
            <div class="formcontainer">
            <div class="content">
                <h3 class="heading">
                    REGISTRATION
                </h3>
                Please enter the following details to create a new account in healthkart
            </div>

            <div class="errors"></div>
            <s:form id="signup_form" method="post" event="signup"  beanclass="com.hk.web.action.core.auth.SSOLoginAction" class="form">

                Name <br/>
                <s:text placeholder="Name" name="userName" id="name" tabindex="10" size="50" maxlength="50" value=""/>
                <br/>
                Email <br/>
                <s:text placeholder="Login Email" name="userLogin" id="email" tabindex="20" size="50" maxlength="50" value=""/>
                <br/>
                Password <br/>
                <s:text type="password" placeholder="Password" name="password" id="signup_password" tabindex="30" size="50" maxlength="50" value=""/>
                <br/>
                Re-enter password <br/>
                <s:text type="password" placeholder="re-enter password" name="repeatPassword" id="passwordConfirm" tabindex="40" size="50" maxlength="50" value=""/>
                <br/>
                <s:hidden name="signup" value="signup"/>
                <s:hidden name="apiKey" value="${sla.apiKey}"/>
                <s:hidden name="redirectUrl" value="${sla.redirectUrl}"/>
                <div id="terms"><s:checkbox name="agreeToTerms" id="agreeToTerms"/> I agree to
                    <a href="${pageContext.request.contextPath}/pages/termsAndConditions.jsp" class="terms">terms and conditions</a>   &nbsp;
                    <s:submit tabindex="60" name="signup" id="signup" class="submit" value="Register"></s:submit>
                </div>

                <br/>

            </s:form>

            </div>
            <div class="clear"></div>
            <div class="footer">
            <span class="bottom">    <c:choose>
                <c:when test="${!empty sla.hkApiUser.terms}">
                    <a class="terms" href="${sla.hkApiUser.terms}">Terms of Use</a>
                </c:when>
                <c:otherwise>
                    <a class="terms" href="${pageContext.request.contextPath}/pages/termsAndConditions.jsp">Terms of Use</a>
                </c:otherwise>
            </c:choose> | &copy; 2013 HealthKart.com</span>
            </div>
        </div>
        <div id="forgetbox" class="formbox" style="display: none;">
            <div class="formcontainer">
                <div class="content">
                    <h3 class="heading">
                        FORGOT PASSWORD?
                    </h3>
                    Enter your email address and we will mail you a link to reset your password
                </div>
            <div class="errors"></div>
            <s:form class="form" id="forget_form" method="post" event="login" beanclass="com.hk.web.action.core.auth.SSOLoginAction">
                Email <br/>
                <s:text type="text" placeholder="Email" name="userLogin" id="forgetEmail" tabindex="10" size="50" maxlength="50" value=""/>

                <br/>

                <s:submit tabindex="30" id="sendResetLink" name="sendResetLink" value="Reset My Password" class="submit" ></s:submit>
                <s:hidden name="sendResetLink" value="sendResetLink"/>

            </s:form>

            <div class="content">Once you reset your password, you can &nbsp;<a id="oldCustomerLogin" class="yellow-button">sign in here</a></div>   <br/>
                </div>
            <div class="clear"></div>
            <div class="footer">
            <span class="bottom">
                <c:choose>
                    <c:when test="${!empty sla.hkApiUser.terms}">
                        <a class="terms" href="${sla.hkApiUser.terms}">Terms of Use</a>
                    </c:when>
                    <c:otherwise>
                        <a class="terms" href="${pageContext.request.contextPath}/pages/termsAndConditions.jsp">Terms of Use</a>
                    </c:otherwise>
                </c:choose>

                
                | &copy; 2013 HealthKart.com</span>
            </div>
        </div>
    </div>
</div>



<script type="text/javascript">
$(document).ready(function()
{

    function LoadingShadow()
    {
        this.ele =$('.loaderContainer');
    }
    LoadingShadow.prototype.show = function(){

        $(this.ele).css({'height':$(window).height()});
        $(this.ele).show();
    }
    LoadingShadow.prototype.hide = function(){

        $(this.ele).hide();
    }

    var loaderInstance=new LoadingShadow();

    function errorTooltip(elementId, message, leftSide){
        if(leftSide==undefined){leftSide=false;}
        var element=$("#"+elementId+"");
        var left,top,tooltipContent;

        //22 is based on the tooltip padding, margin and font-size 5+5+12+20=42
        var top=element.offset().top+(element.height()-42)/2;
        if(leftSide){
            left =element.position().left;
            tooltipContent="<div class='error-tooltip-left'";
            tooltipContent+="style='top:"+top+"px; display:none;'>";
            tooltipContent+=message;
            tooltipContent+="<div class='pointer-left'> ";
            tooltipContent+="<div class='inner-pointer-left'> </div></div>";
            tooltipContent+="</div>";
            element.before(tooltipContent);
            //just now added prev
            prev=element.prev();
            left=left-prev.outerWidth()-17;
            prev.css({'left':left});
            $('.error-tooltip-left').fadeIn();
        }else{
            left =element.position().left+element.outerWidth();
            tooltipContent="<div class='error-tooltip'";
            tooltipContent+="style='left:"+left+"px; top:"+top+"px;display:none;'>";
            tooltipContent+="<div class='pointer'> ";
            tooltipContent+="<div class='inner-pointer'> </div></div>";
            tooltipContent+=message;
            tooltipContent+="</div>";
            element.after(tooltipContent);
            $('.error-tooltip').fadeIn();
        }
    }


    function removeErrorTooltips(){
        $('.error-tooltip').fadeOut();
        $('.error-tooltip-left').fadeOut();
        $('.error-tooltip').remove();
        $('.error-tooltip-left').remove();
    }

    $('#newCustomer').click(function(e){
        e.preventDefault();
        removeErrorTooltips();
        $('#loginbox').fadeOut();
        $('#newToHealthkart').fadeOut();
        $('#forgetbox').fadeOut();
        $('#oldCustomer').attr('checked',false);
        setTimeout(function () {
            $('#signupbox').fadeIn();
            $('#alreadyHaveAccount').fadeIn();
        }, 450);
        hideServerErrors();
    });

    $('#oldCustomer').click(function(e){
        e.preventDefault();
        removeErrorTooltips();
        $('#signupbox').fadeOut();
        $('#alreadyHaveAccount').fadeOut();
        $('#forgetbox').fadeOut();
        setTimeout(function () {
            $('#loginbox').fadeIn();
            $('#newToHealthkart').fadeIn();
        }, 450);
        hideServerErrors();
    });

    $('#oldCustomerLogin').click(function(e){
        e.preventDefault();
        removeErrorTooltips();
        $('#signupbox').fadeOut();
        $('#forgetbox').fadeOut();
        setTimeout(function () {
            $('#loginbox').fadeIn();
            $('#newToHealthkart').fadeIn();
        }, 450);
        hideServerErrors();
    });

    $('#forgotPasswordLink').click(function(e){
        e.preventDefault();
        removeErrorTooltips();
        $('#signupbox').fadeOut();
        $('#loginbox').fadeOut();
        $('#newToHealthkart').fadeOut();
        setTimeout(function () {
            $('#forgetbox').fadeIn();
        }, 450);
        hideServerErrors();
    });

    $('.terms').click(function(e){
        e.preventDefault();
        popupWindow($(this).attr('href'), 'Terms and Conditions', 'width=340,height=340,resizable=1, scrollbars=1, toolbar=1, status=1');
    });

    function popupWindow(url, name, options){
        var ContextWindow = window.open(url,name,options);
        ContextWindow.focus();
        return false;
    }

    var errorflag = false;

    function checkloginName(){

        var loginName;
        var err1=0;
        loginName = $("#loginName").val().length;
        $("#loginName").empty();

        var email_var=$("#loginName").val();
        var rege = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,6})$/g
        if(!rege.test(email_var))
        {
            errorflag=true;
            errorTooltip('loginName', "please enter a valid email address");
        }
        else
        {
            $("#loginNameError").html('');
        }

    }

    function checkpassword(){
        var password;var err=0;
        password = $("#password").val().length;
        $("#passwordError").empty();
        if (password == 0){
            errorflag=true;
            errorTooltip('password', "password cannot be empty");
        }
        else
        {
            $("#passwordError").html('');
        }
    }

    function checkname(){
        var name_signup;var err=0;
        name_signup = $("#name").val().length;
        if (name_signup == 0){ errorflag=true;
            errorTooltip('name',"Enter a name");
        }
    }
    function checkemail(){

        var email;
        var err1=0;
        email_length = $("#email").val().length;
        var email_var=$("#email").val();
        var rege = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        if(!rege.test(email_var) || email_length==0){ errorflag=true;

            errorTooltip('email',"Please enter a valid email address");
        }

    }

    function checkForgetEmail(){

        var email;
        var err1=0;
        email_length = $("#forgetEmail").val().length;
        var email_var=$("#forgetEmail").val();
        var rege = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        if(!rege.test(email_var) || email_length==0){ errorflag=true;
            errorTooltip('forgetEmail',"Please enter a valid email address");
        }

    }
    function checksignup_password(){
        var signup_password;var err=0;
        signup_password = $("#signup_password").val().length;
        if (signup_password == 0){ errorflag=true;
            errorTooltip('signup_password',"Password cannot be left empty")
        }

    }
    function checkpasswordConfirm(){
        var passwordConfirm;
        var err=0;
        passwordConfirm = $("#passwordConfirm").val().length;
        if (passwordConfirm == 0){
            errorflag=true;
            errorTooltip('passwordConfirm',"Retype the password");
        }else{
            checkrpassword();
        }
    }

    function checkrpassword(){
        var npass= $("#signup_password").val();
        var rpass = $('#passwordConfirm').val();
        var checkstring;

        checkstring=npass.substr(0,rpass.length);

        if( checkstring!= rpass || rpass.length!=npass.length) {
            errorflag=true;
            errorTooltip('passwordConfirm', "Passwords don't match. Please retype the correct password");
            return false;
        }
    }

    function checkTermsAcceptance(){
        if(!$('#agreeToTerms').is(':checked')){
            errorflag=true;

            errorTooltip('agreeToTerms', "You have to agree to our terms and conditions", true);
        }
    }

    function enableSubmitButtons(){
        $('.submit').removeAttr("disabled");
        $('.submit').removeClass("disabled_button");
    }

    function disableSubmitButtons(){
        $('.submit').attr("disabled", "disabled");
        $('.submit').addClass("disabled_button");
    }

    $('#login').click(function(e) {
        hideServerErrors();
        removeErrorTooltips();
        errorflag = false;
        checkloginName();
        checkpassword()
        if (errorflag == false) {
            disableSubmitButtons();
            loaderInstance.show();
            $('#signin_form').submit();
        }
        else
        {
            e.preventDefault();
            enableSubmitButtons();
        }
    });

    $('#sendResetLink').click(function(e) {
        hideServerErrors();
        removeErrorTooltips();
        errorflag = false;
        checkForgetEmail();
        if (errorflag == false) {
            disableSubmitButtons();
            loaderInstance.show();
            $('#forget_form').submit();
        }
        else
        {
            e.preventDefault();
            enableSubmitButtons();
        }
    });

    $('#signup').click(function(e) {
        hideServerErrors();
        removeErrorTooltips();
        errorflag = false;
        checkname();
        checkemail();
        checksignup_password();
        checkpasswordConfirm();
        checkTermsAcceptance();
        if (errorflag == false)
        {
            disableSubmitButtons();
            loaderInstance.show();
            $('#signup_form').submit();
        }
        else
        {
            e.preventDefault();
            enableSubmitButtons();
        }
    });

    function showServerErrors(res) {
        hideServerErrors();
        $('.errors').append("<div class='errorList'>"+res.message+"</div>");
        $('.errors').each( function() {
            $(this).fadeIn();
        });
    }

    function hideServerErrors(){
        $('.errors').each( function() {
            $(this).empty();
        });
    }

    function _formSubmitSuccess(res) {
        enableSubmitButtons();
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
             loaderInstance.hide();
        }else if(res.code == '<%=HealthkartResponse.STATUS_REDIRECT%>'){
            if(res.data==null){
                window.location.href=res.message;
            }else{
                window.location.href=res.data.redirectUrl;
            }
        }else if(res.code == '<%=HealthkartResponse.STATUS_ERROR%>'){
            showServerErrors(res);
            loaderInstance.hide();
        }
    }

    $('.form').ajaxForm({dataType: 'json', success: _formSubmitSuccess});

});
</script>
</body>
</html>
