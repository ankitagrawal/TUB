<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" var="ma"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
<c:set var="countryId" value="80"/>
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
       <script type="text/javascript">
      $(document).ready(function(){
         form = $('#addressForm');
                        form.find("input[type='text'][name='address.name']").val(${ma.address.name});
                        form.find("input[type='text'][name='address.line1']").val(${ma.address.line1});
                        if (${ma.address.line2!=null or fn:length(ma.address.line2)>0}) {
                            form.find("input[type='text'][name='address.line2']").val(${ma.address.line2});
                        }
                        form.find("input[type='text'][name='address.city']").val(${ma.address.city});
                        form.find("[name='address.state']").val(${ma.address.state});
                        form.find("input[type='text'][name='address.pincode']").val(${ma.address.pincode.pincode});
                        form.find("input[type='text'][name='address.phone']").val(${ma.address.phone});
                    });
  </script>
    <div>
      <s:form beanclass="com.hk.web.action.admin.catalog.ManufacturerAddressAction" id="addressForm">
        <fieldset class="top_label">
          <legend>Create/Edit Merchant Details</legend>
          <div style="float: left; width:50%">
              <s:hidden name="manufacturer" value="${ma.manufacturer.id}"/>
              <s:layout-render name="/layouts/addressLayout.jsp" />
              <s:hidden name="countryId" value="${countryId}"/>
              <s:submit name="addAddress" value="Save" class="button" style="left: 50px;"/>
        </fieldset>
      </s:form>

    </div>

  </s:layout-component>

</s:layout-render>
