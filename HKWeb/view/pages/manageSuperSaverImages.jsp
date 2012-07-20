<%@ page import="com.hk.constants.catalog.image.EnumImageSize" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction" var="comboBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
    <s:layout-component name="htmlHead">
        <style type="text/css">
            div.errorDiv {
                margin: 10px;
                padding: 5px;
                display: none;
                color: #E80000;
            }

            input[type="submit"] {
                font-size: 0.8em;
            }

            div.headingDiv {
                text-align: center;
            }

            table {
                width: 100%;
            }

            table th {
                text-align: center;
            }

            table td.image {
                width: 25%;
            }

            table td input[type="text"].altText {
                width: 500px;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="content">
        <div class="headingDiv">
            <h2>SUPER SAVER IMAGES</h2>
        </div>

        <s:form beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
            <div class="errorDiv"></div>

            <div class="clear"></div>

            <table>
                <tr>
                    <th>IMAGE</th>
                    <th>COMBO ID</th>
                    <th>RANK</th>
                    <th>ALT TEXT</th>
                        <%--<th>IS MAIN IMAGE</th>--%>
                    <th>HIDE</th>
                </tr>
                <c:forEach var="superSaverImage" items="${comboBean.superSaverImages}" varStatus="productCtr">
                    <tr class="row">
                        <s:hidden name="superSaverImages[${productCtr.index}]"/>

                        <td><hk:superSaverImage imageId="${superSaverImage.id}"
                                                size="<%=EnumImageSize.MediumSize%>" class="image"/></td>
                        <td><s:text name="superSaverImages[${productCtr.index}].product" class="productId"/></td>
                        <td><s:text name="superSaverImages[${productCtr.index}].ranking" class="ranking"/></td>
                        <td><s:text name="superSaverImages[${productCtr.index}].altText" class="altText"/></td>
                            <%--<td><s:checkbox name="superSaverImages[${productCtr.index}].mainImage"/></td>--%>
                        <td><s:checkbox name="superSaverImages[${productCtr.index}].hidden" class="hiddenCheck"/></td>
                    </tr>
                </c:forEach>
            </table>

            <div>
                <s:submit name="editSuperSaverImageSettings" value="Save Changes" class="submitButton"/>
            </div>
        </s:form>

        <s:form beanclass="com.hk.web.action.core.catalog.SuperSaversAction">
            <s:submit name="pre" value="Back to Super Savers"/>
        </s:form>

        <script type="text/javascript">
            $(document).ready(function() {
                $('.submitButton').click(function() {
                    var error = 0;
                    $('.productId').each(function() {
                        if ($(this).val().trim() === "") {
                            error = 1;
                            $('.errorDiv').html("COMBO ID IS A MANDATORY FIELD FOR SUPER SAVER BANNER. KINDLY ENTER APPROPRIATE DATA....");
                            $(this).parents('.row').css({
                                "background":"#E80000"
                            });
                            $('.errorDiv').show();
                            return false;
                        }
                    });
                    
                    $('.altText').each(function() {
                        $(this).val($(this).val().trim());
                    });

                    return !error;
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>


