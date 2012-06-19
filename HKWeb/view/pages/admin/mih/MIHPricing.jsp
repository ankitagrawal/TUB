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
	<s:useActionBean beanclass="com.hk.web.action.admin.mih.MIHPricingAction" var="mihBean"/>
	<s:layout-component name="heading">
		Upload/Download/Update MIH pricing
	</s:layout-component>
	<s:layout-component name="content">
       <fieldset class="right_label">
    	   <legend>Upload/Download MIH Pricing Excel</legend>
         <ul>
        <s:form beanclass="com.hk.web.action.admin.mih.MIHPricingAction">
          <br/>
				<div >
					<li><label>File to Upload</label>
						<s:file name="fileBean" size="30"/>
					</li>
					<li>

							<s:submit name="uploadMIHPricingExcel" value="Upload"/>
            <br/>
             (Worksheet Name: MIHPricing &nbsp&nbsp&nbsp 2 Fields: productVariantID &nbsp mihPrice)</li>
                </div>
					</s:form>

          <s:form beanclass="com.hk.web.action.admin.mih.MIHPricingAction">
          <br/>
				<div >
					<li><s:submit name="downloadMIHPricingExcel">
                Download Pricing Excel
              </s:submit>
					</li>
					<li>

            <br/>

                </div>
					</s:form>
			</ul>
          </fieldset>


	</s:layout-component>
</s:layout-render>