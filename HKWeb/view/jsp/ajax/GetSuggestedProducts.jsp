<%@page import="mhc.servlet.action.PersonalCareProductAction"%>
<%@page import="mhc.pojo.PersonalCareProduct"%>
<%@page import="mhc.servlet.action.NutriProductAction"%>
<%@page import="mhc.pojo.NutriProduct"%>
<%@page import="mhc.servlet.action.HHDProductAction"%>
<%@page import="mhc.pojo.HHDProduct"%>
<%@page import="mhc.servlet.action.WMProductAction"%>
<%@page import="mhc.pojo.WMProduct"%>
<%@page import="mhc.servlet.action.DiabetesProductAction"%>
<%@page import="mhc.pojo.DiabetesProduct"%>
<%@page import="mhc.enums.Level1Category"%>
<%@page import="mhc.servlet.action.BabyProductAction"%>
<%@page import="mhc.pojo.BabyProduct"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<%
	String responseText = "<ul>";
	String product = (String) request.getParameter("product");
//	System.out.println("Product Name Suggestion: " + product);

	String level1Category = (String) request
			.getParameter("level1Category");
//	System.out.println("level1Category: " + level1Category);

	if (level1Category != null) {
		if (level1Category.equals(Level1Category.Baby.toString())) {
			List<BabyProduct> prodList = (new BabyProductAction())
					.getSuggestedProducts(product);
			if (prodList.size() > 0) {
				for (Iterator<BabyProduct> iterator = prodList
						.iterator(); iterator.hasNext();) {
					BabyProduct prod = (BabyProduct) iterator.next();
					String detailedBrand = prod.getProduct();

					responseText += "<li tabindex='-1'><a onclick='javascript:setProduct(\""
							+ prod.getLevel2Category()
							+ "\", \""
							+ detailedBrand
							+ "\", \""
							+ prod.getId()
							+ "\"); '>"
							+ detailedBrand.toUpperCase().replace(
									product.toUpperCase(),
									"<b>" + product.toUpperCase()
											+ "</b>")
							+ "</a><input type='hidden' value='"
							+ prod.getLevel2Category()
							+ "'><input type='hidden' value='"
							+ detailedBrand
							+ "'><input type='hidden' value='"
							+ prod.getId() + "'></li>";
				}
			}
		} else if (level1Category.equals(Level1Category.Diabetes
				.toString())) {
			List<DiabetesProduct> prodList = (new DiabetesProductAction())
					.getSuggestedProducts(product);
			if (prodList.size() > 0) {
				for (Iterator<DiabetesProduct> iterator = prodList
						.iterator(); iterator.hasNext();) {
					DiabetesProduct prod = (DiabetesProduct) iterator
							.next();
					String detailedBrand = prod.getProduct();

					responseText += "<li tabindex='-1'><a onclick='javascript:setProduct(\""
							+ prod.getLevel2Category()
							+ "\", \""
							+ detailedBrand
							+ "\", \""
							+ prod.getId()
							+ "\"); '>"
							+ detailedBrand.toUpperCase().replace(
									product.toUpperCase(),
									"<b>" + product.toUpperCase()
											+ "</b>")
							+ "</a><input type='hidden' value='"
							+ prod.getLevel2Category()
							+ "'><input type='hidden' value='"
							+ detailedBrand
							+ "'><input type='hidden' value='"
							+ prod.getId() + "'></li>";
				}
			}
		} else if (level1Category
				.equals(Level1Category.WeightManagement.toString())) {
			List<WMProduct> prodList = (new WMProductAction())
					.getSuggestedProducts(product);
			if (prodList.size() > 0) {
				for (Iterator<WMProduct> iterator = prodList.iterator(); iterator
						.hasNext();) {
					WMProduct prod = (WMProduct) iterator.next();
					String detailedBrand = prod.getProduct();

					responseText += "<li tabindex='-1'><a onclick='javascript:setProduct(\""
							+ prod.getLevel2Category()
							+ "\", \""
							+ detailedBrand
							+ "\", \""
							+ prod.getId()
							+ "\"); '>"
							+ detailedBrand.toUpperCase().replace(
									product.toUpperCase(),
									"<b>" + product.toUpperCase()
											+ "</b>")
							+ "</a><input type='hidden' value='"
							+ prod.getLevel2Category()
							+ "'><input type='hidden' value='"
							+ detailedBrand
							+ "'><input type='hidden' value='"
							+ prod.getId() + "'></li>";
				}
			}
		} else if (level1Category
				.equals(Level1Category.HomeHealthDevices.toString())) {
			List<HHDProduct> prodList = (new HHDProductAction())
					.getSuggestedProducts(product);
			if (prodList.size() > 0) {
				for (Iterator<HHDProduct> iterator = prodList
						.iterator(); iterator.hasNext();) {
					HHDProduct prod = (HHDProduct) iterator.next();
					String detailedBrand = prod.getProduct();

					responseText += "<li tabindex='-1'><a onclick='javascript:setProduct(\""
							+ prod.getLevel2Category()
							+ "\", \""
							+ detailedBrand
							+ "\", \""
							+ prod.getId()
							+ "\"); '>"
							+ detailedBrand.toUpperCase().replace(
									product.toUpperCase(),
									"<b>" + product.toUpperCase()
											+ "</b>")
							+ "</a><input type='hidden' value='"
							+ prod.getLevel2Category()
							+ "'><input type='hidden' value='"
							+ detailedBrand
							+ "'><input type='hidden' value='"
							+ prod.getId() + "'></li>";
				}
			}
		} else if (level1Category.equals(Level1Category.Nutrition
				.toString())) {
			List<NutriProduct> prodList = (new NutriProductAction())
					.getSuggestedProducts(product);
			if (prodList.size() > 0) {
				for (Iterator<NutriProduct> iterator = prodList
						.iterator(); iterator.hasNext();) {
					NutriProduct prod = (NutriProduct) iterator.next();
					String detailedBrand = prod.getProduct();

					responseText += "<li tabindex='-1'><a onclick='javascript:setProduct(\""
							+ prod.getLevel2Category()
							+ "\", \""
							+ detailedBrand
							+ "\", \""
							+ prod.getId()
							+ "\"); '>"
							+ detailedBrand.toUpperCase().replace(
									product.toUpperCase(),
									"<b>" + product.toUpperCase()
											+ "</b>")
							+ "</a><input type='hidden' value='"
							+ prod.getLevel2Category()
							+ "'><input type='hidden' value='"
							+ detailedBrand
							+ "'><input type='hidden' value='"
							+ prod.getId() + "'></li>";
				}
			}
		} else if (level1Category.equals(Level1Category.PersonalCare
				.toString())) {
			List<PersonalCareProduct> prodList = (new PersonalCareProductAction())
					.getSuggestedProducts(product);
			if (prodList.size() > 0) {
				for (Iterator<PersonalCareProduct> iterator = prodList
						.iterator(); iterator.hasNext();) {
					PersonalCareProduct prod = (PersonalCareProduct) iterator
							.next();
					String detailedBrand = prod.getProduct();

					responseText += "<li tabindex='-1'><a onclick='javascript:setProduct(\""
							+ prod.getLevel2Category()
							+ "\", \""
							+ detailedBrand
							+ "\", \""
							+ prod.getId()
							+ "\"); '>"
							+ detailedBrand.toUpperCase().replace(
									product.toUpperCase(),
									"<b>" + product.toUpperCase()
											+ "</b>")
							+ "</a><input type='hidden' value='"
							+ prod.getLevel2Category()
							+ "'><input type='hidden' value='"
							+ detailedBrand
							+ "'><input type='hidden' value='"
							+ prod.getId() + "'></li>";
				}
			}
		}
	}
	responseText += "</ul> ";

	out.print(responseText);
%>