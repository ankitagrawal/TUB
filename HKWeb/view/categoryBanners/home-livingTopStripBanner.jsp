<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.taglibs.Functions" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<div style="margin-left: auto; margin-right: auto; width:960px;">
    <%--<img src="${pageContext.request.contextPath}/images/banners/top/beauty-promotional-strip-banner.jpg" alt="Beauty Discount">--%>
     <img
            src="${pageContext.request.contextPath}/images/banners/Philips-appliances.jpg"
            alt="Flat 25% Off" usemap="#Map">
      <map name="Map" id="Map">
        <%--<area shape="rect" coords="262,-7,453,94" href="${pageContext.request.contextPath}/home-living/cook-fry/induction-cooker&camp=home_living_25_off" />--%>
        <%--<area shape="rect" coords="468,-1,655,98" href="${pageContext.request.contextPath}/home-living/mix-grind-chop/juicer-mixer-grinder&camp=home_living_25_off" />--%>
        <%--<area shape="rect" coords="667,1,874,80" href="${pageContext.request.contextPath}/home-living/mix-grind-chop/food-processor&camp=home_living_25_off" />--%>

            <area shape="rect" coords="0,-7,500,94"
                  href="${pageContext.request.contextPath}/home-living/home-appliances/iron/steam?brand=Philips&phi"/>
            <area shape="rect" coords="500,-1,999,98"
                  href="${pageContext.request.contextPath}/home-living/mix-grind-chop/juicer-mixer-grinder?brand=Philips&phi"/>
      </map>

</div>





