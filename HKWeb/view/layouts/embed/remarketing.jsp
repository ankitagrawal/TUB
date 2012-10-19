<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-definition>
<%
    // remarketing labels are category labels as of now
    String remarketingLabels = (String) pageContext.getAttribute("labels");
    pageContext.setAttribute("labels", remarketingLabels);

    String brandLabel = (String) pageContext.getAttribute("brandLabel");
    pageContext.setAttribute("brandLabel", brandLabel);

    String productNameLabel = (String) pageContext.getAttribute("productNameLabel");
    pageContext.setAttribute("productNameLabel", productNameLabel);

    List<String> remarketingLabelList = new ArrayList<String>();
    if (StringUtils.isNotBlank(remarketingLabels)) {
        for (String s : StringUtils.split(remarketingLabels, ',')) {
            if (StringUtils.isNotBlank(s)) remarketingLabelList.add(s.trim());
        }
    }
    pageContext.setAttribute("remarketingLabelList", remarketingLabelList);
%>
<c:set var="codeSet" value="<%=false%>"/>

<%-- Site Wide --%>
<%-- For all HK Users --%>
<s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="rX2jCMje6QIQuLjI5QM" id="1018305592"/>

<%-- CATEGORIES --%>

<%-- NUTRITION --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'fat-burner') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="bj8FCPrulAMQztXQ0wM" id="980691662"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'nutrition') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="KayACKKL8QIQztXQ0wM" id="980691662"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>

<%-- SPORTS --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'gym-balls') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="izB1CIXD3wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'gym-gloves') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="ZhUfCP3D3wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'support-gear') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="khjICP3yzAIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'strength-training') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="17ZGCPXzzAIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'sports-bag') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="IHe4CI3C3wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'other-balls') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="G8ZtCK2-3wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'basketball') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="JxV3CLW93wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'football-shoes') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="vQybCNW53wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'jogging-shoes') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="AI_0CN243wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>

<c:if test="${hk:collectionContains(remarketingLabelList, 'rackets') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="yLPMCPWO2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'shuttles') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="yLPMCPWO2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'basketball-shoes') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="aTA-COWQ2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'gloves') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="YsqLCN2R2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'court-shoes') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="oW3WCM2T2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'cricket-shoes') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="VRX_CMWU2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'dumbbells') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="QORSCL2V2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'dumbbells') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="QORSCL2V2gMQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'footwear-master') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="3gibCOW33wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'swimming-equipments') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="YGH0CL283wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'boxing') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="YgUqCMW73wIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>




<c:if test="${hk:collectionContains(remarketingLabelList, 'fitness-accessories') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="8V4PCIXyzAIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('yonex', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="TKLfCIWN2gMQ44T54AM" id="1008616035"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('cosco', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="6IWGCNWS2gMQ44T54AM" id="1008616035"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('prince', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="UobECKWY2gMQ44T54AM" id="1008616035"/>
</c:if>

<%-- DIABETES --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'testing-supplies') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="FBt9CJ3HtAUQ-4DL4QM" id="1009959035"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<%--<c:if test="${hk:collectionContains(remarketingLabelList, 'diabetes') && !codeSet}">--%>
<%--<s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="bH7jCKqK8QIQztXQ0wM" id="980691662"/>--%>
<%--<c:set var="codeSet" value="<%=true%>"/>--%>
<%--</c:if>--%>

<%-- EYE --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'sunglasses') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="qNxXCMnP7AIQn_iQ4gM" id="1011104799"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'lenses') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="qNYjCNHO7AIQn_iQ4gM" id="1011104799"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'eyeglasses') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="LdQvCJHi8gIQn_iQ4gM" id="1011104799"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'eye') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="WHHFCKGB7QIQn_iQ4gM" id="1011104799"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('v+rod', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="7S0iCLn-ggQQn_iQ4gM" id="1011104799"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('black+hammer', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="U5aTCLH_ggQQn_iQ4gM" id="1011104799"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('teenager', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="HnKzCKmAgwQQn_iQ4gM" id="1011104799"/>
</c:if>

<%-- HOME DEVICES --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'stethoscope') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="-R9gCNrCyQMQluHixAM" id="949530774"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'clinical-supplies') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="NBrTCNLDyQMQluHixAM" id="949530774"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'blood-pressure') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="bzUfCMrEyQMQluHixAM" id="949530774"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>

<%-- BEAUTY --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'hair-loss-treatment') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="vxqLCNmk5wMQ_4XqxAM" id="949650175"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContainsBoth(remarketingLabelList, 'fragrances', 'bath-body') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="PxPuCOmi5wMQ_4XqxAM" id="949650175"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContainsBoth(remarketingLabelList, 'fragrances', 'men') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="g-wSCPGh5wMQ_4XqxAM" id="949650175"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'beauty') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="x1edCJno5wMQ_4XqxAM" id="949650175"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>

<%-- PERSONAL CARE --%>
<c:if test="${hk:collectionContainsBoth(remarketingLabelList, 'personal-care', 'women') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="wBVSCJv7ggMQtYzM4QM" id="1009976885"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'elderly-care') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="Az6kCJP8ggMQtYzM4QM" id="1009976885"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'oral-hygiene') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="Tr_eCIv9ggMQtYzM4QM" id="1009976885"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>

