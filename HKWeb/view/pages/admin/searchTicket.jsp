<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.ticket.SearchTicketAction" var="ticketBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>

  <s:layout-component name="heading">${ticketBean.currentBreadcrumb.name}</s:layout-component>
  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.ticket.SearchTicketAction" method="get" renderFieldsPresent="false" renderSourcePage="false">
      <s:errors/>
      <fieldset class="left_label">
        <ul>
          <li><label>Ticket Id</label><s:text name="ticketFilterDto.ticketId"/></li>
          <li><label>Keyword (single)</label><s:text name="ticketFilterDto.keywords"/></li>
          <li><label>Owner</label>
            <s:select name="ticketFilterDto.owner" value="">
              <option value="">Select Ticket Owner</option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketAdminList" value="id" label="name"/>
            </s:select>
          </li>
          <li><label>Reporter</label>
            <s:select name="ticketFilterDto.reporter" value="">
              <option value="">Select Reporter</option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketAdminList" value="id" label="name"/>
            </s:select>
          </li>
          <li><label>Ticket Type</label>
            <s:select name="ticketFilterDto.ticketType" value="">
              <option value="">Select Ticket Type</option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketTypeList" value="id" label="name"/>
            </s:select>
          </li>
          <li><label>Ticket Status</label>
            <s:select name="ticketFilterDto.ticketStatus" value="">
              <option value="">Select Ticket Status</option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketStatusList" value="id" label="name"/>
            </s:select>
          </li>
          <li><label>Create Date
            From</label><s:text name="ticketFilterDto.createDateFrom" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
          </li>
          <li><label>Create Date
            To</label><s:text name="ticketFilterDto.createDateTo" class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"/>
          </li>
          <li><label>Associated Order Id</label><s:text name="ticketFilterDto.associatedOrderId"/></li>
          <li><label>Associated Login</label><s:text name="ticketFilterDto.associatedLogin"/></li>
          <li><label>Associated Phone</label><s:text name="ticketFilterDto.associatedPhone"/></li>
        </ul>
        <div class="buttons" style="width: 50%;">
          <s:submit name="search" value="Search" class="btn"/>
        </div>
      </fieldset>
    </s:form>

    <hr/>

    <h2>Tickets:</h2>
    <table class="cont">
      <thead>
      <tr>
        <th>Ticket Id</th>
        <th>Owner</th>
        <th>Short Description</th>
        <th>Ticket Type</th>
        <th>Ticket Status</th>
        <th>Reporter</th>
        <th>Create Date</th>
        <th>Modified Date</th>
      </tr>
      </thead>
      <c:forEach items="${ticketBean.ticketList}" var="ticket">
        <tr>
          <td>${ticket.id}</td>
          <td>${ticket.owner.name}</td>
          <td>
            <s:link beanclass="com.hk.web.action.admin.ticket.ViewAndEditTicketAction" event="pre">
              ${ticket.shortDescription}
              <s:param name="ticket" value="${ticket.id}"/>
            </s:link> <br/>

            <c:if test="${ticket.associatedOrder != null}">
              [Order Id -
              <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders">
                ${ticket.associatedOrder.id}
                <s:param name="orderId" value="${ticket.associatedOrder.id}"/>
              </s:link>]
            </c:if>
            <c:choose>
              <c:when test="${ticket.associatedEmail != null}">
                [Email: ${ticket.associatedEmail}]
              </c:when>
              <c:otherwise>
                <c:if test="${ticket.associatedUser != null}">
                  [<s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
                  ${ticket.associatedUser.name} - ${ticket.associatedUser.email}
                  <s:param name="userFilterDto.email" value="${ticket.associatedUser.email}"/>
                </s:link>]
                </c:if>
              </c:otherwise>
            </c:choose>
            <c:if test="${hk:isNotBlank(ticket.associatedPhone)}">
              [Phone - ${ticket.associatedPhone}]
            </c:if>
            <c:if test="${hk:isNotBlank(ticket.associatedTrackingId)}">
              [Tracking Id -
              <s:link beanclass="com.hk.web.action.core.order.TrackCourierAction" event="pre" target="_blank">
                ${ticket.associatedTrackingId}
                <s:param name="trackingId" value="${ticket.associatedTrackingId}"/>
                <s:param name="courierId" value="${ticket.associatedCourier.id}"/>
              </s:link>]
            </c:if>


          </td>
          <td>${ticket.ticketType.name}</td>
          <td>${ticket.ticketStatus.name}</td>
          <td>${ticket.reporter.name}</td>
          <td><fmt:formatDate value="${ticket.createDate}" type="both"/></td>
          <td><fmt:formatDate value="${ticket.updateDate}" type="both"/></td>
        </tr>
      </c:forEach>
    </table>

    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${ticketBean}"/>
    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${ticketBean}"/>
  </s:layout-component>

</s:layout-render>
