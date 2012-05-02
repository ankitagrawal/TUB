<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.offer.CreateCouponAction" event="pre" var="couponBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">Step 3: Create offer</s:layout-component>
  <s:layout-component name="content">

    <s:errors/>
    <h3>
      Generate a single coupon:
    </h3>
    <s:form beanclass="com.hk.web.action.admin.offer.CreateCouponAction" method="post">
      <label>Offer: </label>
      <c:choose>
        <c:when test="${couponBean.offer == null}">
          <s:link beanclass="com.hk.web.action.admin.offer.SelectOfferAction">
            Select Offer
          </s:link>
        </c:when>
        <c:otherwise>
          <s:hidden name="offer" value="${couponBean.offer}"/>
          ${couponBean.offer.description}
          <s:link beanclass="com.hk.web.action.admin.offer.SelectOfferAction">
            <s:param name="offer" value="${couponBean.offer.id}"/>
            Change Offer
          </s:link>
        </c:otherwise>
      </c:choose><br/>
      <fieldset class="left_label">
        <ul>
          <li><label>Coupon Code </label>
            <s:text name="couponCode" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>Number of times allowed </label>
            <s:text name="allowedTimes" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>Number of times already used </label>
            <s:text name="alreadyUsed" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>Coupon Expiry Date</label>
            <s:text name="endDate" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" disabled="${couponBean.offer == null ? 'true':'false'}"/>
          </li>
          <li><label>Repetitive Usage?</label>
            <s:checkbox name="repetitiveUsage"/>
          </li>
        </ul>
      </fieldset>
      <s:hidden name="numberOfCoupons" value="1"/>
      <s:submit name="generateSingle" value="create coupon"/> <br/>
    </s:form>

    <h3>
      Generate a multiple coupons:
    </h3>
    <s:form beanclass="com.hk.web.action.admin.offer.CreateCouponAction" method="post">
      <label>Offer: </label>
      <c:choose>
        <c:when test="${couponBean.offer == null}">
          <s:link beanclass="com.hk.web.action.admin.offer.SelectOfferAction">
            Select Offer
          </s:link>
        </c:when>
        <c:otherwise>
          <s:hidden name="offer" value="${couponBean.offer}"/>
          ${couponBean.offer.description}
          <s:link beanclass="com.hk.web.action.admin.offer.SelectOfferAction">
            <s:param name="offer" value="${couponBean.offer.id}"/>
            Change Offer
          </s:link>
        </c:otherwise>
      </c:choose><br/>
      <fieldset class="left_label">
        <ul>
          <li><label>Intial Coupon Code </label>
            <s:text name="couponCode" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>End Part of Coupon Code </label>
            <s:text name="endPart" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>Number of coupons </label>
            <s:text name="numberOfCoupons" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>Number of times allowed </label>
            <s:text name="allowedTimes" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>Number of times already used </label>
            <s:text name="alreadyUsed" disabled="${couponBean.offer == null ? 'true':'false'}"/></li>
          <li><label>Coupon Expiry Date</label>
            <s:text name="endDate" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" disabled="${couponBean.offer == null ? 'true':'false'}"/>
          </li>
          <li><label>Repetitive Usage?</label>
            <s:checkbox name="repetitiveUsage"/>
          </li>
        </ul>
      </fieldset>
      <s:submit name="generateMulti" value="Create coupons"/><br/>
      Campaign Code: <s:text name="campaignCode"/><br/>
      <s:submit name="generateMultiFanCoupon" value="Create coupons and insert into FB Campaign"/>
      <h2>File to Upload
        <s:file name="fileBean" size="30"/></h2>
      <s:submit name="uploadCouponsAndSaveIntoDB" value="Upload(CSV) coupons and insert into DB"/>
    </s:form>

  </s:layout-component>
</s:layout-render>
