<%@ page import="com.hk.constants.FaqCategoryEnums" %>
<html>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="primaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqPrimaryCateogry.getAll() %>"/>
<c:set var="secondaryCategoryList" value="<%= FaqCategoryEnums.EnumFaqSecondaryCateogry.getAll() %>"/>
<s:useActionBean beanclass="com.hk.web.action.faq.FaqAction" var="faqBean"/>

 <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
<script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>
<script>
    function validate(){
            var question = document.getElementById('edit-question').value;
            var answer = document.getElementsByName('edit-answer').value;
            if(question == ""){
                alert('question or answer cannot be left blank');
            }
            return false;
        }
</script>
  <head><title></title></head>
  <body>

  <s:form beanclass="com.hk.web.action.faq.FaqAction" class="edit-faq-form">
          <s:hidden name="faq.id" value="${faqBean.faq.id}"/>
          <span class="faq-question"> Question: </span><br/>
          <textarea class="question-textarea"
                    name="faq.question" rows="3" cols="80" id="edit-question"> ${faqBean.faq.question}</textarea>
          <br/><br/>
          <span class="faq-answer"> Answer: </span>
          <br/>
          <textarea cols="80" name="faq.answer"
                    rows="6" class="ckeditor" id="edit-answer">${faqBean.faq.answer}</textarea>
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
          <s:select name="faq.primaryCategory" value="${faqBean.faq.primaryCategory}">
              <s:option value="">-Select-</s:option>
              <c:forEach items="${primaryCategoryList}" var="primaryCategory">
                  <s:option value="${primaryCategory}">${primaryCategory}</s:option>
              </c:forEach>
          </s:select>
          <span class="question" style="margin-left: 30px;"> Secondary Category</span>
          <s:select name="faq.secondaryCategory" value="${faqBean.faq.secondaryCategory}">
              <s:option value="">-Select-</s:option>
              <c:forEach items="${secondaryCategoryList}" var="secondaryCategory">
                  <s:option value="${secondaryCategory}">${secondaryCategory}</s:option>
              </c:forEach>
          </s:select>
          <br/>
          <span class="question"> Keyword String</span>
          <s:text name="faq.keywords" value="${faqBean.faq.keywords}"
                  style="width: 25em; margin-top:5px;"/>
          <span class="question"> Rank</span>
          <s:text name="faq.pageRank" style="width: 25em; margin-top:5px;"/>
          <br/>
          <s:submit name="saveFaq" value="Save" onclick="validate()"/>
          <s:submit name="deleteFaq" value="Delete Faq"/>
      </s:form>

  </body>
</html>