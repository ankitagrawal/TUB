var CP__counter=0;
var CP__total= $(".cquery-ILL img").length;
var CP__sideBtnAutoHide;
var globalHT;
var globalWT

$.fn.cquery_ILL=function(arg){	
		
}

$.fn.cquery_ILL({sideBtnAutoHide:true, loader:false});



$.fn.cquery_customlightbox=function(obj, arg){
	
	
	
	if($(obj).attr('title')){	titleTxt= $(obj).attr('title');  }else{	titleTxt=''		}
	
	if($(obj).attr('contentID')){	cID= $(obj).attr('contentID');  }else{	cID=''		}
	
	if($(obj).attr('ajaxURL')){	URL= $(obj).attr('ajaxURL');  }else{	URL=''		}
	
	
	CP__showLightBox(arg.width, arg.height, titleTxt, cID, URL);
	
	globalHT=arg.height;
	globalWT=arg.width;	
};


function CP__showLightBox(WT, HT, titleTxt,  cID, URL){	
	
	
	documentHT=$(document).height();
	browserHT=$(window).height();
	
	$('#CP__base').show();
	$('#CP__centerBx').show();
	$('#CP__base').animate({'opacity':'0' },0);
	$('#CP__centerBx').animate({'opacity':'0' },0);	
	
	$('#CP__base').css("height",documentHT+'px');	
	$('#CP__base').animate({'opacity':'0.7' },500);
	$('#CP__centerBx').animate({'opacity':'1' },500);
	
	$('#CP__lightBox').animate({'width' : WT+"px", 'opacity':'1' },250, function (){ });
	$('#CP__contentBx').animate({'height' : HT+"px", 'opacity':'1' },250, function (){});	
		
	alwaysCenter= Math.ceil((browserHT-(Math.ceil(HT)+50))/2);
	
	if(alwaysCenter<0){alwaysCenter=0};
	$('#CP__centerBx').css({ marginTop:($(window).scrollTop()+alwaysCenter) + 'px'},10);	
	

	
	if(URL){	
		ajax(URL,'CP_content',false, 'resetLightbox()');
	}else{		
		$('#CP_content').html(	$('#'+cID)[0].innerHTML	)	;
		
		$('#CP_content').css({background:'none'});
	}
		
	$('#CP__altText').html(titleTxt);
		
}



function resetLightbox(){
	$('#CP__contentBx').css({background:'none'});
}

function CP__hideLightBox(){	
	$('#CP__base').animate({'opacity':'0' },500, function(){ CP__hideCompleteLightBox(); });
	$('#CP__centerBx').animate({'opacity':'0' },500, function(){  CP__hideCompleteLightBox(); });
}

function CP__hideCompleteLightBox(){
	$('#CP__base').hide();
	$('#CP__centerBx').hide();
}



function CP__createLightBx(){	
	lightBx="";		
	baseBG = document.createElement('span');
	baseBG.setAttribute('id','CIL_baseParent');
		
	lightBx+="<div id='CP__base' style='position:absolute; z-index:1000000; width:100%; display:none; background:#000; left:0px; top:0px' onClick='CP__hideLightBox()'></div>";
	
	lightBx +="<div id='CP__centerBx' style='position:absolute; z-index:1000001; height:0px; width:100%; display:none; left:0px; top:0px; text-align:center'>";	
	lightBx +=	"<div id='CP__lightBox' style='margin:auto; text-align:left; background:#fff; width:400px; position:relative'>";
	lightBx +=		"<div id='CP__contentBx' style='margin:auto; text-align:left; height:350px; overflow:hidden ;background:#fff url(graphics/cquery_img/loading.gif) 50% 50% no-repeat; '><div class='CP__pad' id='CP_content'></div></div>";
	
	lightBx +=	"</div>";
	lightBx +="</div>";	
	
	$("body").prepend(baseBG);	
	$('#CIL_baseParent').html(lightBx);	
		
	if(CP__total<=1){ $('#CP__leftArrow').hide(); $('#CP__rightArrow').hide(); }		
}

CP__createLightBx();


$(window).resize(function(){
	documentHT=$(document).height();
	browserHT=$(window).height();
			  
	alwaysCenter= Math.ceil((browserHT-(Math.ceil(globalHT)+50))/2);
	if(alwaysCenter<0){alwaysCenter=0};
	$('#CP__centerBx').css({ marginTop:($(window).scrollTop()+alwaysCenter) + 'px'});	
						 
});