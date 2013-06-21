var ALL_controllButtons=new Array(); 
var ALL_sideButtons=new Array();
var ALL_incr=new Array();
var ALL_currentSlide=new Array();
var ALL_totalFrame=	new Array();;
var ALL_timeSpeed=new Array();
var ALL_first=new Array();
var ALL_setTOut=new Array() ;
var ALL_checkPOS= new Array() ;
var ALL_width= new Array();
var ALL_pos= new Array();
var ALL_direction= new Array();
var ALL__autoplay=new Array();

$.fn.cquery_allslide=function(arg){
	
	ALL_width[arg.cssID]=arg.width
	ALL_sideButtons[arg.cssID]=arg.sideButtons;
	ALL_controllButtons[arg.cssID]=arg.controllButtons;
	ALL_direction[arg.cssID]=arg.direction;
	ALL__autoplay[arg.cssID]=arg.autoplay;		
	
	
	$(".cquery-ALL").each(function(i){	
								   
		if(i==arg.tabIn){
								   
			$(this).attr("id","cquery-ALL-"+arg.cssID);		
			ALL_incr[arg.cssID]=0;
			ALL_currentSlide[arg.cssID]=0;
			ALL_totalFrame[arg.cssID]=	$("#cquery-ALL-"+arg.cssID+" li").length;	
			ALL_timeSpeed[arg.cssID]=4000;
			ALL_first[arg.cssID]=0;
			ALL_setTOut[arg.cssID];	
			ALL_checkPOS[arg.cssID]=0;
			ALL_pos[arg.cssID]=true;
			
			if(ALL_direction[arg.cssID]=='top'){			ALL_direction[arg.cssID]='top';
			}else if(ALL_direction[arg.cssID]=='left'){	ALL_direction[arg.cssID]='left';
			}else if(ALL_direction[arg.cssID]=='fade'){	ALL_direction[arg.cssID]='fade';
			}else{								ALL_direction[arg.cssID]='left';}
		
			
			
			//____________________________________________________________________________     controlled round buttons here	
			listB='';
			cButtons = document.createElement('div');
			cButtons.setAttribute('class','controllButton-ALL-'+arg.cssID)
			for(j=0; j<ALL_totalFrame[arg.cssID]; j++){	
				listB+= "<a onClick='ALL_myPos("+arg.cssID+","+j+")'>&nbsp;</a> ";			
				$("#cquery-ALL-"+arg.cssID+" li")[j].setAttribute("id","all_li_"+arg.cssID+j);
			}		
			cButtons.innerHTML=listB;	
			$("#cquery-ALL-"+arg.cssID).append(cButtons);	
			cButtons.className="controllButton-ALL-"+arg.cssID;	
			
			if(ALL_controllButtons[arg.cssID]==false){	cButtons.style.display='none'; }else{}
			
			
			//____________________________________________________________________________     Next and previous buttons here
			button_left = document.createElement('span');
			button_right = document.createElement('span');	
			var cTxtLeft	= "<a href='javascript:void(0)' class='previous-ALL-"+arg.cssID+"' onClick='ALL_previous("+arg.cssID+")' >&nbsp;</a>";
			var cTxtRight	= "<a href='javascript:void(0)' class='next-ALL-"+arg.cssID+"' onClick='ALL_next("+arg.cssID+")' >&nbsp;</a>";	
			button_left.innerHTML=cTxtLeft;
			button_right.innerHTML=cTxtRight;		
			$("#cquery-ALL-"+arg.cssID).append(button_left);
			$("#cquery-ALL-"+arg.cssID).append(button_right);
			
			if(ALL_sideButtons[arg.cssID]==false){	button_left.style.display='none'; button_right.style.display='none'; }else{}
						
			
			ALL_main(1,1,1,arg.cssID);	
		}
  });
	
	
}




