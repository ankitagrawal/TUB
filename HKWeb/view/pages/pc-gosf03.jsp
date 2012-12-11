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


        <div class="heading1">PERSONAL CARE</div>

        <div class="pages">


            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

         </div>

        <div class="cl"></div>


        <div class="prodBoxes">
           
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='OPT001' productDesc="Ideal for oral and underarm use"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PA004' productDesc="An ideal product for women who face incontinence during and after pregnancy and patients who have been through vaginal surgery"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PF044' productDesc="For maximum foot protection, use this Neat Feat Diabetic Self Moulding Sole "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PF049' productDesc="Gel heel shields that provide comfort and pain relief for those who constantly have to wear high heels"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL002' productDesc="Figure out the best two days you will be most fertile. Grab this I-Sure Ovulation Strip to get pregnant for sure. "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PHL004' productDesc="keep your hands germ free with this anti-bacterial hand sanitizer "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP001' productDesc="Protect yourself with this powerful pepper formula spray."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP002' productDesc="Protect yourself with this powerful pepper formula spray."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP070' productDesc="For a thorough and gentle cleansing of your skin, use Bel Premium Cotton Balls, with Aloe Vera and Provitamin B5"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PP091' productDesc="Switch to Roots Hair Builder for thick and shiny hair instantly"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PPS009' productDesc="Keep your panties dry during summers and when you have vaginal discharge. Try these hygienic panty liners."/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS020' productDesc="Sensational pleasure for your partner with long lasting dotted condoms "/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PS050' productDesc="Climax for men makes sure the partner is left screaming"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='PW006' productDesc="Test your pregnancy in just 2 minutes with this Instant Ovulation Kit"/>
            <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='TP001' productDesc="Made of cotton and rayon, Perfect for those with high mentrual flow and hate changing napkins constantly."/>


        </div>
        <div class="cl"></div>

         <div class="pages">


            <a class="next"  href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf.jsp">1</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/pc-gosf02.jsp">2</a>

            <span class="pages_link">3</span>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

