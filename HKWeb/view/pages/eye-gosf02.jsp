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
<img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg" />
<div class="cl"></div>

    <jsp:include page="../includes/_menuGosf.jsp"/>

<div class="cl"></div>





    
    <div class="heading1">EYE</div>

   <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/eye-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/eye-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>

        <div class="cl"></div>

     
     <div class="prodBoxes">
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE005' productDesc='These lenses made with a slight tint correct the vision. The presence of Hilaficon B and non-ionic B Lens material comfort the eyes and facilitates smooth lens-lid interaction.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE004' productDesc='These lenses help in vision correction. Also capable of blocking UVA and UVB rays, they also protect the eyes.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE035' productDesc='Disposable, colored lenses to offer extra depth to the eyes and enhance beauty. They are soft on the eyes and can be worn for an entire month.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007' productDesc='Built using a special material Aergel, these lenses help in curbing spherical aberration'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE018' productDesc='Ligh blue colored lenses made from Lacreo technology which locks water ingredient in the lens for extra comfort. Blocks upto 97% UVB and 83% UVA.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE017' productDesc='It is made using hydraclear technology which prevents dehydration in the eyes and helps you spend long hours in front of the PC or in AC'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE001' productDesc='Easy to wear contact lenses to provide superior comfort and vision, used in correction of myopia and hypermetropia'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE002' productDesc='Used to correct myopia and hypermetropia. Made from polymacon, these are easy to clean and maintain'/>		
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1093' productDesc='Ultra light smart eye wear for that studios yet stylish looks.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1089' productDesc='Durable eye wear for both men and women. Funky black-blue combination for a stylish you.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1343' productDesc='Made from best quality material, this pair of rimless glasses is sure to give you a studious, sophisticated look.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1092' productDesc='Unique purple-black combination to add that spark to your looks. Made from ultra-light durable plastic. Fits all face types.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE800' productDesc='This fashionably innovative purple rimless frame is extremely light weight and durable. Tailored with spring hinges and silicone nose pads, it is designed for that extra comfort'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE411' productDesc='Made from durable plastic, these sunglasses are highly fashionable. Besides being comfortable, they prevent dust and harmful UV rays from entering into the eyes'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1351' productDesc='Extremely stylish sunglasses with big frame to protect your eyes from harmful sun rays and dust.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1616' productDesc='Sethi has not written this copy'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1626' productDesc='Brown in color, these sunglasses will ensure complete protection from sun and dust. Ideal for holidays and long-drives.'/>
	</div>
                   <div class="cl"></div>

            <div class="pages">

            <a class="next"  href="${pageContext.request.contextPath}/pages/eye-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/eye-gosf.jsp">1</a>

            <span class="pages_link">2</span>

         </div>


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

