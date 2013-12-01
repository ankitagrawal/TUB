var initSliderPaginator = function() {

    $(window).scroll(function() {
        var last_p = $('#pagerTrigger').offset().top;
        var doc_height = $(window).height();
        /* when reaching the element with id "last_para" we want to show the slidebox. Let's get the distance from the top to the element */
        var distanceTop = last_p - doc_height;
        if ($(window).scrollTop() > distanceTop) {
            /*var windowscroll = $(window).scrollTop();*/
            //alert ('Window Scroll Top height is ' + windowscroll);
            //alert ('last paragraph height from the top' + last_p);
            //alert ('Document Height from the top' + doc_height);
            $('#slidebox').animate({'right':'0px'}, 300);
        }
        else
            $('#slidebox').stop(true).animate({'right':'-430px'}, 100);
    });
    /* remove the slidebox when clicking the cross */
    $('#slidebox .close').bind('click', function() {
        $(this).parent().remove();
    });
}
