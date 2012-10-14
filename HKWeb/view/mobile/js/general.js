function hasErr(data) {

	if (data.code == 'error') {
		return true;
	}

	return false;
}
function getErr(errors) {
	var errStr = errors;
	/*for (i = 0; i < errors.errs.length; i++) {
		errStr = errStr + errors.errs[i].msg + '\n';
	}*/
	return errStr;
}
function deactivate(ele) {
	$(ele).addClass('deactive');
}
function activate(ele) {
	$(ele).removeClass('deactive');
}
var ngeneral ;
function generate(type, text) {
	n = noty({
		text : text,
		type : type,
		dismissQueue : true,
		layout : 'center',
		 theme: 'hkp'
	});
	//////console.log('html: ' + n.options.id);
}
function generatePopUpWithTimeout(type, text,interval) {
	var n = noty({
		text : text,
		type : type,
		dismissQueue : true,
		layout : 'center',
		timeout:interval,
		theme: 'hkp'

	});
	////console.log('html: ' + n.options.id);
}
function generateWiderPopUpWithTimeout(type, text,interval) {
if(typeof(ngeneral)!='undefined')
   {
      ngeneral.close();
   }
	ngeneral = noty({
		text : text,
		type : type,
		dismissQueue : true,
		layout : 'centerWider',
		timeout:interval,
		theme: 'hkp'

	});
	
	////console.log('html: ' + n.options.id);
}
function generateButtonAction(type, text,btnCancel,btnStyle,callback) {
    
	var n = noty({
		text : text,
		type : type,
		dismissQueue : true,
		layout : 'center',
		theme: 'hkp',
		buttons: [ {addClass: "btn buTon ui-corner-all", text: btnCancel, onClick: function($noty) {
		             callback($noty);
                   }}
				 
                 
               ]

	});
	}
function generateWiderButtonAction(type, text,btnCancel,btnStyle,callback) {
    
	var n = noty({
		text : text,
		type : type,
		dismissQueue : true,
		layout : 'centerWider',
		theme: 'hkp',
		animation: {
			open: {height: 'toggle'},
			close: {height: 'toggle'},
			
			speed: 1 // opening & closing animation speed
		},
		buttons: [ {addClass: "btn buTon ui-corner-all", text: btnCancel, onClick: function($noty) {
		             callback($noty);
                   }}
				 
                 
               ]

	});
	}
	function generateButtonActionWithTime(type, text,btnCancel,btnStyle,callback,interval) {
    
	var n = noty({
		text : text,
		type : type,
		dismissQueue : true,
		layout : 'center',
		theme: 'hkp',
		timeout:interval,
		buttons: [ {addClass: "btn buTon ui-corner-all", text: btnCancel, onClick: function($noty) {
		             callback($noty);
                   }}
				 
                 
               ]

	});
	}
function generateButtonWithCloseAction(type, text,btnOk,btnCancel,btnStyle,callback) {
     
	var n = noty({
		text : text,
		type : type,
		dismissQueue : false,
		layout : 'center',
		theme: 'hkp',
		timeout: false,
		animation: {
			open: {height: 'toggle'},
			close: {height: 'toggle'},
			
			speed: 500 // opening & closing animation speed
		},
		buttons: [ {addClass: btnStyle, text: btnOk, onClick: function($noty) {
		             callback($noty);
                   }},
				   {addClass: btnStyle, text: btnCancel, onClick: function($noty) {
		          $.noty.closeAll();
					     
                   }
				   }
				   
                 
               ]

	});
	////console.log('html: ' + n.options.id);
}
function generateWiderPopUp(type, text,btnOk,btnCancel,btnStyle,callback) {
     
	var n = noty({
		text : text,
		type : type,
		dismissQueue : false,
		layout : 'centerWider',
		theme: 'hkp',
		timeout: false,
		animation: {
			open: {height: 'toggle'},
			close: {height: 'toggle'},
			
			speed: 500 // opening & closing animation speed
		},
		buttons: [ {addClass: btnStyle, text: btnOk, onClick: function($noty) {
		             callback($noty);
                   }},
				   {addClass: btnStyle, text: btnCancel, onClick: function($noty) {
		          $.noty.closeAll();
					     
                   }
				   }
				   
                 
               ]

	});
	////console.log('html: ' + n.options.id);
}
function generate3ButtonWithCloseAction(type, text,btnOk,btnSec,btnCancel,btnStyle,callback1,callback2) {
     
	var n = noty({
		text : text,
		type : type,
		dismissQueue : false,
		layout : 'center',
		theme: 'hkp',
		timeout: false,
		animation: {
			open: {height: 'toggle'},
			close: {height: 'toggle'},
			
			speed: 500 // opening & closing animation speed
		},
		buttons: [ {addClass: btnStyle, text: btnOk, onClick: function($noty) {
		             callback1($noty);
                   }},
				   {addClass: btnStyle, text: btnSec, onClick: function($noty) {
		          callback2($noty);
					     
                   }
				   },
				   {addClass: btnStyle, text: btnCancel, onClick: function($noty) {
		          $.noty.closeAll();
					     
                   }
				   }
				   
                 
               ]

	});
	////console.log('html: ' + n.options.id);
}
function generateInfo(titleTxt,bodyTxt){
   generateWiderButtonAction('confirm',"<h2 style='background:url(/view/images/black_bg_hk.jpg);color:white;position:relative;top:-8px;left:-10px;line-height:25px;display:block;width:370px; font-size:14px;font-weight:bold' >"+titleTxt+"</h2><p style='font-weight:100; font-size:13px;'>"+bodyTxt+"</p>","OK","btn",function($noty){$noty.close();});
}
function generateWithIcon(icon,text){
	generateButtonAction('confirm',"<h2 class=border-b style='text-align:left'><img src='/view/images/"+icon+"' align=left style='position:relative;top:-8px' /><center>Help</center></h2><p style='font-weight:100; font-size:13px;'>"+text+"</p>","OK","btn",function($noty){$noty.close();});

}
function PopUpMob()
	{
		this.ele = 'popUpMobPar';
		this.shadow = 'popUpMobShadow';
		this.text = 'popUpMobContent';
		this.close = 'popUpMobClose';
	}
	PopUpMob.prototype.init = function(){
		var curPage = '#'+$.mobile.activePage.attr('id');
		var scrHei = $(curPage).height();
		$(curPage).remove('#popUpMobPar');
		$(curPage).find('div[data-role=content]').append('<div id='+this.ele+'><div id='+this.shadow+'></div><div id='+this.text+'></div></div>');
		$('#'+this.ele).css({'width':'100%','height':scrHei,'font-size':'14px','position':'fixed','top':'0','z-index':'10'});
		$('#'+this.shadow).css({'width':'100%','height':'100%','background-color':'#888','opacity':'0.2','filter':' alpha(opacity=0.2)','position':'absolute','top':'0','left':'0'});
		$('#'+this.text).css({'width':'260px','background':'white','min-height':'50px','margin':'0px auto','text-align':'center','position': 'relative','top': '30%'});
	}
