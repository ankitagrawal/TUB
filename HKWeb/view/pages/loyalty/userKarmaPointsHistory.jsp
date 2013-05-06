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
          <c:set var = "userId" value ="${userKarmaHA.user.id}" />
  	    	Member since:  <fmt:formatDate value = "${userKarmaHA.badgeInfo.creationTime}" /> | Points available :  ${hk:getLoyaltyKarmaPointsForUser(userId)}
			| Current status : <c:set var="badgeInfo" value="${userKarmaHA.badgeInfo}" /> ${badgeInfo.badge.badgeName} MEMBER
          </div>
          <div class="topText"> ${userKarmaHA.upgradeString} </div>
		  
          <div class="topText">
            <span><s:link beanclass="com.hk.web.action.core.loyaltypg.UserKarmaProfileHistoryAction" 
            	event="convertPoints" class="blue"  id="rewardLink">
            Click here</s:link></span> to convert your points to Reward points!
          </div>
		 
		</div>
        <div class="grid_14 embedMarginTop40 floatRight">
          <div class="history">
            <div class="dashedLine"></div>
            <div class="historyText">History</div>
            <div class="dashedLine"></div>
          </div>
      <c:choose>
            <c:when test="${!empty userKarmaHA.karmaList}">
  
          <!-- Table starts -->
          <div class="tableHistory embedMarginTop40">

            <div class="headingRow">
              <div class="headRowEmptyValue"></div>
			  <div class="headRowValue">Order Date</div>
              <div class="headRowValue">Order Details</div>
              <div class="headRowValue">Points </div>
              <div class="headRowValue">Points Status</div>
            </div>
			
			<% int count = 0;%>
            <c:forEach items="${userKarmaHA.karmaList}" var="karmaProfile">
            
            <div class="normalRow">
              <div class="headRowEmptyValue"><%= ++count %></div>
              <div class="headRowValue"><fmt:formatDate value="${karmaProfile.creationTime}" /> 
              <p class="expiryRow"> ${karmaProfile.expiryDate}
              </p></div>
              <div class="headRowValue">
				<c:forEach items="${karmaProfile.order.cartLineItems}" var="items">
   					${items.productVariant.product.name} 
                </c:forEach>
			  </div>
              <div class="headRowValue">
              <c:choose>
                    <c:when test="${karmaProfile.karmaPoints >= 0.0}">
                        ${karmaProfile.karmaPoints}
                    </c:when>
                    <c:otherwise>
                       ${0-karmaProfile.karmaPoints}
                    </c:otherwise>
                </c:choose>
              
              </div>
              <div class="headRowValue">${karmaProfile.statusForHistory}</div>
            </div>
           </c:forEach>
            <div class="headingRow"></div>

          </div>
          <!-- Table Ends -->
       <%--    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${userKarmaHA}"/> --%>
            <s:layout-render name="/pages/loyalty/pagination.jsp" paginatedBean="${userKarmaHA}"/>
                <br/><br/>
            </c:when>
       <c:otherwise>
                <br/>
                <br/>
                You haven't ordered anything from healthkart yet as a stellar member.   <s:link beanclass="com.hk.web.action.HomeAction" event="pre" class="buttons blue" >
                Continue Shopping
            </s:link>
            </c:otherwise>
        </c:choose>
  
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
    var rewardPoints = ${userKarmaHA.pointsConverted};
    
    if (rewardPoints > 0) {
    	alert("Congratulations you have been awarded " + rewardPoints + " Reward points.");
    } else if (rewardPoints === 0) {
    	alert("Unfortunaetly you don't have sufficient loyaty points to be converted into reward points at this point of time.");
    }
    
    
    //Pagination click event
    $(".pagi_link").click(function(){
      if($(this).hasClass("grayedButton")){
        $(this).removeClass("grayedButton");
      }
      else{
        $(this).parent().find("li").removeClass("grayedButton");
        $(this).addClass("grayedButton");
      }
    });
    
    // Reward points conversion
  	$("#rewardLink").click(function(){
  		var convert = false;
  		if (confirm("If you click OK then all your loyalty points will be converted to maximum possible reward points. Do you want to covert your loyalty points ?")) {
  			convert = true;
  		}
  		return convert;
  	});  

  };
  
 
</script>
`