function ALL_main(prev,btn,nxt, tabIn){
	
if(ALL_pos[tabIn]==true){	ALL_pos[tabIn]=false;   direction = ALL_direction[tabIn];


	if(prev==1 && btn==1 && nxt==0){ 

	
							clearTimeout (ALL_setTOut[tabIn]);	
							
							if(ALL_currentSlide[tabIn]==ALL_totalFrame[tabIn]-1){ 		
								ALL_incr[tabIn]=0;
								
								cur_ID=tabIn+''+(ALL_currentSlide[tabIn]);
								next_ID=tabIn+''+(ALL_incr[tabIn]);
								
								if(direction=='left'){								
									$("#all_li_"+cur_ID).css({marginLeft:0+'px'});
									$("#all_li_"+next_ID).css({marginLeft:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginLeft' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginLeft' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else if(direction=='top'){
									$("#all_li_"+cur_ID).css({marginTop:0+'px'});
									$("#all_li_"+next_ID).css({marginTop:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginTop' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginTop' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});									
								}else if(direction=='fade'){									
									$("#all_li_"+cur_ID).fadeOut("slow", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).fadeIn("slow", function(){ ALL_ifPos(this,0,tabIn)	});
								}
								
								ALL_currentSlide[tabIn]=0;
								ALL_incr[tabIn]=1;																		
							}else{	
							
								cur_ID=tabIn+''+(ALL_currentSlide[tabIn]);									
								next_ID=tabIn+''+(ALL_incr[tabIn]);
								
								if(direction=='left'){	
									$("#all_li_"+cur_ID).css({marginLeft:0+'px'});
									$("#all_li_"+next_ID).css({marginLeft:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginLeft' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginLeft' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else if(direction=='top'){
									$("#all_li_"+cur_ID).css({marginTop:0+'px'});
									$("#all_li_"+next_ID).css({marginTop:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginTop' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginTop' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else if(direction=='fade'){
									$("#all_li_"+cur_ID).fadeOut("slow", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).fadeIn("slow", function(){ ALL_ifPos(this,0,tabIn)	});
								}
														
								ALL_currentSlide[tabIn]=ALL_incr[tabIn];
								ALL_incr[tabIn]++;
								
							}
							nxt=1;
							ALL_setTOut[tabIn] =setTimeout("ALL_main("+prev+","+btn+","+nxt+","+tabIn+")",ALL_timeSpeed[tabIn]);
		
	
	}else if(prev==0 && btn==1 && nxt==1){
		
							clearTimeout (ALL_setTOut[tabIn]);				
							if(ALL_currentSlide[tabIn]==0){ 	
								
								cur_ID=tabIn+''+(0);									
								next_ID=tabIn+''+(ALL_totalFrame[tabIn]-1);
								
								if(direction=='left'){	
									$("#all_li_"+cur_ID).css({marginLeft:0+'px'});
									$("#all_li_"+next_ID).css({marginLeft:"-"+ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginLeft' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginLeft' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else if(direction=='top'){
									$("#all_li_"+cur_ID).css({marginTop:0+'px'});
									$("#all_li_"+next_ID).css({marginTop:"-"+ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginTop' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginTop' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else{
									$("#all_li_"+cur_ID).fadeOut("slow", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).fadeIn("slow", function(){ ALL_ifPos(this,0,tabIn)	});			
								}
								
								ALL_currentSlide[tabIn]=ALL_totalFrame[tabIn]-1;
								ALL_incr[tabIn]=0;																								
							}else{								
								ALL_incr[tabIn]=ALL_currentSlide[tabIn]-1;								
								
								cur_ID=tabIn+''+(ALL_currentSlide[tabIn]);	
								next_ID=tabIn+''+(ALL_incr[tabIn]);
								
								if(direction=='left'){
									$("#all_li_"+cur_ID).css({marginLeft:0+'px'});
									$("#all_li_"+next_ID).css({marginLeft:"-"+ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginLeft' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginLeft' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else if(direction=='top'){
									$("#all_li_"+cur_ID).css({marginTop:0+'px'});
									$("#all_li_"+next_ID).css({marginTop:"-"+ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginTop' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginTop' : "+="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else{
									$("#all_li_"+cur_ID).fadeOut("slow", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).fadeIn("slow", function(){ ALL_ifPos(this,0,tabIn)	});
								}
																						
								ALL_currentSlide[tabIn]=ALL_incr[tabIn];
								ALL_incr[tabIn]++;
							}
							prev=1;
							ALL_setTOut[tabIn] =setTimeout("ALL_main("+prev+","+btn+","+nxt+","+tabIn+")",ALL_timeSpeed[tabIn]);		
	
	}else if(prev==1 && btn==0 && nxt==1){
		
							ALL_pos[tabIn]=true;
							if(ALL_checkPOS[tabIn]!=ALL_currentSlide[tabIn]){
							clearTimeout (ALL_setTOut[tabIn]);															
							
							cur_ID=tabIn+''+(ALL_currentSlide[tabIn]);
							next_ID=tabIn+''+(ALL_checkPOS[tabIn]);
							
							if(direction=='left'){
								$("#all_li_"+cur_ID).css({marginLeft:0+'px'});
								$("#all_li_"+next_ID).css({marginLeft:ALL_width[tabIn]+'px'});
								$("#all_li_"+next_ID).show();							
								$("#all_li_"+cur_ID).animate(	{'marginLeft' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,1,tabIn)	});
								$("#all_li_"+next_ID).animate({'marginLeft' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,0,tabIn)	});
							}else if(direction=='top'){
								$("#all_li_"+cur_ID).css({marginTop:0+'px'});
								$("#all_li_"+next_ID).css({marginTop:ALL_width[tabIn]+'px'});
								$("#all_li_"+next_ID).show();							
								$("#all_li_"+cur_ID).animate(	{'marginTop' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,1,tabIn)	});
								$("#all_li_"+next_ID).animate({'marginTop' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,0,tabIn)	});								
							}else{
									$("#all_li_"+cur_ID).fadeOut("slow", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).fadeIn("slow", function(){ ALL_ifPos(this,0,tabIn)	});
								}
							
							ALL_currentSlide[tabIn]=ALL_checkPOS[tabIn];		
							
							if(ALL_checkPOS[tabIn]==ALL_totalFrame[tabIn]-1){		ALL_incr[tabIn]=0	
							}else{	ALL_incr[tabIn] =ALL_checkPOS[tabIn]+1	};																														
							btn=1;
							ALL_setTOut[tabIn] =setTimeout("ALL_main("+prev+","+btn+","+nxt+","+tabIn+")",ALL_timeSpeed[tabIn]);		
							}		
	}else if(prev==1 && btn==1 && nxt==1){
		
				if(ALL_first[tabIn]==0){	
							ALL_first[tabIn]=1;	
							ALL_setTOut[tabIn] =setTimeout("ALL_main("+prev+","+btn+","+nxt+","+tabIn+")",ALL_timeSpeed[tabIn]);
				
							ALL_currentSlide[tabIn]=ALL_incr[tabIn];							
							in_ID=tabIn+''+(ALL_incr[tabIn]);							
							$("#all_li_"+in_ID).fadeIn("slow");
							ALL_pos[tabIn]=true;
							
							ALL_incr[tabIn]++;
				}else{				
							clearTimeout (ALL_setTOut[tabIn]);		
							if(ALL__autoplay[tabIn]==true){		
							if(ALL_currentSlide[tabIn]==ALL_totalFrame[tabIn]-1){ 		
								ALL_incr[tabIn]=0;							
								cur_ID=tabIn+''+(ALL_currentSlide[tabIn]);
								next_ID=tabIn+''+(ALL_incr[tabIn]);
								
								if(direction=='left'){
									$("#all_li_"+cur_ID).css({marginLeft:0+'px'});
									$("#all_li_"+next_ID).css({marginLeft:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginLeft' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginLeft' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,0,tabIn)	});
								}else if(direction=='top'){
									$("#all_li_"+cur_ID).css({marginTop:0+'px'});
									$("#all_li_"+next_ID).css({marginTop:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginTop' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginTop' : "-="+ALL_width[tabIn]+"px"},600,"easeInOutCirc", function(){ ALL_ifPos(this,0,tabIn)	});
								}else{
									$("#all_li_"+cur_ID).fadeOut("slow", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).fadeIn("slow", function(){ ALL_ifPos(this,0,tabIn)	});
								}
																
								ALL_currentSlide[tabIn]=0;
								ALL_incr[tabIn]=1;																		
							}else{	
										
								cur_ID=tabIn+''+(ALL_currentSlide[tabIn]);
								next_ID=tabIn+''+(ALL_incr[tabIn]);							
								
								if(direction=='left'){
									$("#all_li_"+cur_ID).css({marginLeft:0+'px'});
									$("#all_li_"+next_ID).css({marginLeft:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginLeft' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginLeft' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});
								}else if(direction=='top'){
									$("#all_li_"+cur_ID).css({marginTop:0+'px'});
									$("#all_li_"+next_ID).css({marginTop:ALL_width[tabIn]+'px'});
									$("#all_li_"+next_ID).show();								
									$("#all_li_"+cur_ID).animate(	{'marginTop' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).animate({'marginTop' : "-="+ALL_width[tabIn]+"px"},"jswing", function(){ ALL_ifPos(this,0,tabIn)	});									
								}else{
									$("#all_li_"+cur_ID).fadeOut("slow", function(){ ALL_ifPos(this,1,tabIn)	});
									$("#all_li_"+next_ID).fadeIn("slow", function(){ ALL_ifPos(this,0,tabIn)	});
								}
								
														
								ALL_currentSlide[tabIn]=ALL_incr[tabIn];
								ALL_incr[tabIn]++;
							}
							}else{ALL_pos[tabIn]=true;}									
							ALL_setTOut[tabIn] =setTimeout("ALL_main("+prev+","+btn+","+nxt+","+tabIn+")",ALL_timeSpeed[tabIn]);
				}				
	} 	
	
	ALL_round_btn_class(tabIn, ALL_currentSlide[tabIn])
	
}
}

function ALL_ifPos(id, val, tabIn){	
	ALL_pos[tabIn]=true;
	if(val==1){ $(id).hide();}
}
	


function ALL_previous(tabIn){  	ALL_main(0,1,1,tabIn); 	}
function ALL_next(tabIn){ 		ALL_main(1,1,0,tabIn);	}

function ALL_myPos(tabIn, arg){ 			ALL_checkPOS[tabIn]=arg; 	ALL_main(1,0,1,tabIn) 	}

function ALL_round_btn_class(tabIn, arg){

	for(i=0; i<ALL_totalFrame[tabIn]; i++){	
		if(arg==i){		
		//alert(arg);	
			$(".controllButton-ALL-"+tabIn+" a")[i].className="setActive";		
		}else{
		$(".controllButton-ALL-"+tabIn+" a")[i].className="";
		}
	}
}	