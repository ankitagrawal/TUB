<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('.addRowButton').click(function() {

          var lastIndex = $('.lastRow').attr('count');
          if (!lastIndex) {
            lastIndex = -1;
          }
          $('.lastRow').removeClass('lastRow');

          var nextIndex = eval(lastIndex+"+1");
          var newRowHtml =
              '<tr count="'+nextIndex+'" class="lastRow">'+
              '  <td>'+
              '    <input type="hidden" name="productFeatures['+nextIndex+'].id" />' +
              '    <input type="text" name="productFeatures['+nextIndex+'].name" />' +
              '  </td>'+
              '  <td>'+
              '    <input type="text" name="productFeatures['+nextIndex+'].value" />' +
              '  </td>'+
              '</tr>';

          $('#featureTable').append(newRowHtml);

          return false;
        });
      });
    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <h2>${pa.product.name}</h2>
    <s:form beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction">
      <table border="1" id="featureTable">
        <c:forEach var="feature" items="${pa.productFeatures}" varStatus="ctr">
          <tr count="${ctr.index}" class="${ctr.last ? 'lastRow':''}">
            <td>
              <s:hidden name="productFeatures[${ctr.index}].id"/>
              <s:text name="productFeatures[${ctr.index}].name"/>
            </td>
            <td>
              <s:text name="productFeatures[${ctr.index}].value"/>
            </td>
          </tr>
        </c:forEach>
      </table>
      <br/>
      <a href="#" class="addRowButton">Add new row</a>

      <s:submit name="saveFeatures" value="save"/>

      <s:hidden name="product" value="${pa.product}"/>
    </s:form>
  </s:layout-component>

</s:layout-render>
