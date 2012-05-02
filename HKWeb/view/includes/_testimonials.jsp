<%@include file="/includes/_taglibInclude.jsp" %>
<div class="happy_customer01">
  <%
    double r = Math.random();
    int testimonialIndex = (int) (r * 5);

    String[][] testimonials = {
        {"I would like to THANK YOU and especially my heartiest THANKS to your executive \"SHEETAL\" for making my FIRST TIME purchasing experience at healthkart.com truly GREAT and very much pleasing!! :)", "Anirban Kar"},
        {"Well i must say ...healthkart provide genuine products ...n on the top of that hv been served with the best service provider ever ... ..these guys r great in dealing with their customers ...thumbs up for them.", "Samuel Jackson"},
        {"Thank you so much for the swift delivery of the items I've ordered online. I'll definitely recommend this site to my friends and family", "Tom"},
        {"Thank you so much for the swift delivery of the items I've ordered online. I'll definitely recommend this site to my friends and family", "Tom"},
        {"This was my 1st exp but unforgettable because of on time or you can say before time commitment delivery.Certainly i will refer this to my other friends to explore.But equally advice you & team to keep delivery,commitment and quality excellent in all aspects.", "Brajesh"}
    };
  %>
<div class="customer_feedback">"<%=testimonials[testimonialIndex][0]%>"  <i>(<%=testimonials[testimonialIndex][1]%>)</i></div>
<%--<div class="customer_seemore"><a href="#"> See more >> </a></div>--%>
</div>
