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
     	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE583' productDesc='A fashionable pair of unisex sunglasses which protects the eyes from harmful UV rays and dust.'/>
     	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1035' productDesc='These classic square sunglasses protect your eyes from harmful UV rays and dust. These are highly durable, made from unbreakable plastic with scratch free lenses.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE303' productDesc='Bold pink colored aviators to protect the eyes from harmful UV sun rays and dust. Ideal for both formal and casual occasions'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1594' productDesc='Durable aviator style sunglasses for those who like to be fashionable. Protects eyes from harmful sun rays and dust.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1590' productDesc='These fashionable aviator style sunglasses protect the eyes from dust and sand. Also protects eyes from harmful UV rays.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1591' productDesc='Stylish aviators suitable for driving. Protects eyes from harmful sun rays and dust.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1621' productDesc='Unanimously appealing fashionable design. Stylish yet contemporary modern looking. Gives 100% protection from UV rays.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1595' productDesc='These ultra-light fashionable sunglasses fit well on any face shape. Protects eyes from harmful sun rays and dirt.'/>     
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE297' productDesc='Geometric, thick-framed sunglasses for that aquiline look.'/>     
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE584' productDesc='Pump up your style quotient with this spiffy pair of sunglasses.'/>
     	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1034' productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1033' productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE034' productDesc='Made from extra thin polymer, these colored lenses correct the vision. Can be used as a fashion accessory'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE026' productDesc='Manufactured using hydro clear technology, these lenses ensure extra clear vision, combat Astigmatism'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE003' productDesc='Made from Polymacon, these are manufactured using "spin-cast" technology. They help in correction of myopia, hyperopia, and aphakia.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE016' productDesc='These are ultra thin, low maintenance lenses which block upto 88% of UV-A radiation and 99% of UV-B radiation from affecting the cornea.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE015' productDesc='These lenses have a high-tech Contour Intelligent Design for extra comfort. They block upto 88% of UV-A radiation and 99% of UV-B radiation'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE030' productDesc='Comfortable, easy to wear lenses which correct astigmatism. Can be worn for long hours, even while sleeping'/>		
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

