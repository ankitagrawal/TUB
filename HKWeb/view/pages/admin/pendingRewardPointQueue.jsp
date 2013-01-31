<%@ taglib prefix="itv" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.user.PendingRewardPointQueueAction" event="pre" var="rpBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pending Reward Points Queue">

  <s:layout-component name="content">

    <fieldset class="top_label">
      <ul>
        <div class="grouped grid_12">
          <s:form beanclass="com.hk.web.action.admin.user.PendingRewardPointQueueAction" method="get" autocomplete="false">

            <li><label style="float:left;">Reward Point Type </label>

              <div class="checkBoxList">
                <s:select name="rewardPointMode">
                  <option value="">All Types</option>
                  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="rewardPointModes" value="id" label="name"/>
                </s:select>
              </div>
            </li>

            <%--<li><label style="float:left;">Reward Point Status </label>

              <div class="checkBoxList">
                <s:select name="rewardPointStatus">
                  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="rewardPointStatusList" value="id" label="name"/>
                </s:select>
              </div>
            </li>--%>
            <div class="buttons"><s:submit name="pre" value="Search"/></div>
          </s:form>
        </div>
      </ul>

    </fieldset>

    <s:form beanclass="com.hk.web.action.admin.user.PendingRewardPointQueueAction">

      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rpBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rpBean}"/>

      <table class="cont">
        <thead>
        <tr>
          <th>Sr No.</th>
          <th>User</th>
          <th>Referred User</th>
          <th>Referred Order</th>
          <th>Type</th>
          <th>Status</th>
          <th>Comment</th>
        </tr>
        </thead>
        <c:forEach items="${rpBean.rewardPointList}" var="rewardPoint" varStatus="ctr">
          <tr>
            <td>
              <s:hidden name="rewardPointList[${ctr.index}]" value="${rewardPoint.id}"/>
              <s:hidden name="rewardPointList[${ctr.index}].id" value="${rewardPoint.id}"/>
              <s:hidden name="rewardPointList[${ctr.index}].user" value="${rewardPoint.user}"/>
              <s:hidden name="rewardPointList[${ctr.index}].referredUser" value="${rewardPoint.referredUser}"/>
              <s:hidden name="rewardPointList[${ctr.index}].createDate" value="${rewardPoint.createDate}"/>
              <s:hidden name="rewardPointList[${ctr.index}].value" value="${rewardPoint.value}"/>
              <s:hidden name="rewardPointList[${ctr.index}].rewardPointMode" value="${rewardPoint.rewardPointMode.id}"/>
              <s:hidden name="rewardPointList[${ctr.index}].referredOrder" value="${rewardPoint.referredOrder}"/>
                ${ctr.count}
            </td>
            <td class="has_table">
              Name: ${rewardPoint.user.name} <br/>
              Email: ${rewardPoint.user.email} <br/><br/>
              User Address:
              <c:forEach items="${rewardPoint.user.addresses}" var="address" varStatus="addressCount">
                <table>
                  <tr>
                    <td>
                        ${address.name} <br/>
                        ${address.line1}<br/>
                        ${address.line2}<br/>
                        ${address.city} - ${address.pincode.pincode}<br/>
                        ${address.state}<br/>
                      Ph: ${address.phone}<br/>
                    </td>
                  </tr>
                </table>
              </c:forEach>
            </td>
            <td class="has_table">
              Name: ${rewardPoint.referredUser.name} <br/>
              Email: ${rewardPoint.referredUser.email}
              <br/>
              <s:link beanclass="com.hk.web.action.admin.user.UserReferrralsAddressesAction" event="pre" target="_blank">
                <s:param name="user" value="${rewardPoint.user}"/>
                Previous Referral Addresses
              </s:link>
              <br/>
              Referred Address:
              <c:forEach items="${rewardPoint.referredUser.addresses}" var="address" varStatus="addressCount">
                <table>
                  <tr>
                    <td>
                        ${address.name} <br/>
                        ${address.line1}<br/>
                        ${address.line2}<br/>
                        ${address.city} - ${address.pincode.pincode}<br/>
                        ${address.state}<br/>
                      Ph: ${address.phone}<br/>
                    </td>
                  </tr>
                </table>
              </c:forEach>
            </td>
            <td class="has_table">
              Order Id:
              <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders" target="_blank">
                <s:param name="orderId" value="${rewardPoint.referredOrder.id}"/>
                ${rewardPoint.referredOrder.id}
              </s:link>
              <br/>
              Order Date: <fmt:formatDate value="${rewardPoint.referredOrder.payment.paymentDate}" pattern="dd/MM/yyyy"/>
              <br/>
              Payment: ${rewardPoint.referredOrder.payment.paymentMode.name}
              &nbsp;-&nbsp;
              <fmt:formatNumber value="${rewardPoint.referredOrder.amount}" type="currency" currencySymbol="Rs. "/>
               <br/>
              Order Status: ${rewardPoint.referredOrder.orderStatus.name}
            </td>
            <td>
                ${rewardPoint.rewardPointMode.name}
            </td>
            <td>
              <s:select name="rewardPointList[${ctr.index}].rewardPointStatus" value="${rewardPoint.rewardPointStatus.id}">
                <itv:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="rewardPointStatusList" value="id" label="name"/>
              </s:select>
            </td>
            <td>
              <s:textarea name="rewardPointList[${ctr.index}].comment" style="height:100px;"/>
            </td>
          </tr>
        </c:forEach>
      </table>
      <s:submit name="save" value="Save"/>

      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rpBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rpBean}"/>

    </s:form>


  </s:layout-component>

</s:layout-render>
