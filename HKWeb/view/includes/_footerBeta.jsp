<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="java.util.Arrays" %>

<s:layout-definition>
    <s:useActionBean beanclass="com.hk.web.action.nwb.NwbMenuAction" var="menuAction" event="pre"/>

    <%
        UserService userService = ServiceLocatorFactory.getService(UserService.class);
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            pageContext.setAttribute("user", loggedInUser);
            pageContext.setAttribute("userRoles", loggedInUser.getRoleStrings());
        }
        pageContext.setAttribute("tempUser", RoleConstants.TEMP_USER);
    %>
    <div id="footer">
         <div class="container clearfix">
            <div class="ftr-main-txt">
                <span class="fnt-bold"> authentic products</span>
                <span class="mrgn-lr-10">|</span>
                <span class="fnt-bold"> free delivery on orders over rs. 499</span>
                <span class="mrgn-lr-10">|</span>
                <span class="fnt-bold">secure payment system</span>
            </div>

            <div class="ftr-link-cntnr clearfix">
                <div class="footer-menu">
                    <h4>Healthkart</h4>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/beta/hk/AboutUs.action">About Us</a> </li>
                        <li><a href="${pageContext.request.contextPath}/beta/hk/Careers.action">Careers</a></li>
                        <li><a href="${pageContext.request.contextPath}/beta/hk/TermsConditions.action">Terms & Conditions</a></li>
                        <li><a href="http://www.healthkart.com/resources">Blog</a></li>
                        <li><a href="${pageContext.request.contextPath}/beta/brand/BrandListing.action">Brand Directory</a></li>
                        <li><s:link beanclass="com.hk.web.action.core.loyaltypg.LoyaltyIntroductionAction">Loyalty</s:link></li>
                    </ul>
                </div>
                <div class="footer-menu">
                    <h4>Can We Help You</h4>
                    <ul>
                        <li><s:link beanclass="com.hk.web.action.core.user.MyAccountAction">Your Account</s:link></li>
                        <%--<li><a href="">Shipping Rates & Policies</a></li>--%>
                        <li><a href="${pageContext.request.contextPath}/beta/hk/FAQ.action?reftag=delivery">Delivery Policies</a></li>
                        <li><a href="${pageContext.request.contextPath}/beta/hk/FAQ.action?reftag=return">Returns Policy</a></li>
                        <li><a href="${pageContext.request.contextPath}/beta/hk/FAQ.action">FAQs and Help</a></li>
                        <li><a href="${pageContext.request.contextPath}/beta/hk/ContactUs.action">Contact Us</a></li>
                    </ul>
                </div>
                <c:set var="menuBreakPoint" value="${fn:length(menuAction.menuNodes)/2}"/>
                <div class="footer-menu">
                    <h4>Categories</h4>
                    <ul>
                        <c:forEach items="${menuAction.menuNodes}" var="menuNode" end="${menuBreakPoint}">
                            <li><a href="${menuNode.url}">${menuNode.name}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="footer-menu">
                    <h4>More categories</h4>
                    <ul>
                        <c:forEach items="${menuAction.menuNodes}" var="menuNode" begin="${menuBreakPoint+1}">
                            <li><a href="${menuNode.url}">${menuNode.name}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                <%--<div class="footer-menu">
                    <h4>Brands</h4>
                    <ul>
                        <li><a href="">MuscleBlaze</a></li>
                        <li><a href="">HealthViva</a></li>
                        <li><a href="">Optimum Nutrition</a></li>
                        <li><a href="">Dymatize</a></li>
                        <li><a href="">Gaspari</a></li>
                        <li><a href="">Accu-Chek</a></li>
                        <li><a href="">L'Oreal Paris</a></li>
                        <li><a href="">Muscletech</a></li>
                    </ul>
                </div>--%>


              <div class="subscibe-mail-cntnr">
                <p class="label-txt">Sign Up for emails and latest offers</p>

                <div class="span4">
                  <input type="text" name="subscriptionEmail" id="subscriptionEmail" placeholder="Email address" value=""/>
                </div>
                <div class="mrgn-l-30 clr-cont">
                  <input type="submit" id="submitSubscription"  name="submitSubscription" value="submit"
                         class="btn btn-gray"/>
                </div>
              </div>
            </div>
            <div class="clearfix about-hk">
                HealthKart.com is India's largest online health & fitness store for men, women, and kids. Shop online from
                the
                latest collections of health, fitness, beauty, sports, eye care, parenting and similar products featuring
                the best
                brands.
            </div>
            <div class="clearfix fnt-sz9">
                <div class="cont-lft mrgn-t-10">
                    <span class="txt-top">Pay using: </span>
                    <span class="icn icn-pmt-methods"></span>
                    <span class="txt-top">| Net Banking | Cash on Delivery</span>
                </div>
                <div class="cont-rht">
                    <span class="txt-middle fnt-caps">CopyRight &copy; 2013-2014, HealthKart.com, or its affiliates </span>
                    <span class="txt-middle mrgn-r-5"> | Connect with us:</span>

                    <span style="display: inline-block;" class="disp-inln txt-middle mrgn-t-10">
                        <a class="icn icn-fb" target="_blank" href="https://www.facebook.com/healthkart"></a>
                        <a class="icn icn-twitter" target="_blank" href="https://twitter.com/healthkart"></a>
                        <a class="icn icn-google-plus" target="_blank" href="https://plus.google.com/+healthkart/posts"></a>
                    </span>
                </div>
            </div>
        </div>

        <div class="go-to-top-cntnr">
            <a href="javascript:void(0)" title="Back to Top" class="go-to-top cont-rht"></a>
        </div>
    </div>
    <s:layout-component name="scriptComponent">
       <script type="text/javascript" src="<hk:vhostJs/>/static/beta/js/newCat.js"></script>
        <script type="text/javascript">
          $(document).ready(function () {

            function goToTop() {
              $(window).scroll(function (e) {
                if ($(window).scrollTop() > 100) {
                  $('.go-to-top-cntnr').css({
                    position: 'fixed',
                    top: '85%',
                    right: '1%'
                  }).fadeIn(500);
                } else {
                  $('.go-to-top-cntnr').fadeOut(500);
                }
              });
            }

            goToTop();

            function showErrMsg(ele, msg) {
              var errorIcn = $("<span class='icn-warning-small err-icn' ></span>");
              var errTxtMsg = $("<p class='err-txt'>" + msg + "</p>");
              if (!$(ele).hasClass('err-brdr')) {
                $(ele).addClass('err-brdr');
                $(ele).after(errorIcn);
                $(ele).after(errTxtMsg);
                return true
              }
            }

            function hideErrMsg(ele) {
              $(ele).removeClass('err-brdr');
              $(ele).next('.err-txt').remove();
              $(ele).next('.icn-warning-small').remove();

            }

            $('[name=submitSubscription]').click(function (event) {
              var doSubmit = true;
              var regexEMAIL = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

              if (!($('[name=subscriptionEmail]').val().length > 0)) {
                hideErrMsg('[name=subscriptionEmail]');
                showErrMsg('[name=subscriptionEmail]', 'Please enter email address');
                doSubmit = false;
              }
              else if (!regexEMAIL.test($('[name=subscriptionEmail]').val())) {
                hideErrMsg('[name=subscriptionEmail]');
                showErrMsg('[name=subscriptionEmail]', 'Email address is not valid');
                doSubmit = false;
              }
              else {
                hideErrMsg('[name=subscriptionEmail]');
              }
              if (!doSubmit) {
                return false;
              }
              var currEle = $('#submitSubscription');
              var email = $('#subscriptionEmail').val();
              HK.element.loader.add(currEle, true);
              var url = "/rest/api/subscribe/" + email;
              var map = null;
              var onSuccess = function (responseData) {
                if (responseData.success) {
                  HK.element.loader.remove(currEle, true);
                  var sucMsg = responseData.msgs;
                  HK.alert({title: 'Thank You !', content: HK.utils.generateHTMLForException(sucMsg), theme: HK.POPUP.THEME.ALERT});

                }
                else  {
                  HK.element.loader.remove(currEle, true);
                  var errorMsg = responseData.msgs;
                  var cntnt =HK.utils.generateHTMLForException(errorMsg);
                  cntnt.find('li:last').append('<span>. Please <a class="send-lnk" href="/core/user/Signup.action">Click here</a> to create an account with us.</span>');
                  HK.alert({title: 'Alert!', content: cntnt, theme: HK.POPUP.THEME.ALERT});

                }
              };
              var onError = function (xhr, a_status) {
                HK.element.loader.remove(currEle, true);
              };
              HK.Ajax.postJson(url, map, onSuccess, onError);

            });
          });


          $('.go-to-top').click(function(e){
                e.preventDefault();
                $('html,body').animate({scrollTop: 0}, 300);
            })
        </script>
    </s:layout-component>
</s:layout-definition>