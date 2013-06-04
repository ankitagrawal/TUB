<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>

<s:layout-definition>

  <div class='footer'>
    <div class='contents'>
      <div class='column'>
        <h5>
          HEALTHKART
        </h5>
        <ul>
          <li><s:link href="/"><span class="txt-white">Home</span></s:link></li>
          <li>
            <s:link href="/pages/aboutCompany.jsp"><span class="txt-white">About Us</span></s:link>
          </li>
          
          <li><s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyIntroductionAction" target="_blank"><span class="txt-white">Stellar</span></s:link></li>
          <li><s:link beanclass="com.hk.web.action.core.affiliate.AffiliateAction"><span class="txt-white">Affiliate</span></s:link></li>
          <%--<li><a href="${pageContext.request.contextPath}/b2b"><span class="txt-white">For Business</span></a></li>--%>
          <li><s:link beanclass="com.hk.web.action.pages.ContactAction"><span class="txt-white">Contact Us</span></s:link>
          </li>
          <li><s:link href="/pages/termsAndConditions.jsp"><span class="txt-white">Terms & Conditions</span></s:link>
          </li>
          <li><s:link href="/pages/returnAndCancellations.jsp"><span class="txt-white">Return and Cancellations</span></s:link>
          </li>
          <li><s:link href="/blog" target="_blank"><span class="txt-white">Blog</span></s:link></li>
          <li><s:link href="/pages/careers.jsp"><span class="txt-white">Careers</span></s:link>
          </li>

        </ul>
      </div>
      <div class='column'>
        <h5>
          Categories
        </h5>
        <s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>
        <ul>
          <c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">
            <c:if test="${topMenuNode.name != 'Baby'}">
            <li><s:link href="${topMenuNode.url}">${topMenuNode.name}</s:link></li>
            </c:if>
          </c:forEach>           
        </ul>
      </div>
      <div class='column'>
        <h5>
          Your Account
        </h5>
        <ul>
          <shiro:hasRole name="<%=RoleConstants.TEMP_USER%>">
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Login</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Signup</s:link></li>
          </shiro:hasRole>
          <shiro:notAuthenticated>
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Login</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="toplinksSecondary">Signup</s:link></li>
          </shiro:notAuthenticated>
          <shiro:authenticated>
              <li><s:link beanclass="com.hk.web.action.core.auth.LogoutAction" class="toplinksSecondary">Logout</s:link></li>
          </shiro:authenticated>
          <shiro:lacksRole name="<%=RoleConstants.TEMP_USER%>">
            <li>
            <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" title='view past orders / edit personal details'>Your Account</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.user.CustomerOrderHistoryAction">Order History</s:link></li>
            <li><s:link beanclass="com.hk.web.action.core.user.CustomerSubscriptionHistoryAction">Subscription History</s:link> </li>
            <%--<li><s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction">Referral Program</s:link></li>--%>
          </shiro:lacksRole>
        </ul>
      </div>
        <div class='column'>
            <h5>
                Misc
            </h5>
          <ul>
            <li><s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction"
                        event="pre">Subscriptions </s:link></li>
            <li>
              <s:link beanclass="com.hk.web.action.core.catalog.SuperSaversAction"><span class="txt-white">Super Savers</span></s:link>
            </li>
          </ul>
        </div>
      <div style="float:right;">
        <img src="<hk:vhostImage/>/images/banners/home/safe_secure_footer.gif" alt="safe and secure shopping at healthkart">
      </div>
      <script type="text/javascript">
        $("#contactBox").click(function() {
          document.location.href = '${pageContext.request.contextPath}/contact';
        });
      </script>
      <div class='floatfix'></div>
    </div>
    <div class='floatfix'></div>
    <div class="footer-bottom" style=" padding: 1 0;">
      <div class="fb-contents">
        <span class="serif"> &copy; 2013 healthkart.com</span>

        <a rel="nofollow" href="http://www.facebook.com/healthkart" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/facebook.png"></a>
        <a rel="nofollow" href="http://www.twitter.com/healthkart" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/twitter.png" alt="HealthKart Twitter">
        </a>
          <a href="https://plus.google.com/116027214366934328880?prsrc=3" rel="publisher" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/googleplus.png" alt="HealthKart Google Plus">
        </a>
        <s:link href="/blog" target="_blank" style="border-bottom:none">
          <img src="<hk:vhostImage/>/images/banners/home/Blog.png" alt="HealthKart Blog"></s:link>

        <div class='floatfix'></div>
      </div>
    </div>
  </div>
  <div id="bulkOrderModal" class="bulkOrderModal"></div>
  <div class='floatfix'></div>

  <script type="text/javascript">
    $('#bulkOrderModal').jqm({trigger: '.bulkOrder'});
    $("#bulkOrderModal").append($('<iframe id="raj_frame" class="bulkModalFrame" src="https://docs.google.com/a/healthkart.com/spreadsheet/viewform?formkey=dDdiX1pReU8zSXk4Y1pqcVMxQU43bnc6MQ#gid=0" width="760" height="760" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>'));
    $(".bulkOrder").click(function(){
      $('html, body').animate({scrollTop: $("#bulkOrderModal").offset().top - 50}, 1000);
    });
  </script>
  <%--<script type="text/javascript">

	  // Set the number of snowflakes (more than 30 - 40 not recommended)
	  var snowmax = 35

	  // Set the colors for the snow. Add as many colors as you like
	  var snowcolor = new Array("#178296", "#23527f", "#666666", "#e5e5e5", "#deeaf6")

	  // Set the fonts, that create the snowflakes. Add as many fonts as you like
	  var snowtype = new Array("Times", "Arial", "Times", "Verdana")

	  // Set the letter that creates your snowflake (recommended: * )
	  var snowletter = "*"

	  // Set the speed of sinking (recommended values range from 0.3 to 2)
	  var sinkspeed = 1.0

	  // Set the maximum-size of your snowflakes
	  var snowmaxsize = 50

	  // Set the minimal-size of your snowflakes
	  var snowminsize = 8

	  // Set the snowing-zone
	  // Set 1 for all-over-snowing, set 2 for left-side-snowing
	  // Set 3 for center-snowing, set 4 for right-side-snowing
	  var snowingzone = 1

	  ///////////////////////////////////////////////////////////////////////////
	  // CONFIGURATION ENDS HERE
	  ///////////////////////////////////////////////////////////////////////////


	  // Do not edit below this line
	  var snow = new Array()
	  var marginbottom
	  var marginright
	  var timer
	  var i_snow = 0
	  var x_mv = new Array();
	  var crds = new Array();
	  var lftrght = new Array();
	  var browserinfos = navigator.userAgent
	  var ie5 = document.all && document.getElementById && !browserinfos.match(/Opera/)
	  var ns6 = document.getElementById && !document.all
	  var opera = browserinfos.match(/Opera/)
	  var browserok = ie5 || ns6 || opera

	  function randommaker(range) {
		  rand = Math.floor(range * Math.random())
		  return rand
	  }

	  function initsnow() {
		  if (ie5 || opera) {
			  marginbottom = document.body.scrollHeight
			  marginright = document.body.clientWidth - 15
		  }
		  else if (ns6) {
			  marginbottom = document.body.scrollHeight
			  marginright = window.innerWidth - 15
		  }
		  var snowsizerange = snowmaxsize - snowminsize
		  for (i = 0; i <= snowmax; i++) {
			  crds[i] = 0;
			  lftrght[i] = Math.random() * 15;
			  x_mv[i] = 0.03 + Math.random() / 10;
			  snow[i] = document.getElementById("s" + i)
			  snow[i].style.fontFamily = snowtype[randommaker(snowtype.length)]
			  snow[i].size = randommaker(snowsizerange) + snowminsize
			  snow[i].style.fontSize = snow[i].size + 'px';
			  snow[i].style.color = snowcolor[randommaker(snowcolor.length)]
			  snow[i].style.zIndex = 1000
			  snow[i].sink = sinkspeed * snow[i].size / 5
			  if (snowingzone == 1) {
				  snow[i].posx = randommaker(marginright - snow[i].size)
			  }
			  if (snowingzone == 2) {
				  snow[i].posx = randommaker(marginright / 2 - snow[i].size)
			  }
			  if (snowingzone == 3) {
				  snow[i].posx = randommaker(marginright / 2 - snow[i].size) + marginright / 4
			  }
			  if (snowingzone == 4) {
				  snow[i].posx = randommaker(marginright / 2 - snow[i].size) + marginright / 2
			  }
			  snow[i].posy = randommaker(2 * marginbottom - marginbottom - 2 * snow[i].size)
			  snow[i].style.left = snow[i].posx + 'px';
			  snow[i].style.top = snow[i].posy + 'px';
		  }
		  movesnow()
	  }

	  function movesnow() {
		  for (i = 0; i <= snowmax; i++) {
			  crds[i] += x_mv[i];
			  snow[i].posy += snow[i].sink
			  snow[i].style.left = snow[i].posx + lftrght[i] * Math.sin(crds[i]) + 'px';
			  snow[i].style.top = snow[i].posy + 'px';

			  if (snow[i].posy >= marginbottom - 2 * snow[i].size || parseInt(snow[i].style.left) > (marginright - 3 * lftrght[i])) {
				  if (snowingzone == 1) {
					  snow[i].posx = randommaker(marginright - snow[i].size)
				  }
				  if (snowingzone == 2) {
					  snow[i].posx = randommaker(marginright / 2 - snow[i].size)
				  }
				  if (snowingzone == 3) {
					  snow[i].posx = randommaker(marginright / 2 - snow[i].size) + marginright / 4
				  }
				  if (snowingzone == 4) {
					  snow[i].posx = randommaker(marginright / 2 - snow[i].size) + marginright / 2
				  }
				  snow[i].posy = 0
			  }
		  }
		  var timer = setTimeout("movesnow()", 50)
	  }

	  for (i = 0; i <= snowmax; i++) {
		  document.write("<span id='s" + i + "' style='position:absolute;top:-" + snowmaxsize + "'>" + snowletter + "</span>")
	  }
	  if (browserok) {
		  window.onload = initsnow
	  }

  </script>--%>

</s:layout-definition>