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

       
        <div class="cl"></div>


        <div class="prodBoxes">        
        	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB047' productDesc="This electric breast pump gently massages the breasts first. It then mimics natural expression for more milk at any given time"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB1510' productDesc="Get a smile on your kid's face with this beautifully imaginative bag that comes with a lunch casement and a bottle case"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB130' productDesc="Preserve your baby's soft and supple skin and natural radiance with this herbal baby lotion"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB390' productDesc="Add extra fun to your kid's bath with these fun filled squirters"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB435' productDesc="Clean every nook and crany of the milk bottle and nipple with this cleaner, "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB293' productDesc="For fast, safe, and comfortable temperature readings of your kid, we recommend Farlin's Flexible Tip Digital Thermometer"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB455' productDesc="Teach your child how to drink without spilling with this No Spill Flip It Sipper"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB046' productDesc="A Breast Pump designed to mimic natural expression of the mothers breast. It ensures even milk flow with minimal effort."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB219' productDesc="Mix the right amount of milk powder. Go for Farlin Milk Powder Container"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB144' productDesc="Read your child's temperature without ever touching. Get Farlin Instant Infrared Ear Thermometer "/>
			<%--<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB2117' productDesc="Pregnancy puts a lot of stress on your back and tummy. This maternity belt ensures both comfort and safety"/>--%>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB270' productDesc="Moisturize your baby's soft and delicate skin with Farlin Baby Wet Wipes "/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB459' productDesc='Ensure your babys health with the Nuby Microwave Steam Sterilizer by keeping his food and water germ free'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB171' productDesc="Your child has all the fun eating with Pigeon Bowl and Weaning Spoon set."/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB1976' productDesc="This electric breast pump gently massages the breasts first. It then mimics natural expression for more milk at any given time"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB2132' productDesc="Prevent your child from inserting fingers in the power socket. Protect with Farlin Safety Guard For Socket"/>
			<%--<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB256' productDesc="Comb your baby's hair without hurting them, by Farlin Comb and Brush Set White"/>--%>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB456' productDesc="Make your toddler learn how to hold and drink slowly without spilling. Get this sipper accompanied with gripper cups"/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB1992' productDesc='Stroll your little one in this buggy inspired Sunbaby Stroller, having cushioned seat and a hood protecting him from sun.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB2087' productDesc='Let your child move with care and comfort in this stylish and versatile Chicco Juvenile Stroller.'/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB2147' productDesc='Calm down your crying baby with this musical toy having soft and soothing lights, and which activates to the sound of cry. '/>
			<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='BAB2158' productDesc='Help your baby sleep in the melodious music and soothing lights of Chicco Sweetheart Bear.'/>
       	</div>

        <div class="cl"></div>

       

    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

