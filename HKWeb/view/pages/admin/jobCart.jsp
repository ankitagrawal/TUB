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

<s:useActionBean beanclass="com.hk.web.action.admin.JobCartAction" var="orderSummary"/>
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
      <th width="75px">Bin</th>
      <th width="525px">Item</th>
      <th width="50px">Quantity</th>
    </tr>
    <c:forEach items="${orderSummary.binHasPVs}" var="binHasPV">
      <tr>
        <td width="75px">
            ${binHasPV.key.barcode}
          <br/>
            ${binHasPV.key.aisle}<br/>
            ${binHasPV.key.rack}<br/>
            ${binHasPV.key.shelf}<br/>
        </td>
        <td colspan="2">
          <table width="550px">
            <c:forEach items="${binHasPV.value}" var="pvQty">
              <tr>
                <td width="525px">
                    ${pvQty.key.product.name}
                  |
                    ${pvQty.key.variantName}
                  |
                    ${pvQty.key.upc}
                  |
                  <em><c:forEach items="${pvQty.key.productOptions}" var="productOption">
                    ${productOption.name} ${productOption.value}
                  </c:forEach></em>
                </td>
                <td width="50px">
                    ${pvQty.value}
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