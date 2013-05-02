<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
		<div class="mainContainer embedMarginTop100">
			<div class="container_16 clearfix">
				<div class="slidingBanner"></div>
				<div class="mainContent">
					<div class="threePartition">
						<div class="grid_5 earnStellar">Earn stellar rewards</div>
						<div class="grid_5 signUpPart">
							<s:form beanclass="com.hk.web.action.core.loyaltypg.JoinLoyaltyProgramAction">
							<div class="joinNow">Join Now!</div>
							<div class="signUp">
								<select class="name" name="gender">
									<option value="MALE">MALE</option>
									<option value="FEMALE">FEMALE</option>
								</select> 
								<input class="name" placeholder="DOB" type="date" name="dob">
								<div class="continueButton">
									<s:submit name="continueProgram" value="continue"/>
								</div>
							</div>
							</s:form>
							<div class="grid_5 exploreStellar">Explore stellar</div>
						</div>
					</div>
				</div>
			</div>
	</stripes:layout-component>
</stripes:layout-render>