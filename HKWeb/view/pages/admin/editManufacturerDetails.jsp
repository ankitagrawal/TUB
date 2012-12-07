<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.ManufacturerAction" var="ma"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <style type="text/css">
      .text {
        float: right;
        margin-right: 450px;
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
      <s:form beanclass="com.hk.web.action.admin.catalog.ManufacturerAction">
        <fieldset class="top_label">
          <legend>Create/Edit Merchant Details</legend>
          <div style="float: left; width:50%">

            <s:label name="name" class="label">Name<span class="aster">*</span></s:label>
            <s:text name="manufacturer.name" class="text"/>

            <div class="clear"></div>

            <s:label name="website" class="label"> Website<span class="aster">*</span> </s:label>
            <s:text name="manufacturer.website" class="text"/>

            <div class="clear"></div>

            <s:label name="desc" class="label"> Description<span class="aster">*</span> </s:label>
            <s:text name="manufacturer.description" class="text"/>

            <div class="clear"></div>

            <s:label name="email" class="label autoadjust"> Email <span class="aster">*</span> </s:label> 
            <s:text name="manufacturer.email" class="text"/>
              <div class="clear"></div>
              <span class="aster"> Enter Multiple Email Id separated by comma(,) </span>

            <div class="clear"></div>

            <s:label name="IsAvilableAllOverIndia" class="label"> Is Available All over India<span class="aster">*</span> </s:label>
            <s:checkbox name="manufacturer.availableAllOverIndia" class="text"/>
          </div>
          <div class="clear"></div>
          <div style="margin-top:15px;"></div>
          <s:hidden name="manufacturer" value="${ma.manufacturer.id}"/>

          &nbsp;&nbsp;<s:submit name="saveManufacturerDetails" value="Create Or Edit" style="font-size: 0.9em"/>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>
        </fieldset>
      </s:form>
    </div>
  </s:layout-component>

</s:layout-render>
