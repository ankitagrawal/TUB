<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
    <s:layout-component name="content">
        <s:useActionBean beanclass="com.hk.web.action.admin.user.UserReferrralsAddressesAction" var="referralBean"/>
        <h2>User Referral Details</h2>
        <c:forEach items="${referralBean.referredUsers}" var="refUser" varStatus="userCount">
            <table>
                <tr>
                    <td>
                        <c:if test="${!empty refUser.id}">
                            <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction">
                                <s:param name="userFilterDto.login" value="${refUser.login}"/>
                                ${refUser.id}
                            </s:link>
                        </c:if>
                    </td>
                    <td>
                            ${refUser.email}
                    </td>
                </tr>
            </table>
            <hr/>
        </c:forEach>
    </s:layout-component>
</s:layout-render>