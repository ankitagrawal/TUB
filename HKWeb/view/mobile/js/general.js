function hasErr(data) {
    if (data.code == 'error') {
        return true;
    }
    return false;
}

function getErr(errors) {
    var errStr = errors;
    return errStr;
}

function deactivate(ele) {
    $(ele).addClass('deactive');
}

function activate(ele) {
    $(ele).removeClass('deactive');
}

var ngeneral;

function PopUpMob() {
    this.ele = 'popUpMobPar'; //global container
    this.shadow = 'popUpMobShadow'; //shadow element, child of ele and sibling of text
    this.text = 'popUpMobContent'; //child of ele and sibling of shadow
    this.close = 'popUpMobClose';
    this.textInternal = 'popUpMobInternal'; //text container, child of text
    this.msgBox = 'popUpMobMsg' //message for confirm box
}

PopUpMob.prototype.init = function () {
    var curPage = '#' + $.mobile.activePage.attr('id');
    var scrHei = $(window).height();
    $('#popUpMobPar').remove();
    $(curPage).find('div[data-role=content]').append('<div id=' + this.ele + '><div id=' + this.shadow + '></div><div id=' + this.text + '></div></div>');
    $('#' + this.ele).css({
        'width': '100%',
        'height': scrHei,
        'font-size': '14px',
        'position': 'fixed',
		'top': '0',
        'z-index': '10'
    });
    $('#' + this.shadow).css({
        'width': '100%',
        'height': '100%',
        'background-color': '#888',
        'opacity': '0.2',
        'filter': ' alpha(opacity=0.2)',
        'position': 'absolute',
        'top': '0',
        'left': '0'
    });
    $('#' + this.text).css({
        'width': '260px',
        'background': 'white',
        'min-height': '65px',
        'margin': '0px auto',
        'text-align': 'center',
        'position': 'relative',
        'top': '30%'
    });
}

PopUpMob.prototype.show = function (text) {
    this.init();
	 $('#' + this.text).html('<div style="background:url(images/navbar_bg.png);width:100%;padding:5px 0px;color:white;text-shadow:none;-webkit-text-shadow:none;font-size:14px;margin-bottom:5px">Alert</div>');
    $('#' + this.text).append(text);
    $('#' + this.text).append('<div style="margin-top:10px"><center><img src="images/ok.png" onclick="$(\'#' + this.ele + '\').hide()" alt="ok"/></center></div>');
    $('#' + this.ele).show();
}

PopUpMob.prototype.showWithTitle = function (title, text) {
    this.init();
    $('#' + this.text).html('<div style="background:url(images/navbar_bg.png);min-height:20px;width:100%;padding:5px 0px;color:white;text-shadow:none;-webkit-text-shadow:none;font-weight:bold;font-size:14px;">' + title + '</div>');
    $('#' + this.text).append(text);
    $('#' + this.text).append('<div style="margin-top:10px"><center><img src="images/ok.png" onclick="$(\'#' + this.ele + '\').hide()" alt="ok"/></center></div>');
    $('#' + this.ele).show();
}

PopUpMob.prototype.showWithConfirm = function (title, field, callBackOk, callBackCancel) {
    this.init();
    $('#' + this.text).html('<div style="background:url(images/navbar_bg.png);min-height:20px;width:100%;padding:5px 0px;color:white;text-shadow:none;-webkit-text-shadow:none;font-weight:bold;font-size:14px;">' + title + '</div>');
    $('#' + this.text).append("<div style='padding-top:10px' id='" + this.textInternal + "'></div>")
    $('#' + this.text + ' ' + '#' + this.textInternal).append(field);
    $('#' + this.text + ' ' + '#' + this.textInternal).append('<div style="margin-top:10px"><center><img src="images/ok.png" id="popUpMobOk" alt="ok"/>&nbsp;&nbsp&nbsp;<img src="images/cancel.png" id="popUpMobCancel" alt="Cancel"/></center></div>');
    $('#popUpMobOk').click(callBackOk);
    $('#popUpMobCancel').click(callBackCancel);
    $('#' + this.ele).show();
}

PopUpMob.prototype.message = function (msg, type) {
    if (type == 'add') {
        this.addMessage(msg);
        console.log(msg);
    } else this.removeMessage();
}

