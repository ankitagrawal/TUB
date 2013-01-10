<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/9/13
  Time: 11:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.review.CreateMailTemplateAction" var="cmta"    />
<s:layout-render name="/layouts/defaultAdmin.jsp">


    <s:layout-component name="content">
        <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
        <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
        <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>
        <s:form beanclass="com.hk.web.action.admin.review.CreateMailTemplateAction">
            <fieldset class="right_label">
                <legend>Create/Edit Mail Template</legend>
                <ul class="mail">
                    <li>
                        <label>Mail Template Name</label>
                        <s:text name="mail.name" /> <s:submit name="editMailTemplate" value="edit" />

                        
                    </li>
                    <li>
                        <label>Template Content</label>
                        <s:textarea  name="mail.content"  id='mail.conent'/>
                        <script type="text/javascript">
                            //<![CDATA[
                            CKEDITOR.replace('mail.content',
                                    {
                                        fullPage : false,
                                        extraPlugins : 'docprops'
                                    });

                            //]]>
                        </script>
                    </li>
                    <li>
                        <c:if test="${cmta.editTemplate}">
                            <s:submit name="saveMailTemplate" value="save"/>
                        </c:if>
                        <c:if test="${!cmta.editTemplate}">
                            <s:submit name="createMailTemplate" value="create"/>
                        </c:if>

                    </li>
                </ul>
            </fieldset>
        </s:form>
    </s:layout-component>
</s:layout-render>