<%@ page import="com.akube.framework.util.FormatUtils" %>
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
        <table>
            <tr>
                <td>
                    <div class="reportBox">
                        <s:form beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction">
                            <s:errors/>
                            <fieldset class="right_label">
                                <legend>AFL Orders</legend>
                                <ul>

                                    <li>
                                        <label>Start
                                            date</label><s:text class="date_input startDate startDateAFL" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="startDate"/>
                                    </li>
                                    <li>
                                        <label>End
                                            date</label><s:text class="date_input endDate endDateAFL" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="endDate"/>
                                    </li>

                                    <li>
                                        <s:submit class="verifyDateAFL" name="updateAFLStatus" value="Update Delivery Status"/>

                                    </li>
                                </ul>
                            </fieldset>
                        </s:form>
                    </div>
                </td>
                <td>
                    <div class="reportBox">
                        <s:form beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction">
                            <s:errors/>
                            <fieldset class="right_label">
                                <legend>Chhotu Orders</legend>
                                <ul>

                                    <li>
                                        <label>Start
                                            date</label><s:text class="date_input startDate startDateChhotu" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="startDate"/>
                                    </li>
                                    <li>
                                        <label>End
                                            date</label><s:text class="date_input endDate endDateChhotu" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="endDate"/>
                                    </li>

                                    <li>
                                        <s:submit name="updateChhotuStatus" value="Update Delivery Status" class="verifyDateChhotu"/>

                                    </li>
                                </ul>
                            </fieldset>
                        </s:form>
                    </div>
                </td>

                <td>
                    <div class="reportBox">
                        <s:form beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction">
                            <s:errors/>
                            <fieldset class="right_label">
                                <legend>DTDC Orders</legend>
                                <ul>

                                    <li>
                                        <label>Start
                                            date</label><s:text class="date_input startDate startDateDTDC" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="startDate"/>
                                    </li>
                                    <li>
                                        <label>End
                                            date</label><s:text class="date_input endDate endDateDTDC" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="endDate"/>
                                    </li>

                                    <li>
                                        <s:submit name="updateDtdcStatus" value="Update Delivery Status" class="verifyDateDTDC"/>

                                    </li>
                                </ul>
                            </fieldset>
                        </s:form>
                    </div>
                </td>
          </tr>
              <tr>
                <td>
                    <div class="reportBox">
                        <s:form beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction">
                            <s:errors/>
                            <fieldset class="right_label">
                                <legend>Delhivery Orders</legend>
                                <ul>

                                    <li>
                                        <label>Start
                                            date</label><s:text class="date_input startDate startDateDelhivery" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="startDate"/>
                                    </li>
                                    <li>
                                        <label>End
                                            date</label><s:text class="date_input endDate endDateDelhivery" style="width:150px"
                                                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                                name="endDate"/>
                                    </li>

                                    <li>
                                        <s:submit name="updateDelhiveryStatus" value="Update Delivery Status" class="verifyDateDelhivery"/>

                                    </li>
                                </ul>
                            </fieldset>
                        </s:form>
                    </div>
                </td>
              <td>
          <div class="reportBox">
            <s:form beanclass="com.hk.web.action.admin.shipment.UpdateDeliveryStatusAction">
              <s:errors/>
              <fieldset class="right_label">
                <legend>BlueDart Orders</legend>
                <ul>

                  <li>
                    <label>Start
                      date</label><s:text class="date_input startDate startDateBlueDart" style="width:150px"
                                          formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                          name="startDate"/>
                  </li>
                  <li>
                    <label>End
                      date</label><s:text class="date_input endDate endDateBlueDart" style="width:150px"
                                          formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                          name="endDate"/>
                  </li>

                  <li>
                    <s:submit class="verifyDateBlueDart" name="updateBlueDartStatus" value="Update Delivery Status"/>

                  </li>
                </ul>
              </fieldset>
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
        $('.verifyDateChhotu').click(function() {
          startDate = $('.startDateChhotu').val();
          endDate = $('.endDateChhotu').val();
          return _checkDateValidity();
        });
        $('.verifyDateAFL').click(function() {
          startDate = $('.startDateAFL').val();
          endDate = $('.endDateAFL').val();
          return _checkDateValidity();
        });
        $('.verifyDateDelhivery').click(function() {
          startDate = $('.startDateDelhivery').val();
          endDate = $('.endDateDelhivery').val();
          return _checkDateValidity();
        });
        $('.verifyDateBlueDart').click(function() {
          startDate = $('.startDateBlueDart').val();
          endDate = $('.endDateBlueDart').val();
          return _checkDateValidity();
        });

        $('.verifyDateDTDC').click(function() {
          startDate = $('.startDateDTDC').val();
          endDate = $('.endDateDTDC').val();
          return _checkDateValidity();
        });

        function _checkDateValidity() {
          if (startDate > endDate) {
            alert("End Date cannot be less than Start Date.");
            return false;
          } else {
            return true;
          }
        }
      });
    </script>
