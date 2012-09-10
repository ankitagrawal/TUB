<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction" var="hkdBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Healthkart Delivery">

    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            $(document).ready(function() {


                function stopRKey(evt) {
                    var evt = (evt) ? evt : ((event) ? event : null);
                    var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);

                    /*Checking if key pressed is enter then add another row in awbTable body.*/
                    if ((evt.keyCode == 13) && (node.type == "text")) {

                        /*function to add new row.*/
                        /*getting last row index using its attribute count*/
                        var lastIndex = $('.lastRow').attr('count');
                        if (!lastIndex) {
                            lastIndex = -1;
                        }
                        /* removing the 'class' attribute of <tr> .Earlier it was 'lastRow lineItemRow',now removing 'lastRow' from it,so that
                         * its no more the last one.*/
                        $('.lastRow').removeClass('lastRow');

                        var nextIndex = eval(lastIndex + "+1");

                        var newRowHtml =
                                '<tr count="' + nextIndex + '" class="lastRow lineItemRow" id="awbTableTr' + nextIndex + '">' +
                                '  <td>' +
                                '    <input type="text" id="trackingIdList' + nextIndex + '" name="trackingIdList[' + nextIndex + ']"/>' +
                                '  </td>' +
                                '</tr>';

                        /*appending the new row*/
                        $('#awbTable').append(newRowHtml);
                        var elmId = '#trackingIdList' + nextIndex;
                        $(elmId).focus();
                        return false;


                    }
                }

                /*calling stopRKey on pressing any key.*/
                document.onkeypress = stopRKey;


                /*setting focus on first Awb Number text onbodyload*/
                $('#trackingIdList0').focus();

                /* reloading page on submitting it*/
                $('.reloadLink').click(function() {
                    location.reload();
                    return false;

                });


                /*function to delete last row.*/
                $('.removeRowButton').click(function() {

                    /*Fetching last row index.*/
                    var lastIndex = $('.lastRow').attr('count');

                    /*checking if index is 0 then don't delete the row,else delete it.*/
                    if (lastIndex == 0) {
                        return false;
                    } else {
                        $('#awbTable tr:last').remove();
                        /*After deleting the row,changing the class attribute from 'lineItemRow' to 'lastRow lineItemRow' to make it the last row. */
                        var previousIndex = eval(lastIndex + "-1");
                        var previousRow = "#awbTableTr" + previousIndex;
                        $(previousRow).removeClass("lineItemRow").addClass("lastRow lineItemRow");
                    }
                    return false;
                });

            });

        </script>

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

    <s:layout-component name="heading">
        Add/Edit Hub
    </s:layout-component>

    <s:layout-component name="content">
        <div class="hkDeliveryBox">
            <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction">


                <fieldset>
                    <legend>Add New Hub</legend>
                    <s:submit name="addNewHub" value="Add a New Hub">
                        <s:param name="addHub" value="false"></s:param>
                        Add a New Hub
                    </s:submit>
                </fieldset>

                <fieldset class="right_label">
                    <legend>Edit Hub</legend>
                    <ul>
                        <br>
                        <li>
                            <table border="1">
                                <thead>
                                <tr>
                                    <th style="width:200px;font-size:medium;">Id</th>
                                    <th style="width:200px;font-size:medium;">Name</th>
                                    <th style="width:200px;font-size:medium;">Address</th>
                                    <th style="width:200px;font-size:medium;">City</th>
                                    <th style="width:200px;font-size:medium;">State</th>
                                    <th style="width:200px;font-size:medium;">Country</th>
                                    <th style="width:200px;font-size:medium;">Pincode</th>
                                    <th style="width:200px;font-size:medium;">Edit/Save</th>
                                </tr>
                                </thead>

                                <tbody id="awbTable">
                                <c:forEach var="hub" items="${hkdBean.hubList}" varStatus="ctr">
                                    <tr >
                                        <td>${hub.id}</td>
                                        <td>${hub.name}</td>
                                        <td>${hub.address}</td>
                                        <td>${hub.pincode.city.name}</td>
                                        <td>${hub.pincode.state.name}</td>
                                        <td>${hub.country}</td>
                                        <td>${hub.pincode.pincode}</td>
                                        <td><s:link
                                                beanclass="com.hk.web.action.admin.hkDelivery.HKDHubAction"
                                                event="editHub">
                                            <s:param name="editExistingHub" value="false"/>
                                            <s:param name="hub" value="${hub.id}"/>
                                            <b>Edit & Save</b> </s:link>
                                        </td>

                                    </tr>

                                </c:forEach>
                                </tbody>
                            </table>

                        </li>
                    </ul>
                </fieldset>
            </s:form>
        </div>

    </s:layout-component>
</s:layout-render>
<script type="text/javascript">
    $('.verifyData').click(function() {
        //alert("in validator");
        var hub = $('.hubName').val();
        //alert(hub);
        if (hub == "-Select Hub-") {
            alert("Please select a Hub.");
            return false;
        }
    });
</script>



