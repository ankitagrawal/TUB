<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: May 23, 2012
  Time: 11:31:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Upload Customer score">
	<s:useActionBean beanclass="com.hk.web.action.admin.clm.CustomerScoreAction" var="csaBean"/>
	<s:layout-component name="heading">
		Upload/Update Customer score
	</s:layout-component>
	<s:layout-component name="content">
       <fieldset class="right_label">
    	   <legend>Upload Customer Score Excel</legend>
         <ul>
        <s:form beanclass="com.hk.web.action.admin.clm.CustomerScoreAction">
          <br/>
				<div >
					<li><label>File to Upload</label>
						<s:file name="fileBean" size="30"/>
					</li>
					<li>

							<s:submit name="uploadScoreExcel" value="Upload"/>
            <br/>
             (Worksheet Name: CustomerScore &nbsp&nbsp&nbsp 2 Fields: userId &nbsp score)</li>
                </div>
					</s:form>
			</ul>
          </fieldset>
        <fieldset class="right_label">
            <legend>Upload Customer Category Score Excel</legend>
            <ul>
                <s:form beanclass="com.hk.web.action.admin.clm.CustomerScoreAction">
                    <br/>
                    <div >
                        <li><label>File to Upload</label>
                            <s:file name="categoryFileBean" size="30"/>
                        </li>
                        <li>

                            <s:submit name="uploadCategoryScoreExcel" value="Upload"/>
                            <br/>
                            (Worksheet Name: CustomerScore &nbsp&nbsp&nbsp  Fields: userId, Baby, Beauty, Diabetes, Eye, Home Devices, Nutrition, Personal Care, Services, Sports scores)</li>
                    </div>
                </s:form>
            </ul>
        </fieldset>


	</s:layout-component>
</s:layout-render>