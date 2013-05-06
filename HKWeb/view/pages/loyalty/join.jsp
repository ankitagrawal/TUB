<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<stripes:layout-render name="/pages/loyalty/info/layoutStatic.jsp">
	<stripes:layout-component name="contents">
				<div class="slidingBanner"></div>
				<div class="mainContent">
					<div class="threePartition">
						<div class="grid_5 earnStellar">Earn stellar rewards</div>
						<div class="grid_5 signUpPart">
							<s:form beanclass="com.hk.web.action.core.loyaltypg.JoinLoyaltyProgramAction">
							<div class="joinNow">Join Now!</div>
							<div class="signUp">
								<select class="name" name="gender" placeholder="Gender">
									<option value="MALE">MALE</option>
									<option value="FEMALE">FEMALE</option>
								</select> 
								<s:text class="name" placeholder="Date Of Birth"  name="dob" id="datepicker">
								</s:text>
								<div class="continueButton">
									<s:submit name="continueProgram" value="Join"
										 style= "background: none; font-size:14px; border:none;"/>
								</div>
							</div>
							</s:form>
						</div>
							<div class="grid_5 exploreStellar">Explore stellar</div>
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