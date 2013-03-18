<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.payment.EnumPaymentType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.offer.CreateOfferAction" var="offerBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">Step 3: Create offer</s:layout-component>
  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.offer.CreateOfferAction">
      <s:errors/>
      <fieldset class="right_label">
        <legend>Create an offer</legend>
        <ul>
          <s:hidden name="offerTrigger"/>
          <s:hidden name="offerAction"/>
          <li>
            <label>Description</label>
            <s:text name="description"/>
          </li>
          <li>
            <label>Offer Trigger</label>
            <c:choose>
              <c:when test="${offerBean.offerTrigger == null}">
                No Trigger selected
              </c:when>
              <c:otherwise>
                ${offerBean.offerTrigger.description}
              </c:otherwise>
            </c:choose>
          </li>
          <li>
            <label>Offer Action</label>${offerBean.offerAction.description}
          </li>
          <li>
            <label>Start date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
          </li>
          <li>
            <label>End date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
          </li>
          <li>
              <label>Offer Identifier</label><s:text name="offerIdentifier"/>
          </li>
          <li>
              <label>Offer Terms</label><s:textarea name="terms"/>
          </li>
          <li><label>Exclude Trigger products</label><s:checkbox name="excludeTriggerProducts"/></li>
	        <li>
		        <label>Applicable Payment Type</label><s:select name="paymentType">
		        <c:forEach items="<%=EnumPaymentType.getAllPaymentTypes()%>" var="pType">
			        <s:option value="${pType.id}">${pType.name}</s:option>
		        </c:forEach>
	        </s:select>
	        </li>
          <li>
              <label>Show Promptly</label><s:checkbox name="showPromptly"/>
          </li>
           <li><label>&nbsp;</label>
            <div class="buttons"><s:submit name="create" value="Create Offer"/></div>
          </li>
        </ul>
      </fieldset>
    </s:form>

  </s:layout-component>
</s:layout-render>
