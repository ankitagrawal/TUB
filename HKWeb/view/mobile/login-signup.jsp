<!DOCTYPE html>
<html>
<head>
<%@ include file='header.jsp'%>

</head>
<body>
	<div data-role="page" id=loginSignup class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtnSrch.jsp'%>
		</div>
		<div data-role="content" style='height: auto'>

			<div
				style='background-color: #319aff; padding: 10px; margin-bottom: 10px'>
				<div data-role="navbar">
					<ul>
						<li style=''><a href="#"
							class="ui-corner-tl ui-corner-bl ui-btn-active gNav"
							data-theme=''
							style='border-color: transparent; background-color: transparent; border-width: 0; border-right-width: 1px; border-right-color: #0f3d6f'
							id='defaultView'>Login</a></li>
						<li><a href="#"
							style='border-top-color: transparent; border-width: 0;'
							id='registarView' class='gNav ui-corner-tr ui-corner-br'>Create
								Account</a></li>

					</ul>
				</div>
			</div>
			<!-- end left -->


			<!-- start right pannel -->
			<div id='defaultViewContent' class=viewContent>
				<div id="login-header">
					<h2 style='color: #2186C6; font-weight: normal'>Login using an
						existing account</h2>
				</div>
				<div id="signin-block" style='width: 95%; margin: 0px auto'>
					<form autocomplete="on" id='authenticate' action="mLogin/login"
						method="post">
						<p style="padding: 10px 0 10px 0">
							<!--label for=username> Email</label-->
							<input type="text" placeholder="Enter your Email address"
								name="email" id="username">
						</p>

						<p style="padding: 5px 0 10px 0">
							<!--label class="youpasswd" for=password>Password </label-->
							<input type="password" placeholder="Enter your Password"
								name="password" id="password">
						</p>

						<p class="keeplogin" style='padding-bottom: 10px'>
							<label for="loginkeeping"><a href="javascript:void(0)"
								id='frgPwd'>Forgot password ?</a></label>
						</p>
						<input type=hidden name=protocol value='REST' />
						<p class="login button">
							<input type="submit" value="Login">
						</p>

					</form>
				</div>
			</div>
			<!-- end right pannel -->
			<div id='registarViewContent' class='viewContent hide'>
				<div id="login-header">
					<h2 style='color: #2186C6; font-weight: normal'>Create account</h2>
					<!-- block -->
					<div id="signin-block" style='width: 95%; margin: 0px auto'>
						<form autocomplete="on" id='registration' action="mSignup/signup">
							<p style="padding: 10px 0 10px 0">
								<!--label for=usernamesignup>Name <span>*</span></label-->
								<input type="text" placeholder="Enter your name" name="name"
									id="usernamesignup" class='ui-corner-all'>
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label class="youmail" for="emailsignup"> Email <span>*</span></label-->
								<input type="email" placeholder="Enter your Email address"
									name="email" id="emailsignup">
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label for="passwordsignup">Password <span>*</span></label-->
								<input type="password" placeholder="Enter your Password"
									required="required" name="password" id="passwordsignup">
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label for="passwordsignup_confirm">Confirm Password <span>*</span></label-->
								<input type="password" placeholder="Re-enter your Password"
									required="required" name="passwordsignup_confirm"
									id="passwordsignup_confirm">
							</p>
							<p class="keeplogin" id='accept'>
								<br> <input type="checkbox" data-role=none style='zoom: 2'
									value="loginkeeping" id="loginkeeping" name="loginkeeping">
								<label for="loginkeeping">Agree to <a data-role=none
									href="${httpPath}/tnc.jsp" target='_blank'>terms and
										conditions *</a></label>

							</p>
							<p class="signin button">
								<br> <input type="submit" value="SignUp">
							</p>
							<br> <br>
						</form>
					</div>

					<!-- block end -->
				</div>
				<div style="clear: both"></div>
			</div>

		</div>
		<script type="text/javascript">
$('#loginSignup').bind('pagebeforeshow',function(){
	<%if (session.getAttribute("userName") != null) {%>
			$.mobile.changePage('${httpPath}/home.jsp');
	<%}%>
})
		</script>

		<script type="text/javascript" src='${httpPath}/js/login-signup.js'></script>
	</div>
</body>
</html>
