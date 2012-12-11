<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
<link href="${pageContext.request.contextPath}/css/gosf.css" rel="stylesheet" type="text/css" />


</s:layout-component>

<s:layout-component name="breadcrumbs">
    <div class='crumb_outer'>
        <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
        &gt;
        <span class="crumb last" style="font-size: 12px;">Cyber Monday</span>

        <h1 class="title">
            Cyber Monday Great Online Shopping Festival
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


         </div>
         <div class="cl"></div>

        <div class="prodBoxes">

        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1103' productDesc='It contains multivitamins,antioxidants and minerals that improves the immune system and maintain a healthy heart.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1101' productDesc='A product that provides a mix of essential vitamins and minerals to the body. Ideal for men over the age of 50.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1128' productDesc='Easy to swallow capsules with multi vitamins & minerals, designed to promote bone formation & cardiovascular health in women.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1100' productDesc='A product that provides a mix of essential vitamins and minerals to the body. Ideal for women over the age of 50.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1025' productDesc='Herbal tonic which increases  nutrition absorption by the body, strengthening the skeletal system.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1075' productDesc='Ideal for people with sleep disorders, psychological stress and general health maintenance. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1034' productDesc='Supports energy production and also helps maintain healthy nerve and red blood cells.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1010' productDesc='Dietary supplement which is essential for healthier red blood cell metabolism, efficient nervous and immune system.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1112' productDesc='Health supplement that stimulates the immune system against diseases and infections'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1109' productDesc='Packed with Vitamin C, it helps improve vascular health, joint health and immunity system.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1017' productDesc='Improves bone-strength, bone metabolism, blood coagulation and vascular biology'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT994' productDesc='Specially formulated to protect, rejuvenate and detoxify the Liver.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT995' productDesc='Formulated with Glucosamine and Natural Antioxidants, it repairs the damaged tissues, relieving from joint and muscle pain'/>

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


         </div>
         


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

