<%@ page import="com.hk.taglibs.Functions" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="org.joda.time.DateTimeFieldType" %>

<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.menu.MenuAction" var="menuAction" event="pre"/>

<s:layout-definition>
    <c:set var="topCategoryUrl" value="/${topCategory}"/>
    <c:set var="allCategories" value="${allCategories}"/>
    <%--
    use menuBarShifted class along with menu class to shift menu down when showing the notice
    the menu bar gets shifted by 30px; siteNotice div is 30px in height.
    --%>
    <%
        DateTime dateTime = new DateTime();
//    int day = dateTime.get(DateTimeFieldType.dayOfMonth());
//    day = day > 14 ? 14 : day;
        Date endOfOfferDate = new Date(new DateTime(2011, 12, 31, 23, 59, 59, 59).getMillis());

        String originalUrlMenu = (String) request.getAttribute("javax.servlet.forward.request_uri");
        if (originalUrlMenu == null) {
            originalUrlMenu = request.getRequestURI();
        }
    %>

    <div class='menuBar <%=dateTime.isBefore(endOfOfferDate.getTime()) ? "menuBarShifted" : ""%>' id="top">

        <%
            if (dateTime.isBefore(endOfOfferDate.getTime())) {
        %>
        <div class="siteNotice">
            <div style="border-top: 5px solid #ff9999; border-bottom: 5px solid #ff6666; height: 24px; padding-top: 6px; font-size: 1em;">
                    <%--<shiro:hasRole name="<%=RoleConstants.TEMP_USER%>">--%>
                    <%--<strong>Happy Children's Day</strong>: Get flat <strong>15%</strong> on all products as store credit. <strong><s:link beanclass="com.hk.web.action.core.auth.LoginAction"><s:param name="redirectUrl" value="<%=originalUrlMenu%>"/>Login/Signup</s:link></strong> to reveal coupon code.--%>
                    <%--</shiro:hasRole>--%>
                    <%--<shiro:guest>--%>
                    <%--<strong>Happy Children's Day</strong>: Get flat <strong>15%</strong> on all products as store credit. <strong><s:link beanclass="com.hk.web.action.core.auth.LoginAction"><s:param name="redirectUrl" value="<%=originalUrlMenu%>"/>Login/Signup</s:link></strong> to reveal coupon code.--%>
                    <%--</shiro:guest>--%>
                    <%--<shiro:user>--%>
                    <%--<shiro:lacksRole name="<%=RoleConstants.TEMP_USER%>">--%>
                    <%--<strong>Happy Children's Day</strong>: Get flat--%>
                    <%--<strong>15%</strong> on all products as store credit. Use coupon <strong>HK14NOV</strong>.--%>
                    <%--</shiro:lacksRole>--%>
                    <%--</shiro:user>--%>
                <strong>Happy New YEAR</strong> : 12% off to welcome 2012 - use coupon code <strong>NEWYEAR12</strong>
                <span style="background-color: #ccff00;">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%>
                </strong> remaining</span>
                    <%--<strong>Beauty Hat-trick Offer</strong> : Get your free coupon by joining us on facebook!
                   <a href="http://www.facebook.com/healthkart?sk=app_130363280399851">Click here</a> to get your coupon!
                   <span style="background-color: #ccff00;">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%>
                   </strong> remaining</span>--%>
                    <%--Our phone lines are down due to technical issues. Please use <strong>+91-9999571440</strong> to reach out to our customer care.--%>
            </div>
        </div>
        <%
            }
        %>

        <ul id="menuUl" class='lvl1'>
            <li class='lvl1' title='go to healthkart home' id='home_button'><s:link href='/'>
                <div id='homeIcon'></div>
            </s:link></li>
            <c:forEach items='${menuAction.menuNodes}' var='topMenuNode' varStatus='idx'>
                <c:if test="${topMenuNode.name != 'Baby'}">
                    <li class='lvl1 ${topMenuNode.url == topCategoryUrl ? 'active' : ''}'>
                        <a href='${pageContext.request.contextPath}${topMenuNode.url}' ${topCategoryUrl == '/' || topCategoryUrl == topMenuNode.url ? '' : 'rel=\'noFollow\''}>${topMenuNode.name}</a>

                            <%--<div class='lvl2'>--%>
                            <%--<c:forEach items='${topMenuNode.childNodes}' var='firstLevelMenuNode'>--%>
                            <%--<div class='categories'>--%>
                            <%--<a href='${pageContext.request.contextPath}${firstLevelMenuNode.url}' ${topCategoryUrl == topMenuNode.url ? '' : 'rel=\'noFollow\''}><span class='head2'>${firstLevelMenuNode.name}</span></a>--%>
                            <%--<ul>--%>
                            <%--<c:forEach items='${firstLevelMenuNode.childNodes}' var='secondLevelMenuNode'>--%>
                            <%--<li>--%>
                            <%--<a href='${pageContext.request.contextPath}${secondLevelMenuNode.url}' ${topCategoryUrl == topMenuNode.url && hk:firstStringContainsSecond(allCategories, firstLevelMenuNode.slug) ? '' : 'rel=\'noFollow\''}>${secondLevelMenuNode.name}</a>--%>
                            <%--</li>--%>
                            <%--</c:forEach>--%>
                            <%--</ul>--%>
                            <%--</div>--%>
                            <%--</c:forEach>--%>
                            <%--</div>--%>
                    </li>
                </c:if>
            </c:forEach>
                <li class='lvl1' id='brands_button' style='float:left;'><s:link href='/brands'>Brands</s:link></li>
            <%--<shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">--%>
                <li class='lvl1 new' id='super_savers_button' style='float:left;background-color:red;font-weight:bold;'><s:link
                        href='/super-savers'>Offers</s:link></li>
            <%--</shiro:hasPermission>--%>
                <%--<li class='lvl1' id='offers_button' style='float:left;'><s:link href='/pages/valentineGifts.jsp'>Gifts</s:link></li>--%>
                <%--<li class='' id='brands_button' style='float:right; margin: 4px 6px 0 0; padding: 2px 4px; background-color: #2b659d; -moz-border-radius: 3px; border-radius: 3px; border: 1px solid #4c97df;'>--%>
                <%--<a href='http://www.healthviva.com' style='color: yellow; border-bottom: 0;' target='_blank'>HealthViva &gt;</a>--%>
                <%--</li>--%>
        </ul>

    </div>

    <c:if test="${hk:isNotBlank(topCategoryUrl) && topCategoryUrl != '/'}">
        <div id="secondaryMenuUl" class='lvl2BarWrapper'>
            <c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">
                <c:if test="${topMenuNode.url == topCategoryUrl}">
                    <div class='lvl2BarWrapper'>
                        <div class='lvl2Bar'>
                            <ul>
                                <c:forEach items="${topMenuNode.childNodes}" var="firstLevelMenuNode">
                                    <li class='lvl2'>
                                        <a href="${pageContext.request.contextPath}${firstLevelMenuNode.url}">
                                            <span class='head2'>${firstLevelMenuNode.name}</span>
                                        </a>

                                        <div class='lvl3Container'>
                                            <ul>
                                                <c:forEach items="${firstLevelMenuNode.childNodes}"
                                                           var="secondLevelMenuNode">
                                                    <c:if test="${hk:firstStringContainsSecond(allCategories, firstLevelMenuNode.slug)}">
                                                        <li>
                                                            <a href="${pageContext.request.contextPath}${secondLevelMenuNode.url}">${secondLevelMenuNode.name}</a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
                <%--<script type="text/javascript">--%>
                <%--<c:forEach items="${menuAction.menuNodes}" var="topMenuNode" varStatus="idx">--%>
                <%--<c:if test="${topMenuNode.url == topCategoryUrl}">--%>
                <%--document.write("<div class='lvl2Bar'><ul>");--%>
                <%--<c:forEach items="${topMenuNode.childNodes}" var="firstLevelMenuNode">--%>
                <%--document.write("<li class='lvl2'><a href='${pageContext.request.contextPath}${firstLevelMenuNode.url}' rel='nofollow'><span class='head2'>${firstLevelMenuNode.name}</span></a><div class='lvl3Container'><ul>");--%>
                <%--<c:forEach items="${firstLevelMenuNode.childNodes}" var="secondLevelMenuNode">--%>
                <%--document.write("<li><a href='${pageContext.request.contextPath}${secondLevelMenuNode.url}' rel='nofollow'>${secondLevelMenuNode.name}</a></li>");--%>
                <%--</c:forEach>--%>
                <%--document.write("</ul></div></li>");--%>
                <%--</c:forEach>--%>
                <%--document.write("</ul></div>");--%>
                <%--</c:if>--%>
                <%--</c:forEach>--%>
                <%--</script>--%>
        </div>
    </c:if>

    <%--<span style="display:none;"><s:link beanclass="web.action.HeartbeatAction" id="heartbeat">H</s:link></span>--%>
    <span style="display:none;" id="topCategoryContainer">${topCategory}</span>

    <script type="text/javascript">
        $('li.search').click(function() {
            return false;
        });
        $(".lvl2Bar li.lvl2 .lvl3Container ul li,  .menuBar ul.lvl1 li.lvl1 div.lvl2 .categories li").click(function() {
            url = $(this).children("a").attr("href");
            document.location.href = url;
        });
        $('[placeholder]').each(function(i, el) {
            var str = $(this).attr("placeholder");
            $(this).val(str);
            $(this).click(function() {
                if ($(this).val() == str) {
                    $(this).val("");
                }
            });
            $(this).blur(function() {
                if ($(this).val() == str) {
                    $(this).val(str);
                }
            });
        });
    </script>
    <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
</s:layout-definition>