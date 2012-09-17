<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.akube.framework.util.BaseUtils" %>
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.HealthkartConstants" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.domain.MapIndia" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.pact.dao.location.MapIndiaDao" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page import="org.stripesstuff.plugin.security.J2EESecurityManager" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<c:set var="redirectParam" value="<%=J2EESecurityManager.redirectAfterLoginParam%>"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CategoryAction" var="categoryBean" event="pre"/>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CatalogAction" var="ca"/>
<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="${categoryBean.seoData.title}">

<c:if test="${categoryBean.category.name == 'services'}">
  <s:layout-render name="/layouts/embed/changePreferredZone.jsp" filterUrlFragment=""/>
  <%
    CategoryDao categoryDao = (CategoryDao)ServiceLocatorFactory.getService(CategoryDao.class);
    Category services = categoryDao.getCategoryByName("services");
    pageContext.setAttribute("services", services);
    
    boolean isSecure = pageContext.getRequest().isSecure();
    pageContext.setAttribute("isSecure", isSecure);
    
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
</c:if>

<s:layout-component name="htmlHead">
  <c:if test="${categoryBean.category.name == 'services'}">
    <script type="text/javascript">
      $(document).ready(function() {

        $('#selectCityWindow').jqm();
        $('#selectCityWindow').jqmShow();

        $('.jqmOverlay').click(function() {
          //        alert("Cliking w/o selecting" + $('#topLevelCategory').val());
          if ($('#topLevelCategory').val() == 'services') {
            //          alert("Inside");
            $.getJSON(
                $('#setDefaultZoneLink').attr('href')
                );
          }
        });

      });
    </script>
  </c:if>
</s:layout-component>

<s:layout-component name="breadcrumbs">

  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">${categoryBean.category}</span>

    <h1 class="title"> ${categoryBean.seoData.h1} </h1>

    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_SEO_METADATA%>">
      <div align="center">
        <s:link beanclass="com.hk.web.action.core.content.seo.SeoAction" event="pre" target="_blank" class="popup">Edit MetaData
          <s:param name="seoData" value="${categoryBean.seoData.id}"/>
        </s:link>
      </div>

    </shiro:hasPermission>

  </div>
</s:layout-component>

<c:if test="${preferredZone == null && categoryBean.category.name == 'services'}">
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
                <s:option value="Bangalore">Bangalore</s:option>
                <s:option value="Chandigarh">Chandigarh</s:option>
                <s:option value="Hyderabad">Hyderabad</s:option>
                <s:option value="Jaipur">Jaipur</s:option>
                <s:option value="Calcutta">Kolkata</s:option>
                <s:option value="Pune">Pune</s:option>
                <s:option value="Ahmedabad">Ahmedabad</s:option>
                <s:option value="All">Rest Of India</s:option>
              </s:select>
            </div>
            <s:submit name="setCookie" value="Select"/>
          </s:form>
        </s:layout-component>
      </s:layout-render>
    </div>
  </s:layout-component>
</c:if>

<s:layout-component name="topCategory">${categoryBean.category.name}</s:layout-component>

<s:layout-component
    name="metaDescription"> ${categoryBean.seoData.metaDescription} </s:layout-component>
<s:layout-component
    name="metaKeywords">${categoryBean.seoData.metaKeyword}</s:layout-component>


<s:layout-component name="mainBanner">
  <div class="promotional">
    <ul id="categoryBanner" class="grid_18">
      <c:forEach var="image" items="${categoryBean.categoryImages}">
        <c:choose>
          <c:when test="${hk:isNotBlank(image.link)}">
            <li>
              <a href="${pageContext.request.contextPath}${image.link}">
                <hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/>
              </a>
            </li>
          </c:when>

          <c:otherwise>
            <li><hk:categoryImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"/></li>
          </c:otherwise>

        </c:choose>
      </c:forEach>
    </ul>
  </div>
  <div class='grid_6'>
    <c:choose>
      <c:when test="${categoryBean.category.name == 'nutrition' || categoryBean.category.name == 'sports'}">
        <a href="${pageContext.request.contextPath}/pages/sheruClassic.jsp"><img src="<hk:vhostImage/>/images/banners/sheru_classic_small.jpg" alt="Sheru Classic 2012"
             class="small_banner"/></a>
      </c:when>
      <c:otherwise>
        <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction" event="pre">
          <img src="<hk:vhostImage/>/images/banners/refer_earn.jpg" alt="refer a friend and earn"
               class="small_banner"/>
        </s:link>
        <img src="<hk:vhostImage/>/images/banners/freeshipping_cod_250.jpg" alt="cash on delivery"
             class="small_banner"/>
      </c:otherwise>
    </c:choose>
  </div>

