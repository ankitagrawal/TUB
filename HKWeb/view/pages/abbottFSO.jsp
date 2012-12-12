<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Abbott FreeStyle Optium Glucometer">

    <s:layout-component name="htmlHead">
        <style type="text/css">

            .cl {
                clear: both;
            }

            #pageContainer {
                margin: 15px auto;
                width: 960px;
                overflow: auto;
                padding-bottom: 260px;
            }


            .productThumb {
                margin-top: 25px;
                padding: 20px;
                border: 1px solid #efefef;
                width: 918px;
                overflow: auto;
                background: #fff;
                float:left;
            }

            .productThumb p {
                margin: 0;
                padding: 0;
            }

            .productThumb .tThumb {
                float: left;
                margin-top: 20px;
                margin-right: 23px;
            }

            .productThumb h3 {
                font-size: 15px;
                line-height: 27px;
                margin-bottom: 10px;
                font-weight: bold;
            }

            .productThumb h3 a {
                font-size: 15px;
                color: #505050;
                border: none;
            }

            .productThumb h3 a:hover {
                color: #000;
            }

            .productThumb .tDesc {
                font-size: 13px;
                background: #f0f0f0;
                border: 1px solid #d9d9d9;
                width:530px;
                float: left;
                padding: 5px;
                margin-bottom: 35px;
                line-height: 14px;
                padding: 10px;
                padding-bottom: 0;
                clear:right;
            }

            .productThumb .tDesc p {
                padding-bottom: 10px;
            }

            .productThumb .tPrice {
                float: left;
                font-size: 16px;
                color: #2484c6;
                margin-bottom: 10px;
                width:530px;
            }
             .productThumb .tPrice img { float:right; }

            .productThumb a {
                border: none;
            }


        </style>

    </s:layout-component>

    <s:layout-component name="breadcrumbs">
        <div class='crumb_outer'>
            <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
            &gt;
            <span class="crumb last" style="font-size: 12px;">Abbott FreeStyle Optium Glucometer</span>

            <h1 class="title">
                Abbott FreeStyle Optium Glucometer
            </h1>
        </div>

    </s:layout-component>

    <s:layout-component
            name="metaDescription">Special Offers on Abbott FreeStyle Optium Glucometer from HealthKart.com.</s:layout-component>
    <s:layout-component name="metaKeywords"></s:layout-component>

    <s:layout-component name="content">
        <div class="cl"></div>
        <div id="pageContainer">
            <img src="${pageContext.request.contextPath}/images/abbottFSO/banner.jpg" width="960" height="250"
                 alt="rakhi banner"/>

            <div class="cl"></div>


            <div class="productThumb">
                <h3><a href="http://www.healthkart.com/product/freestyle-optium-glucometer/DM033">FreeStyle Optium Blood Glucose Monitoring System</a></h3>

                   <img src="${pageContext.request.contextPath}/images/abbottFSO/FreeStyle-Optium-Glucometer.jpg"
                     width="340" height="340" class="tThumb"/>

                <div class="tDesc"><p>Improved testing experience with FreeStyle Optium Blood Glucose Monitoring System. Only 0.6 µL of blood sample required to get an accurate result in 5 seconds.</p>

                    <p>Re-application of blood possible that prevents strip wastage. This simple device comes with a large backlit display and can record upto 450 readings so that once can store blood glucose readings for reference.
</p>
                </div>

                <p class="tPrice">

                    Rs 2,199
                    <a href="http://www.healthkart.com/product/freestyle-optium-glucometer/DM033"><img
                        src="${pageContext.request.contextPath}/images/abbottFSO/buynow.gif" width="89" height="26"
                        alt="Buy Now"/></a>
                </p>


                <p><strong>How to apply Coupon Code to avail discount of INR 700?</strong></p>
                <ul style="list-style:disc; margin-left:350px;">
                <li>Click on the "Buy Now" tab to proceed to the product page.</li>
                <li>Click on "Place Order" tab. Selected item will get added to the cart.</li>
                <li>Proceed to checkout.</li>
                <li>On the checkout page, enter your Coupon Code in the discount bar and click to apply.</li>
                </ul>
            </div>


        </div>

        <div class="cl"></div>


    </s:layout-component>


</s:layout-render>

