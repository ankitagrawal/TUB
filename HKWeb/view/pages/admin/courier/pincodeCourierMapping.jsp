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
<s:useActionBean beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction" var="pcma"/>

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
                $('.YesNo').click(function(){
                    var value = $(this).val();
                    if(value == "Y"){
                        $(this).val("N");
                        $(this).parent().children('.booleanUpdate').attr('value') = false;
                    }
                    else if(value == "N"){
                        $(this).val("Y");
                        $(this).parent().children('.booleanUpdate').attr('value') = true;
                    }
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

                <c:choose>
                <c:when test="${pcma.applicableShipmentServices!=null and fn:length(pcma.applicableShipmentServices)>0}">
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
                <table class="zebra_vert">
                    <thead>
                    <tr>
                        <th>Courier Name</th>
                        <th>Courier Active</th>
                        <th>Prepaid Air</th>
                        <th>Prepaid Ground</th>
                        <th>COD Air</th>
                        <th>COD Ground</th>
                    </tr>
                    </thead>
                    <c:forEach items="${pcma.pincodeCourierMappings}" var="pCourierMap" varStatus="ctr">
                        <tr>
                            <td>
                                <s:hidden name="pincodeCourierMappings[${ctr.index}].courier" value="${pCourierMap.courier.id}"/>
                                    ${pCourierMap.courier.name}
                            </td>
                            <td>
                                    ${pCourierMap.courier.disabled ? 'Y' : 'N'}
                            </td>
                            <td>
                                <s:hidden class="booleanUpdate" name="pincodeCourierMappings[${ctr.index}].prepaidAir" value="${pCourierMap.prepaidAir}"/>
                                <a href="#" class="YesNo">${pCourierMap.prepaidAir ? 'Y':'N'}</a>
                            </td>
                            <td>
                                <s:hidden class="booleanUpdate" name="pincodeCourierMappings[${ctr.index}].prepaidGround" value="${pCourierMap.prepaidGround}"/>
                                <a href="#" class="YesNo">${pCourierMap.prepaidGround ? 'Y':'N'}</a>
                            </td>
                            <td>
                                <s:hidden class="booleanUpdate" name="pincodeCourierMappings[${ctr.index}].codAir" value="${pCourierMap.codAir}"/>
                                <a href="#" class="YesNo">${pCourierMap.codAir ? 'Y':'N'}</a>
                            </td>
                            <td>
                                <s:hidden class="booleanUpdate" name="pincodeCourierMappings[${ctr.index}].codGround" value="${pCourierMap.codGround}"/>
                                <a href="#" class="YesNo">${pCourierMap.codGround ? 'Y':'N'}</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <s:submit name="update" value="SAVE"/>
                     </table>
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
