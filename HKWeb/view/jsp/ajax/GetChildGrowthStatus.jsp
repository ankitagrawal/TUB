<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="mhc.servlet.action.GrowthTrackerAction"%>

<%
	String responseText = new String("<i>(in percentile)</i><br>");
	String dob = (String) request.getParameter("dob");
	String gender = (String) request.getParameter("gender");
	String height = (String) request.getParameter("height");
	String weight = (String) request.getParameter("weight");
	String hc = (String) request.getParameter("hc");

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar dobDate = Calendar.getInstance();
	dobDate.setTime(sdf.parse(dob));
	Calendar today = Calendar.getInstance();
	Long diff = today.getTimeInMillis() - dobDate.getTimeInMillis();
	Long yearInMils = 365 * 24 * 60 * 60 * 1000L;
	double ageInFraction = (double) diff / (double) yearInMils;
	String age = String.valueOf(ageInFraction);

	/*System.out.println(age + ":" + gender + ":" + height + ":" + weight
			+ ":" + hc);*/
	GrowthTrackerAction gta = new GrowthTrackerAction();
	responseText += gta.getPercentile(gender, age, height, weight, hc);
//	System.out.println("responseText: " + responseText);

	out.print(responseText);
%>