</s:layout-component>
<s:layout-component name="content">
  <div class="container_24">
    <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
      <div class="grid_24 alpha omega">
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
          <s:param name="category" value="${categoryBean.category.name}"/>
        </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank"
                class="popup">
          <s:param name="category" value="${categoryBean.category.name}"/>
          Manage Images
        </s:link>
      </div>

      <div class="clear"></div>
      <%--</shiro:hasPermission>--%>

      <%--<shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">--%>
      <div class="grid_24 alpha omega">
        <s:link beanclass="com.hk.web.action.core.catalog.category.CategoryAction" event="editPrimaryCategoryHeadings"
                class="popup" style="font-size:larger; background-color:#003399; color:white;" target="_blank">
          Add/Edit Headings for ${categoryBean.category.name} category
          <s:param name="category" value="${categoryBean.category.name}"/>
        </s:link>
      </div>

      <div class="clear"></div>
    </shiro:hasPermission>

    <c:forEach var="heading" items="${categoryBean.headingsWithRankingSetSortedByRanking}">
      <c:if test="${!empty heading.products}">
        <div class="grid_24 alpha omega" style="width: 950px;">
          <c:choose>
            <c:when test="${hk:isNotBlank(heading.link)}">
              <div class="products" style="margin-left: 0;">
                <h2>
                  <a href="${pageContext.request.contextPath}${heading.link}">
                      ${heading.name}<span class='small'>   (view more products)</span>
                  </a>
                </h2>
              </div>
            </c:when>
            <c:otherwise>
              <div class="products"
                   style="margin-left: 0;color: #228; font-size: 1.5em; border-bottom: 1px solid #228;">
                <h2>
                  <strong>
                      ${heading.name}
                  </strong>
                </h2>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="clear"></div>

        <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
          <div class="grid_24 alpha omega" style="width: 950px;">
            <s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction"
                    event="editPrimaryCategoryHeadingProducts"
                    class="popup" style="background-color:#003399; color:white;" target="_blank">
              Add/Edit Products for Heading
              <s:param name="heading.id" value='${heading.id}'/>
            </s:link>
          </div>

          <div class="clear"></div>
        </shiro:hasPermission>

        <div class="grid_24" style="width: 950px;">
          <c:forEach var="product" items='${hk:getCategoryHeadingProductsSortedByOrder(heading.id, categoryBean.category.name)}' begin="0" end="4">
            <div class="grid_5 alpha omega">
              <s:layout-render name="/layouts/embed/_productThumb175.jsp" product='${product}'/>
            </div>
          </c:forEach>
        </div>
        <div class="clear"></div>
        <%--<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>--%>
      </c:if>

      <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
        <c:if test="${empty heading.products}">
          <div class="grid_24 alpha omega" style="width: 950px;">
            <s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction"
                    event="addPrimaryCategoryHeadingProducts"
                    class="popup" style="background-color:#003399; color:white;">
              <strong>ADD PRODUCTS FOR HEADING: ${heading.name} (Heading not visible as no products are set for
                it)</strong>
              <s:param name="heading.id" value='${heading.id}'/>
            </s:link>
          </div>
        </c:if>

        <div class="clear"></div>
      </shiro:hasPermission>

    </c:forEach>
  </div>

  <div class="clear"></div>

<c:if test="${hk:isNotBlank(categoryBean.seoData.description)}">
  <div style="margin-top: 45px; background-color: #FAFCFE; padding: 10px; float: none; clear: both;">
    <h2><i>${categoryBean.seoData.descriptionTitle}</i>: </h2>
      ${categoryBean.seoData.description}
  </div>
</c:if>
		
	<c:choose>
		<c:when test="${not isSecure}">
		</c:when>
		<c:otherwise>
			<iframe
					src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e200&catid=${categoryBean.category.name}&subcat1id=&subcat2id=&section=1&level=1"
					scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
					frameborder="0"></iframe>
		</c:otherwise>
	</c:choose>
		<%--<c:if test="${not isSecure }">
			<iframe
				src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e200&catid=${categoryBean.category.name}&subcat1id=&subcat2id=&section=1&level=1"
				scrolling="no" width="1" height="1" marginheight="0" marginwidth="0"
				frameborder="0"></iframe>
		</c:if>--%>
	</s:layout-component>
</s:layout-render>
