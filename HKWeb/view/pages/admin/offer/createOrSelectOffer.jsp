<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.offer.CreateOrSelectOfferActionAction" var="offerActionBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="heading">Step 2: Create / Select offer action</s:layout-component>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>

  <s:layout-component name="content">

    <div class="no_data">

      <c:choose>
        <c:when test="${offerActionBean.offerTrigger == null}">
          <h3>No Trigger selected</h3>
        </c:when>
        <c:otherwise>
          <h3>Selected Trigger : ${offerActionBean.offerTrigger.description}     </h3>
        </c:otherwise>
      </c:choose>
    </div>

    <s:form beanclass="com.hk.web.action.admin.offer.CreateOrSelectOfferActionAction">
      <s:errors/>
      <fieldset class="right_label">
        <legend>Select an existing offer action</legend>
        <ul>
          <s:hidden name="offerTrigger"/>
          <li>
            <label>Offer Action</label>
            <s:select name="offerActionSelect">
              <s:options-collection collection="${offerActionBean.offerActionList}" label="description" value="id"/>
            </s:select>
          </li>
          <li><label>&nbsp;</label>

            <div class="buttons"><s:submit name="select" value="Select Offer Action"/></div>
          </li>
        </ul>
      </fieldset>
    </s:form>
    <s:form beanclass="com.hk.web.action.admin.offer.CreateOrSelectOfferActionAction">
      <fieldset class="right_label">
        <legend>Create a new offer action</legend>
        <ul>
          <s:hidden name="offerTrigger"/>
          <li><label>Description</label><s:text name="offerActionCreate.description"/></li>
          <li><label>Product Group</label>
            <s:select name="offerActionCreate.productGroup">
              <s:option label="n.a." value=""/>
              <s:options-collection collection="${offerActionBean.productGroupList}" label="name" value="id"/>
            </s:select>
          </li>
          <li><label>Discount
            percent on HK Price</label><s:text name="offerActionCreate.discountPercentOnHkPrice" formatType="percentage"/></li>
          <li><label>Order level discount</label><s:text name="offerActionCreate.orderLevelDiscountAmount"/></li>
          <li><label>Discount
            percent on MRP</label><s:text name="offerActionCreate.discountPercentOnMarkedPrice" formatType="percentage"/></li>
          <li><label>Discount on
            shipping</label><s:text name="offerActionCreate.discountPercentOnShipping" formatType="percentage"/></li>
          <li><label>Qty limit</label><s:text name="offerActionCreate.qty"/></li>
          <li><label>Cashback Offer</label><s:checkbox name="offerActionCreate.cashback"/></li>
          <li><label>Reward Point Limit</label><s:text name="offerActionCreate.rewardPointCashbackLimit"/></li>
          <li><label>Reward Point Discount Percent</label><s:text name="offerActionCreate.rewardPointDiscountPercent"/></li>
	      <li><label>Reward Points Expiry Date</label><s:text class="date_input endDate" style="width:150px"
                                            formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="offerActionCreate.rewardPointExpiryDate"/>
          </li>
	        <li><label>Reward Point Redeem Within Days</label><s:text name="offerActionCreate.rewardPointRedeemWithinDays"/></li>
          <li><label>Free Variant Id:<s:text name="offerActionCreate.freeVariant"/></label>
          <li><label>&nbsp;</label>

            <div class="buttons"><s:submit name="create" value="Create New Offer Action"/></div>
          </li>
        </ul>
      </fieldset>
    </s:form>
  </s:layout-component>
</s:layout-render>
