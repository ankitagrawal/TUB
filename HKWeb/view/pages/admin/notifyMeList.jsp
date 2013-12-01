<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction" var="notifyMeBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Notify Me List Generator">
<s:layout-component name="htmlHead">
    <script type="text/javascript">
        $(document).ready(function () {
            $('#liveSearchBox').keyup(function () {
                var searchString = $(this).val().toLowerCase();
                $('.notifyMeRow').each(function () {
                    if ($(this).find('.primaryCategory').text().toLowerCase().indexOf(searchString) == -1) {
                        $(this).hide();
                    } else {
                        $(this).show();
                    }
                });
            });
        });
    </script>

    <style type="text/css" media="screen">
        table {
            border-collapse: collapse;
            border: 1px solid black;
            float: left;
        }

        th, td {
            border: 1px solid #777777;
            padding-left: 4px;
            padding-right: 4px;
        }

        th {
            background-color: #000000;
            border-bottom: 3px solid #777777;
        }

            /* Display Tag */
            /* START:odd_even */
        tr:nth-child(odd) {
            background-color: #E5F5E5;
        }

        tr:nth-child(even) {
            background-color: #FFFFFF;
        }

        #table_container {
            width: 100%;
            margin: 0 auto;
        }
    </style>
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
</s:layout-component>
<s:layout-component name="heading">Notify Me List For Instock Product</s:layout-component>
<s:layout-component name="content">
    <%--<s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">--%>
        <%--<s:submit name="getNotifyMeProductVariantListInStock" value="Get Product Variant In Stock"/>--%>
        <%--<shiro:hasAnyRoles name="<%=RoleConstants.ADMIN%>">--%>
            <%--<s:submit name="sendEmailToNotifiedUsersForProductVariantInStock" value="Fire Emails for PV In Stock"/>--%>
        <%--</shiro:hasAnyRoles>--%>
    <%--</s:form>--%>
    <br/><br/>

    <h2>Quick Search <i>(by Primary Category)</i>: <input type="text" id="liveSearchBox"/></h2>
    <fieldset>
        <legend>Search/Download Notification List</legend>
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
            <label>Product Variant Id</label><s:text style="width:150px" name="productVariant"/><br/>
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
            <label>Product Deleted:</label>
            <s:select name="productDeleted">
                <s:option value="">--All--</s:option>
                <s:option value="true">True</s:option>
                <s:option value="false">False</s:option>
            </s:select>
            <s:submit name="generateNotifyMeList" value="Download"/>
            <s:submit name="pre" value="Search"/>
        </s:form>
    </fieldset>

    <%--<fieldset>--%>
        <%--<legend>Send Notification Email</legend>--%>
        <%--<s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">--%>
            <%--<label>Start--%>
                <%--Date:</label><s:text class="date_input startDate" style="width:150px"--%>
                                     <%--formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>--%>
            <%--&nbsp; &nbsp;--%>
            <%--<label>End--%>
                <%--Date:</label><s:text class="date_input endDate" style="width:150px"--%>
                                     <%--formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>--%>
            <%--<label>Product Id</label><s:text style="width:150px" name="product"/>--%>
            <%--<label>Product Variant Id</label><s:text style="width:150px" name="productVariant"/>--%>
            <%--<s:submit name="sendMailToNotifiedUsers" value="Send Email" onclick="return confirm('Are you sure?')"/>--%>

        <%--</s:form>--%>
    <%--</fieldset>--%>


    <fieldset>
        <s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">
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
                <s:submit name="sendAllNotifyMailsForAvailableProductVariant" value="SendAllNotifyMailsForInstockProducts"/>
            </shiro:hasPermission>
        </s:form>
    </fieldset>

    <fieldset>
        <legend>Notify Similar Products For Deleted/OOS/Hidden Products</legend>
        <s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">
            <s:submit name="notifyMeListForDeletedHiddenOOSProduct" value="Notify Me List for Similar Product" />
        </s:form>
    </fieldset>


    <c:choose>
        <c:when test="${notifyMeBean.notifyMeList!=null}">
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
                        <th>Notification Request Date</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                    </tr>
                    </thead>

                    <c:forEach items="${notifyMeBean.notifyMeList}" var="notifyMe" varStatus="ctr">
                        <tr class="notifyMeRow">
                            <td>
                                    ${ctr.index+1}.
                            </td>
                            <td>
                                    ${notifyMe.productVariant.id}
                            </td>
                            <td>
                                    ${notifyMe.productVariant.deleted ? "Deleted": "Non-Deleted"}<br/>
                                    ${notifyMe.productVariant.outOfStock ? "Out of Stock": "In-Stock"}<br/>
                                Available unbooked
                                inventory: ${hk:netAvailableUnbookedInventory(notifyMe.productVariant)}
                            </td>
                            <td>
                                <s:link href="/product/${notifyMe.productVariant.product.slug}/${notifyMe.productVariant.product.id}"
                                        target="_blank">
                                    ${notifyMe.productVariant.product.name}
                                    <br/>
                                    ${notifyMe.productVariant.variantName}<br/>
                                </s:link>
                                <em>
                                    <c:forEach items="${notifyMe.productVariant.productOptions}" var="productOption">
                                        ${productOption.name} ${productOption.value}
                                    </c:forEach>
                                </em>
                            </td>
                            <td class="primaryCategory"> ${notifyMe.productVariant.product.primaryCategory}</td>
                            <td>
                                <fmt:formatDate value="${notifyMe.createdDate}" type="both"/>
                            </td>
                            <td>
                                    ${notifyMe.name}
                            </td>
                            <td>
                                    ${notifyMe.email}
                            </td>
                            <td>
                                    ${notifyMe.phone}
                            </td>
                        </tr>
                    </c:forEach>

                </table>
                <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${notifyMeBean}"/>
                <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${notifyMeBean}"/>
            </div>
        </c:when>
        <c:otherwise>
            <s:form beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction">
                <br/><br/><br/>
                There are no more notifications left for PV in stock. All notification emails for PV in stock have been sent.
                <br/> <br/><br/>
                <s:submit name="pre" value="Show Notify Me List"></s:submit>
            </s:form>
        </c:otherwise>
    </c:choose>
</s:layout-component>
</s:layout-render>