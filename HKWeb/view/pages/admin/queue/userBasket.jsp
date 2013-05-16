<%@ page import="com.hk.pact.dao.BaseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.queue.Bucket" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.AssignUserBasketAction" var="userBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

    <s:layout-component name="heading">${userBean.currentBreadcrumb.name}</s:layout-component>
    <s:layout-component name="content">

        <s:useActionBean beanclass="com.hk.web.action.admin.queue.AssignUserBasketAction" var="userBean"/>
        Name: ${userBean.userName}<br/>
        Login: ${userBean.userLogin}<br/>
        <s:form beanclass="com.hk.web.action.admin.queue.AssignUserBasketAction" method="post">
            <h2>Buckets:</h2>
            <div class="checkBoxList">
                <c:forEach items="${userBean.buckets}" var="bucket" varStatus="ctr">
                    <label><s:checkbox name="buckets[${ctr.index}].selected" />
                        <c:choose>
                            <c:when test="${bucket.selected}">
                               <span style="font-weight:bold">${bucket.name}</span>
                            </c:when>
                            <c:otherwise>
                                ${bucket.name}
                            </c:otherwise>
                        </c:choose>
                        <s:hidden name="buckets[${ctr.index}].id" value="${bucket.id}"  />
                    </label>
                    <br/>
                </c:forEach>
            </div>
            <s:submit name="save" value="Save"/>
        </s:form>
    </s:layout-component>

</s:layout-render>
