<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.webServices.TestMailBoltServicesAction" var="mbAction"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Test Web Services">
    <s:layout-component name="heading">Web Services</s:layout-component>
    <s:layout-component name="content">
        <table>
            <s:form beanclass="com.hk.web.action.admin.webServices.TestMailBoltServicesAction" event="testServices">
                <tr>
                    <td>
                        get users from product Ids
                    </td>
                    <td>
                        <s:textarea name="params"></s:textarea>
                    </td>
                    <td>
                </tr>
                <td>
                    <tr>
                        <td>
                            get projected info: 1 for projected info 0 for entire users
                        </td>
                        <td>
                            <s:text name="minimum"></s:text>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            get verified: true, false or leave blank
                        </td>
                        <td>
                            <s:text name="verified"></s:text>
                        </td>
                    </tr>
                </td>
                </tr>
                <tr>
                    <td>
                        get users from product variant Ids
                    </td>
                    <td>
                        <s:textarea name="pvs"></s:textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        cities
                    </td>
                    <td>
                        <s:textarea name="cities"></s:textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        zones
                    </td>
                    <td>
                        <s:textarea name="zones"></s:textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        states
                    </td>
                    <td>
                        <s:textarea name="states"></s:textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        categories
                    </td>
                    <td>
                        <s:textarea name="categories"></s:textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        emails
                    </td>
                    <td>
                        <s:textarea name="emails"></s:textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        storeIds
                    </td>
                    <td>
                        <s:textarea name="storeIds"></s:textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        UserOrderCount
                    </td>
                    <td>
                        <s:text name="userOrderCount"></s:text>
                    </td>
                    <td>
                        equality
                    </td>
                    <td>
                        <s:text name="equality"></s:text>
                    </td>
                </tr>
                <tr>
                    <td>
                        <s:submit name="testServices" value="Search"></s:submit>
                    </td>
                </tr>
                <tr>
                    <td>
                        number of results ${mbAction.result}
                    </td>
                    <td>
                        time taken (ms) ${mbAction.time}
                    </td>
                </tr>
            </s:form>
        </table>
    </s:layout-component>
</s:layout-render>
