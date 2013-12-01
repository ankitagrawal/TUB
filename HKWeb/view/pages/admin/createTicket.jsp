<%@ page import="com.hk.constants.ticket.EnumTicketStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.ticket.CreateTicketAction" event="pre" var="ticketBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">
  <s:layout-component name="heading">${ticketBean.currentBreadcrumb.name}</s:layout-component>

  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.ticket.CreateTicketAction" method="post">
      <s:errors/>
      <fieldset class="top_label">
        <ul>
          <li><label>Short Description</label><s:text name="ticketDto.shortDescription" style="width:600px;"/></li>
          <li><label>Full Description</label><s:textarea name="ticketDto.fullDescription" style="width:600px;"/></li>
          <li>
            <label>Ticket Type</label>
            <s:select name="ticketDto.ticketType" value="${ticketBean.ticketDto.ticketType.id}">
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketTypeList" value="id" label="name"/>
            </s:select>
          </li>
          <li>
            <label>Owner (Assign to)</label>
            <s:select name="ticketDto.owner" value="">
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="ticketAdminList" value="id" label="name"/>
            </s:select>
          </li>
          <li><label>Associated Order Id</label><s:text name="ticketDto.associatedOrderId"/></li>
          <li><label>Associated Email</label><s:text name="ticketDto.associatedEmail"/></li>
          <li><label>Associated Phone</label><s:text name="ticketDto.associatedPhone"/></li>
          <li><label>Associated Tracking Id</label><s:text name="ticketDto.associatedTrackingId"/></li>
          <li><label>Associated Courier Id</label>
            <s:select name="ticketDto.associatedCourier" class="courierId">
              <option value="0">None</option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id" label="name"/>
            </s:select>
          </li>
          <s:hidden name="ticketDto"/>
          <s:hidden name="ticketDto.ticketStatus" value="<%=EnumTicketStatus.OPEN.getId()%>"/>
          <div class="buttons"><s:submit name="create" value="Create Ticket"/></div> 
        </ul>
      </fieldset>
    </s:form>

  </s:layout-component>

</s:layout-render>
