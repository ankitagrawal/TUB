<!DOCTYPE html>
<html>
<head>
<%@ include file='header.jsp'%>

</head>
<body>
	<div data-role="page" id=contactUs class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtn.jsp'%>
		</div>
		<div data-role="content" style='height: auto'>


			<div id='registarViewContent' class='viewContent'>
				<div id="login-header">
					<h2 style='color: #2186C6; font-weight: normal'>Contact Us</h2>
					<!-- block -->
					<div id="signin-block" style='margin: 0px 10px'>
						<form autocomplete="on" id='registration'
							action="/webservices/users/registration">
							<p style="padding: 10px 0 10px 0">
								<!--label for=usernamesignup>Name <span>*</span></label-->
								<input type="text" placeholder="Enter your name" name="username"
									id="usernamesignup" class='ui-corner-all'>
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label class="youmail" for="emailsignup"> Address Line 1 <span>*</span></label-->
								<input type="email" placeholder="Enter your Email address"
									name="emailaddress" id="emailsignup">
							</p>

							<p style="padding: 10px 0 10px 0">
								<!--label for="passwordsignup">Phone/Mobile <span>*</span></label-->
								<textarea type="text" placeholder="Your Query/Feedback"
									name="password" id="passwordsignup"></textarea>
							</p>
							<p class="signin button">
								<br>
								<!--input type="submit" value="Use this & Continue"-->
								<a href='orderSummary.jsp' data-role=button>Send Your Query</a>
							</p>
							<br> <br>
						</form>
					</div>

					<!-- block end -->
				</div>
				<div style="clear: both"></div>
			</div>
			<%@ include file='menuFooter.jsp'%>
		</div>
	
		<script>
			$('#contactUs')
					.bind(
							'pageshow',
							function() {

								$("#usernamesignup")
										.validate(
												{
													expression : "if (VAL=='') return false; else return true;",
													message : "Please enter name.",
												});

								$("#emailsignup")
										.validate(
												{
													expression : "if (VAL.match(/^[^\\W][a-zA-Z0-9\\_\\-\\.]+([a-zA-Z0-9\\_\\-\\.]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$/)) return true; else return false;",
													message : "Please enter valid Email id",
												});

								$("#passwordsignup")
										.validate(
												{
													expression : "if (VAL==''||VAL.length<6) return false; else return true;",
													message : "Please enter Valid Password. Minimum size 6.",
												});

								var kpass = $("#passwordsignup").val();

								$("#passwordsignup_confirm")
										.validate(
												{
													expression : "if ((VAL == jQuery('#passwordsignup').val()) && VAL) return true; else return false;",
													message : "Confirm password field doesn't match the password field",
												});

								$('#accept')
										.validate(
												{
													expression : "if ( $('#loginkeeping').is(':checked')) return true; else return false;",
													message : "Please Read & Accept Terms and Condition",
												});

								$('.gNav').click(function(e) {

									var ele = e.currentTarget;

									var eleId = $(ele).attr('id');
									$('.viewContent').hide();
									$('#' + eleId + 'Content').show();

								});

							});
		</script>
	</div>
</body>
</html>
