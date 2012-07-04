<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.CreateOrSelectProductAction" var="csp"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
  </s:layout-component>

  <s:layout-component name="content">
    <link href="${pageContext.request.contextPath}/ckeditor/_samples/sample.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
    <script src="${pageContext.request.contextPath}/ckeditor/_samples/sample.js" type="text/javascript"></script>

    <s:form beanclass="com.hk.web.action.admin.catalog.product.CreateOrSelectProductAction">
      <fieldset class="right_label">
        <h2>Select an existing Product</h2>
        <ul>
          <li>
            <label>Product Id</label>
            <s:text name="product"/>
          </li>
          <li>
            <label>&nbsp;</label>

            <div class="buttons">
              <s:submit name="select" value="Select"/>
            </div>
          </li>
        </ul>
      </fieldset>
    </s:form>
    <s:form beanclass="com.hk.web.action.admin.catalog.product.CreateOrSelectProductAction">
      <fieldset class="top_label">
        <h2>Create a new Product</h2>
        <ul>
          <li>
            <label>Product Id</label>
            <s:text name="product.id"/>
          </li>
          <li>
            <label>Product Name</label>
            <s:text name="product.name"/>
          </li>
          <li>
            <label>Sorting</label>
            <s:text name="product.orderRanking"/>
          </li>
          <li>
            <label>Brand</label>
            <s:text name="product.brand"/>
          </li>
          <li>
            <label>Manufacturer</label>
            <s:select name="product.manufacturer">
              <s:option></s:option>
              <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="manufacturerList"
                                         label="name" value="id"/>
            </s:select>
          </li>
          <li>
            <label>New Manufacturer</label>
            <br/>
            Name ;
            <s:text name="manufacturer.name"/>
            Website :
            <s:text name="manufacturer.website"/>
            Description :
            <s:text name="manufacturer.description"/>
          </li>
          <li>
            <label>Categories</label>
            <s:textarea name="categories"/>
          </li>
          <li>
            <label>Primary Category</label>
            <s:text name="product.primaryCategory"/>
          </li>
          <li>
            <label>Secondary Category</label>
            <s:text name="secondaryCategory"/>
          </li>
          <li>
            <h3>OverView</h3>
            <s:textarea name="product.overview" id="overview"/>
            <script type="text/javascript">
              CKEDITOR.replace('overview',
              {
                fullPage:true,
                extraPlugins:'docprops'
              });
            </script>
          </li>
          <li>
            <h3>Description</h3>
            <s:textarea name="product.description" id="description"/>
            <script type="text/javascript">
              //<![CDATA[
              CKEDITOR.replace('description',
              {
                fullPage : false,
                extraPlugins : 'docprops'
              });

              //]]>
            </script>
          </li>

          <li>
            <label>&nbsp;</label>

            <div class="buttons" style="width: 90%;" >
              <s:submit name="create" value="Create" style="font-size: 1.5em;"/>
            </div>
          </li>
        </ul>
      </fieldset>

    </s:form>
  </s:layout-component>
</s:layout-render>
