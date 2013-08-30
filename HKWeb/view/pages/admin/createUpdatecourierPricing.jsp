<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateCourierPricingAction" var="cpea"/>

    <s:layout-component name="htmlHead">
        <script type="text/javascript">

        $(document).ready(function(){
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
        '<select name="courierPricingEngineList[' + nextIndex + '].courier">' +
        '<option value="-1">--Select--</option>' +
         <c:forEach items="${cpea.courierList}" var="courier">
        '<option value="' + ${courier.id} + '"> ' + "${courier.name}" + '</option>' +
         </c:forEach>
        '</select>' +
        '</td>' +
        '<td>' +
        '<select name="courierPricingEngineList[' + nextIndex + '].regionType">' +
        '<option value="-1">--Select--</option>' +
         <c:forEach items="${cpea.regionTypeList}" var="regionType">
        '<option value="' + ${regionType.id} + '"> ' + "${regionType.name}" + '</option>' +
         </c:forEach>
        '</select>' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].firstBaseWt" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].firstBaseCost" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].secondBaseWt" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].secondBaseCost" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].additionalWt" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].additionalCost" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].fuelSurcharge" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].minCodCharges" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].codCuttoffAmount" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].variableCodCharges" />' +
        '</td>' +
        '</tr>';

        $('.courier-prcng-tbl').append(newRowHtml);
        return false;
        });
        });
        </script>

    </s:layout-component>

    <s:layout-component name="heading">
        Search and Update Courier Pricing
    </s:layout-component>

    <s:layout-component name="content">

        <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateCourierPricingAction">
            <div>
                <fieldset style="float:left;" width="60%">

                    Select Courier :
                    <s:select name="courier">
                        <s:option value="">-Select-</s:option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers"
                                                   value="id" label="name"/>
                    </s:select>

                    <s:submit name="search" value="Search"/>

                </fieldset>

            </div>

            <div class="clear">

            </div>

            <fieldset>

                <c:if test="${(cpea.courierPricingEngineList != null) and fn:length(cpea.courierPricingEngineList) > 0}">
                    <h2>Courier Pricing Table</h2>
                    <br/>

                    <table class="courier-prcng-tbl">

                            <thead>
                            <tr>
                                <th>S.No</th>
                                <th>Courier</th>
                                <th>Region</th>
                                <th>First Base Weight</th>
                                <th>First Base Cost</th>
                                <th>Second Base Weight</th>
                                <th>Second Base Cost</th>
                                <th>Additional Weight</th>
                                <th>Additional Cost</th>
                                <th>Fuel Surcharge(in Decimal)</th>
                                <th>Min COD Charge</th>
                                <th>COD Cutoff Amount</th>
                                <th>Variable COD Charges</th>
                            </tr>
                            </thead>


                        <tbody>
                            <c:forEach items="${cpea.courierPricingEngineList}" var="courierPricingEngine" varStatus="ctr">
                                <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                                    <td>
                                        ${ctr.index+1}.
                                        <input type="hidden" name="courierPricingEngineList[${ctr.index}].id" value="${courierPricingEngine.id}" />
                                    </td>

                                    <input type="hidden" name="courierPricingEngineList[${ctr.index}].warehouse" value="${courierPricingEngine.warehouse.id}" />

                                    <td>
                                        ${courierPricingEngine.courier.name}
                                        <input type="hidden" name="courierPricingEngineList[${ctr.index}].courier" value="${courierPricingEngine.courier.id}" />
                                    </td>

                                    <td>
                                        ${courierPricingEngine.regionType.name}
                                        <input type="hidden" name="courierPricingEngineList[${ctr.index}].regionType" value="${courierPricingEngine.regionType.id}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].firstBaseWt" value="${courierPricingEngine.firstBaseWt}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].firstBaseCost" value="${courierPricingEngine.firstBaseCost}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].secondBaseWt" value="${courierPricingEngine.secondBaseWt}"  />

                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].secondBaseCost" value="${courierPricingEngine.secondBaseCost}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].additionalWt" value="${courierPricingEngine.additionalWt}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].additionalCost" value="${courierPricingEngine.additionalCost}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].fuelSurcharge" value="${courierPricingEngine.fuelSurcharge}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].minCodCharges" value="${courierPricingEngine.minCodCharges}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].codCutoffAmount" value="${courierPricingEngine.codCutoffAmount}" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].variableCodCharges" value="${courierPricingEngine.variableCodCharges}" />
                                    </td>

                                </tr>

                            </c:forEach>

                        </tbody>

                    </table>

                <a href="#" class="addRowButton" style="font-size:1.2em">Add new row</a> <br/>
                <s:submit name="save" value="Save Values"/>

                </c:if>

            </fieldset>


        </s:form>

    </s:layout-component>

</s:layout-render>