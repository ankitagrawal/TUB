<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
    <%
        if (pageContext.getAttribute("pageType") != null) {
            String pageType = (String) pageContext.getAttribute("pageType");
            pageContext.setAttribute("pageType", pageType);
        }
    %>
    <!-- Purchase  -->
    <c:if test = "${pageType == 'purchase'} ">
        <script type="text/javascript" class="microad_blade_track">
            <!--
            var microad_blade_gl = microad_blade_gl || { 'params' : new Array(), 'complete_map' : new Object() };
            (function() {
                var param = {'co_account_id' : '4184', 'group_id' : 'convtrack14344', 'country_id' : '4', 'ver' : '2.1.0'};
                microad_blade_gl.params.push(param);

                var src = (location.protocol == 'https:')
                        ? 'https://d-cache.microadinc.com/js/blade_track_gl.js' : 'http://d-cache.microadinc.com/js/blade_track_gl.js';

                var bs = document.createElement('script');
                bs.type = 'text/javascript'; bs.async = true;
                bs.charset = 'utf-8'; bs.src = src;

                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(bs, s);
            })();
            -->
        </script>
    </c:if>

    <!-- Cart Page -->
    <c:if test = "${pageType == 'cart'} ">
        <script type="text/javascript" class="microad_blade_track">
            <!--
            var microad_blade_gl = microad_blade_gl || { 'params' : new Array(), 'complete_map' : new Object() };
            (function() {
                var param = {'co_account_id' : '4184', 'group_id' : 'convtrack14700', 'country_id' : '4', 'ver' : '2.1.0'};
                microad_blade_gl.params.push(param);

                var src = (location.protocol == 'https:')
                        ? 'https://d-cache.microadinc.com/js/blade_track_gl.js' : 'http://d-cache.microadinc.com/js/blade_track_gl.js';

                var bs = document.createElement('script');
                bs.type = 'text/javascript'; bs.async = true;
                bs.charset = 'utf-8'; bs.src = src;

                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(bs, s);
            })();
            -->
        </script>
    </c:if>

    <!-- Cart/Thanks/Catalog -->
    <c:if test = "${pageType == 'product'} || ${pageType == 'cart'} || ${pageType == 'category'}  || ${pageType == 'purchase'} ">
        <script type="text/javascript" class="microad_blade_track">
            <!--
            var microad_blade_gl = microad_blade_gl || { 'params' : new Array(), 'complete_map' : new Object() };
            (function() {
                var param = {'co_account_id' : '4184', 'group_id' : '', 'country_id' : '4', 'ver' : '2.1.0'};
                microad_blade_gl.params.push(param);

                var src = (location.protocol == 'https:')
                        ? 'https://d-cache.microadinc.com/js/blade_track_gl.js' : 'http://d-cache.microadinc.com/js/blade_track_gl.js';

                var bs = document.createElement('script');
                bs.type = 'text/javascript'; bs.async = true;
                bs.charset = 'utf-8'; bs.src = src;

                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(bs, s);
            })();
            -->
        </script>
    </c:if>


</s:layout-definition>