<%@ taglib prefix="itv" uri="http://healthkart.com/taglibs/hkTagLib" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="mhc.common.constants.EnumReviewStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="mhc.web.action.admin.PendingProductReviewAction" event="pre" var="rpBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Pending Reward Points Queue">

  <s:layout-component name="content">

    <fieldset class="top_label">
      <ul>
        <div class="grouped grid_12">
          <s:form beanclass="mhc.web.action.admin.PendingProductReviewAction" method="get" autocomplete="false">
            <li><label>Product</label> <s:text name="product"/></li>
            <li><label style="float:left;">Review Status </label>

              <div class="checkBoxList">
                <s:select name="reviewStatus">
                  <option value="">All Types</option>
                  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="reviewStatusList"
                                             value="id" label="name"/>
                </s:select>
              </div>
            </li>
            <div class="buttons"><s:submit name="pre" value="Search"/></div>
          </s:form>
        </div>
      </ul>

    </fieldset>

    <s:form beanclass="mhc.web.action.admin.PendingProductReviewAction">

      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rpBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rpBean}"/>

      <table class="cont">
        <thead>
        <tr>
          <th>Sr No.</th>
          <th>User</th>
          <th>Date</th>
          <th>Status</th>
          <th>Review</th>
          <th>Mood</th>

        </tr>
        </thead>
        <c:forEach items="${rpBean.productReviews}" var="review" varStatus="ctr">
        <s:hidden name="productReviews[${ctr.index}]" value="${review.id}"/>
        <tr>
          <td>${ctr.count}</td>
          <td>${review.postedBy.name}</td>
          <td>${review.reviewDate}</td>
          <td>
            <s:select name="productReviews[${ctr.index}].reviewStatus">
              <s:option value="<%=EnumReviewStatus.Pending.getId()%>">Pending</s:option>
              <s:option value="<%=EnumReviewStatus.Published.getId()%>">Published</s:option>
              <s:option value="<%=EnumReviewStatus.Deleted.getId()%>">Deleted</s:option>
            </s:select></td>
          <td>
            <pre>${review.review}</pre>
          </td>
          <td><s:text name="productReviews[${ctr.index}].mood"/></td>

          </c:forEach>
      </table>
      <s:submit name="saveReviews" value="Save"/>

      <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${rpBean}"/>
      <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${rpBean}"/>

    </s:form>


  </s:layout-component>

</s:layout-render>
