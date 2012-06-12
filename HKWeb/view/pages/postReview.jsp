<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction" var="pa" event="pre"/>
<c:set var="product" value="${pa.product}"/>
<s:layout-render name="/layouts/genericG.jsp" pageTitle="Write a Review for ${pa.product.name}">

  <s:layout-component name="htmlHead">

    <meta name="keywords" content="${pa.seoData.metaKeyword}"/>
    <meta name="description" content="${pa.seoData.metaDescription}"/>

    <style type="text/css">
      
      table.reviewTable td {
        padding: 10px;
        /*background-color:#EEEEEE;*/
      }

    </style>

  </s:layout-component>

  <s:layout-component name="content">
    <br/>

    <h1>
      Write a review for
      <s:link
          beanclass="com.hk.web.action.core.catalog.product.ProductAction"
          style="font-size:16px;font-style:normal;font-weight:BOLD;">
        <strong style="font-size:20px;font-weight:bold;">${product.name}</strong>
        <s:param name="productSlug" value="${pa.product.slug}"/>
        <s:param name="productId" value="${pa.product.id}"/>
      </s:link>
    </h1>
    <br/><br/>

    <div class="grid_8">
      <div class="productDescDiv">
        <c:choose>
          <c:when test="${product.mainImageId != null}">
            <div class="img128">
              <hk:productImage imageId="${product.mainImageId}" size="<%=EnumImageSize.SmallSize%>"
                               alt="${product.name}"/>
            </div>
          </c:when>
          <c:otherwise>
            <div class="img320">
              <img src='<hk:vhostImage/>/images/ProductImages/ProductImagesOriginal/${product.id}.jpg'
                   alt="${product.name}"/>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
      <div>

        <p><strong>Why A Review ??</strong></p>

        <p><strong>Hands-on Experience</strong></p>

        <p>
          As humans we share our experiences good or bad with our friends and fellas.
          And obviously if you’ve experienced a product purchased from us,
          you’d have some thoughts, so why not pen it down? You find a little time,
          we give you some space…
        </p>

        <p><strong>I am not a professional, how do I write?</strong></p>

        <p>
          Yes we knew that was coming. Well its simple. Be true to yourself.
          Just be sure you’ve used the product that you’re writing about.
          You understand the basics of its functionality and then,
          whether you’d like others to go for it or not.
        </p>

        <p><strong>Try and be factual</strong></p>

        <p>
          It’s not so difficult to be yourself. Your persona should reflect.
          Make sure the information is correct, try and search more about the product.
          Inaccurate information is hazardous at times.
        </p>

        <p><strong>Try and be concise</strong></p>

        <p>
          We appreciate your time and creativity, but it’s always good not to divert from the actual topic/product.
        </p>

        <p><strong>Easy to read, easy on the eyes</strong></p>

        <p>
          Ah! You’re almost there. Now that you’ve spent some time on writing about the product,
          a quick edit and spell check will work wonders.
          Like food, a write-up is best digested if broken into small paragraphs.
        </p>
        <br>
      </div>
    </div>
    <div class="grid_16">
      <s:form beanclass="com.hk.web.action.core.catalog.product.ProductReviewAction">
        <table class="reviewTable" style="border-left:1px solid #EEEEEE;">
          <tr>
            <td><label><strong>Title</strong></label><label style="color:red;">*</label></td>
            <td><s:text name="review.title" class="title" maxlength="45" style="width:480px"/></td>
          </tr>

          <tr>
            <td><label><strong>Review</strong></label><label style="color:red;">*</label></td>
            <td><s:textarea name="review.review" class="review" cols="45"
                            style="resize:none;word-wrap:break-word"/></td>
          </tr>

          <tr>
            <td><label><strong>Rating</strong></label><label style="color:red;">*</label></td>
            <td colspan="3">
              <div id="star"></div>
            </td>
          </tr>
          
          <tr>
            <td><label><strong>Name:</strong></label></td>
            <td><strong>${pa.review.postedBy.name}</strong>
            </td>
          </tr>
          <tr>
            <td></td>
            <td><s:hidden name="review.postedBy" value="${pa.review.postedBy.id}"/>
              <s:hidden name="review.starRating" id="starRating" value="3"/>
              <s:hidden name="review.product" value="${product.id}"/>
              <label style="color:red;">* All fields are mandatory.</label><br/>
              <s:submit name="postReview" value="Submit"
                        class="requiredFieldValidator"/></td>
          </tr>


        </table>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.raty.min.js"></script>

<script type="text/javascript">
  $(document).ready(function() {
    var rating;
    $('#star').raty({
      contextPath: '${pageContext.request.contextPath}',
      click: function(score, evt) {
        rating = score;
        $("#starRating").val(rating);
      }
    });
  });

  $('.requiredFieldValidator').click(function() {
    //alert("in validator");
    var title = $('.title').val();
    var review = $('.review').val();
    if (title == "" || review == "") {
      alert("Please enter all fields .");
      return false;
    } else {
      alert("Thanks.. Your review is submitted successfully.It would appear shortly on Healthkart.com . Please note that all reviews are moderated for quality and relevance");
    }
  });

</script>
