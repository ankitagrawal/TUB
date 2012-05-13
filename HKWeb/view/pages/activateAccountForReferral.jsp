

<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<s:layout-render name="/layouts/default.jsp" pageTitle="Welcome">
  <s:layout-component name="htmlHead">
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/jcarousellite_1.0.1.pack.js"></script>
  </s:layout-component>

  <s:layout-component name="heading">Activate your account</s:layout-component>

  <s:layout-component name="rhsContent">
    <span id="ajaxLoader" style="display:none;"><img src="<hk:vhostImage/>/images/ajax-loader.gif"/></span>
    <shiro:hasAnyRoles name='<%=RoleConstants.HK_UNVERIFIED+","+RoleConstants.HK_DEACTIVATED%>'>
      <p class="lrg">You need to verify your email to be eligible for the referral program.</p>

      <p class="lrg">Please click on the activation link sent to you via e-mail when signing up.</p>

      <p><strong>If you haven't received the mail,
        <s:link beanclass="com.hk.web.action.core.user.ResendAccountActivationLinkAction" event="pre" class="resendActivationEmailLink">click here to resend it.</s:link>
      </strong>
        <span class="emailSendMessage alert" style="display: none;"></span>
        
      </p>

      <p style="display:none;" class="emailNotReceived">
        If you do not receive this email, please check your spam/bulk folder.
        <br/>Write to us at info@healthkart.com if you face problems.
      </p>

    </shiro:hasAnyRoles>

    <br/>

    <div class="buttons" align="left"><s:link beanclass="com.hk.web.action.HomeAction" event="pre">
     Continue Shopping
    </s:link></div>

    <div style="height:250px"></div>
    
    <script type="text/javascript">

      <%-- Re-Send Activation Link --%>
        $('.resendActivationEmailLink').click(function() {

          var clickedLink = $(this);
          var clickedP = clickedLink.parents('p');
          clickedP.find('.emailSendMessage').html($('#ajaxLoader').html()).show();
          $.getJSON(clickedLink.attr('href'), function(res) {
            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
              clickedP.find('.emailSendMessage').html(res.data.message).show();
              $('.emailNotReceived').show();
            }
          });
          return false;
        });

    </script>
  </s:layout-component>

</s:layout-render>
