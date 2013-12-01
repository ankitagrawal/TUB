<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.content.seo.SeoAction" var="sa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
    <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
    <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>
  </s:layout-component>

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.core.content.seo.SeoAction">
      <s:hidden name="seoData" value="${sa.seoData.id}"/>
      <fieldset class="top_label">
        <ul>
          <li><label>Page Title</label><s:text name="seoData.title" style="width: 300px;"/></li>
          <li><label>Heading (H1)</label><s:text name="seoData.h1" style="width: 300px;"/></li>
          <li><label>Meta Keywords</label><s:textarea name="seoData.metaKeyword"/></li>
          <li><label>Meta Description</label><s:textarea name="seoData.metaDescription"/></li>
          <li><label>Description Title</label><s:textarea name="seoData.descriptionTitle"/></li>
          <li><label>Description</label>
            <s:textarea name="seoData.description" id="description"/>
            <script type="text/javascript">
                //<![CDATA[
                      CKEDITOR.replace('description',
                      {
                        fullPage : false,
                        extraPlugins : 'docprops'
                       });

                 //]]>
             </script>
          </li>
        </ul>
      </fieldset>
      <s:submit name="saveSeoData" value="save"/>
    </s:form>
  </s:layout-component>
</s:layout-render>