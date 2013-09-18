<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.shiro.PrincipalImpl" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.service.UserService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.user.User" %>
<%@ page import="java.util.Arrays" %>

<s:layout-definition>

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
                        <%--<li><a href="">Careers</a></li>--%>
                        <%--<li><a href="">Investor Relations</a></li>--%>
                        <%--<li><a href="">Press Releases</a></li>--%>
                        <li><a href="/beta/hk/TermsConditions.action">Terms & Conditions</a></li>
                        <%--<li><a href="">Privacy Notice</a></li>--%>
                        <li><a href="http://www.healthkart.com/resources">Blog</a></li>
                    </ul>
                </div>
                <div class="footer-menu">
                    <h4>Can We Help You</h4>
                    <ul>
                        <li><a href="/beta/account/MyAccount.action">Your Account</a></li>
                        <%--<li><a href="">Shipping Rates & Policies</a></li>--%>
                        <li><a href="/beta/hk/FAQ.action">FAQs</a></li>
                        <li><a href="/beta/hk/FAQ.action#return">Returns Policy</a></li>
                        <li><a href="/beta/hk/ContactUs.action">Contact Us</a></li>
                    </ul>
                </div>
                <%--<div class="footer-menu">--%>
                    <%--<h4>Whats new</h4>--%>
                    <%--<ul>--%>
                        <%--<li><a href="">New At HK</a></li>--%>
                        <%--<li><a href="">Shop By Occassion</a></li>--%>
                        <%--<li><a href="">Shop By Brands</a></li>--%>
                        <%--<li><a href="">Looks & Trends</a></li>--%>
                        <%--<li><a href="">48hr Sale</a></li>--%>
                        <%--<li><a href="">Gift Cards</a></li>--%>
                    <%--</ul>--%>
                <%--</div>--%>
                <div class="span5 offset3 subscibe-mail-cntnr">
                    <p class="label-txt">Sign Up for emails and latest offers</p>
                    <input type="text" name="subscriptionEmail" id="subscriptionEmail" placeholder="Email address"
                           value=""/>
                    <input type="submit" name="submitSubscription" value="submit"
                           class="btn btn-gray"/>
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
            });
            $('.go-to-top').click(function(e){
                e.preventDefault();
                $('html,body').animate({scrollTop: 0}, 300);
            })
        </script>
    </s:layout-component>
</s:layout-definition>