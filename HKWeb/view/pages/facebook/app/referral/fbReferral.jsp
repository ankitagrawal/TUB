<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/fbDefault.jsp">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      window.fbAsyncInit = function() {
        FB.init({
          appId: '132261210189189',
          status: true,
          cookie: true,
          xfbml: true
        });

        FB.getLoginStatus(function(response) {
          if (response.session) {
            // logged in and connected user, someone you know
            alert('logged in');
          } else {
            // no user session available, someone you dont know
            alert('no session available');
          }
        });
      };
      (function() {
        var e = document.createElement('script'); e.async = true;
        e.src = document.location.protocol +
          '//connect.facebook.net/en_US/all.js';
        document.getElementById('fb-root').appendChild(e);
      }());
    </script>
  </s:layout-component>

  <s:layout-component name="heading">Referral</s:layout-component>
  <s:layout-component name="content">
    <div id="fb-root"></div>
    
  </s:layout-component>
</s:layout-render>
