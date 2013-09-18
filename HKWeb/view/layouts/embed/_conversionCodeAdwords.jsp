<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <script type="text/javascript">
    /* <![CDATA[ */
    var google_conversion_language = "en";
    var google_conversion_format = "3";
    var google_conversion_color = "ffffff";
    var google_conversion_id = ${id};
    var google_conversion_label = "${label}";
    var google_conversion_value = 0;
    if (${conversion_value}) {
      google_conversion_value = ${conversion_value};
    }
    var google_custom_params = window.google_tag_params;
    var google_remarketing_only = false;
    /* ]]> */
  </script>
  <script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js"></script>
  <noscript>
    <div style="display:inline;">
  		<img height="1" width="1" style="border-style:none;" alt="" src="//www.googleadservices.com/pagead/conversion/${id}/?value=${conversion_value}&amp;label=${label}&amp;guid=ON&amp;script=0"/>
    </div>
  </noscript>
</s:layout-definition>