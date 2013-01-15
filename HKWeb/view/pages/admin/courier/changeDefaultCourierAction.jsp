<%--
  Created by IntelliJ IDEA.
  User: Shrey
  Date: Jan 9, 2013
  Time: 4:20:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction" var="cdca"/>
<%
    MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
    pageContext.setAttribute("allWarehouse", masterDataDao.getServiceableWarehouses());
    pageContext.setAttribute("allCourier", masterDataDao.getAvailableCouriers());
%>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="ChangeDefault Courier">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            $(document).ready(function(){
                $('#search').click(function(){
                    var pincode = $('#pincode').val();
                    if(pincode == "" || pincode == null){
                        alert("Pincode can't be left Empty");
                        return false;
                    }
                    else if(isNaN(pincode)){
                        alert("Pincode must contain Numbers Only !!!");
                        return false;
                    }
                });

                $('#save').click(function(){
                    var bool = true;
                    $(this).hide();
                    $('.pincode').each(function(){
                        var pincode = $(this).val();
                        if(pincode == "" || pincode == null || isNaN(pincode)){
                            alert("Pincode can't be Left Empty or must contain numbers only");
                            bool = false;
                            return false;
                        }
//                        alert($(this).parent().children('input.pincodeValue').attr('value'));
                        if($(this).parent().children('input.pincodeValue').attr('value')== null || $(this).parent().children('input.pincodeValue').attr('value') == ""){
                            alert("Invalid Pincode !!!!");
                            bool = false;
                            return false;
                        }
                    });

                    $('.estimatedShippingCost').each(function(){
                        var estimatedShippingCost = $(this).val();
                        if(estimatedShippingCost!=null && estimatedShippingCost!="" && isNaN(estimatedShippingCost)){
                            alert("Estimated Shipping Cost must contain Valid Value!!!!");
                            bool = false;
                            return false;
                        }
                    });
                    if(!bool){$(this).show();return false;}
                });
                  $('.pincode').live('change',function(){
                var pincode = $(this).val();
                var obj = $(this);
                $.getJSON($('#pincodeCheck').attr('href'), { pincodeString: pincode }, function (res) {
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>'){
                        obj.parent().children('input.pincodeValue').attr('value',res.data.pincode.id);
                    }
                    else{
                        alert(res.message);
                        obj.val("");
                        return false;
                    }
                }
                        );
            });
                $('.addRowButton').click(function () {

                    var lastIndex = $('.lastRow').attr('count');
                    if (!lastIndex) {
                        lastIndex = -1;
                    }
                    $('.lastRow').removeClass('lastRow');

                    var nextIndex = eval(lastIndex + "+1");
                    var newRowHtml =
                            '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
                            '<td>' + Math.round(nextIndex + 1) + '.</td>' +
                            '<td>' +
                            '<select name="pincodeDefaultCouriers[' + nextIndex + '].courier">' +
                            '<option value="">--Select--</option>' +
                                    <c:forEach items="${allCourier}" var="courier">
                            '<option value="' + ${courier.id} + '"> ' + "${courier.name}" + '</option>' +
                                    </c:forEach>
                            '</select>' +
                            '</td>' +
                            '<td>' +
                            '<select name="pincodeDefaultCouriers[' + nextIndex + '].warehouse">' +
                                   <c:forEach items="${allWarehouse}" var="warehouse">
                            '<option value="' + ${warehouse.id} + '"> ' + "${warehouse.name}" + '</option>' +
                                    </c:forEach>
                            '</select>' +
                            '</td>' +
                            '<td>' +
                            '<input type="text"  class="pincode" />' +
                            '<input type="hidden" value="" class="pincodeValue" name="pincodeDefaultCouriers[' + nextIndex + '].pincode" >' +
                            '</td>' +
                            '<td>' +
                            '<select name="pincodeDefaultCouriers[' + nextIndex + '].cod">' +
                            '<option value="true">YES</option>' +
                            '<option value="false">NO</option>' +
                            '</select>' +
                            '</td>' +
                            '<td>' +
                            '<select name="pincodeDefaultCouriers[' + nextIndex + '].groundShipping" >' +
                            '<option value="true">YES</option>' +
                            '<option value="false">NO</option>' +
                            '</select>' +
                            '</td>' +
                            '<td>' +
                            '<input type="text" name="pincodeDefaultCouriers[' + nextIndex + '].estimatedShippingCost" class="estimatedShippingCost"/>' +
                            '</td>' +
                            '</tr>';

                    $('#courierTable').append(newRowHtml);

                    return false;
                });
            });
        </script>
    </s:layout-component>
    <s:layout-component name="content">
        <div style="display: none;">
    <s:link beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction" id="pincodeCheck" event="getPincodeJson"></s:link>
