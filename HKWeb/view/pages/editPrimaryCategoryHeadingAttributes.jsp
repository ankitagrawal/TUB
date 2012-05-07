<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction" var="ha"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="${ha.heading.name}">

  <s:layout-component name="menu"> </s:layout-component>

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction">
      <fieldset>
        <table>
          <tr>
            <td><label>Heading Name</label></td>
            <td><s:text name="heading.name" style="width: 500px;"/></td>
          </tr>
          <tr>
            <td><label>Link</label></td>
            <td><label style="font-size:medium;"> http://www.healthkart.com</label><s:text name="heading.link"
                                                                                           style="width: 500px;"/>
            </td>
          </tr>
          <tr>
            <td><label>Ranking(If this entry is left blank, the heading wouldn't be displayed)</label></td>
            <td><s:text name="heading.ranking" style="width: 500px;"/></td>
          </tr>
          <tr>
            <td><label>Category</label></td>
            <td><s:text name="heading.category.name" value="${ha.heading.category.name}" readonly="true"
                        style="width: 500px;"/></td>
          </tr>
        </table>
        <s:hidden name="heading.id"/>
        <s:hidden name="heading.products[]"/>
      </fieldset>

      <div class="buttons">
        <s:submit name="savePrimaryCategoryHeadingAttributes" value="Save"/>
        <s:link beanclass="com.hk.web.action.core.catalog.category.CategoryAction" event="editPrimaryCategoryHeadings">
          <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
          <s:param name="category.name" value="${ha.heading.category.name}"/>
        </s:link>
      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>