<%--
<%@ page import="mhc.pojo.HCUser" %>
<%@ page import="mhc.pojo.DietPlanItem" %>
<%@ page import="mhc.servlet.action.DietFrameworkAction" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/hc.js"></script>
<%
  String customerId = "";
  if (session.getAttribute("loggedInUser") != null) {
    HCUser user = (HCUser) session.getAttribute("loggedInUser");
    customerId = user.getAssociatedUser();
  }
  String dpId = "-1";
  if (request.getParameter("dpId") != null) {
    dpId = request.getParameter("dpId");
  }
  List<DietPlanItem> dpiList = (new DietFrameworkAction())
      .dietPlanItemsList(dpId);

  List<String> catList = (new DietFrameworkAction())
      .getCategories("");
%>

<s:layout-render name="/layouts/default.jsp" pageTitle="Standard Diet Plan">
  <s:layout-component name="heading">Standard Diet Plan</s:layout-component>
  <s:layout-component name="lhsContent">
    <div class="main-inn-left" style="width:1%">
    </div>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div class="main-inn-right" style="width:99%">
      <div class="txt-all">

        <div class="diet-plan-head">
          <table width="99%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>Below is a standard diet plan for <strong>1600</strong> Calories. You can customize it and print
                it as per your requirement.
              </td>
            </tr>
          </table>

        </div>

        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="calorie">
          <tr>
            <td width="70%"><strong>Total Calorie Intake:</strong><em>*</em> </td>
            <td width="30%"><input type="text" id="totalCalories1" class="txt-box-calorie"/></td>
          </tr>
        </table>
        <form name="saveDietForm" method="post"
              action="<%=request.getContextPath()%>/mhc/saveDietPlan"
              onsubmit="return validateForm();"><input type="hidden"
                                                       name="command" value="saveDietPlan"/><input type="hidden"
                                                                                                   name="returnURL"
                                                                                                   value='personalised-diet-plans.jsp'>
          <input type="hidden"
                 name="customerId" value="<%=customerId %>"/>

          <table cellpadding="2" cellspacing="0" class="diet-plan">
            <thead>
            <tr>
              <th width="">Time</th>
              <th width="">Category</th>
              <th width="">Item</th>
              <th width="">Quantity</th>
              <th width="">Calories<em>*</em></th>
            </tr>
            </thead>
            <tbody>
            <%
              String oldTimeFrame = "";
              int timeFrameCounter = 0;
              int itemCounter = 0;
              for (Iterator iterator = dpiList.iterator(); iterator.hasNext();) {
                DietPlanItem dpi = (DietPlanItem) iterator.next();
                itemCounter++;

                if (!dpi.getTimeFrame().equals(oldTimeFrame)) {
                  timeFrameCounter++;
                }
                if (timeFrameCounter % 2 == 1) {
            %>
            <tr bgcolor="#DDDDDD">
                  <%
										} else {
									%>

            <tr>
              <%
                }
                if (!dpi.getTimeFrame().equals(oldTimeFrame)) {
              %>
              <td valign="top"><b><%=dpi.getTimeFrame()%>
              </b></td>
              <%
              } else {
              %>
              <td></td>
              <%
                }
                oldTimeFrame = dpi.getTimeFrame();
              %>
              <input type="hidden" name="timeFrame<%=itemCounter %>"
                     value="<%=dpi.getTimeFrame() %>"/>
              <td><select class="selectBox"
                          id="category<%=itemCounter %>"
                          name="category<%=itemCounter %>">
                <option value="">Select Category...</option>
                <%
                  catList = (new DietFrameworkAction()).getCategories(dpi
                      .getTimeFrame());
                  for (Iterator iterator2 = catList.iterator(); iterator2
                      .hasNext();) {
                    String category = (String) iterator2.next();
                    if (category.equals(dpi.getCategory())) {
                %>
                <option value="<%=category %>" selected><%=category%>
                </option>
                <%
                } else {
                %>
                <option value="<%=category %>"><%=category%>
                </option>
                <%
                    }
                  }
                %>
              </select></td>
              <td><input class="inputBox" autocomplete="off"
                         value="<%=dpi.getItem() %>" id="categoryItem<%=itemCounter %>"
                         name="categoryItem<%=itemCounter %>" size="35"
                         autocomplete="off"
                         onkeyup="suggestDietPlanItems(this, '<%=itemCounter %>')"></td>
              <input type="hidden" id="exchangeRate<%=itemCounter %>"
                     name="exchangeRate<%=itemCounter %>"
                     value="<%=dpi.getExchangeRate() %>"/>
              <td><input id="quantity<%=itemCounter %>"
                         name="quantity<%=itemCounter %>" class="inputBox" size="1"
                         value="<%=dpi.getQuantity() %>"
                         onchange="computeTotal(this, '<%=itemCounter %>')"/></td>
              <td><input class="inputBox" size="2" readonly="readonly"
                         id="calories<%=itemCounter %>"
                         name="calories<%=itemCounter %>"
                         value="<%=dpi.getCalroies() %>"/></td>
            </tr>
            <%
              }
            %>
            </tbody>
          </table>

        </form>
      </div>
    </div>

    <div id="itemSearchList" class='item_suggestion'></div>

  </s:layout-component>
</s:layout-render>


