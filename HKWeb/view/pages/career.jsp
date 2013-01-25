<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Careers at HealthKart">

    <s:layout-component name="menu"><div style="height:30px;"></div></s:layout-component>

    <s:layout-component name="htmlHead">
        <style type="text/css">

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
                color:#464646;
            }

            .cl {
                clear: both;
            }
             .headingMain { font-size:28px; font-family: 'whitney_htf_bookregular'; font-weight:bold; margin-bottom:20px; }
            .headingMain span { font-weight:normal; margin-left:20px; }
           .headBanner { font-size:17px; line-height:22px;  font-family: 'whitney_htf_bookregular'; font-weight:normal; margin:20px 0; }
            .roles { float:left; width:50px; font-size: 18px; font-family: 'whitney_htf_bookregular';  padding-top:13px; font-weight:bold;  }
        .tab-container { float:right; width:890px; }
          .etabs { margin: 0; padding: 0; font-family: 'whitney_htf_bookregular';  }
    .tab { display: inline-block; zoom:1; *display:inline; }
    .tab a { font-size: 18px; line-height: 2em; display: block; padding: 0 10px; outline: none; border-bottom:none; color:#464646;  }
    .tab a:hover { text-decoration: none; }
    .tab.active { background: #fff; padding-top: 6px; position: relative; top: 1px; border-bottom:2px solid #464646; }
    .tab a.active { font-weight: normal; }
    .tab-container .panel-container { background: #fff; padding: 10px;  }
    .panel-container { margin-bottom: 10px; font-family: 'whitney_htf_bookregular'; font-size:15px; }
            .panel-container h3 { margin:0; padding:0; font-size:17px; margin-top:10px; font-weight:bold;}
            .panel-container p { margin-left:0; padding-left:0; }
            .panel-container ul { margin-left:20px; font-size:15px; }


        </style>

       <script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.hashchange.min.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/js/jquery.easytabs.min.js" type="text/javascript"></script>

  <script type="text/javascript">
    $(document).ready( function() {
      $('#tab-container').easytabs();
    });
  </script>

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/ddpowerzoomer.js">

/***********************************************
* Image Power Zoomer- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
***********************************************/

</script>

<script type="text/javascript">
jQuery(document).ready(function($){ //fire on DOM ready
 $('#myimage').addpowerzoom({
     defaultpower:4, //default power: 3x original image
     magnifiersize: [200, 200]
 })

})
</script>
    </s:layout-component>

    <s:layout-component name="breadcrumbs">
        <div class='crumb_outer'>
            <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
            &gt;
            <span class="crumb last" style="font-size: 12px;">Careers</span>

            <h1 class="title">
                Careers at HealthKart.com
            </h1>
        </div>

    </s:layout-component>

    <s:layout-component name="metaDescription">Latest Openings at HealthKart.com! Send in your resumes now!</s:layout-component>
    <s:layout-component name="metaKeywords"></s:layout-component>

    <s:layout-component name="content">
        <div class="cl"></div>
        <div id="pageContainer">
        <h3 class="headingMain">Work at healthkart <span>Work never ends... but neither does all the fun</span></h3>
            <img id="myimage" src="${pageContext.request.contextPath}/images/careers/banner-large.jpg" alt="" border="0" style="width:960px; height:265px;"/>


            <div class="cl"></div>
        <h3 class="headBanner">healthkart is India’s biggest health store on the internet. Our singular mission is to reach to provide
access to authentic, quality, and affordable health products to those who don’t. We strive to live up to this
with an ever growing but comprehensive catalog and a delivery team that reaches more that 4000 places in India</h3>

            <div class="cl"></div>

            <div class="roles">Roles</div>
            <div id="tab-container" class='tab-container'>
 <ul class='etabs'>
   <li class='tab'><a href="#tab1">Customer Support</a></li>
   <li class='tab'><a href="#tab2">Tech</a></li>
   <li class='tab'><a href="#tab3">Category</a></li>
 </ul>
 <div class='panel-container'>
  <div id="tab1">
   <h3>Position: Customer Counseling Executive
</h3>
  <p>Responsibilities:</p>
      <ul>
          <li>To answer customer queries related to products, health issues and the process of online ordering
    Internet and phone based counseling</li>
      </ul>
    <p>Skills required:</p>
      <ul>
          <li>Excellent English communication skills, both verbal and written</li>
          <li>Good knowledge of computers, especially internet</li>
          <li>Willingness to learn about health issues and products</li>
          <li>Positive attitude and customer oriented approach</li>
      </ul>
      <p>Experience:</p>
      <ul>
          <li>1-2 years of relevant experience in customer service</li>
      </ul>
      <p>Compensation:</p>
      <ul>
          <li>Rs 1.2-1.5 L per annum</li>
          <li>High performers would be promoted fast</li>
      </ul>
  </div>

     <div id="tab2">
   <h2>JS for these tabs</h2>

   <pre>
    <code>
&lt;script src="/javascripts/jquery.js" type="text/javascript"&gt;&lt;/script&gt;
&lt;script src="/javascripts/jquery.hashchange.js" type="text/javascript"&gt;&lt;/script&gt;
&lt;script src="/javascripts/jquery.easytabs.js" type="text/javascript"&gt;&lt;/script&gt;
    </code>
  </pre>

   <p>Optionally include the jquery <a href="http://benalman.com/projects/jquery-hashchange-plugin/">hashchange plugin</a> (recommended) or <a href="http://www.asual.com/jquery/address/docs/">address plugin</a> to enable forward-
and back-button functionality.</p>

  <pre>
    <code>
$('#tab-container').easytabs();
    </code>
  </pre>

  </div>

     <div id="tab3">
   <h2>JS for these tabs</h2>

   <pre>
    <code>
&lt;script src="/javascripts/jquery.js" type="text/javascript"&gt;&lt;/script&gt;
&lt;script src="/javascripts/jquery.hashchange.js" type="text/javascript"&gt;&lt;/script&gt;
&lt;script src="/javascripts/jquery.easytabs.js" type="text/javascript"&gt;&lt;/script&gt;
    </code>
  </pre>

   <p>Optionally include the jquery <a href="http://benalman.com/projects/jquery-hashchange-plugin/">hashchange plugin</a> (recommended) or <a href="http://www.asual.com/jquery/address/docs/">address plugin</a> to enable forward-
and back-button functionality.</p>

  <pre>
    <code>
$('#tab-container').easytabs();
    </code>
  </pre>

  </div>
 </div>
</div>

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

