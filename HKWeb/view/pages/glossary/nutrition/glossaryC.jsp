<%@ page import="net.sourceforge.stripes.util.ssl.SslUtil" %>
<%@ page import="com.hk.web.filter.WebContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>


<%
  boolean isSecure = WebContext.isSecure();
  pageContext.setAttribute("isSecure", isSecure);
%>
<s:layout-render name="/layouts/categoryBlankLanding-glossary.jsp"
                 pageTitle="HealthKart Glossary">

<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/glossary.css"
	      rel="stylesheet" type="text/css"/>
</s:layout-component>

<s:layout-component name="breadcrumbs">
	<div class='crumb_outer'><s:link
			beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
		&gt; <span class="crumb last" style="font-size: 12px;">HealthKart Glossary</span>

		<h1 class="title">HealthKart Glossary</h1>
	</div>

</s:layout-component>

<s:layout-component name="metaDescription">HealthKart Glossary</s:layout-component>
<s:layout-component name="metaKeywords">HealthKart Glossary</s:layout-component>


<s:layout-component name="content">


<!---- paste all content from here--->

    <%@include file="/includes/_menuGlossary.jsp" %>





    <div class="main-container">
        <div class="heading">C</div>
        <div class="container">



            <div class="txt-container border">
                <h2>Calcium</h2>
                <p>A vital mineral for the maintenance of strong bones and teeth, it is important for a healthy heart.
                    It is also  needed for muscle growth and contraction. Deficiency can lead to joint pain, elevated cholesterol levels, depression and high blood pressure.</p>
            </div>


            <div class="txt-container border">
                <h2>Carbohydrate</h2>
                <p>A compound made up of carbon, oxygen and hydrogen, it is a macronutrient found abundantly in most foods.  A carb typically yields 4 calories per gram.</p>
            </div>

            <div class="txt-container border">
                <h2>Carnitine</h2>
                <p>A substance related to B-vitamins, it helps transport fatty acids to mitochondria where they are burned to provide energy. It increases the use of fat as energy, thus accelerating weight loss and improving muscle strength.</p>
            </div>

            <div class="txt-container border">
                <h2>Casein / Caseinate</h2>
                <p>A milk derived protein, Casein is found in many protein-based supplements. It often comes in the form of calcium caseinate or sodium caseinate. It is rich in all three BCAAs, glutamic acid, glutamine, histidine, tyrosine, and proline.</p>
            </div>

            <div class="txt-container border">
                <h2>Cetyl Myristoleate</h2>
                <p>A medium chain fatty acid, it has been shown to help lubricate joints. It works by normalizing the function of the immune system and reducing the production of pro-inflammatory prostaglandins. </p>
            </div>

            <div class="txt-container border">
                <h2>Choline</h2>
                <p>Needed for proper transmission of nerve impulses, it aids in hormone production as well as fat and cholesterol metabolism.
                    Deficiencies result in brain function impairment, memory dysfunction, inability to digest fats and stunted growth.</p>
            </div>

            <div class="txt-container border">
                <h2>Chondroitin</h2>
                <p>An important element in the formation of cartilage, the tough yet flexible connective tissue found in the joints where it acts as a cushion.
                    Chondroitin belongs to a group of substances called glycosaminoglycans, which attach to proteins and form proteoglycans, that are vital components of cartilage tissue.
                    Chondroitin attracts water to proteoglycans and holds it there.
                    It also aids in protecting existing cartilage from premature degradation by blocking certain enzymes that destroy cartilage.</p>
            </div>


            <div class="txt-container border">
                <h2>Chromium</h2>
                <p>This mineral metabolizes glucose needed for energy, is vital in the synthesis of protein, carbs, fat and  maintains blood sugar levels. Deficiencies can lead to low blood sugar, glucose intolerance and an inadequate metabolism of amino acids.</p>
            </div>


            <div class="txt-container border">
                <h2>Cinnulin</h2>
                <p>An extract from cinnamon bark, validated to have the highest concentration of active Type-A Polymers, Cinnulin supports glucose and cholesterol management, weight management and displays potent antioxidant properties.</p>
            </div>

            <div class="txt-container border">
                <h2>Citrulline Malate</h2>
                <p>A non-essential amino acid that plays a role in nitrogen balance and metabolic processes, Citrulline Malate improves aerobic performance and capacity by influencing lactic acid metabolism and reducing fatigue.</p>
            </div>


            <div class="txt-container border">
                <h2>Coenzyme A</h2>
                <p>Called "The Body's Energizer," Coenzyme A is required by the body to utilize carbohydrates, fat and protein to produce energy. It is essential for supplying the body with an adequate amount of energy to function at the cellular level.
                    It helps to strengthen the immune system and enhances the body’s ability to cope with stress. </p>
            </div>

            <div class="txt-container border">
                <h2>Coenzyme B12</h2>
                <p>A primary active form of Vitamin B-12. </p>
            </div>

            <div class="txt-container border">
                <h2>Colostrum</h2>
                <p>This nutrient-rich, complex mixture of growth factors such as immunoglobulins,
                    glycoconjugates and other important compounds is found in only one place on Earth, in the pre-milk fluid that is secreted by mammals in their milk after giving birth.</p>
            </div>

            <div class="txt-container border">
                <h2>CLA</h2>
                <p>Conjugated Linoleic Acid is a fat burning agent which promotes the use of stored body fat as fuel.</p>
            </div>

            <div class="txt-container border">
                <h2>Complex Carbs</h2>
                <p>They are slower burning carbohydrates which generally produce a mild insulin response and are made of long chains of sugar molecules like starch and fiber.
                    Foods rich in complex carbs include vegetables, whole grains and rice.</p>
            </div>

            <div class="txt-container border">
                <h2>Copper</h2>
                <p>This mineral aids in the formation of healthy bones, nerves and joints, healing from an injury and  energy production. Deficiency leads to baldness, general weakness and impaired respiratory function.</p>
            </div>


            <div class="txt-container border">
                <h2>Creatine</h2>
                <p>One of the most studied supplements of all time, it is a high-powered metabolite used to regenerate the muscles’ ultimate energy source, ATP (adenosine triphosphate).
                    It is also responsible for improving performance in high-intensity exercise, endurance and recovery rates.</p>
            </div>


            <div class="txt-container border">
                <h2>Creatine Ethyl Ester</h2>
                <p> It is creatine monohydrate attached to an ester. Esters are organic compounds that are formed by esterification--the reaction of carboxylic acid and alcohols.
                     This results in an exceptionally soluble creatine resulting in advanced absorption, increased bioavailability and stability.</p>
            </div>

            <div class="txt-container border">
                <h2>Cyanocobalamin</h2>
                <p>Also known as Vitamin B12, it is necessary for digestion, absorption of foods, synthesis of protein and the metabolism of carbs and fats.
                    A vital source for vegetarians, its deficiency leads to bone loss, chronic fatigue, memory loss and drowsiness.</p>
            </div>


            <div class="txt-container">
                <h2>Cystine</h2>
                <p>A non-essential amino acid, it helps  detoxify the body of harmful substances and aids in iron absorption.</p>
            </div>






        </div>
    </div>



    <div class="footer01">© 2013 healthviva.com All Rights Reserved</div>







<c:if test="${not isSecure }">
	<iframe src="http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM112&param=e100&section=1&level=2"
	        scrolling="no" width="1" height="1" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</c:if>


</s:layout-component>

<s:layout-component name="menu">
</s:layout-component>

</s:layout-render>

