<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/default100.jsp" pageTitle="Deals">
  <s:layout-component name="topCategory">deals</s:layout-component>
  <s:layout-component name="htmlHead">

    <script type="text/javascript">
      $(document).ready(function() {
        $('#deals_button').addClass("active");
      });
    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <div class="products">
      <h2>
        Special Offers
      </h2>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="NUT630"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="DCMB01"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="NUT456"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="NUT631"/>
      <a class='more' href=''>
      </a>
    </div>

    <div class="products">
      <h2>
        Beauty
      </h2>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="BTY115"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="BTY429"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="GATCOM1"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="BTY590"/>
      <a class='more' href=''>
      </a>
    </div>

    <div class="products">
      <h2>
        Nutrition
      </h2>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="NUT465"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="NUT385"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="NUT460"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="NUT458"/>
      <a class='more' href=''>
      </a>
    </div>

    <div class="products">
      <h2>
        Eye
      </h2>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="EYE005"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="EYE314"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="EYE035"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="EYE021"/>
      <a class='more' href=''>
      </a>
    </div>

    <div class="products">
      <h2>
        Home Devices
      </h2>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="HB004"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="HB006"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="HR005"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="HP002"/>
      <a class='more' href=''>
      </a>
    </div>

    <div class="products">
      <h2>
        Diabetes
      </h2>
       <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="DC007"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="DM007"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="DM009"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="DS001"/>
      <a class='more' href=''>
      </a>
    </div>

    <div class="products">
      <h2>
        Baby Care
      </h2>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="BAB375"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="BAB016"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="BAB148"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="BAB359"/>
      <a class='more' href=''>
      </a>
    </div>

    <div class="products">
      <h2>
        Personal Care
      </h2>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="PS034"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="PA001"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="PF043"/>
      <s:layout-render name="/layouts/embed/_dealsProductThumbG.jsp" productId="PW015"/>
      <a class='more' href=''>
      </a>
    </div>

  </s:layout-component>

  <%--<s:layout-component name="zopim">
    <jsp:include page="/includes/_zopim.jsp"/>
  </s:layout-component>--%>

</s:layout-render>
