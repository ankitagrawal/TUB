<%@ page import="com.hk.taglibs.Functions" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="org.joda.time.DateTimeFieldType" %>

<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.nwb.NwbMenuAction" var="menuAction" event="pre"/>

<s:layout-definition>
  <c:set var="maxLeafElms" value="5"/>
  <div class="container_24 mrgn-t-10">
  <div class="flyout-menu">
      <div id="dropDownButton" class="menu-hdr">
          <div class="hdr-title font-caps">Categories</div>
          <div class="icn-dwn-cs2"></div>
      </div>
      <div id="dropDownbox1" class="dropDownboxHomePage ${showMenu!=null && showMenu=='true'?'':'hide'}">
          <div class="brdr-t-blu-strip"></div>
          <ul class="gm gl">
              <c:forEach items="${menuAction.menuNodes}" var="menuNode">
                  <li class="gm-mc">
                      <a href="${menuNode.url}" class="gm-mc-nm">${menuNode.name}</a>
                      <span style="">&raquo;</span>

                      <div class="hide gm-sc-cntnr">
                          <div class="brdr-t-blu-strip" style="position: relative;"></div>
                          <h3>
                              <a href="${menuNode.url}" class="gm-mc-nm">${menuNode.name}</a>
                              <hr class="mrgn-b-25">
                          </h3>

                          <ul class="gm-sc-list">
                              <div class="grid_4">
                                  <c:forEach items="${menuNode.childNodes}" var="l1child" varStatus="cntr">

                                      <c:if test="${cntr.index > 0 && cntr.index%2 ==0}">
                              </div>
                              <div class="grid_4">
                                  </c:if>

                                  <a href="${l1child.url}" class="gm-sc-nm">${l1child.name}</a>
                                  <span style="display:inline-block">&raquo;</span>
                                  <c:set var="leafElms"
                                         value="${fn:length(l1child.childNodes)>maxLeafElms?maxLeafElms:fn:length(l1child.childNodes)}"/>
                                  <c:set var="seeMore" value="${fn:length(l1child.childNodes)>maxLeafElms?'true':'false'}"/>
                                  <div class="gl gm-tc-list">

                                      <c:forEach items="${l1child.childNodes}" var="l2child" end="${leafElms}" varStatus="counter">
                                          <a href="${l2child.url}" class="gm-tc-nm">${l2child.name}</a>
                                          <%--c:if test="${counter.last && seeMore}">
                                          <a href="#" class="seeMore">See All in ${l1child.name}</a>
                                          </c:if--%>
                                      </c:forEach>
                                  </div>
              </c:forEach>
      </div>
      </ul>
  </div>
  </li>
  </c:forEach>
  <%--<li class="gm-mc brdr-t">
      &lt;%&ndash;<a href="javascript:void(0)" class="gm-mc-nm">Packs</a>&ndash;%&gt;
      &lt;%&ndash;<span style="">&raquo;</span>&ndash;%&gt;
  &lt;%&ndash;</li>&ndash;%&gt;
  &lt;%&ndash;<li class="gm-mc">&ndash;%&gt;
      <a href="${pageContext.request.contextPath}/beta/brand/BrandListing.action" class="gm-mc-nm">Brands</a>
      <span style="">&raquo;</span>
  </li>--%>
  </ul>
  <%--<div class="seperator"></div>
