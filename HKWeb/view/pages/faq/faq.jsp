<%@ page import="com.hk.constants.FaqCategoryEnums" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="primaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqPrimaryCateogry.getAll() %>"/>
<c:set var="secondaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqSecondaryCateogry.getAll() %>"/>
<s:layout-render name="/layouts/default100.jsp" pageTitle="FAQs: Nutrition">
  <s:useActionBean beanclass="com.hk.web.action.faq.FaqAction" var="faqBean"/>
  <s:layout-component name="htmlHead">
    <style type="text/css">
      p {
        text-align: justify;
        margin-right: 10px;
        font: 12px;
      }

      .faq-question {
        font-size: 1.1em;
      }

      .faq-answer {
        font-size: 1em;
      }

      .question-textarea {
        height: 3.5em;
        width: 55em;
        margin-left: 2px;
        margin-top: 5px;
      }

      .highlight-text {
        background-color: #FFFF88;
      }
    </style>
    <script>
      $(document).ready(function() {
        $('#show-new-faq-form').click(function(event) {
          event.preventDefault();
          $('#new-faq-form ').fadeToggle('medium');
        });

        $('.show-edit-faq-form').click(function(event) {
          event.preventDefault();
          $(this).closest('.faq').find('.faq-question').fadeToggle('fast');
          $(this).closest('.faq').find('.faq-answer').fadeToggle('fast');
          $(this).closest('.faq').find('.edit-faq-form').fadeToggle('fast');
        });

        highlight("faq-area", $('#search-string').val());

        $('.popup_window').popupWindow({
          height: 600,
          width: 900
        });
      })


      function highlight(id, options) {
        var o = {
          words: options,
          caseSensitive: false,
          wordsOnly: true,
          template: '$1<span class="highlight-text" style="color: red;">$2</span>$3'
        }, pattern;
        $.extend(true, o, options || {});

        if (o.words.length == 0) {
          return;
        }
        pattern = new RegExp('(>[^<.]*)(' + o.words + ')([^<.]*)', o.caseSensitive ? "" : "ig");

        $('#' + id).each(function() {
          var content = $(this).html();
          if (!content) return;
          $(this).html(content.replace(pattern, o.template));
        });
      }
      ;
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
    <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>
  </s:layout-component>

  <s:layout-component name="content">
    <div class="grid_24">

    <fieldset class="right_label">
      <s:form beanclass="com.hk.web.action.faq.FaqAction">
        <label>Search FAQs</label>
        &nbsp;&nbsp;&nbsp;
        <s:text name="searchString" id="search-string" style="width:30em; margin-top: 10px"/>
        <s:submit name="searchFaq" value="Search" style="float:right; margin-right: 200px;"/>
      </s:form>
    </fieldset>
    <br/>

    <%--<h1 id="top">FAQs: Weight Management</h1>--%>
    <shiro:hasRole name="<%=RoleConstants.ADMIN%>">
      <s:link href="#" id="show-new-faq-form"
              class="popup" style="margin-bottom:20px;">Add FAQ
      </s:link>
    </shiro:hasRole>

    <s:form beanclass="com.hk.web.action.faq.FaqAction" id="new-faq-form" style="display:none;">
      <span class="question"> Question: </span><br/>
      <textarea class="question-textarea" name="question">${faqBean.question}</textarea>
      <br/><br/>
      <span class="answer"> Answer: </span>
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
      <s:select name="secondaryCategory">
        <s:option value="">-Select-</s:option>
        <c:forEach items="${secondaryCategoryList}" var="secondaryCategory">
          <s:option value="${secondaryCategory}">${secondaryCategory}</s:option>
        </c:forEach>
      </s:select>
      <br/>
      <span class="question"> Keyword String</span>
      <s:text name="keywordString" style="width: 25em; margin-top:5px;"/>
      <s:submit name="addNewFaq" value="Save"/>
    </s:form>
    <br/>
    <br/>

    <h4>${faqBean.primaryCategory}</h4>

    <fieldset id="faq-area">
      <c:forEach items="${faqBean.faqList}" var="faq" varStatus="faqctr">
        <fieldset class="faq">

          <p class="faq-question" style="margin-top:10px;"><strong>Q. </strong>
              ${faq.question}
          </p>

          <p class="faq-answer">
            <strong>A. </strong>
          <fieldset class="faq-answer">${faq.answer}</fieldset>
          </p>

          <shiro:hasRole name="<%=RoleConstants.ADMIN%>">
            <s:link beanclass="com.hk.web.action.faq.FaqAction"
                    event="editFaq"
                    class="popup_window popup"
                    style="float:right;">
              Edit Faq
              <s:param name="faq" value="${faq.id}"/>
            </s:link>
          </shiro:hasRole>


        </fieldset>
      </c:forEach>

    </fieldset>


  </s:layout-component>
</s:layout-render>

