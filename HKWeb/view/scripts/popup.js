function toggle(div_id) {
	var el = document.getElementById(div_id);
	if ( el.style.display == 'none' ) {	el.style.display = 'block';}
	else {el.style.display = 'none';}
}

function blanket_size(popUpDivVar) {
	if (typeof window.innerHeight != 'undefined') {
		viewportheight = window.innerHeight;
	} else {
		viewportheight = document.documentElement.clientHeight;
	}
	if ((viewportheight > document.body.parentNode.scrollHeight) && (viewportheight > document.body.parentNode.clientHeight)) {
		blanket_height = viewportheight;
	} else {
		if (document.body.parentNode.clientHeight > document.body.parentNode.scrollHeight) {
			blanket_height = document.body.parentNode.clientHeight;
		} else {
			blanket_height = document.body.parentNode.scrollHeight;
		}
	}
	var blanket = document.getElementById('blanket');
	blanket.style.height = blanket_height + 'px';	
}

function window_pos(popUpDivVar) {
	if (typeof window.innerWidth != 'undefined') {
		viewportwidth = window.innerWidth;
	} else {
		viewportwidth = document.documentElement.clientWidth;
	}
	if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)) {
		window_width = viewportwidth;
	} else {
		if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
			window_width = document.body.parentNode.clientWidth;
		} else {
			window_width = document.body.parentNode.scrollWidth;
		}
	}
	var popUpDiv = document.getElementById(popUpDivVar);
	var popUpWidth = getStyle(popUpDivVar, 'width');
	popUpWidth = popUpWidth.replace('px', '');	
	var newPopUpWidth = 0 ;
	newPopUpWidth = popUpWidth;
	window_width=window_width/2-newPopUpWidth/2;
	popUpDiv.style.left = window_width + 'px';
	
	if (typeof window.innerHeight != 'undefined') {
		viewportheight = window.innerHeight;
	} else {
		viewportheight = document.documentElement.clientHeight;
	}
	
	var popUpHeight = getStyle(popUpDivVar, 'height');
	popUpHeight = popUpHeight.replace('px', '');	
	var newPopUpHeight = 0 ;
	newPopUpHeight = popUpHeight;
	
	popUpDiv_height=blanket_height - newPopUpHeight - (325 - newPopUpHeight/2);
	popUpDiv.style.top = popUpDiv_height + 'px'
}

function hidePopup(id) {
	blanket_size(id);
	window_pos(id);
	toggle('blanket');
	toggle(id);
}

function showPopup(id) {
	blanket_size(id);
	window_pos(id);
	toggle('blanket');
	toggle(id);
}

function getStyle(el,styleProp)
{
	var x = document.getElementById(el);
	if (x.currentStyle)
		var y = x.currentStyle[styleProp];
	else if (window.getComputedStyle)
		var y = document.defaultView.getComputedStyle(x,null).getPropertyValue(styleProp);
	return y;
}
