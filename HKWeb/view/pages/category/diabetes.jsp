<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:layout-render name="/layouts/category-homeG.jsp" pageTitle="Diabetes Care Products - Buy Accu Chek, Dr Morepen, Omron, One Touch Products in India">


<s:layout-component name="metaDescription">
  Buy diabetes products and supplies online in India on best prices. Free shipping to anywhere in India. Authenticity guaranteed.
</s:layout-component>

<s:layout-component name="metaKeywords">
  Diabetes, Diabetes Care, Diabetes Care Products, Diabetes Store India, Sweetener, Accu Chek, Diabetes Care Products Store India
</s:layout-component>

<s:layout-component name="mainBanner">
  <div class="promotional">
    <ul id="categoryBanner" class="grid_18">
      <li>
        <a href="${pageContext.request.contextPath}/diabetes/testing-supplies/combos?brand=OneTouch">
          <img src="<hk:vhostImage/>/images/banners/Diabetes/onetouch_lancets_banner.jpg" alt="Onetouch offers"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/product/integra-diabetes-day-combo/DC007">
          <img src="<hk:vhostImage/>/images/banners/Diabetes/banner_accu_chek.jpg" alt="Accu Chek integra xmas discount - 40% off"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/product/accu-chek-active/DM008">
          <img src="<hk:vhostImage/>/images/banners/Diabetes/banner_accu-chek_22.jpg" alt="Accu chek active meter"/>
        </a>
      </li>
      <li>
        <a href="${pageContext.request.contextPath}/diabetes/diabetic-food">
          <img src="<hk:vhostImage/>/images/banners/Diabetes/2.jpg" alt="Slide 1"/>
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

<s:layout-component name="topCategory">diabetes</s:layout-component>

<s:layout-component name="breadcrumbs">
  <div class='crumb_outer'>
    <s:link beanclass="com.hk.web.action.HomeAction" class="crumb">Home</s:link>
    &gt;
    <span class="crumb last" style="font-size: 12px;">Diabetes</span>

    <h1 class="title">
      Diabetes Care Products
    </h1>
  </div>
</s:layout-component>
<s:layout-component name="product_rows">
  <div class="products">
    <shiro:hasPermission name="<%=PermissionConstants.UPDATE_PRODUCT_CATALOG%>">
      <br/>
      <div><s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="pre" target="_blank" class="popup"> Upload
        <s:param name="category" value="home-devices"/>
      </s:link>
        &nbsp;|&nbsp;
        <s:link beanclass="com.hk.web.action.core.catalog.image.UploadCategoryImageAction" event="manageCategoryImages" target="_blank" class="popup">
          <s:param name="category" value="diabetes"/>
          Manage Images
        </s:link>
      </div>
    </shiro:hasPermission>
    <h2>
      <a href='${pageContext.request.contextPath}/diabetes/testing-supplies/strips'>
        Top selling Strips
        <span class='small'>
          (view more products)
        </span>
      </a>
      <a class='faq' href='${pageContext.request.contextPath}/pages/aboutDiabetes.jsp' title='read FAQs related to Diabetes'>
        read FAQs
        <div class='icon'></div>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DS001"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DS007"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DS005"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DS020"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DS010"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DS011"/>
    <a class='more' href='${pageContext.request.contextPath}/diabetes/testing-supplies/strips'>
      view more products &rarr;
    </a>
  </div>
  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/diabetes/testing-supplies/meters'>
        Top Selling Meters
        <span class='small'>
          (view more products)
        </span>
      </a>
      <a class='faq' href='${pageContext.request.contextPath}/pages/aboutDiabetes.jsp' title='read FAQs related to Diabetes'>
        read FAQs
        <div class='icon'></div>
      </a>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DM019"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DM008"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DM007"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DM003"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DM014"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="DM009"/>
    <a class='more' href='${pageContext.request.contextPath}/diabetes/testing-supplies/meters'>
      view more products &rarr;
    </a>
  </div>
  <div class="products">
    <h2>
      <a href='${pageContext.request.contextPath}/diabetes/diabetic-food'>
        Top Selling Diabetic Food
        <span class='small'>
          (view more products)
        </span>
      </a>
        <%--<a class='faq' title='read FAQs related to Foot care products'>
          read FAQs
          <div class='icon'></div>
        </a>--%>
    </h2>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SWT002"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT109"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT101"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT115"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="NUT111"/>
    <s:layout-render name="/layouts/embed/_productThumbG.jsp" productId="SWT008"/>
    <a class='more' href='${pageContext.request.contextPath}/diabetes/diabetic-food'>
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
                <h2>Quick Diabetes Test</h2>
              </li>

              <li class="right"></li>
            </ul>
          </div>

          <div class="round-cont-s">
            <div class="txt-box-cont" style="margin-left: 4px;">
              <div class="txt-box-text">HbA1c:*</div>
              <input type="text" id="hba1c" name="hba1c" class="txt-box"/></div>

            <div class="txt-box-cont">
              <div class="txt-box-text">FBG:*</div>
              <input type="text" id="fbg" name="fbg" class="txt-box"/></div>

            <div class="txt-box-cont">
              <div class="txt-box-text">PPBG:*</div>
              <input type="text" id="ppbg" name="ppbg" class="txt-box"/></div>

            <div class="cl"></div>
          </div>

          <div class="round-cont-s" style="margin-top: 0px;"><input
              type="button" class="submit" onclick="getDiabetesPrompt();"/>
          </div>

          <div class="round-cont-s" style="margin-top: 10px;">
            <div id="diabetesPrompt"></div>
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
                <h2>FAQ’s About Diabetes</h2>
              </li>

              <li class="right"></li>
            </ul>
          </div>


          <div class="round-cont-s">

            <div class="faq">
              <script type="text/javascript">
                var iframesrc = "${pageContext.request.contextPath}/pages/db-faq.jsp"
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
                <h2>Diet Planner</h2>
              </li>

              <li class="right"></li>
            </ul>
          </div>


          <div class="round-cont-s">
            <div class="diet-txt"><span><a
                href="<%=request.getContextPath() %>/pages/standard-diet-plan.jsp">Create</a></span>
              and Save your personalized diet plans - Make a weekly plan for your
              family. Database contains majority of popular Indian, Chinese and
              Continental food.
            </div>
          </div>
          <div class="diet-planner-img"></div>
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

  function getDiabetesPrompt() {
    var hba1c = document.getElementById("hba1c").value;
    var fbg = document.getElementById("fbg").value;
    var ppbg = document.getElementById("ppbg").value;
    if (hba1c == '' || fbg == '' || ppbg == '') {
      alert("Mandatory fields are required to generate status.")
      return;
    } else {
      var xmlHttp = CreateXmlHttpObject();
      if (xmlHttp == null) {
        alert("Browser does not support HTTP Request")
        return
      }
      var url = "<%=request.getContextPath()%>/jsp/ajax/GetDiabetesPrompt.jsp";
      url = url + "?hba1c=" + hba1c
      url = url + "&fbg=" + fbg
      url = url + "&ppbg=" + ppbg
      url = url + "&sid=" + Math.random()
      xmlHttp.onreadystatechange = (function afterResponse() {
        if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
          document.getElementById("diabetesPrompt").innerHTML = xmlHttp.responseText;
        }
      })
      xmlHttp.open("GET", url, true)
      xmlHttp.send(null)
    }
  }

</script>
  
</s:layout-component>--%>

</s:layout-render>
