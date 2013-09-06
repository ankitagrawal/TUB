<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction" var="updateReachEngineAction"/>

    <s:layout-component name="content">
        <script type="text/javascript">

            $(document).ready(function() {
                $('.save').click(function(e) {
                    var parentRow = $(this).parent().parent();
                    var fixedCost = parentRow.find('.fixedCost').val();
                    var interCityCost = parentRow.find('.interCityCost').val();
                    if (isNaN(fixedCost) || isNaN(interCityCost) || fixedCost < 0 || interCityCost < 0 || fixedCost == "" || interCityCost == "") {
                        alert("Fixed cost and Inter City Cost should be numbers greater than 0");
                        e.preventDefault();
                        return false;
                    }
                    return true;
                });
            });

        </script>
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
                                <c:forEach items="${updateReachEngineAction.hubs}" var="hub">
                                <s:option value="${hub.id}">${hub.name}</s:option>
                                </c:forEach>
                            </s:select>
                        </td>&nbsp;&nbsp;
                        <td>Inter City Cost(Rs. per kg):</td>
                        <td><s:text name="hkReachPricingEngine.interCityCost" class="interCityCost"/></td>&nbsp;&nbsp;
                        <td>Fixed Hub Cost(Rs. per kg):</td>
                        <td><s:text name="hkReachPricingEngine.fixedCost" class="fixedCost"/></td>
                        <td><s:submit name="saveOrUpdate" value="Add Values" class="save" /></td>
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
                            <th>Inter City Cost(Rs. per kg)</th>
                            <th>Fixed Hub Cost(Rs. per kg)</th>
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
                                    <td><s:text name="hkReachPricingEngine.interCityCost" value="${hkRE.interCityCost}" class="interCityCost" /></td>
                                    <td><s:text name="hkReachPricingEngine.fixedCost" value="${hkRE.fixedCost}" class="fixedCost" /></td>
                                    <td >
                                        <s:submit beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction"
                                                  name="saveOrUpdate" class="green save" >Save</s:submit>
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
