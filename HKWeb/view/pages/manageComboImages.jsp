<%--
<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.ComboAction" var="comboActionBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">

  <s:layout-component name="content">
    --%>
<%--<p>${pa.product.id}</p>--%>
<%--
    <h2>
      ${comboActionBean.combo.name}
    </h2>
    <s:form beanclass="com.hk.web.action.ComboAction">
      <table>
        <tr>
          <th>
            Image
          </th>
          <th>
            Hide
          </th>
          <th>
            Set as Main
          </th>
        </tr>
        <c:forEach var="comboImage" items="${comboActionBean.comboImages}" varStatus="ctr">
          <tr>
            <td>
              <hk:productImage imageId="${comboImage.id}" size="<%=EnumImageSize.MediumSize%>"/>
            </td>
            <td>
              <s:hidden name="comboImages[${ctr.index}]"/>
              <s:checkbox name="comboImages[${ctr.index}].hidden"/>
            </td>
            <td>
              <s:radio value="${comboImage.id}" name="mainImageId"/>
            </td>
          </tr>
        </c:forEach>
        <tr>
          <td>
          <s:submit name="editImageSettings" value="Submit"/>
           <s:hidden name="comboId" value="${comboActionBean.comboId}"/>
          </td>
        </tr>
      </table>

    </s:form>

  </s:layout-component>

</s:layout-render>--%>
