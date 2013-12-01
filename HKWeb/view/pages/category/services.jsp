<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="net.sourceforge.stripes.util.CryptoUtil" %>
<%@ page import="com.hk.pact.dao.location.MapIndiaDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="com.hk.domain.MapIndia" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.ServiceAction" var="sa" event="pre"/>
<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Services | Buy Services Online in India">

  <%
    MapIndiaDao mapIndiaDao = (MapIndiaDao)ServiceLocatorFactory.getService(MapIndiaDao.class);
    Cookie preferredZoneCookie = BaseUtils.getCookie(WebContext.getRequest(), HealthkartConstants.Cookie.preferredZone);
    if (preferredZoneCookie != null && preferredZoneCookie.getValue() != null) {
      MapIndia mapIndia = mapIndiaDao.findByCity(preferredZoneCookie.getValue());
      List<MapIndia> targetCitiesList = mapIndiaDao.getTargetCitiesList();
      pageContext.setAttribute("targetCitiesList", preferredZoneCookie.getValue());
        if (preferredZoneCookie.getValue().equals("All") || (mapIndia != null && mapIndia.isTargetCity() != null && mapIndia.isTargetCity())) {
        pageContext.setAttribute("preferredZone", preferredZoneCookie.getValue());
      }
    }
  %>
  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('#selectCityWindow').jqm();
        $('#selectCityWindow').jqmShow();
      });
    </script>
  </s:layout-component>

  <s:layout-component name="breadcrumbs">
    <div class='crumb_outer'>
      <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
      <span class="crumb last" style="font-size: 12px;">Services</span>
      &gt;
      <h1 class="title">
        Services from Thyrocare, Apollo, Religare and other popular health services
      </h1>
    </div>
  </s:layout-component>

  <s:layout-component name="mainBanner">
    <div class="promotional">
      <ul id="categoryBanner" class="grid_18">
        <li>
          <div>
            <a href="${pageContext.request.contextPath}/services/health-checkups">
              <img src="<hk:vhostImage/>/images/banners/Services/banner_services1.jpg" alt="Popular Health Services"/>
            </a>
          </div>
        </li>
          <li>
            <div>
              <a href="${pageContext.request.contextPath}/services/gyms-health-centers">
                <img src="<hk:vhostImage/>/images/banners/Services/banner_services_wm.jpg" alt="Gym Health Centers "/>
              </a>
            </div>
          </li>
          <li>
            <div>
              <a href="${pageContext.request.contextPath}/services/hair-spa-beauty">
                <img src="<hk:vhostImage/>/images/banners/Services/banner_services_spa.jpg" alt="Hair Spa Beauty"/>
              </a>
            </div>
          </li>
      </ul>

      <div class='grid_6'>
        <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction" event="pre">
          <img src="<hk:vhostImage/>/images/banners/refer_earn.jpg" alt="refer a friend and earn" class="small_banner"/>
        </s:link>
        <img src="<hk:vhostImage/>/images/banners/freeshipping_cod_250.jpg" alt="cash on delivery" class="small_banner"/>
      </div>
      <div class="floatfix"></div>
    </div>
  </s:layout-component>


  <c:if test="${preferredZone == null}">
    <s:layout-component name="modal">
      <div class="jqmWindow" style="width:700px" id="selectCityWindow">
        <s:layout-render name="/layouts/modal.jsp">
          <s:layout-component name="heading">Select City</s:layout-component>
          <s:layout-component name="content">
            <s:form beanclass="com.hk.web.action.core.catalog.category.ServiceAction">
              <s:errors/>
              <div class="round-cont" style="width:650px;margin-top: 20px;">
                <label>To find the relevant deals, Please select your city</label>
                <s:select name="preferredZone">
                  <s:option value="Delhi">Delhi-NCR</s:option>
                  <s:option value="Mumbai">Mumbai</s:option>
                  <s:option value="Chennai">Chennai</s:option>
                  <s:option value="Kolkatta">Calcutta</s:option>
                  <s:option value="Chandigarh">Chandigarh</s:option>
                  <s:option value="Bangalore">Bangalore</s:option>
                  <s:option value="Hyderabad">Hyderabad</s:option>
                  <s:option value="Chennai">Chennai</s:option>
                  <s:option value="Jaipur">Jaipur</s:option>
                  <s:option value="Calcutta">Kolkata</s:option>
                  <s:option value="Pune">Pune</s:option>
                  <s:option value="Ahmedabad">Ahmedabad</s:option>
                  <s:option value="All">Rest Of India</s:option>
                </s:select>
              </div>
              <s:submit name="setCookie" value="Select"/>
              <s:hidden name="redirectUrl" value="${sa.redirectUrl!=null?sa.redirectUrl:param[redirectParam]}"/>
            </s:form>
          </s:layout-component>
        </s:layout-render>
      </div>
    </s:layout-component>
  </c:if>

  <s:layout-component name="topCategory">services</s:layout-component>

  <s:layout-component name="product_rows">
    <div class="products">
      <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
        <br/>
        <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
          <s:param name="category" value="services"/>
        </s:link>
          &nbsp;|&nbsp;
          <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
            <s:param name="category" value="services"/>
            Manage Images
          </s:link>
        </div>
      </shiro:hasPermission>
      <h2>
        <a href='${pageContext.request.contextPath}/services/health-checkups'>
          Top selling Services
        <span class='small'>
          (view more products)
        </span>
        </a>
      </h2>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SER001"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SER003"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SER002"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SER008"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SER010"/>
      <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SER006"/>
      <a class='more' href='${pageContext.request.contextPath}/services/pathalogy'>
        view more products &rarr;
      </a>
    </div>
  </s:layout-component>

</s:layout-render>