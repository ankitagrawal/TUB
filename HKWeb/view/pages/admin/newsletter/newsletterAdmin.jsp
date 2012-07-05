<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Newsletter Home</s:layout-component>
  <s:layout-component name="content">
    <div>
      <div style="float:left;width:20%">
        <s:link beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterAdmin" event="getReadMeFileContents"
                id="readMeLink">ReadMe.txt for Email Campaign Amazon</s:link>
        <br/><br/>
        <s:link
            beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction">Create New Email Campaign</s:link>
        <br/><br/>
        <s:link beanclass="com.hk.web.action.admin.newsletter.SendEmailNewsletterCampaign">
          Send Email Campaign and View Details 
        </s:link>
        <br/><br/>
        <s:link beanclass="com.hk.web.action.admin.marketing.SendWeMissYouEmailer">Send We Miss You Emailer</s:link>
      </div>
      <div style="float:right;width:80%">
        <div id="readMeContentsDiv"
             style="font-size: 0.8em; width: 95%; border: 1px solid; padding: 10px;"></div>
      </div>
    </div>
    <script type="text/javascript">
      $(document).ready(function() {
        $('#readMeContentsDiv').hide();
        $('#readMeLink').click(function() {
          $.getJSON($(this).attr('href'), _getReadMe);
          return false;
        });

        function _getReadMe(res) {
          var readMeContents;
          if (res.code == "<%=HealthkartResponse.STATUS_OK%>") {
            readMeContents = res.data.readMeContents;
            $('#readMeContentsDiv').html("<h5>READ ME.TXT</h5><br/>" + readMeContents);
            $('#readMeContentsDiv').show();
            $('#readMeLink').hide();
          }
        }
      });
    </script>
  </s:layout-component>
</s:layout-render>
