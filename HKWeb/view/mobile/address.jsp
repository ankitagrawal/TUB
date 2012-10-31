<!DOCTYPE html>
<html>
<head>
<%@ include file='header.jsp'%>
</head>
<body>
	<div data-role="page" id=address class="type-home">
		<div data-role=header>
			<%@ include file='menuHeader.jsp'%>
			<%@ include file='menuNavBtn.jsp'%>
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
							id='defaultView'> Existing Addresses </a></li>
						<li><a href="#"
							style='border-top-color: transparent; border-width: 0;'
							id='registarView' class='gNav ui-corner-tr ui-corner-br'> New
								Address </a></li>

					</ul>
				</div>
			</div>
			<div id='defaultViewContent' class=viewContent>
				<div id="address-header" style='clear: both; margin-bottom: 2px'>
					<h2 style='color: #2186C6; font-weight: normal'>Use one of
						your saved Addresses</h2>
				</div>
				<div style='margin-top: 14px' id="address-block"></div>
			</div>
			<br /> <br />
			<div id='registarViewContent' class='viewContent hide'>
				<div id="newaddress-header">
					<h2 style='color: #2186C6; font-weight: normal'>Add new
						address</h2>
					<!-- block -->
					<div id="newaddress-block" style='margin: 0px 10px'>
						<form autocomplete="on" id='address_registration'
							action="mAddress/addAddress">
							<p style="padding: 10px 0 10px 0">
								<!--label for=usernamesignup>Name <span>*</span></label-->
								<input type="text" placeholder="Enter your name" name="name"
									id="address_name" class='ui-corner-all'>
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label for="passwordsignup">City <span>*</span></label-->
								<input type="text" placeholder="Enter line1" name="line1"
									id="address_line1">
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label for="passwordsignup">State <span>*</span></label-->
								<input type="text" placeholder="Enter line2" name="line2"
									id="address_line2">
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label for="passwordsignup">PIN Code <span>*</span></label-->
								<input type="text" placeholder="Enter city" name="city"
									id="address_city">
							</p>
							<p style="padding: 10px 0 10px 0">
								<!--label for="passwordsignup">Phone/Mobile <span>*</span></label-->
								<input type="text" placeholder="Enter state" name="state"
									id="address_state">
							</p>

							<p style="padding: 10px 0 10px 0">

								<input type="text" placeholder="Enter pincode" name="pin"
									id="address_pin">
							</p>
							<p style="padding: 10px 0 10px 0">

								<input type="text" placeholder="Enter phone number" name="phone"
									id="address_phone">
							</p>
							<p class="signin button">
								<br>
								<!--input type="submit" value="Use this & Continue"-->
								<input type='submit' data-role=button id="use_address_button"
									value="Use this & Continue" />
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
		<%@include file='template/address-templates.jsp'%>
		<script type="text/javascript" src='${httpPath}/js/address.js'>
			
		</script>
	</div>
</body>
</html>
