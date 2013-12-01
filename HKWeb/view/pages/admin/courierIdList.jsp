<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.shipment.ParseCourierDeliveryStatusExcelAction"  var="pcdea"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Courier Id List">

  <s:layout-component name="content">
      <s:errors/>
      <p><h1>Available Couriers List</h1></p>
      <c:choose>
      <c:when test="${pcdea.courierList != null}">
          <table id="courierIdsTable">
              <tr>
                  <th>Courier_Name</th>
                  <th>Courier_Id</th>
              </tr>
          <c:forEach items="${pcdea.courierList}" var="cList">
             <tr>
                 <td>
                     ${cList.name}
                 </td>
                 <td>
                     ${cList.id}
                 </td>
             </tr>

          </c:forEach>
           </table>
      </c:when>
          <c:otherwise>
              Sorry,some problem occurred.Please try later.
          </c:otherwise>
       </c:choose>

  </s:layout-component>

</s:layout-render>