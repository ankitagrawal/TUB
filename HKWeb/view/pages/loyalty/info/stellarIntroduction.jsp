<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<stripes:layout-render name="/pages/loyalty/info/layoutStatic.jsp">
	<stripes:layout-component name="contents">

<script type="text/javascript">
$(document).ready(function(){
	$('#button1').click(function() {
	  $('#message1').slideUp('slow', function() {
	  });
	  $('#message2').show();
	});
	$('#button2').click(function() {
	  $('#message2').slideUp('slow', function() {
	  });
	  $('#message3').show();

	});
	
	});
</script>

		<div class="container_16 clearfix">
			<div
				style="width: 100%; font-size: 38px; padding-bottom: 15px; margin-left: 100px;"
				class="embedMargin119">Welcome to healthkart stellar !!</div>
			<div class="topText" id="message1"
				style="float: left; margin-left: 100px; width: 680px;">
				Hi!
				Shop with us, get rewarded with points that you can later redeem
				here. <br> What more, you can convert these points into store
				credits and use them <br> to buy things off healthkart.com <br>
				<br>
				<br>
				
				<div class="addToCompare font-caps embedMargin5" id="button1">Continue</div>
			</div>
			
			<div class="topText" id="message2"
				style="float: left; margin-left: 100px; width: 680px; display:none;">
				So how does this work? <br>Once you have registered on healthkart.com ,
				<br>all you have to do is register on stellar. The more you spend henceforth, the more <br> 
				points you gather. To understand our slab structure better, may we suggest <br>
				you take a look at our <s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyIntroductionAction" event="aboutLoyaltyProgram" class="blue makeCursor" target="_blank">about us</s:link> page
				<br>There’s more. Our catalog on stellar is tailor-made to the way you’ve always<br>shopped with us <br>
				<br>
				<br>
<!-- 				<div class="addToCompare font-caps embedMargin5" id="button2">Continue</div>
			</div>
			
			<div class="topText" id="message3"
				style="float: left; margin-left: 100px; width: 680px; display:none;">
				And there is the redemption of points on healthkart.com <br>
				If you find something not on our catalog and on these two, that you’d like to buy<br>
				<br>you can convert your loyalty points to store credits.<br>1 loyalty points gets converted to 1 store credits. Once you do that, feel free
				<br>to buy straight from our store.<br>
				<br>
				<br>
 -->
 				<a href="${pageContext.request.contextPath}/loyaltypg"><div class="addToCompare font-caps embedMargin5" id="button3">Continue</div></a>
			</div>
			
		</div>
	</stripes:layout-component>
</stripes:layout-render>
	