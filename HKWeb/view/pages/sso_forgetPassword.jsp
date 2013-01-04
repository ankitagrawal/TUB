
<%@ include file="/includes/_taglibInclude.jsp"%>
<c:set var="httpPath" value="${pageContext.request.contextPath}" />
<s:useActionBean beanclass="com.hk.web.action.core.auth.SingleLoginAction" var="sla" />
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Healthkart Login</title>
    <link type="text/css" rel="stylesheet" href="${httpPath}/css/single_login.css" />
</head>
<body>
<div id="container">
    <div id="tabbox">
        <img src="${httpPath}/images/hk_bar_SSO.jpg" alt="top_stripe" border="0" height="10" width="705">
    </div>
    <div class="logo_block">
        <a title="go to healthkart home" href="/"><img src="${httpPath}/images/hk_plus_logo.png" alt="Healthkart Logo" border="0" height="40" width="140"></a>
        <a title="go to healthkart home" href="/"><img src="${httpPath}/images/hk_logo.png" alt="Healthkart Plus Logo" border="0" height="25" width="140"></a>


    </div>
    <s:errors/>
    <div id="panel">
        <h1 class="login-header">Welcome to the Healthkart Network</h1>
        <div id="loginbox">
            <s:form id="signin_form" method="post" event="login" beanclass="com.hk.web.action.core.auth.SingleLoginAction">

                <s:text type="text" placeholder="enter your username" name="userLogin" id="loginName" tabindex="10" size="50" maxlength="50" value=""/>
                <p class="errors_class" id="loginNameError" ></p>
                <s:text type="password" placeholder="enter password" name="password" id="password"  tabindex="20" size="50" maxlength="50" value=""/>
                <p class="errors_class" id="passwordError" ></p>
                <s:hidden name="logintabselected" value="true"></s:hidden>
                <s:hidden name="login" value="login"/>
                <s:hidden name="apiKey" value="${sla.apiKey}"/>
                <s:hidden name="redirectUrl" value="${sla.redirectUrl}"/>
                <div class="login_block">
                    <s:submit tabindex="30" name="login" id="login"  class="submit" value="Log in"/>
                    <a id="signin_forgotPasswordLink" tabindex="40" class="forgot_link" href="">Forgot password?</a>
                </div>

            </s:form>
        </div>
    </div>
    <div class="footer"> <img src="${httpPath}/images/hk_bar_SSO.jpg" alt="bottom_stripe" border="0" height="10" width="705"> </div>
</div>


<script type="text/javascript" src="${httpPath}/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript">
$(document).ready(function()
{

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
            errorflag=1;
            $("#loginNameError").html("Username is not valid.");
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
            errorflag=1;
            $("#passwordError").html("Password cannot be empty.");
        }
        else
        {
            $("#passwordError").html('');
        }
    }

    function checkname(){
        var name_signup;var err=0;
        name_signup = $("#name").val().length;
        $("#nameError").empty();
        if (name_signup == 0){ errorflag=1;
            $("#nameError").html("Name cannot be empty.");}
        else
        {
            $("#nameError").html('');
        }
    }
    function checkemail(){

        var email;
        var err1=0;
        email = $("#email").val().length;
        $("#email").empty();

        var email_var=$("#email").val();
        var rege = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        if(!rege.test(email_var)){ errorflag=1;
            $("#emailError").html("Username is not valid.");}
        else
        {
            $("#emailError").html('');
        }

    }
    function checksignup_password(){
        var signup_password;var err=0;
        signup_password = $("#signup_password").val().length;
        $("#signup_passwordError").empty();
        if (signup_password == 0){ errorflag=1;
            $("#signup_passwordError").html("Password cannot be empty.");}
        else
        {
            $("#signup_passwordError").html('');
        }
    }
    function checkpasswordConfirm(){
        var passwordConfirm;var err=0;
        passwordConfirm = $("#passwordConfirm").val().length;
        $("#passwordConfirmError").empty();
        if (passwordConfirm == 0){ errorflag=1;
            $("#passwordConfirmError").html("Re-enter password cannot be empty.");}
        else
        {
            $("#signup_passwordError").html('');
        }
    }



    $("#loginName").blur(function()
    {
        checkloginName();
    });
    $("#password").blur(function()
    {
        checkpassword();
    });

    $("#name").blur(function()
    {
        checkname();
    });

    $("#email").blur(function()
    {
        checkemail();

    });
    $("#signup_password").blur(function()
    {
        checksignup_password();

    });
    $("#passwordConfirm").blur(function()
    {
        checkpasswordblur();

    });
    $("#passwordConfirm").keyup(function()
    {
        checkpasswordblur();

    });
    function checkpasswordblur(){

        var npass = $('#signup_password').val();
        var rpass = $('#passwordConfirm').val();
        checkrpassword(npass,rpass,1);
    }
    function checkrpasswordkeyup(){
        var npass = $('#signup_password').val();
        var rpass = $('#passwordConfirm').val();
        checkrpassword(npass,rpass,0);

    }
    function checkrpassword(npass,rpass,checkfull){

        if (checkfull == 1) {
            errorflag = false;
            $("#passwordConfirmError").html("Re-enter password cannot be empty.");
            if(errorflag == true)
            { return false;}
            else
                var checkstring=npass.substr(0,npass.length);

        }
        else {
            var checkstring=npass.substr(0,rpass.length);
        }

        if( checkstring!= rpass) {
            errorflag=true;
            $("#passwordConfirmError").html("Re-enter password must match with the password");
            return false;
        } else {
            $("#passwordConfirmError").html('');

        }
    }

    $('#login').click(function(e) {

        errorflag = false;
        checkloginName();
        checkpassword()
        if (errorflag == false) {

            $('#login').attr("disabled", "disabled");
            $('#login').addClass("disabled_button");
            $('#signin_form').submit();

        }
        else
        {
            e.preventDefault();
            $('#login').removeAttr("disabled");
            $('#login').removeClass("disabled_button");

        }


    });
});
</script>
</body>
</html>
