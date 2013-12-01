<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>

<script>// Executes the function when DOM will be loaded fully
$(document).ready(function () {
    // hover property will help us set the events for mouse enter and mouse leave
    $('.navigation li').hover(
        // When mouse enters the .navigation element
            function () {
                //Fade in the navigation submenu
                $('ul', this).fadeIn(); 	// fadeIn will show the sub cat menu
            },
        // When mouse leaves the .navigation element
            function () {
                //Fade out the navigation submenu
                $('ul', this).fadeOut();	 // fadeOut will hide the sub cat menu
            }
            );


});
</script>

<div class="navBox">
    <ul class="navigation">
        <li><a href="${pageContext.request.contextPath}/pages/gosf.jsp">HOME</a>
        <li><a href="${pageContext.request.contextPath}/pages/nutri-gosf.jsp">NUTRITION</a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/pages/nutri-bars-gosf.jsp">BARS AND FAT BURNERS</a></li>
                <li><a href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">DIETARY SUPPLEMENTS</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/pages/nutri-workout-gosf.jsp">PRE WORKOUT</a></li>
                <li><a href="${pageContext.request.contextPath}/pages/nutri-protein-gosf.jsp">PROTEINS & MASS
                    GAINERS</a></li>
            </ul>
        </li>
        <li><a href="${pageContext.request.contextPath}/pages/sports-gosf.jsp">SPORTS & FITNESS</a></li>
        <li><a href="${pageContext.request.contextPath}/pages/diabetes-gosf.jsp">DIABETES</a></li>

        <li><a href="${pageContext.request.contextPath}/pages/homedevices-gosf.jsp">HEALTH DEVICES</a></li>
        <li><a href="${pageContext.request.contextPath}/pages/eye-gosf.jsp">EYE</a></li>
        <li><a href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">PERSONAL CARE</a></li>
        <li><a href="${pageContext.request.contextPath}/pages/beauty-gosf.jsp">BEAUTY</a></li>
        <li><a href="${pageContext.request.contextPath}/pages/parenting-gosf.jsp">PARENTING</a></li>
        <li><a href="${pageContext.request.contextPath}/pages/services-gosf.jsp">SERVICES</a></li>
    </ul>

</div>

