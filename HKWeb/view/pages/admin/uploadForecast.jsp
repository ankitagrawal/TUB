<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.ForecastExcelAction" var="excelBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.inventory.ForecastExcelAction">
      <h2>File to Upload
        <s:file name="fileBean" size="30"/></h2>

      <div class="buttons">
        <s:submit name="parse" value="Upload Excel"/>
      </div>
        <li>
           Excel Format: <br>
           Forecast Date (date e.g. 11-04-2012) , Product Variant (String id) ,Warehouse ID (int), Forecast Value (decimal)
        </li>  

    </s:form>

  </s:layout-component>

</s:layout-render>