<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Variant config tasks">
    <s:layout-component name="heading">
        Run Variant Config
    </s:layout-component>

    <s:layout-component name="content">

      	<s:form beanclass="com.hk.web.action.admin.marketing.MarketingExpenseAction">

        <table>
            <tr>
                <td>Product Variant</td>
                <td><s:text style="width:100px" name="Product_variant"/></td>
                <td>
                    <s:select name="configService">
                    <s:option value="">-select- </s:option>
                    <s:option value="1">config</s:option>
                    </s:select>
                </td>
                <td>
                    <s:submit class="requiredFieldValidator" name="save" value="Save"/>
                </td>

            </tr>

        </table>
        </s:form>
    </s:layout-component>
</s:layout-render>