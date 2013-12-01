<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" var="ua"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="content">
    <h2>
        ${ua.category.displayName}
    </h2>

    <s:form beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction">
      <table>
        <tr>
          <br>
          <br>
            ${ua.errorMessage}
        </tr>
        <tr>
          <th>
            Image
          </th>
          <th>
            Hide
          </th>
          <th>
            Rank
          </th>
          <th>
            Link
          </th>
          <th>
            Position
          </th>
        </tr>

        <tr>
          <td colspan="5" style="text-align:left;font-size:1.2em;font-style:italic;font-weight:bold;">LEFT BANNERS</td>
        </tr>
        <tr>
          <td colspan="5"></td>
        </tr>

        <c:forEach var="categoryImage" items="${ua.categoryImages}" varStatus="ctr">
          <c:if test="${fn:toLowerCase(categoryImage.position) eq('left')}">
            <tr style="border:1px">

              <td>
                <hk:categoryImage imageId="${categoryImage.id}" size="<%=EnumImageSize.SmallSize%>"/>
              </td>
              <td>
                <s:checkbox name="categoryImages[${ctr.index}].hidden"/>
              </td>
              <td>
                <s:text name="categoryImages[${ctr.index}].ranking"/>
              </td>
              <td>
                http://www.healthkart.com <s:text name="categoryImages[${ctr.index}].link"/>
              </td>
              <td>
                <s:select name="categoryImages[${ctr.index}].position">
                  <s:option value="">-None-</s:option>
                  <s:option value="left">Left</s:option>
                  <s:option value="center">Center</s:option>
                  <s:option value="right">Right</s:option>
                </s:select></td>
              <s:hidden name="categoryImages[${ctr.index}]"/>
            </tr>
          </c:if>
        </c:forEach>

        <tr>
          <td colspan="5" style="text-align:left;font-size:1.2em;font-style:italic;font-weight:bold;">CENTER BANNERS</td>
        </tr>

        <tr>
          <td colspan="5"></td>
        </tr>

        <c:forEach var="categoryImage" items="${ua.categoryImages}" varStatus="ctr">
          <c:if test="${fn:toLowerCase(categoryImage.position) eq('center')}">
            <tr style="border:1px">
              <td>
                <hk:categoryImage imageId="${categoryImage.id}" size="<%=EnumImageSize.SmallSize%>"/>
              </td>
              <td>
                <s:checkbox name="categoryImages[${ctr.index}].hidden"/>
              </td>
              <td>
                <s:text name="categoryImages[${ctr.index}].ranking"/>
              </td>
              <td>
                http://www.healthkart.com <s:text name="categoryImages[${ctr.index}].link"/>
              </td>
              <td>
                <s:select name="categoryImages[${ctr.index}].position">
                  <s:option value="">-None-</s:option>
                  <s:option value="left">Left</s:option>
                  <s:option value="center">Center</s:option>
                  <s:option value="right">Right</s:option>
                </s:select>
              </td>
              <s:hidden name="categoryImages[${ctr.index}]"/>
            </tr>
          </c:if>
        </c:forEach>
        <tr>
          <td colspan="5" style="text-align:left;font-size:1.2em;font-style:italic;font-weight:bold;">RIGHT BANNERS</td>
        </tr>

        <tr>
          <td colspan="5"></td>
        </tr>

        <c:forEach var="categoryImage" items="${ua.categoryImages}" varStatus="ctr">
          <c:if test="${fn:toLowerCase(categoryImage.position) eq('right')}">
            <tr style="border:1px">
              <td>
                <hk:categoryImage imageId="${categoryImage.id}" size="<%=EnumImageSize.SmallSize%>"/>
              </td>
              <td>
                <s:checkbox name="categoryImages[${ctr.index}].hidden"/>
              </td>
              <td>
                <s:text name="categoryImages[${ctr.index}].ranking"/>
              </td>
              <td>
                http://www.healthkart.com <s:text name="categoryImages[${ctr.index}].link"/>
              </td>
              <td>
                <s:select name="categoryImages[${ctr.index}].position">
                  <s:option value="">-None-</s:option>
                  <s:option value="left">Left</s:option>
                  <s:option value="center">Center</s:option>
                  <s:option value="right">Right</s:option>
                </s:select>
              </td>
              <s:hidden name="categoryImages[${ctr.index}]"/>
            </tr>
          </c:if>
        </c:forEach>

        <tr>
          <td colspan="5" style="text-align:left;font-size:1.2em;font-style:italic;font-weight:bold;">POSITION: NOT YET ASSIGNED</td>
        </tr>

        <tr>
          <td colspan="5"></td>
        </tr>

        <c:forEach var="categoryImage" items="${ua.categoryImages}" varStatus="ctr">
          <c:if
              test="${fn:toLowerCase(categoryImage.position) !='left' && fn:toLowerCase(categoryImage.position) !='center' && fn:toLowerCase(categoryImage.position) !='right'}">
            <tr style="border:1px">
              <td>
                <hk:categoryImage imageId="${categoryImage.id}" size="<%=EnumImageSize.SmallSize%>"/>
              </td>
              <td>
                <s:checkbox name="categoryImages[${ctr.index}].hidden"/>
              </td>
              <td>
                <s:text name="categoryImages[${ctr.index}].ranking"/>
              </td>
              <td>
                http://www.healthkart.com <s:text name="categoryImages[${ctr.index}].link"/>
              </td>
              <td>
                <s:select name="categoryImages[${ctr.index}].position">
                  <s:option value="">-None-</s:option>
                  <s:option value="left">Left</s:option>
                  <s:option value="center">Center</s:option>
                  <s:option value="right">Right</s:option>
                </s:select>
              </td>
              <s:hidden name="categoryImages[${ctr.index}]"/>
            </tr>
          </c:if>
        </c:forEach>
        <tr>
          <td>
            <s:submit name="editCategoryImageSettings" value="Submit"/>
            <s:hidden name="category" value="${ua.category}"/>
          </td>
        </tr>
      </table>
    </s:form>
  </s:layout-component>
</s:layout-render>