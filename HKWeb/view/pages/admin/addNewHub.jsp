<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Hub">
    <s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction" var="hubAction"/>
    <s:layout-component name="heading">
        Add Hub
    </s:layout-component>
    <s:layout-component name="content">
        <div class="addHub">
            <table>
                <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction">
                    <s:hidden name="hub.id" value="${hubAction.hub.id}"/>
                    <tr>
                        <td>Name:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="hub.name" value="${hubAction.hub.name}" class="name"/></td>
                    </tr>
                    <tr>
                        <td>Address:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="hub.address" value="${hubAction.hub.address}" class="address"/></td>
                    </tr>
                    <tr>
                        <td>Pincode:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="pincode" value="${hubAction.pincode}" class="pincode"/></td>

                    </tr>
                    <tr>
                        <td>Country:<span class='aster' title="this field is required">*</span></td>
                        <td><s:text name="hub.country" value="${hubAction.hub.country}" class="country"/></td>

                    </tr>
                    <tr>
                        <td><s:submit name="addNewHub" value="Add New Hub" class="dataValidator">
                            <s:param name="addHub" value="true" ></s:param>
                            Add New Hub
                            </s:submit>
                        </td>
                    </tr>
                </s:form>
            </table>
        </div>
    </s:layout-component>
</s:layout-render>
<script type="text/javascript">
    $('.dataValidator').click(function() {
    //alert("in validator");
    var name = $('.name').val();
    var address = $('.address').val();
    var pincode = $('.pincode').val();
    var country = $('.country').val();
       //alert(name + address+pincode+country);
    if (name == " " || address == " " || pincode == "" || country == "") {
      alert("Please enter all fields .");
      return false;
    }
  });
  </script>