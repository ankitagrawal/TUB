<!--table class='menuContainer'><tr><td style='width:40px;padding:0px'><div id=homeButton></div></td><td><img src='images/logo.png'/></td><td><img src='images/cart_empty.png' height='35px' align=right /></td></tr></table-->

<table class='menuContainer'><tr><td><img src='${httpPath}/images/logo.png' style='position:relative;top:3px;left:3px'/></td><td>
<div class=sessionOptions><b>Hi <%=(session.getAttribute("userName")!=null?session.getAttribute("userName"):"Guest")%> 
</b><br><%=(session.getAttribute("userName")!=null?"<a href='javascript:void(0)' onclick='javascript:logout()' style='text-decoration:none'>Logout</a>":"<a href='${httpPath}/login-signup.jsp' style='text-decoration:none'>Login | Signup</a>")%></div></td></tr></table>
<script>

//var wSURL = 'http://122.176.33.182:9090/healthkart/rest/api/';
//var wSURL = 'http://192.168.1.25:9090/healthkart/rest/api/';
</script>