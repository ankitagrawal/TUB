<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
  <s:layout-component name="modal"/>
  <div class="modal_header">
    <div class="jqDrag" style="padding: 5px 3px;"><s:layout-component name="heading"/></div>

    <a href="#" class="modal_close jqmClose" style=" margin-top: 0px;"><img src="<hk:vhostImage/>/images/spacer.gif" width="21" height="21"/></a>
  </div>

  <div class="modal_content">
    <s:layout-component name="content"/>
  </div>

</s:layout-definition>