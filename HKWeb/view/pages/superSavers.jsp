<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.SuperSaversAction" var="comboBean"/>

<s:layout-render name="/layouts/default100.jsp" pageTitle="Super Savers">
    <s:layout-component name="htmlHead">
        <style type="text/css">
            div.heading {
                text-align: center;
                padding: 5px;
                margin: 5px;
            }

            input[type="text"].offerQuery {
                height: 14px;
                font-size: .8em;
                width: 90%;
            }
        </style>
    </s:layout-component>

    <s:layout-component name="content">
        <div class="heading">
            <div style="width:70%; float:left;">
                <h1>Super Savers</h1>
            </div>

            <div style="cursor:default; width: 30%; float:right;">
                <s:form beanclass="com.hk.web.action.core.search.SearchAction" method="get" renderFieldsPresent="false"
                        renderSourcePage="false" autocomplete="off" style="position: relative;">
                    <s:text name="query" id="searchbox" class="input_tip offerQuery" title='search our catalog'
                            value="${param['query']}" placeholder='search our catalog'/>

                    <s:image name="search" src="/images/icons/search2.png"
                             style="position: absolute; right: 3px; top: 1px;"/>
                </s:form>
            </div>
        </div>

        <div class="clear"></div>

        <shiro:hasPermission name="<%=PermissionConstants.UPLOAD_PRODUCT_CATALOG%>">
            <div style="padding:5px;">
                <div class="grid_24 alpha omega">
                    <s:link beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
                        <span>Upload</span>
                    </s:link>
                    &nbsp;|&nbsp;
                    <s:link beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction"
                            event="manageSuperSaverImages">
                        <span>Manage Images</span>
                    </s:link>
                </div>
            </div>

            <div class="clear"></div>
            <div style="margin-top:15px;"></div>
        </shiro:hasPermission>

        <c:forEach items="${comboBean.superSaverImages}" var="image">
            <c:set var="product" value="${image.product}"/>
            <c:set var="productName" value="${product.name}"/>
            <div style="margin:15px 0;">
                <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" class="prod_link"
                        title="${productName}">
                    <s:param name="productId" value="${product.id}"/>
                    <s:param name="productSlug" value="${product.slug}"/>
                    <hk:superSaverImage imageId="${image.id}" size="<%=EnumImageSize.Original%>"
                                        alt="${image.altText}"/>
                </s:link>
            </div>
        </c:forEach>

        <script type="text/javascript">
            $(document).ready(function() {
                $('#super_savers_button').addClass("active");
            });
        </script>
    </s:layout-component>
</s:layout-render>