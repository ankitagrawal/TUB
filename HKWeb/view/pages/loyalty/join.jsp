<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<link rel="stylesheet" type="text/css" media="all" href="<hk:vhostJs/>/pages/loyalty/resources/css/jsDatePick_ltr.min.css" />
<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/jsDatePick.min.1.3.js"></script>

<stripes:layout-render name="/pages/loyalty/info/layoutStatic.jsp">
	<stripes:layout-component name="contents">
		
		<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.JoinLoyaltyProgramAction" var="joinAction" />  
		
		<div class="container_16 clearfix">
			<div class="welcomeTitle" style="width: 650px; margin-left:180px;">Welcome to healthkart stellar !!</div>
	    <div class="grid_10 innerContent">
			<s:form beanclass="com.hk.web.action.core.loyaltypg.JoinLoyaltyProgramAction">
		      
			<div class="content">
				<p>One more step before we take you to the catalog.</p>
				<p>We’d like to know you better. Could you please fill in the fields and then, we’re good to go!</p>
			</div>

        <div class="inputFields">
          <div>Your name</div>
          <s:text class="welcomeInput" name="name" value="${joinAction.name}"></s:text>
        </div>

        <div class="inputFields">
          <div>Gender</div>
			<select class="welcomeInput" name="gender" >
				<option value="MALE">MALE</option>
				<option value="FEMALE">FEMALE</option>
			</select>
        </div>

        <div class="inputFields">
          <div>Date of Birth</div>
          <div class="welcomeInput noPadding">
          <input type="text" size="12" id="inputField" class="welcomeSelect" name="dob" placeholder="yyyy-mm-dd"/>
        	</div>
        </div>
		<div>By joining stellar program you agree to the
		 <a href="${pageContext.request.contextPath}/pages/loyalty/info/stellarTerms.jsp" class="blue makeCursor" target="_blank" >Terms and Conditions</a>
		  for the stellar.</div>
		<div><br><br><s:submit name="continueProgram" value="TRY OUR CATALOG OUT?" class="btn"/></div>
		</s:form>
	</div>
	</div>
	<script type="text/javascript">
	window.onload = function(){
		new JsDatePick({
			useMode:2,
			target:"inputField",
			yearsRange:[1978,2020],
			limitToToday:true,
			cellColorScheme:"armygreen",
			dateFormat:"%Y-%m-%d",
			weekStartDay:1
		});
	};
	 
	 
  	</script>
	</stripes:layout-component>
</stripes:layout-render>