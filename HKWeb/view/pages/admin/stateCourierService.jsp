<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction" var="scsaBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="State Courier Service">
    <s:layout-component name="heading">State Courier Service</s:layout-component>
    <s:layout-component name="content">
        <div style="height:100px;">
            <table>
                <tr>
                    <th>State</th>
                    <br/>

                </tr>
                <tr>
                    <s:form beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">
                                              <td>
                         <s:select name="state">
                        <s:option value="">-Select-</s:option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="stateList"
                                                   value="id" label="name"/>
                    </s:select>
                        </td>
                        <td>
                            <s:submit name="search" value="Save"/>
                        </td>
                    </s:form>
                </tr>
            </table>
        </div>

        <div class="clear" style="height:100px;"></div>

        <div>
          <s:form beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">
             <s:hidden name="state"  value="${scsaBean.state}"/>
            <table >
                <%--<c:if test="${scsaBean.state != null}">--%>
                <tr>
                    <td style="font-weight:bold;">Selected State</td> <td> ${scsaBean.state.name}</td>
                </tr>


                <tr>
                    <th>S.No.</th>
                    <th>Courier Name</th>
                    <th>Preference</th>

                </tr>
                <div>
                    <c:forEach items="${scsaBean.stateCourierServiceList}" var="stateCourierService" varStatus="count">
                       <tr >
                        <td> ${count.index+1}</td>
                        <td>
                                ${stateCourierService.courier.name}
                        </td>
                        <td>
                                ${stateCourierService.preference}
                        </td>

                    </tr>

                    </c:forEach>
                         </div>

            </table>
           </div>



    <c:if test="${scsaBean.displayAddNewRow}">
        <table>
         <s:form beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">
             <s:hidden name="stateCourierService.state"  value="${scsaBean.state.id}"/>
         <tr>
                    <td>
                      Select Courier :
                    <s:select name="stateCourierService.courier">
                        <s:option value="">-Select-</s:option>
                        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers"
                                                   value="id" label="name"/>
                    </s:select></td>
                <td>
                  <s:text name="stateCourierService.preference"></s:text>
                </td>
             <td>
            <s:submit name="save" value="Save Row"></s:submit>
             </td>
               </tr>
           </s:form>
          </table>
</c:if>

        <div class="clear">
                    </div>

       <div>
            <div>
           <s:submit name="addNewRow" value="Add Row" />
            </div>
               </s:form>
        </div>



    </s:layout-component>
</s:layout-render>
