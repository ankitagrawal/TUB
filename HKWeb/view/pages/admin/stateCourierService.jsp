<%@ page import="com.hk.constants.courier.StateList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction" var="scsaBean" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="State Courier Service">

  <s:layout-component name="heading">State Courier Service</s:layout-component>
  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.admin.courier.StateCourierServiceAction" method="post">
      <table>
        <tr>
            <th>State</th>
              <br/>
            <th>Selected State</th>

        </tr>
        <%--<c:forEach items="${scsaBean.stateCourierServiceList}" var="stateCourierService" varStatus="ctr">--%>
          <%--<s:hidden name="stateCourierServiceList[${ctr.index}].id" value="${stateCourierService.id}"/>--%>
          <tr>
          <td>
           <s:select name= "scsaBean.state" value="${scsaBean.state}">
              <s:option value="">None</s:option>
               <s:options-collection  collection="<%=StateList.stateList%>">
               </s:options-collection>
           </s:select>
             </td>

           <td>
           ${scsaBean.state}
           </td>
          </tr>



            <%--<td>${ctr.index+1}.</td>--%>

            <%--<td>${stateCourierService.state}--%>
              <%--<s:hidden name="stateCourierServiceList[${ctr.index}].state" value="${stateCourierService.state}"/>--%>
            <%--</td>--%>
            <%--<td>--%>
              <%--<s:select name="stateCourierServiceList[${ctr.index}].courier.id" value="${stateCourierService.courier.id}">--%>
                <%--<s:option value="">None</s:option>--%>
                <%--<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="${scsaBean.state}"--%>
                                           <%--label="name"/>--%>
              <%--</s:select>--%>
            <%--</td>--%>

           <table>
              <tr>
                  <th>S.No.</th>
                  <th>Courier Name</th>
                  <th>Preference</th>
              </tr>


          <tr>
            <c:forEach items="${scsaBean.stateCourierServiceList}" var="stateCourierService" varStatus="count">

               <td> ${count.index+1}</td>
               <td>${stateCourierService.courier.name}</td>
                <td>${stateCourierService.preference}</td>



            </c:forEach>

          </tr>

        <%--</c:forEach>--%>
        </table>
      </table>
      <s:submit name="save" value="Save"/>
    </s:form>
  </s:layout-component>

</s:layout-render>
