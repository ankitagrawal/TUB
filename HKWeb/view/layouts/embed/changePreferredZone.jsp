<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.ServiceAction" var="sa"/>
<s:layout-definition>
  <%
    String urlFragment = (String) pageContext.getAttribute("filterUrlFragment");
    pageContext.setAttribute("urlFragment", urlFragment);
  %>
  <script type="text/javascript">
    function submitForm(){
      $('.submit_button').click();
    }
  </script>
  <s:form beanclass="com.hk.web.action.core.catalog.category.ServiceAction" name="setCityForm">
    <s:errors/>
    <div width="960px" align="center">
      <label style="font-size:1.3em; font-weight:bold; color:navy;">To view deals in other cities</label>
      <s:select name="preferredZone" value="${sa.preferredZone}" onchange="submitForm()">
        <s:option value="">Select-City</s:option>
        <s:option value="Delhi">Delhi-NCR</s:option>
        <s:option value="Mumbai">Mumbai</s:option>
        <s:option value="Chandigarh">Chandigarh</s:option>
        <s:option value="Bangalore">Bangalore</s:option>
        <s:option value="Hyderabad">Hyderabad</s:option>
        <s:option value="Chennai">Chennai</s:option>
        <s:option value="Jaipur">Jaipur</s:option>
        <s:option value="Calcutta">Kolkata</s:option>
        <s:option value="Pune">Pune</s:option>
        <s:option value="Ahmedabad">Ahmedabad</s:option>
        <s:option value="All">Rest Of India</s:option>
      </s:select>
      <c:if test="${sa.preferredZone != null}">
        &nbsp&nbsp
      <strong>Current Location:</strong> ${sa.preferredZone}
      </c:if>
      <s:hidden name="redirectUrl" value="${pageContext.request.contextPath}${urlFragment}"/>
      <s:submit class="submit_button" name="setCookie" style="display:none;"/>
    </div>
  </s:form>
  <br/>
</s:layout-definition>