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
  <c:if test="${hk:collectionContains(remarketingLabelList, 'support-gear') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="khjICP3yzAIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
  </c:if>
  <c:if test="${hk:collectionContains(remarketingLabelList, 'strength-training') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="17ZGCPXzzAIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
  </c:if>
  <c:if test="${hk:collectionContains(remarketingLabelList, 'fitness-accessories') && !codeSet}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="8V4PCIXyzAIQ44T54AM" id="1008616035"/>
    <c:set var="codeSet" value="<%=true%>"/>
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
  <c:if test="${hk:equalsIgnoreCase('loreal', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="NSpnCMGn5wMQ_4XqxAM" id="949650175"/>
  </c:if>
  <c:if test="${hk:equalsIgnoreCase('garnier', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="NwmqCLGp5wMQ_4XqxAM" id="949650175"/>
  </c:if>
  <c:if test="${hk:equalsIgnoreCase('gatsby', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="u1qTCKmq5wMQ_4XqxAM" id="949650175"/>
  </c:if>
  <c:if test="${hk:equalsIgnoreCase('lotus herbals', brandLabel)}">
    <s:layout-render name="/layouts/embed/_remarketingCode.jsp" label="tFq_CJms5wMQ_4XqxAM" id="949650175"/>
  </c:if>
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