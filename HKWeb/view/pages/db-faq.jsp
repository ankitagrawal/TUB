<%--
<%@page import="mhc.pojo.FAQ"%>
<%@page import="mhc.servlet.action.FAQAction"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.hk.constants.Level1Category"%>
<div id="datacontainer"
	style="position: absolute; left: 1px; top: 10px; width: 100%"
	onMouseover="scrollspeed=0" onMouseout="scrollspeed=cache">
<%
	int sNo = 0;
	List<FAQ> faqList = (new FAQAction()).getFAQs("", Level1Category.Diabetes.toString());
	for (Iterator iterator = faqList.iterator(); iterator.hasNext();) {
		FAQ faq = (FAQ) iterator.next();
		sNo++;
%>
<p><a target="_parent" style="color:gray; font-family: Arial,Helvetica,sans-serif; font-size: 13px" 
	href="${pageContext.request.contextPath}/pages/aboutDiabetes.jsp#qId<%=faq.getId() %>"><%=faq.getQuestion()%></a></p> 
<%
	}
%>
</div>

<script type="text/javascript">
//Specify speed of scroll. Larger=faster (ie: 5)
var scrollspeed=cache=1

//Specify intial delay before scroller starts scrolling (in miliseconds):
var initialdelay=500

function initializeScroller(){
	dataobj=document.all? document.all.datacontainer : document.getElementById("datacontainer")
	dataobj.style.top="5px"
	setTimeout("getdataheight()", initialdelay)
}

function getdataheight(){
	thelength=dataobj.offsetHeight
	if (thelength==0)
		setTimeout("getdataheight()",10)
	else
		scrollDiv()
}

function scrollDiv(){
	dataobj.style.top=parseInt(dataobj.style.top)-scrollspeed+"px"
	if (parseInt(dataobj.style.top)<thelength*(-1))
		dataobj.style.top="5px"
	setTimeout("scrollDiv()",40)
}

if (window.addEventListener)
	window.addEventListener("load", initializeScroller, false)
else if (window.attachEvent)
	window.attachEvent("onload", initializeScroller)
else
	window.onload=initializeScroller

</script>--%>
