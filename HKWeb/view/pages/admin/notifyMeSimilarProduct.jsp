<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:useActionBean beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction" var="notifyMeBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Notify Me List Generator">
    <s:layout-component name="htmlHead">
        <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
        <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    </s:layout-component>

    <s:layout-component
            name="heading">Notify Me List Similar Products </s:layout-component>

    <s:layout-component name="content">

        <c:if test="${notifyMeBean.productOutOfStock != null}">
            <c:choose>
                <c:when test="${notifyMeBean.productOutOfStock}">
                    <c:set var="similarproduct" value="true"/>
                </c:when>
                <c:otherwise>
                    <c:set var="similarproduct" value="false"/>
                </c:otherwise>
            </c:choose>
        </c:if>

        <fieldset>
            <legend> Search Notify Request</legend>
            <s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">
                <s:errors/>
                <br/>
                <label>Product Id</label><s:text style="width:150px" name="product"/>
                <label>Primary Category</label><s:select name="primaryCategory">
                <s:option value="">-ALL-</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                           value="name" label="displayName"/>
            </s:select>
                <label>Product Out of stock:</label>
                <s:select name="productOutOfStock">
                    <s:option value="">--All--</s:option>
                    <s:option value="true">True</s:option>
                    <s:option value="false">False</s:option>
                </s:select>
                <c:if test="${similarproduct != null && similarproduct}">
                <label>Similar Product Available:</label>
                <s:select name="similarProductAvailable">
                    <s:option value="">--All--</s:option>
                    <s:option value="true">True</s:option>
                    <s:option value="false">False</s:option>
                </s:select>
                </c:if>
                <s:submit name="showNotifyMeList" value="Search"/>
            </s:form>
        </fieldset>

        <!--  button for similar products !-->
        <c:if test = "${notifyMeBean.productOutOfStock != null && notifyMeBean.productOutOfStock == true}">
                <s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">
                    <%--<shiro:hasPermission name="<%=PermissionConstants.NOTIFY_ME_BULK_EMAIL%>">--%>
                        <fieldset>
                            <legend> Send All Mails For Similar Products</legend>
                            <ol>
                                <li>
                                    <label>Conversion Rate</label>
                                    <s:text name="conversionRate"/>
                                </li>
                                <li>
                                    <label>Buffer Rate</label>
                                    <s:text name="bufferRate"/>
                                </li>
                            </ol>
                            <s:submit name="sendMailsForSimilarProductsAutomation" value="SendMailsForDeletedHiddenOOS"/>
                        </fieldset>
                    <%--</shiro:hasPermission>--%>
                </s:form>
        </c:if>

            <!--  button for InStock Product !-->
            <c:if test="${notifyMeBean.productOutOfStock != null && notifyMeBean.productOutOfStock == false}">
                <fieldset>
                    <legend>Send All Mails For InStock Products</legend>
                    <s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">
                        <%--<shiro:hasPermission name="<%=PermissionConstants.NOTIFY_ME_BULK_EMAIL%>">--%>
                            <ol>
                                <li>
                                    <label>Conversion Rate</label>
                                    <s:text name="conversionRate"/>
                                </li>
                                <li>
                                    <label>Buffer Rate</label>
                                    <s:text name="bufferRate"/>
                                </li>
                            </ol>
                            <s:submit name="sendMailsForInStockProductsAutomation"
                                      value="SendAllNotifyMailsForInstockProducts"/>
                        <%--</shiro:hasPermission>--%>
                    </s:form>
                </fieldset>
        </c:if>


        <c:choose>
            <c:when test="${notifyMeBean.notifyMeDtoList!=null}">
                <div id="table_container">
                    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${notifyMeBean}"/>
                    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${notifyMeBean}"/>

                    <table width="100%">
                        <thead>
                        <tr>
                            <th>S. No.</th>
                            <th>VariantID</th>
                            <th>Current Stock Status</th>
                            <th>Variant</th>
                            <th>Primary Category</th>
                            <c:if test="${similarproduct}">
                                <th>Similar Product</th>
                            </c:if>
                            <th>Pending Request</th>
                        </tr>
                        </thead>

                        <c:forEach items="${notifyMeBean.notifyMeDtoList}" var="notifyMeDto" varStatus="ctr">
                            <tr class="notifyMeRow">
                                <td>
                                        ${ctr.index+1}.
                                </td>
                                <td>
                                        ${notifyMeDto.productVariant.id}
                                </td>
                                <td>
                                        ${notifyMeDto.productVariant.deleted ? "Deleted": "Non-Deleted"}<br/>
                                        ${notifyMeDto.productVariant.outOfStock ? "Out of Stock": "In-Stock"}<br/>
                                        ${notifyMeDto.productVariant.product.hidden ? "Hidden" : "Not Hidden"}<br/>
                                </td>
                                <td>
                                    <s:link href="/product/${notifyMeDto.productVariant.product.slug}/${notifyMeDto.productVariant.product.id}"
                                            target="_blank">
                                        ${notifyMeDto.productVariant.product.name}
                                        <br/>
                                        ${notifyMeDto.productVariant.variantName}<br/>
                                    </s:link>
                                    <em>
                                        <c:forEach items="${notifyMeDto.productVariant.productOptions}"
                                                   var="productOption">
                                            ${productOption.name} ${productOption.value}
                                        </c:forEach>
                                    </em>
                                </td>
                                <td class="primaryCategory"> ${notifyMeDto.productVariant.product.primaryCategory}</td>
                                <c:if test="${similarproduct != null && similarproduct}">
                                    <td>
                                        <c:set var="similarProductInventoryList"
                                               value="${hk:similarProductWithUnbookedInventory(notifyMeDto.productVariant)}"/>
                                        <c:choose>
                                            <c:when test="${similarProductInventoryList != null && fn:length(similarProductInventoryList) > 0}">
                                                <c:forEach items="${similarProductInventoryList}"
                                                           var="productInventoryDto">
                                                    ${productInventoryDto.product.id} -- ${productInventoryDto.inventory}
                                                    <br/>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <s:link beanclass="com.hk.web.action.admin.inventory.EditSimilarProductsAction">Add Similar Products
                                                    <s:param name="productId"
                                                             value="${notifyMeDto.productVariant.product.id}"/>
                                                </s:link>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:if>
                                <td>
                                        ${notifyMeDto.userCount}
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${notifyMeBean}"/>
                    <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${notifyMeBean}"/>
                </div>
            </c:when>
            <c:otherwise>
                <br/><br/><br/>
                There are no more notifications left for PV which are deleted or hidden or OOS. All notification emails such PV have been sent.
            </c:otherwise>
        </c:choose>


    </s:layout-component>
</s:layout-render>