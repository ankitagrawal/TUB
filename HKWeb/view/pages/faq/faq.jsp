<%@ page import="com.hk.constants.FaqCategoryEnums" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="primaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqPrimaryCateogry.getAll() %>"/>
<c:set var="secondaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqSecondaryCateogry.getAll() %>"/>
<c:set var="primaryCategoryNutrition" value="<%= FaqCategoryEnums.EnumFaqPrimaryCateogry.Nutrition.getName()%>" />
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
        margin-bottom: 0px;
      }

      .faq-answer {
        font-size: 10px;
        margin-left: 12px;
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
      };

        function validate(){
            var question = $('#new-question').val();
            var answer = $('#new-answer').val();
            if(question == ""){
                alert('question or answer cannot be left blank');
            }
            return false;
        }
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
    <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>
  </s:layout-component>

  <s:layout-component name="content">
      <c:if test="${faqBean.primaryCategory == primaryCategoryNutrition}">
        <img src="<%=request.getContextPath()%>/images/faq/faq_nutrition.jpg" style="width: 100%; border-color:#E3E7E8; border-top-width:10px;"/>
      </c:if>
      <fieldset class="right_label">
        <s:form beanclass="com.hk.web.action.faq.FaqAction">
          <s:hidden name="primaryCategory" value="${faqBean.primaryCategory }" />
          <s:hidden name="secondaryCategory" value="${faqBean.secondaryCategory}" />
          <label style="margin-left: 100px; ">Search FAQs</label>
          &nbsp;&nbsp;&nbsp;
          <s:text name="searchString" id="search-string" style="width:30em; margin-top: 16px; padding-top:10px"/>
          <s:submit name="searchFaq" value="Search" style="float:right; margin-right:115px; margin-top:15px; padding-top:3px;"/>
        </s:form>
      </fieldset>
    <br/>

    <%--<h1 id="top">FAQs: Weight Management</h1>--%>
    <shiro:hasRole name="<%=RoleConstants.SITE_CONTENT_MANAGER%>">
      <s:link href="#" id="show-new-faq-form"
              class="popup" style="margin-bottom:20px;">Add FAQ
      </s:link>
    </shiro:hasRole>

    <s:form beanclass="com.hk.web.action.faq.FaqAction" id="new-faq-form" style="display:none;">
      <span class="question"> Question: </span><br/>
      <textarea class="question-textarea" name="faq.question" id="new-question">${faqBean.faq.question}</textarea>
      <br/><br/>
      <span class="answer"> Answer: </span>
      <br/>
      <textarea cols="80" id="new-answer" name="faq.answer" rows="6">${faqBean.faq.answer}</textarea>
      <script type="text/javascript">
        //<![CDATA[
        CKEDITOR.replace('faq.answer',
        {
          fullPage : false,
          extraPlugins : 'docprops'
        });

        //]]>
      </script>
      <br/>
      <span class="question"> Primary Category</span>
      <s:select name="faq.primaryCategory">
        <s:option value="">-Select-</s:option>
        <c:forEach items="${primaryCategoryList}" var="primaryCategory">
          <s:option value="${primaryCategory}">${primaryCategory}</s:option>
        </c:forEach>
      </s:select>
      <span class="question" style="margin-left: 30px;"> Secondary Category</span>
      <s:select name="faq.secondaryCategory">
        <s:option value="">-Select-</s:option>
        <c:forEach items="${secondaryCategoryList}" var="secondaryCategory">
          <s:option value="${secondaryCategory}">${secondaryCategory}</s:option>
        </c:forEach>
      </s:select>
      <br/>
      <span class="question"> Keyword String</span>
      <s:text name="faq.keywords" style="width: 25em; margin-top:5px;"/>
      <span class="question" style="margin-left: 20px;"> Rank</span>
      <s:text name="faq.pageRank" style="width: 20px; margin-top:5px;"/>
      <s:submit name="addNewFaq" value="Save" onclick="validate()"/>
    </s:form>
    <br/>
    <br/>

    <h4>${faqBean.primaryCategory}</h4>
      <br/>
 <%--   <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${faqBean}"/>--%>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${faqBean}"/>
      
    <div id="faq-area">
      <c:forEach items="${faqBean.faqList}" var="faq" varStatus="faqctr">
        <fieldset class="faq">

          <p class="faq-question" style="margin-top:10px;"><strong>Q. </strong>
              ${faq.question}
          <shiro:hasRole name="<%=RoleConstants.SITE_CONTENT_MANAGER%>">
            <s:link beanclass="com.hk.web.action.faq.FaqAction"
                    event="editFaq"
                    class="popup_window popup"
                    style="float:right;">
              Edit Faq
              <s:param name="faq" value="${faq.id}"/>
            </s:link>
          </shiro:hasRole>
          </p>

         <%-- <p class="faq-answer">
            <strong>A. </strong>--%>
          <div class="faq-answer">${faq.answer}</div>
          <%--</p>--%>
        </fieldset>
      </c:forEach>

    </div>


  </s:layout-component>
</s:layout-render>

