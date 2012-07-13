<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
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
        </style>
    </s:layout-component>

    <s:layout-component name="content">
        <div class="grid_24">
        <%--<h1 id="top">FAQs: Weight Management</h1>--%>

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

