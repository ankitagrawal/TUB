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
 <div class="clear"></div>
  <div id="logoBoxContainer" style="cursor:default; width: 960px; margin: 35px auto 7px;">

    <div class='logoBox' style="float:left;">
      <s:link href="/" title='go to healthkart home'>
        <img src='<hk:vhostImage/>/images/logo.png' alt="healthkart logo"/>
      </s:link>
    </div>
    
    <s:form beanclass="com.hk.web.action.core.search.SearchAction" method="get" renderFieldsPresent="false" renderSourcePage="false" autocomplete="off" style="float:left">
      <s:text name="query" id="searchbox" class="input_tip" title='Search for products, categories or brands...' style="height:22px; font-size: 15px; width: 315px;" value="${param['query']}" placeholder='Search for products, categories or brands...'/>
      <s:image title="Search" name="search" src="/images/icons/search2.png" style="left:50px; width:20px; vertical-align:middle;"/>
    </s:form>

    <div class="offerAndBrands">
      <div style="float:left"><s:link href='/resources'>HealthMag&nbsp;</s:link></div><div style="float:left; margin-right: 15px;"><s:link href='/resources'><img src="<hk:vhostImage/>/images/resources_16.png"></s:link></div>
      <div style="float:left"><s:link href='/brands'>Brands&nbsp;</s:link></div><div style="float:left; margin-right: 15px;"><s:link href='/brands'><img src="<hk:vhostImage/>/images/brand_16.png"></s:link></div>
      <div style="float:left"><s:link href='/super-savers'>Offers&nbsp;</s:link></div><div style="float:left; margin-right: 15px;"><s:link href='/super-savers'><img src="<hk:vhostImage/>/images/offer_16.png"></s:link></div>
      <div style="float:left"><a href='http://www.healthkartplus.com?src=hk' target='_blank' style="color:#000">HealthKartPlus&nbsp;</a></div><div style="float:left;"><a href='http://www.healthkartplus.com?src=hk' title="HealthKartPlus.com"><img src="${pageContext.request.contextPath}/images/hkp-favicon.jpg"></a></div>
      <div style="float:left; margin-right: 15px;">&nbsp;<s:link href='${pageContext.request.contextPath}/core/loyaltypg/LoyaltyIntroduction.action' target="_blank" 
      style="background-image:url('${pageContext.request.contextPath}/pages/loyalty/resources/images/stellar-bg.png');" >hk<strong>stellar</strong></s:link></div>
    </div>

    <div style="clear:both;"></div>
  </div>

  <script type="text/javascript">
    $(document).ready(function() {
      $("#searchbox").autocomplete({url:'${pageContext.request.contextPath}/autocomplete-search/'});
    });
  </script>

	<%--<%
		DateTime dateTime = new DateTime();
		Date endOfOfferDate = new Date(new DateTime(2012, 12, 26, 18, 59, 59, 59).getMillis());
		if (dateTime.isBefore(endOfOfferDate.getTime())) {
	%>
	<div class="siteNotice" style="width:960px; margin-left:auto; margin-right:auto;">
			--%><%--<div style="height: 44px; padding-top: 6px; font-size: 1em; color: black; background-color: white; border: solid 4px #4484c4;">
									<strong>Great Online Shopping Festival</strong>! Over 500 products, upto <strong>80% off! *</strong>, <strong><a href="http://www.healthkart.com/online-shopping-festival?src=hk2">Click here to start Shopping!</a></strong><br/>
									<span style="font-size: 1.2em; color: #e62580">Only <strong><%=Functions.periodFromNow(endOfOfferDate)%></strong> remaining</span>
							</div>--%><%--

		<div
				style="border-top: 2px solid #ff9999; border-bottom: 2px solid #ff6666; height: 20px; padding-top: 3px; font-size: 1em;">
			Our customer care phone lines are down due to technical issues. Sincere apologies for the inconvenience caused.
		</div>
	</div>
	<%
		}
	%>--%>

    <div class='menuBar' id="top">

        <ul id="menuUl" class='lvl1'>
            <%--<li class='lvl1' title='go to healthkart home' id='home_button'><s:link href='/'>
                <div id='homeIcon'></div>
            </s:link></li>--%>
            <c:forEach items='${menuAction.menuNodes}' var='topMenuNode' varStatus='idx'>
                <%--<c:if test="${topMenuNode.url != '/home-living'}">--%>
                    <li class='lvl1 ${topMenuNode.url == topCategoryUrl ? 'active' : ''} ${topMenuNode.url == '/home-living' ? 'new' : ''}'>
                        <a href='${pageContext.request.contextPath}${topMenuNode.url}' ${topCategoryUrl == '/' || topCategoryUrl == topMenuNode.url ? '' : 'rel=\'noFollow\''}>${topMenuNode.name}</a>
                    </li>
                <%--</c:if>--%>
            </c:forEach>
             <%--  <li class='' id='brands_button' style='float:right; margin: 4px 6px 0 0; padding: 2px 4px; background-color: #2b659d; -moz-border-radius: 3px; border-radius: 3px; border: 1px solid #4c97df;'>
                  <a href='http://www.healthkartplus.com' style='color: yellow; border-bottom: 0;' target='_blank'>HealthKartPlus &gt;</a>
                </li>--%>
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
        </div>
    </c:if>

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

</s:layout-definition>