PopUpMob.prototype.addMessage = function (msg) {
    $('#' + this.msgBox).remove();
    var content = "<div style='background:url(images/navbar_bg.png);border:1px solid #BBB;padding:5px;color:white;text-shadow:none;-webkit-text-shadow:none;font-size:1em' id='" + this.msgBox + "'>";
    content = content + msg;
    content = content + '</div>';
    $('#' + this.text).append(content);
}

PopUpMob.prototype.removeMessage = function () {
    $('#' + this.msgBox).remove();
}

PopUpMob.prototype.hide = function () {

    $('#' + this.ele).hide();
}

var popUpMob = new PopUpMob();

function loadingPop(state, msg) //state is mandatory, msg is mandatory for state 's' only
{
    if (state == 'h') {
        $.mobile.loadingMessage = "Loading";
        $.mobile.hidePageLoadingMsg();
    } else if (state == 's') {
        $.mobile.loadingMessage = msg;
        $.mobile.showPageLoadingMsg();
    }
}

function URLEval() {

}

URLEval.prototype.getURLFromHash = function (hashURL) {
	//in some devices when the JS is fired document URL is with #, therefore URL after # is used
    var uH = hashURL.split('#');
    if (uH.length > 1) {
        return uH[1];
    } else if (uH.length == 0) return uH[0];
    else return uH;
}

function getURLParameterValue(queryString, parameter) {
    var qS = queryString;
    var pA;
    qS = qS.replace('?', '');
    qA = qS.split('&');
    for (i = 0; i < qA.length; i++) {
        if (qA[i].search(parameter) == 0) {
            return decodeURIComponent(qA[i].replace(parameter + "=", ''));
        }

    }
}

(function ($) {
    /* Plugin to scroll a content */
    $.fn.scrollWindow = function (options) {

        var defaults = $.extend({
            'scrollFactor': 18, //higher the value, slower will be scrolling
            'repeatAnimation': true
        }, options);
        this.css('left', 0);
        this.attr('data-scr-res', $(window).width()); // get screen width
        this.attr('data-ele-res', 0); // get default resolution of target element
        var eleRes = 0;
        this.find('td').each(function () {
            eleRes += $(this).width() + 4;
        });
        this.attr('data-ele-res', eleRes);
        this.attr('data-repeat', defaults.repeatAnimation);
        scrollAction(this);

        function scrollAction(ele) {
            if (Number($(ele).attr('data-ele-res')) > Number($(ele).attr('data-scr-res'))) {
                var difRes = eval($(ele).attr('data-ele-res') - $(ele).attr('data-scr-res'));
                var temp = difRes;
                
                if ($(ele).css('left') == '0px') {
                    ///alert('in');
                    $(ele).animate({
                        left: '-' + temp
                    }, temp * defaults.scrollFactor, 'linear', function () {
                        //$(this).css('left',0);

                        if ($(this).attr('data-repeat') == 'true') {
                            //alert('in');
                            scrollAction($(this));
                        }

                    });
                } else {
                    //alert('in');
                    $(ele).animate({
                        left: 0
                    }, temp * defaults.scrollFactor, 'linear', function () {
                        //$(this).css('left',0);

                        if ($(this).attr('data-repeat') == 'true') {
                            //alert('in');
                            scrollAction($(this));
                        }

                    });
                }

            }
        }

    }
})(jQuery);
var __hkG = {
	errs : {
		'requestFail' : 'Request Failed'
	},
	msgs : {
		'successRemove' : 'Successfully Removed',
		'successAdd' : 'Successfully Added',
		'specifyEmail' : 'Please specify Email'
	},
	urls : {
		'primaryCategory' : 'mShop/primaryCategory',
		'address' : 'mAddress/addresses',
		'removeFromCart' : 'mRemoveItem/removeItemfromCart',
		'viewCart' : 'mCart/viewCart',
		'orderSummary' : 'mOrderSummary/orderSummary',
		'payment' : 'mPayment/payment/',
		'search' : 'mSearch/search',
		'catalog' : 'mCatalog/catalog',
		'applyCoupon' : 'mCoupon/applyCoupon',
		'getOffers' : 'mCoupon/getOffers',
		'removeOffer' : 'mCoupon/removeOffer',
		'applyOffer' : 'mCoupon/applyOffer',
		'addToCart' : 'mAddtoCart/addtoCart',
		'subCategory' : 'mShop/secondaryCategory',
		'home' : '/home.jsp',
		'addressJ' : '/address.jsp',
		'orderSuccessJ' : '/orderSuccess.jsp'
	},
	timeOut : {
		'small' : 2000,
		'medium' : 5000,
		'large' : 12000
	}
};