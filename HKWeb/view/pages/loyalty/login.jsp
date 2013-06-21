<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Sign in &middot; Healthkart Loyalty Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">


    <!-- Le styles -->
    <link href="<hk:vhostCss/>/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

    </style>
  </head>

  <body>
	<script src="<hk:vhostJs/>/bootstrap/js/jquery.js"></script>
	<script src="<hk:vhostJs/>/bootstrap/js/bootstrap.js"></script>
	
    <div class="container" >
      <s:form class="form-signin" beanclass="com.hk.web.action.core.loyaltypg.SignInAction" method="post">
      	<div style="text-align: center;">
      	<h5 class="form-signin-heading"><img src="<hk:vhostImage/>/images/logo.png" alt="healthkart logo"><br/>Loyalty Program</h5>
      	</div>
        <input name="email" type="text" class="input-block-level" placeholder="Email address">
        <input name="password" type="password" class="input-block-level" placeholder="Password">
        <s:submit name="login" class="btn btn-large btn-primary" value="Sign In"></s:submit>
      </s:form>

    </div> <!-- /container -->
  </body>
</html>