PopUpMob.prototype.show = function(text){
	this.init();
	$('#'+this.text).html(text);
	$('#'+this.text).append('<div style="margin-top:10px"><center><img src="images/ok.png" onclick="$(\'#'+this.ele+'\').hide()" alt="ok"/></center></div>');
	$('#'+this.ele).show();
}
PopUpMob.prototype.showWithTitle = function(title,text){
	this.init();
	$('#'+this.text).html('<div style="background:url(images/navbar_bg.png);min-height:20px;width:100%;padding:5px 0px;color:white;text-shadow:none;-webkit-text-shadow:none;font-weight:bold;font-size:14px;">'+title+'</div>');
	$('#'+this.text).append(text);
	$('#'+this.text).append('<div style="margin-top:10px"><center><img src="images/ok.png" onclick="$(\'#'+this.ele+'\').hide()" alt="ok"/></center></div>');
	$('#'+this.ele).show();
}
PopUpMob.prototype.hide = function(){
	
	$(this.ele).hide();
}
var popUpMob = new PopUpMob();
function loadingPop(state,msg)//state is mandatory, msg is mandatory for state 's' only
	{
		if(state=='h')
			{ 
			$.mobile.loadingMessage="Loading";
			//$.mobile.showPageLoadingMsg();
			$.mobile.hidePageLoadingMsg();
				
				
			}
		else if(state=='s')
			{
			
				$.mobile.loadingMessage=msg;
				$.mobile.showPageLoadingMsg();
			}
	}
	function URLEval(){
		
	}
	URLEval.prototype.getURLFromHash = function(hashURL){
		var uH = hashURL.split('#');
		//console.log(uH);
		if(uH.length > 1)
		{
			return uH[1];
		}
		else if(uH.length==0)
		return uH[0];
		else
		return uH;
	}
	function getURLParameterValue(queryString,parameter){
		var qS = queryString;
		var pA ;
		qS = qS.replace('?','');
		qA = qS.split('&');
		for(i=0;i<qA.length;i++)
		{
			if(qA[i].search(parameter)==0)
			{
				return decodeURIComponent(qA[i].replace(parameter+"=",''));
			}
			
		}
	}
(function($){
/* Plugin to scroll a content */
			$.fn.scrollWindow = function(options){
			
			var defaults = $.extend( {
					'scrollFactor' : 18, 	//higher the value, slower will be scrolling
					'repeatAnimation': true
				}, options);
				this.css('left',0);
				this.attr('data-scr-res',$(window).width());// get screen width
				this.attr('data-ele-res',0); // get default resolution of target element
				var eleRes=0;
				this.find('td').each(function(){
					eleRes += $(this).width()+4;
				});
				this.attr('data-ele-res',eleRes);
				this.attr('data-repeat',defaults.repeatAnimation);
				scrollAction(this);
				function scrollAction(ele)
				{
				if(Number($(ele).attr('data-ele-res')) > Number($(ele).attr('data-scr-res')))
					{
					//alert('in');
						var difRes = eval($(ele).attr('data-ele-res') -$(ele).attr('data-scr-res'));				
						//this.width(scrRes); // set target element width = width of screen
						var temp = difRes;
						if($(ele).css('left')=='0px')
						{
							///alert('in');
							$(ele).animate({left:'-'+temp},temp*defaults.scrollFactor,'linear',function(){
							//$(this).css('left',0);
							
							if($(this).attr('data-repeat')=='true')
							{
								//alert('in');
								scrollAction($(this));
							}
							
							});
						}
						else
						{
						//alert('in');
							$(ele).animate({left:0},temp*defaults.scrollFactor,'linear',function(){
							//$(this).css('left',0);
							
							if($(this).attr('data-repeat')=='true')
							{
								//alert('in');
								scrollAction($(this));
							}
							
							});
						}
				
					}
				}
				
			}
		})(jQuery);