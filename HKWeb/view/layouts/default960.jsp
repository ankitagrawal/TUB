<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html>
  <head>
    <title>${pageTitle} | HealthKart.com</title>
    <jsp:include page="/includes/_style.jsp"/>
    <s:layout-component name="analytics">
      <s:layout-render name="/layouts/embed/_analytics.jsp" topCategory="" allCategories="" brand="" isProd="<%=false%>"/>
    </s:layout-component>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
    <s:layout-component name="htmlHead"/>
  </head>
  <body id="${bodyId}">
  <s:layout-component name="modal"/>
  <div id="wrapper">
    <div id="container">
      <div id="header">
        <s:layout-component name="header">
          <s:layout-render name="/layouts/embed/_header.jsp"/>
        </s:layout-component>
        <c:if test="${hk:isNotBlank(topHeading)}">
          <h1 style="color: #a8a7a7"><s:layout-component name="topHeading"/></h1>
        </c:if>
      </div>

      <div id="main">
        <div style="display:none;" id="topCategory">
          <s:layout-component name="topCategory">home</s:layout-component>
        </div>
        <div class="main-inn">
          <!-- Top Menu and Banner in LHS -->
          <s:layout-component name="menu">
            <jsp:include page="/includes/_menu.jsp"/>
          </s:layout-component>
          <div class="cl"></div>

        </div>

        <h1><s:layout-component name="heading"/></h1>

        <div class="breadcrumbContainer">
          <s:layout-component name="breadcrumb"/>
        </div>


        <div class="container_16">
          <s:layout-component name="content"/>
        </div>

        <div class="clear"></div>

      </div>


      <s:layout-component name="footer">
        <div style="bottom: 0;">
          <jsp:include page="/includes/_footer.jsp"/>
        </div>
      </s:layout-component>


      <div class="cl"></div>
    </div>
    <div class="cl"></div>

  </div>


  </body>
  </html>
</s:layout-definition>