<script>
  window.onload = function() {
    computeTotal();
  };

  function suggestDietPlanItems(elm, elmRank) {
    list = document.getElementById("itemSearchList");
    list.style.display = "block";
    var category = document.getElementById("category" + elmRank).value;
    xmlHttp = CreateXmlHttpObject();
    if (xmlHttp == null) {
      alert("Browser does not support HTTP Request")
      return
    }
    var url = "<%=request.getContextPath()%>/jsp/ajax/GetSuggestedItems.jsp"
    url = url + "?category=" + escape(category)
    url = url + "&item=" + elm.value
    url = url + "&elmRank=" + elmRank
    url = url + "&sid=" + Math.random()
    xmlHttp.onreadystatechange = (function stateChangedForSuggestedBrands() {
      if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
        list.innerHTML = xmlHttp.responseText;
        elm.parentNode.appendChild(list);

        elm.onkeypress = function(e) {
          var key = getKeyCode(e);
          if (key == 13) { // enter
            selectList(elm);
            selectedIndex = 0;
            return false;
          }
          ;

          if (key == 9) {  // tab
            selectedIndex = 0;
            clearList();
            document.getElementById('exchangeRate' + elmRank).value = "";
            document.getElementById('quantity' + elmRank).value = "";
            document.getElementById('calories' + elmRank).value = "";
            computeTotal();
          }
        };

        elm.onkeyup = function(e) {
          var key = getKeyCode(e);
          switch (key) {
            case 27:  // esc
              elm.value = "";
              selectedIndex = 0;
              clearList();
              break;
            case 38: // up
              navList("up");
              break;
            case 40: // down
              navList("down");
              break;
            default:
              suggestDietPlanItems(elm, elmRank);
              break;
          }
          ;
        };
      }
    } )
    xmlHttp.open("GET", url, true)
    xmlHttp.send(null)
  }

  function selectList(elm) {
    li = list.getElementsByTagName("li");
    a = li[selectedIndex - 1].getElementsByTagName("a")[0];
    inp = li[selectedIndex - 1].getElementsByTagName("input");
    setExchangeRate(inp[0].value, inp[1].value, a.innerHTML);
    clearList();
    elm.blur();
  }
  ;

  function setExchangeRate(cal, elmRank, itemName) {
    document.getElementById('categoryItem' + elmRank).value = itemName;
    clearList();
    var oldCal = document.getElementById('calories' + elmRank).value;
    if (oldCal != cal) {
      document.getElementById('exchangeRate' + elmRank).value = cal;
      document.getElementById('quantity' + elmRank).value = "1";
      document.getElementById('calories' + elmRank).value = cal;

      var totalCal = 0;
      for (var i = 1; i < 25; i++) {
        var calElm = document.getElementById("calories" + i);
        if (calElm != null) {
          totalCal += Math.round(calElm.value);
        }
      }
      document.getElementById('totalCalories1').value = totalCal;
      document.getElementById('totalCalories2').value = totalCal;
      if (totalCal > 1600) {
        document.getElementById('totalCalories1').style.color = "red";
        document.getElementById('totalCalories2').style.color = "red";
      }

    }

    //document.getElementById('categoryItem'+Math.round(elmRank*1+1)).focus();
  }

  function computeTotal(elm, elmRank) {
    if (elm != null) {
      var exr = document.getElementById('exchangeRate' + elmRank).value;
      var tot = Math.round(exr * elm.value);
      document.getElementById('calories' + elmRank).value = tot;
    }

    var totalCal = 0;
    for (var i = 1; i < 25; i++) {
      var calElm = document.getElementById("calories" + i);
      if (calElm != null) {
        totalCal += Math.round(calElm.value);
      }
    }
    document.getElementById('totalCalories1').value = totalCal;
  }

  function init() {
    var totalCal = 0;
    for (var i = 1; i < 25; i++) {
      var calElm = document.getElementById("calories" + i);
      if (calElm != null) {
        calElm.value = "0"
        document.getElementById("quantity" + i).value = "0";
      }
    }
  }

  function saveDietPlan() {
    if ('<%=customerId%>' == "") {
      goToLogin();
    } else {
      showPopup('saveAsDiv');
      var popUpDiv = document.getElementById('saveAsDiv');
      popUpDiv.style.top = '125px';
    }
  }

  function validateForm() {
    var name = document.saveDietForm.name.value;
    if (name == '') {
      alert('Name is mandatory');
      return false;
    }
  }
</script>

<style>
  .sf_inactive {
    border: 2px #3d91a5 solid;
    background: #3d91a5;
    color: #b4d3db;
  }

  .sf_active {
    border: 2px #8BB544 solid;
    background: #fff;
    color: #333;
  }

  .sf_text {
    border: 2px #3c90a5 solid;
    background: #fff;
    color: #888;
  }

  .item_suggestion {
    position: relative;
  }

  .item_suggestion ul {
    position: absolute;
    margin: 0;
    padding: 0;
    background: #ffffff;
    top: 0;
    left: 0;
    width: 333px;
  }

  .item_suggestion li {
    margin: 0;
    padding: 0;
    list-style: none;
    border: 1px solid #ef9e4d;
  }

  .item_suggestion li a {
    display: block;
    text-indent: 5px;
    color: #666; /*margin-left: 15px;*/
    padding: 2px;
    font-family: "Lucida Grande", "Helvetica Neue", Helvetica, Arial, Verdana, sans-serif;
  }

  .item_suggestion li.selected a {
    background: #DDDDDD;
  }
</style>--%>
