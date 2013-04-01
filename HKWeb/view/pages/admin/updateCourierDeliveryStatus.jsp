<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction" var="AFLDelivery"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

    </s:layout-component>

    <s:layout-component name="heading">Update Courier Order Status</s:layout-component>

    <s:layout-component name="content">
        <table width="70%">
            <tr>
                <td>
                    <div class="updateCourierStatus" style="width:70%;height:300px">
                        <s:form beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction">
                            <s:errors/>   
                            <fieldset class="right_label">
                                <legend>Orders</legend>
                                <ul>
                                    <li>
                                        <label>Courier</label><s:select name="courierName" class="courier">
                                                      <s:option value="">-Select Courier-</s:option>
                                                      <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierListForAutoDeliveryMarking"/>
                                                    </s:select>

                                    </li>

                                    <li>
                                        <label>Start
                                            date</label><s:text class="date_input startDate startDateCourier" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="startDate"/>
                                    </li>
                                    <li>
                                        <label>End
                                            date</label><s:text class="date_input endDate endDateCourier" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="endDate"/>
                                    </li>

                                    <li>
                                        <s:submit class="verifyData" name="updateCourierStatus" value="Update Delivery Status"/>

                                    </li>
                                </ul>
                            </fieldset>
                            <c:if test="${AFLDelivery.unmodifiedTrackingIdsAsString != null}">
                               <p> Could'nt  get  response for following Tracking Ids:</p>
                                <p>${AFLDelivery.unmodifiedTrackingIdsAsString}</p>

                            </c:if>
                        </s:form>
                    </div>
                </td>
            </tr>
        </table>
    </s:layout-component>
</s:layout-render>
<script type="text/javascript">
      $(document).ready(function() {
        var startDate = "";
        var endDate = "";
        var courierName="";
        $('.verifyData').click(function() {
          startDate = $('.startDateCourier').val();
          endDate = $('.endDateCourier').val();
          return _checkDateValidity();
        });
          function _checkDateValidity() {
              if (startDate == "yyyy-mm-dd hh:mm:ss" || endDate == "yyyy-mm-dd hh:mm:ss")
              {
                  alert("Please enter both dates.")
                  return false;
              } else if (startDate > endDate) {
                  alert("End Date cannot be less than Start Date.");
                  return false;
              } else {
                  return true;
              }
          }
      });
    </script>
