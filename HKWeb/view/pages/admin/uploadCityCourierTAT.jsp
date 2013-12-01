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
                       <div class="grid_4">
              <li><label>File to Upload</label>
                   <s:file name="fileBean" size="30"/>
                 </li>
                <li>
                    <div class="buttons">
                        <s:submit name="uploadCityExcel" value="Upload"/>
                    </div>
                </li>
                           <li>
                               Excel Format CITY(city name) , COURIER_ID(int) ,CITY_TAT(decimal)

                           </li>
             </div>
             </s:form>
         </ul>
         </fieldset>
         </s:layout-component>
</s:layout-render>