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
        <%--<s:form beanclass="com.hk.web.action.admin.marketing.MarketingProductFeedAction">--%>
            <%--<s:label>Select your feed</s:label>--%>
            <%--&lt;%&ndash;<s:select name="marketingFeed" value="">&ndash;%&gt;--%>
                <%--&lt;%&ndash;<c:forEach items="${productFeedBean.feedNames}" var="feedName">&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<s:option value="${feedName}">${feedName}</s:option>&ndash;%&gt;--%>
                <%--&lt;%&ndash;</c:forEach>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</s:select>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<s:textarea name="productIds" value=""/>&ndash;%&gt;--%>
            <%--<br/>--%>
            <%--&lt;%&ndash;<s:submit name="getProductsForFeed" value="Get Feed Products"/>&ndash;%&gt;--%>
            <%--<s:submit name="saveProductsForFeed" value="Add Products"/>--%>
            <%--<s:submit name="removeProductsFromFeed" value="Remove Products"/>--%>
        <%--</s:form>--%>
    </s:layout-component>
</s:layout-render>
