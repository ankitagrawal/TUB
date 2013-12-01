<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" var="ma"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">

    <div>
      <s:form beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" id="newAddressForm">
        <fieldset>
          <legend>Search Address by City</legend>
          <s:label name="cityLabel" style="padding: 10px;">City</s:label>
          <s:text name="city"/>
          <s:hidden name="manufacturer"/>
          <s:submit name="getManufacturerAddresses" value="Search" style="font-size: 0.9em"/>
        </fieldset>
      </s:form>

    </div>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ma}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ma}"/>

    <div class='left2' style="margin-left:20px; width: 100%;">
      <c:if test="${!empty ma.addresses}">
        <s:form beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction">
          <c:forEach var="address" items="${ma.addresses}" varStatus="addressCount" >
            <div class="address"
                 style="float: left; margin-left: 10px; margin-right: 10px; width: 25%; height: 200px; padding: 15px">
              <h5 class="name">${address.name}</h5>

              <div style="float:right;">
                <%--<s:text name="latitude[${addressCount.index}]" value="${ma.latitude}"/>--%>
                <%--<s:text name="longitude[${addressCount.index}]" value="${ma.longitude}"/>--%>
                <c:set var="addressCountIndex" value="${addressCount.index}"/>
                <a href="http://maps.google.com/maps?q=${ma.latitude[addressCountIndex]},${ma.longitude[addressCountIndex]}" target="_blank">
                  View Location On Map
                </a>
              </div>

              <div class='street street1'>${address.line1}</div>
              <c:if test="${hk:isNotBlank(address.line2)}">
                <div class="street street1">${address.line2}</div>
              </c:if>
              <div class='city'>${address.city}</div>
              <div class='state'>${address.state}</div>
              <div class='pin'>${address.pincode.pincode}</div>
              <div class='phone'>${address.phone}</div>

              <div style="margin-top:10px;"></div>

              <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" event="remove" class="delete"
                      onclick="return confirm('Are you sure you want to delete this address?')">
                <s:param name="address" value="${address.id}"/>
                <s:param name="manufacturer" value="${ma.manufacturer.id}"/>
                (delete)
              </s:link>
              <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" event="setAsDefaultAddress"
                      class="save" onclick="return confirm('This will be shown in product page!!')">
                <s:param name="address" value="${address.id}"/>
                <s:param name="manufacturer" value="${ma.manufacturer.id}"/>
                (Set as Default)
              </s:link>
              <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" event="addOrEditNewAddresses"
                      class="save" target="_blank">
                <s:param name="address" value="${address.id}"/>
                <s:param name="manufacturer" value="${ma.manufacturer.id}"/>
                (Edit)
              </s:link>
            </div>
          </c:forEach>

        </s:form>
      </c:if>
    </div>
  </s:layout-component>

</s:layout-render>
