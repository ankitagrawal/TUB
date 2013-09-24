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
                    <h4>Get to Know Us</h4>
                    <ul>
                        <li><a href="/beta/hk/AboutUs.action">About Us</a> </li>
                        <li><a href="/beta/hk/Careers.action">Careers</a></li>
                        <li><a href="/beta/hk/TermsConditions.action">Terms & Conditions</a></li>
                        <li><a href="http://www.healthkart.com/resources">Blog</a></li>
                    </ul>
                </div>
                <div class="footer-menu">
                    <h4>Can We Help You</h4>
                    <ul>
                        <li><a href="/beta/account/MyAccount.action">Your Account</a></li>
                        <%--<li><a href="">Shipping Rates & Policies</a></li>--%>
                        <li><a href="/beta/hk/FAQ.action#delivery">Delivery Policies</a></li>
                        <li><a href="/beta/hk/FAQ.action#return">Returns Policy</a></li>
                        <li><a href="/beta/hk/FAQ.action">FAQs and Help</a></li>
                        <li><a href="/beta/hk/ContactUs.action">Contact Us</a></li>
                    </ul>
                </div>
                <div class="footer-menu">
                    <h4>Categories</h4>
                    <ul>
                        <c:forEach items="${menuAction.menuNodes}" var="menuNode">
                            <li><a href="${menuNode.url}">${menuNode.name}</a></li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="footer-menu">
                    <h4>Brands</h4>
                    <ul>
                        <li><a href="/brand/muscleblaze?navKey=BR-539">MuscleBlaze</a></li>
                        <li><a href="/brand/healthviva?navKey=BR-427">HealthViva</a></li>
                        <li><a href="/brand/optium?navKey=BR-203">Optimum Nutrition</a></li>
                        <li><a href="/brand/dymatize?navKey=BR-497">Dymatize</a></li>
                        <li><a href="/brand/gaspari-nutrition?navKey=BR-498">Gaspari</a></li>
                        <li><a href="/brand/accu-chek?navKey=BR-165">Accu-Chek</a></li>
                        <li><a href="/brand/loreal-paris?navKey=BR-107">L'Oreal Paris</a></li>
                        <li><a href="/brand/muscletech?navKey=BR-502">Muscletech</a></li>
                    </ul>
                </div>


              <div class="subscibe-mail-cntnr">
                <p class="label-txt">Sign Up for emails and latest offers</p>

                <div class="span4">
                  <input type="text" name="subscriptionEmail" id="subscriptionEmail" placeholder="Email address" value=""/>
                </div>
                <div class="mrgn-l-30">
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
        <script type="text/javascript">
            $(document).ready(function(){

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
              function hideErrMsg(ele){
                $(ele).removeClass('err-brdr');
                $(ele).next('.err-txt').remove();
                $(ele).next('.icn-warning-small').remove();

              }
                $('[name=submitSubscription]').click(function (event) {
                  var doSubmit = true;
                    if (! ($('[name=subscriptionEmail]').val().length > 0)) {
                        if(! $('[name=subscriptionEmail]').hasClass('err-brdr')){
                            $('[name=subscriptionEmail]').addClass('err-brdr');
                            $('#subscriptionEmail').after($("<p class='err-txt'>Please enter Email address</p>"));
                            $('#subscriptionEmail').after($("<span class='icn-warning-small err-icn2' ></div>"));
                        }
                        doSubmit = false;
                    }
                    else {

                        regexEMAIL = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
                        if (! regexEMAIL.test($('[name=subscriptionEmail]').val())) {
                            if(! $('[name=subscriptionEmail]').hasClass('err-brdr')){
                                $('[name=subscriptionEmail]').addClass('err-brdr');
                                $('subscriptionEmail').after($("<p class='err-txt'>Email address is not valid</p>"));
                                $('#subscriptionEmail').after($("<span class='icn-warning-small err-icn2' ></div>"));
                            }
                            doSubmit = false;
                        }
                        else{
                            doSubmit = true;
                        }
                    }
                    if (doSubmit != true) {
                        event.preventDefault();
                    }
                });
            });


            $('.go-to-top').click(function(e){
                e.preventDefault();
                $('html,body').animate({scrollTop: 0}, 300);
            })
        </script>
    </s:layout-component>
</s:layout-definition>