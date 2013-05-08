<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ include file="/includes/_taglibInclude.jsp"%>
<%@ include file="/layouts/_userData.jsp"%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>

<s:layout-render name="/layouts/b2bLayout.jsp">

	<div>Attachments:</div>
	<div>
		<s:errors field="attachments" />
	</div>
	<div class="left">
			<div>
				<s:file name="loyaltyProducts" />
			</div>
	</div>
	<div class="left">
		<s:submit name="upload" value="Upload" />
	</div>
</s:layout-render>
