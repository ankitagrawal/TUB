<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction" var="hkdBean"/>
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

    <s:layout-component name="content">
        <div class="hkDeliveryWorksheetBox">
            <s:form beanclass="com.hk.web.action.admin.hkDelivery.HKDRunsheetAction">
                <fieldset class="right_label">
                    <legend>Download Healthkart Delivery Worksheet</legend>
                    <ul>
                        <li>
                            <label style="font-size:medium;">Hub :</label>
                            <s:select name="hub" class="hubName">
                                <s:option value="-Select Hub-">-Select Hub-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="hubList" value="id"
                                                           label="name"/>
                            </s:select>
                        </li>
                        <li>
                            <label style="font-size:medium;">Assigned to:</label>
                            <s:select name="agent" class="agentName">
                                <s:option value="-Select Agent-">-Select Agent-</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                                           serviceProperty="HKDeliveryAgentList" value="id"
                                                           label="name"/>
                            </s:select>
                        </li>
                        <br>
                        <li>
                            <table border="1">
                                <thead>
                                <tr>
                                    <th style="width:200px;font-size:medium;">&nbsp;&nbsp;&nbsp;&nbsp;AWB Number</th>
                                </tr>
                                </thead>

                                <tbody id="awbTable">
                                <tr count="0" class="lastRow lineItemRow" id="awbTableTr0">
                                    <td>
                                        <input type="text" id="trackingIdList0" name="trackingIdList[0]"/>
                                    </td>
                                </tr>
                                <c:forEach var="trackingIdList" items="${hkdBean.trackingIdList}" varStatus="ctr">
                                    <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
                                        <td>
                                                ${trackingIdList}
                                        </td>
                                    </tr>

                                </c:forEach>
                                </tbody>
                            </table>

                        </li>
                        <li>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="hkDeliveryWorksheet.jsp#" class="removeRowButton"
                               style="font-size:1.2em;color:blue;">Remove row</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="hkDeliveryWorksheet.jsp#" class="reloadLink"
                                                           style="font-size:1.2em;color:blue;">Reload</a>


                        </li>
                        <li>
                            <s:submit id="previewButton" name="previewRunsheet">
                                <s:param name="runsheetPreview" value="true"/>
                                Preview Runsheet
                            </s:submit>
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
    var assignedTo =$('.agentName').val();
       //alert(hub);
    if (hub == "-Select Hub-") {
      alert("Please select a Hub.");
      return false;
    }
       if (assignedTo == "-Select Agent-") {
           alert("Please select an Agent.");
           return false;
       }
  });
   $(document).ready(function () {
       $('.popup').popupWindow({
				height:600,
				width:900
			});
   });

</script>