<div class="dropDownitem">Separated link</div>--%>
  </div>
  </div>
  </div>
  <script type="text/javascript">
  /**
   * menu-aim is a jQuery plugin for dropdown menus that can differentiate
   * between a user trying hover over a dropdown item vs trying to navigate into
   * a submenu's contents.
   * https://github.com/kamens/jQuery-menu-aim
   */
  (function ($) {

      $.fn.menuAim = function (opts) {
          // Initialize menu-aim for all elements in jQuery collection
          this.each(function () {
              init.call(this, opts);
          });

          return this;
      };

      function init(opts) {
          var $menu = $(this),
                  activeRow = null,
                  mouseLocs = [],
                  lastDelayLoc = null,
                  timeoutId = null,
                  options = $.extend({
                      rowSelector: "> li",
                      submenuSelector: "*",
                      submenuDirection: "right",
                      tolerance: 75,  // bigger = more forgivey when entering submenu
                      enter: $.noop,
                      exit: $.noop,
                      activate: $.noop,
                      deactivate: $.noop,
                      exitMenu: $.noop
                  }, opts);

          var MOUSE_LOCS_TRACKED = 3,  // number of past mouse locations to track
                  DELAY = 300;  // ms delay when user appears to be entering submenu
          var mousemoveDocument = function (e) {
              mouseLocs.push({x: e.pageX, y: e.pageY});

              if (mouseLocs.length > MOUSE_LOCS_TRACKED) {
                  mouseLocs.shift();
              }
          };
          var mouseleaveMenu = function () {
              if (timeoutId) {
                  clearTimeout(timeoutId);
              }
              if (options.exitMenu(this)) {
                  if (activeRow) {
                      options.deactivate(activeRow);
                  }

                  activeRow = null;
              }
          };
          var mouseenterRow = function () {
                      if (timeoutId) {
                          // Cancel any previous activation delays
                          clearTimeout(timeoutId);
                      }

                      options.enter(this);
                      possiblyActivate(this);
                  },
                  mouseleaveRow = function () {
                      options.exit(this);
                  };
          var clickRow = function () {
              activate(this);
          };

          var activate = function (row) {
              if (row == activeRow) {
                  return;
              }

              if (activeRow) {
                  options.deactivate(activeRow);
              }

              options.activate(row);
              activeRow = row;
          };
          var possiblyActivate = function (row) {
              var delay = activationDelay();

              if (delay) {
                  timeoutId = setTimeout(function () {
                      possiblyActivate(row);
                  }, delay);
              } else {
                  activate(row);
              }
          };
          var activationDelay = function () {
              if (!activeRow || !$(activeRow).is(options.submenuSelector)) {
                  return 0;
              }

              var offset = $menu.offset(),
                      upperLeft = {
                          x: offset.left,
                          y: offset.top - options.tolerance
                      },
                      upperRight = {
                          x: offset.left + $menu.outerWidth(),
                          y: upperLeft.y
                      },
                      lowerLeft = {
                          x: offset.left,
                          y: offset.top + $menu.outerHeight() + options.tolerance
                      },
                      lowerRight = {
                          x: offset.left + $menu.outerWidth(),
                          y: lowerLeft.y
                      },
                      loc = mouseLocs[mouseLocs.length - 1],
                      prevLoc = mouseLocs[0];

              if (!loc) {
                  return 0;
              }

              if (!prevLoc) {
                  prevLoc = loc;
              }

              if (prevLoc.x < offset.left || prevLoc.x > lowerRight.x ||
                      prevLoc.y < offset.top || prevLoc.y > lowerRight.y) {
                  return 0;
              }

              if (lastDelayLoc &&
                      loc.x == lastDelayLoc.x && loc.y == lastDelayLoc.y) {
                  return 0;
              }

              function slope(a, b) {
                  return (b.y - a.y) / (b.x - a.x);
              }

              ;

              var decreasingCorner = upperRight,
                      increasingCorner = lowerRight;

              if (options.submenuDirection == "left") {
                  decreasingCorner = lowerLeft;
                  increasingCorner = upperLeft;
              } else if (options.submenuDirection == "below") {
                  decreasingCorner = lowerRight;
                  increasingCorner = lowerLeft;
              } else if (options.submenuDirection == "above") {
                  decreasingCorner = upperLeft;
                  increasingCorner = upperRight;
              }

              var decreasingSlope = slope(loc, decreasingCorner),
                      increasingSlope = slope(loc, increasingCorner),
                      prevDecreasingSlope = slope(prevLoc, decreasingCorner),
                      prevIncreasingSlope = slope(prevLoc, increasingCorner);

              if (decreasingSlope < prevDecreasingSlope &&
                      increasingSlope > prevIncreasingSlope) {
                  lastDelayLoc = loc;
                  return DELAY;
              }

              lastDelayLoc = null;
              return 0;
          };

          $menu
                  .mouseleave(mouseleaveMenu)
                  .find(options.rowSelector)
                  .mouseenter(mouseenterRow)
                  .mouseleave(mouseleaveRow)
                  .click(clickRow);

          $(document).mousemove(mousemoveDocument);

      }

      ;
  })(jQuery);
    $(document).ready(function() {
      /*Dropdown js */
      $('#dropDownButton').mouseenter(function() {
        //deactivateSubmenu($("#dropDownbox1").find('.maintainHover').parents('.gm-mc'));
        $("#dropDownbox1").css("display", "block");
        //$("#dropDownButton").addClass("menu-hdr-hover");
        $(".icn-dwn-cs2").addClass("icn-dwn-cs-hover");
      });
      $('#dropDownButton').mouseleave(function() {
        $("#dropDownbox1").css("display", "none");

        $(".icn-dwn-cs2").removeClass("icn-dwn-cs-hover");
      });

      $('#dropDownbox1').hover(function() {
        $("#dropDownbox1").css("display", "block");
        //$("#dropDownButton").addClass("menu-hdr-hover");
        $(".icn-dwn-cs2").addClass("icn-dwn-cs-hover");
      },
          function() {
            $("#dropDownbox1").css("display", "none");
            $(".gm-sc-cntnr").css("display", "none");
            $(".maintainHover").removeClass("maintainHover");

            $(".icn-dwn-cs2").removeClass("icn-dwn-cs-hover");
          }
          );

      $('.gm-mc').mouseover(function(e) {
        var ele = e.currentTarget;
        setTimeout(function() {

          if ($('.gm-sc-cntnr:visible').length == 0 && $(ele).find('a').attr('href') == $('.gm-mc:hover').find('a').attr('href')) {
            $(ele).find('.gm-sc-cntnr').show();
            console.log('in');
          }
        }, 200);

      });


      var $menu = $(".gm");
      $menu.mouseleave(function() {
        $('.gm-sc-cntnr:visible').hide();
      });
      // jQuery-menu-aim: <meaningful part of the example>
      // Hook up events to be fired on menu row activation.
      $menu.menuAim({
        activate: activateSubmenu,
        deactivate: deactivateSubmenu
      });
      // jQuery-menu-aim: </meaningful part of the example>

      // jQuery-menu-aim: the following JS is used to show and hide the submenu
      // contents. Again, this can be done in any number of ways. jQuery-menu-aim
      // doesn't care how you do this, it just fires the activate and deactivate
      // events at the right times so you know when to show and hide your submenus.
      function activateSubmenu(row) {


        var $row = $(row),

            $submenu = $row.find('.gm-sc-cntnr')
        height = $menu.outerHeight(),
            width = $menu.outerWidth();
        var maxHeight = 0;
        $submenu.find('.grid_4:last').css('border-right', 'none');
        $row.find('.gm-sc-cntnr').css('font-weight', 'bold');
        // Show the submenu
        $submenu.css({
          display: "block",
          top: 0,
          left: width - 3

        });

        // Keep the currently activated row's highlighted look
        $row.find("a").addClass("maintainHover");

        //Fix vertical line height

          $submenu.find('.grid_4').each(function(){getMaxHeight($(this))});
      //  maxHeight = 'auto';
        if(maxHeight>100){
            $submenu.find('.grid_4').height(maxHeight);
        }

        function getMaxHeight(ref){
               maxHeight = (maxHeight < ref.height()) ? ref.height() : maxHeight;
        }


      }

      function deactivateSubmenu(row) {
        var $row = $(row),
            submenuId = $row.data("submenuId"),
            $submenu = $row.find('.gm-sc-cntnr');

        // Hide the submenu and remove the row's highlighted look
        $submenu.css("display", "none");
        $row.find("a").removeClass("maintainHover");
      }

      // Bootstrap's dropdown menus immediately close on document click.
      // Don't let this event close the menu if a submenu is being clicked.
      // This event propagation control doesn't belong in the menu-aim plugin
      // itself because the plugin is agnostic to bootstrap.
      $(".gm li").click(function(e) {
        e.stopPropagation();
      });
    });


  </script>
