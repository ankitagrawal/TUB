<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="web.action.PrimaryCategoryHeadingAction" var="ha"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add Category Heading Products">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      var nextIndex = 0;
      $(document).ready(function() {
        $('.addRowButton').click(function() {

          var lastIndex = $('.lastRow').attr('count');
          if (!lastIndex) {
            lastIndex = -1;
          }
          $('.lastRow').removeClass('lastRow');

          var limitIndex = eval(lastIndex + "+1");

            if (limitIndex >= 6)
            {
              alert("Product display limit exceeded!!");
              $(".addRowButton").hide();
              return false;
            }

          var newRowHtml =
              '<tr count="' + limitIndex + '" class="lastRow">' +
              '  <td>' +                                            
              '    <input type="text" name="products[' + nextIndex + ']" />' +
              '  </td>' +
              '</tr>';

          $('#featureTable').append(newRowHtml);

          nextIndex = eval(nextIndex + "+1");

          return false;
        });
      });
    </script>


  </s:layout-component>

  <s:layout-component name="content">
    <h2>${ha.heading.name}</h2>
    <s:form beanclass="web.action.PrimaryCategoryHeadingAction">
      <table border="1" id="featureTable">
        <c:forEach var="product" items="${ha.heading.products}" varStatus="ctr">
          <tr count="${ctr.index}" class="${ctr.last ? 'lastRow':''}">
            <td>
                 <label>${product.id}</label>
            </td>
          </tr>
        </c:forEach>
        <s:hidden name="heading.id" value="${ha.heading.id}"/>
      </table>
      <br/>

      <div style="font-weight:bold; font-size:150%">
        <a href="#" class="addRowButton">Add new product</a>
      </div>
      <br/><br/>

      <s:submit name="savePrimaryCategoryHeadingProducts" value="ADD"/>

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
  </s:layout-component></s:layout-render>