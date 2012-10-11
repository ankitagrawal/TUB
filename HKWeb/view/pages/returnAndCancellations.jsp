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
        list-style: circle;
      }
      .niceList ul li {
        list-style-type: decimal;
      }
    </style>
    <div class="grid_18">
      <h4>14 Day Return Policy</h4>

      <p>
        We offer you complete peace of mind while ordering at HealthKart - you can return all items within 14 days of receipt of goods.
      </p>

      <h4>Defective Items:</h4>

      <p>
        If an item is found defective, please get in touch with our customer care on info@healthkart.com or call us on
        0124-4551616 within 14 days of receipt of the product. When we receive the item, we will inspect it to ensure
        everything is present and is in its original condition and then issue either a full refund, Healthkart Cash
        (store credit) or a different item in exchange, as per your request.
      </p>

      <p><em>Please note that replacements are subject to availability of the particular product.</em></p>

      <h4>
        Returns:
      </h4>

      <p>
        If you are not satisfied with the product or your purchase, you may return the items back to HealthKart within
        14 days of receipt of your products. You will be refunded the purchase amount and the return
        shipping charges in the form of Healthkart Cashback
      </p>

      <ul class="niceList">
        <li>
          All items returned must be unused and in their original condition with all original tags or boxes and packaging intact
        </li>
        <li>
          To coordinate a return, send a picture of the item(s) to info@healthkart.com with the email title “Return” or call 0124-4551616
          within 14 days of the receipt of the product to ensure return eligibility of the product. Please bear in mind that we should
          receive the return package within 10 days of us sending the authorizing email
        </li>
        <li>
          Once eligibility is verified, please pack the item(s) securely and ship to following address:
          <p>
            Aquamarine Healthcare Pvt Ltd<br/>
            T-01 MG Road, Parsvnath Arcadia, 2nd Floor<br/>
            Sector 14, Opp Motorola Bldg,<br/>
            Gurgaon, Haryana – 122001
          </p>
        </li>
        <li>Once your return package is received by us, you will be reimbursed a cash back equivalent to Rs 100 
          (for shipping) (in the form of Healthkart Cashback-store credit) + Purchase Amount in your HealthKart account,
          which could be redeemed towards future purchases. This is subject to your return having met the requirements of
          being unused and returned within 14 days of receipt of the particular order by the customer</li>
        <li>
          If however the return is found not eligible for refund, we will courier the same back to you. In either
          scenario your return issue will be closed within 7 days of our receiving of the return package from you 
          <p><em>Please Note (In case of COD or Bank Deposit): Healthkart will make refund cheques only in the registered name of the customer</em></p>
        </li>
      </ul>

      <h5>Following is the specific return policy for various product categories :</h5>

      <ul class="niceList">
        <li><strong>Nutrition</strong>: Products should be received in original packaging and sealed condition. Opened or used boxes will not be 
          accepted as returns. For issues like rashes, stomach upset, headache, flavor like/dislike, flavor difference from one brand
          to other etc. products would not be applicable for return. Please consult with the doctor before buying the product</li>
        <li>
          <strong>Sports & Fitness</strong>: Return policy applicable only in case of issues with size of Apparel and Shoes etc.
        </li>
        <li><strong>Diabetes</strong>: Products in this category are NOT eligible for returns</li>
        <li><strong>Beauty</strong>: Returns will be considered for change of color requests</li>
        <li><strong>Home Devices</strong>: Products in this category are NOT eligible for returns</li>
        <li><strong>Eye</strong>: If contact lenses are returned, they have to be returned in sealed boxes;
          opened or used lenses will not be accepted as returns. Lens solutions, lens cases are NOT eligible for returns</li>
        <li>
          <strong>Personal Care</strong>: Adult Diapers, Women hygiene products (sanitary napkins, tampons, panty liners),
           Condoms, Ovulation Kits, Toothbrushes, Toothpastes etc. are NOT eligible for returns
        </li>
        <li><strong>Parenting</strong>: Ovulation Kits and Diapers are NOT eligible for returns</li>
        <li><strong>Services</strong>: Products in this category are NOT eligible for returns</li>
      </ul>

      <h5>Following shall not be eligible for return or replacement :</h5>

      <ul class="niceList">
        <li>Damages due to misuse of product</li>
        <li>Incidental damage due to malfunctioning of product</li>
        <li>Any consumable item which has been used or installed</li>
        <li>Products with tampered or missing serial / UPC numbers</li>
        <li>Any damage / defect which are not covered under the manufacturer's warranty</li>
        <li>Any product that is returned without all original packaging and accessories, including the box,
          manufacturer's packaging if any, and all other items originally included with the product(s) delivered</li>
        <li>Some special rules for promotional offers may override "Healthkart.com's 14 Day Returns Policy"</li>
      </ul>

      <p>In case of any queries, please write to our customer care on info@healthkart.com or call us on 0124-4551616</p>

    </div>

  </s:layout-component>
</s:layout-render>


<script type="text/javascript">
  window.onload = function() {
    document.getElementById('rncLink').style.fontWeight = "bold";
  };
</script>