<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
   <s:useActionBean beanclass="com.hk.web.action.admin.courier.CityCourierTatAction" var="cpea"/>
  <s:layout-component name="heading">
      Upload City's Courier Turn Around Time
    </s:layout-component>
     <s:layout-component name="content">
         <fieldset class="right_label">
        <ul>
         <s:form beanclass="com.hk.web.action.admin.courier.CityCourierTatAction">
             <li>
             <label>Select City To Upload File</label>
              <s:select name="City">
              <s:option>-All-</s:option>
                  <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="cityList" label="name"
                           value="id"></hk:master-data-collection>
              </s:select>
             </li>
             <div class="grid_4">
              <li><label>File to Upload</label>
                   <s:file name="fileBean" size="30"/>
                 </li>
                <li>
            <div class="buttons">
              <s:submit name="" value="Upload"/>
            </div>
          </li>
             </div>
             </s:form>
         </ul>
         </fieldset>
         </s:layout-component>
</s:layout-render>