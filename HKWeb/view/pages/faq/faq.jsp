<%@ page import="com.hk.constants.FaqCategoryEnums" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="primaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqPrimaryCateogry.getAll() %>" />
<c:set var="secondaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqSecondaryCateogry.getAll() %>" />
<s:layout-render name="/layouts/default100.jsp" pageTitle="FAQs: Nutrition">
    <s:useActionBean beanclass="com.hk.web.action.faq.FaqAction" var="faqBean"/>
    <s:layout-component name="htmlHead">
        <style type="text/css">
            p {
                text-align: justify;
                margin-right: 10px;
                font: 12px;
            }

            .question {
                font-size: 1.1em;
            }

            .question-textarea {
                height: 3.5em;
                width: 55em;
                margin-left: 2px;
                margin-top: 5px;
            }
        </style>
        <script>
            $(document).ready(function() {
                $('#show-new-faq-form').click(function(event) {
                    event.preventDefault();
                    $('#new-faq-form ').slideDown();
                });
            })
        </script>
    </s:layout-component>

    <s:layout-component name="content">
        <div class="grid_24">
        <%--<h1 id="top">FAQs: Weight Management</h1>--%>
        <br/>
        <s:link href="#" id="show-new-faq-form"
                class="popup">Add FAQ
        </s:link>
        <br/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
        <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
        <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>

        <s:form beanclass="com.hk.web.action.faq.FaqAction" id="new-faq-form" style="display:none;">
            <span class="question"> Question: </span><br/>
            <textarea class="question-textarea" name="question">${faqBean.question}</textarea>
            <br/><br/>
            <span class="question"> Answer: </span>
            <br/>
            <textarea cols="80" id="answer" name="answer" rows="6">${faqBean.answer}</textarea>
            <script type="text/javascript">
                //<![CDATA[
                CKEDITOR.replace('answer',
                {
                    fullPage : false,
                    extraPlugins : 'docprops'
                });

                //]]>
            </script>
            <br/>
            <span class="question"> Primary Category</span>
            <s:select name="primaryCategory">
                <s:option value="">-Select-</s:option>
                <c:forEach items="${primaryCategoryList}" var="primaryCategory">
                    <s:option value="${primaryCategory}">${primaryCategory}</s:option>
                </c:forEach>
            </s:select>
            <span class="question" style="margin-left: 30px;"> Secondary Category</span>
            <s:select name="primaryCategory">
                <s:option value="">-Select-</s:option>
                <c:forEach items="${secondaryCategoryList}" var="secondaryCategory">
                    <s:option value="${secondaryCategory}">${secondaryCategory}</s:option>
                </c:forEach>
            </s:select>
            <br />
            <span class="question"> Keyword String</span>
            <s:text name="keywordString" style="width: 25em; margin-top:5px;" />
            <s:submit name="addNewFaq" value="Save"/>
        </s:form>
        <br/>
        <br/>

        <h4>${faqBean.primaryCategory}</h4>

        <ul type="square">
            <c:forEach items="${faqBean.faqList}" var="faq" varStatus="faqctr">

                <p class="question" id="250856"><strong>Q. </strong>
                        ${faq.question}</p>

                <p><strong>A. </strong>${faq.answer}</p>

            </c:forEach>

        </ul>


    </s:layout-component>
</s:layout-render>

