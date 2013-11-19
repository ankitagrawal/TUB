<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.webServices.TestMailBoltServicesAction" var="mbAction"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Test Web Services">
    <s:layout-component name="heading">Web Services</s:layout-component>
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.webServices.TestMailBoltServicesAction" event="testServices">
            <table>
                <table>
                    <tr>
                        <strong>Search By Products</strong>
                    </tr>
                    <tr>
                        <td>Product Ids (csv)</td>
                        <td><s:textarea name="prodnames"></s:textarea></td>
                        <td>Product Variant Ids (csv)</td>
                        <td><s:textarea name="pvs"></s:textarea></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <strong>Search By Orders</strong>
                    </tr>
                    <tr>
                        <td>User Order Count (single integer value)</td>
                        <td><s:text name="userOrderCount"></s:text></td>
                        <td>Equality (single value, range: ge,gt,eq,le,lt)</td>
                        <td><s:text name="equality"></s:text></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <strong>Search By Address</strong>
                    </tr>
                    <tr>
                        <td>States (csv)</td>
                        <td><s:textarea name="states"></s:textarea></td>
                        <td>Cities (csv)</td>
                        <td><s:textarea name="cities"></s:textarea></td>
                        <td>Zones (csv), range : 1,2,3,4</td>
                        <td><s:textarea name="zones"></s:textarea></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <strong>Search By Miscellaneous Criterion</strong>
                    </tr>
                    <tr>
                        <td>Verfied (true, false or leave blank)</td>
                        <td><s:text name="verified"></s:text></td>
                        <td>Categories(csv)</td>
                        <td><s:textarea name="categories"></s:textarea></td>
                        <td>Emails (csv)</td>
                        <td><s:textarea name="emails"></s:textarea></td>
                        <td>Store Ids(csv)</td>
                        <td><s:textarea name="storeIds"></s:textarea></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <strong>Advanced Options</strong>
                    </tr>
                    <tr>
                        <td>Test as if on Production DB(overrides setting in environment.properties)</td>
                        <td><s:checkbox name="production"></s:checkbox></td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>
                            <s:submit name="testServices" value="Search"></s:submit>
                        </td>
                    </tr>
                </table>
                    <%--<table>
                        <tr>
                            <td>
                                <strong>Size of Results returned </strong> ${mbAction.result}
                            </td>
                            <td>
                                <strong>Time taken (ms) </strong> ${mbAction.time}
                            </td>
                        </tr>
                    </table>--%>
            </table>
        </s:form>
    </s:layout-component>
</s:layout-render>
