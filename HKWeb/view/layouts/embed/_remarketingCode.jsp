<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
  <%
    String label= (String) pageContext.getAttribute("label");
    pageContext.setAttribute("label", label);

    String id= (String) pageContext.getAttribute("id");
    pageContext.setAttribute("id", id);
  %>
  <!-- Google Code for Remarketing List -->
  <script type="text/javascript">
/* <![CDATA[ */
  var google_conversion_id = ${id};
  var google_conversion_language = "en";
  var google_conversion_format = "3";
  var google_conversion_color = "666666";
  var google_conversion_label = "${label}";
  var google_conversion_value = 0;
/* ]]> */
  </script>
  <script type="text/javascript" src="http://www.googleadservices.com/pagead/conversion.js">
  </script>
  <noscript>
  <div style="display:inline;">
  <img height="1" width="1" style="border-style:none;" alt="" src="http://www.googleadservices.com/pagead/conversion/${id}/?label=${label}&amp;guid=ON&amp;script=0"/>
  </div>
  </noscript>
</s:layout-definition>