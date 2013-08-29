<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction" var="updateReachEngineAction"/>
    <s:layout-component name="content">

        <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction" id="searchForm">
            <h4>Search HKReach Pricing Engine </h4>
            <br>Warehouse
            <s:select name="warehouseParam">
                <s:option>- Select -</s:option>
                <c:forEach items="${updateReachEngineAction.onlineWarehouses}" var="warehouse">
                    <s:option value="${warehouse.id}">${warehouse.identifier}</s:option>
                </c:forEach>
            </s:select> &nbsp;&nbsp;
            Hub
            <s:select name="hubParam">
                <s:option value="">- Select -</s:option>
                <c:forEach items="${updateReachEngineAction.hubs}" var="hub">
                    <s:option value="${hub.id}">${hub.name}</s:option>
                </c:forEach>
            </s:select>
            <s:submit name="search"  value="Search"/>
        </s:form>

        <fieldset style="float:left;">
            <table>
                <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction">
                    <s:hidden name="hkReachPricingEngine.id"/>
                    <tr>
                        <td>Select Warehouse:</td>
                        <td>
                            <s:select name="hkReachPricingEngine.warehouse">
                                <c:forEach items="${updateReachEngineAction.onlineWarehouses}" var="warehouse">
                                    <s:option value="${warehouse.id}">${warehouse.identifier}</s:option>
                                </c:forEach>
                            </s:select>
                        </td> &nbsp;&nbsp;
                        <td>Select Hub: </td>
                        <td>
                            <s:select name="hkReachPricingEngine.hub">
                                <s:option value="">-Select-</s:option>
                                <<c:forEach items="${updateReachEngineAction.hubs}" var="hub">
                                <s:option value="${hub.id}">${hub.name}</s:option>
                                </c:forEach>
                            </s:select>
                        </td>&nbsp;&nbsp;
                        <td>Inter City Cost(per kg):</td>
                        <td><s:text name="hkReachPricingEngine.interCityCost"/></td>&nbsp;&nbsp;
                        <td>Fixed Cost(per kg):</td>
                        <td><s:text name="hkReachPricingEngine.fixedCost"/></td>
                        <td><s:submit name="saveOrUpdate" value="Add Values"/></td>
                    </tr>

                </s:form>
            </table>
        </fieldset>
        <div style="clear:both;">
            <c:if test="${not empty updateReachEngineAction.hkReachEngines}">
                <div id="hkReachTable">
                    <table style="width:100%;">
                        <thead><tr>
                            <th>S No.</th>
                            <th style="width:150px;">Warehouse</th>
                            <th >Hub</th>
                            <th>Inter City Cost(per kg)</th>
                            <th>Fixed Cost(per kg)</th>
                            <th> &nbsp;</th>
                        </tr></thead>
                        <%int count=0; %>
                        <c:forEach items="${updateReachEngineAction.hkReachEngines}" var="hkRE">
                            <tbody><tr>
                                <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction">
                                    <td><%=++count %>
                                        <s:hidden name="hkReachPricingEngine.id" value="${hkRE.id}" />
                                    </td>
                                    <td><s:select name="hkReachPricingEngine.warehouse" value="${hkRE.warehouse}">
                                        <c:forEach items="${updateReachEngineAction.onlineWarehouses}" var="hkWarehouse">
                                            <s:option value="${hkWarehouse.id}">${hkWarehouse.identifier}</s:option>
                                        </c:forEach>
                                    </s:select>
                                    </td>
                                    <td>
                                        <s:select name="hkReachPricingEngine.hub" value="${hkRE.hub.id}">
                                            <c:forEach items="${updateReachEngineAction.hubs}" var="hub">
                                                <s:option value="${hub.id}">${hub.name}</s:option>
                                            </c:forEach>
                                        </s:select>
                                    </td>
                                    <td><s:text name="hkReachPricingEngine.interCityCost" value="${hkRE.interCityCost}" /></td>
                                    <td><s:text name="hkReachPricingEngine.fixedCost" value="${hkRE.fixedCost}" /></td>
                                    <td >
                                        <s:submit beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction"
                                                  name="saveOrUpdate" class="green" >Save</s:submit>
                                    </td>
                                </s:form>
                            </tr></tbody>
                        </c:forEach>
                    </table>
                </div>
            </c:if>
        </div>


    </s:layout-component>
</s:layout-render>
