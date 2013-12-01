<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultG.jsp"
                 pageTitle="HealthKart.com: Buy Nutrition, Health Care, Beauty & Personal Care Products Online in India">

    <s:layout-component name="htmlHead">
        <meta name="description"
              content="Online Shopping for Nutrition, Health, Beauty & Personal Care Products in India: Buy Nutrition Supplements, Health Equipments, Diabetes supplies, Lenses, Home Devices & Other Products online at Lowest Price & Free Shipping in India – Healthkart.com"/>
        <meta name="keywords"
              content="Online Shopping, online shopping india, nutrition, healthcare products, buy health care health equipments, beauty care products, shop online, nutrition supplements, protein supplements, diabetes, skin care, eye care,  healthcart, healthkkart, healthkarts, price, india"/>

        <link href="<hk:vhostCss/>/css/960.24.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.responsiveslides.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <%--<link href="files/favicon.ico" rel="SHORTCUT ICON"/>--%>
        <script type="text/javascript" src="files/swfobject.js"></script>
        <%--<link href="files/style.css" rel="Stylesheet" type="text/css"/>--%>
        <style>
            body {
                margin: 0;
            }
        </style>
        <script>
            var flashvars = {
                htmlURL:document.location.toString(),
                FileServer:"C://temp/",
                autoTimerInterval:"05"
            };
            var params = {
                menu:"false",
                wmode:"transparent"
            };
            var attributes = {
                id:"myId",
                name:"myId"
            };
            swfobject.embedSWF("Healthkart_24_12_2012.swf", "myContent", "100%", "100%", "10.2.0", "expressInstall.swf", flashvars, params, attributes);
            function addToCart(products) {
                alert(products);
            }
        </script>
    </s:layout-component>
    <s:layout-component name="lhsContent">

        <div id="wrapper">
            <table width="960px" border="0" align="center" cellpadding="0" cellspacing="0" id="Table1">
                <tr>
                    <td width="960px" height="560px">
                        <div id="myContent">
                            <h1>
                                Alternative content</h1>

                            <p>
                                <a href="http://www.adobe.com/go/getflashplayer">
                                    <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif"
                                         alt="Get Adobe Flash player"/></a></p>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div id="footerBoxFx"></div>
    </s:layout-component>
</s:layout-render>
