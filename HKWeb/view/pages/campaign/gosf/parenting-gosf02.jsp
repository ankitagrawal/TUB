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


        <div class="heading1">PARENTING</div>

       <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/parenting-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/parenting-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>

        <div class="cl"></div>


        <div class="prodBoxes">
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB344' productDesc="Baby has fun bathing with Gentle Bath Cleanser, it smells great too. "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB390' productDesc="Add extra fun to your kid's bath with these fun filled squirters"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB435' productDesc="Clean every nook and crany of the milk bottle and nipple with this cleaner, "/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB454' productDesc="Make feeding time more natural and enjoyable with this sipper shaped like a mother's teat"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB455' productDesc="Teach your child how to drink without spilling with this No Spill Flip It Sipper"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB456' productDesc="Make your toddler learn how to hold and drink slowly without spilling. Get this sipper accompanied with gripper cups"/>
<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB459' productDesc=""/>
        

         </div>

        <div class="cl"></div>

        <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/parenting-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/parenting-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

