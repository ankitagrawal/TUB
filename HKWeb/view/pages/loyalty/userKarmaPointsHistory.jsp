<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@include file="/loyalty/LoyaltyJunk/css/style2.css" %>
<%@include file="/loyalty/LoyaltyJunk/css/jquery.jscrollpane.css" %>
<%@include file="/loyalty/LoyaltyJunk/css/grid.css" %>
<%@include file="/loyalty/LoyaltyJunk/css/accordian_navs.css" %>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.UserKarmaProfileHistoryAction" var="userKarmaHA"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">History</s:layout-component>
  
  <s:layout-component name="rhsContent">

    <div class="main-inn-right">

      <div class="round-cont">
        <c:choose>
            <c:when test="${!empty userKarmaHA.karmaList}">
                <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${userKarmaHA}"/>
                <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${userKarmaHA}"/>
               
                <table class="cont footer_color">
                    <tr>
                        <th> </th>
                        <th>Order Date</th>
                        <th>Order Details</th>
                        <th>Activity</th>
                        <th>Points</th>

                    </tr>
                    <tbody>
                    <% int count = 0; %>
                    <c:forEach items="${userKarmaHA.karmaList}" var="karmaProfile">
                        <tr>
                            <td>
                               <%= ++count %>

                            </td>
                            <td>
                                <fmt:formatDate value="${karmaProfile.creationTime}" />
                            </td>
                            <td>
                                    To be added!!
                            </td>
                            <td>
                                    ${karmaProfile.transactionType}
                            </td>
                            <td>
                                    ${karmaProfile.karmaPoints}
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${userKarmaHA}"/>
                <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${userKarmaHA}"/>
                <br/><br/>
            </c:when>
            <c:otherwise>
                <br/>
                <br/>
                You haven't ordered anything from healthkart yet.   <s:link beanclass="com.hk.web.action.HomeAction" event="pre" class="buttons" >
                Continue Shopping
            </s:link>
            </c:otherwise>
        </c:choose>
      </div>
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById("ohLink").style.fontWeight = "bold";
  };
</script>
<style type="text/css">
  table {
    width: 100%;
    margin-bottom: 10px;
    margin-top: 5px;
    border: 1px solid;
    border-collapse: separate;
  }

  table th {
    background: #f0f0f0;
    padding: 5px;
    text-align: left;
  }

  table td {
    padding: 5px;
    text-align: left;
    font-size: small;
  }
</style>