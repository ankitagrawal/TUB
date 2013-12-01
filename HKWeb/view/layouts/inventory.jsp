<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html>
  <head>
    <title>${pageTitle} | HealthKart.com</title>
    <jsp:include page="/includes/_style.jsp"/>
    <link href="${pageContext.request.contextPath}/css/fluid.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <s:layout-component name="htmlHead"/>
    <style type="text/css">
      .messages li {font-size: 40px; background: red; color: white; font-size: 30px;}
      .error li {font-size: 40px; background: red; color: white; font-size: 30px;}
    </style>
  </head>
  <body id="${bodyId}">
  <s:layout-component name="modal"/>
  <%--<span style="display:none;"><s:link beanclass="com.hk.web.action.HeartbeatAction" id="heartbeat">H</s:link></span>--%>

  <div class="container_12">
    <div class="grid_12">
      <s:layout-component name="menu">
        <jsp:include page="/includes/_adminMenu.jsp"/>
      </s:layout-component>
    </div>

    <div class="grid_12" style="text-align:center;">
      <s:layout-component name="heading"/>
      <div class="error"><s:errors/></div>
      <div class="messagesOuter"><s:messages key="generalMessages"/></div>
      <s:layout-component name="content"/>
    </div>
  </div>
  <script type="text/javascript">
    $('input[type="checkbox"]').click(function() {
      $(this).parent("label").toggleClass('highlight');
    });
  </script>
  </body>
  </html>
</s:layout-definition>