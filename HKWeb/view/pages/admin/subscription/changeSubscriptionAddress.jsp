<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/18/12
  Time: 2:44 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.subscription.ChangeSubscriptionAddressAction" var="subBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
    <s:layout-component name="heading">${subBean.currentBreadcrumb.name}</s:layout-component>

    <s:layout-component name="content">

        <fieldset class="right_label">
            <legend>Edit Subscription Address</legend>
            <br/>
            <div>
                <ul>
                    <li>
                        Subscription Id: ${subBean.subscription.id}
                    </li>
                    <li>
                        User Name: ${subBean.subscription.user.name}
                    </li>
                    <li>
                        User Login: ${subBean.subscription.user.login}
                    </li>
                </ul>

            </div>
            <br/>
        <div>
            <ul>
                <li>
                    <h2> Address: </h2>
                    <table border="1">
                        <c:set value="${subBean.subscription.address}" var="address"/>
                        <tr>
                            <td width="200">
                                    ${address.name}<br/>
                                    ${address.line1}<br/>
                                    ${address.line2}<br/>
                                    ${address.city} - ${address.pincode.pincode}<br/>
                                    ${address.state}<br/>
                                Ph: ${address.phone}<br/>
                            </td>
                            <td width="100">
                                <s:link beanclass="com.hk.web.action.admin.address.AdminAddressListAction" event="changeSubscriptionAddress">
                                    Get from address book
                                    <s:param name="subscription" value="${subBean.subscription.id}"/>
                                </s:link>
                            </td>
                            <td width="75">
                                <s:link beanclass="com.hk.web.action.admin.subscription.ChangeSubscriptionAddressAction" event="edit">
                                    edit
                                    <s:param name="subscription" value="${subBean.subscription.id}"/>
                                </s:link>
                            </td>
                        </tr>
                    </table>
                </li>
            </ul>

        <br/>
        </div>
        </fieldset>

    </s:layout-component>

</s:layout-render>
