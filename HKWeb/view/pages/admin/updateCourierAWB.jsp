<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <fieldset class="right_label">
      <legend>Download AWB Excel</legend>
      <ul>
        <s:form beanclass="com.hk.web.action.admin.courier.CourierAWBAction">
          <li><label>Courier AWB numbers to download : </label><s:select name="courier" class="codModeSelect">
            <s:option value="">-Select Courier-</s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers" value="id" label="name"/>
          </s:select></li>
          <li>
	        <li><label>AWB Status : </label>
	        <s:select name="awbStatus">
		        <s:option value="">All Status</s:option>
		        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="allAwbStatus"
		                                   value="id" label="status"/>
	        </s:select></li>
	        <li>
            <div class="buttons">
              <s:submit name="generateCourierAWBExcel" value="Download"/>
            </div>

          </li>
        </s:form>
      </ul>
    </fieldset>

    <fieldset class="right_label">
      <legend>Upload Courier Airway Bill Nos Excel</legend>
      <ul>

        <s:form beanclass="com.hk.web.action.admin.courier.CourierAWBAction">
            <li><label>Select Courier to Upload File: </label><s:select name="courier" class="codModeSelect">
            <s:option value="">-All-</s:option>
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="availableCouriers" value="id" label="name"/>
          </s:select></li>
        <div class="grid_4">
          <li><label>File to Upload</label>
            <s:file name="fileBean" size="30"/>
          </li>
          <li>
              <div class="buttons">
                  <s:submit name="uploadCourierAWBExcel" value="Upload"/>
              </div>
          </li>
            <li>
                Excel in Format COURIER_ID (int) ,AWB_NUMBER (alphanumeric) ,COD (1 if COD , 0 if tech process) ,
                WAREHOUSE (1 for Gurgaon 2, Mumbai)

            </li>
        </div>
        </s:form>
      </ul>
    </fieldset>



	   <fieldset class="right_label">
      <legend style="color:darkblue;font-size:15px;font-weight:bold;">Upload AWB  Excel to Delete</legend>
      <ul>

        <s:form beanclass="com.hk.web.action.admin.courier.CourierAWBAction">

        <div class="grid_4">
          <li><label>File to Upload</label>
            <s:file name="fileBean" size="30"/>
          </li>
          <li>
              <div class="buttons">
                  <s:submit name="uploadCourierAWBExcelForDelete" value="Delete"/>
              </div>
          </li>
            <li>
                Excel in Format COURIER_ID (int) ,AWB_NUMBER (alphanumeric) ,COD (1 if COD , 0 if tech process) ,
                WAREHOUSE (1 for Gurgaon 2, Mumbai)
            </li>
        </div>
        </s:form>
      </ul>
    </fieldset>

  </s:layout-component>

</s:layout-render>
