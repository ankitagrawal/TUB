<%@page import="mhc.pojo.ExchangeList"%>
<%@page import="mhc.servlet.action.DietFrameworkAction"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<%
	String responseText = "<ul>";
	String category = request.getParameter("category");
	String item = request.getParameter("item");
	String elmRank = request.getParameter("elmRank");
//	System.out.println(category + " : " + item + " : " + elmRank);

	List<ExchangeList> exList = (new DietFrameworkAction())
			.getExchangeList(category, item);
	for (Iterator iterator = exList.iterator(); iterator.hasNext();) {
		ExchangeList exl = (ExchangeList) iterator.next();
		String itemName = exl.getItem();
		responseText += "<li tabindex='-1'><a onclick='javascript:setExchangeRate(\""
				+ exl.getCalories()
				+ "\", \""
				+ elmRank
				+ "\", \""
				+ itemName
				+ "\");'>"
				+itemName				
				+ "</a><input type='hidden' value="+exl.getCalories()+"><input type='hidden' value="+elmRank+"></li>";
	}

	responseText += "</ul> ";

	out.print(responseText);
%>