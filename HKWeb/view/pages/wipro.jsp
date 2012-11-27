<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Special Offers for Wipro Employees at HealthKart.com">

    <s:layout-component name="menu"><div style="height:30px;"></div></s:layout-component>

    <s:layout-component name="htmlHead">
        <style type="text/css">
            .cl {
                clear: both;
            }

            ul, ol {
                margin: 0;
                padding: 0;
            }

            @font-face
            {
                font-family: 'whitney_htf_bookregular'
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-book-webfont.svg#whitney_htf_bookregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }

            @font-face
            {
                font-family: 'whitney_htf_scbold'
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-boldsc-webfont.svg#whitney_htf_scbold') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }
            @font-face
            {
                font-family: 'whitney_htf_mediumregular'
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/whitneyhtf-medium-webfont.svg#whitney_htf_mediumregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }
            #pageContainer {
                margin: 15px auto;
                width: 960px;
                overflow: auto;
                padding-bottom: 260px;
                font-family: 'whitney_htf_bookregular';
                color:#231f20;
            }

            .cl {
                clear: both;
            }

            .browseCat {
                width: 836px;
                margin: 25px auto 0 auto;
                position: relative;

            }

            .browseCat h3 {
                font-family: 'whitney_htf_scbold';
                font-size: 11px;
                margin-bottom: 10px;
            }

            .browseCat ul.cat {
                background: #fff200;
                float: left;
                width: 138px;
                font-size: 11px;
            }

            .browseCat ul.cat li {
                border-bottom: 2px solid #fff;
                padding-left: 20px;
                line-height: 24px;
            }

            .browseCat ul.cat li a {
                color: #231f20;
                display: block;
            }

            .browseCat ul.cat li.last {
                border-bottom: none;
            }

            .browseCat .redeem {
                float: left;
                background: #f1f1f2;
                width: 638px;
                padding: 37px 30px;
            }

            .browseCat .redeem h2 {
                font-size: 23px;
                font-family: 'whitney_htf_scbold';
                border-bottom: 1px solid #515254;
                padding-bottom: 11px;

            }

            .browseCat .redeem ul li {
                float: left;
                line-height: 11px;
                width: 310px;
                margin-top: 10px;
                margin-right: 9px;
                color:#000;
            }

            .browseCat .redeem ul li img {
                float: left;
                margin-right: 5px;
            }

            hr {
                border: 0;
                color: #515254;
                background-color: #515254;
                height: 1px;
                text-align: left;
            }
            .productBox {
                width: 736px;
                margin: 25px auto 0 auto;
                position: relative;
                padding-left:100px;
            }
            .productBox h2 { color:#fff; padding:10px; font-size:12px; text-transform:uppercase; margin-bottom:10px; }
            .productBox h2.personal { background:#6b3518; }
            .productBox h2.nutrition { background:#f58022; }
            .productBox h2.homedevices { background:#343467; }
            .productBox h2.beauty { background:#006641; }
            .productBox h2.eye { background:#ba282e; }
            .productBox h2.parenting { background:#7d0063; }
            .productBox h2.sports { background:#ed1750; }
            .productBox h2.diabetes { background:#0099d3; }
            .productBox .product { height:250px; margin-left:20px; }
            .productBox .product, .productBox .num, .productBox .special, .productBox .prices { font-family: 'whitney_htf_bookregular'; text-align:left; }
            .productBox .product .img128 { border:4px solid #c6c8ca; }
            .productBox a.prod_link { color:#231f20; font-size:14px; text-align:left; border-bottom:none; }
            .green { color:#7cbe23; }
            .prices { padding:5px 0; }
            .special { font-style:normal; font-weight:bold; font-size:13px; }
            .hk .num { color:#0054a6; font-size:14px; }

#table-3 {
	border: 1px solid #DFDFDF;
	background-color: #F9F9F9;
	width: 100%;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
	font-family: Arial,"Bitstream Vera Sans",Helvetica,Verdana,sans-serif;
	color: #333;
  margin-bottom: 10px;
}
#table-3 td, #table-3 th {
	border-top-color: white;
	border-bottom: 1px solid #DFDFDF;
	color: #555;
}
#table-3 th {
	text-shadow: rgba(255, 255, 255, 0.796875) 0px 1px 0px;
	font-family: Georgia,"Times New Roman","Bitstream Charter",Times,serif;
	font-weight: normal;
	padding: 7px 7px 8px;
	text-align: left;
	line-height: 1.3em;
	font-size: 14px;
  background-color: yellow;
}
#table-3 td {
	font-size: 12px;
	padding: 4px 7px 2px;
	vertical-align: top;
}
        </style>


    </s:layout-component>

    <s:layout-component name="breadcrumbs">
        <div class='crumb_outer'>
            <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
            &gt;
            <span class="crumb last" style="font-size: 12px;">Offers for Wipro Employees</span>

            <h1 class="title">
                Special Offers for Wipro Employees at HealthKart.com
            </h1>
        </div>

    </s:layout-component>

    <s:layout-component name="metaDescription">Special Offers for Wipro Employees at HealthKart.com</s:layout-component>
    <s:layout-component name="metaKeywords"></s:layout-component>

    <s:layout-component name="content">
        <div class="cl"></div>
        <div id="pageContainer">
            <img src="${pageContext.request.contextPath}/images/wipro/header.gif" alt="" border="0"/>

            <div class="cl"></div>
            <div class="browseCat">
                <h3>BROWSE BY CATEGORIES</h3>
                <ul class="cat">
                    <li><a href="#beauty">BEAUTY</a></li>
                    <li><a href="#eye">EYE</a></li>
                    <li><a href="#nutrition">NUTRITION</a></li>
                    <li><a href="#sports">SPORTS</a></li>
                    <li><a href="#personal">PERSONAL CARE</a></li>
                    <li><a href="#home">HOME DEVICES</a></li>
                    <li><a href="#parenting">PARENTING</a></li>
                    <li class="last"><a href="#diabetes">DIABETES</a></li>
                </ul>

                <div class="redeem">
                    <h2>DISCOUNT COUPONS</h2>
                    <table id="table-3">
                      <thead>
                      <tr>
                        <th>Order Value</th>
                        <th>Extra Discount %</th>
                        <th>Coupon Code</th>
                      </tr>
                      </thead>
                      <tbody>
                      <tr>
                        <td>Rs. 0 - Rs. 500</td>
                        <td>5% Extra OFF</td>
                        <td>HKWIPRO5OFF</td>
                      </tr>
                      <tr>
                        <td>Rs. 501 - Rs. 1000</td>
                        <td>10% Extra OFF</td>
                        <td>HKWIPRO10OFF</td>
                      </tr>
                      <tr>
                        <td>Rs. 1001 or more</td>
                        <td>15% Extra OFF</td>
                        <td>HKWIPRO15OFF</td>
                      </tr>
                      <tr><td colspan="3">Only applicable on the products listed on this page. Subject to stock availability. Offer ends after Dec 2, 2012.<br/>Max Discount : Rs. 500</td></tr>
                      </tbody>
                    </table>
                    <h2>HOW ONE REDEEMS DISCOUNT ON HEALTHKART</h2>
                    <ul>
                        <li><img src="${pageContext.request.contextPath}/images/wipro/one.gif"/>Sign in / Register on healthkart
                            with your wipro email id
                            <hr/>
                            Discounts won't be valid on any other email id
                        </li>
                        <li><img src="${pageContext.request.contextPath}/images/wipro/three.gif"/>Add the products you
                            like to the cart
                            <hr/>
                        </li>
                        <span class="clear"></span>
                        <li><img src="${pageContext.request.contextPath}/images/wipro/two.gif"/>You’ll get a
                            confirmation mail from us upon activation
                            <hr/>
                            Click on the link given to go back to healthkart.com
                        </li>
                        <li><img src="${pageContext.request.contextPath}/images/wipro/four.gif"/>At check out, paste the
                            coupon code in the box given
                            <hr/>
                            It is important that you apply the right coupon code
                        </li>
                    </ul>
                </div>
            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="beauty" id="beauty">Beauty</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='VLC001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='PHILI04'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='VAADI22'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY536'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MYBLN30'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY355'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY351'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='GILI18'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='VAADI5'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY164'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='VEDIC30'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MYBLN20'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='VIVI05'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='ALVDA02'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='LOREAL13'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY499'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='ALVDA19'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='VLCCFRT61'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='JRDNA12'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY456'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY485'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BTY571'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='VAADI2'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BASCAR19'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NBB07'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NBB06'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BASCAR88'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='IMPL09'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NYX51'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='XMPD1'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='CLRBR21'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BASCAR9'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NBB16'/>
            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="eye" id="eye">Eye</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE799'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1104'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE442'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1093'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1187'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1188'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE732'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE005'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE004'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE039'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE035'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE023'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE024'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE484'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE297'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE622'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1236'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1033'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1224'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1111'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='EYE1025'/>
            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="nutrition" id="nutrition">Nutrition</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT1367'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT1368'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT590'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT420'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT722'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT410'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT717'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT978'/>

            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="sports" id="sports">Sports</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT109'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT707'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT739'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT053'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT400'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT1186'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT1219'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT105'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT303'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT129'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT1374'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT1165'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT1150'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT1167'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT521'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT539'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT603'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SPT146'/>
            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="personal" id="personal">Personal Care</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MOS001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MOS002'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='PHL002'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='PW007'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='COL001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='IPSA001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='AIR001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='PHL004'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='DET002'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='PRS001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='PP002'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='ERA001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='PRS003'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='OH021'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='OH056'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MED001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MED022'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MED020'/>
            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="homedevices" id="home">Home Devices</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='FCDIA003'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BEU70'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BREMED024'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='HP008'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='HB004'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='HB005'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='HB006'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='RD001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='HT003'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='HP004'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='HR006'/>
            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="parenting" id="parenting">Parenting</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB326'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB2063'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB2065'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB364'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB2074'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB339'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB2220'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB2215'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB397'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB1967'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB1966'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB1970'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB012'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB112'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB083'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='MOR003'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB1908'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB1905'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB925'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB925'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='BAB402'/>
            </div>
            <div class="cl"></div>

            <div class="productBox">
                <h2 class="diabetes" id="diabetes">Diabetes</h2>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='DS001'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SWT033'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='DM008'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SWT037'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='DS005'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='SWT035'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT109'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='NUT485'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='INSULIN007'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='DM002'/>
                <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId='DM018'/>
            </div>
            <div class="cl"></div>
        </div>


        <div class="cl"></div>
        <!-- Container Ends-->
        <!-- AddThis Button BEGIN -->
        <div class="addthis_toolbox addthis_floating_style addthis_counter_style" style="right:10px;top:50px;">
            <a class="addthis_button_facebook_like" fb:like:layout="box_count"></a>
            <a class="addthis_button_tweet" tw:count="vertical"></a>
            <a class="addthis_button_google_plusone" g:plusone:size="tall"></a>
            <a class="addthis_counter"></a>
        </div>
        <script type="text/javascript"
                src="http://s7.addthis.com/js/300/addthis_widget.js#pubid=ra-509261de45f6e306"></script>
        <!-- AddThis Button END -->
    </s:layout-component>


</s:layout-render>

