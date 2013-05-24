<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
    <%
	    boolean isSecure = SslUtil.isSecure();
	    pageContext.setAttribute("isSecure", isSecure);
	    String label = (String) pageContext.getAttribute("label");
	    pageContext.setAttribute("label", label);

	    String id= (String) pageContext.getAttribute("id");
        pageContext.setAttribute("id", id);
    %>
    
    
    <%-- Script for Facebook Retargeting --%>
	<script type="text/javascript">
	adroll_adv_id = "SKDGP6YYENHVJCJDIKHUF7";
	adroll_pix_id = "JLZMDLGRYBFDFHEIKFE456";
	(function () {
	var oldonload = window.onload;
	window.onload = function(){
	   __adroll_loaded=true;
	   var scr = document.createElement("script");
	   var host = (("https:" == document.location.protocol) ? "https://s.adroll.com" : "http://a.adroll.com");
	   scr.setAttribute('async', 'true');
	   scr.type = "text/javascript";
	   scr.src = host + "/j/roundtrip.js";
	   ((document.getElementsByTagName('head') || [null])[0] ||
	    document.getElementsByTagName('script')[0].parentNode).appendChild(scr);
	   if(oldonload){oldonload()}};
	}());
	</script>
	    
    <!-- Google Code for Remarketing List -->
    <script type="text/javascript">
        /* <![CDATA[ */
        var google_conversion_id = ${id};
        var google_conversion_language = "en";
        var google_conversion_format = "3";
        var google_conversion_color = "666666";
        var google_conversion_label = "${label}";
        var google_conversion_value = 0;
        /* ]]> */
    </script>
	<script type="text/javascript">
  var conversionJsHost = (("https:" == document.location.protocol) ? "https://www." : "http://www.");
  document.write(unescape("%3Cscript src='" + conversionJsHost + "googleadservices.com/pagead/conversion.js' type='text/javascript'%3E%3C/script%3E"));
</script>
	<!--<script type="text/javascript" src="http://www.googleadservices.com/pagead/conversion.js">
    </script>-->
    <noscript>
        <div style="display:inline;">

	        <c:choose>
		        <c:when test="${isSecure}">
			        <img height="1" width="1" style="border-style:none;" alt="" src="http://www.googleadservices.com/pagead/conversion/${id}/?label=${label}&amp;guid=ON&amp;script=0"/>
		        </c:when>
		        <c:otherwise>
			        <img height="1" width="1" style="border-style:none;" alt="" src="http://www.googleadservices.com/pagead/conversion/${id}/?label=${label}&amp;guid=ON&amp;script=0"/>
		        </c:otherwise>
	        </c:choose>
        </div>
    </noscript>
</s:layout-definition>