<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html>
  <head>
    <title>${pageTitle} | HealthKart.com</title>
    <s:layout-component name="analytics"><s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="" allCategories="" brand="" isProd="<%=false%>"/></s:layout-component>
    <script type="text/javascript" src="http://connect.facebook.net/en_US/all.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/otherScripts/jQuery-1.4.2.js"></script>
    <s:layout-component name="htmlHead"/>
  </head>
  <body id="${bodyId}">
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
      <hr/>
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