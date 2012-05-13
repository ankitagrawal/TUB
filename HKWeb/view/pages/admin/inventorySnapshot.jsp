<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.DoomDayInventorySnapshotAction" var="ddis"/>

<s:layout-render name="/layouts/inventory.jsp" pageTitle="Search Batches for UPC/VariantID">
  <s:layout-component name="heading">
    <div align="center">
      <h1 style="font-size:80px;margin-top:50px;width:1500px;height:100px">
        CHECKIN
      </h1>
    </div>
  </s:layout-component>
  <s:layout-component name="content">
    <div style="margin-top:100px" height="500px" align="center">
      <s:form beanclass="com.hk.web.action.admin.inventory.DoomDayInventorySnapshotAction" autocomplete="off">
        <div id="savedVariant">
          <c:if test="${param['variantSaved'] == 'true'}">
            ${param['productVariantSaved']}
          </c:if>
        </div>
        <label><b>SCAN THE BAR CODE HERE</b></label>
        <br/><br/>
        <label>Barcode : </label><s:text name="barcode" id="barcode" style="font-size:40px; padding:20px;height:30px;width:500px;"/><br/><br/>
        <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Qty : </label><s:text name="qty" id="qty" style="font-size:40px; padding:20px;height:30px;width:500px;"/><br/><br/>
        <script language=javascript type=text/javascript>

          function stopRKey(evt) {
            var evt = (evt) ? evt : ((event) ? event : null);
            var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
            if ((evt.keyCode == 13) && (node.type == "text")) {$('#qty').focus();return false;}
          }

          $('#barcode').keydown(stopRKey);
          $('#barcode').keydown(function() {$('.error, .messages, #savedVariant').html("");});

          $(document).ready(function() {
            $('#barcode').focus();
            if ($('.messages').text() === "SAVED!!!") {$('.messages ul, .messages li').css('background', 'green')};
          });

        </script>
        <br/>
        <br/>
        <table>
          <c:if test="${param['showVariants'] == 'true'}">
            <c:forEach items="${ddis.productVariants}" var="productVariant">
              <tr>
                <td><s:radio name="productVariant" value="${productVariant.id}"/></td>
                <td>${productVariant.id}</td>
                <td>${productVariant.product.name}</td>
                <td>
                  ${productVariant.optionsCommaSeparated}
                </td>
                <td>Is Deleted? - ${productVariant.deleted}</td>
              </tr>
            </c:forEach>
          </c:if>
        </table>
        </div>
        <s:submit name="saveSnapshot" value="SAVE"/>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>