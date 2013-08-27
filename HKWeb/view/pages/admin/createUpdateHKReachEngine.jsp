<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction" var="updateReachEngineAction"/>
    <s:layout-component name="heading">
        Enter Values For Courier Pricing
    </s:layout-component>
    <s:layout-component name="content">

        <table>
            <fieldset style="float:left;">
                <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction">
                    <s:hidden name="hkReachPricingEngine.id"/>

                    <tr>
                        <td>Select Warehouse:</td>
                        <td>
                            <s:select name="hkReachPricingEngine.warehouse">
                                <c:forEach items="${updateReachEngineAction.onlineWarehouses}" var="warehouse">
                                    <s:option value="${warehouse.id}">${warehouse.name}</s:option>
                                </c:forEach>
                            </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Select Hub: </td>
                        <td>
                        <s:select name="hkReachPricingEngine.hub">
                            <s:option value="">-Select-</s:option>
                            <<c:forEach items="${updateReachEngineAction.hubs}" var="hub">
                            <s:option value="${hub.id}">${hub.name}</s:option>
                        </c:forEach>
                        </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Inter City Cost:</td>
                        <td><s:text name="hkReachPricingEngine.interCityCost"/></td>
                    </tr>

                    <tr>
                        <td>Fixed Cost:</td>
                        <td><s:text name="hkReachPricingEngine.fixedCost"/></td>
                    </tr>


                    <div class="clear"></div>
                    <s:submit name="save" value="Save Values"/>
                </s:form>
            </fieldset>
        </table>

    </s:layout-component>
</s:layout-render>
