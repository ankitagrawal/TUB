<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.ticket.ViewAndEditTicketAction" var="ticketBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
  <s:layout-component name="heading">${ticketBean.currentBreadcrumb.name}
    <em>[${ticketBean.ticket.ticketStatus.name}]</em>: Type - ${ticketBean.ticket.ticketType.name}</s:layout-component>

  <s:layout-component name="content">
    <s:errors/>
    <div style="background-color:beige; padding: 5px; margin-bottom: 10px; border: 1px solid darkgray; width: 600px;">
      <div style="position: relative; float: right;">
        <span class="sml gry" title="<fmt:formatDate value="${ticketBean.ticket.createDate}" type="both"/>">Created: ${hk:timeAgo(ticketBean.ticket.createDate)}</span>
        <c:if test="${not (ticketBean.ticket.createDate eq ticketBean.ticket.updateDate)}">
          <br/><span class="sml gry" title="<fmt:formatDate value="${ticketBean.ticket.updateDate}" type="both"/>">Updated: ${hk:timeAgo(ticketBean.ticket.updateDate)}</span>
        </c:if>
      </div>
      <h3>${ticketBean.ticket.shortDescription}</h3>

      <p>${hk:convertNewLineToBr(ticketBean.ticket.fullDescription)}</p>
      <hr style="border: 1px dotted darkgray"/>
      <div style="position: relative; float: right;">
        <span class="sml gry">Reported By: ${ticketBean.ticket.reporter.name} (${ticketBean.ticket.reporter.email})</span><br/>
        <span class="sml gry">Assigned To: ${ticketBean.ticket.owner.name} (${ticketBean.ticket.owner.email})</span>
      </div>
      <div>
        <em>Ticket context data</em><br/>
        <c:if test="${ticketBean.ticket.associatedUser != null}">
          <span class="sml gry">Associated User:</span>
          <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
            ${ticketBean.ticket.associatedUser.email}
            <s:param name="userFilterDto.email" value="${ticketBean.ticket.associatedUser.email}"/>
          </s:link><br/>
        </c:if>
        <c:if test="${ticketBean.ticket.associatedOrder != null}">
          <span class="sml gry">Associated Order:</span>
          <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders">
            ${ticketBean.ticket.associatedOrder.id}
            <s:param name="orderId" value="${ticketBean.ticket.associatedOrder.id}"/>
          </s:link>
          <br/>
        </c:if>
        <c:if test="${ticketBean.ticket.associatedEmail != null}">
          <span class="sml gry">Associated Email:</span> ${ticketBean.ticket.associatedEmail}<br/>
        </c:if>
        <c:if test="${ticketBean.ticket.associatedPhone != null}">
          <span class="sml gry">Associated Phone:</span> ${ticketBean.ticket.associatedPhone}<br/>
        </c:if>
        <c:if test="${ticketBean.ticket.associatedTrackingId != null}">
          <span class="sml gry">Associated Tracking Id:</span>
          <s:link beanclass="com.hk.web.action.core.order.TrackCourierAction" event="pre" target="_blank">
            ${ticketBean.ticket.associatedTrackingId}
            <s:param name="trackingId" value="${ticketBean.ticket.associatedTrackingId}"/>
            <s:param name="courierId" value="${ticketBean.ticket.associatedCourier.id}"/>
          </s:link>
          <br/>
        </c:if>
        <c:if test="${ticketBean.ticket.associatedCourier != null}">
          <span class="sml gry">Associated Courier:</span> ${ticketBean.ticket.associatedCourier.name}<br/>
        </c:if>
      </div>
    </div>

    <hr/>
    <h2>Ticket History</h2>

    <div style="background-color: #ffffcc; padding: 5px; margin-bottom: 10px; border:1px solid darkgray; width:600px">
      <c:choose>
        <c:when test="${fn:length(ticketBean.displayTicketHistoryDtos) == 0}">
          <p>None</p>
        </c:when>
        <c:otherwise>
          <c:forEach items="${ticketBean.displayTicketHistoryDtos}" var="displayDto">
            <div>
              <ul class="cont">
                <c:if test="${hk:isNotBlank(displayDto.ownerChangeMessage)}">
                  <li><strong>Owner:</strong> ${displayDto.ownerChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.statusChangeMessage)}">
                  <li><strong>Status:</strong> ${displayDto.statusChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.typeChangeMessage)}">
                  <li><strong>Type:</strong> ${displayDto.typeChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.shortDescriptionChangeMessage)}">
                  <li><strong>Short Description:</strong> ${displayDto.shortDescriptionChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.fullDescriptionChangeMessage)}">
                  <li><strong>Full Description:</strong> ${displayDto.fullDescriptionChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.associatedOrderChangeMessage)}">
                  <li><strong>Associated Order:</strong> ${displayDto.associatedOrderChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.associatedUserChangeMessage)}">
                  <li><strong>Associated User:</strong> ${displayDto.associatedUserChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.associatedEmailChangeMessage)}">
                  <li><strong>Associated Email:</strong> ${displayDto.associatedEmailChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.associatedPhoneChangeMessage)}">
                  <li><strong>Associated Phone:</strong> ${displayDto.associatedPhoneChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.associatedTrackingIdChangeMessage)}">
                  <li><strong>Associated Tracking Id:</strong> ${displayDto.associatedTrackingIdChangeMessage}</li>
                </c:if>
                <c:if test="${hk:isNotBlank(displayDto.associatedCourierChangeMessage)}">
                  <li><strong>Associated Courier:</strong> ${displayDto.associatedCourierChangeMessage}</li>
                </c:if>
              </ul>
              <c:if test="${hk:isNotBlank(displayDto.comment)}">
                <span>${displayDto.comment}</span><br/>
              </c:if>
              <p class="sml gry" style="text-align: right;" title="<fmt:formatDate value="${displayDto.changedDate}" type="both" />">${hk:timeAgo(displayDto.changedDate)},
                changed
                by ${displayDto.changedBy.name} (${displayDto.changedBy.email})</p>
              <hr style="border: 1px dotted darkgray"/>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>

    <s:form beanclass="com.hk.web.action.admin.ticket.ViewAndEditTicketAction" method="post">
      <hr/>
      <div>
        <h2>Update</h2>
        <fieldset class="top_label">
          <ul>
            <li>
              <label>Comment</label><s:textarea name="ticketHistoryDto.comment" style="width: 600px;"/>
            </li>
            <li>
              <label>Change Ticket Status To</label>
              <s:select name="ticketHistoryDto.ticketStatus" value="${ticketBean.ticket.ticketStatus.id}">
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketStatusList" value="id" label="name"/>
              </s:select>
            </li>
          </ul>
        </fieldset>
      </div>
      <hr/>
      <div>
        <h2>Edit Ticket</h2>
        <s:hidden name="ticketHistoryDto.ticket" value="${ticketBean.ticket.id}"/>
        <s:hidden name="ticketHistoryDto" value=""/>
        <fieldset class="top_label">
          <ul>
            <li>
              <label>Short
                Description</label><s:text name="ticketHistoryDto.shortDescription" value="${ticketBean.ticket.shortDescription}" style="width:600px;"/>
            </li>
            <li><label>Full
              Description</label><s:textarea name="ticketHistoryDto.fullDescription" value="${ticketBean.ticket.fullDescription}" style="width:600px;"/>
            </li>
            <li>
              <label>Change Ticket Type To</label>
              <s:select name="ticketHistoryDto.ticketType" value="${ticketBean.ticket.ticketType.id}">
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketTypeList" value="id" label="name"/>
              </s:select>
            </li>
            <li>
              <label>Change Owner To</label>
              <s:select name="ticketHistoryDto.owner" value="${ticketBean.ticket.owner.id}">
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketAdminList" value="id" label="name"/>
              </s:select>
            </li>
            <li><label>Associated Order
              Id</label><s:text name="ticketHistoryDto.associatedOrder" value="${ticketBean.ticket.associatedOrder.id}"/>
            </li>
            <li>
              <label>Associated Email</label>
              <s:text name="ticketHistoryDto.associatedEmail" value="${ticketBean.ticket.associatedUser != null ? ticketBean.ticket.associatedUser.email : ticketBean.ticket.associatedEmail}"/>
            </li>
            <li><label>Associated
              Phone</label><s:text name="ticketHistoryDto.associatedPhone" value="${ticketBean.ticket.associatedPhone}"/>
            </li>
            <li><label>Associated Tracking
              Id</label><s:text name="ticketHistoryDto.associatedTrackingId" value="${ticketBean.ticket.associatedTrackingId}"/>
            </li>
            <li><label>Associated Courier</label>
              <s:select name="ticketHistoryDto.associatedCourier" value="${ticketBean.ticket.associatedCourier.id}" class="courierId">
                <option value="0">None</option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
              </s:select>
            </li>
          </ul>
          <div class="buttons">
            <s:submit name="edit" value="Submit Changes"/>
          </div>
        </fieldset>
      </div>
    </s:form>

  </s:layout-component>

</s:layout-render>
