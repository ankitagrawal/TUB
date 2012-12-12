<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Marut
  Date: 10/19/12
  Time: 4:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<s:useActionBean beanclass="com.hk.web.action.admin.crm.UserListByPhoneAction" var="userAdmin" event="pre"/>
<s:layout-render name="/layouts/agentLayout.jsp" pageTitle="User Orders" >
    <s:layout-component name="content">
        <s:form beanclass="com.hk.web.action.admin.crm.UserListByPhoneAction" method="get" style="">
            <s:text name="email" value="Email Id"  style="width: 300px"/>
            <s:hidden name="phone" value="${userAdmin.phone}"/>
            <s:submit name="searchByEmail" value="Search by Email" />
            </br>
            Users for phone: ${userAdmin.phone}
            <table>
                <th>Email Id</th>
                <th>Phone</th>
                <c:forEach items="${userAdmin.userDetailList}" var="user">
                <tr>
                    <td><s:link beanclass="com.hk.web.action.admin.crm.AgentSearchOrderAction"
                                event="pre">
                        <s:param name="phone" value="${userAdmin.phone}"/>
                        <s:param name="email" value="${user.user.email}"/>
                        <%--Hard-coded for auth--%>
                        <s:param name="key" value="US3jbSEN5EKVVzlabDl95loyWf_hloCZ"/>
                        ${user.user.email}</s:link><br/>
                    </td>
                    <td>
                        ${user.phone}
                    </td>
                </tr>
                </c:forEach>
            </table>
        </s:form>
    </s:layout-component>
 </s:layout-render>