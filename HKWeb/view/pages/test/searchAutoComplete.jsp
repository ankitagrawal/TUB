<%@include file="/includes/_taglibInclude.jsp" %>
<html>
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
<script type="text/javascript">
  $(function() {
    function log(message) {
      $("<div/>").text(message).prependTo("#log");
      $("#log").scrollTop(0);
    }

    $("#city").autocomplete({
      source: function(request, response) {
        $.ajax({
//          url: "http://ws.geonames.org/searchJSON",
          url:'http://localhost:8080/healthkart/autocomplete/',
          dataType: "jsonp",
          data: {
            featureClass: "P",
            style: "full",
            maxRows: 12,
            name_startsWith: request.terms
          },
          success: function(data) {
            response($.map(data.geonames, function(item) {
              return {
                //     label: item.name + (item.adminName1 ? ", " + item.adminName1 : "") + ", " + item.countryName,
                label: item.name,
                value: item.name
              }
            }));
          }
        });
      },
      minLength: 2,
      select: function(event, ui) {
        log(ui.item ?
            "Selected: " + ui.item.label :
            "Nothing selected, input was " + this.value);
      },
      open: function() {
        $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
      },
      close: function() {
        $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
      }
    });
  });
</script>
<body>
<div class="demo">

  <div class="ui-widget">
    <label for="city">Product: </label>
    <input id="city"/>
  </div>

  <div class="ui-widget" style="margin-top:2em; font-family:Arial">
    Result:
    <div id="log" style="height: 200px; width: 300px; overflow: auto;" class="ui-widget-content"></div>
  </div>
</div>
</body>

</html>
