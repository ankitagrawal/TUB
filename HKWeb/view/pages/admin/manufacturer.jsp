<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.ManufacturerAction" var="ma"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Manufacturer List">
  <s:layout-component name="htmlHead">
    <style type="text/css">
      table {
        margin-bottom: 10px;
        margin-top: 5px;
        border: 1px solid;
        border-collapse: separate;
      }

      th {
        background: #f0f0f0;
        padding: 5px;
        text-align: left;
      }

      td {
        padding: 5px;
        text-align: left;
        font-size: small;
      }
    </style>
  </s:layout-component>
  <s:layout-component name="content">
    <div style="text-align: center;"><h2>Manufacturer List</h2></div>

    <p><s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAction"
               event="createOrEditManufacturer" target="_blank">Add New Manufacturer</s:link></p>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ma}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ma}"/>
    <table class="cont footer_color" width="100%">
      <tr>
        <th>Name</th>
        <th>Website</th>
        <th>Description</th>
        <th>Email</th>
        <th>Actions</th>
      </tr>

      <c:forEach items="${ma.manufacturerList}" var="manufacturerVar">
        <tr>

          <td>${manufacturerVar.name}</td>
          <td>${manufacturerVar.website}</td>
          <td>${manufacturerVar.description}<br/>
          <td>${manufacturerVar.email}</td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAction" event="createOrEditManufacturer"
                    target="_blank">Edit
              <s:param name="manufacturer" value="${manufacturerVar.id}"/></s:link>
            &nbsp;
            <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction"
                    event="getManufacturerAddresses" target="_blank">get addresses
              <s:param name="manufacturer" value="${manufacturerVar.id}"/></s:link>
            <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" event="addOrEditNewAddresses"
                    target="_blank">add new address
              <s:param name="manufacturer" value="${manufacturerVar.id}"/></s:link>
          </td>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ma}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ma}"/>

  </s:layout-component>
</s:layout-render>