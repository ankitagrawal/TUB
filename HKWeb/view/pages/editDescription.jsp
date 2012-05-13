<html>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" var="pa" event="editDescription"/>
  <head><title></title></head>
  <body>
  <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
  <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>
  <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>

   <s:form beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction">
   <s:hidden name="productId" value="${pa.product.id}"/>
  <textarea cols="80" id="description" name="description" rows="6">${pa.product.description}</textarea>
  <script type="text/javascript">
      //<![CDATA[
            CKEDITOR.replace('description',
            {
              fullPage : false,
              extraPlugins : 'docprops'
             });

       //]]>
   </script>
   <s:submit name="saveDescription" value="Save"/>
   </s:form>

  </body>
</html>