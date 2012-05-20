    <%@ page import="java.text.DateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Order Invoice</title>
  <style type="text/css">
    table {
      border-collapse: collapse;
      width: 100%
    }

    table tr td {
      padding: 5px;
      border: 1px solid #CCC;
    }

    table tr th {
      padding: 5px;
      border: 1px solid #CCC;
      text-align: left;
    }

    h2t {
      margin: 0;
      padding: 0;
    }

    h1 {
      margin: 0;
      padding: 0;
    }

    table.header tr td {
      border: none;
      vertical-align: top
    }

    .clear {
      clear: both;
      display: block;
      overflow: hidden;
      visibility: hidden;
      width: 0;
      height: 0
    }
  </style>
</head>
<body>
 <s:errors/>
<s:useActionBean beanclass="com.hk.web.action.admin.queue.JobCartAction" var="orderSummary"/>
<table class="header">
  <tr>
    <td>
      <p>Job Cart for Orders
        <c:forEach items="${orderSummary.pickingQueueOrders}" var="order">
          #${order.gatewayOrderId};
        </c:forEach>
      </p>
    </td>
  </tr>

</table>

<div class="clear"></div>
<div>
  <h3>Order Details</h3>

  <table style="font-size: .8em; width:650px; padding:0">
    <tr>
      <th width="30%">Bin</th>
      <th width="40%">Item </th>
      <th width="20%">Quantity</th>
    </tr>
    <c:forEach items="${orderSummary.binHasPVs}" var="mapentry">
      <tr width="100%">
          <c:set var="key" value="${mapentry.key}"/>
          <c:set var="map" value="${orderSummary.idBinMap}"/>
          <c:set var="prodlist" value="${mapentry.value}"/>
        <c:set var="length" value="${fn:length(prodlist)}"/>
        <td width="30%" rowspan="${length}">
           ${map[key].barcode}
          <br/>
           ${map[key].aisle}<br/>
            ${map[key].rack}<br/>
            ${map[key].shelf}<br/>
        </td>

        <td width="70%" >
          <table width="100%">
            <c:forEach items="${mapentry.value}" var="productVariant">
              <tr width="100%">
                <td width="70%">
                    ${productVariant.product.name}
                  |
                    ${productVariant.variantName}
                  |
                    ${productVariant.upc}
                  |
                  <em><c:forEach items="${productVariant.productOptions}" var="productOption">
                    ${productOption.name} ${productOption.value}
                  </c:forEach></em>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                         <c:set var="hkCodeMap" value="${orderSummary.skuGroupMapProductVariant}"/>

                    ${hkCodeMap[productVariant.id].barcode}
                </td>


             <td width="30%">
                 <c:set var="Qtymap" value="${orderSummary.productVariantQty}"/>
                    ${Qtymap[productVariant.id]}
                </td>
                   </tr>
           </c:forEach>
           </table>
        </td>
      </tr>
    </c:forEach>
  </table>

</div>
</body>
</html>