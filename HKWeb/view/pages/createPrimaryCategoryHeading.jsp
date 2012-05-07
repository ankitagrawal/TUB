<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction" var="ha"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="New Heading">

  <s:layout-component name="menu"> </s:layout-component>

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('.buttons').click(function() {
          var data = document.getElementById('link').value;
          if (!(data.isNull())) {
            if (data.search("(http|www)") >= 0) {
              alert("kindly enter the heading link after 'http://www.healthkart.com'");
              return false;
            }
            else if ((data.search("^/"))) {
              alert("kindly add a '/' before the link entered.");
              return false;
            }
            else return true;
          }
          else return true;
        });
      });
    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction">
      <fieldset class="top_label">
        <h2>Create a new Heading</h2>
        <table>
          <tr>
            <td><label>Heading Name</label></td>
            <td><s:text name="heading.name" style="width: 500px;"/></td>
          </tr>
          <tr>
            <td><label>Link</label></td>
            <td><label style="font-size:medium;"> http://www.healthkart.com</label><s:text id="link"
                                                                                           name="heading.link"
                                                                                           style="width: 500px;"/>
            </td>
          </tr>
          <tr>
            <td><label>Ranking(If this entry is left blank, the heading wouldn't be displayed)</label></td>
            <td><s:text name="heading.ranking" style="width: 500px;"/></td>
          </tr>
          <tr>
            <td><label>Category</label></td>
            <td><s:text name="heading.category" style="width:500px;" readonly="true" value='${ha.category.name}'/></td>
          </tr>
        </table>

        <div class="buttons" style="width: 90%;">
          <s:submit name="create" value="Create" style="font-size:1.5saem;"/>
        </div>
        <s:link beanclass="com.hk.web.action.core.catalog.category.CategoryAction" event="pre">
          <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
          <s:param name="category.name" value="${ha.category.name}"/>
        </s:link>
      </fieldset>
    </s:form>
  </s:layout-component>
</s:layout-render>