<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction" var="comboBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="content">
        <h2>SUPER SAVER IMAGES</h2>

        <s:form beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
            <table>
                <tr>
                    <th>IMAGE</th>
                    <th>RANK</th>
                    <th>LINK</th>
                    <th>IS MAIN IMAGE</th>
                    <th>HIDE</th>
                </tr>
                <c:forEach var="superSaverImage" items="${comboBean.superSaverImages}" varStatus="productCtr">
                    <tr>
                        <s:hidden name="superSaverImages[${productCtr.index}]"/>
                        
                        <td><hk:superSaverImage imageId="${superSaverImage.id}"
                                                size="<%=EnumImageSize.MediumSize%>"/></td>
                        <td><s:text name="superSaverImages[${productCtr.index}].ranking"/></td>
                        <td>http://www.healthkart.com <s:text name="superSaverImages[${productCtr.index}].link"/></td>
                        <td><s:checkbox name="superSaverImages[${productCtr.index}].mainImage"/></td>
                        <td><s:checkbox name="superSaverImages[${productCtr.index}].hidden"/></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td><s:submit name="editSuperSaverImageSettings" value="Save Changes"/></td>
                </tr>
            </table>
        </s:form>
    </s:layout-component>
</s:layout-render>


