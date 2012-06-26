<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: Jun 19, 2012
  Time: 11:25:30 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Upload Customer score">
  <s:useActionBean beanclass="com.hk.web.action.admin.store.StorePricingAction" var="storePricing" event="pre"/>
  <s:layout-component name="heading">
    Upload/Download/Update Store pricing
  </s:layout-component>
  <s:layout-component name="content">


    <s:form beanclass="com.hk.web.action.admin.store.StorePricingAction">
      <fieldset class="right_label">
        <legend>Upload Store Pricing Excel</legend>

        <div>
          <ul>
            <li>
              <label>Store</label>
              <s:select name="store">
                <s:option value="">--select store--</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="storeList"
                                           value="id" label="name"/>
              </s:select>
            </li>
            <li><label>File to Upload</label>
              <s:file name="fileBean" size="30"/>
              &nbsp; <s:submit name="uploadStorePricingExcel" value="Upload"/>
            </li>
            <li>
          </ul>
          <p>
            <strong>Worksheet Name:</strong> storePricing<br/>
            <strong>3 Fields:</strong> variantId &nbsp; storePrice &nbsp; hidden
          </p>
        </div>
      </fieldset>
    </s:form>

    <s:form beanclass="com.hk.web.action.admin.store.StorePricingAction">

      <fieldset class="right_label">
        <legend>Download Store Pricing Excel</legend>
        <br/>

        <div>
          <ul>
            <li>
              <label>Store</label>
              <s:select name="store">
                <s:option value="">--select store--</s:option>
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="storeList"
                                           value="id" label="name"/>
              </s:select>
            </li>
          </ul>
          <s:submit name="downloadStorePricingExcel" style="padding:1px;">
            Download Pricing Excel
          </s:submit>

          <br/>

        </div>
      </fieldset>
    </s:form>


  </s:layout-component>
</s:layout-render>