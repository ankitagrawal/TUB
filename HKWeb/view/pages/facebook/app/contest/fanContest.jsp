<%@ page import="com.hk.constants.FbConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.facebook.app.contest.FanContestAction" var="fanContestBean"/>

<s:layout-render name="/layouts/fbDefault.jsp">
  <s:layout-component name="htmlHead">
    <script type="text/javascript" src="http://connect.facebook.net/en_US/all.js"></script>
    <script type="text/javascript">
      $(document).ready(function() {

        FB.init({
          appId  : '<%=FbConstants.contestAppId%>',
          status : true, // check login status
          cookie : true, // enable cookies to allow the server to access the session
          xfbml  : true  // parse XFBML
        });

        FB.getLoginStatus(function(response) {
          if (response.session) {
            alert('logged in user');
            isFan();
          } else {
            alert('not logged in');

            FB.login(function(response) {
              if (response.session) {
                // user successfully logged in
                alert('success');
                isFan();
              } else {
                // user cancelled login
                alert('fail');
              }
            });
          }
        });

      });

      function isFan() {
        FB.api({
          method: 'fql.query',
          query: 'SELECT target_id FROM connection WHERE source_id = ${fanContestBean.uid} AND target_id = <%=FbConstants.fbFanPageId%>'
        }, function(response) {
          if (response[0]) {
            var msg = '';
            for (var key in response[0]) {
              msg += key + ' = ' + response[0][key] + '\n';
            }
            alert(msg);
          }
        });
      }
    </script>
  </s:layout-component>

  <s:layout-component name="heading">Fan Contest App</s:layout-component>

  <s:layout-component name="content">
    <div id="fb-root"></div>

    <p>
      access token: ${fanContestBean.access_token}<br/>
      UID : ${fanContestBean.uid}
    </p>

  </s:layout-component>

</s:layout-render>
