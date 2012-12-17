<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Great Online Shopping Festival">

<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css" />


</s:layout-component>

<s:layout-component name="breadcrumbs">
    <div class='crumb_outer'>
        <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
        &gt;
        <span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

        <h1 class="title">
            Great Online Shopping Festival
        </h1>
    </div>

</s:layout-component>

<s:layout-component name="metaDescription">Google Cyber Monday</s:layout-component>
<s:layout-component name="metaKeywords"></s:layout-component>


<s:layout-component name="content">


    <!---- paste all content from here--->

    <div id="pageContainer">
        <img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg"/>

        <div class="cl"></div>

        <jsp:include page="../includes/_menuGosf.jsp"/>

        <div class="cl"></div>


        <div class="heading1">DIETARY SUPPLEMENTS</div>

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>



            <span class="pages_link">8</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf09.jsp">9</a>
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf09.jsp">Next →</a>


         </div>
         <div class="cl"></div>

        <div class="prodBoxes">
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1136' productDesc='These Chews are rich in essential nutrients that aids immune and growth factors.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1445' productDesc='Rich source of fiber, this promotes healthy digestion and prevents growth of harmful bacteria in the body besides reducing hunger and leading to weight loss.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1106' productDesc='Rein in your blood pressure fluctuations and boost your immunity. Excellent for those suffering from high blood pressure'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1250' productDesc='An exotic herbal mix that improves memory retention.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1461' productDesc='Found in fishes and some seeds, Omega 3 fatty acids aid in brain development, immunity, heart function & joint support. '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1418' productDesc='This facilitates overall joint and bone health. It reduces inflammation and helps the body recover and function optimally.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT2005' productDesc='Rich in Methylsulfonylmethane, it helps maintaining flexible and strong bones and provides with essential nutrients.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1069' productDesc='Supports heart function and helps in circulation and blood flow throughout the body.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1310' productDesc='It is a combination of botanical extracts that reduce the absorption of unwanted fats and maintain healthy levels of hormones.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1309' productDesc='Contains antioxidants that protects the liver and assist in liver management of fats.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT885' productDesc='Maintains cholesterol levels within normal range and supports heart and vascular tissue from the oxidative effects of stress and aging.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1512' productDesc='Stress relieving tea which vitalizes energy levels and reduces weight.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1469' productDesc='Enriched with Gymnema Sylvestre, it fights with the problem of daibetes and arthritis.'/>					
        </div>

        <div class="cl"></div>

        

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf02.jsp">2</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>



            <span class="pages_link">8</span>
            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf09.jsp">9</a>
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf09.jsp">Next →</a>


         </div>
         


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

