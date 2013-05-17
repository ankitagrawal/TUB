<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>

<link href="<hk:vhostJs/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<stripes:layout-render name="/pages/loyalty/layout.jsp">

	<stripes:layout-component name="contents">
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/bootbox.js"></script>
	<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/validator.js"></script>
	 <div style="display:none;">
            <s:link beanclass="com.hk.web.action.core.autocomplete.AutoCompleteAction" event="populateAddress" id="populateaddress">
            </s:link>
     </div>
		<s:useActionBean
			beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction"
			var="asa" />

		<s:useActionBean
			beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca" />
		<s:form
			beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction"
			id="selectAddress" name="selectAddress">
			<div class="row">
				<div class="span6">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th style="width: 150px;" colspan="2">Select one of your
									saved address</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${asa.addressList}" var="address">

								<tr>
									<td>
										<address>
											<s:link beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction" event="confirm" class= "blue">
												<s:param name="selectedAddressId" value="${address.id}"/>
												<strong><span class="nameEdit">${address.name} </span></strong>
												<br><span class="line1">${address.line1}</span>,
												<span class="line2">${address.line2}</span><br>
												<span class="city">${address.city}</span><br>
												<span class="state">${address.state}</span>, <span class="pinEdit">${address.pincode.pincode}</span><br> 
												Ph: <span class="phone">${address.phone}</span>
											</s:link>
										</address>
									</td>
									<td><span class="edit">Edit</span> |
									<s:link beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction" event="remove" class="delete">
                            		<s:param name="deleteAddress" value="${address.id}"/> Delete </s:link> </td>
								</tr>

							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="span6">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th style="width: 150px;" colspan="2">Or add a new Shipping
									Address</th>
							</tr>
						</thead>
						<tbody>
						<tr  id="error-row" >
						<td>
						<div id="error" class="errorMessage" ></div>
						</td>
						<tr>	
							<tr>
								<td>
									<div class="form-horizontal">
										<div class="control-group">
											<div class="pull-left">
												<label>Name:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="address.name" placeholder="Name" />
												</div>
											</div>
										</div>

										<div class="control-group">
											<div class="pull-left">
												<label>Address Line 1:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="address.line1" placeholder="Line 1" />
												</div>
											</div>
										</div>

										<div class="control-group">
											<div class="pull-left">
												<label>Address Line 2:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="address.line2" placeholder="Line 2" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<div class="pull-left">
												<label>Address Pincode:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="pincode" id="pin" placeholder="Pin Code" maxlength="6"/>
												</div>
											</div>
										</div>
										<div class="control-group">
											<div class="pull-left">
												<label>City:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="address.city" id="citySelect" placeholder="City" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<div class="pull-left">
												<label>State:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
												  <s:select  name="address.state"  id="stateSelect">
										            <s:option> </s:option>
										            <c:forEach items="<%=StateList.stateList%>" var="state">
										              <s:option value="${state}">${state}</s:option>
										            </c:forEach>
										          </s:select>
												</div>
											</div>
										</div>
										<div class="control-group">
											<div class="pull-left">
												<label>Phone:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text id ="phone" name="address.phone" placeholder="Phone" />
												</div>
											</div>
										</div>

									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>

			<div class="row">
				<div class="span9"></div>
				<div class="span3">
					<div class="pull-right">
						<s:submit id="confirmButton" name="confirm" value="Confirm Shipping Address" class="btn btn-primary" />
					</div>
				</div>
			</div>
		</s:form>
	</stripes:layout-component>
</stripes:layout-render>
<script type="text/javascript">
$(document).ready(function() {

    var groundShippingAllowed = ${asa.groundShippingAllowed};
    
    if (!groundShippingAllowed) {
    	bootbox.alert("This order is not servicable at this pincode!!");
    }	
var bool = false;
 $("#error-row").hide();
 $("#confirmButton").click(validateAddressForm);


 $("#pin").blur(function(event) {
	$("#error").html("");
 	var pin = $("#pin").val();
 	_validatePincode(pin);
 	if (err) {
 		$("#error-row").show();
 	} else {
 		$.getJSON($("#populateaddress").attr('href'), {pincode:pin}, function(responseData) {
 			if (responseData.code == '<%=HealthkartResponse.STATUS_OK%>') {
 				$("#stateSelect").val(responseData.data.stateName);
 				$("#citySelect").val(responseData.data.cityName);
 			}
 			else {
                 $("#pin").val("");
 				$("#stateSelect").val("");
 				$("#citySelect").val("");
 				$("#error").empty();
                bootbox.alert("We don't Service to this pincode, please Enter a valid Pincode or Contact to Customer Care.");
 				$("#error").html("<br/>We do not service this pincode.<br/>Please enter a valid Pincode<br/>OR <br/>Contact customer care.<br/><br/>");
 				 $("#error-row").show();
 			}
 		});
 	}
 });
 
 $('.edit').click(function() {
   form = $("#selectAddress");
   addressBlock = $(this).parent().parent().find('address');
   name = addressBlock.find('.nameEdit').text();
   street1 = addressBlock.find('.line1').text();
   street2 = addressBlock.find('.line2').text();
   city = addressBlock.find('.city').text();
   state = addressBlock.find('.state').text();
   pin = addressBlock.find('.pinEdit').text();
   phone = addressBlock.find('.phone').text();
   id = addressBlock.find("selectedAddressId").val();
   form.find("input[type='text'][name='address.name']").val(name);
   form.find("input[type='text'][name='address.line1']").val(street1);
   if (street2) {
     form.find("input[type='text'][name='address.line2']").val(street2);
   }
   form.find("input[type='text'][name='pincode']").val(pin);
   form.find("input[type='text'][name='address.city']").val(city);
   form.find("[name='address.state']").val(state.toUpperCase());
   form.find("input[type='text'][name='address.phone']").val(phone);
   form.find("input[type='hidden'][name='address.id']").val(id);
 });
    $('.delete').click(function() {
        if (confirm('Are you sure you want to delete this address?')) {
            bool = true;
            return true;
        } else {
            return false;
        }
    });

    $('.address').hover(
		        function() {
			        $(this).children('.hidden').slideDown(100);
			        $(this).children("#edit").click(function() {
				        return false;
			        });
                 $(this).children('.delete').click(function() {
                   if(bool) return true;
                     else
                    return false;
                 });
		        },
		        function() {
			        $(this).children('.hidden').slideUp(50);
		        }
		        );
     $('.address').click(function() {
	        var add_url = $(this).children('a').attr('href');
	        document.location.href = add_url;
     });
 });
</script>

