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
            <a href="${pageContext.request.contextPath}/${menuNode.url}" class="gm-mc-nm">${menuNode.name}</a>
            <span style="">&raquo;</span>

            <div class="hide gm-sc-cntnr">
              <div class="brdr-t-blu-strip" style="position: relative;"></div>
              <h3>
                <a href="${pageContext.request.contextPath}/${menuNode.url}" class="gm-mc-nm">${menuNode.name}</a>
                <hr class="mrgn-b-25">
              </h3>

              <ul class="gm-sc-list">
                <div class="span3">
                  <c:forEach items="${menuNode.childNodes}" var="l1child" varStatus="cntr">

                  <c:if test="${cntr.index > 0 && cntr.index%2 ==0}">
                </div>
                <div class="span3">
                  </c:if>

                  <a href="${pageContext.request.contextPath}/${l1child.url}" class="gm-sc-nm">${l1child.name}</a>
                  <span style="display:inline-block">&raquo;</span>
                  <c:set var="leafElms"
                         value="${fn:length(l1child.childNodes)>maxLeafElms?maxLeafElms:fn:length(l1child.childNodes)}"/>
                  <c:set var="seeMore" value="${fn:length(l1child.childNodes)>maxLeafElms?'true':'false'}"/>
                  <div class="gl gm-tc-list">

                    <c:forEach items="${l1child.childNodes}" var="l2child" end="${leafElms}" varStatus="counter">
                      <a href="${pageContext.request.contextPath}/${l2child.url}" class="gm-tc-nm">${l2child.name}</a>
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
        <li class="gm-mc brdr-t">
          Packs<span style="">&raquo;</span>
        </li>
        <li class="gm-mc">
          Brands<span style="">&raquo;</span>
        </li>
      </ul>
        <%--<div class="seperator"></div>
      <div class="dropDownitem">Separated link</div>--%>
    </div>
  </div>
  <script type="text/javascript">
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
        $submenu.find('.span3:last').css('border-right', 'none');
        $row.find('.gm-sc-cntnr').css('font-weight', 'bold');
        // Show the submenu
        $submenu.css({
          display: "block",
          top: -5,
          left: width - 3

        });

        // Keep the currently activated row's highlighted look
        $row.find("a").addClass("maintainHover");

        //Fix vertical line height

        $submenu.find('.span3').each(function(){getMaxHeight($(this))});
      //  maxHeight = 'auto';
        if(maxHeight>100){
            $submenu.find('.span3').height(maxHeight);
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
</s:layout-definition>