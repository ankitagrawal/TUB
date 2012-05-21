<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.hk.domain.catalog.product.ProductVariant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Assign Bin">
    <s:layout-component name="content">
<c:set var="isSuccess" value="false"/>
      <script >
          function()
          {
              $('#Sucess_message').show();
          }
      </script>
<c:if test="${isSuccess}">
      <div id="Sucess_message" style="display:none;">

         Bin Has been assigned to Product !!

      </div>
     </c:if>

        <div class="binForm" >
            <s:form beanclass="com.hk.web.action.admin.queue.JobCartAction"  id="binwindow" var="job">
                <div style="text-align:left; padding: 5px 0 5px 0; font-size: 1em;">
                    Aisle:<span class='aster' title="this field is required">*</span>
                    <s:text  name="bin.aisle"/><br/><br/>
                    Rack:<span class='aster' title="this field is required">*</span>
                    <s:text  name="bin.rack"/><br/><br/>
                    Shelf:<span class='aster' title="this field is required">*</span>
                    <s:text  name="bin.shelf"/><br/><br/>
                   Bin:<span class='aster' title="this field is required">*</span>
                    <s:text  name="bin.bin"/><br/><br/>
                </div>
                <% String productvariant=request.getParameter("productVariant");
                String grnn=request.getParameter("grn");
                String SkuGroup=request.getParameter("skuGroupField"); %>
                <s:hidden name="productVariant" value="${productvariant}"/>
                <s:hidden name="grn" value="${grnn}"/>
                <s:hidden name="skuGroupField" value="SkuGroup"/>
                <s:submit  name="addBinForNewProdcutVariant" value="Save" class="submit_Button"  />
                            </s:form>

        </div>

          </s:layout-component>
</s:layout-render>

