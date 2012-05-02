<%@ page import="com.hk.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.admin.StateCourierServiceAction" var="scsaBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="State Courier Service">

  <s:layout-component name="heading">State Courier Service</s:layout-component>
  <s:layout-component name="content">

    <s:form beanclass="web.action.admin.StateCourierServiceAction" method="post">
      <table>
        <tr>
          <th>S.No.</th>
          <th>State</th>
          <th>Default Courier</th>
        </tr>
        <c:forEach items="${scsaBean.stateCourierServiceList}" var="stateCourierService" varStatus="ctr">
          <s:hidden name="stateCourierServiceList[${ctr.index}].id" value="${stateCourierService.id}"/>
          <tr>
            <td>${ctr.index+1}.</td>
            <td>${stateCourierService.state}
              <s:hidden name="stateCourierServiceList[${ctr.index}].state" value="${stateCourierService.state}"/>
            </td>
            <td>
              <s:select name="stateCourierServiceList[${ctr.index}].courier.id" value="${stateCourierService.courier.id}">
                <s:option value="">None</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
                                           label="name"/>
              </s:select>
            </td></tr>
        </c:forEach>
      </table>
      <s:submit name="save" value="Save"/>
    </s:form>
  </s:layout-component>

</s:layout-render>
