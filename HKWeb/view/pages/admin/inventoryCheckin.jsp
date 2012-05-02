<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.InventoryCheckinAction" var="ica"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Inventory Checkin">
  <s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
  </s:layout-component>
  <s:layout-component name="heading">Inventory Checkin</s:layout-component>
  <s:layout-component name="content">
    <div style="display:inline;float:left;">
      <h2>Item Checkin against GRN#${ica.grn.id}</h2>
      <s:form beanclass="com.hk.web.action.admin.InventoryCheckinAction">
        <s:hidden name="grn" value="${ica.grn.id}"/>
        <table border="1">
          <tr>
            <td>UPC(Barcode) or VariantID:</td>
            <td><s:text name="upc" class="variant"/></td>
          </tr>
          <tr>
            <td>Qty:</td>
            <td><s:text name="qty" value="0"/></td>
          </tr>
          <tr>
            <td>Batch Number:</td>
            <td><s:text name="batch"/></td>
          </tr>
          <tr>
            <td>Mfg. Date:</td>
            <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="mfgDate"/></td>
          </tr>
          <tr>
            <td>Expiry Date:</td>
            <td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="expiryDate"/></td>
          </tr>
          <tr>
            <td>Invoice Number:</td>
            <td><s:text name="invoiceNumber" value="${ica.grn.invoiceNumber}" readonly="readonly"/></td>
          </tr>
          <tr>
            <td>Invoice Date:</td>
            <td>
              <s:text class="date_input" formatPattern="yyyy-MM-dd" name="invoiceDate" value="${ica.grn.invoiceDate}" readonly="readonly"/></td>
          </tr>
        </table>
        <script language=javascript type=text/javascript>
          $('#courierTrackingId').focus();

          function stopRKey(evt) {
            var evt = (evt) ? evt : ((event) ? event : null);
            var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
            if ((evt.keyCode == 13) && (node.type == "text")) {return false;}
          }
          document.onkeypress = stopRKey;

        </script>
        <br/>
        <s:submit name="save" value="Save"/>
      </s:form>
      <span style="display:inline;float:right;"><h2><s:link beanclass="com.hk.web.action.admin.GRNAction">&lang;&lang;&lang;
        Back to GRN List</s:link></h2></span>
    </div>
    <div style="display:inline;" align="center">

      <table style="font-size: .8em;">
        <tr>
          <th width="">S.No.</th>
          <th width="">Item</th>
          <th width="">VariantId</th>
          <th width="">UPC</th>
          <th width="">Qty</th>
          <th width="">Checked-in Qty</th>
        </tr>
        <c:forEach items="${ica.grn.grnLineItems}" var="grnLineItem" varStatus="ctr">
          <c:set value="${grnLineItem.sku.productVariant}" var="productVariant"/>
          <tr>
            <td>${ctr.index+1}</td>
            <td>
                ${productVariant.product.name}<br/>
              <em><c:forEach items="${productVariant.productOptions}" var="productOption">
                ${productOption.name} ${productOption.value}
              </c:forEach></em>
            </td>
            <td><a href="#" onclick="$('.variant').val(this.innerHTML);">${productVariant.id}</a></td>
            <td>${productVariant.upc}</td>
            <td>${grnLineItem.qty}</td>
            <td style="color:green; font-weight:bold">${grnLineItem.checkedInQty}</td>
          </tr>
        </c:forEach>
      </table>
      <%--<hr/>
      <div style="display:inline;float:right; width:450px">
        <s:form beanclass="com.hk.web.action.admin.InventoryCheckinAction">
          <s:hidden name="grn" value="${actionBean.grn.id}"/>
          <s:submit name="generateGRNExcel" value="Download GRN"/>
        </s:form>
      </div>

      <div style="display:inline;float:right; width:450px">
        <fieldset>
          <legend>Upload Excel to Checkin</legend>
          <br/>
          <s:form beanclass="com.hk.web.action.admin.InventoryCheckinAction">
            <h2>File to Upload: <s:file name="fileBean" size="30"/></h2>
            <s:hidden name="grn" value="${actionBean.grn.id}"/>

            <div class="buttons">
              <s:submit name="parse" value="Checkin"/>
            </div>
          </s:form>
        </fieldset>
      </div>--%>

    </div>
  </s:layout-component>
</s:layout-render>