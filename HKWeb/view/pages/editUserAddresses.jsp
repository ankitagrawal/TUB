<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.UserManageAddressAction" var="umaa"/>
<s:layout-render name="/layouts/default.jsp">
<s:layout-component name="heading">Your Account</s:layout-component>
<s:layout-component name="lhsContent">
  <jsp:include page="myaccount-nav.jsp"/>
</s:layout-component>

<s:layout-component name="rhsContent">
       <c:choose>
         <c:when test="${umaa.address ==null}">
           <h4 class="strikeline">New Address</h4>
         </c:when>
         <c:otherwise>
           <h4 class="strikeline">Edit Address</h4>
         </c:otherwise>
       </c:choose>
     <script type="text/javascript">
      $(document).ready(function(){
         form = $('#addressForm');
                        form.find("input[type='text'][name='address.name']").val(${umaa.address.name});
                        form.find("input[type='text'][name='address.line1']").val(${umaa.address.line1});
                        if (${umaa.address.line2!=null or fn:length(umaa.address.line2)}) {
                            form.find("input[type='text'][name='address.line2']").val(${umaa.address.line2});
                        }
                        form.find("input[type='text'][name='address.city']").val(${umaa.address.city});
                        form.find("[name='address.state']").val(${umaa.address.state});
                        form.find("input[type='text'][name='address.pincode']").val(${umaa.address.pincode.pincode});
                        form.find("input[type='text'][name='address.phone']").val(${umaa.address.phone});
                    });
  </script>
  <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction" id="addressForm">
      <s:layout-render name="/layouts/addressLayout.jsp" />
      <s:submit name="saveAddress" value="Save" class="button" />
  </s:form>
</s:layout-component>
</s:layout-render>