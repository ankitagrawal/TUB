<%@page import="mhc.servlet.action.DiabetesAlgorithmAction"%>

<%
	String responseText = new String("");
	String bmi = (String) request.getParameter("bmi");
	//System.out.println(bmi);

	try {
		responseText += (new DiabetesAlgorithmAction())
				.getBMIState(Double.parseDouble(bmi));
	} catch (Exception e) {

	}

	out.print(responseText);
%>