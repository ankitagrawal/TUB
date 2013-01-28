<%@ taglib prefix="g" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hk.domain.catalog.category.Category" %>
<%@ page import="com.hk.dto.menu.MenuNode" %>
<%@ page import="com.hk.helper.MenuHelper" %>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
    <script type="text/javascript">
        var pathName = window.location.pathname;
        var pathNames = pathName.split("/");

        var hkPageType = $("#pageType").val();
        var prodId = '';
        var pCategory = '';
        var cartProducts = new Array();
        var canSet = true;

        if(hkPageType == 'category'){
            pCategory = $("#topLevelCategory").val();
            /*if((pCategory == "beauty") || (pCategory=="sports")){
                canSet = true;
            }*/

            if(pathNames.length > 3){
                var thirdLevelSlug = pathNames[4];
                //google disallowed
                switch (thirdLevelSlug){
                    case "rehabilitation-supports":
                    case "personal-hygiene":
                    case "breast-cancer-home-test":
                    case "women-nutrition":
                        canSet = false;
                        break;
                    default :
                        break;
                }
            }

        }
        else if(hkPageType == 'product'){
            prodId = $("#googleProductId").val();
            canSet = true;
        }else if(hkPageType == 'cart' || hkPageType == 'purchase'){
            prodId = $("#cartProductId").val();
            if(prodId && prodId.length > 0){
                var firstChar = prodId.charAt(0);
                prodId = prodId.substr(1,prodId.length - 1);
            }
        }else if(hkPageType == 'home'){
        }
        if(canSet)
        {
            alert("pageType " + "'" + hkPageType + "'" + " category " + pCategory + " product " + prodId);
            var google_tag_params = {
                prodid: "'" + prodId + "'" ,
                pagetype: "'" + hkPageType + "'",
                pCat: "'" + pCategory + "'"
            };
        }
    </script>
    <script type="text/javascript">
        /* <![CDATA[ */
        var google_conversion_id = 1001307632;
        var google_conversion_label = "wwUqCJDx5AQQ8Pu63QM";
        var google_custom_params = window.google_tag_params;
        var google_remarketing_only = true;
        /* ]]> */
    </script>
    <script type="text/javascript" src="//www.googleadservices.com/pagead/conversion.js">
    </script>
    <noscript>
        <div style="display:inline;">
            <img height="1" width="1" style="border-style:none;" alt="" src="//googleads.g.doubleclick.net/pagead/viewthroughconversion/1001307632/?value=0&amp;label=wwUqCJDx5AQQ8Pu63QM&amp;guid=ON&amp;script=0"/>
        </div>
    </noscript>
</s:layout-definition>