<%-- BABY --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'baby') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="BH1LCIKP8QIQztXQ0wM" id="980691662"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>

<%-- PARENTING --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'am-i-pregnant') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="BgNSCLyJxgMQpIrJ3QM" id="1001538852"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'skin-care') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="HXYoCLSKxgMQpIrJ3QM" id="1001538852"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'breast-feeding') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="Xp3LCKyLxgMQpIrJ3QM" id="1001538852"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'maternity-belts') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="J6KPCKSMxgMQpIrJ3QM" id="1001538852"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'sippers-mugs') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="w-8TCJSOxgMQpIrJ3QM" id="1001538852"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'parenting') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="hTFCCMSIxgMQpIrJ3QM" id="1001538852"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>


<%-- SERVICES --%>
<c:if test="${hk:collectionContains(remarketingLabelList, 'health-checkups') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="zKMICKPeuQIQ9aL74AM" id="1008652661"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'hair-spa-beauty') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="JSwZCJvfuQIQ9aL74AM" id="1008652661"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'gyms-health-centres') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="g05-CMPDugIQ9aL74AM" id="1008652661"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>
<c:if test="${hk:collectionContains(remarketingLabelList, 'services') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="r8tKCKvduQIQ9aL74AM" id="1008652661"/>
    <c:set var="codeSet" value="<%=true%>"/>
</c:if>



<%-- BRANDS --%>
<%-- BEAUTY --%>
<c:if test="${hk:equalsIgnoreCase('vlcc', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="-p3wCIGg5wMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('gillette', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="7USkCPmg5wMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('neutrogena', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="6WBkCMmm5wMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('loreal+paris', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="NSpnCMGn5wMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('garnier', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="NwmqCLGp5wMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('gatsby', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="u1qTCKmq5wMQ_4XqxAM" id="949650175"/>
</c:if>
<!--<c:if test="${hk:equalsIgnoreCase('lotus herbals', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="tFq_CJms5wMQ_4XqxAM" id="949650175"/>
</c:if>
-->
<c:if test="${hk:equalsIgnoreCase('lotus+herbals', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="tFq_CJms5wMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('absolute', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="ExjZCOGR7QMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('fabindia', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="4DUfCNmS7QMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('biotique', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="2_kYCNGT7QMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('revlon', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="-InxCMmU7QMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('braun', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="-2fKCOmawgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('babyliss', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="fxpDCPGZwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('andis', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="9cMxCPmYwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('ponds', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="CRq0CIGYwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('remington', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="DSTOCImXwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('street+wear', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="hdajCJGWwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('axe', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="4iYeCJmVwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('khadi', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="e83JCKGUwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('colorbar', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="IXhPCKmTwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('h2o', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="yGIBCLGSwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('dove', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="I6R9CLmRwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('vichy', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="dFZ_CIH54QQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('nivea', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="1o8dCMmPwgQQ_4XqxAM" id="949650175"/>
</c:if>
<!--<c:if test="${hk:equalsIgnoreCase('aroma-magic', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="dosSCNGOwgQQ_4XqxAM" id="949650175"/>
</c:if>
-->
<c:if test="${hk:equalsIgnoreCase('aroma+magic', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="dosSCNGOwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('shahnaz', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="BPJFCNmNwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('scholl', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="5HaGCOGMwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('elle18', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="oszMCOmLwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('lakme', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="UXQoCPGKwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('philips', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="kkrACPmJwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('kaya+skin+clinic', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="CzgdCIGJwgQQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('maybelline', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="UMOACLmo5wMQ_4XqxAM" id="949650175"/>
</c:if>
<%-- EYE --%>
<c:if test="${hk:equalsIgnoreCase('bausch+&+lomb', brandLabel) || hk:equalsIgnoreCase('bausch & lomb', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="pA4ECKnE7AIQn_iQ4gM" id="1011104799"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('acuvue', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="7ehoCKHF7AIQn_iQ4gM" id="1011104799"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('soflens', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="izBlCJnG7AIQn_iQ4gM" id="1011104799"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('rayban', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="DrdhCJHH7AIQn_iQ4gM" id="1011104799"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('fastrack', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="6e87COHM7AIQn_iQ4gM" id="1011104799"/>
</c:if>
<c:if test="${hk:equalsIgnoreCase('aislin', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="_opyCKGf7QIQn_iQ4gM" id="1011104799"/>
</c:if>

<%-- PRODUCT --%>
<c:if test="${hk:containsIgnoreCase(productNameLabel, 'olay regenerist')}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="7vs3CNGl5wMQ_4XqxAM" id="949650175"/>
</c:if>
<c:if test="${hk:containsIgnoreCase(productNameLabel, 'accu-chek active') || hk:containsIgnoreCase(productNameLabel, 'accu chek active')}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="LbkRCLXEtAUQ-4DL4QM" id="1009959035"/>
</c:if>

</s:layout-definition>