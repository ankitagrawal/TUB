<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.UserManageAddressAction" var="umaa"/>
<s:layout-render name="/layouts/defaultBeta.jsp">
<s:layout-component name="heading">Addresses</s:layout-component>

    <s:layout-component name="centralContent">
        <%--breadcrumbs begins--%>
        <div class="hk-breadcrumb-cntnr mrgn-bt-10">
                <span>
                   <s:link beanclass="com.hk.web.action.HomeAction">Home</s:link>
                </span>
            <span>&raquo;</span>
            <span class="txt-blue fnt-bold">
                <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction">Addresses</s:link>
            </span>
            <span>&raquo;</span>
            <span class="txt-blue fnt-bold">Edit Address</span>
        </div>
        <%--breadcrumbs ends--%>
    </s:layout-component>

<s:layout-component name="lhsContent">
  <jsp:include page="myaccount-navBeta.jsp"/>
</s:layout-component>
<c:set var="countryId" value="80"/>
<s:layout-component name="rhsContent">
    <div class="mrgn-l-40 my-acnt-ht">
       <c:choose>
         <c:when test="${umaa.address ==null}">
           <h2 class="strikeline" style="margin-bottom: 10px;">New Address</h2>
         </c:when>
         <c:otherwise>
           <h2 class="strikeline">Edit Address</h2>
         </c:otherwise>
       </c:choose>
  <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction" id="addressForm">
      <s:layout-render name="/layouts/addressLayoutBeta.jsp" />
      <s:hidden name="countryId" value="${countryId}"/>
      <s:submit name="saveAddress" value="Save Address" class="btn btn-blue continue"  style="margin-top:20px;margin-left:0;"/>
  </s:form>
    </div>
</s:layout-component>

</s:layout-render>
<script type="text/javascript">
    window.onload = function() {
        $('#myAddressesLink').addClass('selected');
    };

    $(document).ready(function(){
        $('.remove-error').click(function () {
            $(this).parent('.err-cntnr').remove();
        });
        $('.remove-success').click(function () {
            $(this).parent('.alert-cntnr').remove();
        });
    });
</script>