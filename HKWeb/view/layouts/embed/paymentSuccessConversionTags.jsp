<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
<%
	if (AnalyticsConstants.analytics) {
%>
<c:set var="searchString" value="'" />
<c:set var="replaceString" value="\\'" />

<script type="text/javascript">
  var google_tag_params = {
    pageType: '${pageType}'
  };
</script>

<%--HealthKart Nutrition [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="thdECLLh1wIQztXQ0wM"
    id="980691662"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_Beauty [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="RPRVCNGP5gMQ_4XqxAM"
    id="949650175"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_Diabetes [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="W6B4CKWFoAUQ-4DL4QM"
    id="1009959035"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_Eye [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="QbbXCJHe6wIQn_iQ4gM"
    id="1011104799"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart Home & Living [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="LZvTCJ26rwUQk7ak2AM"
    id="990452499"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_HomeDevices [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="FpFlCKKgwQMQluHixAM"
    id="949530774"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_PersonalCare [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="LCQRCJvK-wIQtYzM4QM"
    id="1009976885"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_Sports [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="ZldBCPXCxQIQ44T54AM"
    id="1008616035"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_Parenting [verfied]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="CbkyCIysjAMQpIrJ3QM"
    id="1001538852"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--HealthKart_Brand [verified]--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="jPm5CLja1wIQuLjI5QM"
    id="1018305592"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--<!-- Google Code for hk_sale_common Conversion Page [aqua bright common] -->--%>
<s:layout-render
    name="/layouts/embed/_conversionCodeAdwords.jsp"
    label="z2A_CJuTlQcQ5cWB1AM"
    id="981492453"
    conversion_value="${conversion_value}"
    order_id="${order_id}"
    order="${order}"
    />

<%--<!-- dynamic remarketing conv tracking [verified]-->--%>
  <s:layout-render
      name="/layouts/embed/_conversionCodeAdwords.jsp"
      label="HPpqCK2F2AQQk6-l1wM"
      id="988370835"
      conversion_value="${conversion_value}"
      order_id="${order_id}"
      order="${order}"
      />

<%--Facebook Conversion Tracking Codes--%>

<%--BrightLifecare Account--%>
<script type="text/javascript">
var fb_param = {};
fb_param.pixel_id = '6010654549884';
fb_param.value = '0.00';
if (${conversion_value}) {
	fb_param.value = ${conversion_value};
}
fb_param.currency = 'INR';
(function(){
  var fpw = document.createElement('script');
  fpw.async = true;
  fpw.src = '//connect.facebook.net/en_US/fp.js';
  var ref = document.getElementsByTagName('script')[0];
  ref.parentNode.insertBefore(fpw, ref);
})();
</script>
<noscript><img height="1" width="1" alt="" style="display:none" src="https://www.facebook.com/offsite_event.php?id=6010654549884&amp;value=${conversion_value}&amp;currency=INR" /></noscript>

<%--Aquamarine Account--%>
<script type="text/javascript">
var fb_param = {};
fb_param.pixel_id = '6013681325720';
fb_param.value = '0.00';
if (${conversion_value}) {
	fb_param.value = ${conversion_value};
}
fb_param.currency = 'INR';
(function(){
  var fpw = document.createElement('script');
  fpw.async = true;
  fpw.src = '//connect.facebook.net/en_US/fp.js';
  var ref = document.getElementsByTagName('script')[0];
  ref.parentNode.insertBefore(fpw, ref);
})();
</script>
<noscript><img height="1" width="1" alt="" style="display:none" src="https://www.facebook.com/offsite_event.php?id=6013681325720&amp;value=${conversion_value}&amp;currency=INR" /></noscript>


<%
  }
%>
</s:layout-definition>