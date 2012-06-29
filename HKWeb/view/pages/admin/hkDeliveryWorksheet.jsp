<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.HKDeliveryAction" var="hkdBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Healthkart Delivery">

<s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {

          /*function to add new row.*/
        $('.addRowButton').click(function() {

          var lastIndex = $('.lastRow').attr('count');
          if (!lastIndex) {
            lastIndex = -1;
          }
          $('.lastRow').removeClass('lastRow');

          var nextIndex = eval(lastIndex + "+1");

            var newRowHtml =
              '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
              '  <td>' +
              '    <input type="text"  name="trackingIdList[' + nextIndex + ']"/>' +
              '  </td>' +
              '</tr>';
          $('#awbTable').append(newRowHtml);
          $('#awbNumber').focus();


          return false;
        });

          /*function to delete last row.*/
          $('.removeRowButton').click(function() {
              $('#awbTable tr:last').remove();

                    return false;
                  });


      });


      function stopRKey(evt) {
          var evt = (evt) ? evt : ((event) ? event : null);
          var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
          if ((evt.keyCode == 13) && (node.type == "text")) {
              //$('#qty').focus();
              alert("hello");
              return false;
          }
      }

      $('#barcode').keydown(stopRKey);
      $('#barcode').keydown(function() {
          $('.error, .messages, #savedVariant').html("");
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
  <s:form beanclass="com.hk.web.action.admin.courier.HKDeliveryAction">
    <fieldset class="right_label">
      <legend>Download Healthkart Delivery Worksheet</legend>
      <ul>

          <li>
              <label style="font-size:medium;">Assigned to:</label><s:text name="assignedTo"/>
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
              <a href="hkDeliveryWorksheet.jsp#" class="addRowButton" style="font-size:1.2em;color:blue;">Add new row</a>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <a href="hkDeliveryWorksheet.jsp#" class="removeRowButton" style="font-size:1.2em;color:blue;">Remove row</a>

          </li>
        <li>
          <s:submit name="downloadDeliveryWorkSheet" value="Download Delivery Worksheet" class="verifyData"/>

        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

</s:layout-component>
</s:layout-render>
<%--
<script type="text/javascript">

    $(document).ready(function() {
        var awbNumber = "";
        $('.verifyData').click(function() {
            var lastIndex = $('.lastRow').attr('count');
                      if (!lastIndex) {
                        lastIndex = -1;
                      }
                      $('.lastRow').removeClass('lastRow');

            lastIndex=eval(lastIndex + "+1");
            alert ("no of rows"+lastIndex);
            $('#awbTable tr td').each(function() {
                awbNumber = $(this).find(".awbNumber").html();
                alert("awb" + awbNumber);
            });


        });
    });
    
</script>
--%>


