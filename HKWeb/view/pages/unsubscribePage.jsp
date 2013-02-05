<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Confirmation for Unsubscription">

<s:layout-component name="htmlHead">
  <style type="text/css">
		.cl { clear:both; }


 .outer { padding-bottom:240px; width:960px;  }
.box	{ padding:15px; width:918px; overflow:auto; clear:both; border:1px solid #ececec; }
      .outer h2 { font-size:18px; margin-bottom:15px; }
     .leftBox { float:left; width:200px; }
        .leftBox input[type="checkbox"], .rightBox input[type="checkbox"] { float:right; margin:0; padding:0; }
        .leftBox input[type="button"], .leftBox input[type="submit"], .rightBox input[type="button"], .rightBox input[type="submit"] { margin-left:0px; }
      .rightBox { float:right; width:500px; border:1px solid #ececec; padding:15px; }
      .leftBox p, .rightBox p { margin:0; padding:0; margin-bottom:15px; }
      
        </style>


</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">We Hate to See you go!</span>

    <h1 class="title">
     Unsubscribe Confirmation
    </h1>
  </div>
  
</s:layout-component>

<s:layout-component name="metaDescription">Confirmation for Unsubscription from HealthKart.com</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>

<s:layout-component name="content">
<div class="outer">

  <div class="box">
      	<h2>We Hate to See you go!</h2>
      <div class="leftBox">
          <p><strong>Reduce frequency options:</strong></p>
          <form action="">
          4-6 mails a month <input type="checkbox" /><br />
              <br />
          Once a month <input type="checkbox" />     <br />
              <br />
          <input type="submit" value="Go Ahead!" />
          </form>
          </div>


      <div class="rightBox">
          <p><strong>Still Want to Leave? Please tell us why:</strong></p>
          <form action="">
              I dont remember subscribing <input type="checkbox" /><br /><br />
              I had hoped for more exclusive offers in the emails <input type="checkbox" /><br /><br />
              I only use specific products from HK. not interested in others. <input type="checkbox" /><br /><br />
              No reason.  I just want to unsubscribe <input type="checkbox" /><br /><br />
              <input type="submit" value="Unsubscribe" />
          </form>
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
<script type="text/javascript" src="http://s7.addthis.com/js/300/addthis_widget.js#pubid=ra-509261de45f6e306"></script>
<!-- AddThis Button END --    >
  
</s:layout-component>


</s:layout-render>

