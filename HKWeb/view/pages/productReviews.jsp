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
  <h2>Review of <s:link beanclass="mhc.web.action.ProductAction">
   ${pa.product.name}
    <s:param name="productSlug" value="${pa.product.slug}"/>
    <s:param name="productId" value="${pa.product.id}"/>
  </s:link> </h2>
   <s:link beanclass="mhc.web.action.ProductReviewAction" event="writeNewReview">
     <s:param name="product" value="${pa.product.id}"/>
     Write a Review
   </s:link>
  <hr/>
  <table width="100%">
    <tr>
      <th width="25%"><strong>Name</strong></th>
      <th width="25%"><strong>Date</strong></th>
      <th width="50%"><strong>Reviews</strong></th>
    </tr>
  <c:forEach items="${pa.productReviews}" var="review">
    <tr>
      <td>
         ${review.postedBy.name}
    </td>
      <td>
         ${review.reviewDate}
    </td>
    <td>
         ${review.review}
    </td>
    </tr>

  </c:forEach>
  </table>

 </s:layout-component>

</s:layout-render>