<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction" var="updateReachEngineAction"/>
    <s:layout-component name="heading">
        Enter Values For Courier Pricing
    </s:layout-component>
    <s:layout-component name="content">
        <div>
            <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction" name="updatePricing">
                <fieldset style="float:left;" width="60%">

                    <s:submit name="getCourierPricing" value="Update Pricing"/>

                </fieldset>
            </s:form>
        </div>

        <div class="clear">
        </div>




        <table>
            <fieldset style="float:left;">
                <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction">
                    <s:hidden name="hkReachPricingEngine.id"/>

                    <tr>
                        <td>Select Warehouse:</td>
                        <td>
                            <s:select name="hkReachPricingEngine.warehouse">
                                <c:forEach items="${updateReachEngineAction.warehouses}" var="warehouse">
                                    <s:option value="${warehouse.id}">${warehouse.name}</s:option>
                                </c:forEach>
                            </s:select>
                        </td>

                    <s:select name="hkReachPricingEngine.hub">
                        <s:option value="">-Select-</s:option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers"
                                                   value="id" label="name"/>
                    </s:select>
                    <tr>
                        <td>Inter City Cost:</td>
                        <td><s:text name="hkReachPricingEngine.interCityCost"/></td>
                    </tr>

                    <tr>
                        <td>Last Mile Cost:</td>
                        <td><s:text name="hkReachPricingEngine.lastMileCost"/></td>
                    </tr>


                    <div class="clear"></div>
                    <s:submit name="save" value="Save Values"/>
                </s:form>
            </fieldset>
        </table>




    </s:layout-component>
</s:layout-render>