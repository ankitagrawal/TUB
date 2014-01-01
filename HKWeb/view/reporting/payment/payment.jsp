<%@ page import="com.hk.pact.dao.BaseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="org.hibernate.SQLQuery" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.math.BigInteger" %>
<html>
<head>
    <title>Payment Performance</title>
    <script type="text/javascript" src="jscharts.js"></script>

</head>
<body>
<h1>Payment Performance : <%=new Date()%>
</h1>
<%
    // Set refresh, autoload time as 5 minutes
    response.setIntHeader("Refresh", 5 * 60);

    BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);

    //Total Payment Attempts
    String maxPaymentSQL = "SELECT count(*) as totalPayments FROM payment " +
            "where date(payment_date) = date(now()) group by hour(payment_date) order by totalPayments desc limit 1";
    SQLQuery maxPaymentQuery = baseDao.createSqlQuery(maxPaymentSQL);
    BigInteger maxPayments = (BigInteger) maxPaymentQuery.uniqueResult();
    if (maxPayments == null)
        maxPayments = new BigInteger("100");

    //Total Payment Attempts
    String totalPaymentSQL = "SELECT hour(payment_date) as hour, count(*) as totalPayments FROM payment " +
            "where date(payment_date) = date(now()) group by hour(payment_date)";
    SQLQuery totalPaymentQuery = baseDao.createSqlQuery(totalPaymentSQL);
    List<Object[]> totalPaymentList = totalPaymentQuery.list();

    //Total Payment Successfuls
    String totalPaymentSuccessfulSQL = "SELECT hour(payment_date) as hour, count(*) as totalPayments FROM payment " +
            "where date(payment_date) = date(now()) and payment_status_id in (2,3,7) group by hour(payment_date)";
    SQLQuery totalPaymentSuccessfulQuery = baseDao.createSqlQuery(totalPaymentSuccessfulSQL);
    List<Object[]> totalPaymentSuccessfulList = totalPaymentSuccessfulQuery.list();

    //Total Online Attempts
    String totalOnlinePaymentSQL = "SELECT hour(payment_date) as hour, count(*) as totalPayments FROM payment " +
            "where date(payment_date) = date(now()) and payment_mode_id = 1000 and payment_status_id <= 5 group by hour(payment_date)";
    SQLQuery totalOnlinePaymentQuery = baseDao.createSqlQuery(totalOnlinePaymentSQL);
    List<Object[]> totalOnlinePaymentList = totalOnlinePaymentQuery.list();

    //Total Online Successful
    String totalOnlinePaymentSuccessfulSQL = "SELECT hour(payment_date) as hour, count(*) as totalPayments FROM payment " +
            "where date(payment_date) = date(now()) and payment_mode_id = 1000 and payment_status_id in (2,3) group by hour(payment_date)";
    SQLQuery totalOnlinePaymentSuccessfulQuery = baseDao.createSqlQuery(totalOnlinePaymentSuccessfulSQL);
    List<Object[]> totalOnlinePaymentSuccessfulList = totalOnlinePaymentSuccessfulQuery.list();
%>
<div id="chart_container">Loading chart...</div>
<script type="text/javascript">
    var myChart = new JSChart('chart_container', 'line', '');
    myChart.setDataArray([
        <%
       for (Object[] totalPayment : totalPaymentList) {
            %>
        [<%=totalPayment[0]%>, <%=totalPayment[1]%>],
        <%
}
        %>
    ],
            'Total Payments Attempts'
            );
    myChart.setLineColor('#37379E', 'Total Payments Attempts');

    myChart.setDataArray([
        <%
       for (Object[] totalPayment : totalPaymentSuccessfulList) {
            %>
        [<%=totalPayment[0]%>, <%=totalPayment[1]%>],
        <%
}
        %>
    ],
            'Total Payments Successful'
            );
    myChart.setLineColor('#C83636', 'Total Payments Successful');

    myChart.setDataArray([
        <%
       for (Object[] totalPayment : totalOnlinePaymentList) {
            %>
        [<%=totalPayment[0]%>, <%=totalPayment[1]%>],
        <%
}
        %>
    ],
            'Total Online Attempts'
            );
    myChart.setLineColor('#FFD700', 'Total Online Attempts');

    myChart.setDataArray([
        <%
       for (Object[] totalPayment : totalOnlinePaymentSuccessfulList) {
            %>
        [<%=totalPayment[0]%>, <%=totalPayment[1]%>],
        <%
}
        %>
    ],
            'Total Online Successful'
            );
    myChart.setLineColor('#008000', 'Total Online Successful');

    myChart.colorize(['#3E90C9','#3E90C9','#3E90C9','#3E90C9','#3E90C9','#3E90C9','#3E90C9','#3E90C9','#3E90C9','#3E90C9','#3E90C9']);
    myChart.setSize(1100, 600);
    myChart.setTitle('Payment Performance');
    myChart.setTitleColor('#37379E');
    myChart.setTitleFontSize(10);
    myChart.setLineOpacity(1);
    myChart.setLineWidth(2);
    myChart.setAxisValuesColor('#37379E');
    myChart.setAxisNameX('Hour');
    myChart.setAxisNameY('Payments');
    myChart.setAxisColor('#B7C611');
    myChart.setGridColor('#B7C611');
    myChart.setFlagColor('#37379F');
    myChart.setFlagRadius(4);
    myChart.setAxisValuesNumberX(<%=totalPaymentList.size()%>);
    myChart.setAxisValuesNumberY(11);
    myChart.setIntervalEndY(<%=maxPayments%>);
    myChart.setIntervalStartY(0);
    myChart.setLegendShow(true);
    myChart.draw();
</script>
</body>
</html>