<%@ page import="com.hk.constants.discount.OfferConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.discount.AvailabeOfferListAction" event="pre" var="offerBean"/>
<c:set var="infiniteQty" value="<%=OfferConstants.INFINITE_QTY%>"/>

<s:layout-render name="/layouts/modal.jsp" pageTitle="Available offers">

  <s:layout-component name="heading">Available offers</s:layout-component>

  <s:layout-component name="content">

    <c:choose>
      <c:when test="${fn:length(offerBean.offerInstanceList) > 0}">
        <s:form beanclass="com.hk.web.action.core.discount.AvailabeOfferListAction" method="post" id="offerForm">
          <p class="lrg gry em">Please select the offer to be applied. Offers cannot be coupled.</p>
          <table class="cont_2 offerTable">
            <thead>
            <tr>
              <th width="20">&nbsp;</th>
              <th>Description</th>
              <th>Valid till</th>
              <%--<th>Quantity</th>--%>
              <th>Coupon</th>
            </tr>
            </thead>
            <c:forEach items="${offerBean.offerInstanceList}" var="offerInstance" varStatus="offerCount">
              <tr>
                <s:hidden name="offerInstanceList[${offerCount.index}]" value="${offerInstance.id}"/>
                <td align="center">
                  <s:radio value="${offerInstance.id}" name="selectedOffer"/></td>
                <td width="200">
                  ${offerInstance.offer.description}
                  <c:if test="${hk:isNotBlank(offerInstance.offer.terms)}">
                  <br/>
                    <h3>Terms:</h3>
                    ${hk:convertNewLineToBr(offerInstance.offer.terms)}
                  </c:if>
                </td>
                <td width="150"><fmt:formatDate value="${offerInstance.endDate}"/></td>
                <%--<td>--%>
                  <%--<span class="upc gry sml">${offerInstance.offer.offerAction.qty == infiniteQty || offerInstance.offer.offerAction.qty == null ? "No Limit" : offerInstance.offer.offerAction.qty}</span>--%>
                <%--</td>--%>
                <td width="100">

                  <span class="upc gry sml">${offerInstance.coupon.code != null ? offerInstance.coupon.code : "No Coupon Code"}</span>

                </td>
              </tr>
            </c:forEach>
          </table>
          <table class="btns" class="blocked">
            <tr>
              <td>
                <div class="buttons">
                  <s:submit name="applyOffer" value="Apply Offer"/>
                </div>
              </td>
              <td>
                <a href="#" onclick="return false;" class="jqmClose">
                  or, Cancel
                </a>
              </td>
              <td>
                <div class="buttons">
                  <s:submit name="removeAppliedOffer" value="Remove Applied Offer"/>
                </div>
              </td>
            </tr>
          </table>
          <p class="offerMessage alert"></p>

        </s:form>

      </c:when>

      <c:otherwise>
        <p class="no_data">You do not have any offers in your account.</p>
      </c:otherwise>
    </c:choose>

    <script type="text/javascript">
      $('#offerForm').ajaxForm({dataType: 'json', success: function(response) {
        if (response.code == '<%=HealthkartResponse.STATUS_OK%>') {
          $('.offerMessage').html('Applying offer..');
          $.getJSON($('#updatePricingLink').attr('href'), function(res) {
            _updateTotals(res);
            $('.offerMessage').html('Offer applied!').parents('.jqmWindow').jqmHide();
          });
        } else {
          alert(response);
        }
      }});

      function highlightSelectedOffer() {
        $('#offerForm :checked').parents('tr').addClass('highlight').siblings('tr').removeClass('highlight');
      }

      highlightSelectedOffer();

      $('#offerForm :radio').click(highlightSelectedOffer);
    </script>
  </s:layout-component>

</s:layout-render>
