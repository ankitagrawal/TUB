function switchTab(target) {
  if (target == '#all') {
    $('.tabbed').fadeIn();
    $('.tabs a').removeClass('active');
  }
  else {
    $('.tabbed').fadeOut();
    $(target).fadeIn();
  }
  $('.tabs a').removeClass('active').filter('a[href="' + target + '"]').addClass('active');
}

$(document).ready(function() {
  $('.tabs a').click(function() {
    var target_tab = $(this).attr('href');
    switchTab(target_tab);
    document.location.hash = target_tab + '_section';
    return false;
  });

  if (document.location.hash) {
    var target_tab = document.location.hash.replace("_section", "");

    //The valid tabs test is made only to ensure that the tab change happens only if the URL references any of the tabs.
    // Otherwise the full page will be shown. This also ensures normal behaviour exists for other hash links (such as payment methods)
    var validTabs = new Array();
    $('.tabs a').each(function(i) {
      validTabs[i] = $(this).attr('href');
    });
    for (i = 0; i < validTabs.length; i++) {
      if (target_tab == validTabs[i]) {
        switchTab(target_tab);
        return;
      }
      ;
    }
  }

});