<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.UserKarmaProfileHistoryAction" var="userKarmaHA"/>
<s:layout-render name="/pages/loyalty/layout.jsp">
 
  <s:layout-component name="contents">
  <div class="mainContainer">
      <div class="container_16 clearfix">
        <div class="mainContent">
        <div class="grid_14">
          <div class="topText">
  	    	Member since:  <fmt:formatDate value = "${userKarmaHA.user.createDate}" /> | Points available : ${userKarmaHA.validPoints} 
			| Current status : <c:set var="badgeInfo" value="${userKarmaHA.badgeInfo}" /> ${badgeInfo.badge.badgeName} MEMBER
          </div>
          <div class="topText"> ${userKarmaHA.upgradeString} </div>
		  <!--
          <div class="topText">
            <a href="#" class="clickForPoints">Click here</a> to convert your points to cash!
          </div>
		  -->
		</div>
        <div class="grid_14 embedMarginTop40 floatRight">
          <div class="history">
            <div class="dashedLine"></div>
            <div class="historyText">History</div>
            <div class="dashedLine"></div>
          </div>
      <c:choose>
            <c:when test="${!empty userKarmaHA.karmaList}">
                <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${userKarmaHA}"/>
                <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${userKarmaHA}"/>
  
          <!-- Table starts -->
          <div class="tableHistory embedMarginTop40">

            <div class="headingRow">
              <div class="headRowEmptyValue"></div>
			  <div class="headRowValue">Order Date</div>
              <div class="headRowValue">Order Details</div>
              <div class="headRowValue">Points Earned</div>
              <div class="headRowValue">Points Expiry</div>
            </div>
			
			<% int count = 0;%>
            <c:forEach items="${userKarmaHA.karmaList}" var="karmaProfile">
            
            <div class="normalRow">
              <div class="headRowEmptyValue"><%= ++count %></div>
              <div class="headRowValue"><fmt:formatDate value="${karmaProfile.creationTime}" /></div>
              <div class="headRowValue">
				<c:forEach items="${karmaProfile.userOrderKey.order.cartLineItems}" var="items">
   					${items.productVariant.product.name} 
                </c:forEach>
			  </div>
              <div class="headRowValue">${karmaProfile.karmaPoints}</div>
              <div class="headRowValue">${karmaProfile.expiryDate}</div>
            </div>
           </c:forEach>
            <div class="headingRow"></div>

          </div>
          <!-- Table Ends -->
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
  
          <div class="pagination embedMarginTop20">
            <ul class="pageNums">
              <li class="pageNum">PREVIOUS</li>
              <li class="pageNum">03</li>
              <li class="pageNum">04</li>
              <li class="dots">...</li>
              <li class="pageNum">11</li>
              <li class="pageNum">12</li>
              <li class="pageNum">13</li>
              <li class="dots">...</li>
              <li class="pageNum">22</li>
              <li class="pageNum">23</li>
              <li class="pageNum">NEXT</li>
            </ul>
          </div>

        </div>

      </div>

      </div>
    </div>


  
  
  
  <!-- The page ends here -->
               
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById("ohLink").style.fontWeight = "bold";
  };
</script>
