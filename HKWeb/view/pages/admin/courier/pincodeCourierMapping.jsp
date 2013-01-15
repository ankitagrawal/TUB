<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 8, 2013
  Time: 12:45:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction" var="pcma"/>
 <%
    MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
    pageContext.setAttribute("allCourier", masterDataDao.getAvailableCouriers());
%>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pincode Courier Mapping">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            $(document).ready(function(){
                $('.check').click(function(){
                    var text = $('#pin').val();
                    if(text == "" || text == null){
                        alert("Pincode can't be left Empty");
                        return false;
                    }
                    else if(isNaN(text)){
                        alert("Pincode must contain numbers only");
                        return false;
                    }
                });
                 $('.addRowButton').click(function () {
                     var pincode = ${pcma.pincode.id};
                    var lastIndex = $('.lastRow').attr('count');
                    if (!lastIndex) {
                        lastIndex = -1;
                    }
                    $('.lastRow').removeClass('lastRow');

                    var nextIndex = eval(lastIndex + "+1");
                    var newRowHtml =
                            '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                            '<td>' + Math.round(nextIndex + 1) +
                            '<input type="hidden" value=' + pincode + ' name="pincodeCourierMappings[' + nextIndex + '].pincode">'+
                            '.</td>' +
                            '<td>' +
                            '<select name="pincodeCourierMappings[' + nextIndex + '].courier">' +
                            '<option value="">--Select--</option>' +
                                    <c:forEach items="${allCourier}" var="courier">
                            '<option value="' + ${courier.id} + '"> ' + "${courier.name}" + '</option>' +
                                    </c:forEach>
                            '</select>' +
                            '</td>' +
                            '<td>' +
                            '</td>' +
                            '<td>' +
                             '<input type="checkbox" name="pincodeCourierMappings[' + nextIndex + '].prepaidAir"/>' +
                            '</td>' +
                            '<td>' +
                            '<input type="checkbox" name="pincodeCourierMappings[' + nextIndex + '].prepaidGround"/>' +
                            '</td>' +
                            '<td>' +
                            '<input type="checkbox" name="pincodeCourierMappings[' + nextIndex + '].codAir"/>' +
                            '</td>' +
                            '<td>' +
                             '<input type="checkbox" name="pincodeCourierMappings[' + nextIndex + '].codGround"/>' +
                             '</td>' +
                            '</tr>';

                    $('#courierTable').append(newRowHtml);

                    return false;
                });
            });
        </script>
    </s:layout-component>

    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
            <label>Enter Pincode</label>
            <s:text name="pin" id="pin"/>
            <s:submit name="search" value="Basic Search" class="check"/>
            <s:submit name="detailedAnalysis" value="Detailed Analysis" class="check"/>
           
         <div class="clear"></div>
                <c:choose>
                <c:when test="${pcma.applicableShipmentServices!=null and fn:length(pcma.applicableShipmentServices)>0}">
                    <h2>Applicable Shipment Services</h2>
                    <br>
                    <table>
                        <c:forEach items="${pcma.applicableShipmentServices}" var="applicableShipmentService">
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
                </c:when>
                <c:otherwise>
                <c:if test="${pcma.pincodeCourierMappings!=null and fn:length(pcma.pincodeCourierMappings)>0}">
                  <h2>Pincode Courier Mappings</h2>
                    <br>
                <table class="zebra_vert" id="courierTable">
                    <thead>
                    <tr>
                        <th>S.No.</th>
                        <th>Courier Name</th>
                        <th>Courier Disable</th>
                        <th>Prepaid Air</th>
                        <th>Prepaid Ground</th>
                        <th>COD Air</th>
                        <th>COD Ground</th>
                    </tr>
                    </thead>
                    <c:forEach items="${pcma.pincodeCourierMappings}" var="pCourierMap" varStatus="ctr">
                        <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                            <td>
                                ${ctr.index + 1}.
                                <input type="hidden" name="pincodeCourierMappings[${ctr.index}].pincode" value="${pCourierMap.pincode.id}"/>
                                <s:hidden name="pincodeCourierMappings[${ctr.index}].id" value="${pCourierMap.id}"/>
                            </td>
                            <td>
                                <s:hidden name="pincodeCourierMappings[${ctr.index}].courier" value="${pCourierMap.courier.id}"/>
                                    ${pCourierMap.courier.name}
                            </td>
                            <td>
                                    ${pCourierMap.courier.disabled ? 'No' : 'Yes'}
                            </td>
                            <td>
                                <c:out value="${pCourierMap.prepaidAir}"/>
                                <s:checkbox name="pincodeCourierMappings[${ctr.index}].prepaidAir" />
                            </td>
                            <td>
                                <c:out value="${pCourierMap.prepaidGround}"/>
                                <s:checkbox  name="pincodeCourierMappings[${ctr.index}].prepaidGround" />
                            </td>
                            <td>
                                <c:out value="${pCourierMap.codAir}"/>
                                <s:checkbox name="pincodeCourierMappings[${ctr.index}].codAir" />
                            </td>
                            <td>
                                <c:out value="${pCourierMap.codGround}" />
                                <s:checkbox name="pincodeCourierMappings[${ctr.index}].codGround" />
                                <s:hidden name="pincodeCourierMappings[${ctr.index}].routingCode" value="${pCourierMap.routingCode}"/>
                            </td>
                        </tr>
                    </c:forEach>
                     </table>
                    <a href="pincodeCourierMapping.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
                    <br/>
                     <s:submit name="update" value="SAVE"/>
                    </c:if>
                    </c:otherwise>
                    </c:choose>

            <h2>File to Upload
                <s:file name="fileBean" size="30"/></h2>
            <s:submit name="uploadExcel" value="Upload Courier Excel"/>
            <div class="clear"></div>

            <h2>Download Pincode Courier Excel</h2>
                <s:select name="updateCourier"  id="status">
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList"
                                               value="id" label="name"/>
                </s:select>
                <s:submit name="generateExcel" value="Download Courier Excel"/>
        </s:form>
    </s:layout-component>
</s:layout-render>
