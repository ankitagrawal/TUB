<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="web.action.PrimaryCategoryHeadingAction" var="ha"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Delete Category Heading Products">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('.deleteBtn').click(function() {
          var check = 0;
          $('.checkbox').each(function() {
            if ($(this).attr("checked") == "checked") {
              check = 1;
            }
          });
          if (!check) {
            alert("Please select the product(s) to be deleted!");
            return false;
          }
          else {
            var proceed = confirm('Are you sure that you want to delete the product(s) selected?');
            if (proceed) return true;
            else return false;
          }
        });
      });
    </script>

  </s:layout-component>

  <s:layout-component name="content">
    <h2>${ha.heading.name}</h2>
    <s:form beanclass="web.action.PrimaryCategoryHeadingAction">
      <table border="1" id="featureTable">
        <c:forEach var="product" items="${ha.heading.products}">
          <tr>
            <td>
              <s:checkbox class="checkbox" value="${product.id}" name="products[]"/>${product.id}
            </td>
          </tr>
        </c:forEach>
        <s:hidden name="heading.id" value="${ha.heading.id}"/>
      </table>

      <br/><br/>

      <div align="center">
        <s:submit name="addPrimaryCategoryHeadingProducts" value="Add New" class="addBtn"/>
        <s:submit name="deleteSelectedPrimaryCategoryHeadingProducts" value="Delete" class="deleteBtn"/>
      </div>
      <c:choose>
        <c:when test="${ha.heading.category.name != 'home'}">
          <s:link beanclass="web.action.CategoryAction" event="pre">
            <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
            <s:param name="category.name" value="${ha.heading.category.name}"/>
          </s:link>
        </c:when>

        <c:otherwise>
          <s:link beanclass="web.action.HomeAction" event="pre">
            <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
            <s:param name="category.name" value="${ha.heading.category.name}"/>
          </s:link>
        </c:otherwise>
      </c:choose>

    </s:form>
  </s:layout-component>
</s:layout-render>