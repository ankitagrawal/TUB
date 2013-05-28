<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction" var="hkdBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Healthkart Delivery">

  <s:layout-component name="htmlHead">

    <style type="text/css">

      fieldset input[type="text"], input[type="text"] {
        font-size: 14px;
        padding: 2px;
        height: 18px;
        width: 200px;
        max-width: 300px;
      }

    </style>

  </s:layout-component>

  <s:layout-component name="content">
    <div class="hkDeliveryWorksheetBox">
      <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDConsignmentAction">
        <fieldset class="right_label">
          <legend>Track Consignment</legend>
          <ul>
            <li>
              <label style="font-size:medium;">Consignment Number :</label>
              <s:text name="consignmentNumber" value="${hkdBean.consignmentNumber}" class="consignmentNumber"/>
            </li>
            <li>
              <s:submit id="submitButton" name="trackConsignment" value="Track Consignment"
                        class="verifyData">
                Track Consignment
                <s:param name="doTracking" value="true"/>
              </s:submit>
            </li>
          </ul>
        </fieldset>
      </s:form>
    </div>
    <c:if test="${!empty hkdBean.consignmentTrackingList}">
      <div id="consignmentTrackingData" style="font-size: small">
        <label style="font-size:medium; font-weight:bold;">Assignned to : </label>
          ${hkdBean.consignment.runsheet.agent.name} <br/> <br/>
        <label style="font-size:medium; font-weight: bold;">Address : </label>
          ${hkdBean.consignment.address}
        <table class="zebra_vert" style="font-size: medium;">
          <thead>
          <tr>
            <th>Consignment Number</th>
            <th>Cnn Number</th>
            <th>Source Hub</th>
            <th>Destination Hub</th>
            <th>Date</th>
            <th>Status</th>
            <th>Remarks</th>
            <th>NDR Action</th>
            <th>Activity by user:</th>
          </tr>
          </thead>
          <c:forEach items="${hkdBean.consignmentTrackingList}" var="consignmentTrackingList" varStatus="ctr">
            <tr>
              <td>${consignmentTrackingList.consignment.awbNumber}</td>
              <td>${consignmentTrackingList.consignment.cnnNumber}</td>
              <td>${consignmentTrackingList.sourceHub.name}</td>
              <td>${consignmentTrackingList.destinationHub.name}</td>
              <td><fmt:formatDate value="${consignmentTrackingList.createDate}" type="both" timeStyle="short"/></td>
              <td>${consignmentTrackingList.consignmentLifecycleStatus.status}</td>
              <td>${consignmentTrackingList.remarks}</td>
              <td>${consignmentTrackingList.ndrResolution}</td>
              <td>${consignmentTrackingList.user.name}</td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </c:if>
  </s:layout-component>
</s:layout-render>
<script type="text/javascript">
  $('.verifyData').click(function () {
    //alert("in validator");
    var consignmentNumber = $('.consignmentNumber').val();
    if (consignmentNumber == "") {
      alert("Please enter consignment number.");
      return false;
    }
  });
</script>


