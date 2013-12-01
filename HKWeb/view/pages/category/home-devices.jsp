<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="BP Monitor, Nebulizer and other Home Health Devices in India">

<s:layout-component name="metaKeywords">blood pressure monitor, nebulizers, sleep apnea machines, thermometers, Health Products, Medical Equipments, Medical Store, Home Health Aide, Health Care Products, Health Products Shop</s:layout-component>
<s:layout-component name="metaDescription">Buy health devices like BP Monitors, nebulizers, blood sugar montiors, supports and rehab aids online. Free shipping to anywhere in India</s:layout-component>


<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Home Devices</span>

    <h1 class="title">
      Blood pressure monitors, nebulizers, cpap and other home health devices
    </h1>
  </div>
</s:layout-component>
<s:layout-component name="mainBanner">
  <div class="promotional">
    <ul id="categoryBanner" class="grid_18">
      <li>
        <a href="${pageContext.request.contextPath}/product/omron-bp-monitor-upper-arm-hem-7111/HB004">
          <img src="<hk:vhostImage/>/images/banners/HomeDevices/banner_bp5.jpg" alt="Omron 7111"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/home-devices/miscellaneous/clinical-supplies/stethoscope">
          <img src="<hk:vhostImage/>/images/banners/HomeDevices/banner_stethoscope.jpg" alt="Stethoscope"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/home-devices/respiratory-care/nebulizer">
          <img src="<hk:vhostImage/>/images/banners/HomeDevices/banner_nebulizer2.jpg" alt="Unbeatable prices on Nebulizers"/>
        </a>
      </li>
      <%--<li>
        <a href="">
          <img src="<hk:vhostImage/>/images/banners/HomeDevices/2.jpg" alt="One stop shop for personal health devices"/>
        </a>
      </li>--%>
      <li>
        <a href="${pageContext.request.contextPath}/home-devices/miscellaneous/heat-treatment/heat-therapy">
          <img src="<hk:vhostImage/>/images/banners/HomeDevices/banner_electric_blanket.jpg" alt="electric blankett"/>
        </a>
      </li>
    </ul>
    <div class='grid_6'>
      <s:link beanclass="com.hk.web.action.core.referral.ReferralProgramAction" event="pre">
        <img src="<hk:vhostImage/>/images/banners/refer_earn.jpg" alt="refer a friend and earn" class="small_banner"/>
      </s:link>
      <img src="<hk:vhostImage/>/images/banners/freeshipping_cod_250.jpg" alt="cash on delivery" class="small_banner"/>
    </div>
    <div class="floatfix"></div>
  </div>
</s:layout-component>

<s:layout-component name="topCategory">home-devices</s:layout-component>

<s:layout-component name="product_rows">
  <div class="products">
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <br/>
      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
        <s:param name="category" value="home-devices"/>
      </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
          <s:param name="category" value="home-devices"/>
          Manage Images
        </s:link>
      </div>
    </shiro:hasPermission>
    <h2>
      <a href='${pageContext.request.contextPath}/home-devices/blood-pressure/bp-monitor'>
        Top selling BP Monitors
            <span class='small'>
              (view more products)
            </span>
      </a>
      <a class='faq' href="${pageContext.request.contextPath}/pages/aboutBP.jsp" title='read FAQs related to Blood Pressure'>
        read FAQs
        <div class='icon'></div>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HB004"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HB005"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HB006"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HB010"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HB007"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HB011"/>
    <a class='more' href='${pageContext.request.contextPath}/home-devices/blood-pressure/bp-monitor'>
      view more products &rarr;
    </a>
  </div>

  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/home-devices/respiratory-care'>
        Top selling Respiratory Care Devices
            <span class='small'>
              (view more products)
            </span>
      </a>
        <%--<a class='faq' title='read FAQs related to Respiratory Care'>
          read FAQs
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HR005"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HR006"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HR007"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HR014"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HR009"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HR034"/>
    <a class='more' href='${pageContext.request.contextPath}/home-devices/respiratory-care'>
      view more products &rarr;
    </a>
  </div>
  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/home-devices/miscellaneaous'>
        Top selling Miscellaneaous Devices
            <span class='small'>
              (view more products)
            </span>
      </a>
        <%--<a class='faq' href="${pageContext.request.contextPath}/pages/aboutSleepApnea.jsp" title='read FAQs related to Sleep Apnea'>
          read FAQs
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BCKRST001"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HP028"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="BREMED015"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HT013"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="AS010"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="LIT001"/>
    <a class='more' href='${pageContext.request.contextPath}/home-devices/miscellaneaous'>
      view more products &rarr;
    </a>
  </div>

  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/home-devices/rehabilitation-aids'>
        Top selling Rehab Aids
            <span class='small'>
              (view more products)
            </span>
      </a>
        <%--<a class='faq' href="${pageContext.request.contextPath}/pages/aboutSleepApnea.jsp" title='read FAQs related to Sleep Apnea'>
          read FAQs
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="RK012"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="RA006"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="HP019"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="RW007"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="RW010"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="RZ003"/>
    <a class='more' href='${pageContext.request.contextPath}/home-devices/rehabilitation-aids'>
      view more products &rarr;
    </a>
  </div>