</div>
        <s:form beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">
            <label>Enter Pincode</label>
            <s:text name="pincodeString" id="pincode"/>
            <label>IsCod</label>
            <s:checkbox name="isCod"/>
            <label>IsGround</label>
            <s:checkbox name="isGround"/>
            <label>Select WareHouse</label>
            <s:select name="warehouse">
                <option value="">--Select--</option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allWarehouse"
                                           value="id" label="name"/>
            </s:select>
            <s:submit name="search" value="Search" id="search"/>

            <c:if test="${cdca.pincodeDefaultCouriers!=null and fn:length(cdca.pincodeDefaultCouriers)>0}">
                <h2>Pincode Default Courier</h2>
                <br/>
                <table id="courierTable" class="zebra_vert">
                    <tr>
                        <th>S.No</th>
                        <th>Courier Name</th>
                        <th>WareHouseName</th>
                        <th>Pincode</th>
                        <th>COD</th>
                        <th>Ground Shipping</th>
                        <th>Estimate Shipping Cost</th>
                    </tr>
                    <c:forEach items="${cdca.pincodeDefaultCouriers}" var="pincodeDefaultCourier" varStatus="ctr">
                        <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                            <td>
                                    ${ctr.index+1}.
                                <s:hidden name="pincodeDefaultCouriers[${ctr.index}].id" value="${pincodeDefaultCourier.id}"/>
                            </td>
                            <td>
                                <s:select name="pincodeDefaultCouriers[${ctr.index}].courier">
                                    <option value="">--Select--</option>
                                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList"
                                                               value="id" label="name"/>
                                </s:select>
                            </td>
                            <td>
                                    ${pincodeDefaultCourier.warehouse.name}
                                <s:hidden name="pincodeDefaultCouriers[${ctr.index}].warehouse" value="${pincodeDefaultCourier.warehouse.id}"/>
                            </td>
                            <td>
                                    ${pincodeDefaultCourier.pincode.pincode}
                                <input type="hidden" name="pincodeDefaultCouriers[${ctr.index}].pincode" value="${pincodeDefaultCourier.pincode.id}"/>
                            </td>
                            <td>
                                    ${pincodeDefaultCourier.cod}
                                <s:hidden name="pincodeDefaultCouriers[${ctr.index}].cod" value="${pincodeDefaultCourier.cod}"/>
                            </td>
                            <td>
                                    ${pincodeDefaultCourier.groundShipping}
                                <s:hidden name="pincodeDefaultCouriers[${ctr.index}].groundShipping" value="${pincodeDefaultCourier.groundShipping}"/>
                            </td>
                            <td>
                                    ${pincodeDefaultCourier.estimatedShippingCost}
                                <s:hidden name="pincodeDefaultCouriers[${ctr.index}].estimatedShippingCost" value="${pincodeDefaultCourier.estimatedShippingCost}"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </table>
                    <a href="changeDefaultCourierAction.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
                    <br/>
                    <s:submit name="save" value="Save" id="save"/>
                    </c:if>
                    <c:if test="${cdca.pincodeDefaultCouriers!=null and fn:length(cdca.pincodeDefaultCouriers)>0}">
                       <h2>Pincode Courier Mappings</h2>
                         <table class="zebra_vert">
                    <tr>
                        <th>S.No</th>
                        <th>Courier Name</th>
                        <th>Pincode</th>
                        <th>Prepaid Air</th>
                        <th>Prepaid Ground</th>
                        <th>COD Air</th>
                        <th>COD Ground</th>
                        <th>Routing Code</th>
                    </tr>
                       <c:forEach items="${cdca.pincodeCourierMappings}" var="pincodeCourierMapping" varStatus="ctr">
                         <tr>
                          <td>
                            ${ctr.index+1}.
                          </td>
                             <td>
                                 ${pincodeCourierMapping.courier.name}
                             </td>
                             <td>
                                 ${pincodeCourierMapping.pincode.pincode}
                             </td>
                             <td>
                                 ${pincodeCourierMapping.prepaidAir}
                             </td>
                             <td>
                                 ${pincodeCourierMapping.prepaidGround}
                             </td>
                             <td>
                                 ${pincodeCourierMapping.codAir}
                             </td>
                             <td>
                                 ${pincodeCourierMapping.codGround}
                             </td>
                             <td>
                                 ${pincodeCourierMapping.routingCode}
                             </td>
                        </tr>
                       </c:forEach>
                        </table>
                        </c:if>
                    <br/>
        </s:form>
    </s:layout-component>
</s:layout-render>