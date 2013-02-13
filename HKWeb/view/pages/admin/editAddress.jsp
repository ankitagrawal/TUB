<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
<c:set var="countryId" value="80"/>
  <s:layout-component name="content">

    <s:useActionBean beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction" var="addressBean" event="edit"/>
     <script type="text/javascript">
      $(document).ready(function(){
         form = $('#addressForm');
                        form.find("input[type='text'][name='address.name']").val(${addressBean.address.name});
                        form.find("input[type='text'][name='address.line1']").val(${addressBean.address.line1});
                        if (${addressBean.address.line2!=null or fn:length(addressBean.address.line2)>0}) {
                            form.find("input[type='text'][name='address.line2']").val(${addressBean.address.line2});
                        }
                        form.find("input[type='text'][name='address.city']").val(${addressBean.address.city});
                        form.find("[name='address.state']").val(${addressBean.address.state});
                        form.find("input[type='text'][name='address.pincode']").val(${addressBean.address.pincode.pincode});
                        form.find("input[type='text'][name='address.phone']").val(${addressBean.address.phone});
                    });
  </script>
    <s:form beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction" id="addressForm">
        <fieldset>
            <legend>Edit order address</legend>
            <ul>
                <li>
                    <s:layout-render name="/layouts/addressLayout.jsp" />
                    <s:hidden name="countryId" value="${countryId}"/>
                    <s:hidden name="order" value="${addressBean.order.id}"/>
                    Copy This Address to user's address book <s:checkbox name="copyToUserAddressBook"/><br/>
                    <s:submit name="save" value="save"/>
                    <s:link beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction">
                        cancel
                        <s:param name="order" value="${addressBean.order.id}"/>
                    </s:link>
                </li>
            </ul>
        </fieldset>

    </s:form>

  </s:layout-component>

</s:layout-render>