</s:layout-component>


<%--<s:layout-component name="rhsContent">
  <div class="main-inn-r">
    <div class="round-box-inner">
      <ul>
        <li class="left"></li>

        <li class="mid">
          <div class="blue-round-box">
            <ul>
              <li class="left"></li>

              <li class="mid" style="width:217px;">
                <h2>BMI Calculator</h2>
              </li>

              <li class="right"></li>
            </ul>
          </div>

          <div class="round-cont-s">
            <div class="txt-box-cont" style="margin-left: 4px;">
              <div class="txt-box-text">Weight:</div>
              <input onchange="isDouble(this);"
                     id="weight" name="weight" class="txt-box"/>Kgs
            </div>

            <div class="txt-box-cont">
              <div class="txt-box-text">Height:</div>
              <input onchange="isDouble(this);"
                     id="height" name="height" class="txt-box"/>cms
            </div>

            <div class="round-cont-s" style="margin-top: 0px;"><input
                type="button" class="submit" onclick="computeBMI('weight', 'height', 'bmiholder');getBMIPrompt();"/>
            </div>


            <div class="cl"></div>
          </div>

          <div class="round-cont-s" style="margin-top: 10px;">
            <div id="bmiholder">

            </div>

            <div id="bmiState"></div>
          </div>

        </li>

        <li class="right"></li>
      </ul>
      <div class="cl"></div>
    </div>

    <div class="round-box-inner">

      <div class="cl"></div>
    </div>

    <div class="round-box-inner">
      <ul>
        <li class="left"></li>

        <li class="mid">
          <div class="blue-round-box">
            <ul>
              <li class="left"></li>

              <li class="mid" style="width:217px;">
                <h2>FAQ’s About BP</h2>
              </li>

              <li class="right"></li>
            </ul>
          </div>


          <div class="round-cont-s">

            <div class="faq">
              <script type="text/javascript">
                var iframesrc = "${pageContext.request.contextPath}/pages/bp-faq.jsp"
                document.write('<iframe id="datamain" src="' + iframesrc + '" width="100%" height="200px" marginwidth="0" marginheight="0" hspace="0" vspace="0" 	frameborder="0" 	scrolling="no"></iframe>')
              </script>
            </div>

          </div>

        </li>

        <li class="right"></li>
      </ul>
      <div class="cl"></div>
    </div>

    <div class="round-box-inner">
      <ul>
        <li class="left"></li>

        <li class="mid">
          <div class="blue-round-box">
            <ul>
              <li class="left"></li>

              <li class="mid" style="width:217px;">
                <h2>FAQ’s About Sleep Apnea</h2>
              </li>

              <li class="right"></li>
            </ul>
          </div>


          <div class="round-cont-s">

            <div class="faq">
              <script type="text/javascript">
                var iframesrc = "${pageContext.request.contextPath}/pages/cpap-faq.jsp"
                document.write('<iframe id="datamain" src="' + iframesrc + '" width="100%" height="200px" marginwidth="0" marginheight="0" hspace="0" vspace="0" 	frameborder="0" 	scrolling="no"></iframe>')
              </script>
            </div>

          </div>

        </li>

        <li class="right"></li>
      </ul>
      <div class="cl"></div>
    </div>

  </div>

  <script type="text/javascript">
  function CreateXmlHttpObject() {
    var objXMLHttp = null
    if (window.XMLHttpRequest) {
      objXMLHttp = new XMLHttpRequest()
    } else if (window.ActiveXObject) {
      objXMLHttp = new ActiveXObject("Microsoft.XMLHTTP")
    }
    return objXMLHttp
  }

  function getBMIPrompt() {
    var bmi = document.getElementById("bmi").innerHTML;
    if (bmi != "") {
      var xmlHttp = CreateXmlHttpObject();
      if (xmlHttp == null) {
        alert("Browser does not support HTTP Request")
        return
      }
      var url = "<%=request.getContextPath()%>/jsp/ajax/GetBMIPrompt.jsp"
      url = url + "?bmi=" + bmi
      url = url + "&sid=" + Math.random()
      xmlHttp.onreadystatechange = (function afterResponse() {
        if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
          document.getElementById("bmiState").innerHTML = xmlHttp.responseText;
        }
      })
      xmlHttp.open("GET", url, true)
      xmlHttp.send(null)
    }
  }

  function computeBMI(wt, ht, bmi) {
    document.getElementById(bmi).innerHTML = "";

    var w = document.getElementById(wt).value;
    var h = document.getElementById(ht).value;
    if (w != '' && h != '') {
      var hInMSqr = ((h / 100) * (h / 100));
      document.getElementById(bmi).innerHTML = "<div class='txt-box-text'>BMI: <label id='bmi'>" + (w / hInMSqr).toFixed(2) + "</label> (kg/m<sup>2</sup>)</div>";
    }
  }

</script>
</s:layout-component>--%>

</s:layout-render>

