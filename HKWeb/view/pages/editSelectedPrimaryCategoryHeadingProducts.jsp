<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction" var="ha"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Selected Category Heading Products">
  <s:layout-component name="content">
    <h2>${ha.heading.name}</h2>
    <s:form beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction">
      <table border="1" id="featureTable">
          <tr><th>ID</th><th>Product</th><th>Sorting</th></tr>       
        <c:forEach var="headingProduct" items="${ha.headingProducts}" varStatus="ctr">
          <tr count="${ctr.index}" class="${ctr.last ? 'lastRow':''}">
              <s:hidden name="products[${ctr.index}]" value="${headingProduct.product.id}"/>
              <td>
                 ${headingProduct.product.id}
            </td>
              <td>
                 <label>${headingProduct.product.name}</label>
            </td>
              <td>
                  <input type="text" class="rank" name="ranks[${ctr.index}]" value="${headingProduct.rank}"/>
              </td>
          </tr>
        </c:forEach>
        <s:hidden name="heading.id" value="${ha.heading.id}"/>
      </table>
      <br/>

      <s:submit name="saveSelectedPrimaryCategoryHeadingProducts" id="save" value="Save"/>

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
      <script type="text/javascript">
          $(document).ready(function(){
             $("#save").click(function(){
                 var bool = true;
                $(".rank").each(function(){
                    var data = $(this).val();
                    data = data.trim(data);
                    if(data.length > 10){
                        alert("Please Enter the length of rank less than 11");
                        bool = false;
                        return false;
                    }
                    else if(data<=0){
                        alert("Rank must be grater than 0");
                        bool = false;
                        return false;
                    }
                });
                 if(!bool){
                     return false;
                 }
             });
          });
      </script>
  </s:layout-component>
</s:layout-render>