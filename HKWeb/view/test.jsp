<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<script type="text/javascript">
  var pricinDto = {"code":"ok","message":"","data": {
    "lineItem":{
      "id":18,
      "lineItemStatus":{"handler":{"interfaces":[{}],"constructed":true,"persistentClass":{},"overridesEquals":false,"initialized":false,"entityName":"mhc.domain.LineItemStatus","id":5,"unwrap":false}},
      "lineItemType":{"handler":{"interfaces":[{}],"constructed":true,"persistentClass":{},"overridesEquals":false,"target":{"id":10,"name":"Product"},"initialized":true,"entityName":"mhc.domain.LineItemType","id":10,"unwrap":false}},
      "productVariant":{"handler":{"interfaces":[{}],"constructed":true,"persistentClass":{},"overridesEquals":true,"target":{"id":"BAB080-01","hkPrice":120.0,"markedPrice":120.0,"discountPercent":0.0,"shippingBaseQty":1,"shippingBasePrice":30.0,"shippingAddQty":1,"shippingAddPrice":30.0,"tax":{"handler":{"interfaces":[{}],"constructed":true,"persistentClass":{},"overridesEquals":false,"initialized":false,"entityName":"mhc.domain.Tax","id":1,"unwrap":false}},"outOfStock":false},"initialized":true,"entityName":"mhc.domain.ProductVariant","id":"BAB080-01","unwrap":false}},
      "tax":{"handler":{"interfaces":[{}],"constructed":true,"persistentClass":{},"overridesEquals":false,"initialized":false,"entityName":"mhc.domain.Tax","id":1,"unwrap":false}},
      "qty":2,
      "markedPrice":120.0,
      "hkPrice":120.0,
      "discountOnHkPrice":0.0
    },"pricingDto":{
      "productsSubTotal":1146.1,"productsDiscount":138.9,"productsTotal":1007.2,
      "productLineCount":3,"orderLevelDiscount":0.0,"orderLevelDiscountLines":0,"redeemedRewardPoints":0.0,
      "rewardPointTotal":0.0,"shippingSubTotal":0.0,"shippingDiscount":0.0,"shippingTotal":0.0,"shippingLineCount":0,
      "codSubTotal":0.0,"codDiscount":0.0,"codTax":0.0,"codTotal":0.0,"codLineCount":0,"grandTotal":1007.2,
      "totalDiscount":138.9,"totalLineCount":3
    }
  }};
</script>