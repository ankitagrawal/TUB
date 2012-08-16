<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Hub">
    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction" var="hubAction"/>
    <s:layout-component name="heading">
        Edit Hub
    </s:layout-component>
    <s:layout-component name="content">
        <div class="addHub">
            <table>
                <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction">
                    <s:hidden name="hub.id" value="${hubAction.hub.id}"/>
                    <tr>
                        <td>Name:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="hub.name" value="${hubAction.hub.name}"/></td>
                    </tr>
                    <tr>
                        <td>Address:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="hub.address" value="${hubAction.hub.address}"/></td>
                    </tr>
                    <tr>
                        <td>Pincode:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="pincode" value="${hubAction.pincode}"/></td>

                    </tr>
                    <tr>
                        <td>Country:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="hub.country" value="${hubAction.hub.country}"/></td>

                    </tr>
                    <tr>
                        <td><s:submit name="editHub" value="Add New Hub">
                            <s:param name="editExistingHub" value="true"></s:param>
                            Edit Hub
                            </s:submit>
                        </td>
                    </tr>
                </s:form>
            </table>
        </div>
    </s:layout-component>
</s:layout-render>