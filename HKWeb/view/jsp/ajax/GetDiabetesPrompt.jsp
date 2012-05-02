<%@page import="mhc.servlet.action.DiabetesAlgorithmAction"%>

<%
	String responseText = new String("");
	String hba1c = (String) request.getParameter("hba1c");
	String fbg = (String) request.getParameter("fbg");
	String ppbg = (String) request.getParameter("ppbg");
//	System.out.println(hba1c + ":" + fbg + ":" + ppbg);

	try {
		responseText += (new DiabetesAlgorithmAction())
				.getDiabetesRecommendation(Double.parseDouble(hba1c),
						Double.parseDouble(fbg), Double
								.parseDouble(ppbg), 0, 0, 0, "", 0.0,
						0, 0, "", 0.0);
	} catch (Exception e) {

	}

//	System.out.println("responseText: " + responseText);

	out.print(responseText);
%>