<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.offer.EditCouponAction" var="couponBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Edit Coupon</s:layout-component>
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="content">
    <div>
      <s:form beanclass="com.hk.web.action.admin.offer.EditCouponAction">
        <s:errors/>
        <s:text name="code"/>
        <s:submit name="find" value="Edit Coupon"/>
        <s:submit name="findOfferInstanceListByCoupon" value="Find Offer Instances"/>
      </s:form>
      <s:form beanclass="com.hk.web.action.admin.offer.EditCouponAction">
        <s:errors/>
        <div class="round-cont" style="width:650px;margin-top: 20px;">
          <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 14px;">
            <tr style="font-size: 12px;">
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Code</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Offer Id</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">End Date</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Allowed Times</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Already Used</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Create Date</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Referred User ID</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Valid</th>
              <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Repetitive Usage</th>
            </tr>
            <tr style="border-bottom: 1px solid #f0f0f0;">
              <td width="100px">
                  ${couponBean.coupon.code}
              </td>
              <td width="50px">
                    ${couponBean.coupon.offer.id}
              </td>
              <td width="200px">
                <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate" value="${couponBean.coupon.endDate}"/>
              </td>
              <td width="25px">
                <s:text name="allowedTimes" value="${couponBean.coupon.allowedTimes}"/>
              </td>
              <td width="25px">
                  ${couponBean.coupon.alreadyUsed}
              </td>
              <td width="200px">
                <fmt:formatDate value="${couponBean.coupon.createDate}" type="both"/>
              </td>
              <td>
                <c:if test="${!empty couponBean.coupon.referrerUser.id}">
                  <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction">
                    <s:param name="userFilterDto.login" value="${couponBean.coupon.referrerUser.login}"/>
                    ${couponBean.coupon.referrerUser.id}
                  </s:link>
                </c:if>
              </td>
              <td width="50px">
                  ${couponBean.coupon.valid}
              </td>
               <td width="50px">
                 <s:checkbox name="repetitiveUsage" checked="${couponBean.coupon.repetitiveUsage}"/>
              </td>
            </tr>
          </table>
        </div>
        <s:hidden name="coupon" value="${couponBean.coupon}"/>
        <s:hidden name="code" value="${couponBean.code}"/>
        <s:submit name="save" value="Save"/>
      </s:form>
        <s:form beanclass="com.hk.web.action.admin.offer.EditCouponAction">
          <s:errors/>
          <div class="round-cont" style="width:650px;margin-top: 20px;">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size: 14px;">
              <tr style="font-size: 12px;">
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Offer Instance Id</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">User id</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">User Email</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Create Date</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">End Date</th>
                <th style="background: #f0f0f0; padding: 5px; font-weight: bold;">Active</th>
              </tr>
              <c:forEach items="${couponBean.offerInstanceList}" var="offerInstance" varStatus="ctr">
              <tr style="border-bottom: 1px solid #f0f0f0;">
                <td width="50px">
                    ${offerInstance.id}
                </td>
                <td width="25px">
                    <c:if test="${!empty offerInstance.user.id}">
                      <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction">
                        <s:param name="userFilterDto.login" value="${offerInstance.user.email}"/>
                        ${offerInstance.user.id}
                      </s:link>
                    </c:if>
                </td>
                <td width="150px">
                    ${offerInstance.user.email}
                </td>
                <td width="200px">
                    <fmt:formatDate value="${offerInstance.createDate}" type="both"/>
                </td>
                <td width="200px">
                    <fmt:formatDate value="${offerInstance.endDate}" type="both"/>
                </td>
                <td width="50px">
                    ${offerInstance.active}
                </td>                 
              </tr>
              </c:forEach>
            </table>
          </div>
        </s:form>
    </div>
  </s:layout-component>
</s:layout-render>