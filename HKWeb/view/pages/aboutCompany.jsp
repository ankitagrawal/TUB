<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultG.jsp" pageTitle="About Us">
  <s:layout-component name="heading">About Us</s:layout-component>
  <s:layout-component name="lhsContent">
      <jsp:include page="about-nav.jsp"/>
  </s:layout-component>
  <s:layout-component name="rhsContent">
    <div class="grid_18">

      <p style="text-align: justify">
        Founded in March 2011, Healthkart.com (Aquamarine HealthCare Pvt. Ltd.) was formed on the grounds of giving simple, effective solutions to health enthusiasts.
        Today, Healthkart offers India's largest range of genuine health products across categories and all major brands.
      </p>

      <p style="text-align: justify">
        HealthKart is run by a core team of highly qualified professionals (graduates from IIT, Stanford, Harvard), with experience in India
        and USA and bringing in strong skills in Healthcare, Technology, Operations and Customer Management. HealthKart has grown from a
        humble team of 4 in March 2011 to strength of 100+, heading towards the cherished goal of becoming India’s e-health mega store.
        Our team of trained nutritionists, counselors and product experts help customers in choosing the right product/service via
        live chat, email and phone. We are backed by some of the most prestigious investors in the business – Sequoia Capital,
        Omidiyar Network and Kae Capital.
      </p>

      <p style="text-align: justify">
        We source the best health & wellness products from across the globe & provide them to every Indian via online shopping.
        Our offering covers categories like Nutrition, Sports and Fitness, Diabetes, Home Devices, Eye, Personal Care,
        Beauty, Parenting & Health Services. We partner directly with brands and their authorized channels, to ensure strict quality control
        and deliver 100% genuine products.
      </p>

      <p style="text-align: justify">
        We believe it is high time that we combined great technology, smart design and awesome customer care to give India a massive health
        and wellness boost. While there are challenges that need to be overcome, we are driven by our vision to become a dependable, household
        name in India, and offering customers direct access to genuine health products at best prices.
      </p>

    </div>

  </s:layout-component>
</s:layout-render>


<script type="text/javascript">
  window.onload = function() {
    document.getElementById('aboutLink').style.fontWeight = "bold";
  };
</script>