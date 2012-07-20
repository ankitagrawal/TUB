<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/15/12
  Time: 5:58 PM
--%>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.subscription.CreateEditSubscriptionProductAction" var="cspa"    />
<s:layout-render name="/layouts/defaultAdmin.jsp">

    <s:layout-component name="content">


        <s:form beanclass="com.hk.web.action.admin.catalog.subscription.CreateEditSubscriptionProductAction">

            <fieldset class="right_label">
                <legend>Create a new Subscription Product</legend>
                <c:if test="${cspa.editSubscription}">
                    <s:hidden name="subscriptionProduct.id"/>
                </c:if>

                <ul class="variantList">
                    <li>
                        <label>Product ID</label>
                        <s:text name="product" />  (eg: NUT130) <s:submit name="editSubscriptionProduct" value="edit" />
                    </li>
                    <li>
                        <label>Max Qty Per Delivery</label>
                        <s:text name="subscriptionProduct.maxQtyPerDelivery"  />
                    </li>
                    <li>
                        <label>Min Frequency(days)</label>
                        <s:text name="subscriptionProduct.minFrequencyDays"  />
                    </li>
                    <li>
                        <label>Max Frequency(days)</label>
                        <s:text name="subscriptionProduct.maxFrequencyDays" />
                    </li>
                    <li>
                        <label>Discount Percent 180 days</label>
                        <s:text name="subscriptionProduct.subscriptionDiscount180Days"  />   (eg: 2.65 for 2.65%)
                    </li>

                    <li>
                        <label>Discount Percent 360 days</label>
                        <s:text name="subscriptionProduct.subscriptionDiscount360Days"  />   (eg: 2.65 for 2.65%)
                    </li>
                    <li>
                        <label>is Subscribable</label>
                        <s:checkbox name="subscribable"/> &nbsp;&nbsp;
                        <c:if test="${cspa.editSubscription}">
                            <s:submit name="saveSubscriptionProduct" value="save"/>
                        </c:if>
                        <c:if test="${!cspa.editSubscription}">
                            <s:submit name="createSubscriptionProduct" value="create"/>
                        </c:if>

                    </li>
                </ul>
            </fieldset>
        </s:form>

        <s:form beanclass="com.hk.web.action.admin.catalog.subscription.CreateEditSubscriptionProductAction">
            <fieldset class="right_label">
                <legend>Upload Subscription Products Excel</legend>

                <div>
                    <ul>
                        <li><label>File to Upload</label>
                            <s:file name="fileBean" size="30"/>
                            &nbsp; <s:submit name="uploadSubscriptionProductsExcel" value="Upload"/>
                        </li>
                        <li>
                    </ul>
                    <p>
                        <strong>Worksheet Name:</strong> subscriptionProducts<br/>
                        <strong>7 Fields:</strong> productId &nbsp; maxQtyPerDelivery &nbsp; minFrequencyDays &nbsp; maxFrequencyDays &nbsp;DiscountPercent180Days &nbsp; DiscountPercent360Days &nbsp; isSubscribable
                    </p>
                </div>
            </fieldset>
        </s:form>

        <s:form beanclass="com.hk.web.action.admin.catalog.subscription.CreateEditSubscriptionProductAction">

            <fieldset class="right_label">
                <legend>Download Subscription Products Excel</legend>
                <br/>

                <div>
                    <ul>
                        <li>
                            <label>Category</label>
                            <s:select name="category">
                                <s:option value="">--select category--</s:option>
                                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="topLevelCategoryList"
                                                           value="name" label="displayName"/>
                            </s:select>
                            <s:submit name="downloadSubscriptionProductsExcel" style="padding:1px;">
                                Download Subscription Products Excel
                            </s:submit>

                        </li>
                    </ul>

                </div>
            </fieldset>
        </s:form>

    </s:layout-component>
</s:layout-render>