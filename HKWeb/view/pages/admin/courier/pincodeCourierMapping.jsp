<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:useActionBean beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction" var="mappingBean"/>
    <s:layout-component name="content">
        <fieldset class="right_label">
            <legend>Download Pincode Courier Mapping Excel</legend>
            <ul>
                <s:form beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
                    <li><label>Pincode Courier Mapping to download : </label>
                        <label style="float:left;width:50px;">Couriers</label>

                        <div class="checkBoxList">
                            <c:forEach items="${mappingBean.couriers}" var="courier" varStatus="ctr">
                                <label><s:checkbox name="couriers[${ctr.index}]"
                                                   value="${courier.id}"/> ${courier.name}</label>
                                <br/>
                            </c:forEach>
                        </div>
                    </li>
                    <li>

                        <div class="buttons">
                            <s:submit name="generatePincodeCourierMappingExcel" value="Download"/>
                        </div>

                    </li>
                </s:form>
            </ul>
        </fieldset>

        <fieldset class="right_label">
            <legend>Upload Pincode Courier Mapping Excel</legend>
            <ul>

                <s:form beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
                    <div class="grid_4">
                        <li><label>File to Upload</label>
                            <s:file name="fileBean" size="30"/>
                        </li>
                        <li>
                            <div class="buttons">
                                <s:submit name="uploadPincodeCourierMappingExcel" value="Upload"/>
                            </div>
                        </li>
                </s:form>
            </ul>
        </fieldset>
        <fieldset class="right_label">
            <legend>Update Courier Service</legend>
            <s:form beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
                <table>
                    <tr>
                        <td>Pincode:</td>
                        <td><s:text name="pincode"
                                    value="${mappingBean.pincodeCourierMapping.pincode.pincode}"/></td>
                    </tr>
                    <tr>
                        <td>Courier:</td>
                        <td><s:select name="mappingBean.pincodeCourierMapping.courier" value="${mappingBean.pincodeCourierMapping.courier.id}"
                                      class="applicableCourier">
                            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
                                                       label="name"/>
                        </s:select></td>
                    </tr>
                    <tr>
                        <td>Prepaid Air</td>
                        <td>
                            <s:select name="mappingBean.pincodeCourierMapping.prepaidAir" value="">
                                <s:option value="0">N</s:option>
                                <s:option value="1">Y</s:option>
                            </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Prepaid Ground</td>
                        <td>
                            <s:select name="mappingBean.pincodeCourierMapping.prepaidGround" value="">
                                <s:option value="0">N</s:option>
                                <s:option value="1">Y</s:option>
                            </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Cod Air</td>
                        <td>
                            <s:select name="mappingBean.pincodeCourierMapping.codAir" value="">
                                <s:option value="0">N</s:option>
                                <s:option value="1">Y</s:option>
                            </s:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Cod Ground</td>
                        <td>
                            <s:select name="mappingBean.pincodeCourierMapping.codGround" value="">
                                <s:option value="0">N</s:option>
                                <s:option value="1">Y</s:option>
                            </s:select>
                        </td>
                    </tr>
                </table>
                <s:submit name="updatePincodeCourierMapping" value="Update"></s:submit>
            </s:form>
        </fieldset>

    </s:layout-component>

</s:layout-render>
