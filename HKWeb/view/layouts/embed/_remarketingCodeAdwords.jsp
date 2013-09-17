<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <script type="text/javascript">
    /* <![CDATA[ */
    var google_conversion_id = ${id};
    var google_conversion_label = "${label}";
    var google_custom_params = window.google_tag_params;
    var google_remarketing_only = true;
    /* ]]> */
  </script>
  <script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js"></script>
  <noscript>
    <div style="display:inline;">
      <img height="1" width="1" style="border-style:none;" alt="" src="//googleads.g.doubleclick.net/pagead/viewthroughconversion/${id}/?value=0&amp;label=${label}&amp;guid=ON&amp;script=0"/>
    </div>
  </noscript>
</s:layout-definition>