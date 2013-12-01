<%@include file="/includes/_taglibInclude.jsp" %>
<script language="JavaScript" type="text/javascript">
  // for date inputs, here's what we're doing
  // 1. adding an image for the date selector icon
  // 2. initializing dynDate plugin and setting the onUpdate even of dynDate:
  //      explicitly removing the input_tip class and calling labelify again
  //      this is being done so that when the user selects a date, it does not stay gray (labelify does not update onChange, and I couldn't get it to work either)
  // 3. setting title attribute to dd/mm/yyyy for labelify
  // 4. finally  call labelify
  $(document).ready(function() {
    $('.date_input').each(function() {
      var fieldName = $(this).attr('name');
      if ($(this).hasClass("startDate")) {
        $(this).after('<a href="#"><img src="<hk:vhostImage/>/images/act_cal.gif"/></a>').
            dynDateTime({
          showsTime: false,
          ifFormat: "%Y-%m-%d 00:00:00",
          button: '.next()',
          onUpdate: function() {$(':text[name=' + fieldName + ']').removeClass('input_tip').labelify({ labelledClass:'input_tip'});}
        }).
            attr('title', 'yyyy-mm-dd hh:mm:ss').
            labelify({ labelledClass:'input_tip'});
      } else if ($(this).hasClass("endDate")) {
        $(this).after('<a href="#"><img src="<hk:vhostImage/>/images/act_cal.gif"/></a>').
            dynDateTime({
          showsTime: false,
          ifFormat: "%Y-%m-%d 23:59:59",
          button: '.next()',
          onUpdate: function() {$(':text[name=' + fieldName + ']').removeClass('input_tip').labelify({ labelledClass:'input_tip'});}
        }).
            attr('title', 'yyyy-mm-dd hh:mm:ss').
            labelify({ labelledClass:'input_tip'});
      } else {
        $(this).after('<a href="#"><img src="<hk:vhostImage/>/images/act_cal.gif"/></a>').
            dynDateTime({
          showsTime: false,
          ifFormat: "%Y-%m-%d",
          button: '.next()',
          onUpdate: function() {$(':text[name=' + fieldName + ']').removeClass('input_tip').labelify({ labelledClass:'input_tip'});}
        }).
            attr('title', 'yyyy-mm-dd').
            labelify({ labelledClass:'input_tip'});
      }
    });
  });
</script>