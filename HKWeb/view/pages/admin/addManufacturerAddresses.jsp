<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" var="ma"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <style type="text/css">
      .text {
        float: right;
        margin-right: 400px;
        margin-top: 20px;
      }

      .label {
        margin-top: 20px;
        float: left;
        margin-left: 10px;
      }
    </style>
  </s:layout-component>

  <s:layout-component name="content">

    <div>
      <s:form beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" id="newAddressForm">
        <fieldset class="top_label">
          <legend>Create/Edit Merchant Details</legend>
          <div style="float: left; width:50%">
              <s:hidden name="manufacturer" value="${ma.manufacturer.id}"/>
              <s:hidden name="address" value="${ma.address.id}"/>

            <s:label name="name" class="label">Name<span class="aster">*</span></s:label>
              <s:text class="text" name="address.name"/>

            <div class="clear"></div>

            <s:label name="name" class="label">Address Line 1<span class="aster">*</span></s:label>
              <s:text class="text" name="address.line1"/>

            <div class="clear"></div>

            <s:label name="name" class="label"> Address Line 2<span class="aster">*</span></s:label>
              <s:text class="text" name="address.line2"/>

            <div class="clear"></div>

            <s:label name="name" class="label">City<span class="aster">*</span></s:label>
              <s:text class="text" name="address.city"/>

            <div class="clear"></div>

            <s:label name="name" class="label">State<span class="aster">*</span></s:label>
              <s:text class="text" name="address.state"/>

            <div class="clear"></div>

            <s:label name="name" class="label">PIN Code<span class="aster">*</span></s:label>
              <s:text class="text" name="address.pin"/>

            <div class="clear"></div>

            <s:label name="name" class="label">Phone / Mobile<span class="aster">*</span></s:label>
              <s:text class="text" name="address.phone"/>

            <div class="clear"></div>
            <div style="margin-top:15px;"></div>
              <s:submit name="addAddress" value="Save" class="button" style="left: 50px;"/>
        </fieldset>
      </s:form>

    </div>

  </s:layout-component>

</s:layout-render>
