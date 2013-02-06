<%@ page import="com.akube.framework.util.BaseUtils"%>
<%@ page import="com.hk.constants.core.HealthkartConstants"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page import="com.hk.taglibs.Functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
<stripes:layout-component name="contents">
	<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.LoyaltyCatalogAction" var="lca" />
	<span class="muted" style="font-size:20px;">Following badges can be earned by a user: </span>
	<br>
	<ul style="margin-top:20px;">
		<c:forEach items="${lca.badgeList}" var="badge">
			<li><strong>${badge.badgeName}: </strong> ${badge.loyaltyPercentage}</li>
		</c:forEach>
	</ul>


</stripes:layout-component>
</stripes:layout-render>

