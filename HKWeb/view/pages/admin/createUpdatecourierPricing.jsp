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
        '<select name="courierPricingEngineList[' + nextIndex + '].courier" class="courier">' +
        '<option value="">--Select--</option>' +
         <c:forEach items="${cpea.courierList}" var="courier">
        '<option value="' + ${courier.id} + '"> ' + "${courier.name}" + '</option>' +
         </c:forEach>
        '</select>' +
        '</td>' +
        '<td>' +
        '<select name="courierPricingEngineList[' + nextIndex + '].regionType" class="regionType">' +
        '<option value="">--Select--</option>' +
         <c:forEach items="${cpea.regionTypeList}" var="regionType">
        '<option value="' + ${regionType.id} + '"> ' + "${regionType.name}" + '</option>' +
         </c:forEach>
        '</select>' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].firstBaseWt" class="firstBaseWt" />' +
        '<input type="hidden" name="courierPricingEngineList[' + nextIndex + '].warehouse" value="2" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].firstBaseCost" class="firstBaseCost" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].secondBaseWt" class="secondBaseWt" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].secondBaseCost" class="secondBaseCost" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].additionalWt" class="additionalWt" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].additionalCost" class="additionalCost" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].fuelSurcharge" class="fuelSurcharge" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].minCodCharges" class="minCodCharges" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].codCutoffAmount" class="codCutoffAmount" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].variableCodCharges" class="variableCodCharges" />' +
        '</td>' +
        '<td>' +
        '<input type="text" name="courierPricingEngineList[' + nextIndex + '].validUpto" class="validUpto" title="dd/MM/yyyy"/>' +
        '</td>' +
        '</tr>';

        $('.courier-prcng-tbl').append(newRowHtml);
        return false;
        });

        $('#save').click(function() {
            var courier = $('.courier').val();
            var regionType = $('.regionType').val();
            var firstBaseWt = $('.firstBaseWt').val();
            var firstBaseCost = $('.firstBaseCost').val();
            var secondBaseWt = $('.secondBaseWt').val();
            var secondBaseCost = $('.secondBaseCost').val();
            var additionalWt = $('.additionalWt').val();
            var additionalCost = $('.additionalCost').val();
            var fuelSurcharge = $('.fuelSurcharge').val();
            var minCodCharges = $('.minCodCharges').val();
            var codCutoffAmount = $('.codCutoffAmount').val();
            var variableCodCharges = $('.variableCodCharges').val();
            var validUpto = $('.validUpto').val();
            if(courier == "" || regionType == "" || firstBaseWt == "" || firstBaseCost == "" || secondBaseWt == "" || secondBaseCost == "" || additionalWt == "" ||
                   additionalCost == "" || fuelSurcharge == "" || minCodCharges == "" || codCutoffAmount == "" || variableCodCharges == "" || validUpto == "") {
                alert("All fields are compulsory");
                return false;
            }

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
                                <th>Valid Upto</th>
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
                                        <input type="hidden" name="courierPricingEngineList[${ctr.index}].regionType" value="${courierPricingEngine.regionType.id}" class="regionType" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].firstBaseWt" value="${courierPricingEngine.firstBaseWt}" class="firstBaseWt" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].firstBaseCost" value="${courierPricingEngine.firstBaseCost}" class="firstBaseCost" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].secondBaseWt" value="${courierPricingEngine.secondBaseWt}" class="secondBaseWt" />

                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].secondBaseCost" value="${courierPricingEngine.secondBaseCost}" class="secondBaseCost" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].additionalWt" value="${courierPricingEngine.additionalWt}" class="additionalWt" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].additionalCost" value="${courierPricingEngine.additionalCost}" class="additionalCost" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].fuelSurcharge" value="${courierPricingEngine.fuelSurcharge}" class="fuelSurcharge" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].minCodCharges" value="${courierPricingEngine.minCodCharges}" class="minCodCharges" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].codCutoffAmount" value="${courierPricingEngine.codCutoffAmount}" class="codCutoffAmount" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].variableCodCharges" value="${courierPricingEngine.variableCodCharges}" class="variableCodCharges" />
                                    </td>

                                    <td>
                                        <input type="text" name="courierPricingEngineList[${ctr.index}].validUpto" value="${hk:formatDateUI(courierPricingEngine.validUpto)}" class="validUpto" style="width:90px;"/>
                                    </td>
                                </tr>

                            </c:forEach>

                        </tbody>

                    </table>

                <a href="#" class="addRowButton" style="font-size:1.2em">Add new row</a> <br/>
                <s:submit name="save" id="save" value="Save Values" />

                </c:if>

            </fieldset>


        </s:form>

    </s:layout-component>

</s:layout-render>