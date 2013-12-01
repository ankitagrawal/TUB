<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.PublishOnFBAction" var="actionBean"/>
<div class="modal_header">
  <div class="jqDrag" style="padding: 5px 3px;">Share and Earn!</div>

  <a href="#" class="modal_close jqmClose" style=" margin-top: 2px;"><img src="<hk:vhostImage/>/images/spacer.gif" width="21" height="21"/></a>
</div>
<div>
  <img src="${pageContext.request.contextPath}/images/facebook/fb-share-and-earn.jpg" alt="Facebook - share and earn">
</div>
<div style="display:inline;float:left;margin-left:10px;margin-top:10px;">
  <img src="${pageContext.request.contextPath}/images/facebook/no_thanks_button.jpg" alt="No, Thanks" onclick="$('#publishOnFBWindow').jqmHide();" onmouseover="this.style.cursor='pointer'">
</div>
<div style="display:inline;float:right; margin-right:10px;margin-bottom:20px;">
  <img src="${pageContext.request.contextPath}/images/facebook/share_button.jpg" alt="Share" id="share_button" onmouseover="this.style.cursor='pointer'">
  <c:set var="topOrderedVariant" value="${hk:getTopDealVariant(actionBean.order)}"/>
  <s:layout-render name="/layouts/embed/_fbShareOrder.jsp" variantId="${topOrderedVariant.id}" orderId="${actionBean.order.id}"></s:layout-render>
</div>
