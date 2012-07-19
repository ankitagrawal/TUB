<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.SuperSaversAction" var="comboBean"/>

<s:layout-render name="/layouts/default100.jsp" pageTitle="Super Savers">
    <s:layout-component name="htmlHead">
        <script type="text/javascript">
            $(document).ready(function() {
                $('#super_savers_button').addClass("active");
            });
        </script>
    </s:layout-component>

    <s:layout-component name="content">
        <h1>Super Savers</h1>

        <fieldset>
            <legend>Upload/Edit Banners</legend>
            <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
                <div class="grid_24 alpha omega">
                    <s:link beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
                        Upload
                    </s:link>
                    &nbsp;|&nbsp;
                    <s:link beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction"
                            event="manageSuperSaverImages">
                        Manage Images
                    </s:link>
                </div>
            </shiro:hasPermission>
        </fieldset>

        <div class="clear"></div>
        <div style="margin-top:15px;"></div>

        <c:forEach items="${comboBean.superSaverImages}" var="image">
            <c:set var="product" value="${image.product}"/>
            <c:set var="productName" value="${product.name}"/>
            <div>
                <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" class="prod_link"
                        title="${productName}">
                    <s:param name="productId" value="${product.id}"/>
                    <s:param name="productSlug" value="${product.slug}"/>
                    <s:param name="imageId" value="${image.id}"/>
                    <c:choose>
                        <c:when test="${product.mainImageId != null}">
                            <hk:superSaverImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"
                                                alt="${productName}"/>
                        </c:when>
                        <c:otherwise>
                            <img src='<hk:vhostImage/>/images/banners/Combo/${product.id}.jpg' alt="${productName}"/>
                        </c:otherwise>
                    </c:choose>
                </s:link>
            </div>
        </c:forEach>
    </s:layout-component>
</s:layout-render>