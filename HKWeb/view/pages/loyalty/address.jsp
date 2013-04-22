<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.hk.constants.courier.StateList" %>
<%@include file="/includes/_taglibInclude.jsp"%>
<link href="<hk:vhostJs/>/pages/loyalty/LoyaltyFiles/css/bootstrap.css" rel="stylesheet">
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<stripes:layout-render name="/pages/loyalty/layout.jsp">
	<stripes:layout-component name="contents">
		<s:useActionBean
			beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction"
			var="asa" />

		<s:useActionBean
			beanclass="com.hk.web.action.core.loyaltypg.CartAction" var="ca" />

		<s:form
			beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction"
			id="selectAddress">
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
											<s:link beanclass="com.hk.web.action.core.loyaltypg.AddressSelectionAction"
											        event="confirm" style="font-size:15px; text-decoration: none;">
												<s:param name="selectedAddressId" value="${address.id}"/>
												<strong>${address.name}</strong><br> ${address.line1},
												${address.line2}<br> ${address.city}<br>
												${address.state}, ${address.pincode.pincode}<br> <abbr
													title="Phone">P:</abbr>
												${address.phone}
											</s:link>
										</address>
									</td>
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
												<label>City:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="address.city" placeholder="City" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<div class="pull-left">
												<label>State:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
												  <s:select  name="address.state">
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
												<label>Address Pincode:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="pincode" placeholder="Pin Code" />
												</div>
											</div>
										</div>
										<div class="control-group">
											<div class="pull-left">
												<label>Phone:</label>
											</div>
											<div class="controls">
												<div class="pull-right">
													<s:text name="address.phone" placeholder="Phone" />
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
						<s:submit name="confirm" value="Confirm Shipping Address"
							class="btn btn-primary" />
					</div>
				</div>
			</div>
		</s:form>
	</stripes:layout-component>
</stripes:layout-render>

