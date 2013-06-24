var tOffset=0;
var distancefromtop=5;
var bln=0;
var incr = 1;

window.onscroll=function(){
	var apName=navigator.userAgent;
	var searchApName=apName.indexOf("Chrome")
	if(searchApName>1){	var tScroll=document.body.scrollTop; }else{	var tScroll=document.documentElement.scrollTop;	}
	tOffset=tScroll;	
}

function fixT(){
if(incr==1){
	
openF('ttf',480,360);

}
}

function beforeCls(){

closeF('ttf',480,360)
}
	
function openF(ID,_x,_y){
if(bln==0){
	var OB=document.getElementById(ID);
	var OBW=OB.style.width.replace("px", "")*1
	var OBH=OB.style.height.replace("px", "")*1
	var OBL=OB.style.marginLeft.replace("px", "")*1
	easeW=Math.ceil(_x/8);
	easeH=Math.ceil(_y/8);
	OB.style.display='block';
	OB.style.width=(OBW+easeW)+"px";		
	OB.style.height=(OBH+easeH)+"px";
	OB.style.marginLeft=(OBL-easeW)+"px";	
	_x-=easeW;
	_y-=easeH;	
	
	if(_x>0 && _y>0){setTimeout("openF('"+ID+"',"+_x+","+_y+")",15); incr++; }else{bln=1; incr=1}
	}
}

function closeF(ID,_x,_y){
	bln=0;
	var OB=document.getElementById(ID);
	var OBW=OB.style.width.replace("px", "")*1
	var OBH=OB.style.height.replace("px", "")*1
	var OBL=OB.style.marginLeft.replace("px", "")*1
	easeW=Math.ceil(_x/8);
	easeH=Math.ceil(_y/8);
	OB.style.width=(OBW-easeW)+"px";		
	OB.style.height=(OBH-easeH)+"px";
	OB.style.marginLeft=(OBL+easeW)+"px";	
	_x-=easeW;
	_y-=easeH;	
	if(_x>0 && _y>0){setTimeout("closeF('"+ID+"',"+_x+","+_y+")",15);	}else{OB.style.display='none';}
}

function plus(){
	var OB=document.getElementById('rMenu');
	var OBH=OB.style.marginTop.replace("px", "")*1
	getD=tOffset-OBH;
	if(getD<0){getD=getD*-1; mns=0}else{mns=1};
	ease=Math.ceil(getD/8);
	var nOBH=OBH+distancefromtop;		
	if(mns==1){OB.style.marginTop=(nOBH+ease)+"px";	}else{OB.style.marginTop=(nOBH-ease)+"px";}
	setTimeout("plus()",20);	
}
	plus();