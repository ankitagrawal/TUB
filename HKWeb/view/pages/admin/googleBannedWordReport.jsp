<%--
  Created by IntelliJ IDEA.
  User: Developer
  Date: Jan 2, 2012
  Time: 5:57:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.marketing.GoogleBannedWordAction" var="googleBannedWordReportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Google Banned Word Report">
    <div align="center">
        <s:layout-component name="heading">Google Banned Words Report</s:layout-component>
        <s:layout-component name="content">

            <table>
                <thead>
                <tr>
                    <th>Banned Word</th>
                    <th>Product Name</th>
                    <th>Product ID</th>
                </tr>
                </thead>
                <c:forEach items="${googleBannedWordReportBean.googleBannedWordDtoList}" var="googleBannedWordDto">
                    <tr>
                        <td>${googleBannedWordDto.googleBannedWord.bannedWord}</td>
                        <td><s:link
                                href="/product/${googleBannedWordDto.product.slug}/${googleBannedWordDto.product.id}"
                                target="_blank"> ${googleBannedWordDto.product.name}</s:link></td>
                        <td>${googleBannedWordDto.product.id}</td>
                    </tr>
                </c:forEach>
            </table>
        </s:layout-component>
    </div>
</s:layout-render>