<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateCourierPricingAction" var="cpea"/>
    <s:layout-component name="heading">
        Enter Values For Courier Pricing
    </s:layout-component>
    <s:layout-component name="content">
        <div>
            <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateCourierPricingAction" name="updatePricing">
                <fieldset style="float:left;" width="60%">

                    Select Courier :
                    <s:select name="courier">
                        <s:option value="">-Select-</s:option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers"
                                                   value="id" label="name"/>
                    </s:select>
                    <td> Select Region:</td>
                    <td><s:select name="regionType">
                        <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                   serviceProperty="regionTypeList"
                                                   value="id" label="name"/>
                        </s:select>
                    </td>
                    <s:submit name="getCourierPricing" value="Update Pricing"/>

                </fieldset>
            </s:form>
        </div>

        <div class="clear">
        </div>


          

                <table>
                    <fieldset style="float:left;">
                        <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateCourierPricingAction">
                            <s:hidden name="courierPricingEngine.id"/>
                            <s:hidden name="courierPricingEngine.courier" value="${cpea.courier}"/>
                            <s:hidden name="courierPricingEngine.regionType" value="${cpea.regionType}"/>
                            <s:hidden name="courierPricingEngine.warehouse" value="2"/>
                            <tr>
                                <td>First Base Weight:</td>
                                <td><s:text name="courierPricingEngine.firstBaseWt"/></td>
                            </tr>

                            <tr>
                                <td>First Base Cost:</td>
                                <td><s:text name="courierPricingEngine.firstBaseCost"/></td>
                            </tr>

                            <tr>
                                <td>Second Base Weight:</td>
                                <td><s:text name="courierPricingEngine.secondBaseWt"/></td>
                            </tr>

                            <tr>
                                <td>Second Base Cost:</td>
                                <td><s:text name="courierPricingEngine.secondBaseCost"/></td>
                            </tr>

                            <tr>
                                <td>Additional Weight:</td>
                                <td><s:text name="courierPricingEngine.additionalWt"/></td>
                            </tr>

                            <tr>
                                <td>Additional Cost:</td>
                                <td><s:text name="courierPricingEngine.additionalCost"/></td>
                            </tr>

                            <tr>
                                <td>Fuel Surcharge :(in Decimal)</td>
                                <td><s:text name="courierPricingEngine.fuelSurcharge"/></td>
                            </tr>

                            <tr>
                                <td>Min COD Charges</td>
                                <td><s:text name="courierPricingEngine.minCodCharges"/></td>
                            </tr>

                            <tr>
                                <td>COD Cutoff Amount:</td>
                                <td><s:text name="courierPricingEngine.codCutoffAmount"/></td>
                            </tr>

                            <tr>
                                <td>Varibale COD Charges:</td>
                                <td><s:text name="courierPricingEngine.variableCodCharges"/></td>
                            </tr>

                            <div class="clear"></div>
                            <s:submit name="save" value="Save Values"/>
                        </s:form>
                    </fieldset>
                </table>


      

    </s:layout-component>
</s:layout-render>