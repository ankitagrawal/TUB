<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="mhc.web.action.ProductReviewAction" var="pa" event="pre"/>
<s:layout-render name="/layouts/genericG.jsp" pageTitle="${pa.seoData.title}">

<s:layout-component name="htmlHead">

  <meta name="keywords" content="${pa.seoData.metaKeyword}"/>
  <meta name="description" content="${pa.seoData.metaDescription}"/>

  <script type="text/javascript">

  </script>

</s:layout-component>

<s:layout-component name="content">
  <h2>Let others know what you liked/disliked about ${pa.product.name}</h2>

  <s:form beanclass="mhc.web.action.ProductReviewAction">
    <label>Title*</label><s:text name="review.title"></s:text><br/>
    <label>Review*</label><s:textarea name="review.review"/><br/>
    <label>Rating*</label><s:text name="review.starRating" maxlength="1" value="3"/><br/>
    <label>Name*</label>${pa.review.postedBy.name}<br/>
    <s:hidden name="review.postedBy" value="${pa.review.postedBy.id}"/>
    <s:hidden name="review.product" value="${pa.product.id}"/>

    <s:submit name="postReview" value="Submit"/>
  </s:form>

 </s:layout-component>

</s:layout-render>