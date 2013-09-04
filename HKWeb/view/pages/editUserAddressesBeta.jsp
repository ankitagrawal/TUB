<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.UserManageAddressAction" var="umaa"/>
<s:layout-render name="/layouts/defaultBeta.jsp">
<s:layout-component name="heading">Your Account</s:layout-component>
<s:layout-component name="lhsContent">
  <jsp:include page="myaccount-navBeta.jsp"/>
</s:layout-component>
<c:set var="countryId" value="80"/>
<s:layout-component name="rhsContent">
       <c:choose>
         <c:when test="${umaa.address ==null}">
           <h4 class="strikeline">New Address</h4>
         </c:when>
         <c:otherwise>
           <h4 class="strikeline">Edit Address</h4>
         </c:otherwise>
       </c:choose>
  <s:form beanclass="com.hk.web.action.core.user.UserManageAddressAction" id="addressForm">
      <s:layout-render name="/layouts/addressLayoutBeta.jsp" />
      <s:hidden name="countryId" value="${countryId}"/>
      <s:submit name="saveAddress" value="Save Address" class="btn btn-blue continue" />
  </s:form>
</s:layout-component>
</s:layout-render>