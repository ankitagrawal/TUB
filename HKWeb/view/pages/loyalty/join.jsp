<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<stripes:layout-render name="/pages/loyalty/info/layoutStatic.jsp">
	<stripes:layout-component name="contents">
		<div class="container_16 clearfix">
			<div style="width: 100%; font-size: 38px; padding-bottom: 15px; margin-left: 100px;"
				class="embedMargin119">Welcome to healthkart stellar !!</div>
				<div class="topText" id="message1"
				style="float: left; margin-left: 100px; width: 680px;">
						<div class="grid_5 signUpPart">
							<s:form beanclass="com.hk.web.action.core.loyaltypg.JoinLoyaltyProgramAction">
							<div class="joinNow">One more step before we take you to catalog.</div>
							<br><br>
							<div>We'd like to know you better. Would you please fill in some more information for us?
							</div>
							<br>
								<div class="signUp">
								<div >Name</div> 
								<s:text class="name" placeholder="Name"  name="name" >
								</s:text>
								<div>Gender: </div>
								<select class="name" name="gender" placeholder="Gender">
									<option value="MALE">MALE</option>
									<option value="FEMALE">FEMALE</option>
								</select>
							<!-- 	<div style="width: 40%;">Date of Birth</div> 
								<s:text class="name" placeholder="Date Of Birth"  name="dob" id="datepicker">
								</s:text>
							 -->	<div >
							 <br>
									<s:submit name="continueProgram" value="TRY OUR CATALOG OUT?" class="btn"/>
								</div>
							</div>
							</s:form>
						</div>
				</div>
		</div>
				
	<script type="text/javascript">
		$(document).ready(function(){

			    $( "#datepicker" ).datepicker({
			      changeMonth: true,
			      changeYear: true,
				  minDate: "-90Y", maxDate: "-15Y" 
				});
		});
		
	
	</script>
	</stripes:layout-component>
</stripes:layout-render>