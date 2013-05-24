<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
	boolean isSecure = SslUtil.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding.jsp"
                 pageTitle="Great Online Shopping Festival">

<s:layout-component name="htmlHead">


	<link href="${pageContext.request.contextPath}/css/gosf.css"
	      rel="stylesheet" type="text/css"/>

</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">Cyber
		Monday</span>

		<h1 class="title">Great Online Shopping Festival</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">Google Cyber Monday</s:layout-component>
<s:layout-component name="metaKeywords">Google Cyber Monday</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

<div id="pageContainer"><img
		src="${pageContext.request.contextPath}/images/GOSF/banner.jpg" alt="gosf banner"/>

<div class="cl"></div>

<jsp:include page="../includes/_menuGosf.jsp"/>

<div class="cl"></div>


<div class="heading1">PRIME DEALS</div>
<div class="prodBoxes">
    <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
                         productId='NUT469'
                         productDesc=' Rich in L-Carnitine, it burns your extra fat, reduces hunger pangs, improves metablosim, and reduces cholestrol.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
                         productId='NUT130'
                         productDesc='Provides 11g of Leucine and 13g of additional BCAAs per serving. Ideal for rapid muscle gain and recovery.'/>
	
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1600'
	                 productDesc='Suitable for people who want to lose weight, it helps burn fat, maintain stamina and improve metabolism.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1369'
	                 productDesc='Build strength and endurance with MuscleBlaze Creatine. It consistently delivers a powerful workout performance.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='RD001'
	                 productDesc='A waist shaper that supports the lumbar spine and corrects posture. Ideal for women who want to look slim and trim.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT412'
	                 productDesc='A supplement that improves the nutrient absorption power in adults and boosts energy levels. Ideal for senior citizens.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT716'
	                 productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1368'
	                 productDesc='Designed for people who want to gain mass. Packed with 52gm of high quality protein blend and 9gm of added fiber.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT716'
	                 productDesc='Keep muchies at bay with Oh Yeah Bars! Get the goodness of healthy fats and low sugar in 1 go; in 5 amazing flavors. '/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE035'
	                 productDesc='Disposable, colored lenses to offer extra depth to the eyes and enhance beauty. They are soft on the eyes and can be worn for an entire month.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='MOS001'
	                 productDesc="This Super King Size net protects your little one against dengue, malaria and other diseases caused by insects"/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT716'
	                 productDesc='Fighting all those cravings just got easier. Feed yourself 30gms of Protein, with healthy fats and low sugar with Oh Yeah Bars!'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1367'
	                 productDesc='Tailored for fitness enthusiasts engaged in heavy workouts, it has 25gm of 100% Whey Protein blend and EAAs per serving.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT905'
	                 productDesc='For bodybuilders who want to gain mass. Infused with 1000 calories, 70g proteins and 10g Branched-Chain Amino Acids.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE007'
	                 productDesc='Built using a special material Aergel, these lenses help in curbing spherical aberration'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='HB014'
	                 productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1165'
	                 productDesc='Ideal for making health drinks on the move, HK Shaker & Blender Bottle makes lump-free drinks anytime, anywhere.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='HB006'
	                 productDesc='A BP monitor to read blood pressure levels quickly.  It comes with one-year warranty.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT893'
	                 productDesc='Ideal for fitness enthusiasts working for lean muscle gain, it is a rapid absorption formula that builds strength and stamina.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE018'
	                 productDesc='Ligh blue colored lenses made from Lacreo technology which locks water ingredient in the lens for extra comfort. Blocks upto 97% UVB and 83% UVA.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1367'
	                 productDesc='Tailored for fitness enthusiasts engaged in heavy workouts, it has 25gm of 100% Whey Protein blend and EAAs per serving.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='AYUCR4'
	                 productDesc="Recover lost vitality with Ayucare Vaji Tailam. Experience the rekindling of youth and the tingle of intimacy."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT335'
	                 productDesc='A product with a potent combination of essential vitamins and minerals. Improves the strength, immunity and energy levels of the body.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE017'
	                 productDesc='It is made using hydraclear technology which prevents dehydration in the eyes and helps you spend long hours in front of the PC or in AC'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='OH006'
	                 productDesc="Get a  cleaner tongue with this carefully tested cleaner blister"/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT131'
	                 productDesc='Get the purest whey protein money can buy. All this with no carbs whatsoever. '/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT590'
	                 productDesc='Packed with low fat, high protein and the best taste in one package – MuscleTech Premium Whey Protein. Suitable for all gym goers.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='AYUCR1'
	                 productDesc="Regain lost shape with Ayucare Lavan Tailam. Get rid of unwanted fat and retained water. Losing weight was never this pleasurable."/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT717'
	                 productDesc='A complete supplement, it supports the whole body and enhances energy levels. Meant for men with an active lifestyle.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT318'
	                 productDesc='A product meant for building strength and endurance, it supplies a unique blend of Creatine, Glutamine and Taurine'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='PHL004'
	                 productDesc="keep your hands germ free with this anti-bacterial hand sanitizer "/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='HR005'
	                 productDesc='A nebulizer that stimulates easy breathing. Ideal for asthmatic patients.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='CERSUP004'
	                 productDesc='A pillow that counters stress and strain for patients suffering from cervical spondylosis.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT471'
	                 productDesc='The only protein that is useful both during bulking and cutting. Benefit from low carb, low fat & high protein.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='DS003'
	                 productDesc='Self-testing sensor comfort strips to determine blood glucose levels with greater accuracy'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1368'
	                 productDesc='Designed for people who want to gain mass. Packed with 52gm of high quality protein blend and 9gm of added fiber.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT420'
	                 productDesc='Custom made for serious body builders, build and maintain your muscles with confidence'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE001'
	                 productDesc='Easy to wear contact lenses to provide superior comfort and vision, used in correction of myopia and hypermetropia'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE002'
	                 productDesc='Used to correct myopia and hypermetropia. Made from polymacon, these are easy to clean and maintain'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT056'
	                 productDesc='Get the best carb free, lactose free whey protein known to man. Suitable for bodybuilding & athletic training alike. '/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT468'
	                 productDesc='Increased muscle mass and strength along with improved bones and ligaments'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE1034'
	                 productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1599'
	                 productDesc='Meant for serious bodybuilders. Packed with the purest of amino acids for better muscle formation.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='EYE1033'
	                 productDesc='These unique square shaped sunglasses cover the eyes, completely protecting them from dust and harmful sun rays. They are extremely light weight, durable and scratch resistant'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='METRO001'
	                 productDesc='A facial sauna that unclog pores through deep perspiration.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1151'
	                 productDesc='Boost your gym performance with MuscleTech Hydroxystim. Enhance your focus and blaze through your fitness goals. '/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT1367'
	                 productDesc='Tailored for fitness enthusiasts engaged in heavy workouts, it has 25gm of 100% Whey Protein blend and EAAs per serving.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp"
	                 productId='NUT906'
	                 productDesc='Want better focus, higher endurance and power when you work out? Just pick MuscleTech Neurocore and you will never look back.'/>
	<s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT890'
	                 productDesc="Use MusclePharm Shred Matrix to get accelerated fat burn, enhanced focus and muscular endurance. Try now to never look back again!"/>

</div>

<div class="cl"></div>

</div>


<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

<s:layout-component name="menu">
</s:layout-component>

</s:layout-render>

