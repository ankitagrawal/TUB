<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Upload Courier Collection">
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.shipment.ParseCourierCollectionChargeExcelAction">
	    <h5 style="color:red;">This is the actual courier collection charges excel upload. Please verify your excel before uploading.</h5> <br/><br/>
      <h2>File to Upload
        <s:file name="fileBean" size="30"/></h2>
      <div class='label'>Email </div>
        <s:text name="email" placeholder="Your Email"/>
	    <br/>
	    Enter your email address to receive the error report
      <div class="buttons">
        <s:submit name="parse" value="Update"/>
      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>