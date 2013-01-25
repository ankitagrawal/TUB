<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultG.jsp" pageTitle="Terms and Conditions">
  <s:layout-component name="heading">Returns & Cancellations</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="about-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <style type="text/css">
      em {
        font-style: italic;
      }
      ul.niceList {
        margin-left:2.5em;
        padding-left:0.2em;
        margin-bottom:1em;
      }
      ul.niceList li {
        padding-left: 1.8em;
          margin-bottom:10px;
        list-style: circle;
      }
      .niceList ul li {
        list-style-type: decimal;
      }
    </style>
    <div class="grid_18">
      <h4>14 day return policy</h4>

      <p>
        We understand that you sometimes are not satisfied with what you have bought at healthkart. That is why we offer a comprehensive 14 day return policy on our products. 
      </p>

        <h4>How it works</h4>

      <p>
          In case of a defective item <br/>
          If you unfortunately were delivered a defective product, please get in touch with our customer care agents at info@healthkart.com or by calling us at 0124-4502930, within 14 days of delivery. We will take back the item from you and inspect it to confirm the damage. Once this is done, we will follow up with you and will ask you to choose from the following options.

      </p>
        <ul class="niceList">
        <li>A  full refund</li>
            <li>Store credits on healthkart of the same value</li>
            <li>Another product in exchange, of the same value (note that this is subject to availability)</li>

      </ul>



      <h4>Defective Items:</h4>

      <p>
        If an item is found defective, please get in touch with our customer care at info@healthkart.com or call us on
        0124-4502930 within 14 days of receipt of the product. When we receive the item, we will inspect it to ensure
        everything is present and is in its original condition. We will then issue either a full refund, Healthkart Cash
        (store credit) or a different item in exchange, as per your request.
      </p>

      <p><em>Please note that replacements are subject to availability of the particular product.</em></p>

      <h4>
        Returns
      </h4>

      <p>
        If you are not satisfied with what you have bought, we’ll gladly take it back within 14 days from the date of delivery.
          If you have paid by card then we will reverse the payment. In case of Cash on Delivery or Bank Deposits as modes of payment,
          we will issue a cheque in the registered name of the customer. <br/>
          We also will refund you the shipping charges in store credits. <br/>
          Please ensure however that the product is unused and the tags, boxes and other packaging remains intact.
      </p>
       <h5>To coordinate a return  </h5>
      <ul class="niceList">
        <li>Send a picture of the item to info@healthkart.com with the subject line “RETURN” or call us at 0124-4502930 within 14 days of receiving the product. 
            Please also ensure that we should receive the product back within 10 days of sending us the request, otherwise the product will be ineligible for return.
        </li>
        <li>Mail us back the product at<br/>
             Aquamarine Healthcare Pvt. Ltd. <br/>
            T-01 MG Road, Parsvnath Arcadia, 2nd Floor  <br/>
            Sector 14, Opp. Motorola Building   <br/>
            Gurgaon, Haryana – 122001
        </li>
        
        <li>Once we receive the package, we will issue a refund of the purchase amount and Rs.100 for courier charges borne by you, as store credits.
            You can redeem your store credit towards future purchases from us.
            In case the return is found ineligible for refund, we will courier the product back to you.</li>
        <li>
          We will strive to close the issue within 7 days of receiving the product. 
        </li>
      </ul>

      <h5>Our category specific policy is as follows</h5>

      <ul class="niceList">
        <li><strong>Nutrition</strong>: Products should be sealed and in original packaging. The product will be ineligible for return otherwise.
            For issues that include rashes, stomach upsets, flavor like/dislike, 
            flavor difference from one brand to other etc. products would not be applicable for return. </li>
        <li>
          <strong>Sports & Fitness</strong>: Return policy is applicable only in case of issues with size of the apparel or footwear.
        </li>
        <li><strong>Diabetes</strong>: Products in this category are NOT eligible for return.</li>
        <li><strong>Beauty</strong>: Returns will be considered for change of color requests.</li>
        <li><strong>Health Devices</strong>: Are not eligible for return</li>
        <li><strong>Eye</strong>: Items such as contact lenses can only be returned in sealed, unopened boxes. Lens solutions and cases are not eligible for return.</li>
        <li>
          <strong>Personal Care</strong>: Adult Diapers, Women hygiene products (sanitary napkins, tampons, panty liners), Condoms, Ovulation Kits, Toothbrushes, Toothpastes etc. are not eligible for return.
        </li>
        <li><strong>Parenting</strong>: Ovulation Kits and Diapers are not eligible for return</li>
        <li><strong>Services</strong>: Are not eligible for return</li>
      </ul>

      <h5>The following products are eligible for return or replacement</h5>

      <ul class="niceList">
        <li>Products with damages due to misuse</li>
        <li>Products with incidental damages due to malfunction</li>
        <li>Tampered products or ones with missing serial numbers</li>
        <li>Products with damages not covered under manufacturers warranty</li>
        <li>Any product returned without all original packaging or accessories, including the box, manufacturer’s packaging if any, and all other items originally included in the packaging.</li>
        <li>Some special rules for promotional offers may override "Healthkart.com's 14 Day Returns Policy"</li>
        </ul>

      <p>In case of queries, please write to us at info@healthkart.com or call us at 0124-4502930</p>

    </div>

  </s:layout-component>
</s:layout-render>


<script type="text/javascript">
  window.onload = function() {
    document.getElementById('rncLink').style.fontWeight = "bold";
  };
</script>