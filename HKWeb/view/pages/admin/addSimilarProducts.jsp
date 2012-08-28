<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.EditSimilarProductsAction" var="spa"/>
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

                var nextIndex = eval(lastIndex + "+1");
                var newRowHtml =
                        '<tr count="' + nextIndex + '" class="lastRow productRow">' +
                                '<td>' + Math.round(nextIndex + 1) + '.</td>' +
                                '<td></td>' +
                                '  <td>' +
                                '    <input type="text" class="simProduct" name="similarProductsList[' + nextIndex + '].similarProduct"/>' +
                                '  </td>' +
                                '<td class="pvDetails"></td>' +
                                '  <td>' +
                                '    <input type="text" name="similarProductsList[' + nextIndex + '].relationShip" />' +
                                '  </td>' +
                                '</tr>';

                $('#spaTable').append(newRowHtml);

                return false;
            });

            $('.simProduct').live("change", function() {
                var productRow = $(this).parents('.productRow');
                var productId = productRow.find('.simProduct').val();
                var productDetails = productRow.find('.pvDetails');
                $.getJSON(
                        $('#pvInfoLink').attr('href'), {productId: productId},
                        function(res) {
                            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                                productDetails.html(
                                        res.data.product
                                             <%--   + '<br/>' + res.data.options --%>
                                );
                            } else {
                                $('.variantDetails').html('<h2>' + res.message + '</h2>');
                            }
                        }
                );
            });
        });
    </script>
</s:layout-component>

<s:layout-component name="content">
<div style="display: none;">
    <s:link beanclass="com.hk.web.action.admin.inventory.EditSimilarProductsAction" id="pvInfoLink"
            event="getPVDetails"></s:link>
</div>
<s:form beanclass="com.hk.web.action.admin.inventory.EditSimilarProductsAction">
  <h4>Product :</h4>
  <input type="text" name="inputProduct"/>
    <p><p>
  <h4>Add Similar Products</h4>
    <p></p>
  <a href="addSimilarProducts.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>
    <p></p>
  <table border="1">
    <thead>
    <tr>
        <th>S.No.</th>
        <th></th>
        <th>SimilarProduct</th>
        <th>Details</th>
        <th>Relationship</th>
    </tr>
    </thead>
    <tbody id="spaTable">
    <c:forEach var="similarProductItem" items="${spa.similarProductsList}" varStatus="ctr">
       <tr count="${ctr.index}" class="${ctr.last ? 'lastRow productRow':'productRow'}">
            <td>${ctr.index+1}.</td>
            <td></td>
             <td>
                    ${similarProductItem.similarProduct}
             </td>
             <td></td>
             <td>
                     ${similarProductItem.relationShip}
             </td>
       </tr>
    </c:forEach>
    </tbody>
    </table>
        <s:submit name="save" value="Save"/>

    </s:form>
</s:layout-component>
</s:layout-render>

