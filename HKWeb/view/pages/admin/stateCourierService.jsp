<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction" var="scsaBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="State Courier Service">

    <s:layout-component name="heading">State Courier Service</s:layout-component>
    <s:layout-component name="content">
        <table>
            <tr>
                <th>State</th>
                <br/>

            </tr>
            <tr>
                <s:form beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction">
                    <td>
                        <s:select name="state">
                            <s:options-collection collection="${scsaBean.stateList}"/>
                        </s:select>
                    </td>
                    <td>
                        <s:submit name="search" value="Save"/>
                    </td>
                </s:form>
            </tr>
            <c:if test="${scsaBean.state != null}">
                <table>
                    <tr> <th>Selected State</th> </tr>
                      <tr>  ${scsaBean.state}   </tr>
                     <tr>   </tr>   <tr>   </tr>
                     <tr>
                        <th>S.No.</th>
                        <th>Courier Name</th>
                        <th>Preference</th>
                    </tr>
                    <tr>
                        <c:forEach items="${scsaBean.stateCourierServiceList}" var="stateCourierService"
                                   varStatus="count">
                            <td> ${count.index+1}</td>
                            <td>
                             <td>:</td>
                                <td><s:text name="stateCourierService.courier.name"/></td>

                            <%--${stateCourierService.courier.name}--%>
                            </td>
                            <td>
                              <td>First Base Weight:</td>
                                <td><s:text name="stateCourierService.preference"/></td>
                            <%--${stateCourierService.preference}--%>

                            </td>
                        </c:forEach>
                    </tr>

                </table>
            </c:if>
        </table>
    </s:layout-component>
</s:layout-render>
