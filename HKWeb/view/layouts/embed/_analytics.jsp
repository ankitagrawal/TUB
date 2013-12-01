<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%--
Custom Vars
-----------
Slot 1: sessionOrderCount=1
Slot 2: firstPurchaseDate=Y2012M12W54D352
Slot 3: lpBrand=Vitamin Shoppe BodyTech
Slot 4: lpPrimCat=health-nutrition
Slot 5: lpCatTree=/health-nutrition/dietary-supplements/omegas-fish-oil-flax

New Custom Events:

Category: [brandProdPageView]
Action: [OOS, InStock]
Label: [brand:Vitamin Shoppe BodyTech]

--%>
<s:layout-definition>
  <%
    String urlFragment = (String) pageContext.getAttribute("urlFragment");
    pageContext.setAttribute("urlFragment", urlFragment);

	  Boolean isProd = (Boolean) pageContext.getAttribute("isProd");
	  pageContext.setAttribute("isProd", isProd);

    if (AnalyticsConstants.analytics) {
  %>
  <c:set var="searchString" value="'" />
  <c:set var="replaceString" value="\\'" />
  <script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', '<%=AnalyticsConstants.gaCode%>']);
  <%
    Boolean orderCountSet = (Boolean) session.getAttribute(HealthkartConstants.Session.orderCountSetBoolean);
    Integer orderCount = (Integer) session.getAttribute(HealthkartConstants.Session.orderCount);
    if ((orderCountSet == null || !orderCountSet) && (orderCount != null)) {
      session.setAttribute(HealthkartConstants.Session.orderCountSetBoolean, true);
  %>
    window.orderCount = '<%=orderCount%>';
    _gaq.push(['_setCustomVar',
      <%=AnalyticsConstants.CustomVarSlot.sessionOrderCount.getSlot()%>,
      "<%=AnalyticsConstants.CustomVarSlot.sessionOrderCount.getName()%>",
      "<%=orderCount%>",
      <%=AnalyticsConstants.CustomVarSlot.sessionOrderCount.getScope().getLevel()%>
    ]);
  <%
    }

    Boolean newSession = (Boolean) session.getAttribute(HealthkartConstants.Session.newSession);
    //out.write("new session = "+newSession);
    if (newSession == null) {
      newSession = false;
			session.setAttribute(HealthkartConstants.Session.newSession, newSession);
      // this is a new session!
        String originalUrlHeader = (String) request.getAttribute("javax.servlet.forward.request_uri");
        if (originalUrlHeader == null) {
          originalUrlHeader = request.getRequestURI();
        }
        pageContext.setAttribute("originalUrlHeader", originalUrlHeader);
        // the landing page URL gives us info on the context of this page
        // or we can also use the variables passed in
  %>
    window.lpBrand = '${fn:replace(brand, searchString, replaceString)}';
    _gaq.push(['_setCustomVar',
      <%=AnalyticsConstants.CustomVarSlot.lpBrand.getSlot()%>,
      "<%=AnalyticsConstants.CustomVarSlot.lpBrand.getName()%>",
      '${fn:replace(brand, searchString, replaceString)}',
      <%=AnalyticsConstants.CustomVarSlot.lpBrand.getScope().getLevel()%>
    ]);
    window.lpPrimCat = "${fn:replace(topCategory, searchString, replaceString)}";
    _gaq.push(['_setCustomVar',
      <%=AnalyticsConstants.CustomVarSlot.lpPrimCat.getSlot()%>,
      "<%=AnalyticsConstants.CustomVarSlot.lpPrimCat.getName()%>",
      "${topCategory}",
      <%=AnalyticsConstants.CustomVarSlot.lpPrimCat.getScope().getLevel()%>
    ]);
    window.lpCatTree = "${hk:isNotBlank(urlFragment) ? urlFragment : originalUrlHeader}";
    _gaq.push(['_setCustomVar',
      <%=AnalyticsConstants.CustomVarSlot.lpCatTree.getSlot()%>,
      "<%=AnalyticsConstants.CustomVarSlot.lpCatTree.getName()%>",
      "${hk:isNotBlank(urlFragment) ? urlFragment : originalUrlHeader}",
      <%=AnalyticsConstants.CustomVarSlot.lpCatTree.getScope().getLevel()%>
    ]);
  <%
    }
  %>
    <%
    if (isProd != null && isProd == true) {
    %>
    _gaq.push(['_trackEvent','brandProdPageView','${isOutOfStockPage?"OOS":"InStock"}','${brand}']);
    <%
    }
    %>
    _gaq.push(['_trackPageview']);
    (function() {
      var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
      ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();

    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

    ga('create', '<%=AnalyticsConstants.uaCode%>', 'healthkart.com');
    ga('send', 'pageview');

  </script>

  <%--<!--Begin: Tracking code for MicroAd Blade-->--%>
  <%--<script type="text/javascript">--%>
  	<%--var blade_co_account_id='4184';--%>
  	<%--var blade_group_id='';--%>
  	<%--(function() {--%>
  	<%--var host = (location.protocol == 'https:') ? 'https://d-cache.microadinc.com' : 'http://d-cache.microadinc.com';--%>
  	<%--var path = '/js/bl_track_others.js';--%>

  	<%--var bs = document.createElement('script');--%>
  	<%--bs.type = 'text/javascript'; bs.async = true;--%>
  	<%--bs.charset = 'utf-8'; bs.src = host + path;--%>

  	<%--var s = document.getElementsByTagName('script')[0];--%>
  	<%--s.parentNode.insertBefore(bs, s);--%>
  	<%--})();--%>
  <%--</script>--%>
  <%--<!--End: Tracking code for MicroAd Blade-->--%>
  <%
    }
  %>
</s:layout-definition>
