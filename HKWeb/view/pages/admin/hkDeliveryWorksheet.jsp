<%--
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.HKDeliveryAction" var="hkdBean"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Healthkart Delivery">

<s:layout-component name="htmlHead">

  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

</s:layout-component>

<s:layout-component name="content">
<div class="hkDeliveryWorksheetBox">
  <s:form beanclass="com.hk.web.action.admin.courier.HKDeliveryAction">
    <fieldset class="right_label">
      <legend>Download Healthkart Delivery Worksheet</legend>
      <ul>

        <li>
          <label>Enter AWB Numbers:</label><br>(Please enter comma seperated values)
            <br>
            <br>
            <s:textarea name="trackingId" class="trackingIds" cols="52"
                            style="resize:none;word-wrap:break-word"></s:textarea>

            --%>
<%--<s:text class="date_input startDate" style="width:150px"
                                formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name=""/>--%>
<%--
        </li>
        <li>
          <s:submit name="downloadDeliveryWorkSheet" value="Download Delivery Worksheet"/>

        </li>
      </ul>
    </fieldset>
  </s:form>
</div>

</s:layout-component>
</s:layout-render>

--%>

<%--Code for testing--%>

<%@ page import="com.hk.domain.inventory.rv.ReconciliationType" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.courier.HKDeliveryAction" var="hkda"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="HK Delivery Worksheet">
<jsp:useBean id="now" class="java.util.Date" scope="request" />
  <s:layout-component name="htmlHead">

 <%
	MasterDataDao masterDataDao = ServiceLocatorFactory.getService(MasterDataDao.class);
  List<ReconciliationType> reconciliationTypeList  = masterDataDao.getReconciliationTypeList();
  pageContext.setAttribute("reconciliationTypeList", reconciliationTypeList);
%>


    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
      $(document).ready(function() {
        $('.addRowButton').click(function() {

          var lastIndex = $('.lastRow').attr('count');
          if (!lastIndex) {
            lastIndex = -1;
          }
          $('.lastRow').removeClass('lastRow');

          var nextIndex = eval(lastIndex + "+1");


          var reconciliationTypeOptions = '<select class="reconciliationType valueChange" name="rvLineItems[' + nextIndex + '].reconciliationType">';
                  <c:forEach items="${reconciliationTypeList}" var="reconciliationTypeVar">
                   reconciliationTypeOptions += '<option value="'+${reconciliationTypeVar.id}+'">'+"${reconciliationTypeVar.name}"+'</option>';
                 </c:forEach>



          var newRowHtml =
              '<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
              '  <td>' +
              '    <input type="hidden" name="rvLineItems[' + nextIndex + '].id" />' +
              '    <input type="text" class="variant" name="rvLineItems[' + nextIndex + '].productVariant"/>' +
              '  </td>' +
              '  <td class="pvDetails"></td>' +
              '  <td>' +
              '    <input type="text" name="rvLineItems[' + nextIndex + '].qty" />' +
              '  </td>' +
              '   <td>' +
              	   reconciliationTypeOptions+
              '</select>'+
              '<input type="hidden" value="finance" class="reconciliationTypeIdentifier"/>'+
              '</td>' +
              '  <td>' +
              '    <input class="costPrice" type="text" name="rvLineItems[' + nextIndex + '].costPrice" />' +
              '  </td>' +
              '  <td>' +
              '    <input class="mrp" type="text" name="rvLineItems[' + nextIndex + '].mrp" />' +
              '  </td>' +
              '  <td>' +
              '    <input type="text" name="rvLineItems[' + nextIndex + '].batchNumber" />' +
              '  </td>' +
              '  <td>' +
              '    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="rvLineItems[' + nextIndex + '].mfgDate" />' +
              '  </td>' +
              '  <td>' +
              '    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="rvLineItems[' + nextIndex + '].expiryDate" />' +
              '  </td>' +
              '</tr>';

          $('#poTable').append(newRowHtml);

          return false;
        });

        $('.variant').live("change", function() {
          var variantRow = $(this).parents('.lineItemRow');
          var productVariantId = variantRow.find('.variant').val();
          var productVariantDetails = variantRow.find('.pvDetails');
          $.getJSON(
              $('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${whAction.setWarehouse.id}},
              function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                  variantRow.find('.mrp').val(res.data.variant.markedPrice);
                  variantRow.find('.costPrice').val(res.data.variant.costPrice);
                  productVariantDetails.html(
                      res.data.product + '<br/>' +
                      res.data.options
                      );
                } else {
                   $('.variantDetails').html('<h2>'+res.message+'</h2>');
                }
              }
              );
        });

      });
    </script>
  </s:layout-component>

  <s:layout-component name="content">
        <h2>HK Delivery Worksheet</h2>
    <s:form beanclass="com.hk.web.action.admin.courier.HKDeliveryAction">

      <table border="1">
        <thead>
        <tr>
          <th>AWB Number</th>
          <th>Address</th>
          <th>COD/Pre-Paid Amount</th>
        </tr>
        </thead>
        <tbody id="poTable">
        <c:forEach var="rvLineItem" items="${hkda.shippingOrderListi}" varStatus="ctr">
          <c:set var="productVariant" value="${rvLineItem.sku.productVariant}"/>
          <s:hidden name="rvLineItems[${ctr.index}]" value="${rvLineItem.id}"/>
          <tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
            <td>
                ${productVariant.id}
            </td>
            <td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
            </td>
            <td>${rvLineItem.qty}
            </td>
            <td class="reconciliationType">
              <input type="hidden" value="finance"
                     class="reconciliationTypeIdentifier"/>
              <s:select name="rvLineItems[${ctr.index}].reconciliationType"
							          value="${rvLineItem.reconciliationType.id}" class="valueChange">
								<hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="reconciliationTypeList" value="id"
								                           label="name"/>
							</s:select>
            </td>
            <td>${rvLineItem.costPrice}
            </td>
            <td>${rvLineItem.mrp}
            </td>
            <td>${rvLineItem.batchNumber}</td>
            <td>
              <fmt:formatDate value="${rvLineItem.mfgDate}" type="both"/></td>
            <td>
              <fmt:formatDate value="${rvLineItem.expiryDate}" type="both"/></td>


          </tr>

        </c:forEach>
        </tbody>
      </table>
      <div class="variantDetails info"></div>
      <br/>
      <a href="reconciliationVoucher.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

      <s:submit name="save" value="Save" class="saveButton"/>
    </s:form>
   <script type="text/javascript">
    $(document).ready(function() {
       $('.saveButton').click(function disableSaveButton(){
        $(this).css("display", "none");
      });
    });
  </script>
  </s:layout-component>


</s:layout-render>

