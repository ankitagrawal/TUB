<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Date" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.cart.CartAction" var="cartAction" event="getCartItems"/>
<s:useActionBean beanclass="com.hk.web.action.core.discount.RewardPointTxnStatementAction" event="pre" var="rpBean"/>

<%

	String projectEnv = (String) ServiceLocatorFactory.getProperty(Keys.Env.projectEnv);
	pageContext.setAttribute("projectEnv", projectEnv);

  PrincipalImpl principal = (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
  UserService userService = ServiceLocatorFactory.getService(UserService.class);
  User loggedInUser = userService.getLoggedInUser();
  if (loggedInUser != null) {
    pageContext.setAttribute("user", loggedInUser);
    pageContext.setAttribute("userId", loggedInUser.getId());
    pageContext.setAttribute("userRoles", loggedInUser.getRoleStrings());
  } else {
    pageContext.setAttribute("userId", null);
  }

  String requestUrI = (String) request.getAttribute("javax.servlet.forward.request_uri");
  if(requestUrI == null){
    requestUrI = request.getRequestURI();
  }
  String queryString = request.getQueryString();
  StringBuilder stringBuilder = new StringBuilder(requestUrI);
  if(queryString!=null){
    stringBuilder.append("?").append(queryString);
  }
  String originalUrlHeader = stringBuilder.toString();

  pageContext.setAttribute("tempUser", RoleConstants.TEMP_USER);
  pageContext.setAttribute("b2bUser", RoleConstants.B2B_USER);
  pageContext.setAttribute("loyaltyUser", RoleConstants.HK_LOYALTY_USER);
  pageContext.setAttribute("hkRoles", Arrays.asList(RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER));
  pageContext.setAttribute("groupAdminRoles", RoleConstants.ROLE_GROUP_ADMINS_LIST);
%>

<s:layout-definition>
  <c:set var="attachRedirectParam" value="${attachRedirectParam}"/>
  <%
    String attachRedirectParamStr = (String) pageContext.getAttribute("attachRedirectParam");
//    System.out.println("aattachRedirectParam="+attachRedirectParamStr);
    boolean attachRedirectParam = attachRedirectParamStr == null ? true : Boolean.getBoolean(attachRedirectParamStr);
  %>
  <%
    DateTime currentDateTime = new DateTime();
    Date startOfOfferDate = new Date(new DateTime(2013, 01, 25, 19, 59, 59, 59).getMillis());
    Date endOfOfferDate = new Date(new DateTime(2013, 01, 27, 8, 59, 59, 59).getMillis());
  %>

  <div class='topBar container_24'>
      <div class="fnt-sz10 fnt-light" style="">

          <a href="${pageContext.request.contextPath}/beta/hk/FAQ.action?reftag=return" target="_blank">14 day return policy</a>&nbsp;
          <a href="${pageContext.request.contextPath}/beta/hk/ContactUs.action" target="_blank">Contact Us</a>&nbsp;
          <a href="http://www.healthkartplus.com" class="cont-rht" target="_blank"><img src="<hk:vhostImage/>/images/logo/HKPlus-Logo.png" class="pad-r-10" alt="HK Plus"/></a>&nbsp;&nbsp;
          <a href="http://www.healthkart.com/resources" class="cont-rht" target="_blank"><img src="<hk:vhostImage/>/images/logo/HKResources-Logo.png" class="pad-r-10" alt="HK Resources"/></a>
      </div>
      <div style="text-align:center">
      <s:link href="/" title='go to healthkart home'>
      <img src="<hk:vhostImage/>/images/logo/HK-Logo.png" class="pad-r-10" alt="Healthkart" style="position:relative;top:-10px;"/><br>
      </s:link>
      </div>
    <div class='topBarContent' style="position: absolute;right:0px;">

      <!--div class="message">
        <div class="arrow"></div>
        <span class="line1"></span>
        <s:link beanclass="com.hk.web.action.core.cart.CartAction" id="message_cart_link" rel="noFollow">
          <span class="line2">
            Proceed to checkout &rarr;
          </span>
        </s:link>

        <div class="close">(click anywhere to close this message)</div>
      </div>

      <div class='userCP'>

        <div style='float:right;'>

	        <div title='view your shopping cart'>
		        <div class="cartText">
			        <s:link beanclass="com.hk.web.action.core.cart.CartAction" rel="noFollow">
				        Cart (<span class='num' id="productsInCart">${cartAction.itemsInCart}</span>)
			        </s:link>
		        </div>
		        <s:link beanclass="com.hk.web.action.core.cart.CartAction" rel="noFollow">
			        <c:choose>
				        <c:when test="${cartAction.itemsInCart > 0}">
					        <img class='icon cartIcon' src='${pageContext.request.contextPath}/images/icons/cart.png'/>
				        </c:when>
				        <c:otherwise>
					        <img class='icon cartIcon'
					             src='${pageContext.request.contextPath}/images/icons/cart_empty.png'/>
				        </c:otherwise>
			        </c:choose>
		        </s:link>
	        </div>

        </div-->

        <div style="margin-top: 2px; float: right; margin-right: 10px;">
               <style>
                       /** header drop downs **/
                   .popUp .bdySctn{
                       background-color: #FFF;
                   min-height: 50px;
                   text-align: left;
                   border: 1px solid #F5EC13;
                   font-size: 0.9em;
                   line-height: 1.5em;
                       }
                   #cartPop.popUp {
                       position: absolute;
                       z-index: 1;
                       width: 200px;
                       margin-left: -92px;
                       margin-top: -2px;
                   }
                   #cartPop.popUp .bdySctn{
                       border: 1px solid #C8C8C8;
                       box-shadow: 0px 0px 20px #C8C8C8;
                   }
                   #cartPop.popUp .bdySctn .body > .msg{
                       font-size: 1.1em;
                       padding: 8px;
                       text-align: center
                   }

                   .popUp .bdySctn {
                       background-color: #FFF;
                       min-height: 50px;
                       text-align: left;

                       font-size: 0.9em;
                       line-height: 1.5em;
                   }
                   .hdr-drop-cntnr, .cart-pop-container {

                       position: relative;
                       top: -10px;
                       display: inline-block;
                       width:100px;
                       background-color: white;
                       /*   border: 1px solid transparent;*/
                   }

                   .hdr-drop-cntnr-hover, .cart-pop-container-hover {
                       z-index: 2;
                       background-color: white;
                       border-color: #C8C8C8;
                       box-shadow: 0px -1px 20px #C8C8C7;
                       border-bottom: 0;
                   }

                   .hdr-drop-label, .cart-pop-label, .offers-label {
                       padding: 4px;
                       padding-right: 8px;
                       text-align: right;
                       position: relative;
                       z-index: 11;

                   }

                   .hdr-drop-cntnr-hover .hdr-drop-label, .cart-pop-container-hover .cart-pop-label {
                       background: white;
                   }

                   .hdr-drop-down {
                       background-color: white;
                       position: absolute;
                       width: 147px;
                       margin-left: -41px;
                       margin-top: -2px;
                       padding-top: 10px;
                       padding-bottom: 10px;
                       border: 1px solid #c8c8c8;
                       box-shadow: 0px 4px 20px #c8c8c8;
                       z-index: 10;
                   }

                   .hdr-drop-down li {
                       padding: 5px 0px 5px 10px;

                   }

                   .hdr-drop-down li:hover {

                       background-color: #e6e6e6;

                   }

                       /** **/
               </style>





            <div class="hdr-drop-cntnr">
                <div class="hdr-drop-label">
                    <c:if test="${hk:collectionContainsAnyCollectionItem(userRoles, hkRoles)}">
                        <div class="fnt-caps">Account</div>
                        <span class="fnt-sz10">Hi <span style="max-width: 64%;line-height:1em;display: inline-block;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">${user.firstName}</span>  <span class="icn icn-dwn-arrow"></span></span>
                    </c:if>
                    <c:if test="${hk:collectionContains(userRoles, tempUser)}">
                        <div class="fnt-caps">Account</div>
                        <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="fnt-sz10" > Sign in
                        <s:param name="redirectUrl" value="<%=originalUrlHeader%>" />
                        </s:link> <span class="icn icn-dwn-arrow"></span>
                    </c:if>
                    <shiro:guest>
                        <div class="fnt-caps" >Account</div>
                        <s:link beanclass="com.hk.web.action.core.auth.LoginAction" class="fnt-sz10"> Sign in
                          <s:param name="redirectUrl" value="<%=originalUrlHeader%>" />
                        </s:link> <span class="icn icn-dwn-arrow"></span>
                    </shiro:guest>

                </div>
                <ul class="hdr-drop-down gl pad hide ">
                    <s:link beanclass="com.hk.web.action.core.user.MyAccountAction" event="pre">
                        <li>
                            Profile
                            <s:param name="tabId" value="1"/>
                        </li>
                    </s:link>
                    <s:link beanclass="com.hk.web.action.core.user.UserManageAddressAction">
                        <li>
                            Addresses
                            <s:param name="manageAddresses" value=""/>

                        </li>
                    </s:link>

                    <!--li>Orders</li>
                    <li>Rewards</li-->
                    <c:if test="${hk:collectionContainsAnyCollectionItem(userRoles, hkRoles)}">
                        <s:link beanclass="com.hk.web.action.core.auth.LogoutAction"><li class="brdr-t">LOG OUT</li>
                        <s:param name="redirectUrl" value="/" />
                        </s:link>
                    </c:if>
                </ul>

            </div>


               <div class="span2 cart-pop-container">
                   <div class="cart-pop-label">
                       <div class="fnt-caps">Your cart</div>
                       <span class="fnt-sz10"><span data-role="cart-counter">${cartAction.itemsInCart}</span> item  <span class="icn icn-dwn-arrow"></span></span>
                   </div>
                   <div id='cartPop' class='popUp hide'>
                       <div class=bdySctn>
                           <div class=body>
                               <div class=msg>
                                   <div class="fnt-bold mrgn-t-5">Cart Summary</div>
                                   <div>${cartAction.itemsInCart} item</div>
                                   <s:link beanclass="com.hk.web.action.core.cart.CartAction" class="btn btn-blue mrgn-bt-10" style="display:inline-block">PROCEED TO CART</s:link>
                               </div>
                           </div>
                       </div>
                   </div>
               </div>



        </div>

      </div>
    </div>

  <script>
      $(document).ready(function(){
      $('.hdr-drop-cntnr').hover(function () {
                  $(this).addClass('hdr-drop-cntnr-hover').find('.hdr-drop-down').show();
              },
              function () {
                  $(this).removeClass('hdr-drop-cntnr-hover').find('.hdr-drop-down').hide();
              });

      $('.hdr-drop-cntnr').on('click',function(e){

          $(this).trigger('mouseenter');
          $('.cart-pop-container').trigger('mouseleave');
          e.stopPropagation();
      });

      $('.cart-pop-container').hover(function () {
                  $(this).addClass('cart-pop-container-hover').find('#cartPop').show();
              },
              function () {
                  $(this).removeClass('cart-pop-container-hover').find('#cartPop').hide();
              });
      $('.cart-pop-container').click(function(e){
          $(this).trigger('mouseenter');
          $('.hdr-drop-cntnr').trigger('mouseleave');
          e.stopPropagation();
      });
      });
      $(document).click(function(){
        $('.cart-pop-container').removeClass('cart-pop-container-hover');
        $('#cartPop').hide();
          $('.cart-pop-container').removeClass('cart-pop-container-hover');
          $('.hdr-drop-cntnr').removeClass('hdr-drop-cntnr-hover');
          $('.popUp,.hdr-drop-down').hide();
      });
  </script>
</s:layout-definition>