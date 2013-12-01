<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.email.EmailListByCategoryAction" event="pre" var="userBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="EmailListByCategoryAction">

    <s:layout-component name="heading">Emailing List</s:layout-component>
    <s:layout-component name="content">
        <div align="center" style="position:absolute;
            visibility:visible;
            width:200px;
            height:22px;
            background-color:orange;
            border:1px none #000000">
            <s:link href="/${userBean.mailingListCSV_URL}">
                Download ${userBean.category} mailing list
            </s:link>
        </div>
        <%--
                <textarea rows="10" cols="100">
                        name,email
                    <c:forEach var="user" items="${userBean.userList}">
                        ${user.name},${user.email}
                    </c:forEach></textarea>
        --%>
    </s:layout-component>

</s:layout-render>
