<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.loyaltypg.UserKarmaProfileHistoryAction" var="userKarmaHA"/>
<s:layout-render name="/pages/loyalty/layout.jsp">
 
  <s:layout-component name="contents">
<script type="text/javascript" src="<hk:vhostJs/>/pages/loyalty/resources/js/bootbox.js"></script>
<link href="<hk:vhostCss/>/pages/loyalty/resources/css/bootstrap.css" rel="stylesheet">
  <div class="mainContainer">
      <div class="container_16 clearfix">
        <div class="mainContent">
        <div class="grid_14">
          <div class="topText">
          <c:set var = "userId" value ="${userKarmaHA.user.id}" />
  	    	Member since:  <fmt:formatDate value = "${userKarmaHA.badgeInfo.creationTime}" /> | Points available :  ${hk:roundNumberForDisplay(hk:getLoyaltyKarmaPointsForUser(userId))}
			| Current status : <c:set var="badgeInfo" value="${userKarmaHA.badgeInfo}" /> ${badgeInfo.badge.badgeName} MEMBER
          </div>
          <div class="topText"> ${userKarmaHA.upgradeString} </div>
		  
          <div class="topText">
            <s:form beanclass="com.hk.web.action.core.loyaltypg.UserKarmaProfileHistoryAction"  event="convertPoints" id="conversionForm">
            <s:hidden name="convertPoints" value="convertPoints"></s:hidden>
            <span class="blue makeCursor" id="rewardLink">Click here</span> to convert your points to Reward points!</s:form>
            
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
              <div class="headRowValue" style="text-align: center;">Points </div>
              <div class="headRowValue">Points Status</div>
            </div>
			<c:set var="count" value="${userkarmaHA.count}" />
		    <c:forEach items="${userKarmaHA.karmaList}" var="karmaProfile">
			<c:set var="count" value="${count+1}" />

            <div class="normalRow">
              <div class="headRowEmptyValue">${count}</div>
              <div class="headRowValue"><fmt:formatDate value="${karmaProfile.creationTime}" /> 
              <p class="expiryRow"> ${karmaProfile.expiryDate}
              </p></div>
              <div class="headRowValue">
				<c:forEach items="${karmaProfile.order.cartLineItems}" var="items">
   					${items.productVariant.product.name} 
                </c:forEach>
			  </div>
              <div class="headRowValue" style="text-align: center;">
              <c:choose>
                    <c:when test="${karmaProfile.karmaPoints >= 0.0}">
                        ${hk:roundNumberForDisplay(karmaProfile.karmaPoints)}
                    </c:when>
                    <c:otherwise>
                       ${hk:roundNumberForDisplay(0-karmaProfile.karmaPoints)}
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
$(document).ready(function(){
    var rewardPoints = ${userKarmaHA.pointsConverted};
    
    if (rewardPoints > 0) {
    	bootbox.alert("Hi! " + rewardPoints + " Loyalty Points have been successfully converted to " + rewardPoints + 
				" Reward Points. Please note these points are valid for 6 months only.");
    } else if (rewardPoints === -1) {
    	bootbox.alert("Sorry, you don’t have enough loyalty points for conversion. Why don’t you shop more and earn some points.");
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
  	$("#rewardLink").click(function(e){
  		bootbox.confirm("Please note that by clicking OK all your loyalty points will be converted to reward points." +
  				" They will be valid for next six months only. Do you wish to continue?", function(result){
  				if (result) {
  					$("#conversionForm").submit();
  			  	}
  		});
  	});  

  });
  
 
</script>