<style>
        /** global menu navigation util starts **/

    .gm {
        display: inline-block;
        position: relative;
        z-index: 100;
        background: white;
        margin: 0px;
        box-shadow: 0px 0px 25px #ccc;
        border: 1px solid #c8c8c8;
        border-top: none;
        width: 218px;
    }

    .gm h1, .gm h2, .gm h3 {
        color: #aaa;
        padding: 0px 20px 0px 19px;
    }

    .gm a {
        display: block;
        padding: 5px 0px 5px 10px;
    }

    .gm li {

    }

    .gm .gl {
        margin: 0px;
    }

    .gm-lnk {

    }

    .gm-mc {
        padding-top: 4px;
        padding-bottom: 4px;
    }

    .gm-mc:hover {
        background: #e6e6e6;
        color: #1b75bb;
    }

    .gm-mc:last-child {

    }

    .gm-mc > span {
        float: right;
        padding-right: 10px;
        padding-top: 5px;
    }

    .gm-mc:hover > a {
        color: #1b75bb;
    }

    .gm-mc > a {
        display: inline-block;
        width: 175px;
        padding-left: 18px;
    }

    .gm-mc a:hover {

    }

    .gm-mc-nm {
        text-overflow: ellipsis;
        overflow: hidden;
        font-weight: 600;
        text-transform: capitalize;
    }

    .gm-mc-nm:hover {

    }

    .gm-sc-cntnr {
        position: absolute;
        left: 175px;
        min-height: 300px;
        top: 0px;
        border: 1px solid #c8c8c8;
        border-top: none;
        background-color: white;
        box-shadow: 0px 0px 25px #ccc;
        z-index: 10000;
    }

    .gm-sc-cntnr a {
        padding-left: 0px;
    }

    .gm-sc-cntnr h3 a {
        height: 37px;
        padding-bottom: 0px;
    }

    .gm-sc-list {
        width: 526px;
        padding-left:20px;

    }

    .gm-sc-list .grid_4 {
        border-right: 1px solid #ddd;
        padding-right: 12px;

    }

    .gm-sc-list > li {
        display: inline-block;
        vertical-align: top;
        width: 150px;
        margin-right: 40px;
        margin-bottom: 10px;
    }

    a.gm-sc-nm {
        font-size: 1.1em;
        line-height: 1em;
        font-weight: 600;
        text-transform: capitalize;
        display: inline-block;
        width: 91%;
    }

    .gm-tc-nm {
        color: #444;
        font-size: 1em;
        text-transform: capitalize;
        font-weight: 400;

    }

    .gm .gm-tc-list {
        margin-bottom: 30px;
    }

    .gm-tc-list a {
        padding: 2px 0px;
    }

    .gm-tc-list a.seeMore {
        font-size: 0.8em;
        color: #bbb;
        line-height: 1.1em
    }

    .hide {
        display: none;
    }

