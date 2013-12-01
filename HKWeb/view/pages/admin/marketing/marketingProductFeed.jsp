<%--
  Created by IntelliJ IDEA.
  User: marut
  Date: 18/04/13
  Time: 6:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.marketing.MarketingProductFeedAction" var="productFeedBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="heading">Add/Remove Products to Feed</s:layout-component>
    <s:layout-component name="content">
        <h2>Please Enter comma separated product ids for product feed : </h2>
        <s:form beanclass="com.hk.web.action.admin.marketing.MarketingProductFeedAction">
            <label>Select your feed</label>
            <s:select name="marketingFeed" value="">
                <c:forEach items="${productFeedBean.feedNames}" var="feedName">
                    <s:option value="${feedName}">${feedName}</s:option>
                </c:forEach>
            </s:select>
            <s:textarea name="productIds"/>
            <br/>
            <s:submit name="getProductsForFeed" value="Get Feed Products"/>
            <s:submit name="saveProductsForFeed" value="Add Products"/>
            <s:submit name="removeProductsFromFeed" value="Remove Products"/>
        </s:form>
    </s:layout-component>
</s:layout-render>
