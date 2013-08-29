<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Search/Add Pincode">
	<s:useActionBean beanclass="com.hk.web.action.admin.courier.MasterPincodeAction" var="mpaBean"/>
	<s:layout-component name="heading">
		Search and Add Pincode
        <script type="text/javascript">
            $('document').ready(function(){
                $('#search').click(function(){
                   var pincode = $('#pincodeString').val();
                    if(pincode == null || pincode == "" || isNaN(pincode) || pincode.length!=6){
                        alert("Pincode Value can't be left Empty or Must contain numbers and length must be 6!!");
                        $('#pincodeString').val("");
                        return false;
                    }
                });
                $('#save').click(function(){
                   var pincode   = $('#pincode').val();
                   var region    = $('#region').val();
                   var locality  = $('#locality').val();
                   var city      = $('#city').val();
                   var state     = $('#state').val();
                   var zone      = $('#zone').val();
                   var hub       = $('#nearestHub').val();
                   var lastMCost = $('#lastMileCost').val();
                    if(pincode == null || pincode == "" || isNaN(pincode) || pincode.length!=6){
                        alert("pincode can't be empty or must contain numbers only and length must be 6!!");
                        $('#pincode').val("");
                        return false;
                    }
                    if(region == null || region == "" || locality == null || locality == ""){
                        alert("Value can't be left empty!!");
                        return false;
                    }
                    if(city == null || city == "" || state == null || state == "" || zone == null || zone == ""
                            || hub == null || hub == ""){
                       alert("City, State, Nearest Hub and Zone must be selected !!");
                       return false;
                    }
                    if (isNan(lastMCost)) {
                        alert("Last mile Cost has to be a number.")
                        return false;
                    }
                    $('#savePincodeString').attr('val',pincode);
                });
            });
        </script>
	</s:layout-component>
	<s:layout-component name="content">
      <fieldset class="right_label">
        <legend>Search Pincode</legend>
			<s:form beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
							<br/><br/>
				<s:text name="pincodeString" id="pincodeString" style="width:200px;"/>
				<br/>
				<br/>
				<s:submit name="search" value="Search" id="search"/>
			 </s:form>
        </fieldset>
        <div style="display:inline-block;">
            <h2 style="color:red;">Pincode once saved can't be edit</h2>
       <fieldset style="float:left;">
         <legend>Add Pincode Or Edit Pincode Details</legend>
			<table>
				<s:form beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
                    <s:hidden name="pincodeString" value="${mpaBean.pincode.pincode}" id="savePincodeString" />
					<tr>
						<td>Pincode:</td>
                        <td>
                        <c:choose>
                        <c:when test="${mpaBean.pincode!=null}">
						${mpaBean.pincode.pincode}
                        </c:when>
                         <c:otherwise>
                             <s:text name="pincode.pincode" id="pincode" maxlength="6"/>
                         </c:otherwise>
                        </c:choose>
                        </td>
					</tr>
                    <tr>
						<td>Region:</td>
                        <td>
						<c:choose>
                        <c:when test="${mpaBean.pincode!=null}">
						${mpaBean.pincode.region}
                        </c:when>
                         <c:otherwise>
                             <s:text name="pincode.region" id="region"/>
                         </c:otherwise>
                        </c:choose>
                        </td>
					</tr>
					<tr>
						<td>Locality:</td>
                        <td>
						<c:choose>
                        <c:when test="${mpaBean.pincode!=null}">
						${mpaBean.pincode.locality}
                        </c:when>
                         <c:otherwise>
                             <s:text name="pincode.locality" id="locality"/>
                         </c:otherwise>
                        </c:choose>
                        </td>
					</tr>
					<tr>
						<td>City:</td>
                        <td>
                       <c:choose>
                        <c:when test="${mpaBean.pincode!=null}">
						${mpaBean.pincode.city.name}
                        </c:when>
                         <c:otherwise>
                             <s:select name="pincode.city" id="city">
                               <s:option value="">--Select--</s:option>
                              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="cityList"
                                               value="id" label="name"/>
                             </s:select>
                         </c:otherwise>
                        </c:choose>
                        </td>
					</tr>
					<tr>
						<td>State:</td>
						 <td>
                       <c:choose>
                        <c:when test="${mpaBean.pincode!=null}">
						${mpaBean.pincode.state.name}
                        </c:when>
                         <c:otherwise>
                             <s:select name="pincode.state" id="state">
                               <s:option value="">--Select--</s:option>
                              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="stateList"
                                               value="id" label="name"/>
                             </s:select>
                         </c:otherwise>
                        </c:choose>
                        </td>
					</tr>
					<tr>
						<td>
							<label>Zone:</label>
								</td>
						 <td>
                       <c:choose>
                        <c:when test="${mpaBean.pincode!=null}">
						${mpaBean.pincode.zone.name}
                        </c:when>
                         <c:otherwise>
                             <s:select name="pincode.zone" id="zone">
                               <s:option value="">--Select--</s:option>
                              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allZones"
                                               value="id" label="name"/>
                             </s:select>
                         </c:otherwise>
                        </c:choose>
                        </td>
					</tr>
                    <tr>
                        <td>Nearest Hub:</td>
                        <td>
                            <c:choose>
                                <c:when test="${mpaBean.pincode!=null && mpaBean.pincode.nearestHub!=null}">
                                    ${mpaBean.pincode.hub.name}
                                </c:when>
                                <c:otherwise>
                                    <s:select name="pincode.nearestHub" id="nearestHub">
                                        <s:option value="">--Select--</s:option>
                                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="hubList"
                                                                   value="id" label="name"/>
                                    </s:select>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Last Mile Cost:</label>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${mpaBean.pincode!=null}">
                                    ${mpaBean.pincode.lastMileCost}
                                </c:when>
                                <c:otherwise>
                                    <s:text name="pincode.lastMileCost" id="lastMileCost"></s:text>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
					<tr>
                        <c:if test="${mpaBean.pincode==null}">
						<td><s:submit name="save" value="Save" id="save"/></td>
                        </c:if>
					</tr>
				</s:form>
			</table>
          </fieldset>
       <fieldset style="float:left;">
			<table align="center" class="cont"> Available Courier Services:
				<c:forEach items="${mpaBean.applicableShipmentServices}" var="applicableShipmentService" varStatus="ctr">
					<tr>
						<td>
						${applicableShipmentService.key}
                        </td>
						<td>
						${applicableShipmentService.value}
						</td>
					</tr>
				</c:forEach>
			</table>
          </fieldset>
		</div>
       <fieldset class="right_label">
    	   <legend>Upload Pincode List</legend>
         <ul>
        <s:form beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
          <br/>
				<div >
					<li><label>File to Upload</label>
						<s:file name="fileBean" size="30"/>
					</li>
					<li>

							<s:submit name="uploadPincodeExcel" value="Upload"/>
            <br/>
             (Worksheet Name: PincodeInfo &nbsp;&nbsp;&nbsp; 7 Fields: PINCODE &nbsp;CITY &nbsp;STATE &nbsp;REGION
                        &nbsp;LOCALITY &nbsp;ZONE &nbsp;NEAREST_HUB &nbsp;CONVEYANCE_COST)</li>
                    <br>
                    <s:submit name="generatePincodeExcel"    value="Download Pincode Xls"/>
					</s:form>
			</ul>
          </fieldset>
		<div>
			<fieldset style="display:inline-block;width:250px; padding:7px;">
			<legend>Click Link To Add PRZ</legend>
			 <span style="font:bold;color:darkolivegreen;"><s:link
					 beanclass="com.hk.web.action.admin.courier.MasterPincodeAction" event="directToPincodeRegionZone">
				 Add Pincode Region Zone
			 </s:link></span>
			</fieldset>
		</div>
	</s:layout-component>
</s:layout-render>