<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.WishlistAction" var="wa"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
  </s:layout-component>
</s:layout-render>


<script type="text/javascript">
  window.onload = function() {
    document.getElementById("wishlistLink").style.fontWeight = "bold";
  };
</script>
