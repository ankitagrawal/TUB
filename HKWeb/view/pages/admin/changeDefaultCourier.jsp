<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction" var="mpaBean"/>
	<s:layout-component name="heading">
		Search and Add Pincode
	</s:layout-component>

	<s:layout-component name="content">
   <script>
       $(document).ready(function(){
           $('#pincodeString').focus();

           $('#show-add-new-form').click(function(event) {
               event.preventDefault();
               $('#new-default-courier-form').slideDown();
           });
       })
    </script>
      <fieldset class="right_label">
			<s:form beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">
				<label>Search Pincode</label>
				<br/><br/>
				<s:text name="pincodeString" id="pincodeString" style="width:200px;"/>
               <label>Cod Available</label>
                 <s:select name="pincodeDefaultCourier.cod" value="">
                                        <s:option value="0">N</s:option>
                                        <s:option value="1">Y</s:option>
                  </s:select>      &nbsp;
                <label>Ground Shipping Available </label>
                 <s:select name="pincodeDefaultCourier.groundShipping" value="">
                                        <s:option value="0">N</s:option>
                                        <s:option value="1">Y</s:option>
                 </s:select>
                <s:submit name="search" value="Search"/>
                <br/>
				<br/>  				
                <label>Warehouse</label>
                <s:select name="pincodeDefaultCourier.warehouse">
                                    <s:option value="1">Gurgaon</s:option>
                                    <s:option value="2">Mumbai</s:option>
                    </s:select>
        <s:submit name="generatePincodeExcel"    value="Download Pincode Xls"/>
			</s:form>
        </fieldset>
       <fieldset class="right_label">
    	<ul>

				<s:form beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">
				<div >
					<li><label>File to Upload</label>
						<s:file name="fileBean" size="30"/>
					</li>
					<li>

							<s:submit name="uploadPincodeExcel" value="Upload"/>

					</li>
                </div>
				</s:form>
		</ul>
       </fieldset>
       <fieldset style="float:left;">
           <c:forEach items="${mpaBean.pincodeDefaultCouriers}" var="defaultCourier" varStatus="pincodeDefaultCouriersCount">
            <table>
                <s:form beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">
                    <s:hidden name="pincodeDefaultCourier" value="${defaultCourier.id}"/>
                    <tr>
                        <td>Pincode:</td>
                        <td><s:hidden name="pincodeDefaultCourier.pincode" value="${defaultCourier.pincode.id}"/>${defaultCourier.pincode.pincode}</td>
                    </tr>
                    <tr>
                        <td>Warehouse:</td>
                        <td><s:hidden name="pincodeDefaultCourier.warehouse" value="${defaultCourier.warehouse.id}"/>${defaultCourier.warehouse.name}</td>
                         <s:hidden name="pincodeDefaultCourier.cod" value="${mpaBean.pincodeDefaultCourier.cod}"/>
                         <s:hidden name="pincodeDefaultCourier.groundShipping" value="${mpaBean.pincodeDefaultCourier.groundShipping}"/>

                    </tr>
                     <c:if test="${mpaBean.pincodeDefaultCourier.cod && !mpaBean.pincodeDefaultCourier.groundShipping }">
                    <tr>
                        <td>Default Courier (COD)for Air Shipped:</td>
                        <td><s:select name="pincodeDefaultCourier.courier" value="${defaultCourier.courier.id}">
                                <s:option value="">-Select-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
                            </s:select>
                        </td>
                    </tr>
                    </c:if>
                      <c:if test="${!mpaBean.pincodeDefaultCourier.cod && !mpaBean.pincodeDefaultCourier.groundShipping }">
                      <tr>
                        <td>Default Courier (NON COD)for Air Shipped:</td>
                        <td><s:select name="pincodeDefaultCourier.courier" value="${defaultCourier.courier.id}">
                                <s:option value="">-Select-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
                            </s:select>
                        </td>
                    </tr>
                     </c:if>
                    <c:if test="${mpaBean.pincodeDefaultCourier.cod && mpaBean.pincodeDefaultCourier.groundShipping }">
                     <tr>
                        <td>Default Courier (COD)for Ground Shipped:</td>
                        <td><s:select name="pincodeDefaultCourier.courier" value="${defaultCourier.courier.id}">
                                <s:option value="">-Select-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
                            </s:select>
                        </td>
                    </tr>
                    </c:if>
                    <c:if test="${!mpaBean.pincodeDefaultCourier.cod && mpaBean.pincodeDefaultCourier.groundShipping }">
                     <tr>
                        <td>Default Courier (NON COD)for Ground Shipped:</td>
                        <td><s:select name="pincodeDefaultCourier.courier" value="${defaultCourier.courier.id}">
                                <s:option value="">-Select-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
                            </s:select>
                        </td>
                    </tr>
                    </c:if>
                    <tr>
                        <td>Estimated Shippping Cost </td>
                        <td><s:text name="pincodeDefaultCourier.estimatedShippingCost" value="${defaultCourier.estimatedShippingCost}" /></td>
                    </tr>                      
                    <tr>
                        <td><s:submit name="save" value="Save"/></td>
                    </tr>
                </s:form>
            </table>
           </c:forEach>

          </fieldset>

            <fieldset style="float:left; display:none;" id="new-default-courier-form">
                    <label>Add New</label>
                    <table>
                        <s:form beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">
                            <tr>
                                <td>Pincode:</td>
                                <td><s:text name="pincodeString"  />
                            </tr>
                            <tr>
                                <td>Warehouse:</td>
                                <td><s:select name="pincodeDefaultCourier.warehouse">
                                    <s:option value="1">Gurgaon</s:option>
                                    <s:option value="2">Mumbai</s:option>
                                     </s:select>
                                </td>
                            </tr>
                            <tr>
                                <td>Default Courier </td>
                                <td><s:select name="pincodeDefaultCourier.courier">
                                    <s:option value="">-Select-</s:option>
                                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
                                    </s:select>
                                </td>
                            </tr>
                            <tr>
                                <td>Cod Available</td>
                                <td>
                                    <s:select name="pincodeDefaultCourier.cod" value="">
                                        <s:option value="0">N</s:option>
                                        <s:option value="1">Y</s:option>
                                    </s:select>
                                </td>
                            </tr>
                            <tr>
                                <td>Ground Shipping Available</td>
                                <td>
                                    <s:select name="pincodeDefaultCourier.groundShipping" value="">
                                        <s:option value="0">N</s:option>
                                        <s:option value="1">Y</s:option>
                                    </s:select>
                                </td>
                            </tr>
                            <tr>
                                <td>Estimated Shippping Cost </td>
                                <td><s:text name="pincodeDefaultCourier.estimatedShippingCost"/></td>
                            </tr>
                          <tr>                               
                            <tr>
                                <td><s:submit name="add_pincode" value="Add"/></td>
                            </tr>
                        </s:form>
                    </table>
                  </fieldset>
        <a href="#" id="show-add-new-form">Add New Default Courier for Existing pincode and warehouse</a><br />
       <c:if test="${!empty mpaBean.courierServiceList}">
           <fieldset style="float:left;">
                <table align="center" class="cont"> Available Courier Services corresponding to the entered Pincode:
                    <thead>
                    <tr>
                        <th>Courier Service</th>
                        <th>Cod Available</th>
                        <th> Ground Shipping Available</th>
                        <th> Cod Available on Ground Shipping</th>
                    </tr>
                    </thead>
                    <c:forEach items="${mpaBean.courierServiceList}" var="courierServiceList" varStatus="ctr">
                        <tr>
                            <td>${courierServiceList.courier.name}</td>
                            <td>
                                <c:if test="${courierServiceList.codAvailable}">Y</c:if>
                                <c:if test="${!courierServiceList.codAvailable}">N</c:if>
                            </td>
                            <td>
                                 <c:if test="${courierServiceList.groundShippingAvailable}">Y</c:if>
                                <c:if test="${!courierServiceList.groundShippingAvailable}">N</c:if>
                            </td>
                             <td>
                                 <c:if test="${courierServiceList.codAvailableOnGroundShipping}">Y</c:if>
                                <c:if test="${!courierServiceList.codAvailableOnGroundShipping}">N</c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
              </fieldset>
        </c:if>


	</s:layout-component>
</s:layout-render>