<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/categoryBlankLanding.jsp" pageTitle="Cyber Monday Great Online Shopping Festival">

<s:layout-component name="htmlHead">
  <style type="text/css">

  
.navigation {
	margin:20px 0;
	height:25px;
	width:960px;
	background-color:#333;}

#nav, #nav ul{
    font-size:14px;
margin:0;
padding:0;
list-style-type:none;
list-style-position:outside;
position:relative;
line-height:1.5em;

}

#nav a{
display:block;
padding:0px 5px;
border:1px solid #333;
color:#fff;
text-decoration:none;
background-color:#333;
}

#nav a:hover{
background-color:#fff;
color:#333;
}

#nav li{
margin-left:20px;
float:left;
position:relative;
}

#nav ul {
position:absolute;
display:none;
width:12em;
top:1.5em;
}



#nav li ul a{
margin-left:-20px;
width:12em;
height:auto;
float:left;
}

#nav ul ul{
top:auto;
}

#nav li ul ul {
left:12em;
margin:0px 0 0 10px;
}

#nav li:hover ul ul, #nav li:hover ul ul ul, #nav li:hover ul ul ul ul{
display:none;
}
#nav li:hover ul, #nav li li:hover ul, #nav li li li:hover ul, #nav li li li li:hover ul{
display:block;
}




            a, a:hover{border-bottom:none;}
      .cl {
clear:both;}

p, h3, h1, h2, h4 {margin:0; padding:0;}

 @font-face
            {
                font-family: 'robotoregular'
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-regular-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-regular-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/roboto-regular-webfont.svg#robotoregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }

            @font-face
            {
                font-family: 'roboto_ltregular'
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-light-webfont.eot')
            ;
                src: url('${pageContext.request.contextPath}/css/roboto-light-webfont.eot?#iefix') format('embedded-opentype'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.woff') format('woff'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.ttf') format('truetype'), url('${pageContext.request.contextPath}/css/roboto-light-webfont.svg#roboto_ltregular') format('svg')
            ;
                font-weight: normal
            ;
                font-style: normal
            ;

            }


 #pageContainer {
                margin: 15px auto;
                width: 960px;
                overflow: auto;
                padding-bottom: 260px;
                font-family: 'robotoregular';
                color:#231f20;
            }


      .product {
	width:215px;
          border-bottom:none;
    text-align:left;
	float:left;
	margin:20px 15px 0 10px;
	position:relative;
          height:520px;
	}

.product  .gosf-logo { position:absolute; top:177px; right:0px;	}

.product .img{
	border:1px solid #CCC;
	width:213px;
	height:256px;
	}




.product h3, .product h2, .product .bid{
	font-size:13px;
	font-weight:normal;
	color:#333;
    padding:0;
	}
  /*.product h3 { height:23px; }*/

.product .description{
	font-size:12px;
    line-height:normal;
	font-weight:normal;
	color:#999;
	margin-bottom:20px;
    padding:0;
	}

.product h3 a{
font-size:13px;
	color:#333;
	text-decoration:none;
	}

	.product h3 a:hover{
        font-size:13px;
	color:#333;

	}

