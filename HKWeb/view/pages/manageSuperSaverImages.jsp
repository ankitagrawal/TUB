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

		table th, table td {
			text-align: center;
		}

		table td.image {
			width: 25%;
		}

		table td input[type="text"].altText {
			width: 500px;
		}

		div.paginationDiv {
			float: right;
			margin: 15px;
		}

		fieldset {
			padding: 5px;
			text-align: center;
		}

		fieldset#categoryBrandFilter, fieldset#productFilter {
			width: 45%;
		}

		fieldset#categoryBrandFilter {
			float: left;
		}

		fieldset#productFilter {
			float: left;
			width: 25%;
		}

		fieldset#productAssignFilter {
			float: right;
			width: 18%;
		}
	</style>
</s:layout-component>
<s:layout-component name="content">
	<div class="headingDiv">
		<h2>SUPER SAVER IMAGES</h2>
	</div>

	<div class="searchDiv">
		<s:form beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
			<fieldset>
				<legend>Filter Super Savers</legend>

				<fieldset id="categoryBrandFilter">
					<legend>By Category and Brand</legend>
					<label>Category:</label><s:text name="categories[0]"/>
					&nbsp; &nbsp;
					<label>Brand:</label><s:text name="brands[0]"/>
					&nbsp; &nbsp;
					<s:submit name="getSuperSaversForCategoryAndBrand" value="Filter" id="categoryBrandSubmit"/>
				</fieldset>

				<fieldset id="productFilter">
					<legend>By Product Id</legend>
					<label>Product Id:</label><s:text name="product" id="productText"/>
					&nbsp; &nbsp;
					<s:submit name="getSuperSaversForProduct" value="Filter" id="productSubmit"/>
				</fieldset>

				<fieldset id="productAssignFilter">
					<legend>No Product Assigned</legend>
					<s:submit name="getSuperSaversWithNoProductAssigned" value="Filter" id="noProductSubmit"/>
				</fieldset>
			</fieldset>
		</s:form>
	</div>

	<div class="paginationDiv">
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${comboBean}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${comboBean}"/>
	</div>

	<s:form beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction">
		<div class="errorDiv"></div>

		<div class="clear"></div>

		<table>
			<tr>
				<th>IMAGE</th>
				<th class="assignedSuperSaverHeader">PRODUCT ID</th>
				<th class="assignedSuperSaverHeader">RANK</th>
				<th class="assignedSuperSaverHeader">ALT TEXT</th>
				<th class="assignedSuperSaverHeader">HIDE</th>
				<th class="assignedSuperSaverHeader">DELETE</th>
			</tr>
			<c:forEach var="superSaverImage" items="${comboBean.superSaverImages}" varStatus="productCtr">
				<tr class="row">
					<s:hidden name="superSaverImages[${productCtr.index}]"/>
					<c:choose>
						<c:when test="${superSaverImage.product != null }">
							<td><hk:superSaverImage imageId="${superSaverImage.id}"
							                        size="<%=EnumImageSize.MediumSize%>" class="image"/></td>
						</c:when>
						<c:otherwise>
							<td><s:link beanclass="com.hk.web.action.core.catalog.image.UploadSuperSaverImageAction"
							            event="editUnassignedSuperSaver" class="unassignedImageLink">
								<hk:superSaverImage imageId="${superSaverImage.id}"
								                    size="<%=EnumImageSize.MediumSize%>" class="image"/>
								<s:param name="unassignedSuperSaver" value="${superSaverImage}"/>
							</s:link></td>
						</c:otherwise>
					</c:choose>
					<c:if test="${superSaverImage.product != null }">
						<td><s:text name="superSaverImages[${productCtr.index}].product"
						            value="${superSaverImage.product.id}" class="productId"/></td>
						<td><s:text name="superSaverImages[${productCtr.index}].ranking" class="ranking"/></td>
						<td><s:text name="superSaverImages[${productCtr.index}].altText"
						            value="${superSaverImage.altText}" class="altText"/></td>
						<td><s:checkbox name="superSaverImages[${productCtr.index}].hidden"
						                class="hiddenCheck"/></td>
						<td><s:checkbox name="superSaverImages[${productCtr.index}].deleted"
						                class="deleteCheck"/></td>
					</c:if>
				</tr>
			</c:forEach>
		</table>

		<div>
			<s:submit name="editSuperSaverImageSettings" value="Save Changes" class="submitButton"/>
		</div>
	</s:form>

	<div class="paginationDiv">
		<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${comboBean}"/>
		<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${comboBean}"/>
	</div>

	<s:form beanclass="com.hk.web.action.core.catalog.SuperSaversAction">
		<s:submit name="pre" value="Back to Super Savers"/>
	</s:form>

	<script type="text/javascript">
		$(document).ready(function() {
			$('.submitButton').click(function() {
				var error = false;
				$('.productId').each(function() {
					if ($(this).val().trim() === "") {
						error = true;
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

			$('#productSubmit').click(function() {
				var productId = $('#productText').val().trim();
				if (productId === "") {
					$('.errorDiv').html("KINDLY ENTER A VALID PRODUCT ID FOR WHICH SUPER SAVERS NEED TO BE FILTERED....");
					$('.errorDiv').show();
					return false;
				} else {
					return true;
				}
			});

			removeHeader();
		});

		function removeHeader() {
			if ($('.unassignedImageLink').size() > 0) {
				$('.assignedSuperSaverHeader').each(function() {
					$(this).hide();
				});
				$('.submitButton').hide();
			}
		}

	</script>
</s:layout-component>
</s:layout-render>


