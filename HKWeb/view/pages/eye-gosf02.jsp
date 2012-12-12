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
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1093' productDesc='Ultra light smart eye wear for that studios yet stylish looks.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1089' productDesc='Durable eye wear for both men and women. Funky black-blue combination for a stylish you.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1035' productDesc='These classic square sunglasses protect your eyes from harmful UV rays and dust. These are highly durable, made from unbreakable plastic with scratch free lenses.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1343' productDesc='Made from best quality material, this pair of rimless glasses is sure to give you a studious, sophisticated look.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1092' productDesc='Unique purple-black combination to add that spark to your looks. Made from ultra-light durable plastic. Fits all face types.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE303' productDesc='Bold pink colored aviators to protect the eyes from harmful UV sun rays and dust. Ideal for both formal and casual occasions'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE800' productDesc='This fashionably innovative purple rimless frame is extremely light weight and durable. Tailored with spring hinges and silicone nose pads, it is designed for that extra comfort'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE411' productDesc='Made from durable plastic, these sunglasses are highly fashionable. Besides being comfortable, they prevent dust and harmful UV rays from entering into the eyes'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1594' productDesc='Durable aviator style sunglasses for those who like to be fashionable. Protects eyes from harmful sun rays and dust.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1351' productDesc='Extremely stylish sunglasses with big frame to protect your eyes from harmful sun rays and dust.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1590' productDesc='These fashionable aviator style sunglasses protect the eyes from dust and sand. Also protects eyes from harmful UV rays.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1591' productDesc='Stylish aviators suitable for driving. Protects eyes from harmful sun rays and dust.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1595' productDesc='These ultra-light fashionable sunglasses fit well on any face shape. Protects eyes from harmful sun rays and dirt.'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1616' productDesc='Sethi has not written this copy'/>
		<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1621' productDesc='Unanimously appealing fashionable design. Stylish yet contemporary modern looking. Gives 100% protection from UV rays.'/>
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

