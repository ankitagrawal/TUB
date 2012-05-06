<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.shipment.UpdateAFLChhotuDeliveryStatusAction" var="AFLDelivery"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	    <script type="text/javascript">
		    $(document).ready(function() {
			    $('.verifyDateChhotu').click(function() {
				    var startDateChhotu = $('.startDateChhotu').val();
				    var endDateChhotu = $('.endDateChhotu').val();

				    if (startDateChhotu > endDateChhotu) {
					    alert("End Date cannot be less than Start Date.");
					    return false;
				    }
			    });
			    $('.verifyDateAFL').click(function() {
				    var startDateAFL = $('.startDateAFL').val();
				    var endDateAFL = $('.endDateAFL').val();
				    if (startDateAFL > endDateAFL) {
					    alert("End Date cannot be less than Start Date.");
					    return false;
				    }
			    });
		    });
	    </script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

    </s:layout-component>

    <s:layout-component name="heading">Update AFL/Chhotu Order Status</s:layout-component>

    <s:layout-component name="content">
        <table>
            <tr>
                <td>
                    <div class="reportBox">
                        <s:form beanclass="com.hk.web.action.admin.shipment.UpdateAFLChhotuDeliveryStatusAction">
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
                        <s:form beanclass="com.hk.web.action.admin.shipment.UpdateAFLChhotuDeliveryStatusAction">
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
            </tr>
        </table>
    </s:layout-component>
</s:layout-render>
