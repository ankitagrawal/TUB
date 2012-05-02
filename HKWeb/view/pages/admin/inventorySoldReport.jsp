<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Generator">

  <s:layout-component name="heading">Sale Per Product</s:layout-component>
  <s:layout-component name="content">

    <strong>TOTAL PRODUCTS SOLD : ${reportBean.total} </strong>

    <table class="align_top" width="100%">

      <thead>
      <tr>
        <th>Product Id</th>
        <th>Product Name</th>
        <th>Count</th>

      </tr>
      </thead>

      <c:forEach items="${reportBean.inventorySoldList}" var="inventorySoldList">

        <tr>
          <td>
              ${inventorySoldList.productId}
          </td>
          <td>
              ${inventorySoldList.productName}
          </td>
          <td>
              ${inventorySoldList.countSold}
          </td>
        </tr>

      </c:forEach>
    </table>
  </s:layout-component>
</s:layout-render>