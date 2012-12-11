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

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/eye-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/eye-gosf02.jsp">Next →</a>

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
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1034' productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1033' productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE034' productDesc='Made from extra thin polymer, these colored lenses correct the vision. Can be used as a fashion accessory'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE026' productDesc='Manufactured using hydro clear technology, these lenses ensure extra clear vision, combat Astigmatism'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE003' productDesc='Made from Polymacon, these are manufactured using "spin-cast" technology. They help in correction of myopia, hyperopia, and aphakia.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE016' productDesc='These are ultra thin, low maintenance lenses which block upto 88% of UV-A radiation and 99% of UV-B radiation from affecting the cornea.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE015' productDesc='These lenses have a high-tech Contour Intelligent Design for extra comfort. They block upto 88% of UV-A radiation and 99% of UV-B radiation'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE030' productDesc='Comfortable, easy to wear lenses which correct astigmatism. Can be worn for long hours, even while sleeping'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE583' productDesc='A fashionable pair of unisex sunglasses which protects the eyes from harmful UV rays and dust.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE038' productDesc='These monthly disposable colored contact lenses correct vision with style. Made from Methafilcon A 45%, they have high water content to avoid itchy/dry eyes.'/>
     </div>
     <div class="cl"></div>

         <div class="pages">

            <span class="pages_link">1</span>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/eye-gosf02.jsp">2</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/eye-gosf02.jsp">Next →</a>

         </div>


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

