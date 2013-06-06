<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<s:useActionBean beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction" var="notifyMeBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Notify Me List Generator">
    <s:layout-component
            name="heading">Notify Me List Similar Products </s:layout-component>

    <s:layout-component name="content">

        <fieldset>
        <legend>Search Similar Products</legend>
        <s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">
            <s:errors/>
            <br/>
            <label>Start
                Date:</label><s:text class="date_input startDate" style="width:150px"
                                     formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
            &nbsp; &nbsp;
            <label>End
                Date:</label><s:text class="date_input endDate" style="width:150px"
                                     formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
            <label>Product Id</label><s:text style="width:150px" name="product"/>
            <label>Primary Category</label><s:select name="primaryCategory">
            <s:option value="">-ALL-</s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                       value="name" label="displayName"/>
        </s:select>
            <label>Product Out of stock:</label>
            <s:select name="productInStock">
                <s:option value="">--All--</s:option>
                <s:option value="true">True</s:option>
                <s:option value="false">False</s:option>
            </s:select>
            <label>Product Deleted:</label>
            <s:select name="productDeleted">
                <s:option value="true">True</s:option>
                <s:option value="false">False</s:option>
            </s:select>
            <label>Product Hidden:</label>
            <s:select name="productHidden">
                <s:option value="">--All--</s:option>
                <s:option value="true">True</s:option>
                <s:option value="false">False</s:option>
            </s:select>
            <s:submit name="notifyMeListForDeletedHiddenOOSProduct" value="Search"/>

            </fieldset>

            <fieldset>
                <legend>Send All Mails for Similar Product By Above Filled Filter</legend>
                <shiro:hasPermission name="<%=PermissionConstants.NOTIFY_ME_BULK_EMAIL%>">
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
                </shiro:hasPermission>
                <s:submit name="sendAllMailsForDeletedProducts" value="SendMailsForDeletedHiddenOOS"/>
            </fieldset>
        </s:form>


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
                            <th>Similar Product</th>
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
                                    unbooked inventory: ${hk:netAvailableUnbookedInventory(notifyMeDto.productVariant)}
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
                                <td>
                                    <c:set var="similarProductInventoryList"
                                           value="${hk:similarProductWithUnbookedInventory(notifyMeDto.productVariant)}"/>
                                    <c:choose>
                                    <c:when test="${similarProductInventoryList != null && fn:length(similarProductInventoryList) > 1}">
                                        <c:forEach items="${similarProductInventoryList}" var="productInventoryDto">
                                            ${productInventoryDto.product.id} -- ${productInventoryDto.inventory}
                                            <br/>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        No similar products.
                                    </c:otherwise>
                                    </c:choose>
                                </td>
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