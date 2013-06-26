<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html>
  <head>
    <title>${pageTitle} | HealthKart.com</title>
    <link href="<hk:vhostCss/>/css/fb510.css" rel="stylesheet" type="text/css">
    <s:layout-component name="analytics"><s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="" allCategories="" brand="" isProd="<%=false%>"/></s:layout-component>
    <script type="text/javascript" src="${pageContext.request.contextPath}/otherScripts/jQuery-1.4.2.js"></script>
    <s:layout-component name="htmlHead"/>
  </head>
  <body id="${bodyId}">
  <div id="fb-root"></div>

  <script type="text/javascript">
  // Load the SDK Asynchronously
  (function(d){
     var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
     js = d.createElement('script'); js.id = id; js.async = true;
     js.src = "//connect.facebook.net/en_US/all.js";
     d.getElementsByTagName('head')[0].appendChild(js);
   }(document));

  </script>
  <s:layout-component name="modal"/>
    <div class="container_8">
      <s:layout-component name="header">
      </s:layout-component>
    </div>
    <div class="clear"></div>

  <div class="content_cont">
    <div class="container_8" id="container">

      <h1><s:layout-component name="heading"/></h1><%-- adds a gap when no h1 defined--%>
      <h2 class="sub_heading"><s:layout-component name="subHeading"/></h2>

      <s:layout-component name="stripesError"><s:errors/></s:layout-component>

      <div class="alert messages"><s:messages key="generalMessages"/></div>

      <s:layout-component name="content"/>
      <div class="clear"></div>

    </div>
  </div>
  <div class="footer_cont">
    <div class="container_8">
      <s:layout-component name="footer">
      </s:layout-component>
    </div>
  </div>
  </body>
  </html>
</s:layout-definition>