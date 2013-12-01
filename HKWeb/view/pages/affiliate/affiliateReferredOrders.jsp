<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.affiliate.AffiliateInsightsAction" var="affiliateBean"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Insights</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="../myaccount-nav.jsp"/>

    <s:layout-component name="htmlHead">
      <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
      <link href="<hk:vhostCss/>/css/new.css?v=1.1" rel="stylesheet" type="text/css"/>
      <%--<link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet" type="text/css"/>--%>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
      <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div>
      <h4 class="strikeline">Orders Count:- ${affiliateBean.affiliateReferredOrdersCount}</h4>
      <s:form beanclass="com.hk.web.action.core.affiliate.AffiliateInsightsAction">
        <s:errors/>

        <div style="margin-top:15px;"></div>

        <fieldset class="traffic">
          <legend>GENERATE INSIGHTS</legend>

          <div style="margin-top:15px;"></div>

          <div class="row">
            <s:label class="rowLabel" name="Start Date"/>
            <div class="rowText">
              <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"
                      style="width:150px;"/>
            </div>
          </div>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>

          <div class="row">
            <s:label class="rowLabel" name="End Date"/>
            <div class="rowText">
              <s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"
                      style="width:150px;"/>
            </div>
          </div>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>

          <div class="row">
            <s:submit name="getReferredOrderDetails" value="Generate Insights" style="font-size:0.8em;"/>
          </div>

          <div class="clear"></div>
          <div style="margin-top:15px;"></div>
        </fieldset>

        <div class="clear"></div>
        <div style="margin-top:15px;"></div>

        <fieldset class="traffic">
          <legend>REFERRED ORDERS' DETAILS</legend>

          <div style="margin-top:15px;"></div>

          <div class="row">
            <c:if test="${!empty affiliateBean.affiliateTxnOrdersList}">
              <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
              <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>

              <div style="margin-top:10px;"></div>

              <table class="cont footer_color" style="font-size: 14px;">
                <tr>
                  <th>Time Stamp</th>
                  <th>Order Amount</th>
                  <th>Amount Credited</th>
                  <th>Order Status</th>
                  <th>City</th>
                </tr>
                <c:forEach items="${affiliateBean.affiliateTxnOrdersList}" var="txn">
                  <tr>
                    <td><fmt:formatDate value="${txn.date}" type="both"/></td>

                    <td style=" padding: 10px;">
                        ${txn.order.amount}
                      <s:link beanclass="com.hk.web.action.core.referral.ReferredOrderDetailsAction">
                        <s:param name="order" value="${txn.order.id}"/>
                        [Break-Up]
                      </s:link>
                    </td>

                    <td>
                      <c:if test="${txn.affiliateTxnType.id ne 10}">
                        <div class='prices' style="font-size: 12px;margin-left:10px;margin-bottom:5px">
                          <div class='cut' style="font-size: 12px;">
                            <span class='num' style="font-size: 12px;">
                              Rs <fmt:formatNumber value="${txn.amount}" maxFractionDigits="0"/>
                            </span>
                          </div>
                        </div>
                      </c:if>
                      <c:if test="${txn.affiliateTxnType.id eq 10}">
                        <div class='prices' style="font-size: 12px;margin-left:10px;margin-bottom:5px">
                          <div class='hk' style="font-size: 12px;margin-top:0px">
                            <span class='num' style="font-size: 12px;;text-decoration:none">
                              Rs <fmt:formatNumber value="${txn.amount}" maxFractionDigits="0"/>
                            </span>
                          </div>
                        </div>
                      </c:if>
                    </td>

                    <td>${txn.affiliateTxnType.name}</td>

                    <td>${txn.order.address.city}</td>
                  </tr>
                </c:forEach>
              </table>

              <div style="margin-top:10px;"></div>

              <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${affiliateBean}"/>
              <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${affiliateBean}"/>
            </c:if>

            <c:if test="${empty affiliateBean.affiliateTxnOrdersList}">
              <s:label name="No Orders Placed Yet"/>
            </c:if>

            <div class="clear"></div>
            <div style="margin-top:15px;"></div>

          </div>
        </fieldset>
        <s:hidden name="affiliate" value="${affiliateBean.affiliate}"/>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById('myReferredOrders').style.fontWeight = "bold";
  };
</script>
<style type="text/css">
  table.cont {
    width: 600px;
    border: 1px solid #B0B0B0;
    border-collapse:separate;
    table-layout: fixed;
    word-wrap: break-word;
    overflow: hidden;
  }

  table.cont tbody {
    margin: 0;
    padding: 0;
    border: 0;
    outline: 0;
    font-size: 100%;
    vertical-align: baseline;
    background: transparent;
  }

  table.cont th {
    /*background: #f0f0f0;*/
    /*border: 1px solid #B0B0B0;*/
    color: #444;
    font-size: 16px;
    padding: 3px 10px;
    text-align: center;
    background-color:lavender;
  }

  table.cont td {
    padding: 3px 10px;
    text-align:center;
    font-size:0.9em;
  }

  table tr:nth-child(odd){
  background: #F2F2F2;
  }

  /*table.cont {*/
    /*width: 600px;*/
    /*margin-bottom: 10px;*/
    /*margin-top: 5px;*/
    /*border: 1px solid;*/
    /*border-collapse: separate;*/
    /*table-layout: fixed;*/
    /*word-wrap: break-word;*/
    /*overflow: hidden;*/
  /*}*/

  /*table.cont th {*/
    /*background: #f0f0f0;*/
    /*padding: 5px;*/
    /*text-align: center;*/
  /*}*/

  /*table.cont td {*/
    /*padding: 5px;*/
    /*text-align: center;*/
    /*font-size: 0.9em;*/
  /*}*/

  .row {
    margin-top: 0;
    float: left;
    margin-left: 0;
    padding-top: 2px;
    padding-left: 26px;
  }

  .rowLabel {
    float: left;
    padding-right: 5px;
    padding-left: 5px;
    width: 100px;
    height: 24px;
    margin-top: 5px;
  }

  .rowText {
    float: left;
    border-width: 0;
    padding-top: 0;
    padding-bottom: 0;
    margin-left: 30px;
    font: inherit;
  }

  fieldset.traffic {
    background: #fafaee;
    border-radius: 5px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    margin: 1em;
  }

  fieldset.traffic legend {
    display: block;
    color: #f87500;
    background: white;
    padding: 0 0.5em;
  }
</style>