<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:useActionBean beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction" var="updateReachEngineAction"/>

    <s:layout-component name="content">

        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

        <script type="text/javascript">

            $(document).ready(function() {
                $('.save').click(function(e) {
                    var parentRow = $(this).parent().parent();
                    var fixedCost = parentRow.find('.fixedCost').val();
                    var interCityCost = parentRow.find('.interCityCost').val();
                    if (isNaN(fixedCost) || isNaN(interCityCost) || fixedCost < 0 || interCityCost < 0 ||
                                                                           fixedCost == "" || interCityCost == "") {
                        alert("Fixed cost and Inter City Cost should be numbers greater than 0");
                        e.preventDefault();
                        return false;
                    }
                    return true;
                });

                $('.save1').click(function(e) {
                    var fixedCost1 = $('.fixedCost1').val();
                    var interCityCost1 = $('.interCityCost1').val();
                    if (isNaN(fixedCost1) || isNaN(interCityCost1) || fixedCost1 < 0 || interCityCost1 < 0 ||
                                                                     fixedCost1 == "" || interCityCost1 == "") {
                        alert("Fixed cost and Inter City Cost should be numbers greater than 0");
                        e.preventDefault();
                        return false;
                    }
                    return true;
                });
            });

        </script>
        <s:form beanclass="com.hk.web.action.admin.courier.CreateUpdateHKReachPricingEngineAction">
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

            <fieldset style="float:left;">
                <table>
                    <s:hidden name="hkReachPricingEngine.id"/>
                    <tr>
                        <td>Warehouse:</td>
                        <td>
                            <s:select name="hkReachPricingEngine.warehouse">
                                <c:forEach items="${updateReachEngineAction.onlineWarehouses}" var="warehouse">
                                    <s:option value="${warehouse.id}">${warehouse.identifier}</s:option>
                                </c:forEach>
                            </s:select>
                        </td> &nbsp;&nbsp;
                        <td>Hub: </td>
                        <td>
                            <s:select name="hkReachPricingEngine.hub">
                                <c:forEach items="${updateReachEngineAction.hubs}" var="hub">
                                    <s:option value="${hub.id}">${hub.name}</s:option>
                                </c:forEach>
                            </s:select>
                        </td>&nbsp;&nbsp;
                        <td>Inter City Cost(Rs. per kg):</td>
                        <td><s:text name="hkReachPricingEngine.interCityCost" class="interCityCost1" /></td>&nbsp;&nbsp;
                        <td>Fixed Hub Cost(Rs. per kg):</td>
                        <td><s:text name="hkReachPricingEngine.fixedCost" class="fixedCost1" /></td>
                        <td> Valid From </td>
                        <td>
                                <s:text name="hkReachPricingEngine.validFrom" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                    class="validFrom input_tip date_input" style="width:75px;" />
                        </td>
                        <td><s:submit name="add" value="Add Values" class="save1" /></td>
                    </tr>

                </table>
            </fieldset>

        <div style="clear:both;">
            <c:if test="${not empty updateReachEngineAction.hkReachEngines}">
                <div id="hkReachTable">
                    <table>
                        <thead><tr>
                            <th style="width: 70px;">S No.</th>
                            <th style="width:150px;">Warehouse</th>
                            <th style="width: 150px;">Hub</th>
                            <th style="width: 70px;">Inter City Cost(Rs. per kg)</th>
                            <th style="width: 70px;">Fixed Hub Cost(Rs. per kg)</th>
                            <th style="width: 70px;">Valid From</th>
                            <th style="width: 75px;"> Tick to update</th>
                        </tr></thead>
                        <c:forEach items="${updateReachEngineAction.hkReachEngines}" var="hkRE" varStatus="ctr">
                            <tbody><tr count="${ctr.index}">
                                    <td>
                                        ${ctr.index + 1}
                                        <input type="hidden" name="hkReachEngines[${ctr.index}].id" value="${hkRE.id}" />
                                    </td>
                                    <td>
                                        ${hkRE.warehouse.identifier}
                                        <input type="hidden" name="hkReachEngines[${ctr.index}].warehouse" value="${hkRE.warehouse.id}" />
                                    </td>
                                    <td>
                                        ${hkRE.hub.name}
                                            <input type="hidden" name="hkReachEngines[${ctr.index}].hub" value="${hkRE.hub.id}" />
                                    </td>
                                    <td>
                                        <s:text name="hkReachEngines[${ctr.index}].interCityCost" value="${hkRE.interCityCost}" class="interCityCost" />
                                    </td>
                                    <td>
                                        <s:text name="hkReachEngines[${ctr.index}].fixedCost" value="${hkRE.fixedCost}" class="fixedCost" />
                                    </td>
                                    <td>
                                        <s:text name="hkReachEngines[${ctr.index}].validFrom" value="${hkRE.fixedCost}"
                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                class="validFrom input_tip date_input" style="width:75px;" />
                                    </td>
                                    <td>
                                        <s:checkbox name="hkReachEngines[${ctr.index}].selected" />
                                    </td>
                            </tr></tbody>
                        </c:forEach>
                    </table>
                </div>
                <s:submit name="save" class="green save" >Save</s:submit>
            </c:if>
        </div>
        </s:form>


    </s:layout-component>
</s:layout-render>
