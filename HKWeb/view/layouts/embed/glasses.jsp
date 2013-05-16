<%@ page import="com.hk.domain.catalog.product.Product"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:layout-definition>
	<%
	    Product product = (Product) pageContext.getAttribute("product");
	    pageContext.setAttribute("product", product);
	%>

	<c:set value="${product.productVariants[0]}" var="variant" />
	<c:set value="${variant.hkPrice + hk:getPostpaidAmount(variant)}"
		var="ourPriceForVariant" />



	<div id="tabs"><input id="variantId" type="hidden"
		value="${product.productVariants[0].id}">
	<ul>
		<li><a href="#selection-tab"> <span
			style="float: left; font-size: 15px; font-weight: bold; vertical-align: top; padding: 0 5px">1</span>
		<span style="float: right;">Your<br />
		Selection</span> </a></li>
		<li><a href="#prescription-tab"> <span
			style="float: left; font-size: 15px; font-weight: bold; vertical-align: top; padding: 0 5px">2</span>
		<span style="float: right;">Enter<br />
		Your Prescription</span> </a></li>
		<li><a href="#coating-tab"> <span
			style="float: left; font-size: 15px; font-weight: bold; vertical-align: top; padding: 0 5px">3</span>
		<span style="float: right;">Select<br />
		Lens Thickness/Coating</span> </a></li>
	</ul>

	<!-- Start Selection Tab-->
	<div id="selection-tab" style="overflow: hidden;">
	<div class='prices' style="float: left; font-size: 14px;"><c:if
		test="${variant.discountPercent > 0}">
		<div class='cut' style="font-size: 14px;"><span class='num'
			style="font-size: 14px;"> Rs <fmt:formatNumber
			value="${variant.markedPrice}" maxFractionDigits="0" /> </span></div>
		<div class='hk' style="font-size: 16px; margin-top: 12px;">Our
		Price <span class='num' style="font-size: 20px; margin-top: 12px;">
		Rs <fmt:formatNumber value="${ourPriceForVariant}"
			maxFractionDigits="0" /> </span></div>
		<div class="special green" style="font-size: 14px; margin-top: 12px">
		you save <span style="font-weight: bold;"><fmt:formatNumber
			value="${variant.discountPercent*100}" maxFractionDigits="0" />%</span></div>
	</c:if> <c:if test="${variant.discountPercent == 0}">
		<div class='hk' style="font-size: 16px; margin-top: 12px">Our
		Price <span class='num' style="font-size: 20px; margin-top: 12px">
		Rs <fmt:formatNumber value="${ourPriceForVariant}"
			maxFractionDigits="0" /> </span></div>
	</c:if> <c:choose>
		<c:when test="${variant.outOfStock}">
			<span class="outOfStock">Sold Out</span>

			<div align="center"><s:link
				beanclass="com.hk.web.action.core.user.NotifyMeAction"
				class="notifyMe button_orange">
				<b>Notify Me!!</b>
				<s:param name="productVariant" value="${variant}" />
			</s:link></div>
		</c:when>
		<c:otherwise>
			
				<input type="button" id="buyFrame" style="float: left"
					value="Buy This Frame" class="cta button_green" />
			
			<input type="button" id="addLens" style="float: left"
				value="Add Lens & Buy" class="cta button_green" />
		</c:otherwise>
	</c:choose></div>
	<div
		style="float: right; font-size: 14px; border-left: 1px solid #CCCCCC;">

	<div style="margin: 5px; font-size: 16px;"><%--<s:layout-render name="/layouts/embed/_hkAssistanceMessageForSingleVariant.jsp"/>--%>
	<%--<img src="${pageContext.request.contextPath}/images/banners/frame_chart.jpg"/>--%>
	</div>
	</div>
	<div class="clear"></div>
	<div style="font-size: 12px;"><%--<c:if test="${hk:isNotBlank(variant.optionsCommaSeparated)}">
      <h4>Options</h4><br/> ${variant.optionsCommaSeparated}
    </c:if>--%></div>
	</div>

	<!-- End Selection Tab--> <c:set var="PR" value="PR" /> <c:set
		var="BF" value="BF" /> <c:set var="TH" value="TH" /> <c:set
		var="THBF" value="THBF" /> <c:set var="CO" value="CO" /> <c:set
		var="COBF" value="COBF" /> <c:set var="BRANDCO" value="BRANDCO" /> <c:set
		var="BRANDTH" value="BRANDTH" /> <c:set var="BRANDTHBF"
		value="BRANDTHBF" /> <!-- Start Prescription Tab-->
	<div id="prescription-tab" style="overflow: hidden;">
	<div class="row">
	<div class="floatleft hkLabel" style="color: rgb(21, 101, 179);">Right
	Eye (OD)</div>
	<div class="floatleft hkLabel"
		style="margin-left: 200px; color: rgb(21, 101, 179);">Left Eye
	(OS)</div>
	</div>
	<div class="clear"></div>
	<c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">
		<c:if test="${configOption.additionalParam eq PR}">
			<c:if test="${configOptionCtr.index %2 ==0}">
				<div class="row">
			</c:if>
			<c:choose>
				<c:when test="${configOptionCtr.index %2 ==0}">
					<div class="floatleft hkLabel" style="width: 99px;">${configOption.displayName}</div>
					<div class="floatleft hkField"><select
						id="${configOption.id}" name="${configOption.name}"
						class="eyeparamselect">
				</c:when>
				<c:otherwise>
					<div class="floatleft hkLabel"
						style="width: 99px; margin-left: 70px;">${configOption.displayName}</div>
					<div class="floatleft hkField"><select
						id="${configOption.id}" name="${configOption.name}"
						class="eyeparamselect right">
				</c:otherwise>
			</c:choose>
			<option value="999" price="999">Please Select</option>
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue">
				<option value="${configValue.value}" valueId="${configValue.id}"
					price="${configValue.additonalPrice}">${configValue.valueWithPrice}</option>
			</c:forEach>
			</select></div>
	<c:if test="${configOptionCtr.index %2 !=0}"></div>

	<div class="clear"></div>
	</c:if>
	</c:if>
	</c:forEach>

	<div class="row">
	<div class="floatleft hkLabel"
		style="font-weight: bold; font-size: 14px;">Do you have a
	Bi-focal power?</div>
	<div class="floatleft hkField" style="margin-left: 90px;"><input
		type="radio" id="biFocalYes" name="biFocalPowers" class="biFocalRadio" /> Yes <input
		type="radio" id="biFocalNo" name="biFocalPowers" checked="checked" class="biFocalRadio" />
	No</div>
	</div>
	<div class="clear"></div>

	<div id="biFocalPowers" style="display: none"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">
		<c:if test="${configOption.additionalParam eq BF}">
			<c:if test="${configOptionCtr.index %2 ==0}">
				<div class="row">
			</c:if>
			<c:choose>
				<c:when test="${configOptionCtr.index %2 ==0}">
					<div class="floatleft hkLabel" style="width: 99px;">${configOption.displayName}</div>
					<div class="floatleft hkField"><select
						id="${configOption.id}" name="${configOption.name}"
						class="eyeparamselect biFocalParam">
				</c:when>
				<c:otherwise>
					<div class="floatleft hkLabel"
						style="width: 99px; margin-left: 70px;">${configOption.displayName}</div>
					<div class="floatleft hkField"><select
						id="${configOption.id}" name="${configOption.name}"
						class="eyeparamselect right biFocalParam">
				</c:otherwise>
			</c:choose>
			<option value="999" price="999">Please Select</option>
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue">
				<option value="${configValue.value}" valueId="${configValue.id}"
					price="${configValue.additonalPrice}">${configValue.valueWithPrice}</option>
			</c:forEach>
			</select></div>
	<c:if test="${configOptionCtr.index %2 !=0}">
		</div>

		<div class="clear"></div>
	</c:if>
	</c:if>
	</c:forEach>
	</div>

	<div class="row">
	<div class="floatleft"><input type="button" id="addPowers"
		value="Continue" class="cta button_green" /></div>
	</div>
	<div class="clear"></div>
	</div>


	<div id="coating-tab" style="overflow: hidden;">

	<div class="row"
		style="border-bottom: solid #CCCCCC; padding-bottom: 10px;">
	<div class="floatleft" style="font-weight: bold; font-size: 14px;">
	Thickness & Material</div>
	<div class="clear"></div>

	<div id="thickness"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">

		<c:if test="${configOption.additionalParam eq TH}">
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue" varStatus="configValueCtr">
				<div class="row">
				<div class="floatleft hkLabel"><input type="radio"
					id="thickenss-${configValue.id}" optionId="${configOption.id}"
					class="thickness" name="thickness"
					price="${configValue.additonalPrice}" valueId="${configValue.id}"
					value="${configValue.value}" ${(configValueCtr.index
					eq 0)?'checked="checked" default="thickness" ':''}
                />
				${configValue.value}</div>
				<div class="floatright hkLabel"><c:set var="priceString"
					value="+Rs. ${configValue.additonalPrice}/Eye" /> <label
					style="float: right;"> ${(configValue.additonalPrice eq
				0)?'included':priceString} </label></div>
				</div>

				<div class="clear"></div>
			</c:forEach>

		</c:if>
	</c:forEach></div>

	<%--added--%>

	<div id="brandthickness" style="display: none"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">

		<c:if test="${configOption.additionalParam eq BRANDTH}">
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue" varStatus="configValueCtr">
				<div class="row">
				<div class="floatleft hkLabel"><input type="radio"
					id="thickenss-${configValue.id}" optionId="${configOption.id}"
					class="thickness" name="thickness"
					price="${configValue.additonalPrice}" valueId="${configValue.id}"
					value="${configValue.value}" ${(configValueCtr.index
					eq 0)?'  default="brandthickness" ':''}
                                    />
				${configValue.value}</div>
				<div class="floatright hkLabel"><c:set var="priceString"
					value="+Rs. ${configValue.additonalPrice}/Eye" /> <label
					style="float: right;"> ${(configValue.additonalPrice eq
				0)?'included':priceString} </label></div>
				</div>

				<div class="clear"></div>
			</c:forEach>

		</c:if>
	</c:forEach></div>

	<div id="biFocalThickness"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">

		<c:if test="${configOption.additionalParam eq THBF}">
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue" varStatus="configValueCtr">
				<div class="row">
				<div class="floatleft hkLabel"><input type="radio"
					id="bfThickenss-${configValue.id}" optionId="${configOption.id}"
					name="thickness" price="${configValue.additonalPrice}"
					value="${configValue.value}" valueId="${configValue.id}"
					${(configValueCtr.index eq 0)?' default="bfThickness"
					':''}
                    class="thickness" />
				${configValue.value}</div>
				<div class="floatright hkLabel"><label style="float: right;">
				+Rs. ${configValue.additonalPrice}/Eye</label></div>
				</div>

				<div class="clear"></div>
			</c:forEach>

		</c:if>
	</c:forEach></div>

	<%--added   --%>
	<div id="brandbiFocalThickness" style="display: none"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">

		<c:if test="${configOption.additionalParam eq BRANDTHBF}">
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue" varStatus="configValueCtr">
				<div class="row">
				<div class="floatleft hkLabel"><input type="radio"
					id="bfThickenss-${configValue.id}" optionId="${configOption.id}"
					name="thickness" price="${configValue.additonalPrice}"
					value="${configValue.value}" valueId="${configValue.id}"
					${(configValueCtr.index eq 0)?' default="brandbifocalthickness"
					':''}
                                            class="thickness" />
				${configValue.value}</div>
				<div class="floatright hkLabel"><label style="float: right;">
				+Rs. ${configValue.additonalPrice}/Eye</label></div>
				</div>

				<div class="clear"></div>
			</c:forEach>

		</c:if>
	</c:forEach></div>


	</div>
	<div class="clear"></div>

	<div class="row"
		style="border-bottom: solid #CCCCCC; padding-bottom: 10px;">
	<div class="floatleft" style="font-weight: bold; font-size: 14px;">
	Coating</div>
	<div class="clear"></div>

	<div id="coating-div"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">

		<c:if test="${configOption.additionalParam eq CO}">
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue">
				<div class="row">
				<div class="floatleft hkLabel"><input type="radio"
					id="normalcoating" optionId="${configOption.id}" name="coating"
					price="${configValue.additonalPrice}" valueId="${configValue.id}"
					value="${configValue.value}" ${(configValue.additonalPrice
					eq 0)?'checked="checked" default="coating"
					':''}
                class="coating" /> ${configValue.value}</div>
				<div class="floatright hkLabel"><c:set var="priceString"
					value="+Rs. ${configValue.additonalPrice}/Eye" /> <label
					style="float: right;"> ${(configValue.additonalPrice eq
				0)?'included':priceString} </label></div>
				</div>

				<div class="clear"></div>
			</c:forEach>

		</c:if>
	</c:forEach></div>

	<div id="biFocalCoating"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">

		<c:if test="${configOption.additionalParam eq COBF}">
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue" varStatus="configValueCtr">
				<div class="row">
				<div class="floatleft hkLabel"><input type="radio"
					id="bfCoating-${configValue.id}" optionId="${configOption.id}"
					name="coating" price="${configValue.additonalPrice}"
					value="${configValue.value}" valueId="${configValue.id}"
					${(configValueCtr.index eq 0)?' default="bfCoating"
					':''}
                    class="coating" /> ${configValue.value}
				</div>
				<div class="floatright hkLabel"><label style="float: right;">
				+Rs. ${configValue.additonalPrice}/Eye</label></div>
				</div>

				<div class="clear"></div>
			</c:forEach>

		</c:if>
	</c:forEach></div>

	<%--added--%>
	<div id="branded-coating"><c:forEach
		items="${product.productVariants[0].variantConfig.variantConfigOptions}"
		var="configOption" varStatus="configOptionCtr">

		<c:if test="${configOption.additionalParam eq BRANDCO}">
			<c:forEach items="${configOption.variantConfigValues}"
				var="configValue">
				<div class="row">
				<div class="floatleft hkLabel"><input type="radio"
					id="brandedcoating" optionId="${configOption.id}" name="coating"
					price="${configValue.additonalPrice}" valueId="${configValue.id}"
					value="${configValue.value}" ${(configValueCtr.index
					eq 0)?' default="brandedcoating"
					':''}
                                   class="coating" />
				${configValue.value}</div>
				<div class="floatright hkLabel"><c:set var="priceString"
					value="+Rs. ${configValue.additonalPrice}/Eye" /> <label
					style="float: right;"> ${(configValue.additonalPrice eq
				0)?'included':priceString} </label></div>
				</div>

				<div class="clear"></div>
			</c:forEach>

		</c:if>
	</c:forEach></div>

	</div>
	<div class="clear"></div>

	<div class="row">
	<div class="floatleft">
	<div class="row">

	<div class="floatleft hkLabel"
		style="font-weight: bold; font-size: 15px;">Total</div>
	<input id="totalPriceHidden" type="hidden" value="">

	<div class="floatright hkLabel" id="totalPrice"
		style="font-weight: bold; font-size: 15px; margin-left: 100px;"></div>
	</div>
	<div class="clear"></div>
	<div class="row">

	<div class="floatleft hkLabel">Frame price</div>
	<input id="ourPrice" type="hidden" value="${ourPriceForVariant}">

	<div class="floatright hkLabel" id="framePrice"
		style="margin-left: 70px;">Rs <fmt:formatNumber
		value="${ourPriceForVariant}" maxFractionDigits="2"
		minFractionDigits="2" /></div>
	</div>
	<div class="clear"></div>

	<div class="row" id="leftLensPriceRow" style="display: none;">

	<div class="floatleft hkLabel">Standard Prescription Lens(L)</div>
	<input id="leftLensPrice" type="hidden" value="">

	<div class="floatright hkLabel" id="leftLensPriceLabel"
		style="margin-left: 70px;"></div>
	</div>
	<div class="clear"></div>

	<div class="row" id="rightLensPriceRow" style="display: none;">

	<div class="floatleft hkLabel">Standard Prescription Lens(R)</div>
	<input id="rightLensPrice" type="hidden" value="">

	<div class="floatright hkLabel" id="rightLensPriceLabel"
		style="margin-left: 70px;"></div>
	</div>
	<div class="clear"></div>

	<div class="row" id="thickNessPriceRow">

	<div class="floatleft hkLabel" id="thickNessLabel"></div>
	<input id="thickNessPrice" type="hidden" value="">

	<div class="floatright hkLabel" id="thickNessPriceLabel"
		style="margin-left: 70px;"></div>
	</div>
	<div class="clear"></div>

	<div class="row" id="coatingPriceRow">

	<div class="floatleft hkLabel " id="coatingLabel"></div>
	<input id="coatingPrice" type="hidden" value="">

	<div class="floatright hkLabel" id="coatingPriceLabel"
		style="margin-left: 70px;"></div>
	</div>
	<div class="clear"></div>
	</div>

	<div class="floatright"
		style="height: 80px; border-left: solid #CCCCCC; padding: 20px;">
	<s:form
		beanclass="com.hk.web.action.core.cart.AddToCartWithLineItemConfigAction"
		onsubmit="return false;">
		<s:submit name="buyNow" id="buyNow" value="Buy Now" class="" />
	</s:form></div>


	</div>
	<div class="clear"></div>

	</div>
	<!-- End Tabs-->
	</div>

	<%--<div>
	<strong>Eye:</strong>52mm&nbsp;
	<strong>Bridge:</strong>12mm&nbsp;
	<strong>Vertical:</strong>24mm&nbsp;
	<strong>Temple:</strong>112mm&nbsp;
</div>
<div align="center">
	<img src="${pageContext.request.contextPath}/images/banners/frame_chart.jpg"/>
</div>--%>

	<script type="text/javascript" src="<hk:vhostJs/>/js/jquery-ui.min.js"></script>
	<link href="<hk:vhostCss/>/css/jquery-ui.css" rel="stylesheet"
		type="text/css" />

	<style type="text/css">
a {
	text-decoration: none;
	border-bottom: 0px;
	margin-top: 0px;
	font-size: 13px;
	margin-bottom: 0px;
}

.eyeparamselect {
	height: auto;
	width: 110px;
	border: 1px solid #a2c4e5;
	border-radius: 5px;
	padding: 2px;
	font-size: 12px;
	margin-left: 3px;
}

.hkLabel {
	display: block;
	text-align: left;
	font-size: 9pt;
}

.hkField {
	color: #000000;
	display: inline;
	font-size: 11px;
	margin-left: 3px;
}

.row {
	margin: 10px 0;
}
</style>
	<script>
$(document).ready(function() {
  var tabs = $("#tabs").tabs();
  $("#addLens").click(function() {
    tabs.tabs('select', 1);
  });


  function initPage() {
    if ($('input[name=biFocalPowers]:checked').attr('id') == 'biFocalYes') {
      $("#biFocalPowers").show();
      $("#thickness").hide();
      $("#biFocalThickness").show();
      //$("#coating-div").hide();
    } else {
      $("#biFocalPowers").hide();
      $("#thickness").show();
      $("#biFocalThickness").hide();
      //$("#coating-div").show();
    }
    initPriceRows();
  }

  function initPriceRows() {
    renderThicknessPriceRow();
    renderCoatingPriceRow();
    renderTotalPrice();
  }

  function renderTotalPrice() {
    var framePrice = $.trim($("#ourPrice").val());
    var thicknessPrice = $.trim($("#thickNessPrice").val());
    var coatingPrice = $.trim($("#coatingPrice").val());
    //    var leftLensPrice = $.trim($("#leftLensPrice").val());
    //    var rightLensPrice = $.trim($("#rightLensPrice").val());

    var totalPrice = parseFloat(framePrice);
    if (thicknessPrice != '') {
      totalPrice += parseFloat(thicknessPrice) * 2;
    }
    if (coatingPrice != '') {
      totalPrice += parseFloat(coatingPrice) * 2;
    }
    //    if (leftLensPrice != '') {
    //      totalPrice += parseFloat(leftLensPrice);
    //    }
    //    if (rightLensPrice != '') {
    //      totalPrice += parseFloat(rightLensPrice);
    //    }

    $("#totalPrice").html("Rs. " + totalPrice);
    $("#totalPriceHidden").val(totalPrice);
  }

  $(".thickness").click(function(event) {
    renderThicknessPriceRow();
  });

  function renderThicknessPriceRow() {
    var selectedThickness = $('.thickness:checked');
    var thPrice = $.trim(selectedThickness.attr('price'));
    var thValue = $.trim(selectedThickness.attr('value'));

    $("#thickNessLabel").html(thValue);
    if (thPrice == 0) {
      $("#thickNessPriceLabel").html("included");
    } else {
      $("#thickNessPriceLabel").html("Rs. " + thPrice * 2);
    }
    $("#thickNessPrice").val(thPrice);

    renderTotalPrice();
  }


   $(".coating").click(function(event) {

        var selectedCoating = $('.coating:checked');
        var coating = $.trim(selectedCoating.attr('id'));
        var brandtThickness = $('input[default=brandthickness]');
        var brandbiFocalThickness = $('input[default=brandbifocalthickness]');
        var biFocalThickness = $('input[default=bfThickness]');
        var normalThickness = $('input[default=thickness]');
        var brandedCoating = $('input[default=brandedcoating]');
        var normalCoating = $('input[default=coating]');


        if (coating == 'brandedcoating') {


            brandedCoating.attr("checked", true);
            normalCoating.attr("checked", false);

            if ($('input[name=biFocalPowers]:checked').attr('id') == 'biFocalYes') {

                $("#brandbiFocalThickness").show();
                $("#brandthickness").hide();
                //                $("#biFocalPowers").hide();
                $("#thickness").hide();
                $("#biFocalThickness").hide();
                biFocalThickness.attr("checked", false);
                brandtThickness.attr("checked", false);
                normalThickness.attr("checked", false);
                brandbiFocalThickness.attr("checked", true);


            } else {

                $("#brandbiFocalThickness").hide();
                //                $("#biFocalPowers").hide();
                $("#thickness").hide();
                $("#biFocalThickness").hide();
                $("#brandthickness").show();


                normalThickness.attr("checked", false);
                brandbiFocalThickness.attr("checked", false);
                biFocalThickness.attr("checked", false);
                brandtThickness.attr("checked", true);


                //$("#coating-div").show();
            }

        }
        else {

            brandedCoating.attr("checked", false);
            normalCoating.attr("checked", true);
            if ($('input[name=biFocalPowers]:checked').attr('id') == 'biFocalYes') {

                $("#biFocalPowers").show();
                $("#thickness").hide();
                $("#biFocalThickness").show();
                $("#brandbiFocalThickness").hide();
                $("#brandthickness").hide();

                normalThickness.attr("checked", false);
                brandedCoating.attr("checked", false);
                brandbiFocalThickness.attr("checked", false);
                biFocalThickness.attr("checked", true);
                normalCoating.attr("checked", true);
                brandtThickness.attr("checked", false);


                //$("#coating-div").hide();
            } else {

                $("#biFocalPowers").hide();
                $("#thickness").show();
                $("#biFocalThickness").hide();
                $("#brandbiFocalThickness").hide();
                $("#brandthickness").hide();

                biFocalThickness.attr("checked", false);
                normalThickness.attr("checked", true);
                brandbiFocalThickness.attr("checked", false);
                brandtThickness.attr("checked", false);
                brandedCoating.attr("checked", false);
                normalCoating.attr("checked", true);

                //$("#coating-div").show();
            }
        }

        initPriceRows();
        //        renderCoatingPriceRow();
    });

  function renderCoatingPriceRow() {
    var selectedCoating = $('.coating:checked');
    var coPrice = $.trim(selectedCoating.attr('price'));
    var coValue = $.trim(selectedCoating.attr('value'));

    $("#coatingLabel").html(coValue);
    if (coPrice == 0) {
      $("#coatingPriceLabel").html("included");
    } else {
      $("#coatingPriceLabel").html("Rs. " + coPrice * 2);
    }
    $("#coatingPrice").val(coPrice);

    renderTotalPrice();
  }

  //  $(".eyeparamselect").change(function(event) {
  //    var select = $(event.target);
  //    var price = $.trim(select.find(":selected").attr('price'));
  //    var eyeClass = $.trim(select.attr('class'));
  //
  //    var isLeftLensPrice = eyeClass.indexOf("right") == 999;
  //    renderLensPriceRows(isLeftLensPrice, parseFloat(price));
  //
  //  });

$("#addPowers").click(function() {

        var brandedCoating = $('input[default=brandedcoating]');
        var normalCoating = $('input[default=coating]');
        brandedCoating.attr("checked", false);
        normalCoating.attr("checked", true);
        if ($('input[name=biFocalPowers]:checked').attr('id') == 'biFocalNo') {
            $("#biFocalNo").click();

        }
        tabs.tabs('select', 2);
        initPriceRows();
    });

  $("#biFocalYes").click(function() {

        $("#biFocalPowers").show();
        $("#thickness").hide();
        $("#biFocalThickness").show();
        $("#brandbiFocalThickness").hide();
        $("#brandthickness").hide();

        var biFocalThickness = $('input[default=bfThickness]');
        var brandedCoating = $('input[default=brandedcoating]');
        var normalCoating = $('input[default=coating]');
        brandedCoating.attr("checked", false);
        biFocalThickness.attr("checked", true);
        normalCoating.attr("checked", true);
        biFocalThickness.click();

  });

  $("#biFocalNo").click(function() {
     $("#biFocalPowers").hide();
        $("#thickness").show();
        $("#biFocalThickness").hide();
        $("#brandbiFocalThickness").hide();
        $("#brandthickness").hide();
        var normalThickness = $('input[default=thickness]');
        var brandedCoating = $('input[default=brandedcoating]');
        var normalCoating = $('input[default=coating]');

        normalThickness.attr("checked", true);
        brandedCoating.attr("checked", false);
        normalCoating.attr("checked", true);
        normalCoating.click();
  });
  function _addGlassToCart(res) {
    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
      $('.message .line1').html("<strong>" + res.data.name + "</strong> has been added to your shopping cart");
      $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
      $('.progressLoader').hide();

      $('.message').css("top", "70px");
      $('.message').animate({
        opacity: 1
      }, 500);
    }
    if(res.code == '<%=HealthkartResponse.STATUS_ERROR%>'){
      alert(res.message);
    }
  }
  
  $("#buyFrame").click(function() {
  	 var params = {}, idx = 0, data={};
  	 var variantList = [{
  	 	qty : 1,
  	 	selected : true
  	 }];
  	 params.productVariantList = variantList;
     params.productReferrerId = $('#productReferrerId').val();
    
  	$.ajax({
      type: "POST",
      url: "/core/cart/AddToCart.action?productVariantList[0]="+$("#variantId").val(),
      data: params,
      dataType: 'json',
      success: _addGlassToCart,
      error:function onError() {
        alert('Could not add product to cart please try again');
      }
    });
  });

	$('.biFocalRadio').change(function(){
		if ($('input[name=biFocalPowers]:checked').attr('id') == 'biFocalNo') {
			$.each($(".biFocalParam"), function(index, value) {
				$(value).val(999);
			});
		}
	});

  $("#buyNow").click(function() {
    var params = {},data = [],idx = 0;

    $.each($(".eyeparamselect"), function(index, value) {
      var select = $(value);
      var value = $.trim(select.find(":selected").attr('value'));
      //var price = $.trim(select.find(":selected").attr('price'));
      var selectValId = $.trim(select.find(":selected").attr('valueId'));
      var id = select.attr('id');
      if (value != 999) {
        var values = {};
        values.optionId = id;
        values.valueId = selectValId;
        //values.price = price;
        //values.value = value;
        data[idx] = values;
        idx++;
      }
    });

    var selectedThickness = $('.thickness:checked');
    //var thPrice = $.trim(selectedThickness.attr('price'));
    // var thValue = $.trim(selectedThickness.attr('value'));
    var thId = $.trim(selectedThickness.attr('optionId'));
    var thValId = $.trim(selectedThickness.attr('valueId'));

    var values = {};
    values.optionId = thId;
    values.valueId = thValId;
    //values.price = thPrice;
    //values.value = thValue;
    data[idx] = values;

    var selectedCoating = $('.coating:checked');
    //var coPrice = $.trim(selectedCoating.attr('price'));
    //var coValue = $.trim(selectedCoating.attr('value'));
    var coValId = $.trim(selectedCoating.attr('valueId'));
    var coId = $.trim(selectedCoating.attr('optionId'));
    idx++;
    var values = {};
    values.optionId = coId;
    // values.price = coPrice;
    //values.value = coValue;
    values.valueId = coValId;
    data[idx] = values;
    params.configValues = data;
    params.variantId = $("#variantId").val();
    params.productReferrerId = $('#productReferrerId').val();
    var form = this.form;
    $.ajax({
      type: "POST",
      url: form.action,
      data: params,
      dataType: 'json',
      success: _addGlassToCart,
      error:function onError() {
        alert('Could not add product to cart please try again');
      }
    });
  })

  //  function renderLensPriceRows(isLeftLensRow, price) {
  //
  //    if (isLeftLensRow) {
  //      if (price > 0) {
  //        $("#leftLensPriceLabel").html("Rs. " + price);
  //        $("#leftLensPrice").val(price);
  //        $("#leftLensPriceRow").show();
  //      } else {
  //        $("#leftLensPriceRow").hide();
  //        $("#leftLensPrice").val(0);
  //      }
  //    } else {
  //      if (price > 0) {
  //        $("#rightLensPriceLabel").html("Rs. " + price);
  //        $("#rightLensPrice").val(price);
  //        $("#rightLensPriceRow").show();
  //      } else {
  //        $("#rightLensPriceRow").hide();
  //        $("#rightLensPrice").val(0);
  //      }
  //    }
  //
  //    renderTotalPrice();
  //  }

  initPage();

});
</script>
</s:layout-definition>
