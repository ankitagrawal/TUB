<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %><%
    DateTime dateTime = new DateTime();
    Date endOfOfferDate = new Date(new DateTime(2013, 3, 31, 23, 59, 59, 59).getMillis());
    if (dateTime.isBefore(endOfOfferDate.getTime())) {
%>
<div class="box_alert_thin" style="line-height: 1.6em;">
  <strong>Note</strong>: We are upgrading our warehouse operations over the weekend and order processing may be delayed by 2-3 days.<br/>
  Our apologies for the inconvenience. We expect better and faster services in the future as a result of this upgrade.
</div>
<%
    }
%>
