<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:useActionBean beanclass="com.hk.web.action.core.discount.CategoryLevelDiscountAction" var="categoryCommission"/>
  <s:layout-component name="htmlHead">
  </s:layout-component>

  <s:layout-component name="content">

    <s:form beanclass="com.hk.web.action.core.discount.CategoryLevelDiscountAction">
      <s:errors/>
      <table>
        <thead>
        <tr>
          <th>Affiliate Category</th>
          <th>Overview</th>
          <th>Commission First Transaction</th>
          <th>Commission Lateral Transaction</th>
          <th>Save/Delete</th>
        </tr>
        </thead>
        <c:forEach items="${categoryCommission.defaultAffiliateCategoryCommissionList}" var="affiliateCategoryCommission" varStatus="ctr">
          <tr>
            <td>
              <s:hidden name="defaultAffiliateCategoryCommissionList[${ctr.index}].affiliateCategory" value="${affiliateCategoryCommission.affiliateCategory.affiliateCategoryName}"/>
                ${affiliateCategoryCommission.affiliateCategory.affiliateCategoryName}
            </td>
            <td>
                ${affiliateCategoryCommission.affiliateCategory.overview}</td>
            <td>
              <s:text name="defaultAffiliateCategoryCommissionList[${ctr.index}].commissionFirstTime" value="${affiliateCategoryCommission.commissionFirstTime}"/></td>
            <td>
              <s:text name="defaultAffiliateCategoryCommissionList[${ctr.index}].commissionLatterTime" value="${affiliateCategoryCommission.commissionLatterTime}"/></td>
            <td><s:checkbox name="defaultAffiliateCategoryCommissionList[${ctr.index}].selected"/>
            </td>
            <s:hidden name="overview" value="${affiliateCategoryCommission.affiliateCategory}"/>
            <s:hidden name="category" value="${affiliateCategoryCommission.affiliateCategory}"/>
            <s:hidden name="commissionFirstTime" value="${affiliateCategoryCommission.commissionFirstTime}"/>
            <s:hidden name="commissionLatterTime" value="${affiliateCategoryCommission.commissionLatterTime}"/>
          </tr>
        </c:forEach>
      </table>
      <label>&nbsp;</label>

      <div class="buttons"><s:submit name="saveExisting" value="Save"/></div>
      <%--<div class="buttons"><s:submit name="deleteExisting" value="Delete"/></div>--%>
    </s:form>

    <s:form beanclass="com.hk.web.action.core.discount.CategoryLevelDiscountAction">
      <br><br><br><br>
      <fieldset style="float:left;">
        <legend>Add New Category</legend>
        <table>
          <tr>
            <td><label>Category :-</label></td>
            <td><s:text name="category"/></td>
          </tr>
          <tr>
            <td><label>Overview</label></td>
            <td><s:text name="overview"/></td>
          </tr>
          <tr>
            <td><label>Commission for First Transaction</label></td>
            <td><s:text name="commissionFirstTime"/></td>
          </tr>
          <tr>
            <td><label>Commission for Latter Transaction</label></td>
            <td><s:text name="commissionLatterTime"/></td>
          </tr>
          <td>
            <tr><label>&nbsp;</label>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>
                <div class="buttons"><s:submit name="saveNew" value="Save"/></div>
              </td>
            </tr>
        </table>
      </fieldset>
    </s:form>
      <s:form beanclass="com.hk.web.action.core.discount.CategoryLevelDiscountAction">
          <br><br><br><br>
          <fieldset style="float:left;">
              <legend>Assign Brand To Existing Categoryy</legend>
              <table>
                  <tr>
                      <td><label>Affiliate Category :-</label></td>
                      <td><s:text name="category"/></td>
                  </tr>
                  <tr>
                      <td><label>Brand</label></td>
                      <td><s:text name="brand"/></td>
                  </tr>
                  <td>
                      <tr><label>&nbsp;</label>
                      </tr>
                      <tr>
                          <td>&nbsp;</td>
                          <td>
                              <div class="buttons"><s:submit name="addBrandsToExistingAffiliateCategory" value="Add Brand"/></div>
                          </td>
                      </tr>
              </table>
          </fieldset>
      </s:form>

  </s:layout-component>
</s:layout-render>