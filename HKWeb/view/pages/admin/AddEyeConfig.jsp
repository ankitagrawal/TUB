<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.admin.pact.service.inventory.AdminInventoryService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Variant config tasks">
    <s:layout-component name="heading">
        Run Variant Config
    </s:layout-component>

    <s:layout-component name="content">
        <%
           AdminInventoryService adminInventoryService= ServiceLocatorFactory.getService(AdminInventoryService.class);
            pageContext.setAttribute("confList",adminInventoryService.getAllVariantConfig());
         %>

      	<s:form beanclass="com.hk.web.action.admin.catalog.product.AddEyeConfigAction" focus="">
         <s:errors/>
         <s:messages/>  
        <table>
            <tr>
                <td>Product Variant</td>
                <td><s:text  name="productVariantList" style="width:100px"/></td>
                <td>
                    <s:select name="configId">
                    <s:option value="0">-select- </s:option>
                      <c:forEach items="${confList}" var = "conf">
                          <s:option value ="${conf.id}">${conf.name}</s:option>
                      </c:forEach>                    
                    </s:select>
                </td>
                <td>
                    <s:submit class="requiredFieldValidator" name="save" value="Save"/>
                </td>

            </tr>

        </table>
        </s:form>
    </s:layout-component>
</s:layout-render>