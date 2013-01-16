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
        <div id ="error"></div>
        <s:messages/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
        <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
        <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>
        <s:form beanclass="com.hk.web.action.admin.newsletter.EmailNewsletterCampaignAction" var ="enca">
            <fieldset class="right_label">
                <legend>Create Mail Template</legend>
                <c:if test="${cmta.editTemplate}">
                    <s:hidden name="mail.id"/>
                </c:if>
                <ul class="mail">
                    <li>
                        <label>Mail Template Name</label>
                        <s:text name="mail.name" id="name"/>
                        <s:form beanclass="com.hk.web.action.admin.review.CreateMailTemplateAction" var ="cmta"><s:submit name="searchMail" value="Search" /></s:form>
                    </li>
                    <li>
                        Modify Content: <s:file name="contentBean"/><br/>
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
                        <s:submit name="collectionReview" value="GENERATE FTL" id="btn"/>
                    </li>
                </ul>
            </fieldset>
        </s:form>
        <s:link beanclass="com.hk.web.action.admin.review.ReviewMailSettingsAction">BACK </s:link>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#btn').click(function() {
                    var name = $('#name').val();
                    var validNameRegex = /^[A-Za-z0-9_]+$/;
                    if (!validNameRegex.test(name)) {
                        $('#error').html("Invalid Mail type name: " + name + " . Only Alphanumeric characters and underscore is allowed!");
                        return false;
                    } else {
                        return true;
                    }
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>