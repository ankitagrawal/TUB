<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction" var="ha"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Category Heading Products">

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
            alert("Please select the product(s) to be deleted!!");
            return false;
          }
          else {
            var proceed = confirm('Are you sure that you want to delete the product(s) selected?');
            if (proceed) return true;
            else return false;
          }
        });
           $('.editBtn').click(function() {
          var check = 0;
          $('.checkbox').each(function() {
            if ($(this).attr("checked") == "checked") {
              check = 1;
            }
          });
          if (!check) {
            alert("Please select the product(s) to be edited!!");
            return false;
          }
        });
      });
    </script>

  </s:layout-component>

  <s:layout-component name="content">
    <h2>${ha.heading.name}</h2>
    <s:form beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction">
      <table border="1" id="featureTable">
	      <tr>
		      <th>ID</th></th><th>Product</th><th>OOS</th><th>Deleted</th><th>Hidden</th><th>Sorting</th>
	      </tr>
        <c:forEach var="headingProducts" items="${ha.headingProducts}">
          <tr>
            <td>
              <s:checkbox class="checkbox" value="${headingProducts.product.id}" name="products[]"/>${headingProducts.product.id}
            </td>
	          <td>${headingProducts.product.name}</td>
	          <td>${headingProducts.product.outOfStock}</td>
	          <td>${headingProducts.product.deleted}</td>
	          <td>${headingProducts.product.hidden}</td>
	          <td>${headingProducts.rank}</td>
          </tr>
            <s:hidden name="productId" value="${headingProducts.product.id}"/>
        </c:forEach>
        <s:hidden name="heading.id" value="${ha.heading.id}"/>
      </table>

      <br/><br/>

      <div align="center">
          <s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction" event="addPrimaryCategoryHeadingProducts" class="addBtn button_orange">Add
              <s:param name="heading.id" value="${ha.heading.id}"/>
          </s:link>
        <%--<s:submit name="addPrimaryCategoryHeadingProducts" value="Add New" class="addBtn"/>--%>
        <s:submit name="editSelectedPrimaryCategoryHeadingProducts" value="Edit" class="editBtn"/>
           <%--<s:link beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction" event="editSelectedPrimaryCategoryHeadingProducts" class="editBtn button_orange">Edit--%>
              <%--<s:param name="heading.id" value="${ha.heading.id}"/>--%>
          <%--</s:link>--%>
          <s:submit name="deleteSelectedPrimaryCategoryHeadingProducts" value="Delete" class="deleteBtn"/>
      </div>
      <c:choose>
        <c:when test="${ha.heading.category.name != 'home'}">
          <s:link beanclass="com.hk.web.action.core.catalog.category.CategoryAction" event="pre">
            <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
            <s:param name="category.name" value="${ha.heading.category.name}"/>
          </s:link>
        </c:when>

        <c:otherwise>
          <s:link beanclass="com.hk.web.action.HomeAction" event="pre">
            <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
            <s:param name="category.name" value="${ha.heading.category.name}"/>
          </s:link>
        </c:otherwise>
      </c:choose>

    </s:form>
  </s:layout-component>
</s:layout-render>