.product .price {text-align:left; margin:5px 0; font-size:18px; color:#333; line-height:normal; font-family: 'roboto_ltregular';}

.product .price span{font-size:15px; color:#999; text-decoration:line-through; font-style:normal;}

.product a.buynow {color:#000; display:inline-block; font-size:18px; padding:5px 15px; border:1px #000 solid; text-decoration:none; margin-bottom:10px;}
.product .buynow a:hover{color:#000; text-decoration:none; }
.product:hover {border-bottom:none;}

    

.heading1 {
	border-bottom:1px solid #c0c0c0;
	width:960px;
	height:40px;
	line-height:40px;
	margin:40px 0 10px 0;
	text-align:center;
	font-weight:bold;
	color:#666; font-size:20px;}
.product .img180 img { max-height:180px; max-width:180px; }

.see-more {font-size:13px;
	font-weight:normal;
	color:#999;
	margin-top:20px;
    text-decoration:none;
  }

.see-more a{
	color:#999;
    text-decoration:none;
  }

  .see-more a:hover{
	color:#000;
  }


</style>


<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.2.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/menu-gosf.js"></script>


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
<img src="${pageContext.request.contextPath}/images/GOSF/main-banner.jpg" />
<div class="cl"></div>

    <jsp:include page="../includes/_menuGosf.jsp"/>

<div class="cl"></div>





    
    <div class="heading1">EYE</div>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE007' productDesc='Built using a special material Aergel, these lenses help in curbing spherical aberration'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE017' productDesc='It is made using hydraclear technology which prevents dehydration in the eyes and helps you spend long hours in front of the PC or in AC'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE002' productDesc='Used to correct myopia and hypermetropia. Made from polymacon, these are easy to clean and maintain'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1034' productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE015' productDesc='These lenses have a high-tech Contour Intelligent Design for extra comfort. They block upto 88% of UV-A radiation and 99% of UV-B radiation'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE583' productDesc='A fashionable pair of unisex sunglasses which protects the eyes from harmful UV rays and dust.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1093' productDesc='Ultra light smart eye wear for that studios yet stylish looks.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1035' productDesc='These classic square sunglasses protect your eyes from harmful UV rays and dust. These are highly durable, made from unbreakable plastic with scratch free lenses.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1092' productDesc='Unique purple-black combination to add that spark to your looks. Made from ultra-light durable plastic. Fits all face types.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1591' productDesc='Stylish aviators suitable for driving. Protects eyes from harmful sun rays and dust.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1626' productDesc='Brown in color, these sunglasses will ensure complete protection from sun and dust. Ideal for holidays and long-drives.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE005' productDesc='These lenses made with a slight tint correct the vision. The presence of Hilaficon B and non-ionic B Lens material comfort the eyes and facilitates smooth lens-lid interaction.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE035' productDesc='Disposable, colored lenses to offer extra depth to the eyes and enhance beauty. They are soft on the eyes and can be worn for an entire month.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE018' productDesc='Ligh blue colored lenses made from Lacreo technology which locks water ingredient in the lens for extra comfort. Blocks upto 97% UVB and 83% UVA.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE002' productDesc='Used to correct myopia and hypermetropia. Made from polymacon, these are easy to clean and maintain'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE034' productDesc='Made from extra thin polymer, these colored lenses correct the vision. Can be used as a fashion accessory'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE016' productDesc='These are ultra thin, low maintenance lenses which block upto 88% of UV-A radiation and 99% of UV-B radiation from affecting the cornea.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1089' productDesc='Durable eye wear for both men and women. Funky black-blue combination for a stylish you.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1595' productDesc='These ultra-light fashionable sunglasses fit well on any face shape. Protects eyes from harmful sun rays and dirt.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1351' productDesc='Extremely stylish sunglasses with big frame to protect your eyes from harmful sun rays and dust.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE001' productDesc='Easy to wear contact lenses to provide superior comfort and vision, used in correction of myopia and hypermetropia'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1033' productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE034' productDesc='Made from extra thin polymer, these colored lenses correct the vision. Can be used as a fashion accessory'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE026' productDesc='Manufactured using hydro clear technology, these lenses ensure extra clear vision, combat Astigmatism'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE003' productDesc='Made from Polymacon, these are manufactured using "spin-cast" technology. They help in correction of myopia, hyperopia, and aphakia.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE038' productDesc='These monthly disposable colored contact lenses correct vision with style. Made from Methafilcon A 45%, they have high water content to avoid itchy/dry eyes.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE800' productDesc='This fashionably innovative purple rimless frame is extremely light weight and durable. Tailored with spring hinges and silicone nose pads, it is designed for that extra comfort'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1616' productDesc='Sethi has not written this copy'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE004' productDesc='These lenses help in vision correction. Also capable of blocking UVA and UVB rays, they also protect the eyes.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE001' productDesc='Easy to wear contact lenses to provide superior comfort and vision, used in correction of myopia and hypermetropia'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE003' productDesc='Made from Polymacon, these are manufactured using "spin-cast" technology. They help in correction of myopia, hyperopia, and aphakia.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE030' productDesc='Comfortable, easy to wear lenses which correct astigmatism. Can be worn for long hours, even while sleeping'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1343' productDesc='Made from best quality material, this pair of rimless glasses is sure to give you a studious, sophisticated look.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE303' productDesc='Bold pink colored aviators to protect the eyes from harmful UV sun rays and dust. Ideal for both formal and casual occasions'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE411' productDesc='Made from durable plastic, these sunglasses are highly fashionable. Besides being comfortable, they prevent dust and harmful UV rays from entering into the eyes'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1594' productDesc='Durable aviator style sunglasses for those who like to be fashionable. Protects eyes from harmful sun rays and dust.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1590' productDesc='These fashionable aviator style sunglasses protect the eyes from dust and sand. Also protects eyes from harmful UV rays.'/>
     <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='EYE1621' productDesc='Unanimously appealing fashionable design. Stylish yet contemporary modern looking. Gives 100% protection from UV rays.'/>
     


                   <div class="cl"></div>

     


</div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

