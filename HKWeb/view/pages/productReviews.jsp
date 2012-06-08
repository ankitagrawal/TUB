<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction" var="pa" event="pre"/>
<s:layout-render name="/layouts/genericG.jsp" pageTitle="Product Reviews of ${pa.product.name}">

  <s:layout-component name="htmlHead">

    <style type="text/css">
      table.contentTable td {
        padding: 10px;
      }

      table.linkTable td {
        padding: 10px;
      }

      .rating_bar {
        width: 73px;
        background: url('${pageContext.request.contextPath}/images/faintStar.png') 0 0 repeat-x;
      }

      .rating_bar div {
        height: 16px;
        background: url('${pageContext.request.contextPath}/images/blueStar.jpg') 0 0 repeat-x;
      }
    </style>


    <meta name="keywords" content="${pa.seoData.metaKeyword}"/>
    <meta name="description" content="${pa.seoData.metaDescription}"/>

    <script type="text/javascript">

    </script>

  </s:layout-component>
  <s:layout-component name="content">
    <table class="linkTable" width="900">
      <tr>
        <td style="width:700px;font-size:16px;font-style:normal;font-weight:bold;">Reviews of &nbsp;<s:link
            beanclass="com.hk.web.action.core.catalog.product.ProductAction"
            style="font-size:16px;font-style:normal;font-weight:BOLD;">
          ${pa.product.name}
          <s:param name="productSlug" value="${pa.product.slug}"/>
          <s:param name="productId" value="${pa.product.id}"/>
        </s:link>
        </td>
        <td>
          <s:link beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction" event="writeNewReview"
                  class="writeNewReview button_green">
            <s:param name="product" value="${pa.product.id}"/>
            Write a Review
          </s:link>
        </td>
      </tr>
    </table>
    <hr style="color:#F0F0F0;height:0em;"/>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pa}"/>
    <table width="950" class="contentTable">
      <tr>
        <td><strong>Name</strong></td>
        <td><strong>Reviews</strong></td>
      </tr>
      <c:forEach items="${pa.productReviews}" var="review" varStatus="ctr">
        <tr>
          <td style="word-wrap:break-word">
            <strong>${review.postedBy.name}</strong>
          </td>
          <td style="border-style:none;border-color:#F0F0F0;">
            <h4>${review.title}</h4>
          </td>
        </tr>
        <tr>
          <td>
            <div class="rating_bar">
              <div class="blueStarRating${ctr.index}"></div>
              <script type="text/javascript">
                var index = ${ctr.index};
                var rating =${review.starRating};
                rating = (rating * 20) + "%";
                $('.blueStarRating' + index).width(rating);
              </script>
            </div>
          </td>
          <td>
            <div style="width:780px;overflow:auto">
              <pre width="400" style="word-wrap:break-word">${review.review}</pre>
            </div>
          </td>
        </tr>
        <tr style="border-style:none">
          <td colspan="2" style="border-style:none">
            <hr style="color:#F0F0F0;border-style:dotted">
          </td>
        </tr>
      </c:forEach>
    </table>
    <br/>
    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${pa}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${pa}"/>
    <br/>
  </s:layout-component>

</s:layout-render>