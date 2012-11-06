<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultG.jsp" pageTitle="Sign Up at FitnessPro">
    <s:layout-component name="menu"> </s:layout-component>
<s:layout-component name="heading"></s:layout-component>
<s:layout-component name="lhsContent">
 
</s:layout-component>

<s:layout-component name="rhsContent">
<div class="grid_24">

<div align="center" style="width:630px; margin:20px auto;"><img src="${pageContext.request.contextPath}/images/logo.png" align="left" width="156" /><span style="float:right; font-weight:bold; font-size:15px;">FITNESS PRO</span><br /> <br /> <iframe scrolling="no" height="800" frameborder="0" src="https://docs.google.com/spreadsheet/viewform?fromEmail=true&formkey=dDZaUGh4OGx0T1BJTE1zcXBsaGN2M1E6MQ" width="100%"></iframe></div>
</div>

</s:layout-component>
</s:layout-render>


<script type="text/javascript">
  window.onload = function() {
    document.getElementById('tncLink').style.fontWeight = "bold";
  };
</script>
