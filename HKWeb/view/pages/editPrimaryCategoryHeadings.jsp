<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.category.CategoryAction" var="ca"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Edit Category Headings">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('.deleteBtn').click(function() {
          var check = 0;
          $('.radio').each(function() {
            if ($(this).attr("checked") == "checked") {
              check = 1;
            }
          });
          if (!check) {
            alert("Please select the heading to be deleted!");
            return false;
          }
          else {
            var proceed = confirm('Are you sure that you want to delete the heading selected?');
            if (proceed) return true;
            else return false;
          }
        });

        $('.editBtn').click(function() {
          var check = 0;
          $('.radio').each(function() {
            if ($(this).attr("checked") == "checked") {
              check = 1;
            }
          });
          if (check) {
            return true;
          }
          else {
            alert("Please select the heading to be edited!");
            return false;
          }
        });
      });
    </script>
  </s:layout-component>

  <s:layout-component name="content">
    <center><h2>CATEGORY:${ca.category.name}</h2></center>
    <s:form beanclass="com.hk.web.action.core.catalog.category.PrimaryCategoryHeadingAction">
      <table>
        <tr>
          <th></th>
          <th>DISPLAY NAME</th>
          <th>RANKING</th>
        </tr>
        <c:forEach var="heading" items="${ca.headingsSortedByRanking}" varStatus="ctr">
          <tr>
            <c:if test="${heading.ranking != null}">
              <td>
                <s:radio class="radio" value="${heading.id}" name="heading.id"/>
              </td>
              <td style="font-size:medium;">
                  ${heading.name}
              </td>
              <td style="font-size:medium;">
                <c:choose>
                  <c:when test="${heading.ranking != null}">
                    ${heading.ranking}
                  </c:when>
                  <c:otherwise>
                    not set yet
                  </c:otherwise>
                </c:choose>
              </td>
            </c:if>
          </tr>
        </c:forEach>
        <c:forEach var="heading" items="${ca.headingsSortedByRanking}" varStatus="ctr">
          <tr>
            <c:if test="${heading.ranking == null}">
              <td>
                <s:radio class="radio" value="${heading.id}" name="heading.id"/>
              </td>
              <td style="font-size:medium;">
                  ${heading.name}
              </td>
              <td style="font-size:medium;">
                not set yet
              </td>
            </c:if>
          </tr>
        </c:forEach>
      </table>
      <s:hidden name="category" value="${ca.category.name}"/>

      <div class="buttons">
        <s:submit name="addPrimaryCategoryHeading" value="Add"/>
        <s:submit name="editPrimaryCategoryHeading" value="Edit" class="editBtn"/>
        <s:submit name="deletePrimaryCategoryHeading" value="Delete" class="deleteBtn"/>

        <c:choose>
          <c:when test="${ca.category.name != 'home'}">
            <s:link beanclass="com.hk.web.action.core.catalog.category.CategoryAction" event="pre">
              <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
              <s:param name="category.name" value="${ca.category.name}"/>
            </s:link>
          </c:when>

          <c:otherwise>
            <s:link beanclass="com.hk.web.action.HomeAction" event="pre">
              <div align="right" style="font-weight:bold; font-size:150%">BACK</div>
              <%--<s:param name="category.name" value="${ca.category.name}"/>--%>
            </s:link>
          </c:otherwise>
        </c:choose>

      </div>
    </s:form>
  </s:layout-component>
</s:layout-render>