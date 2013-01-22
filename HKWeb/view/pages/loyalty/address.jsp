<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction" var="asa" />


<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca"/>

<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>HealthKart | Loyalty Program</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Deepak Chauhan">

    <link href="<hk:vhostJs/>/bootstrap/css/bootstrap.css" rel="stylesheet">

	<style type="text/css">
      body {
        padding-top: 20px;
        padding-bottom: 40px;
		padding-right: 20px;
		padding-left: 20px;
      }
    </style>
  </head>

  <body>
	<script src="<hk:vhostJs/>/bootstrap/js/jquery.js"></script>
	<script src="<hk:vhostJs/>/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript">
	</script>
	
    <div class="container">
      <div class="masthead">
        <ul class="nav nav-pills pull-right">
          <li class="active"><a href="">Home</a></li>
          <li><a href="">Visit Healthkart</a></li>
          <li><a href="core/loyaltypg/Cart.action">View Cart</a></li>
          <li><a href="">Sign In</a></li>
        </ul>
        <h3 class="muted">HealthKart</h3>
      </div>

     <s:form beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction" id="selectAddress">
      <hr>

		<table class="table table-bordered">
          <thead>
           <tr>
             <th style="width: 150px;">Address</th>
             <th>Select</th>
           </tr>
          </thead>
          <tbody>
          <c:forEach items="${asa.addressList}" var="address">
           <tr>
             <td>
             	<address>
  				<strong>${address.line1}, ${address.line2}</strong><br>
  					${address.city}<br>
  					${address.state}, ${address.pin}<br>
  					<abbr title="Phone">P:</abbr> ${address.phone}
				</address>
			</td>
			<td>
				<s:radio name="selectedAddressId" value="${address.id}"/>
			</td>
           </tr>
           </c:forEach>
          </tbody>
        </table>
        
        <div class="row">
        	<div class="span9">
        		
        	</div>
        	<div class="span3" >
	        	<div class="pull-right">
	        		<s:submit name="confirm" value="Confirm you order" class="btn btn-primary"/>
				</div>
        	</div>
        </div>
		</s:form>        
	  <hr>
      <div class="footer">
        <p>© Footer goes here!!</p>
      </div>
    </div>
</body>
</html>
