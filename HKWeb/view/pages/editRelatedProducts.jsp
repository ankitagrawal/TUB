<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction" var="pa"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Recommended Products List">

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
              '    <input type="text" name="relatedProducts['+nextIndex+']" />' +
              '  </td>'+
              '</tr>';

          $('#featureTable').append(newRowHtml);

          return false;
        });
      });
    </script>
  </s:layout-component>
  <s:layout-component name="heading">
    Recommended Products - ${pa.product.name}
  </s:layout-component>
  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.catalog.product.EditProductAttributesAction">
       <table border="1" id="featureTable">
        <c:forEach var="relatedProduct" items="${pa.product.relatedProducts}" varStatus="ctr">
          <tr count="${ctr.index}" class="${ctr.last ? 'lastRow':''}">
            <td>
              <s:text name="relatedProducts[${ctr.index}]" value="${relatedProduct}"/>
            </td>
          </tr>
        </c:forEach>
      </table>
      <br/>
      <a href="#" class="addRowButton">Add new row</a>

      <s:submit name="saveRelatedProducts" value="save"/>

      <s:hidden name="product" value="${pa.product}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>