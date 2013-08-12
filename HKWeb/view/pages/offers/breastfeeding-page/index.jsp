<%--
  Created by IntelliJ IDEA.
  User: Rahul Dixit
  Date: 8/1/13
  Time: 12:26 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Breastfeeding details">
<%
    boolean isSecure = pageContext.getRequest().isSecure();
    pageContext.setAttribute("isSecure", isSecure);
%>

<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/pages/offers/breastfeeding-page/css/breastfeeding-style.css"
          rel="stylesheet" type="text/css"/>
</s:layout-component>

    <s:layout-component name="breadcrumbs">
        <div class='crumb_outer'><s:link
                beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
            &gt; <span class="crumb last" style="font-size: 12px;">World's Breastfeeding Week</span>

            <h1 class="title">sports Offers</h1>
        </div>

    </s:layout-component>
    <s:layout-component name="content">


        <div class="container">
            <div class="greenBx" id="rMenu">
                <div class="row">
                    <a href="http://www.healthkart.com/parenting/feeding-nursing/breastfeeding-aids" target="_blank"><img src="images/breastfeeding-aids2.jpg" /></a>
                </div>
                <div class="row">
                    <a href="http://www.healthkart.com/personal-care/sexual-wellness/supplements" target="_blank"><img src="images/breastfeeding-supplements2.jpg" /></a>
                </div>

            </div>

            <div class="row"><img src="images/worlds-breastfeeding-week.jpg" alt="World's Breastfeeding Week" /></div>
            <div class="row">
                <div class="inrContainer">
                    <div class="row">
                        <div class="bx1"><img src="images/wbw-img1.jpg" /></div>
                        <div class="bx2"><img src="images/wbw-img2.jpg" /></div>
                    </div>

                    <div class="rowGap"></div>

                    <div class="row">
                        <div class="bx3And4Outer">
                            <div class="bx3"><img src="images/wbw-img3.jpg" /></div>
                            <div class="bx4"><a href="http://www.healthkart.com/product/newmom-disposable-breast-pad/NM002" target="_blank"><img src="images/wbw-img4.jpg" /></a></div>
                        </div>

                        <div class="bx5And6Outer">
                            <div class="bx5"><img src="images/wbw-img5.jpg" /></div>
                            <div class="bx6"><img src="images/wbw-img6.jpg" /></div>
                        </div>
                    </div>

                    <div class="rowGap"></div>

                    <div class="row">
                        <div class="bx7"><img src="images/wbw-img7.jpg" /></div>
                        <div class="bx8"><a href="http://www.healthkart.com/product/farlin-manual-breast-pump/BAB045" target="_blank"><img src="images/wbw-img8.jpg" /></a></div>
                    </div>

                    <div class="rowGap"></div>

                    <div class="row">
                        <div class="bx9"><a href="http://www.healthkart.com/product/breastfeeding-multivitamin/FER016" target="_blank"><img src="images/wbw-img9.jpg" /></a></div>
                        <div class="bx10"><img src="images/wbw-img10.jpg" /></div>
                    </div>

                    <div class="rowGap"></div>

                    <div class="row">
                        <div class="bx11To17">
                            <div class="bx11"><img src="images/wbw-img11.jpg" /></div>
                            <div class="row">
                                <div class="bx12"><img src="images/wbw-img12.jpg" /></div>
                                <div class="bxsGap1"></div>
                                <div class="bx13"><img src="images/wbw-img13.jpg" /></div>
                                <div class="bxsGap1"></div>
                                <div class="bx14"><img src="images/wbw-img14.jpg" /></div>
                            </div>
                            <div class="rowGap"></div>
                            <div class="bx15"><img src="images/wbw-img15.jpg" /></div>
                            <div class="row">
                                <div class="bx16And17"><a href="http://www.healthkart.com/product/medela-purelan-natural-nipple-skin-care/BAB445" target="_blank"><img src="images/wbw-img16.jpg" /></a></div>
                                <div class="bxsGap1"></div>
                                <div class="bx16And17"><a href="http://www.healthkart.com/product/medela-contact-nipple-shields/BAB446" target="_blank"><img src="images/wbw-img17.jpg" /></a></div>
                            </div>
                        </div>
                        <div class="bx18"><img src="images/wbw-img18.jpg" /></div>
                    </div>

                    <div class="rowGap"></div>

                    <div class="row">
                        <div class="bx19To21">
                            <div class="bx19"><img src="images/wbw-img19.jpg" /></div>
                            <div class="row">
                                <div class="bx20And21"><img src="images/wbw-img20.jpg" /></div>
                                <div class="bxsGap1"></div>
                                <div class="bx20And21"><img src="images/wbw-img21.jpg" /></div>
                            </div>
                        </div>
                        <div class="bx22To24">
                            <div class="bx22"><a href="http://www.healthkart.com/product/nursing-time-tea/FER019" target="_blank"><img src="images/wbw-img22.jpg" /></a></div>
                            <div class="bx23"><img src="images/wbw-img23.jpg" /></div>
                            <div class="bx24"><img src="images/wbw-img24.jpg" /></div>
                        </div>
                    </div>

                    <div class="rowGap"></div>
                    <div class="bx25"><img src="images/wbw-img25.jpg" /></div>

                </div>
            </div>

            <div class="cl"></div>
            <script src="js/srcoll-menu.js"></script>
    </s:layout-component>

</s:layout-render>






