.flyout-menu{
    width:218px;
}
.flyout-menu .menu-hdr {
    z-index: 10;
    position: relative;
    background: #0092D7;
    height: 35px;
    left: 0;
    color: #fff;
    cursor: pointer;

}

.menu-hdr-hover {
    background: white !important;
    color: rgb(49, 118, 241) !important;
    border-color: rgb(211, 211, 211);
    border-width: 1px 1px 0 1px !important;
    border-style: solid;

}

.flyout-menu .menu-hdr .hdr-title {
    position: relative;
    float: left;
    font-weight: 700;
    top: 3px;
    left: 18px;
    text-transform: uppercase;
}

#dropDownbox1 {
    position: absolute;
}

.flyout-submenu {
    display: none;
    position: absolute;
    z-index: 1;
    float: left;
    clear: both;
    border: 1px solid rgb(211, 211, 211);
    box-shadow: 0px 8px 10px -3px #888;
    top: 36px;
    width: 175px;
    height: auto;
    background: white;
}

.nav-menu {
    float: right;
    min-width: 360px;
    margin-top: 20px;
    height: 36px;
}

.nav-menu > div {
    float: left;
    width: 80px;
    margin-left: 10px;
}

.nav-menu p {
    margin: 0;
}

</style>
</s:layout-definition>