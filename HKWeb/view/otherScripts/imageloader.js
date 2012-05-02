$(document).ready(function(){
	
	var isie6 = false;
	
	jQuery.each(jQuery.browser, function(i, val) {
		if((i=="msie") && (jQuery.browser.version=='6.0')) {
			isie6 = true;
		}
	});
	
	$('#category-menu').hover(function(){
		$(this).children('.list').css({display:'block'});
	},function(){
		$(this).children('.list').css({display:'none'});
	});
	
	$('#category-menu a').focus(function(){
		$(this).parents('#category-menu').children('.list').css({display:'block'});
	});
	
	$(window).load(function(){
		$('a.image img, a.featured-image img').each(function(){
			outerHeight = $(this).parent().height();
			innerHeight = $(this).height();
			difference = outerHeight-innerHeight;
			newMargin = Math.floor(difference/2);
			if(difference>8) {
				if(isie6==true) { newMargin = newMargin-3; }
				$(this).css({marginTop:newMargin+'px'});
			}
		});
	});
	
	var flashVars = {
		cfg_xmlURL: '/_inc/flash/test.xml?d=20080303',
		cfg_fadeDelay: 7,
		cfg_fadeSpeed: 3,
		cfg_text: 'Bringing you your home food.'
	}
	swfobject.embedSWF("/_inc/flash/header.swf", "flash", "730", "110", "8.0.0", "/_inc/flash/expressInstall.swf", flashVars, {menu:false,wmode:'transparent'});
	
    Shadowbox.init({
        resizeLgImages: true,
        handleUnsupported: 'remove',
        keysClose: ['c', 27],
        autoplayMovies: false
    });

	
});

function posImages() {
	$('a.image img, a.featured-image img').each(function(){
		outerHeight = $(this).parent().height();
		innerHeight = $(this).height();
		difference = outerHeight-innerHeight;
		newMargin = Math.floor(difference/2);
		if(difference>8) {
			if(isie6==true) { newMargin = newMargin-3; }
			$(this).css({marginTop:newMargin+'px'});
		}
	});
}