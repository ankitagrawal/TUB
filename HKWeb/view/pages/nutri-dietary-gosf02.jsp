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


        <div class="heading1">DIETARY SUPPLEMENTS</div>

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <span class="pages_link">2</span>



            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">8</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">Next →</a>


         </div>
        <div class="cl"></div>

        <div class="prodBoxes">


        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT449' productDesc='Enriched with Vitamin E and soy protein, it improves your cardiovascular system and helps remember things better'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT886' productDesc='Infused with powerful antioxidants for better vision'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1508' productDesc='Boost your heart & liver functions, combat aeging, stress & hypertension with every sip of this Ayurvedic marvel!'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1516' productDesc='With the essence of Brahmi, it serves as a brain tonic, treats insomnia and enhances mental abilities'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1510' productDesc='A natural blend of extracts that helps maintain blood sugar'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1511' productDesc='Fortifies your immune system and protects against cold, cough, asthma and viral infections.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1512' productDesc='Stress relieving tea which vitalizes energy levels and reduces weight.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1521' productDesc='A herbal extract tailormade to boost your vitality '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1599' productDesc='Meant for serious bodybuilders. Packed with the purest of amino acids for better muscle formation.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1171' productDesc='Reduces muscle breakdown and increases body weight'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT986' productDesc='A supplement that provides Creating for enhancing stamina and building muscles. Ideal for intense workouts.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT662' productDesc='These capsules contain essential vitamins and minerals including lutein and lycopene that helps promote a healthy heart.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT982' productDesc='Weight loss product that also minimizes premature ageing and improves cardiovascular health.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT657' productDesc='Improve muscle & heart function and keep osteo arthritis at bay with Calcium 600 with Vitamin D3. Ideal for all. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT665' productDesc='Combination of Calcium and Vitamin D makes it easy to absorb and improves the health of teeth and bones'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1379' productDesc='A potent source of antioxidants, it fortifies your cardiovascular system and stabilizes blood pressure.'/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1378' productDesc='Preservative-free capsule that controls blood pressure and reduces incidences of heart attacks  '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT1167' productDesc='Cleanse, detox and regulate digestion naturally! Amazing bowel movement with fibers from fruits, grains & botanicals. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT830' productDesc='Supplements your diet with Flax Oil that is rich in Omega 3 and 6 fatty acids. Flax Seed Oil help boost energy levels and ensures a healthy heart and cells. '/>
        <s:layout-render name="/layouts/embed/_productThumb200gosf.jsp" productId='NUT954' productDesc='Enriched with Omega-3 and Omega-6, it promotes overall well being and maintains healthy cardiovascular system.'/>
    </div>

        <div class="cl"></div>

        

        <div class="pages">
            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">← Previous</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf.jsp">1</a>

            <span class="pages_link">2</span>



            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">3</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf04.jsp">4</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf05.jsp">5</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf06.jsp">6</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf07.jsp">7</a>

            <a class="pages_link" href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf08.jsp">8</a>

            <a class="next"  href="${pageContext.request.contextPath}/pages/nutri-dietary-gosf03.jsp">Next →</a>


         </div>


    </div>

</s:layout-component>

<s:layout-component name="menu"> </s:layout-component>

</s:layout-render>

